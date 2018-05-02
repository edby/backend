/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.controller;

import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.DateConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.*;
import com.blocain.bitms.trade.account.consts.AccountConsts;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.entity.AccountCertification;
import com.blocain.bitms.trade.account.model.PolicyModel;
import com.blocain.bitms.trade.account.service.AccountCertificationService;
import com.blocain.bitms.trade.account.service.AccountPolicyService;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.*;
import com.blocain.bitms.trade.fund.enums.FundEnums;
import com.blocain.bitms.trade.fund.model.FundModel;
import com.blocain.bitms.trade.fund.service.*;
import com.blocain.bitms.trade.quotation.QuotationController;
import com.blocain.bitms.trade.risk.model.EnableModel;
import com.blocain.bitms.trade.risk.service.EnableService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.entity.StockRate;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.stockinfo.service.StockRateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *  Fund提币出金  控制器
 * <p>File：WithdrawCashController.java</p>
 * <p>Title: WithdrawCashController</p>
 * <p>Description:WithdrawCashController</p>
 * <p>Copyright: Copyright (c) 2017年7月14日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
@Api(description = "Fund提币出金")
public class WithdrawCashController extends QuotationController
{
    public static final Logger          logger = LoggerFactory.getLogger(WithdrawCashController.class);
    
    @Autowired(required = false)
    private AccountCashWithdrawService  accountCashWithdrawService;
    
    @Autowired(required = false)
    private StockRateService            stockRateService;
    
    @Autowired(required = false)
    private EnableService               enableService;
    
    @Autowired(required = false)
    private AccountService              accountService;
    
    @Autowired(required = false)
    private FundCurrentService          fundCurrentService;
    
    @Autowired(required = false)
    private AccountPolicyService        accountPolicyService;
    
    @Autowired(required = false)
    private StockInfoService            stockInfoService;
    
    @Autowired(required = false)
    private AccountCertificationService accountCertificationService;
    
    @Autowired(required = false)
    private AccountCollectBankService   accountCollectBankService;
    
    /**
     * Fund提币出金页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/withdrawCash", method = RequestMethod.GET)
    @ApiOperation(value = "Fund提币出金页面导航", httpMethod = "GET")
    public ModelAndView withdrawCash(String symbol) throws BusinessException
    {
        if (null == symbol || "".equals(symbol)) {
            symbol = "eur";
        }
        ModelAndView mav = new ModelAndView("fund/withdrawCash");
        StockInfo stockInfo = new StockInfo();
        stockInfo.setRemark(symbol);
        stockInfo.setStockType(FundConsts.STOCKTYPE_CASHCOIN);
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
        if (stockInfoList.size() == 0) { throw new BusinessException(" stockinfo error"); }
        StockInfo info = stockInfoList.get(0);
        if (!StringUtils.equalsIgnoreCase(info.getStockType(), FundConsts.STOCKTYPE_CASHCOIN)) { throw new BusinessException(" stockinfo error:" + info.getStockType()); }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        AccountCertification tempCertification = accountCertificationService.findByAccountId(principal.getId());
        boolean certStatus = false;
        if (null != tempCertification)
        {
            if (tempCertification.getStatus().intValue() == 1)
            {
                certStatus = true;
            }
            mav.addObject("realName", tempCertification.getRealname() + " " + tempCertification.getSurname());
        }
        else
        {
            mav.addObject("realName", "");
        }
        mav.addObject("email", principal.getUserMail());
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(principal.getId());
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        enableModel.setStockinfoId(info.getId());// btc
        enableModel.setRelatedStockinfoId(info.getId());
        enableModel = enableService.entrustTerminalEnable(enableModel);
        if (enableModel != null)
        {
            if (enableModel.getEnableAmountEx() == null)
            {
                enableModel.setEnableAmountEx(BigDecimal.ZERO);
            }
        }
        StockRate stockRate = new StockRate();
        stockRate.setStockinfoId(info.getId());
        stockRate.setRateType(FundConsts.RATE_TYPE_WITHDRAW_FEE);
        List<StockRate> feerateList = stockRateService.findList(stockRate);
        BigDecimal feeRate = BigDecimal.ZERO;
        if (feerateList.size() > 0)
        {
            StockRate rateEntity = feerateList.get(0);
            if (rateEntity.getRate() != null)
            {
                feeRate = rateEntity.getRate();
            }
            else
            {
                throw new BusinessException("feeRate error:record null");
            }
        }
        else
        {
            throw new BusinessException("feeRate error:no record");
        }
        AccountCollectBank accountCollectBank = new AccountCollectBank();
        accountCollectBank.setAccountId(principal.getId());
        accountCollectBank.setStockinfoId(info.getId());
        List<AccountCollectBank> banklist = accountCollectBankService.findList(accountCollectBank);
        if (banklist.size() > 0)
        {
            AccountCollectBank bank = banklist.get(0);
            bank.setCardNo(EncryptUtils.desDecrypt(bank.getCardNo()));
            mav.addObject("bank", bank);
        }
        else
        {
            mav.addObject("bank", null);
        }
        AccountCashWithdraw accountWithdrawRecord = new AccountCashWithdraw();
        accountWithdrawRecord.setAccountId(principal.getId());
        accountWithdrawRecord.setStockinfoId(info.getId());
        String dateStr = CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMD);
        accountWithdrawRecord.setTimeStart(dateStr + " 00:00:00");
        accountWithdrawRecord.setTimeEnd(dateStr + " 23:59:59");
        logger.debug("AccountWithdrawRecordERC20:" + accountWithdrawRecord.toString());
        java.math.BigDecimal usedAmt = accountCashWithdrawService.findSumAmtByAccount(accountWithdrawRecord);
        logger.debug("已使用提款额度："+usedAmt);
        BigDecimal canUsing = BigDecimal.ZERO;
        if(info.getAuthedUserWithdrawDayUpper().subtract(usedAmt).compareTo(BigDecimal.ZERO)>0)
        {
            canUsing = info.getAuthedUserWithdrawDayUpper().subtract(usedAmt);
        }
        mav.addObject("enableAmount", enableModel.getEnableAmount());
        mav.addObject("account", accountService.selectByPrimaryKey(OnLineUserUtils.getId()));
        mav.addObject("certStatus", certStatus);
        mav.addObject("coin", info.getStockCode());
        mav.addObject("feeRate", feeRate);
        mav.addObject("stockinfoId", info.getId());
        mav.addObject("quota", info.getAuthedUserWithdrawDayUpper());
        mav.addObject("canUsing", canUsing);
        return mav;
    }
    
    /**
     * Fund提币出金历史列表
     * @param accountCashWithdraw
     * @param pagin
     * @return
     * @throws BusinessException
     * @author sunbiao  2017年7月19日 上午11:08:10
     */
    @ResponseBody
    @RequestMapping(value = "/withdrawCash/withdrawCashList", method = RequestMethod.POST)
    @ApiOperation(value = "Fund提币出金历史列表", httpMethod = "POST")
    public JsonMessage withdrawCashList(@ModelAttribute AccountCashWithdraw accountCashWithdraw, @ModelAttribute Pagination pagin) throws BusinessException
    {
        if (null == accountCashWithdraw.getStockinfoId()) { throw new BusinessException(FundEnums.ERROR_STOCKINFOID_NOT_EXIST); }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        accountCashWithdraw.setAccountId(principal.getId());
        accountCashWithdraw.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);// 钱包账户资产
        accountCashWithdraw.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);// 钱包账户提现
        accountCashWithdraw.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);// 过滤 资产减少
        logger.debug("withdrawList accountWithdrawRecord:" + accountCashWithdraw.toString());
        // accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getStockinfoId()).getTableFundCurrent());
        PaginateResult<AccountCashWithdraw> result = accountCashWithdrawService.search(pagin, accountCashWithdraw);
        for (AccountCashWithdraw curr : result.getList())
        {
            if (curr.getWithdrawCardNo() != null && !curr.getWithdrawCardNo().equals(""))
            {
                curr.setWithdrawCardNo(EncryptUtils.desDecrypt(curr.getWithdrawCardNo()));// des解密
            }
        }
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * Fund提币出金-申请
     * @param accountCashWithdraw
     * @return
     * @throws BusinessException
     * @author sunbiao  2017年7月13日 上午11:33:27
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/withdrawCash/withdrawCash", method = RequestMethod.POST)
    @ApiOperation(value = "Fund提币出金申请", httpMethod = "POST")
    public JsonMessage withdrawCash(AccountCashWithdraw accountCashWithdraw, String fundPwd, @ModelAttribute PolicyModel policy)
            throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        logger.debug("/withdrawCash/withdrawCash page form = " + accountCashWithdraw.toString());
        FundModel fundModel = new FundModel();
        StockRate stockRate = new StockRate();
        stockRate.setStockinfoId(accountCashWithdraw.getStockinfoId());
        stockRate.setRateType(FundConsts.RATE_TYPE_WITHDRAW_FEE);
        List<StockRate> feeRateList = stockRateService.findList(stockRate);
        BigDecimal feeRate = BigDecimal.ZERO;
        if (feeRateList.size() > 0)
        {
            StockRate rateEntity = feeRateList.get(0);
            if (rateEntity.getRate() != null)
            {
                feeRate = rateEntity.getRate();
            }
            else
            {
                throw new BusinessException("feeRate error:record null");
            }
        }
        else
        {
            throw new BusinessException("feeRate error:no record");
        }
        fundModel.setFee(feeRate.multiply(accountCashWithdraw.getOccurAmt()));
        accountCashWithdraw.setFee(feeRate.multiply(accountCashWithdraw.getOccurAmt()));
        if (StringUtils.isBlank(fundPwd)) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        if (accountCashWithdraw.getOccurAmt().compareTo(BigDecimal.valueOf(100)) < 0
                || accountCashWithdraw.getOccurAmt().compareTo(BigDecimal.valueOf(100000)) > 0) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        // 小数位数判断
        fundModel.setAmount(accountCashWithdraw.getOccurAmt());
        NumericUtils.checkDecimalDigits("amount", fundModel.getAmount(), 2);
        // ---------用户数据验证 资金验证 安全策略验证 资金密码验证 start----------
        Account account = accountService.selectByPrimaryKey(principal.getId());
        checkAccountDataValidate(account);
        // 资金密码
        if (com.blocain.bitms.tools.utils.StringUtils.isBlank(account.getWalletPwd())) { throw new BusinessException(CommonEnums.ERROR_WALLET_VALID_NOEXIST, null); }
        boolean wallertpwdvalidate = EncryptUtils.validatePassword(String.valueOf(fundPwd), account.getWalletPwd());
        if (!wallertpwdvalidate) { throw new BusinessException(CommonEnums.ERROR_WALLET_VALID_FAILED, null); }
        // ---------用户数据验证 资金验证 安全策略验证 资金密码验证 end----------
        fundModel.setStockinfoId(accountCashWithdraw.getStockinfoId());
        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        accountCashWithdraw.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        fundModel.setAccountId(principal.getId());
        accountCashWithdraw.setAccountId(principal.getId());
        AccountCollectBank accountCollectBank = new AccountCollectBank();
        accountCollectBank.setAccountId(principal.getId());
        accountCollectBank.setStockinfoId(accountCashWithdraw.getId());
        List<AccountCollectBank> banklist = accountCollectBankService.findList(accountCollectBank);
        if (banklist.size() > 0)
        {
            checkCollectAddrDataValidate(banklist.get(0));
            fundModel.setAddress(banklist.get(0).getCardNo());
            accountCashWithdraw.setWithdrawCardNo(banklist.get(0).getCardNo());
            accountCashWithdraw.setWithdrawBankName(banklist.get(0).getBankName());
            accountCashWithdraw.setSwiftBic(banklist.get(0).getSwiftBic());
        }
        else
        {
            throw new BusinessException(CommonEnums.ERROR_INVALID_ADDRESS);
        }
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(principal.getId());
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        enableModel.setStockinfoId(accountCashWithdraw.getStockinfoId());
        enableModel.setRelatedStockinfoId(accountCashWithdraw.getStockinfoId());
        enableModel = enableService.entrustTerminalEnable(enableModel);
        if (enableModel.getEnableAmount()
                .compareTo(fundModel.getAmount().add(fundModel.getFee())) < 0) { throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE); }

        account.setSecurityPolicy(AccountConsts.SECURITY_POLICY_NEEDGA);// GA
        accountPolicyService.validSecurityPolicy(account, policy);
        fundCurrentService.doApplyCashWithdraw(fundModel, accountCashWithdraw);

        // 刷新缓存
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        List<StockInfo> list = stockInfoService.findList(stockInfo);
        for (StockInfo stockInfo1 : list)
        {
            // 刷新缓存
            setAccountAssetCache(principal.getId(), FundConsts.WALLET_BTC_TYPE, stockInfo1.getId());
        }

        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * Fund取消提币
     * @param id
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/withdrawCash/withdrawCashCancel", method = RequestMethod.POST)
    @ApiOperation(value = "Fund取消提币", httpMethod = "POST")
    public JsonMessage withdrawCashCancel(Long id,Long stockinfoId) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        Account account = accountService.selectByPrimaryKey(principal.getId());
        checkAccountDataValidate(account);
        fundCurrentService.doWithdrawCancel(id, principal.getId(), stockinfoId);
        BigDecimal amount = BigDecimal.ZERO;
        AccountCashWithdraw accountCashWithdraw = accountCashWithdrawService.selectByPrimaryKey(id);
        if(accountCashWithdraw!=null)
        {
            amount=accountCashWithdraw.getOccurAmt();
        }
        // 刷新缓存
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        List<StockInfo> list = stockInfoService.findList(stockInfo);
        for (StockInfo stockInfo1 : list)
        {
            // 刷新缓存
            setAccountAssetCache(principal.getId(), FundConsts.WALLET_BTC_TYPE, stockInfo1.getId());
        }

        return getJsonMessage(CommonEnums.SUCCESS, amount);
    }
    
    /**
     * 新增提币收款地址
     * @param accountCollectBank
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/withdrawCash/withdrawCashAddrAdd", method = RequestMethod.POST)
    @ApiOperation(value = "新增提币收款地址", httpMethod = "POST")
    public JsonMessage withdrawCashAddrAdd(HttpServletRequest request, @ModelAttribute AccountCollectBank accountCollectBank, String fundPwd,
            @ModelAttribute PolicyModel policy) throws BusinessException
    {
        if (null == accountCollectBank.getStockinfoId())
        {//
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        if(accountCollectBank.getStockinfoId().longValue()==FundConsts.WALLET_EUR_TYPE.longValue())
        {
            accountCollectBank.setBankName("Eurobank");
        }
        if (StringUtils.isEmpty(accountCollectBank.getBankName()) || StringUtils.isEmpty(accountCollectBank.getCardNo())
                || StringUtils.isEmpty(accountCollectBank.getSwiftBic()) || StringUtils.isEmpty(fundPwd) || StringUtils.isEmpty(policy.getGa())
                || StringUtils.isEmpty(policy.getSms())) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        // ---------用户数据验证 资金验证 安全策略验证 资金密码验证 start----------
        Account account = accountService.selectByPrimaryKey(principal.getId());
        checkAccountDataValidate(account);
        // 资金密码
        if (com.blocain.bitms.tools.utils.StringUtils.isBlank(account.getWalletPwd())) { throw new BusinessException(CommonEnums.ERROR_WALLET_VALID_NOEXIST, null); }
        boolean wallertpwdvalidate = EncryptUtils.validatePassword(String.valueOf(fundPwd), account.getWalletPwd());
        if (!wallertpwdvalidate) { throw new BusinessException(CommonEnums.ERROR_WALLET_VALID_FAILED, null); }
        account.setSecurityPolicy(4);// GA+短信
        accountPolicyService.validSecurityPolicy(account, policy);
        // ---------用户数据验证 资金验证 安全策略验证 资金密码验证 end----------
        // AccountCollectAddr entity = new AccountCollectAddr();
        accountCollectBank.setAccountId(principal.getId());
        accountCollectBank.setCreateBy(principal.getId());
        // 判断输入的卡号是否已经存在 并且不是自己
        AccountCollectBank accountCollectBankSearch = new AccountCollectBank();
        accountCollectBankSearch.setCardNo(EncryptUtils.desEncrypt(accountCollectBank.getCardNo()));
        accountCollectBankSearch.setStockinfoId(accountCollectBank.getStockinfoId());
        List<AccountCollectBank> banklist = accountCollectBankService.findList(accountCollectBankSearch);
        if (banklist.size() > 0)
        {
            AccountCollectBank bank = banklist.get(0);
            if (principal.getId().longValue() != bank.getAccountId().longValue())
            {
                throw new BusinessException("The current card number has already existed");
            }
        }
        accountCollectBankSearch = new AccountCollectBank();
        accountCollectBankSearch.setAccountId(principal.getId());
        accountCollectBankSearch.setStockinfoId(accountCollectBank.getStockinfoId());
        banklist = accountCollectBankService.findList(accountCollectBankSearch);
        if (banklist.size() > 0)
        {
            AccountCollectBank bank = banklist.get(0);
            bank.setBankName(accountCollectBank.getBankName());
            bank.setCardNo(EncryptUtils.desEncrypt(accountCollectBank.getCardNo()));
            bank.setSwiftBic(accountCollectBank.getSwiftBic());
            accountCollectBankService.updateByPrimaryKey(bank);
        }
        else
        {
            accountCollectBank.setAccountId(principal.getId());
            accountCollectBank.setCreateBy(principal.getId());
            accountCollectBank.setCreateDate(new Date());
            accountCollectBank.setCardNo(EncryptUtils.desEncrypt(accountCollectBank.getCardNo()));
            accountCollectBankService.insert(accountCollectBank);
        }
        return getJsonMessage(CommonEnums.SUCCESS);
    }

    private void checkAccountDataValidate(Account account)
    {
        if (null == account) { throw new BusinessException(CommonEnums.ERROR_LOGIN_ACCOUNT); }
        if (account.getStatus().intValue() != AccountConsts.ACCOUNT_STATUS_NORMAL.intValue()) { throw new BusinessException(CommonEnums.ERROR_LOGIN_LOCK); }
        if (null != account && !account.verifySignature())
        {// 校验数据
            logger.info("账户信息 数据校验失败");
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
        }
    }

    private void checkCollectAddrDataValidate(AccountCollectBank accountCollectAddr)
    {
        if (null == accountCollectAddr) { throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR); }
        if (null != accountCollectAddr && !accountCollectAddr.verifySignature())
        {// 校验数据
            logger.debug("提币地址 数据校验失败");
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
        }
    }
}

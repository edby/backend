/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.controller;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.boss.common.service.MsgRecordNoSql;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Fund(erc20)提币出金  控制器
 * <p>File：WithdrawERC20Controller.java</p>
 * <p>Title: WithdrawERC20Controller</p>
 * <p>Description:WithdrawERC20Controller</p>
 * <p>Copyright: Copyright (c) 2018-03-01 13:20:27</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
@Api(description = "Fund(ERC20提币出金")
public class WithdrawERC20Controller extends QuotationController
{
    public static final Logger                logger = LoggerFactory.getLogger(WithdrawERC20Controller.class);
    
    @Autowired(required = false)
    private StockRateService                  stockRateService;
    
    @Autowired(required = false)
    private EnableService                     enableService;
    
    @Autowired(required = false)
    private AccountService                    accountService;
    
    @Autowired(required = false)
    private FundCurrentService                fundCurrentService;
    
    @Autowired(required = false)
    private AccountPolicyService              accountPolicyService;
    
    @Autowired(required = false)
    private StockInfoService                  stockInfoService;
    
    @Autowired(required = false)
    private AccountCollectAddrERC20Service    accountCollectAddrERC20Service;
    
    @Autowired(required = false)
    private AccountFundWithdrawService        accountFundWithdrawService;
    
    @Autowired(required = false)
    private MsgRecordNoSql                    msgRecordService;
    
    @Autowired(required = false)
    private AccountCertificationService       accountCertificationService;
    
    @Autowired(required = false)
    private AccountWithdrawRecordERC20Service accountWithdrawRecordERC20Service;
    
    /**
     * Fund提币出金页面导航 ERC20
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/withdrawERC20", method = RequestMethod.GET)
    @ApiOperation(value = "Fund(ERC20)提币出金页面导航", httpMethod = "GET")
    public ModelAndView withdrawEth(String symbol) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("fund/withdrawEth");
        StockInfo stockInfo = new StockInfo();
        if (StringUtils.isBlank(symbol))
        {
            symbol = "eth";
        }
        stockInfo.setRemark(symbol);
        stockInfo.setStockType(FundConsts.STOCKTYPE_ERC20_TOKEN);
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
        if (stockInfoList.size() == 0) { throw new BusinessException(" stockinfo error"); }
        StockInfo info = stockInfoList.get(0);
        if (!StringUtils.equalsIgnoreCase(info.getStockType(),
                FundConsts.STOCKTYPE_ERC20_TOKEN)) { throw new BusinessException(" stockinfo error:" + info.getStockType()); }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        mav.addObject("email", principal.getUserMail());
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(principal.getId());
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        enableModel.setStockinfoId(info.getId());
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
        AccountCollectAddrERC20 accountCollectBank = new AccountCollectAddrERC20();
        accountCollectBank.setAccountId(principal.getId());
        accountCollectBank.setStockinfoId(FundConsts.WALLET_ETH_TYPE);// TOKEN 共用同一个ETH地址
        List<AccountCollectAddrERC20> banklist = accountCollectAddrERC20Service.findList(accountCollectBank);
        if (banklist.size() > 0)
        {
            AccountCollectAddrERC20 bank = banklist.get(0);
            bank.setCollectAddr(EncryptUtils.desDecrypt(bank.getCollectAddr()));
            mav.addObject("bank", bank);
        }
        else
        {
            mav.addObject("bank", null);
        }
        AccountCertification tempCertification = accountCertificationService.findByAccountId(principal.getId());
        boolean certStatus = false;
        if (null != tempCertification)
        {
            if (tempCertification.getStatus().intValue() == 1)
            {
                certStatus = true;
            }
        }
        AccountWithdrawRecordERC20 accountWithdrawRecord = new AccountWithdrawRecordERC20();
        accountWithdrawRecord.setAccountId(principal.getId());
        accountWithdrawRecord.setStockinfoId(info.getId());
        String dateStr = CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMD);
        accountWithdrawRecord.setTimeStart(dateStr + " 00:00:00");
        accountWithdrawRecord.setTimeEnd(dateStr + " 23:59:59");
        logger.debug("AccountWithdrawRecordERC20:" + accountWithdrawRecord.toString());
        java.math.BigDecimal usedAmt = accountWithdrawRecordERC20Service.findSumAmtByAccount(accountWithdrawRecord);
        logger.debug(stockInfo.toString());
        // 已认证
        if (certStatus)
        {
            BigDecimal limitAmount = info.getAuthedUserWithdrawDayUpper().subtract(usedAmt);
            mav.addObject("limitAmount", (limitAmount.compareTo(BigDecimal.ZERO)>0?limitAmount:BigDecimal.ZERO));
        }
        // 未认证
        else
        {
            BigDecimal limitAmount = info.getUnauthUserWithdrawDayUpper().subtract(usedAmt);
            mav.addObject("limitAmount", (limitAmount.compareTo(BigDecimal.ZERO)>0?limitAmount:BigDecimal.ZERO));
        }
        StockInfo stockInfoSearch = new StockInfo();
        stockInfoSearch.setStockType(FundConsts.STOCKTYPE_ERC20_TOKEN);
        List<StockInfo> listCoin = stockInfoService.findListByTypes(FundConsts.STOCKTYPE_ERC20_TOKEN,FundConsts.STOCKTYPE_DIGITALCOIN);
        mav.addObject("listCoin", listCoin);
        mav.addObject("enableAmount", enableModel.getEnableAmount());
        mav.addObject("account", accountService.selectByPrimaryKey(OnLineUserUtils.getId()));
        mav.addObject("coin", info.getStockCode());
        mav.addObject("name", info.getStockName());
        mav.addObject("feeRate", feeRate);
        mav.addObject("stockinfoId", info.getId());
        return mav;
    }
    
    /**
     * Fund提币出金历史列表
     * @param accountWithdrawRecordERC20
     * @param pagin
     * @return
     * @throws BusinessException
     * @author sunbiao  2017年7月19日 上午11:08:10
     */
    @ResponseBody
    @RequestMapping(value = "/withdrawEth/withdrawEthList", method = RequestMethod.POST)
    @ApiOperation(value = "Fund提币出金历史列表", httpMethod = "POST")
    public JsonMessage withdrawEthList(@ModelAttribute AccountWithdrawRecordERC20 accountWithdrawRecordERC20, @ModelAttribute Pagination pagin) throws BusinessException
    {
        if (null == accountWithdrawRecordERC20.getStockinfoId()) { throw new BusinessException(FundEnums.ERROR_STOCKINFOID_NOT_EXIST); }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        accountWithdrawRecordERC20.setAccountId(principal.getId());
        accountWithdrawRecordERC20.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);// 钱包账户资产
        accountWithdrawRecordERC20.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);// 钱包账户提现
        accountWithdrawRecordERC20.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);// 过滤 资产减少
        logger.debug("withdrawList accountWithdrawRecordERC20:" + accountWithdrawRecordERC20.toString());
        PaginateResult<AccountWithdrawRecordERC20> result = accountWithdrawRecordERC20Service.search(pagin, accountWithdrawRecordERC20);
        for (AccountWithdrawRecordERC20 curr : result.getList())
        {
            if (curr.getWithdrawAddr() != null && !curr.getWithdrawAddr().equals(""))
            {
                curr.setWithdrawAddr(EncryptUtils.desDecrypt(curr.getWithdrawAddr()));// des解密
            }
        }
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * Fund提币出金-申请
     * @param fundModel
     * @return
     * @throws BusinessException
     * @author sunbiao  2017年7月13日 上午11:33:27
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/withdrawEth/withdrawEth", method = RequestMethod.POST)
    @ApiOperation(value = "Fund提币出金申请", httpMethod = "POST")
    public JsonMessage withdrawEth(HttpServletRequest request, FundModel fundModel,  String fundPwd, String activeStatus,
            @ModelAttribute PolicyModel policy) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        activeStatus = FundConsts.PUBLIC_STATUS_YES;
        logger.debug("/withdrawEth/withdrawEth page form = " + fundModel.toString());
        if (StringUtils.isBlank(fundPwd)) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        if (fundModel.getStockinfoId() == null) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(fundModel.getStockinfoId());
        if (stockInfo == null || !StringUtils.equalsIgnoreCase(FundConsts.STOCKTYPE_ERC20_TOKEN, stockInfo.getStockType())) { throw new BusinessException("coin error!"); }
        fundModel.setStockinfoIdEx(fundModel.getStockinfoId());
        /*
         * if (fundModel.getAmount().compareTo(BigDecimal.ZERO) <= 0 || fundModel.getAmount().compareTo(BigDecimal.valueOf(50)) > 0)
         * {
         * throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
         * }
         */
        // 小数位数判断
        NumericUtils.checkDecimalDigits("amount", fundModel.getAmount(), 4);
        StockRate stockRate = new StockRate();
        stockRate.setStockinfoId(fundModel.getStockinfoId());
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
        fundModel.setFee(feeRate); // 这里是手续费 不是费率
        // ---------用户数据验证 资金验证 安全策略验证 资金密码验证 start----------
        Account account = accountService.selectByPrimaryKey(principal.getId());
        checkAccountDataValidate(account);
        // 资金密码
        if (StringUtils.isBlank(account.getWalletPwd())) { throw new BusinessException(CommonEnums.ERROR_WALLET_VALID_NOEXIST, null); }
        boolean wallertpwdvalidate = EncryptUtils.validatePassword(String.valueOf(fundPwd), account.getWalletPwd());
        if (!wallertpwdvalidate) { throw new BusinessException(CommonEnums.ERROR_WALLET_VALID_FAILED, null); }
        /*
         * account.setSecurityPolicy(AccountConsts.SECURITY_POLICY_NEEDGA);// GA
         * accountPolicyService.validSecurityPolicy(account, policy);
         */
        // ---------用户数据验证 资金验证 安全策略验证 资金密码验证 end----------
        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        fundModel.setAccountId(principal.getId());
        String lang = CookieUtils.get(request, BitmsConst.COOKIE_LOCALE);
        if (StringUtils.isBlank(lang)) lang = LanguageUtils.getLang(request);
        String addressimg = "";
        AccountCollectAddrERC20 accountCollectBank = new AccountCollectAddrERC20();
        accountCollectBank.setAccountId(principal.getId());
        accountCollectBank.setStockinfoId(FundConsts.WALLET_ETH_TYPE); // TOKEN 共用一套地址
        List<AccountCollectAddrERC20> banklist = accountCollectAddrERC20Service.findList(accountCollectBank);
        if (banklist.size() > 0)
        {
            checkCollectAddrDataValidate(banklist.get(0));
            fundModel.setAddress(banklist.get(0).getCollectAddr());
            addressimg = EncryptUtils.desDecrypt(fundModel.getAddress());
        }
        else
        {
            throw new BusinessException(CommonEnums.ERROR_INVALID_ADDRESS);
        }
        if (!ERC20TokenCoinUtils.ValidateERC20TokenCoinAddress(addressimg))
        {
            // 判断提币地址是否有效
            throw new BusinessException(CommonEnums.ERROR_INVALID_ADDRESS);
        }
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(principal.getId());
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        enableModel.setStockinfoId(fundModel.getStockinfoId());
        enableModel.setRelatedStockinfoId(fundModel.getStockinfoId());
        enableModel = enableService.entrustTerminalEnable(enableModel);
        if (enableModel.getEnableAmount()
                .compareTo(fundModel.getAmount().add(fundModel.getFee())) < 0) { throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE); }
        AccountFundWithdraw accountFundWithdraw = fundCurrentService.doApplyWithdrawERC20(lang, fundModel, activeStatus, accountCollectBank.getCertStatus());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", accountFundWithdraw.getId());
        map.put("withdrawAmt", accountFundWithdraw.getWithdrawAmt());
        map.put("netFee", accountFundWithdraw.getNetFee());
        map.put("image", "data:image/jpeg;base64," + ImageUtils.BufferedImageToBase64(
                ImageUtils.GraphicsToConfirmWithdrawBufferedImage(fundModel.getAmount(), addressimg, accountFundWithdraw.getConfirmCode(), stockInfo.getStockCode())));
        return getJsonMessage(CommonEnums.SUCCESS, map);
    }
    
    /**
     * Fund提币出金-确认
     * @param request
     * @param id
     * @param confirmCode
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/withdrawEth/withdrawEth/confirm", method = RequestMethod.POST)
    @ApiOperation(value = "Fund提币出金确认", httpMethod = "POST")
    public JsonMessage withdrawConfirm(HttpServletRequest request, Long id, String confirmCode) throws BusinessException
    {
        logger.debug("/withdraw/withdraw/confirm page form id=" + id + " confirmCode=" + confirmCode);
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        AccountFundWithdraw entity = accountFundWithdrawService.selectByPrimaryKey(id);
        if (null == entity) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        if (principal.getId().longValue() != entity.getAccountId().longValue()) { throw new BusinessException(CommonEnums.USER_NOT_LOGIN); }
        if (StringUtils.isBlank(confirmCode))
        {
            logger.debug("验证码空");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        if (!StringUtils.equals(entity.getConfirmCode(), confirmCode)) { throw new BusinessException(CommonEnums.ERROR_LOGIN_CAPTCHA); }
        String lang = CookieUtils.get(request, BitmsConst.COOKIE_LOCALE);
        if (StringUtils.isBlank(lang)) lang = LanguageUtils.getLang(request);
        lang = "en_US";
        fundCurrentService.doComfirmWithdrawERC20(entity, confirmCode, lang);
        /*
         * if (BitmsConst.REMIND_PHONE_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
         * {// 短信提醒
         * UserPrincipal principalRemind = OnLineUserUtils.getPrincipal();
         * if (com.blocain.bitms.tools.utils.StringUtils.isNotBlank(principalRemind.getUserMobile()))
         * {// 确保手机已绑定过
         * String vagueMobile = com.blocain.bitms.tools.utils.StringUtils.vagueMobile(principalRemind.getUserMobile());
         * String mobile = new StringBuffer(principalRemind.getCountry()).append(principalRemind.getUserMobile()).toString();
         * msgRecordService.sendRemindSMS(mobile, MessageConst.REMIND_WITHDRAWCONFIRM_PHONE, principalRemind.getLang(), vagueMobile,
         * CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
         * }
         * }
         */
        msgRecordService.sendActiveWithdrawEmail(principal.getUserMail(), entity, lang);
        // 刷新缓存
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        List<StockInfo> list = stockInfoService.findList(stockInfo);
        for (StockInfo stockInfo1 : list)
        {
            // 刷新缓存
            clearAccountAssetCache(principal.getId(), stockInfo1.getId(), stockInfo1.getId());
        }
        return getJsonMessage(CommonEnums.SUCCESS, entity);
    }
    
    /**
     * Fund取消提币
     * @param id
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/withdrawEth/withdrawEthCancel", method = RequestMethod.POST)
    @ApiOperation(value = "Fund取消提币", httpMethod = "POST")
    public JsonMessage withdrawEthCancel(Long id, Long stockinfoId) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        Account account = accountService.selectByPrimaryKey(principal.getId());
        checkAccountDataValidate(account);
        fundCurrentService.doWithdrawCancel(id, principal.getId(), stockinfoId);
        BigDecimal amount = BigDecimal.ZERO;
        AccountWithdrawRecordERC20 accountCashWithdraw = accountWithdrawRecordERC20Service.selectByPrimaryKey(id);
        if (accountCashWithdraw != null)
        {
            amount = accountCashWithdraw.getOccurAmt();
        }
        // 刷新缓存
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        List<StockInfo> list = stockInfoService.findList(stockInfo);
        for (StockInfo stockInfo1 : list)
        {
            // 刷新缓存
            clearAccountAssetCache(principal.getId(), stockInfo1.getId(), stockInfo1.getId());
        }
        return getJsonMessage(CommonEnums.SUCCESS, amount);
    }
    
    /**
     * 新增提币收款地址
     * @param accountCollectAddrERC20
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/withdrawEth/withdrawEthAddrAdd", method = RequestMethod.POST)
    @ApiOperation(value = "新增提币收款地址", httpMethod = "POST")
    public JsonMessage withdrawEthAddrAdd(HttpServletRequest request, @ModelAttribute AccountCollectAddrERC20 accountCollectAddrERC20, String fundPwd,
            @ModelAttribute PolicyModel policy) throws BusinessException
    {
        if (null == accountCollectAddrERC20.getStockinfoId())
        {//
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        if (StringUtils.isEmpty(accountCollectAddrERC20.getCollectAddr()) || StringUtils.isEmpty(fundPwd) || StringUtils.isEmpty(policy.getGa())
                || StringUtils.isEmpty(policy.getSms())) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        if (!ERC20TokenCoinUtils.ValidateERC20TokenCoinAddress(accountCollectAddrERC20.getCollectAddr()))
        {
            // 判断提币地址是否有效
            throw new BusinessException(CommonEnums.ERROR_INVALID_ADDRESS);
        }
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
        accountCollectAddrERC20.setAccountId(principal.getId());
        accountCollectAddrERC20.setCreateBy(principal.getId());
        AccountCollectAddrERC20 accountCollectBankSearch = new AccountCollectAddrERC20();
        accountCollectBankSearch.setAccountId(principal.getId());
        accountCollectBankSearch.setStockinfoId(FundConsts.WALLET_ETH_TYPE);
        List<AccountCollectAddrERC20> banklist = accountCollectAddrERC20Service.findList(accountCollectBankSearch);
        if (banklist.size() > 0)
        {
            AccountCollectAddrERC20 bank = banklist.get(0);
            bank.setCollectAddr(EncryptUtils.desEncrypt(accountCollectAddrERC20.getCollectAddr()));
            bank.setIsActivate(FundConsts.PUBLIC_STATUS_YES);
            bank.setStatus(FundConsts.PUBLIC_STATUS_YES);
            bank.setCertStatus(FundConsts.WALLET_AUTH_STATUS_AUTH);
            accountCollectAddrERC20Service.updateByPrimaryKey(bank);
        }
        else
        {
            accountCollectAddrERC20.setCreateBy(principal.getId());
            accountCollectAddrERC20.setCreateDate(new Date());
            accountCollectAddrERC20.setIsActivate(FundConsts.PUBLIC_STATUS_YES);
            accountCollectAddrERC20.setStatus(FundConsts.PUBLIC_STATUS_YES);
            accountCollectAddrERC20.setCertStatus(FundConsts.WALLET_AUTH_STATUS_AUTH);
            accountCollectAddrERC20.setCollectAddr(EncryptUtils.desEncrypt(accountCollectAddrERC20.getCollectAddr()));
            accountCollectAddrERC20Service.insert(accountCollectAddrERC20);
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
    
    private void checkCollectAddrDataValidate(AccountCollectAddrERC20 accountCollectAddr)
    {
        if (null == accountCollectAddr) { throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR); }
        if (null != accountCollectAddr && !accountCollectAddr.verifySignature())
        {// 校验数据
            logger.debug("提币地址 数据校验失败");
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
        }
    }
}

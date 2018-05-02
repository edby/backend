/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.exchange.controller;

import com.blocain.bitms.boss.common.consts.ParamConsts;
import com.blocain.bitms.boss.common.entity.SysParameter;
import com.blocain.bitms.boss.common.service.SysParameterService;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.quotation.entity.RtQuotationInfo;
import com.blocain.bitms.quotation.service.RtQuotationInfoService;
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
import com.blocain.bitms.trade.account.model.PolicyModel;
import com.blocain.bitms.trade.account.service.AccountPolicyService;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountWalletAsset;
import com.blocain.bitms.trade.fund.entity.AccountWithdrawRecordERC20;
import com.blocain.bitms.trade.fund.service.AccountWalletAssetService;
import com.blocain.bitms.trade.stockinfo.entity.Erc20Token;
import com.blocain.bitms.trade.fund.model.FundModel;
import com.blocain.bitms.trade.fund.service.AccountWithdrawRecordERC20Service;
import com.blocain.bitms.trade.stockinfo.service.Erc20TokenService;
import com.blocain.bitms.trade.fund.service.FundCurrentService;
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
 * <p>File：WithdrawController.java</p>
 * <p>Title: WithdrawController</p>
 * <p>Description:WithdrawController</p>
 * <p>Copyright: Copyright (c) 2018-03-28</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.EXCHANGE)
@Api(description = "Fund(ERC20提币出金")
public class WithdrawController extends QuotationController
{
    public static final Logger                logger = LoggerFactory.getLogger(WithdrawController.class);
    
    @Autowired(required = false)
    private StockRateService                  stockRateService;
    
    @Autowired(required = false)
    private EnableService                     enableService;
    
    @Autowired(required = false)
    private AccountService                    accountService;
    
    @Autowired(required = false)
    private FundCurrentService                fundCurrentService;
    
    @Autowired(required = false)
    private AccountWalletAssetService         accountWalletAssetService;
    
    @Autowired(required = false)
    private StockInfoService                  stockInfoService;
    
    @Autowired(required = false)
    private Erc20TokenService                 erc20TokenService;
    
    @Autowired(required = false)
    private AccountWithdrawRecordERC20Service accountWithdrawRecordERC20Service;
    
    @Autowired(required = false)
    private SysParameterService               sysParameterService;
    
    @Autowired(required = false)
    private RtQuotationInfoService            rtQuotationInfoService;
    
    /**
     * withdraw
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/withdraw", method = RequestMethod.GET)
    @ApiOperation(value = "withdraw", httpMethod = "GET")
    public ModelAndView withdraw(String contractAddr, Long trade) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("exchange/withdraw");
        boolean isLogin = checkLogin();
        mav.addObject("longStatus", isLogin);
        if (isLogin)
        {
            if (StringUtils.isBlank(contractAddr))
            {
                contractAddr = FundConsts.WALLET_VINPAIR_CONTRACTADDR;
            }
            else
            {
                if (StringUtils.equalsIgnoreCase("eth", contractAddr))
                {
                    contractAddr = FundConsts.WALLET_VINPAIR_CONTRACTADDR;
                }
            }
            Long id;
            if (StringUtils.equalsIgnoreCase("eth", contractAddr) || trade > 0)
            {
                contractAddr = FundConsts.WALLET_VINPAIR_CONTRACTADDR;
                id = getStockInfo(contractAddr).getCapitalStockinfoId();
            }
            else
            {
                id = getStockInfo(contractAddr).getTradeStockinfoId();
            }
            StockInfo stockInfo = stockInfoService.selectByPrimaryKey(id);
            Erc20Token erc20Token = erc20TokenService.getErc20Token(contractAddr, null);
            if (erc20Token == null)
            {
                mav.addObject("symbol", "");
                mav.addObject("symbolName", "");
                mav.addObject("totalSupply", "");
                mav.addObject("ERC20ContractEnd", "");
                mav.addObject("ERC20Contract", "");
                mav.addObject("activeDays", "0");
            }
            else
            {
                mav.addObject("symbol", stockInfo.getStockCode());
                mav.addObject("symbolName", stockInfo.getStockName());
                mav.addObject("totalSupply", erc20Token.getTotalSupply());
                mav.addObject("ERC20ContractEnd",
                        erc20Token.getContractAddr().length() > 10 ? erc20Token.getContractAddr().substring(erc20Token.getContractAddr().length() - 10) : "");
                mav.addObject("ERC20Contract", stockInfo.getTokenContactAddr());
                Double day = DateUtils.getDistanceOfTwoDate(new Date(), erc20Token.getActiveEndDate());
                if (day < 0) day = 0d;
                mav.addObject("activeDays", day.longValue());
            }
            mav.addObject("stockinfoId", id);
            mav.addObject("label", true);
        }
        return mav;
    }
    
    /**
     * 获取可用以及提现额度费率等
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/getEnable", method = RequestMethod.GET)
    @ApiOperation(value = "getEnable", httpMethod = "GET")
    @ResponseBody
    public JsonMessage getEnable(Long id)
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        // ==============================检查币种 start====================================
        StockInfo entity = new StockInfo();
        entity.setStockType(FundConsts.STOCKTYPE_ERC20_TOKEN);
        entity.setId(id);
        List<StockInfo> stockInfoList = stockInfoService.findList(entity);
        if (stockInfoList.size() == 0) { throw new BusinessException("not has this symbol!"); }
        StockInfo stockInfo = stockInfoList.get(0);
        // ==============================检查币种 end====================================
        //
        // ==============================获取可用 start====================================
        Map<String, Object> map = new HashMap<>();
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(principal.getId());
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        enableModel.setStockinfoId(stockInfo.getId());
        enableModel.setRelatedStockinfoId(stockInfo.getId());
        enableModel = enableService.entrustTerminalEnable(enableModel);
        if (enableModel != null)
        {
            if (enableModel.getEnableAmountEx() == null)
            {
                enableModel.setEnableAmountEx(BigDecimal.ZERO);
            }
        }
        map.put("enableModel", enableModel);
        StockRate stockRate = new StockRate();
        stockRate.setStockinfoId(stockInfo.getId());
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
        AccountWithdrawRecordERC20 accountWithdrawRecord = new AccountWithdrawRecordERC20();
        accountWithdrawRecord.setAccountId(principal.getId());
        String dateStr = CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMD);
        accountWithdrawRecord.setTimeStart(dateStr + " 00:00:00");
        accountWithdrawRecord.setTimeEnd(dateStr + " 23:59:59");
        logger.debug("AccountWithdrawRecordERC20:" + accountWithdrawRecord.toString());
        Integer usedCnt = accountWithdrawRecordERC20Service.findCuntByAccount(accountWithdrawRecord);
        // 当日最大提现次数
        SysParameter params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(ParamConsts.ERC20TOKEN_WITHDRAW_CNT_ETH_FOR_DAY);
        params = sysParameterService.getSysParameterByEntity(params);
        int cnt = BigDecimal.valueOf(Double.valueOf(params.getValue())).toBigInteger().intValue();
        // 单笔最大提现
        params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(ParamConsts.ERC20TOKEN_WITHDRAW_UP_ETH_FOR_ONE);
        params = sysParameterService.getSysParameterByEntity(params);
        BigDecimal upLine = BigDecimal.valueOf(Double.valueOf(params.getValue()));
        BigDecimal ethUpLine = BigDecimal.valueOf(Double.valueOf(params.getValue()));
        if (id.longValue() != FundConsts.WALLET_ETH_TYPE.longValue())
        {
            StockInfo stockInfoPair = stockInfoService.findByContractAddr(stockInfo.getTokenContactAddr());
            BigDecimal platPrice = BigDecimal.ZERO;
            RtQuotationInfo rtQuotationInfo = rtQuotationInfoService.queryRtQuotationInfo(stockInfoPair.getTradeStockinfoId(), stockInfoPair.getId());
            if (null != rtQuotationInfo)
            {
                platPrice = rtQuotationInfo.getPlatPrice();
            }
            if (platPrice == null)
            {
                upLine = BigDecimal.valueOf(10000000);
            }
            else
            {
                if (platPrice.compareTo(BigDecimal.ZERO) <= 0)
                {
                    upLine = BigDecimal.valueOf(0);
                }
                else
                {
                    if (platPrice.compareTo(BigDecimal.ZERO) <= 0)
                    {
                        upLine = BigDecimal.valueOf(0);
                    }
                    else
                    {
                        upLine = upLine.divide(platPrice,4,BigDecimal.ROUND_DOWN);
                    }
                }
            }
        }
        map.put("fee", feeRate);
        map.put("usedCnt", usedCnt); // 当日已经使用提现次数
        map.put("totalCnt", cnt); // 当日最大提现次数
        map.put("ethUpLine", ethUpLine); // 单笔最大提现额度折合ETH
        map.put("limitAmount", upLine); // 单笔最大提现额度折合TOKEN
        // ==============================获取可用 end====================================
        return getJsonMessage(CommonEnums.SUCCESS, map);
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
    @RequestMapping(value = "/withdrawEth/withdrawEthList", method = RequestMethod.GET)
    @ApiOperation(value = "Fund提币出金历史列表", httpMethod = "GET")
    public JsonMessage withdrawEthList(@ModelAttribute AccountWithdrawRecordERC20 accountWithdrawRecordERC20, String isCapital, @ModelAttribute Pagination pagin,
            String contractAddr) throws BusinessException
    {
        if (isCapital == null) isCapital = "no";
        // 证券信息ID判断
        if (StringUtils.isNotBlank(contractAddr))
        {
            if (StringUtils.equalsIgnoreCase(contractAddr, "eth"))
            {
                contractAddr = FundConsts.WALLET_VINPAIR_CONTRACTADDR;
                isCapital = "yes";
            }
            StockInfo stockInfo = getStockInfo(contractAddr);
            accountWithdrawRecordERC20.setStockinfoId(StringUtils.equalsIgnoreCase(isCapital, "yes") ? stockInfo.getCapitalStockinfoId() : stockInfo.getTradeStockinfoId());
        }
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
    public JsonMessage withdrawEth(HttpServletRequest request, FundModel fundModel, String activeStatus, PolicyModel policy, String address) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        activeStatus = FundConsts.PUBLIC_STATUS_YES;
        logger.debug("/withdrawEth/withdrawEth page form = " + fundModel.toString());
        if (fundModel.getStockinfoId() == null) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        if (StringUtils.isEmpty(address) || StringUtils.isEmpty(policy.getGa())) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        if (!ERC20TokenCoinUtils.ValidateERC20TokenCoinAddress(address))
        {
            // 判断提币地址是否有效
            throw new BusinessException(CommonEnums.ERROR_INVALID_ADDRESS);
        }
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(fundModel.getStockinfoId());
        if (stockInfo == null || !StringUtils.equalsIgnoreCase(FundConsts.STOCKTYPE_ERC20_TOKEN, stockInfo.getStockType())) { throw new BusinessException("coin error!"); }
        if (!StringUtils.equalsIgnoreCase(stockInfo.getCanWithdraw(),FundConsts.PUBLIC_STATUS_YES))
        {
            throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
        }
        fundModel.setStockinfoIdEx(fundModel.getStockinfoId());
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
        account.setSecurityPolicy(AccountConsts.SECURITY_POLICY_NEEDGA);// GA
        if (StringUtils.isBlank(account.getAuthKey())) { throw new BusinessException(CommonEnums.ERROR_GA_NOT_BIND); }
        String secretKey = EncryptUtils.desDecrypt(account.getAuthKey());
        checkGaErrorCnt(account.getUnid(), secretKey, policy.getGa());
        // ---------用户数据验证 资金验证 安全策略验证 资金密码验证 end----------
        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        fundModel.setAccountId(principal.getId());
        String lang = CookieUtils.get(request, BitmsConst.COOKIE_LOCALE);
        if (StringUtils.isBlank(lang)) lang = LanguageUtils.getLang(request);
        fundModel.setAddress(EncryptUtils.desEncrypt(address));
        BigDecimal enableAmount = BigDecimal.ZERO;
        String isActiveTrade = "no";
        AccountWalletAsset accountWalletAsset = new AccountWalletAsset();
        accountWalletAsset.setAccountId(fundModel.getAccountId());
        accountWalletAsset.setStockinfoId(fundModel.getStockinfoId());
        logger.debug("从钱包账户资产db中查找可用数量 accountWalletAsset:" + accountWalletAsset.toString());
        List<AccountWalletAsset> accountWalletAssetList = accountWalletAssetService.findList(accountWalletAsset);
        if (accountWalletAssetList.size() > 0)
        {
            accountWalletAsset = accountWalletAssetList.get(0);
            // 如果冻结数量小于等于 直接取0 防止风险
            if (accountWalletAsset.getFrozenAmt().compareTo(BigDecimal.ZERO) <= 0)
            {
                accountWalletAsset.setFrozenAmt(BigDecimal.ZERO);
            }
            enableAmount = accountWalletAsset.getAmount().subtract(accountWalletAsset.getFrozenAmt());
            isActiveTrade = accountWalletAsset.getIsActiveTrade();
        }
        if(!StringUtils.equalsIgnoreCase(isActiveTrade,FundConsts.PUBLIC_STATUS_YES))
        {
            throw new BusinessException("Withdraw Forbidden!");
        }
        if (enableAmount
                .compareTo(fundModel.getAmount().add(fundModel.getFee())) < 0) { throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE); }
        fundCurrentService.doTradexWithdrawERC20(fundModel);
        // 刷新缓存
        stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        List<StockInfo> list = stockInfoService.findList(stockInfo);
        for (StockInfo stockInfo1 : list)
        {
            // 刷新缓存
            setAccountAssetCache(principal.getId(), FundConsts.WALLET_BTC_TYPE, stockInfo1.getId());
        }
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    public StockInfo getStockInfo(String addr)
    {
        StockInfo stockInfo = stockInfoService.findByContractAddr(addr);
        if (!StringUtils.equalsIgnoreCase(stockInfo.getIsActive(), FundConsts.PUBLIC_STATUS_YES)) { throw new BusinessException("pair not open"); }
        return stockInfo;
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
     * 检查登录
     */
    public boolean checkLogin()
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (principal == null)
        {
            return false;
        }
        else
        {
            if (principal.getId() == null)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
    }
}

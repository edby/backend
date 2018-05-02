/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.controller;

import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.boss.common.consts.ParamConsts;
import com.blocain.bitms.boss.common.entity.Dictionary;
import com.blocain.bitms.boss.common.entity.SysParameter;
import com.blocain.bitms.boss.common.service.DictionaryService;
import com.blocain.bitms.boss.common.service.MsgRecordNoSql;
import com.blocain.bitms.boss.common.service.SysParameterService;
import com.blocain.bitms.monitor.entity.AcctAssetChkResult;
import com.blocain.bitms.monitor.service.AcctAssetChkService;
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
import com.blocain.bitms.trade.fund.entity.AccountCollectAddr;
import com.blocain.bitms.trade.fund.entity.AccountFundCurrent;
import com.blocain.bitms.trade.fund.entity.AccountFundWithdraw;
import com.blocain.bitms.trade.fund.entity.AccountWithdrawRecord;
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
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Fund提币出金  控制器
 * <p>File：WithdrawController.java</p>
 * <p>Title: WithdrawController</p>
 * <p>Description:WithdrawController</p>
 * <p>Copyright: Copyright (c) 2017年7月14日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
@Api(description = "Fund提币出金")
public class WithdrawController extends QuotationController
{
    public static final Logger         logger = LoggerFactory.getLogger(WithdrawController.class);
    
    @Autowired(required = false)
    private AccountFundCurrentService    accountFundCurrentService;

    @Autowired(required = false)
    private AccountWithdrawRecordService accountWithdrawRecordService;
    
    @Autowired(required = false)
    private AccountCollectAddrService    accountCollectAddrService;
    
    @Autowired(required = false)
    private SysParameterService          sysParameterService;
    
    @Autowired(required = false)
    private EnableService                enableService;
    
    @Autowired(required = false)
    private FundCurrentService           fundCurrentService;
    
    @Autowired(required = false)
    private DictionaryService            dictionaryService;
    
    @Autowired(required = false)
    private AccountService                  accountService;
    
    @Autowired(required = false)
    private AcctAssetChkService         acctAssetChkService;
    
    @Autowired(required = false)
    private AccountFundWithdrawService  accountFundWithdrawService;
    
    @Autowired(required = false)
    private MsgRecordNoSql              msgRecordService;
    
    @Autowired(required = false)
    private AccountPolicyService        accountPolicyService;
    
    @Autowired(required = false)
    private StockInfoService            stockInfoService;

    @Autowired(required = false)
    private StockRateService            stockRateService;
    
    @Autowired(required = false)
    private AccountCertificationService accountCertificationService;
    
    /**
     * Fund提币出金页面导航BTC
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/withdraw", method = RequestMethod.GET)
    @ApiOperation(value = "Fund提币出金页面导航", httpMethod = "GET")
    public ModelAndView withdraw() throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        Map<String, Object> map = new HashMap<String, Object>();
        List<SysParameter> list = sysParameterService.getSysValueByParams(TradeEnums.SYS_PARAMETER_SYSTEM_NAME_TRADE.code,
                ParamConsts.WITHDRAW_TANSFER_BTC_COSTAVER,
                ParamConsts.WITHDRAW_TANSFER_BTC_COSTLOW, ParamConsts.WITHDRAW_TANSFER_BTC_COSTUPPER);
        for (SysParameter sysParameter : list)
        {
            map.put(sysParameter.getParameterName(), sysParameter.getValue());
        }

        StockRate stockRate = new StockRate();
        stockRate.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
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
        map.remove(ParamConsts.WITHDRAW_TANSFER_BTC_COSTAVER);
        map.put(ParamConsts.WITHDRAW_TANSFER_BTC_COSTAVER, feeRate);
        AccountCertification tempCertification = accountCertificationService.findByAccountId(principal.getId());
        boolean certStatus = false;
        if (null != tempCertification )
        {
            if(tempCertification.getStatus().intValue() == 1)
            {
                certStatus = true;
            }
        }
        ModelAndView mav = new ModelAndView("fund/withdraw");
        mav.addObject("email", principal.getUserMail());
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(principal.getId());
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        enableModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);// btc
        enableModel.setRelatedStockinfoId(FundConsts.WALLET_BTC2USD_TYPE);
        enableModel = enableService.entrustTerminalEnable(enableModel);
        if (enableModel != null)
        {
            if (enableModel.getEnableAmountEx() == null)
            {
                enableModel.setEnableAmountEx(BigDecimal.ZERO);
            }
            map.put("btcEnableAmount", enableModel.getEnableAmount());
        }

        // 查询当日上限
        boolean certificationStatus = false;
        if (null != tempCertification)
        {
            if (tempCertification.getStatus().intValue() == 1)
            {
                certificationStatus = true;
            }
        }
        logger.debug("certificationStatus=" + certificationStatus);
        StockInfo info = stockInfoService.selectByPrimaryKey(FundConsts.WALLET_BTC_TYPE);
        map.remove(ParamConsts.WITHDRAW_TANSFER_BTC_DAY_QUOTAUPPER_FOR_AUTH);
        map.put(ParamConsts.WITHDRAW_TANSFER_BTC_DAY_QUOTAUPPER_FOR_AUTH, info.getAuthedUserWithdrawDayUpper());
        map.remove(ParamConsts.WITHDRAW_TANSFER_BTC_DAY_QUOTAUPPER);
        map.put(ParamConsts.WITHDRAW_TANSFER_BTC_DAY_QUOTAUPPER, info.getUnauthUserWithdrawDayUpper());

        AccountWithdrawRecord accountWithdrawRecord = new AccountWithdrawRecord();
        accountWithdrawRecord.setAccountId(principal.getId());
        accountWithdrawRecord.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        String dateStr = CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMD);
        accountWithdrawRecord.setTimeStart(dateStr + " 00:00:00");
        accountWithdrawRecord.setTimeEnd(dateStr + " 23:59:59");
        logger.debug("accountWithdrawRecord:" + accountWithdrawRecord.toString());
        accountWithdrawRecord.setTableName(getStockInfo(accountWithdrawRecord.getStockinfoId()).getTableFundCurrent());
        java.math.BigDecimal btcUsedAmt = accountWithdrawRecordService.findSumAmtByAccount(accountWithdrawRecord);
        map.put("btcUsedAmt", btcUsedAmt);// 已经使用的BTC额度
        mav.addObject("paramlist", JSONObject.toJSON(map));
        mav.addObject("feeRate", feeRate);
        mav.addObject("account", accountService.selectByPrimaryKey(OnLineUserUtils.getId()));
        mav.addObject("certStatus", certStatus);
        StockInfo stockInfoSearch = new StockInfo();
        stockInfoSearch.setStockType(FundConsts.STOCKTYPE_ERC20_TOKEN);
        List<StockInfo> listCoin = stockInfoService.findListByTypes(FundConsts.STOCKTYPE_ERC20_TOKEN,FundConsts.STOCKTYPE_DIGITALCOIN);
        mav.addObject("listCoin", listCoin);


        return mav;
    }

    /**
     * Fund提币出金历史列表
     * @param accountWithdrawRecord
     * @param pagin
     * @return
     * @throws BusinessException
     * @author sunbiao  2017年7月19日 上午11:08:10
     */
    @ResponseBody
    @RequestMapping(value = "/withdraw/withdrawList", method = RequestMethod.POST)
    @ApiOperation(value = "Fund提币出金历史列表", httpMethod = "POST")
    public JsonMessage withdrawList(@ModelAttribute AccountWithdrawRecord accountWithdrawRecord, @ModelAttribute Pagination pagin) throws BusinessException
    {
        if (null == accountWithdrawRecord.getStockinfoId()) { throw new BusinessException(FundEnums.ERROR_STOCKINFOID_NOT_EXIST); }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        accountWithdrawRecord.setAccountId(principal.getId());
        accountWithdrawRecord.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);// 钱包账户资产
        accountWithdrawRecord.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);// 钱包账户提现
        accountWithdrawRecord.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);// 过滤 资产减少
        logger.debug("withdrawList accountWithdrawRecord:" + accountWithdrawRecord.toString());
        //accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getStockinfoId()).getTableFundCurrent());
        PaginateResult<AccountWithdrawRecord> result = accountWithdrawRecordService.search(pagin, accountWithdrawRecord);
        for (AccountWithdrawRecord curr : result.getList())
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
    @RequestMapping(value = "/withdraw/withdraw", method = RequestMethod.POST)
    @ApiOperation(value = "Fund提币出金申请", httpMethod = "POST")
    public JsonMessage withdraw(HttpServletRequest request, FundModel fundModel, String fundPwd, String activeStatus, @ModelAttribute PolicyModel policy)
            throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        checkSwitch();// 查询开关
        logger.debug("/withdraw/withdraw page form = " + fundModel.toString());
        if (StringUtils.isBlank(fundPwd)) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        if (fundModel.getAmount().compareTo(BigDecimal.ZERO) <= 0) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        // 小数位数判断
        NumericUtils.checkDecimalDigits("amount", fundModel.getAmount(), 4);
        StockRate stockRate = new StockRate();
        stockRate.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
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
        fundModel.setFee(feeRate);
        // ---------用户数据验证 资金验证 安全策略验证 资金密码验证 start----------
        Account account = accountService.selectByPrimaryKey(principal.getId());
        checkAccountDataValidate(account);
//        checkAccountAssetDataValidate(account.getId());
//        if (!com.blocain.bitms.tools.utils.StringUtils.equalsIgnoreCase(activeStatus, "yes"))
//        {
//            accountPolicyService.validSecurityPolicy(account, policy);
//        }
        // 资金密码
        if (StringUtils.isBlank(account.getWalletPwd())) { throw new BusinessException(CommonEnums.ERROR_WALLET_VALID_NOEXIST, null); }
        boolean wallertpwdvalidate = EncryptUtils.validatePassword(String.valueOf(fundPwd), account.getWalletPwd());
        if (!wallertpwdvalidate) { throw new BusinessException(CommonEnums.ERROR_WALLET_VALID_FAILED, null); }
        // ---------用户数据验证 资金验证 安全策略验证 资金密码验证 end----------
        fundModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        String temp = fundModel.getAddress().toString();
        temp = temp.substring(temp.indexOf("_") + 1);
        fundModel.setAddress(temp);// address 有备注的内容，前台不好处理
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        fundModel.setAccountId(principal.getId());
        String lang = CookieUtils.get(request, BitmsConst.COOKIE_LOCALE);
        if (StringUtils.isBlank(lang)) lang = LanguageUtils.getLang(request);
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(principal.getId());
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        enableModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);// btc
        enableModel.setRelatedStockinfoId(FundConsts.WALLET_BTC2USD_TYPE);
        enableModel = enableService.entrustTerminalEnable(enableModel);
        if (enableModel.getEnableAmount()
                .compareTo(fundModel.getAmount().add(fundModel.getFee())) < 0) { throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE); }
        AccountCollectAddr accountCollectAddr = new AccountCollectAddr();
        accountCollectAddr.setAccountId(principal.getId());
        accountCollectAddr.setCollectAddr(EncryptUtils.desEncrypt(fundModel.getAddress()));// des加密 加密以后才能查
        accountCollectAddr.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        accountCollectAddr.setIsActivate(FundConsts.PUBLIC_STATUS_YES);
        accountCollectAddr.setStatus(FundConsts.PUBLIC_STATUS_YES);
        accountCollectAddr = accountCollectAddrService.findAccountCollectAddr(accountCollectAddr);
        if (accountCollectAddr == null) { throw new BusinessException(FundEnums.ERROR_WITHDRAW_ADDRESS_NOT_EXIST); }
        checkCollectAddrDataValidate(accountCollectAddr);
        AccountFundWithdraw accountFundWithdraw = fundCurrentService.doApplyWithdraw(lang, fundModel, activeStatus, accountCollectAddr.getCertStatus());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", accountFundWithdraw.getId());
        map.put("withdrawAmt", accountFundWithdraw.getWithdrawAmt());
        map.put("netFee", accountFundWithdraw.getNetFee());
        map.put("image", "data:image/jpeg;base64," + ImageUtils.BufferedImageToBase64(
                ImageUtils.GraphicsToConfirmWithdrawBufferedImage(fundModel.getAmount(), fundModel.getAddress(), accountFundWithdraw.getConfirmCode())));
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
    @RequestMapping(value = "/withdraw/withdraw/confirm", method = RequestMethod.POST)
    @ApiOperation(value = "Fund提币出金确认", httpMethod = "POST")
    public JsonMessage withdrawConfirm(HttpServletRequest request, Long id, String confirmCode) throws BusinessException
    {
        checkSwitch();// 查询开关
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
        fundCurrentService.doComfirmWithdraw(entity, confirmCode, lang);
       /* if (BitmsConst.REMIND_PHONE_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
        {// 短信提醒
            UserPrincipal principalRemind = OnLineUserUtils.getPrincipal();
            if (com.blocain.bitms.tools.utils.StringUtils.isNotBlank(principalRemind.getUserMobile()))
            {// 确保手机已绑定过
                String vagueMobile = com.blocain.bitms.tools.utils.StringUtils.vagueMobile(principalRemind.getUserMobile());
                String mobile = new StringBuffer(principalRemind.getCountry()).append(principalRemind.getUserMobile()).toString();
                msgRecordService.sendRemindSMS(mobile, MessageConst.REMIND_WITHDRAWCONFIRM_PHONE, principalRemind.getLang(), vagueMobile,
                        CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
            }
        }*/
        msgRecordService.sendActiveWithdrawEmail(principal.getUserMail(), entity, lang);
        // 刷新缓存
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        List<StockInfo> list = stockInfoService.findList(stockInfo);
        for (StockInfo stockInfo1 : list)
        {
            // 刷新缓存
            setAccountAssetCache(principal.getId(), FundConsts.WALLET_BTC_TYPE, stockInfo1.getId());
        }
        return getJsonMessage(CommonEnums.SUCCESS, entity);
    }
    
    /**
     * Fund提币地址激活时间过期重发激活邮件
     * @param request
     * @param id
     * @param confirmCode
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/withdraw/withdraw/sendActiveEmail", method = RequestMethod.POST)
    @ApiOperation(value = "Fund提币地址激活时间过期重发激活邮件", httpMethod = "POST")
    public JsonMessage sendActiveEmail(HttpServletRequest request, Long id, String confirmCode) throws BusinessException
    {
        checkSwitch();// 查询开关
        String lang = CookieUtils.get(request, BitmsConst.COOKIE_LOCALE);
        if (StringUtils.isBlank(lang)) lang = LanguageUtils.getLang(request);
        lang = "en_US";
        AccountCollectAddr accountCollectAddr = accountCollectAddrService.selectByPrimaryKey(id);
        if (accountCollectAddr == null) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        Account account = accountService.selectByPrimaryKey(principal.getId());
        if (principal.getId().longValue() != accountCollectAddr.getAccountId().longValue()) { throw new BusinessException(CommonEnums.USER_NOT_LOGIN); }
        StringBuffer cacheKey = new StringBuffer(BitmsConst.MESSAGE).append(BitmsConst.SEPARATOR).append(accountCollectAddr.getId());
        String sessionId = RedisUtils.get(cacheKey.toString());
        if (StringUtils.isBlank(sessionId))
        {
            msgRecordService.sendActiveCollectAddrEmail(account.getEmail().toString(), accountCollectAddr, lang);
            accountCollectAddr.setCreateDate(new Timestamp(System.currentTimeMillis()));
            accountCollectAddrService.updateByPrimaryKey(accountCollectAddr);
            return getJsonMessage(CommonEnums.SUCCESS);
        }
        else
        {
            return getJsonMessage(CommonEnums.FAIL);
        }
    }
    
    /**
     * Fund取消提币
     * @param id
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/withdraw/withdrawCancel", method = RequestMethod.POST)
    @ApiOperation(value = "Fund取消提币", httpMethod = "POST")
    public JsonMessage withdrawCancel(Long id) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        checkSwitch();// 查询开关
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        Account account = accountService.selectByPrimaryKey(principal.getId());
        checkAccountDataValidate(account);
        // checkAccountAssetDataValidate(account.getId());
        fundCurrentService.doWithdrawCancel(id, principal.getId(), FundConsts.WALLET_BTC_TYPE);
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
     * 提币出金收款地址列表
     * @param accountCollectAddr
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/withdraw/withdrawAddr", method = RequestMethod.POST)
    @ApiOperation(value = "提币出金收款地址列表", httpMethod = "POST")
    public JsonMessage withdrawAddr(@ModelAttribute AccountCollectAddr accountCollectAddr) throws BusinessException
    {
        if (null == accountCollectAddr.getStockinfoId()) { throw new BusinessException(FundEnums.ERROR_STOCKINFOID_NOT_EXIST); }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        accountCollectAddr.setAccountId(principal.getId());
        logger.debug("withdrawAddr accountCollectAddr:" + accountCollectAddr.toString());
        PaginateResult<AccountCollectAddr> result = accountCollectAddrService.search(accountCollectAddr);
        for (AccountCollectAddr addr : result.getList())
        {
            if (addr.getCollectAddr() != null && !addr.getCollectAddr().equals(""))
            {
                addr.setCollectAddr(EncryptUtils.desDecrypt(addr.getCollectAddr()));
            }
        }
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 新增提币收款地址
     * @param accountCollectAddr
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/withdraw/withdrawAddrAdd", method = RequestMethod.POST)
    @ApiOperation(value = "新增提币收款地址", httpMethod = "POST")
    public JsonMessage withdrawAddrAdd(HttpServletRequest request, @ModelAttribute AccountCollectAddr accountCollectAddr, String fundPwd,
            @ModelAttribute PolicyModel policy) throws BusinessException
    {
        checkSwitch();// 查询开关
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        // ---------用户数据验证 资金验证 安全策略验证 资金密码验证 start----------
        Account account = accountService.selectByPrimaryKey(principal.getId());
        checkAccountDataValidate(account);
        // 资金密码
        if (com.blocain.bitms.tools.utils.StringUtils.isBlank(account.getWalletPwd())) { throw new BusinessException(CommonEnums.ERROR_WALLET_VALID_NOEXIST, null); }
        boolean wallertpwdvalidate = EncryptUtils.validatePassword(String.valueOf(fundPwd), account.getWalletPwd());
        if (!wallertpwdvalidate) { throw new BusinessException(CommonEnums.ERROR_WALLET_VALID_FAILED, null); }
        // ---------用户数据验证 资金验证 安全策略验证 资金密码验证 end----------
//        AccountCollectAddr entity = new AccountCollectAddr();
//        entity.setAccountId(principal.getId());
//        entity.setCollectAddr(EncryptUtils.desEncrypt(accountCollectAddr.getCollectAddr()));// 加密后查询
//        entity.setIsActivate(FundConsts.PUBLIC_STATUS_YES);
//        List<AccountCollectAddr> list = accountCollectAddrService.findList(entity);
//        if (list.size() > 0) { throw new BusinessException(CommonEnums.ERROR_RAISE_ADDR_EXIST); }
        accountCollectAddr.setAccountId(principal.getId());
        accountCollectAddr.setCreateBy(principal.getId());
        String lang = CookieUtils.get(request, BitmsConst.COOKIE_LOCALE);
        if (StringUtils.isBlank(lang)) lang = LanguageUtils.getLang(request);
        lang = "en_US";
        // 先检查数据  最后验证安全策略
        account.setSecurityPolicy(4);// GA+短信
        accountPolicyService.validSecurityPolicy(account, policy);
        accountCollectAddr.setCertStatus(FundConsts.WALLET_AUTH_STATUS_AUTH);
        fundCurrentService.doWithdrawAddrAdd(lang, accountCollectAddr);
        /*if (BitmsConst.REMIND_PHONE_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
        {// 短信提醒
            UserPrincipal principalRemind = OnLineUserUtils.getPrincipal();
            if (com.blocain.bitms.tools.utils.StringUtils.isNotBlank(principalRemind.getUserMobile()))
            {// 确保手机已绑定过
                String vagueMobile = com.blocain.bitms.tools.utils.StringUtils.vagueMobile(principalRemind.getUserMobile());
                String mobile = new StringBuffer(principalRemind.getCountry()).append(principalRemind.getUserMobile()).toString();
                msgRecordService.sendRemindSMS(mobile, MessageConst.REMIND_ADDWITHDRAWADDR_PHONE, principalRemind.getLang(), vagueMobile,
                        CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
            }
        }*/
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 删除提币收款地址
     * @param id
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/withdraw/withdrawAddrDelete", method = RequestMethod.POST)
    @ApiOperation(value = "删除提币收款地址", httpMethod = "POST")
    public JsonMessage withdrawAddrDelete(Long id) throws BusinessException
    {
        checkSwitch();// 查询开关
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        Account account = accountService.selectByPrimaryKey(principal.getId());
        checkAccountDataValidate(account);
        AccountCollectAddr accountCollectAddr = accountCollectAddrService.selectByPrimaryKey(id);
        // checkCollectAddrDataValidate(accountCollectAddr);
        accountCollectAddrService.delete(id);
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 提币地址邮件激活操作
     * @param request
     * @return
     * @throws BusinessException
     */
    @RequestMapping("/withdraw/activeCollectAddr")
    @ApiOperation(value = "提币地址邮件激活操作", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "uid", value = "帐户ID", required = true, paramType = "form"),
            @ApiImplicitParam(name = "op_id", value = "操作ID", required = true, paramType = "form"),
            @ApiImplicitParam(name = "id", value = "ID", required = true, paramType = "form")})
    public ModelAndView activeCollectAddr(HttpServletRequest request) throws BusinessException
    {
        boolean flag = true;
        String id = request.getParameter("id");
        String uid = request.getParameter("uid");
        String oid = request.getParameter("op_id");
        if (StringUtils.isBlank(id) || StringUtils.isBlank(oid) || StringUtils.isBlank(uid))
        {// 判断会话和unid
            flag = false;
        }
        StringBuffer cacheKey = new StringBuffer(BitmsConst.MESSAGE).append(BitmsConst.SEPARATOR).append(id);
        String sessionId = RedisUtils.get(cacheKey.toString());
        if (StringUtils.isBlank(sessionId) || !StringUtils.equalsIgnoreCase(sessionId, oid))
        {// 判断会话是否超时或匹配
            flag = false;
        }
        AccountCollectAddr accountCollectAddr = new AccountCollectAddr();
        ModelAndView mav = new ModelAndView("fund/activeAddr");
        if (flag)
        {
            accountCollectAddr = accountCollectAddrService.selectByPrimaryKey(Long.parseLong(id));
            checkCollectAddrDataValidate(accountCollectAddr);
            if (accountCollectAddr == null)
            {// 判断地址是否还存在
                flag = false;
            }
            AccountCollectAddr entity = new AccountCollectAddr();
            entity.setAccountId(Long.parseLong(uid));
            entity.setCollectAddr(accountCollectAddr.getCollectAddr());
            entity.setIsActivate(FundConsts.PUBLIC_STATUS_YES);
            List<AccountCollectAddr> list = accountCollectAddrService.findList(entity);
            if (list.size() > 0)
            {
                // 不能重复激活同一个地址
                flag = false;
            }
            if (flag)
            {
                if (accountCollectAddr.getIsActivate().toString().equals(FundConsts.PUBLIC_STATUS_NO))
                {
                    accountCollectAddr.setIsActivate(FundConsts.PUBLIC_STATUS_YES);
                    accountCollectAddrService.updateByPrimaryKey(accountCollectAddr);
                    RedisUtils.del(id);// 验证成功 清理session key
                }
            }
            mav.addObject("uid", accountCollectAddr.getAccountId());
            mav.addObject("addr", accountCollectAddr.getCollectAddr());
            mav.addObject("status", accountCollectAddr.getIsActivate());
        }
        else
        {
            mav.addObject("uid", accountCollectAddr.getAccountId());
            mav.addObject("addr", accountCollectAddr.getCollectAddr());
            mav.addObject("status", "no");
        }
        return mav;
    }
    
    /**
     * 提币邮件激活操作
     * @param request
     * @return
     * @throws BusinessException
     */
    @RequestMapping("/withdraw/activeWithdraw")
    @ApiOperation(value = "提币邮件激活操作", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "uid", value = "帐户ID", required = true, paramType = "form"),
            @ApiImplicitParam(name = "op_id", value = "操作ID", required = true, paramType = "form"),
            @ApiImplicitParam(name = "id", value = "ID", required = true, paramType = "form")})
    public ModelAndView activeRaise(HttpServletRequest request) throws BusinessException
    {
        String id = request.getParameter("id");
        String uid = request.getParameter("uid");
        String oid = request.getParameter("op_id");
        boolean flag = true;
        if (StringUtils.isBlank(id) || StringUtils.isBlank(oid) || StringUtils.isBlank(uid))
        {// 判断会话和unid
            flag = false;
        }
        StringBuffer cacheKey = new StringBuffer(BitmsConst.MESSAGE).append(BitmsConst.SEPARATOR).append(id);
        String sessionId = RedisUtils.get(cacheKey.toString());
        if (StringUtils.isBlank(sessionId) || !StringUtils.equalsIgnoreCase(sessionId, oid))
        {// 判断会话是否超时或匹配
            flag = false;
        }
        AccountFundWithdraw accountFundWithdraw = accountFundWithdrawService.selectByPrimaryKey(Long.parseLong(id));
        Account account = accountService.selectByPrimaryKey(accountFundWithdraw.getAccountId());
        checkAccountDataValidate(account);
        logger.debug(accountFundWithdraw.toString());
        ModelAndView mav = new ModelAndView("fund/activeWithdraw");
        try
        {
            fundCurrentService.doActiveWithdraw(accountFundWithdraw);
        }
        catch (BusinessException e)
        {
            flag = false;
            logger.debug(e.getErrorCode().getMessage());
        }
        if (flag)
        {
            RedisUtils.del(id);// 验证成功 清理session key
            mav.addObject("status", "yes");
        }
        else
        {
            mav.addObject("status", "no");
        }
        return mav;
    }

    /**
     * 2018-01-16 pan让拿掉
     */
    public void checkSwitch()
    {
      /*  SysParameter params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(ParamConsts.WITHDRAW_SWITCH);
        params = sysParameterService.getSysParameterByEntity(params);
        if (params == null) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        if (!StringUtils.isBlank(params.getValue()))
        {
            if (!params.getValue().equals("yes")) { throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN); }
        }
        else
        {
            throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
        }*/
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
    
    private void checkAccountAssetDataValidate(Long accountId)
    {
        // (存储过程执行 returnCode：0 执行成功，-1 执行失败; 检查结果 chekResult 1.正常 ，小于0 异常; 提示信息 chekMsg)
        AcctAssetChkResult result = acctAssetChkService.doAcctAssetChk(accountId);
        if(result!=null && result.getReturnCode()!=null && result.getChekResult()!=null)
        {
            if (result.getReturnCode().intValue() != 1 || result.getChekResult().intValue() != 1)
            {
                String msg = result.getChekMsg() + "  accountId=" + accountId;
                throw new BusinessException(msg);
            }
        }
    }
    
    private void checkCollectAddrDataValidate(AccountCollectAddr accountCollectAddr)
    {
        if (null == accountCollectAddr) { throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR); }
        if (null != accountCollectAddr && !accountCollectAddr.verifySignature())
        {// 校验数据
            logger.debug("提币地址 数据校验失败");
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
        }
    }
    
    private void checkFundCurrentDataValidate(AccountFundCurrent accountFundCurrent)
    {
        if (null == accountFundCurrent) { throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR); }
        if (null != accountFundCurrent)
        {// 校验数据
            logger.info("资金流水 数据校验失败");
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
        }
    }
    
    private StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
}

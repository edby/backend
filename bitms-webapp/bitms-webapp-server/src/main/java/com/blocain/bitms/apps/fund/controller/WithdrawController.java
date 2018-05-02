package com.blocain.bitms.apps.fund.controller;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.trade.account.service.AccountPolicyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.apps.account.beans.SessionInfo;
import com.blocain.bitms.apps.basic.beans.AppsMessage;
import com.blocain.bitms.apps.basic.controller.AppsController;
import com.blocain.bitms.apps.basic.utils.AuthTokenUtils;
import com.blocain.bitms.apps.fund.model.*;
import com.blocain.bitms.apps.sdk.BitmsConstants;
import com.blocain.bitms.apps.sdk.internal.util.Encrypt;
import com.blocain.bitms.boss.common.service.MsgRecordNoSql;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.consts.DateConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.*;
import com.blocain.bitms.trade.account.consts.AccountConsts;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.entity.AccountCertification;
import com.blocain.bitms.trade.account.model.PolicyModel;
import com.blocain.bitms.trade.account.service.AccountCertificationService;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.*;
import com.blocain.bitms.trade.fund.enums.FundEnums;
import com.blocain.bitms.trade.fund.model.FundChangeModel;
import com.blocain.bitms.trade.fund.model.FundModel;
import com.blocain.bitms.trade.fund.service.*;
import com.blocain.bitms.trade.risk.model.EnableModel;
import com.blocain.bitms.trade.risk.service.EnableService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.entity.StockRate;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.stockinfo.service.StockRateService;

import io.swagger.annotations.ApiOperation;

/**
 * <p>Author：ChenGang</p>
 * <p>Description: WithdrawController</p>
 * <p>Date: Create in 18:31 2018/3/20</p>
 * <p>Modify By: ChenGang</p>
 *
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
public class WithdrawController extends AppsController {
    // 账户信息KEY: platscan_fundCurrent_[acctid]
    private static final String keyPrefix = new StringBuffer(CacheConst.REDIS_PLATSCAN_PREFIX).append(BitmsConst.SEPARATOR).append(BitmsConst.OP_FUND_ASSET)
            .append(BitmsConst.SEPARATOR).toString();

    @Autowired(required = false)
    private StockInfoService stockInfoService;

    @Autowired(required = false)
    private AccountCertificationService accountCertificationService;

    @Autowired(required = false)
    private EnableService enableService;

    @Autowired(required = false)
    private AccountCollectAddrService accountCollectAddrService;

    @Autowired(required = false)
    private AccountWithdrawRecordService accountWithdrawRecordService;

    @Autowired(required = false)
    private AccountCashWithdrawService accountCashWithdrawService;

    @Autowired(required = false)
    private AccountWithdrawRecordERC20Service accountWithdrawRecordERC20Service;

    @Autowired(required = false)
    private StockRateService stockRateService;

    @Autowired(required = false)
    private AccountService accountService;

    @Autowired(required = false)
    private FundCurrentService fundCurrentService;

    @Autowired(required = false)
    private FundScanService fundScanService;

    @Autowired(required = false)
    private AccountFundWithdrawService accountFundWithdrawService;

    @Autowired(required = false)
    private MsgRecordNoSql msgRecordService;

    @Autowired(required = false)
    private AccountCollectBankService accountCollectBankService;

    @Autowired(required = false)
    private AccountPolicyService accountPolicyService;

    @Autowired(required = false)
    private AccountCollectAddrERC20Service accountCollectAddrERC20Service;

    /**
     * Fund提币出金历史列表
     *
     * @param request
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/withdrawList")
    @ApiOperation(value = "Fund提币出金历史列表")//测试完成
    public AppsMessage withdrawList(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        WithdrawListModel withdrawListModel = checkSign(params, WithdrawListModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, withdrawListModel)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(withdrawListModel.getAuthToken());
        checkSesion(session,withdrawListModel.getAuthToken());
        Account account = checkAccount(session.getId());
        // 登录判断
        Long accountId = session.getId();
        if (null == accountId) {
            throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        }
        // 币种判断
        StockInfo stockInfo = new StockInfo();
        stockInfo.setRemark(withdrawListModel.getSymbol());
        List<StockInfo> stockInfos = stockInfoService.findList(stockInfo);
        if (stockInfos.size() == 0) {
            throw new BusinessException(FundEnums.ERROR_STOCKINFOID_NOT_EXIST);
        }
        stockInfo = stockInfos.get(0);
        // 判断分页参数
        String page = String.valueOf(withdrawListModel.getPage());
        String rows = String.valueOf(withdrawListModel.getRows());
        if (StringUtils.isBlank(page) || StringUtils.isBlank(rows)) {
            getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        Pagination pagin = new Pagination();
        pagin.setPage(Integer.parseInt(page));
        pagin.setRows(Integer.parseInt(rows));
        PaginateResult<AccountWithdrawRecordERC20> eRC20Result = null;
        // 数字货币
        if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_DIGITALCOIN)) {
            if (stockInfo.getId().longValue() == FundConsts.WALLET_BTC_TYPE.longValue()) {
                AccountWithdrawRecord accountWithdrawRecord = new AccountWithdrawRecord();
                accountWithdrawRecord.setStockinfoId(stockInfos.get(0).getId());
                accountWithdrawRecord.setAccountId(accountId);
                accountWithdrawRecord.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);// 钱包账户资产
                accountWithdrawRecord.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);// 钱包账户提现
                accountWithdrawRecord.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);// 过滤 资产减少
                logger.debug("withdrawList accountWithdrawRecord:" + accountWithdrawRecord.toString());
                PaginateResult<AccountWithdrawRecord> result = accountWithdrawRecordService.search(pagin, accountWithdrawRecord);
                for (AccountWithdrawRecord curr : result.getList()) {
                    if (curr.getWithdrawAddr() != null && !curr.getWithdrawAddr().equals("")) {
                        curr.setWithdrawAddr(EncryptUtils.desDecrypt(curr.getWithdrawAddr()));// des解密
                    }
                    if (null == curr.getConfirmStatus()) curr.setConfirmStatus("unconfirm");
                }
                String resultJson = JSON.toJSONString(result);
                String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
                message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
                return message;
            } else {
                throw new BusinessException(CommonEnums.ERROR_NOT_SUPPORT_WITHDRAW);
            }
            // ERC20
        } else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_ERC20_TOKEN)) {
            AccountWithdrawRecordERC20 accountWithdrawRecordERC20 = new AccountWithdrawRecordERC20();
            accountWithdrawRecordERC20.setStockinfoId(stockInfos.get(0).getId());
            accountWithdrawRecordERC20.setAccountId(accountId);
            accountWithdrawRecordERC20.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);// 钱包账户资产
            accountWithdrawRecordERC20.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);// 钱包账户提现
            accountWithdrawRecordERC20.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);// 过滤 资产减少
            logger.debug("withdrawList accountWithdrawRecordERC20:" + accountWithdrawRecordERC20.toString());
            eRC20Result = accountWithdrawRecordERC20Service.search(pagin, accountWithdrawRecordERC20);
            for (AccountWithdrawRecordERC20 curr : eRC20Result.getList()) {
                if (curr.getWithdrawAddr() != null && !curr.getWithdrawAddr().equals("")) {
                    curr.setWithdrawAddr(EncryptUtils.desDecrypt(curr.getWithdrawAddr()));// des解密
                }
                if (null == curr.getConfirmStatus()) curr.setConfirmStatus("unconfirm");
            }
        } else {
            throw new BusinessException(CommonEnums.ERROR_NOT_SUPPORT_RECHARGE);
        }
        String resultJson = JSON.toJSONString(eRC20Result);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }

    private void checkAccountDataValidate(Account account) {
        if (null == account) {
            throw new BusinessException(CommonEnums.ERROR_LOGIN_ACCOUNT);
        }
        if (account.getStatus().intValue() != AccountConsts.ACCOUNT_STATUS_NORMAL.intValue()) {
            throw new BusinessException(CommonEnums.ERROR_LOGIN_LOCK);
        }
        if (null != account && !account.verifySignature()) {// 校验数据
            logger.info("账户信息 数据校验失败");
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
        }
    }

    /**
     * 获取提现限额情况
     *
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/withdrawLimit")//测试完成
    public AppsMessage withdrawLimit(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        RecharegeModel tokenRequest = checkSign(params, RecharegeModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, tokenRequest)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(tokenRequest.getAuthToken());
        checkSesion(session,tokenRequest.getAuthToken());
        Account account = checkAccount(session.getId());
        // 登录判断
        Long accountId = session.getId();
        if (null == accountId) {
            throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        }
        // 判断当前币种是否存在
        StockInfo stockInfo = new StockInfo();
        stockInfo.setRemark(tokenRequest.getSymbol());
        List<StockInfo> list = stockInfoService.findList(stockInfo);
        if (list.size() == 0) {
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        Map<String, Object> map = new HashMap<>();
        stockInfo = list.get(0);
        if (!StringUtils.equalsIgnoreCase(stockInfo.getCanWithdraw(), FundConsts.PUBLIC_STATUS_YES)) {
            logger.debug("改币种暂时不支持提现！");
            throw new BusinessException(CommonEnums.ERROR_NOT_SUPPORT_WITHDRAW);
        }
        map.put("unauthUserWithdrawDayUpper", stockInfo.getUnauthUserWithdrawDayUpper());
        map.put("authUserWithdrawDayUpper", stockInfo.getAuthedUserWithdrawDayUpper());
        BigDecimal usedAmount = BigDecimal.ZERO;
        // 数字货币
        if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_DIGITALCOIN)) {
            if (stockInfo.getId().longValue() == FundConsts.WALLET_BTC_TYPE.longValue()) {
                AccountWithdrawRecord accountWithdrawRecord = new AccountWithdrawRecord();
                accountWithdrawRecord.setAccountId(accountId);
                accountWithdrawRecord.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
                String dateStr = CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMD);
                accountWithdrawRecord.setTimeStart(dateStr + " 00:00:00");
                accountWithdrawRecord.setTimeEnd(dateStr + " 23:59:59");
                logger.debug("accountWithdrawRecord:" + accountWithdrawRecord.toString());
                accountWithdrawRecord.setTableName(stockInfo.getTableFundCurrent());
                usedAmount = accountWithdrawRecordService.findSumAmtByAccount(accountWithdrawRecord);
            } else {
                throw new BusinessException(CommonEnums.ERROR_NOT_SUPPORT_WITHDRAW);
            }
        }
        // 现金
        else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_CASHCOIN)) {
            AccountCashWithdraw accountWithdrawRecord = new AccountCashWithdraw();
            accountWithdrawRecord.setAccountId(accountId);
            accountWithdrawRecord.setStockinfoId(stockInfo.getId());
            String dateStr = CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMD);
            accountWithdrawRecord.setTimeStart(dateStr + " 00:00:00");
            accountWithdrawRecord.setTimeEnd(dateStr + " 23:59:59");
            logger.debug("AccountWithdrawRecordERC20:" + accountWithdrawRecord.toString());
            usedAmount = accountCashWithdrawService.findSumAmtByAccount(accountWithdrawRecord);
        }
        // ERC20
        else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_ERC20_TOKEN)) {
            AccountWithdrawRecordERC20 accountWithdrawRecord = new AccountWithdrawRecordERC20();
            accountWithdrawRecord.setAccountId(accountId);
            accountWithdrawRecord.setStockinfoId(stockInfo.getId());
            String dateStr = CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMD);
            accountWithdrawRecord.setTimeStart(dateStr + " 00:00:00");
            accountWithdrawRecord.setTimeEnd(dateStr + " 23:59:59");
            logger.debug("AccountWithdrawRecordERC20:" + accountWithdrawRecord.toString());
            usedAmount = accountWithdrawRecordERC20Service.findSumAmtByAccount(accountWithdrawRecord);
        } else {
            throw new BusinessException(CommonEnums.ERROR_NOT_SUPPORT_WITHDRAW);
        }
        map.put("usedAmount", usedAmount);
        // 查询用户认证状态
        AccountCertification tempCertification = accountCertificationService.findByAccountId(accountId);
        boolean certStatus = false;
        if (null != tempCertification) {
            if (tempCertification.getStatus().intValue() == 1) {
                certStatus = true;
            }
        }
        if (certStatus) {
            map.put("surname", tempCertification.getSurname());
            map.put("realname", tempCertification.getRealname());
            map.put("regionId", tempCertification.getRegionId());
            map.put("passportId", tempCertification.getPassportId());
            map.put("authState", true);
            map.put("canUseAmount", stockInfo.getAuthedUserWithdrawDayUpper().subtract(usedAmount));
        } else {
            map.put("surname", null);
            map.put("realname", null);
            map.put("regionId", null);
            map.put("passportId", null);
            map.put("authState", false);
            map.put("canUseAmount", stockInfo.getUnauthUserWithdrawDayUpper().subtract(usedAmount));
        }
        String resultJson = JSON.toJSONString(map);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }

    /**
     * 提现手续费
     *
     * @param request
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/withdrawFee")//测试完成
    public AppsMessage withdrawFee(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        RecharegeModel recharegeModel = checkSign(params, RecharegeModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, recharegeModel)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(recharegeModel.getAuthToken());
        checkSesion(session,recharegeModel.getAuthToken());
        Account account = checkAccount(session.getId());
        // 登录判断
        Long accountId = session.getId();
        if (null == accountId) {
            throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        }
        // 判断当前币种是否存在
        StockInfo stockInfo = new StockInfo();
        stockInfo.setRemark(recharegeModel.getSymbol());
        List<StockInfo> list = stockInfoService.findList(stockInfo);
        if (list.size() == 0) {
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        stockInfo = list.get(0);
        StockRate rateEntity;
        if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_DIGITALCOIN)
                || StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_ERC20_TOKEN)) {
            StockRate stockRate = new StockRate();
            stockRate.setStockinfoId(stockInfo.getId());
            stockRate.setRateType(FundConsts.RATE_TYPE_WITHDRAW_FEE);
            List<StockRate> feerateList = stockRateService.findList(stockRate);
            if (feerateList.size() > 0) {
                rateEntity = feerateList.get(0);
            } else {
                throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
            }
        } else {
            throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
        }
        String resultJson = JSON.toJSONString(rateEntity);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }

    /**
     * Fund取消提币
     *
     * @param request
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "withdrawCancel")
    @ApiOperation(value = "Fund取消提币")
    public AppsMessage withdrawCancel(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        WithdrawCancelModel withdrawCancelModel = checkSign(params, WithdrawCancelModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, withdrawCancelModel)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(withdrawCancelModel.getAuthToken());
        checkSesion(session,withdrawCancelModel.getAuthToken());
        Account account = checkAccount(session.getId());
        // 登录判断
        Long accountId = session.getId();
        if (null == accountId) {
            throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        }
        // 判断当前币种是否存在
        StockInfo tempStockInfo = stockInfoService.selectByPrimaryKey(withdrawCancelModel.getStockInfoId());
        if (null == tempStockInfo) {
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        fundCurrentService.doWithdrawCancel(withdrawCancelModel.getId(), accountId, tempStockInfo.getId());
        // 刷新缓存
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        List<StockInfo> list = stockInfoService.findList(stockInfo);
        for (StockInfo stockInfo1 : list) {
            // 刷新缓存
            setAccountAssetCache(accountId, stockInfo1.getId(), stockInfo1.getId());
        }
        String resultJson = JSON.toJSONString(withdrawCancelModel);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }


    /**
     * Fund提币出金-确认
     *
     * @param request
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "withdrawConfirm")
    @ApiOperation(value = "Fund提币出金确认")
    public AppsMessage withdrawConfirm(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        WithdrawConfirmModel withdrawConfirmModel = checkSign(params, WithdrawConfirmModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, withdrawConfirmModel)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(withdrawConfirmModel.getAuthToken());
        checkSesion(session,withdrawConfirmModel.getAuthToken());
        Account account = checkAccount(session.getId());
        // 登录判断
        Long accountId = session.getId();
        if (null == accountId) {
            throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        }
        // 判断当前币种是否存在
        StockInfo tempStockInfo = new StockInfo();
        tempStockInfo.setRemark(withdrawConfirmModel.getSymbol());
        List<StockInfo> tempList = stockInfoService.findList(tempStockInfo);
        if (tempList.size() == 0) {
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        tempStockInfo = tempList.get(0);

        AccountFundWithdraw entity = accountFundWithdrawService.selectByPrimaryKey(Long.parseLong(withdrawConfirmModel.getId()));
        if (null == entity) {
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        if (accountId.longValue() != entity.getAccountId().longValue()) {
            throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        }
        if (StringUtils.isBlank(withdrawConfirmModel.getConfirmCode())) {
            logger.debug("验证码空");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        if (!StringUtils.equals(entity.getConfirmCode(), withdrawConfirmModel.getConfirmCode())) {
            throw new BusinessException(CommonEnums.ERROR_LOGIN_CAPTCHA);
        }
        String lang = CookieUtils.get(request, BitmsConst.COOKIE_LOCALE);
        if (StringUtils.isBlank(lang)) lang = LanguageUtils.getLang(request);
        lang = "en_US";
        //数字货币
        if (StringUtils.equalsIgnoreCase(tempStockInfo.getStockType(), FundConsts.STOCKTYPE_DIGITALCOIN)) {
            if (tempStockInfo.getId().longValue() == FundConsts.WALLET_BTC_TYPE.longValue()) {
                fundCurrentService.doComfirmWithdraw(entity, withdrawConfirmModel.getConfirmCode(), lang);
            } else {
                throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
            }
        } else if (StringUtils.equalsIgnoreCase(tempStockInfo.getStockType(), FundConsts.STOCKTYPE_ERC20_TOKEN)) {
            fundCurrentService.doComfirmWithdrawERC20(entity, withdrawConfirmModel.getConfirmCode(), lang);
        } else {
            throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
        }
        //需要用户邮件的信息
        msgRecordService.sendActiveWithdrawEmail(account.getEmail(), entity, lang);

        // 刷新缓存
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        List<StockInfo> list = stockInfoService.findList(stockInfo);
        for (StockInfo stockInfo1 : list) {
            // 刷新缓存
            clearAccountAssetCache(accountId, stockInfo1.getId(), stockInfo1.getId());
        }
        String resultJson = JSON.toJSONString(list);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }


    public void setAccountAssetCache(Long accountId, Long exchangePairVCoin, Long exchangePairMoney) {
        String key = new StringBuilder(keyPrefix).append(accountId).append(BitmsConst.SEPARATOR).append(exchangePairMoney).toString(); // changescan_fundCurrent_[acctid]
        logger.debug("getAccountFundAsset key=" + key);
        logger.debug("数据库中读取accountFundAsset");
        logger.debug("★★fundChangeScan key=" + key);
        FundChangeModel fundChangeModel = fundScanService.setAccountAssetAttr(accountId, exchangePairVCoin, exchangePairMoney);
        logger.debug("★★fundChangeScan fundChangeModel=" + fundChangeModel.toString());
        RedisUtils.putObject(key, fundChangeModel, CacheConst.DEFAULT_CACHE_TIME); // 缓存有效期限5分钟
    }

    public void clearAccountAssetCache(Long accountId, Long exchangePairVCoin, Long exchangePairMoney) {
        String key = new StringBuilder(keyPrefix).append(accountId).append(BitmsConst.SEPARATOR).append(exchangePairMoney).toString(); // changescan_fundCurrent_[acctid]
        RedisUtils.del(key);
    }

    /**
     * Fund提币出金-申请
     *
     * @param request
     * @return
     * @throws BusinessException
     * @author sunbiao  2017年7月13日 上午11:33:27
     */
    @ResponseBody
    @RequestMapping(value = "/withdrawApply")
    @ApiOperation(value = "Fund提币出金申请")
    public AppsMessage withdraw(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        Map<String, Object> map = new HashMap<String, Object>();
        if (!checkParams(params, message)) return message;// 验证公共参数
        WithDrawFundModel withDrawFundModel = checkSign(params, WithDrawFundModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, withDrawFundModel)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(withDrawFundModel.getAuthToken());
        checkSesion(session,withDrawFundModel.getAuthToken());
        Account account = checkAccount(session.getId());
        // 登录判断
        Long accountId = session.getId();
        if (null == accountId) {
            throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        }
        // 判断当前币种是否存在
        StockInfo stockInfo = new StockInfo();
        stockInfo.setRemark(withDrawFundModel.getSymbol());
        List<StockInfo> list = stockInfoService.findList(stockInfo);
        if (list.size() == 0) return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
//        Map<String, Object> maps = new HashMap<>();
        stockInfo = list.get(0);
        String stockType = stockInfo.getStockType();
        if (!StringUtils.equalsIgnoreCase(stockInfo.getCanWithdraw(), FundConsts.PUBLIC_STATUS_YES)) {
            logger.debug("改币种暂时不支持提现！");
            throw new BusinessException(CommonEnums.ERROR_NOT_SUPPORT_WITHDRAW);
        }
//        maps.put("unauthUserWithdrawDayUpper", stockInfo.getUnauthUserWithdrawDayUpper());
//        maps.put("authUserWithdrawDayUpper", stockInfo.getAuthedUserWithdrawDayUpper());
        BigDecimal usedAmount = BigDecimal.ZERO;

        String fundPwd = withDrawFundModel.getFundPwd();
        String activeStatus = FundConsts.PUBLIC_STATUS_YES;
        FundModel fundModel = new FundModel();
        fundModel.setAccountId(session.getId());
        fundModel.setAddress(withDrawFundModel.getCollectAddr());
        fundModel.setStockinfoId(stockInfo.getId());
        fundModel.setAmount(withDrawFundModel.getAmount());
//        PolicyModel policy = new PolicyModel();
//        org.apache.commons.beanutils.BeanUtils
        BeanUtils.copyProperties(withDrawFundModel, fundModel);
//        BeanUtils.copyProperties(withDrawFundModel, policy);
//        checkSwitch();// 查询开关
        logger.debug("/withdraw/withdraw page form = " + fundModel.toString());
        if (StringUtils.isBlank(fundPwd)) {
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        if (fundModel.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        // 小数位数判断
        NumericUtils.checkDecimalDigits("amount", fundModel.getAmount(), 4);
        // ---------用户数据验证 资金验证 安全策略验证 资金密码验证 start----------
//        checkAccountAssetDataValidate(account.getId());
//        if (!com.blocain.bitms.tools.utils.StringUtils.equalsIgnoreCase(activeStatus, "yes"))
//        {
//            accountPolicyService.validSecurityPolicy(account, policy);
//        }
        // 资金密码
        if (StringUtils.isBlank(account.getWalletPwd()))
            throw new BusinessException(CommonEnums.ERROR_WALLET_VALID_NOEXIST, null);
        boolean wallertpwdvalidate = EncryptUtils.validatePassword(String.valueOf(fundPwd), account.getWalletPwd());
        if (!wallertpwdvalidate) throw new BusinessException(CommonEnums.ERROR_WALLET_VALID_FAILED, null);
        switch (stockType) {
            case FundConsts.STOCKTYPE_DIGITALCOIN://digitalCoin
            {
                fundModel = modifyFundModel(fundModel, stockType);
                map = digitalCoinApply(stockInfo, fundModel, accountId, withDrawFundModel, request);
                break;
            }
            case FundConsts.STOCKTYPE_CASHCOIN://cashCoin
            {
                break;
            }
            case FundConsts.STOCKTYPE_ERC20_TOKEN://erc20Token
            {
                fundModel = modifyFundModel(fundModel, stockType);
                map = erc20WithdrawApply(stockInfo, fundModel, request);
                break;
            }
            default:
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        String resultJson = JSON.toJSONString(map);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }

    //计算费率
    public FundModel modifyFundModel(FundModel fundModel, String type) throws BusinessException {
        StockRate stockRate = new StockRate();
        switch (type) {
            case FundConsts.STOCKTYPE_DIGITALCOIN://digitalCoin
            {
                stockRate.setStockinfoId(fundModel.getStockinfoId());
                stockRate.setRateType(FundConsts.RATE_TYPE_WITHDRAW_FEE);
                break;
            }
            case FundConsts.STOCKTYPE_CASHCOIN://cashCoin
            {
                //cash未开放
                logger.debug("cashCoin类型的提币申请未开放");
                throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
            }
            case FundConsts.STOCKTYPE_ERC20_TOKEN://erc20Token
            {
                stockRate.setStockinfoId(fundModel.getStockinfoId());
                stockRate.setRateType(FundConsts.RATE_TYPE_WITHDRAW_FEE);
                break;
            }
            default:
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        List<StockRate> feerateList = stockRateService.findList(stockRate);
        BigDecimal feeRate = BigDecimal.ZERO;
        if (feerateList.size() > 0) {
            StockRate rateEntity = feerateList.get(0);
            if (rateEntity.getRate() != null) {
                feeRate = rateEntity.getRate();
            } else {
                throw new BusinessException("feeRate error:record null");
            }
        } else {
            throw new BusinessException("feeRate error:no record");
        }
        fundModel.setFee(feeRate);
        return fundModel;
    }

    public boolean cashWithdrawApply(Long accountId, AccountCashWithdraw accountCashWithdraw, String fundPwd, @ModelAttribute PolicyModel policy) throws BusinessException {
        logger.debug("/withdrawCash/withdrawCash page form = " + accountCashWithdraw.toString());
        FundModel fundModel = new FundModel();
        StockRate stockRate = new StockRate();
        stockRate.setStockinfoId(accountCashWithdraw.getStockinfoId());
        stockRate.setRateType(FundConsts.RATE_TYPE_WITHDRAW_FEE);
        List<StockRate> feeRateList = stockRateService.findList(stockRate);
        BigDecimal feeRate = BigDecimal.ZERO;
        if (feeRateList.size() > 0) {
            StockRate rateEntity = feeRateList.get(0);
            if (rateEntity.getRate() != null) {
                feeRate = rateEntity.getRate();
            } else {
                throw new BusinessException("feeRate error:record null");
            }
        } else {
            throw new BusinessException("feeRate error:no record");
        }
        fundModel.setFee(feeRate.multiply(accountCashWithdraw.getOccurAmt()));
        accountCashWithdraw.setFee(feeRate.multiply(accountCashWithdraw.getOccurAmt()));
        if (StringUtils.isBlank(fundPwd)) {
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        if (accountCashWithdraw.getOccurAmt().compareTo(BigDecimal.valueOf(100)) < 0
                || accountCashWithdraw.getOccurAmt().compareTo(BigDecimal.valueOf(100000)) > 0) {
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        // 小数位数判断
        fundModel.setAmount(accountCashWithdraw.getOccurAmt());
        NumericUtils.checkDecimalDigits("amount", fundModel.getAmount(), 2);
        // ---------用户数据验证 资金验证 安全策略验证 资金密码验证 start----------
        Account account = checkAccount(accountId);
        // 资金密码
        if (com.blocain.bitms.tools.utils.StringUtils.isBlank(account.getWalletPwd())) {
            throw new BusinessException(CommonEnums.ERROR_WALLET_VALID_NOEXIST, null);
        }
        boolean wallertpwdvalidate = EncryptUtils.validatePassword(String.valueOf(fundPwd), account.getWalletPwd());
        if (!wallertpwdvalidate) {
            throw new BusinessException(CommonEnums.ERROR_WALLET_VALID_FAILED, null);
        }
        // ---------用户数据验证 资金验证 安全策略验证 资金密码验证 end----------
        fundModel.setStockinfoId(accountCashWithdraw.getStockinfoId());
        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        accountCashWithdraw.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        fundModel.setAccountId(accountId);
        accountCashWithdraw.setAccountId(accountId);
        AccountCollectBank accountCollectBank = new AccountCollectBank();
        accountCollectBank.setAccountId(accountId);
        accountCollectBank.setStockinfoId(accountCashWithdraw.getId());
        List<AccountCollectBank> banklist = accountCollectBankService.findList(accountCollectBank);
        if (banklist.size() > 0) {
            checkCollectAddrDataValidate(banklist.get(0));
            fundModel.setAddress(banklist.get(0).getCardNo());
            accountCashWithdraw.setWithdrawCardNo(banklist.get(0).getCardNo());
            accountCashWithdraw.setWithdrawBankName(banklist.get(0).getBankName());
            accountCashWithdraw.setSwiftBic(banklist.get(0).getSwiftBic());
        } else {
            throw new BusinessException(CommonEnums.ERROR_INVALID_ADDRESS);
        }
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(accountId);
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        enableModel.setStockinfoId(accountCashWithdraw.getStockinfoId());
        enableModel.setRelatedStockinfoId(accountCashWithdraw.getStockinfoId());
        enableModel = enableService.entrustTerminalEnable(enableModel);
        if (enableModel.getEnableAmount().compareTo(fundModel.getAmount().add(fundModel.getFee())) < 0)
            throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
        account.setSecurityPolicy(AccountConsts.SECURITY_POLICY_NEEDGA);// GA
        accountPolicyService.validSecurityPolicy(account, policy);
        fundCurrentService.doApplyCashWithdraw(fundModel, accountCashWithdraw);

        // 刷新缓存
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        List<StockInfo> list = stockInfoService.findList(stockInfo);
        for (StockInfo stockInfo1 : list) {
            // 刷新缓存
            setAccountAssetCache(accountId, FundConsts.WALLET_BTC_TYPE, stockInfo1.getId());
        }
        return true;
    }

    public Map<String, Object> digitalCoinApply(StockInfo stockInfo, FundModel fundModel, Long accountId, WithDrawFundModel withDrawFundModel, HttpServletRequest request) throws BusinessException {
        Map<String, Object> map = new HashMap<>();
        String activeStatus = FundConsts.PUBLIC_STATUS_YES;
        // ---------用户数据验证 资金验证 安全策略验证 资金密码验证 end----------
        fundModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        String temp = fundModel.getAddress().toString();
        temp = temp.substring(temp.indexOf("_") + 1);
        fundModel.setAddress(temp);// address 有备注的内容，前台不好处理
        fundModel.setAccountId(accountId);
        String lang = CookieUtils.get(request, BitmsConst.COOKIE_LOCALE);
        if (StringUtils.isBlank(lang)) lang = LanguageUtils.getLang(request);
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(accountId);
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        enableModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);// btc
        enableModel.setRelatedStockinfoId(FundConsts.WALLET_BTC2USD_TYPE);
        enableModel = enableService.entrustTerminalEnable(enableModel);
        if (enableModel.getEnableAmount()
                .compareTo(fundModel.getAmount().add(fundModel.getFee())) < 0) {
            throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
        }
        AccountCollectAddr accountCollectAddr = new AccountCollectAddr();
        accountCollectAddr.setAccountId(accountId);
        accountCollectAddr.setCollectAddr(EncryptUtils.desEncrypt(fundModel.getAddress()));// des加密 加密以后才能查
        accountCollectAddr.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        accountCollectAddr.setIsActivate(FundConsts.PUBLIC_STATUS_YES);
        accountCollectAddr.setStatus(FundConsts.PUBLIC_STATUS_YES);
        accountCollectAddr = accountCollectAddrService.findAccountCollectAddr(accountCollectAddr);
        if (accountCollectAddr == null) {
            throw new BusinessException(FundEnums.ERROR_WITHDRAW_ADDRESS_NOT_EXIST);
        }
        checkCollectAddrDataValidate(accountCollectAddr);
        AccountFundWithdraw accountFundWithdraw = fundCurrentService.doApplyWithdraw(lang, fundModel, activeStatus, accountCollectAddr.getCertStatus());
        map.put("id", accountFundWithdraw.getId());
        map.put("withdrawAmt", accountFundWithdraw.getWithdrawAmt());
        map.put("netFee", accountFundWithdraw.getNetFee());
        map.put("remark", stockInfo.getRemark());//币种小写
        map.put("stockCode", stockInfo.getStockCode());//币种
        map.put("amount", fundModel.getAmount());//提币数量
        map.put("withdrawAddress", accountFundWithdraw.getWithdrawAddr());//提币数量
        map.put("image", "data:image/jpeg;base64," + ImageUtils.BufferedImageToBase64(
                ImageUtils.GraphicsToConfirmWithdrawBufferedImage(withDrawFundModel.getAmount(), withDrawFundModel.getCollectAddr(), accountFundWithdraw.getConfirmCode())));
        return map;
    }

    /**
     * ERC20提币提现申请
     *
     * @param stockInfo
     * @param fundModel
     * @param request
     * @return
     * @throws BusinessException
     */
    public Map<String, Object> erc20WithdrawApply(StockInfo stockInfo, FundModel fundModel, HttpServletRequest request) throws BusinessException {
        String activeStatus = FundConsts.PUBLIC_STATUS_YES;
        Long accountId = fundModel.getAccountId();
        logger.debug("/withdrawEth/withdrawEth page form = " + fundModel.toString());
        fundModel.setStockinfoIdEx(fundModel.getStockinfoId());
        // ---------用户数据验证 资金验证 安全策略验证 资金密码验证 end----------
        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        fundModel.setAccountId(accountId);
        String lang = CookieUtils.get(request, BitmsConst.COOKIE_LOCALE);
        if (StringUtils.isBlank(lang)) lang = LanguageUtils.getLang(request);
        String addressimg = "";
        AccountCollectAddrERC20 accountCollectBank = new AccountCollectAddrERC20();
        accountCollectBank.setAccountId(accountId);
        accountCollectBank.setStockinfoId(FundConsts.WALLET_ETH_TYPE); // TOKEN 共用一套地址
        List<AccountCollectAddrERC20> banklist = accountCollectAddrERC20Service.findList(accountCollectBank);
        if (banklist.size() > 0) {
            checkCollectAddrDataValidate(banklist.get(0));
            fundModel.setAddress(banklist.get(0).getCollectAddr());
            addressimg = EncryptUtils.desDecrypt(fundModel.getAddress());
        } else {
            throw new BusinessException(CommonEnums.ERROR_INVALID_ADDRESS);
        }
        if (!ERC20TokenCoinUtils.ValidateERC20TokenCoinAddress(addressimg)) {
            // 判断提币地址是否有效
            throw new BusinessException(CommonEnums.ERROR_INVALID_ADDRESS);
        }
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(accountId);
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        enableModel.setStockinfoId(fundModel.getStockinfoId());
        enableModel.setRelatedStockinfoId(fundModel.getStockinfoId());
        enableModel = enableService.entrustTerminalEnable(enableModel);
        if (enableModel.getEnableAmount()
                .compareTo(fundModel.getAmount().add(fundModel.getFee())) < 0) {
            throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        AccountFundWithdraw accountFundWithdraw = fundCurrentService.doApplyWithdrawERC20(lang, fundModel, activeStatus, accountCollectBank.getCertStatus());
        map.put("id", accountFundWithdraw.getId());
        map.put("withdrawAmt", accountFundWithdraw.getWithdrawAmt());
        map.put("netFee", accountFundWithdraw.getNetFee());
        map.put("remark", stockInfo.getRemark());//币种小写
        map.put("stockCode", stockInfo.getStockCode());//币种
        map.put("amount", fundModel.getAmount());//提币数量
        map.put("withdrawAddress", accountFundWithdraw.getWithdrawAddr());//提币数量
        map.put("image", "data:image/jpeg;base64," + ImageUtils.BufferedImageToBase64(
                ImageUtils.GraphicsToConfirmWithdrawBufferedImage(fundModel.getAmount(), addressimg, accountFundWithdraw.getConfirmCode(), stockInfo.getStockCode())));
        return map;
    }


    /**
     * @param request
     * @return
     * @author:yukai
     */
    @ResponseBody
    @RequestMapping("/withdrawCommonInfo")
    public AppsMessage getCommonWithDrawInfo(HttpServletRequest request) {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        RecharegeModel recharegeModel = checkSign(params, RecharegeModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, recharegeModel)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(recharegeModel.getAuthToken());
        checkSesion(session,recharegeModel.getAuthToken());
        Account account = checkAccount(session.getId());
        // 登录判断
        Long accountId = session.getId();
        if (null == accountId) {
            throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        }

        // 判断当前币种是否存在
        StockInfo stockInfo = new StockInfo();
        stockInfo.setRemark(recharegeModel.getSymbol());
        List<StockInfo> list = stockInfoService.findList(stockInfo);
        if (list.size() == 0) {
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        stockInfo = list.get(0);
        //调用得到数据
        //6.13
        StockRate stockRate = withdrawFee(stockInfo, accountId);
        Map<String, Object> mapStock = new HashMap<>();
        mapStock.put("rate", stockRate.getRate());
        mapStock.put("rateValueType", stockRate.getRateValueType());
        //6.14
        Map<String, Object> map = withdrawQuota(stockInfo, accountId);
        //6.15
        EnableModel enableModel = withdrawEnableAmount(stockInfo, accountId);
        Map<String, Object> mapEnable = new HashMap<>();
        mapEnable.put("enableAmount", enableModel.getEnableAmount());
        mapEnable.put("frozenAmt", enableModel.getFrozenAmt());

        WithDrawCommonModel withDrawCommonModel = new WithDrawCommonModel(mapStock, map, mapEnable);
        String resultJson = JSON.toJSONString(withDrawCommonModel);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }

    //6.13  提现手续费
    public StockRate withdrawFee(StockInfo stockInfo, Long accountId) throws BusinessException {
        StockRate rateEntity;
        if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_DIGITALCOIN)
                || StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_ERC20_TOKEN)) {
            StockRate stockRate = new StockRate();
            stockRate.setStockinfoId(stockInfo.getId());
            stockRate.setRateType(FundConsts.RATE_TYPE_WITHDRAW_FEE);
            List<StockRate> feerateList = stockRateService.findList(stockRate);
            if (feerateList.size() > 0) {
                rateEntity = feerateList.get(0);
            } else {
                throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
            }
        } else {
            throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
        }
        return rateEntity;
    }

    //6.14  提现限额
    public Map<String, Object> withdrawQuota(StockInfo stockInfo, Long accountId) throws BusinessException {
        Map<String, Object> map = new HashMap<>();
        if (!StringUtils.equalsIgnoreCase(stockInfo.getCanWithdraw(), FundConsts.PUBLIC_STATUS_YES)) {
            logger.debug("改币种暂时不支持提现！");
            throw new BusinessException(CommonEnums.ERROR_NOT_SUPPORT_WITHDRAW);
        }
        map.put("unauthUserWithdrawDayUpper", stockInfo.getUnauthUserWithdrawDayUpper());
        map.put("authUserWithdrawDayUpper", stockInfo.getAuthedUserWithdrawDayUpper());
        BigDecimal usedAmount = BigDecimal.ZERO;
        // 数字货币
        if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_DIGITALCOIN)) {
            if (stockInfo.getId().longValue() == FundConsts.WALLET_BTC_TYPE.longValue()) {
                AccountWithdrawRecord accountWithdrawRecord = new AccountWithdrawRecord();
                accountWithdrawRecord.setAccountId(accountId);
                accountWithdrawRecord.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
                String dateStr = CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMD);
                accountWithdrawRecord.setTimeStart(dateStr + " 00:00:00");
                accountWithdrawRecord.setTimeEnd(dateStr + " 23:59:59");
                logger.debug("accountWithdrawRecord:" + accountWithdrawRecord.toString());
                accountWithdrawRecord.setTableName(stockInfo.getTableFundCurrent());
                usedAmount = accountWithdrawRecordService.findSumAmtByAccount(accountWithdrawRecord);
            } else {
                throw new BusinessException(CommonEnums.ERROR_NOT_SUPPORT_WITHDRAW);
            }
        }
        // 现金
        else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_CASHCOIN)) {
            AccountCashWithdraw accountWithdrawRecord = new AccountCashWithdraw();
            accountWithdrawRecord.setAccountId(accountId);
            accountWithdrawRecord.setStockinfoId(stockInfo.getId());
            String dateStr = CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMD);
            accountWithdrawRecord.setTimeStart(dateStr + " 00:00:00");
            accountWithdrawRecord.setTimeEnd(dateStr + " 23:59:59");
            logger.debug("AccountWithdrawRecordERC20:" + accountWithdrawRecord.toString());
            usedAmount = accountCashWithdrawService.findSumAmtByAccount(accountWithdrawRecord);
        }
        // ERC20
        else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_ERC20_TOKEN)) {
            AccountWithdrawRecordERC20 accountWithdrawRecord = new AccountWithdrawRecordERC20();
            accountWithdrawRecord.setAccountId(accountId);
            accountWithdrawRecord.setStockinfoId(stockInfo.getId());
            String dateStr = CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMD);
            accountWithdrawRecord.setTimeStart(dateStr + " 00:00:00");
            accountWithdrawRecord.setTimeEnd(dateStr + " 23:59:59");
            logger.debug("AccountWithdrawRecordERC20:" + accountWithdrawRecord.toString());
            usedAmount = accountWithdrawRecordERC20Service.findSumAmtByAccount(accountWithdrawRecord);
        } else {
            throw new BusinessException(CommonEnums.ERROR_NOT_SUPPORT_WITHDRAW);
        }
        map.put("usedAmount", usedAmount);
        // 查询用户认证状态
        AccountCertification tempCertification = accountCertificationService.findByAccountId(accountId);
        boolean certStatus = false;
        if (null != tempCertification) {
            if (tempCertification.getStatus().intValue() == 1) {
                certStatus = true;
            }
        }
        if (certStatus) {
            map.put("surname", tempCertification.getSurname());
            map.put("realname", tempCertification.getRealname());
            map.put("regionId", tempCertification.getRegionId());
            map.put("passportId", tempCertification.getPassportId());
            map.put("authState", true);
            map.put("canUseAmount", stockInfo.getAuthedUserWithdrawDayUpper().subtract(usedAmount));
        } else {
            map.put("surname", null);
            map.put("realname", null);
            map.put("regionId", null);
            map.put("passportId", null);
            map.put("authState", false);
            map.put("canUseAmount", stockInfo.getUnauthUserWithdrawDayUpper().subtract(usedAmount));
        }
        return map;
    }

    //6.15  提现可用余额
    public EnableModel withdrawEnableAmount(StockInfo stockInfo, Long accountId) throws BusinessException {
        EnableModel enableModel = new EnableModel();
        if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_DIGITALCOIN)
                || StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_ERC20_TOKEN)) {
            enableModel.setAccountId(accountId);
            enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
            enableModel.setStockinfoId(stockInfo.getId());
            enableModel.setRelatedStockinfoId(FundConsts.WALLET_USD_TYPE);
            enableModel = enableService.entrustTerminalEnable(enableModel);
        } else {
            throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
        }
        return enableModel;
    }

    //btc校验地址
    private void checkCollectAddrDataValidate(AccountCollectAddr accountCollectAddr) {
        if (null == accountCollectAddr) {
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        if (null != accountCollectAddr && !accountCollectAddr.verifySignature()) {// 校验数据
            logger.debug("提币地址 数据校验失败");
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
        }
    }

    //cash校验地址
    private void checkCollectAddrDataValidate(AccountCollectBank accountCollectAddr) {
        if (null == accountCollectAddr) {
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        if (null != accountCollectAddr && !accountCollectAddr.verifySignature()) {// 校验数据
            logger.debug("提币地址 数据校验失败");
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
        }
    }

    //ERC20地址
    private void checkCollectAddrDataValidate(AccountCollectAddrERC20 accountCollectAddr) {
        if (null == accountCollectAddr) {
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        if (null != accountCollectAddr && !accountCollectAddr.verifySignature()) {// 校验数据
            logger.debug("提币地址 数据校验失败");
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
        }
    }

}

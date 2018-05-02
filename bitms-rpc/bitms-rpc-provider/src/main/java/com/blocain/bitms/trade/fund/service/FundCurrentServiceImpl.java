/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.bitpay.entity.BitpayKeychain;
import com.blocain.bitms.bitpay.entity.BitpayKeychainERC20;
import com.blocain.bitms.bitpay.service.BitpayKeychainERC20Service;
import com.blocain.bitms.bitpay.service.BitpayKeychainService;
import com.blocain.bitms.boss.common.consts.ParamConsts;
import com.blocain.bitms.boss.common.entity.SysParameter;
import com.blocain.bitms.boss.common.service.MsgRecordNoSql;
import com.blocain.bitms.boss.common.service.SysParameterService;
import com.blocain.bitms.boss.system.entity.UserInfo;
import com.blocain.bitms.boss.system.service.UserInfoService;
import com.blocain.bitms.monitor.entity.AcctAssetChkResult;
import com.blocain.bitms.monitor.service.AcctAssetChkService;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.payment.bitgo.model.BitPayModel;
import com.blocain.bitms.payment.bitgo.BitGoRemoteV2Service;
import com.blocain.bitms.payment.eth.Erc20TokenLocalService;
import com.blocain.bitms.payment.eth.EthLocalService;
import com.blocain.bitms.payment.eth.EtherscanRemoteService;
import com.blocain.bitms.quotation.entity.RtQuotationInfo;
import com.blocain.bitms.quotation.service.RtQuotationInfoService;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.consts.DateConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.google.Authenticator;
import com.blocain.bitms.tools.utils.*;
import com.blocain.bitms.trade.account.consts.AccountConsts;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.entity.AccountCertification;
import com.blocain.bitms.trade.account.enums.AccountEnums;
import com.blocain.bitms.trade.account.service.AccountCertificationService;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.*;
import com.blocain.bitms.trade.fund.enums.FundEnums;
import com.blocain.bitms.trade.fund.model.FundModel;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import org.jsr166.ConcurrentHashMap8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 提币申请
 * <p>File：RaiseServiceImpl.java</p>
 * <p>Title: RaiseServiceImpl</p>
 * <p>Description:RaiseServiceImpl</p>
 * <p>Copyright: Copyright (c) 2017年8月17日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Service
public class FundCurrentServiceImpl implements FundCurrentService
{
    public static final Logger                logger    = LoggerFactory.getLogger(FundCurrentServiceImpl.class);
    
    @Autowired(required = false)
    private AccountFundCurrentService         accountFundCurrentService;
    
    @Autowired(required = false)
    private AccountWithdrawRecordService      accountWithdrawRecordService;
    
    @Autowired(required = false)
    private AccountWithdrawRecordERC20Service accountWithdrawRecordERC20Service;
    
    @Autowired(required = false)
    private FundService                       fundService;
    
    @Autowired(required = false)
    private MsgRecordNoSql                    msgRecordService;
    
    @Autowired(required = false)
    private AccountService                    accountService;
    
    @Autowired(required = false)
    private SysParameterService               sysParameterService;
    
    @Autowired(required = false)
    private AccountCollectAddrService         accountCollectAddrService;
    
    @Autowired(required = false)
    private AccountFundAdjustService          accountFundAdjustService;
    
    @Autowired(required = false)
    private AccountFundCurrentService         accountCurrentService;
    
    @Autowired(required = false)
    private AccountFundTransferService        accountFundTransferService;
    
    @Autowired(required = false)
    private BitpayKeychainService             bitpayKeychainService;
    
    @Autowired(required = false)
    private BitpayKeychainERC20Service        bitpayKeychainERC20Service;
    
    @Autowired(required = false)
    private AcctAssetChkService               acctAssetChkService;
    
    @Autowired(required = false)
    private RedisTemplate                     redisTemplate;
    
    @Autowired(required = false)
    private BitGoRemoteV2Service              bitGoRemoteV2Service;
    
    @Autowired(required = false)
    private AccountFundWithdrawService        accountFundWithdrawService;
    
    @Autowired(required = false)
    private StockInfoService                  stockInfoService;
    
    @Autowired(required = false)
    private UserInfoService                   userInfoService;
    
    @Autowired(required = false)
    private AccountCertificationService       accountCertificationService;
    
    @Autowired(required = false)
    private AccountCashWithdrawService        accountCashWithdrawService;
    
    @Autowired(required = false)
    private AccountWalletAssetService         accountWalletAssetService;
    
    @Autowired
    private WalletCashTransferCurrentService  walletCashTransferCurrentService;
    
    @Autowired(required = false)
    private Erc20TokenLocalService            erc20TokenLocalService;
    
    @Autowired(required = false)
    private EthLocalService                   ethLocalService;
    
    @Autowired(required = false)
    private BlockTransConfirmERC20Service     blockTransConfirmERC20Service;
    
    @Autowired(required = false)
    private RtQuotationInfoService            rtQuotationInfoService;
    
    @Autowired(required = false)
    private EthAddrsService                   ethAddrsService;
    
    @Autowired(required = false)
    private EtherscanRemoteService            etherscanRemoteService;
    
    // 账户信息KEY: platscan_fundCurrent_[acctid]
    private static final String               keyPrefix = new StringBuffer(CacheConst.REDIS_PLATSCAN_PREFIX).append(BitmsConst.SEPARATOR).append(BitmsConst.OP_FUND_ASSET)
            .append(BitmsConst.SEPARATOR).toString();
    
    // 过期
    @Override
    public void doWithdraw(String lang, FundModel fundModel, String fundPwd, String activeStatus, String type, String checkCode)
    {
        Account account = accountService.selectByPrimaryKey(fundModel.getAccountId());
        checkAccountDataValidate(account);
        // 提币出金地址判断
        if (StringUtils.isBlank(fundModel.getAddress())) { throw new BusinessException(FundEnums.ERROR_WITHDRAW_ADDRESS_NOT_EXIST); }
        // 提币出金数量判断
        if (null == fundModel.getAmount()
                || fundModel.getAmount().compareTo(BigDecimal.ZERO) <= 0) { throw new BusinessException(FundEnums.ERROR_WITHDRAW_AMOUNT_GREATER_ZEOR); }
        // 提币出金费用判断
        if (null == fundModel.getFee()) { throw new BusinessException(FundEnums.ERROR_WITHDRAW_FEE_NOT_EXIST); }
        // 证券信息ID判断
        if (null == fundModel.getStockinfoId()) { throw new BusinessException(FundEnums.ERROR_STOCKINFOID_NOT_EXIST); }
        if (!BitcoinUtils.ValidateBitcoinAddress(fundModel.getAddress()))
        {
            // 判断提币地址是否有效
            throw new BusinessException(CommonEnums.ERROR_INVALID_ADDRESS);
        }
        // 检查完毕 进行des加密 存入current
        fundModel.setAddress(EncryptUtils.desEncrypt(fundModel.getAddress()));
        // 查询当日上限
        String paramName = "";
        if (fundModel.getStockinfoId().equals(FundConsts.WALLET_BTC_TYPE))
        {
            paramName = ParamConsts.WITHDRAW_TANSFER_BTC_DAY_QUOTAUPPER;
        }
        if (fundModel.getStockinfoId().equals(FundConsts.WALLET_LTC_TYPE))
        {
            paramName = ParamConsts.WITHDRAW_TANSFER_LTC_DAY_QUOTAUPPER;
        }
        if (fundModel.getStockinfoId().equals(FundConsts.WALLET_ETH_TYPE))
        {
            paramName = ParamConsts.WITHDRAW_TANSFER_ETH_DAY_QUOTAUPPER;
        }
        SysParameter params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(paramName);
        params = sysParameterService.getSysParameterByEntity(params);
        if (params == null) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        BigDecimal upLine = BigDecimal.valueOf(Double.parseDouble(params.getValue().toString()));
        logger.debug("raise upLine = " + upLine);
        // 当日已提现
        AccountFundCurrent accountFundCurrent = new AccountFundCurrent();
        accountFundCurrent.setAccountId(fundModel.getAccountId());
        accountFundCurrent.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        String dateStr = CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMD);
        accountFundCurrent.setTimeStart(dateStr + " 00:00:00");
        accountFundCurrent.setTimeEnd(dateStr + " 23:59:59");
        accountFundCurrent.setTableName(getStockInfo(fundModel.getStockinfoId()).getTableFundCurrent());
        java.math.BigDecimal usedAmt = accountFundCurrentService.findSumAmtByAccount(accountFundCurrent);
        logger.debug("already raised = " + usedAmt);
        if (upLine.setScale(12, BigDecimal.ROUND_HALF_UP).subtract(usedAmt).compareTo(fundModel.getAmount()) < 0)
        {
            logger.debug("已经超过当天最大额度");
            throw new BusinessException(CommonEnums.ERROR_GT_DAY_MAX_AMT);
        }
        if (!StringUtils.equalsIgnoreCase(activeStatus, "yes"))
        {
            if (StringUtils.equalsIgnoreCase(AccountConsts.ACCOUNT_VALID_SMS, type))
            {// 短信验证
                if (StringUtils.isBlank(account.getMobNo())) { throw new BusinessException(AccountEnums.ACCOUNT_PHONE_NOTBIND); }
                if (!msgRecordService.validSMSCode(new StringBuffer(account.getCountry()).append(account.getMobNo()).toString(), checkCode))
                {
                    // 判断验证码
                    // 开始记录操作次数
                    int count = 1;
                    String opCountKey = new StringBuffer(CacheConst.OPERATION_COUNT_PREFIX)// 加入缓存前缀
                            .append(BitmsConst.SEPARATOR).append(BitmsConst.OP_RAISE_DO_RAISE)// 加入模块标识
                            .append(BitmsConst.SEPARATOR).append(OnLineUserUtils.getId()).toString();
                    String opTimes = RedisUtils.get(opCountKey);
                    if (StringUtils.isNotBlank(opTimes))
                    {// 表示操作记数缓存中已经存在
                        count = Integer.valueOf(opTimes) + 1;
                        if (count >= BitmsConst.LOCK_INTERVAL_COUNT)
                        {// 操作频率达到30次时,锁定用户
                            accountService.modifyAccountStatusToFrozen(OnLineUserUtils.getId(), AccountConsts.FROZEN_REASON_DO_WITHDRAW);
                        }
                    }
                    RedisUtils.putObject(opCountKey, String.valueOf(count), CacheConst.ONE_HOUR_CACHE_TIME);
                    throw new BusinessException(AccountEnums.ACCOUNT_SMSCODE_ERROR);
                }
            }
            if (StringUtils.equalsIgnoreCase(AccountConsts.ACCOUNT_VALID_GA, type))
            {// 谷歌验证
                if (StringUtils.isBlank(account.getAuthKey())) { throw new BusinessException(CommonEnums.ERROR_GA_NOT_BIND); }
                Authenticator authenticator = new Authenticator();
                if (!authenticator.checkCode(account.getAuthKey(), Long.valueOf(checkCode)))
                {// 判断验证码
                 // 开始记录操作次数
                    int count = 1;
                    String opCountKey = new StringBuffer(CacheConst.OPERATION_COUNT_PREFIX)// 加入缓存前缀
                            .append(BitmsConst.SEPARATOR).append(BitmsConst.OP_RAISE_DO_RAISE)// 加入模块标识
                            .append(BitmsConst.SEPARATOR).append(OnLineUserUtils.getId()).toString();
                    String opTimes = RedisUtils.get(opCountKey);
                    if (StringUtils.isNotBlank(opTimes))
                    {// 表示操作记数缓存中已经存在
                        count = Integer.valueOf(opTimes) + 1;
                        if (count >= BitmsConst.LOCK_INTERVAL_COUNT)
                        {// 操作频率达到30次时,锁定用户
                            accountService.modifyAccountStatusToFrozen(OnLineUserUtils.getId(), AccountConsts.FROZEN_REASON_DO_WITHDRAW);
                        }
                    }
                    RedisUtils.putObject(opCountKey, String.valueOf(count), CacheConst.ONE_HOUR_CACHE_TIME);
                    throw new BusinessException(AccountEnums.ACCOUNT_GACODE_ERROR);
                }
            }
        }
        // 资金密码
        if (StringUtils.isBlank(account.getWalletPwd())) { throw new BusinessException(CommonEnums.ERROR_WALLET_VALID_NOEXIST, null); }
        boolean wallertpwdvalidate = EncryptUtils.validatePassword(String.valueOf(fundPwd), account.getWalletPwd());
        if (!wallertpwdvalidate) { throw new BusinessException(CommonEnums.ERROR_WALLET_VALID_FAILED, null); }
        try
        {
            fundModel.setAccountId(fundModel.getAccountId());
            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
            fundModel.setCreateBy(fundModel.getAccountId());
            logger.debug("raise fundModel:" + fundModel.toString());
            fundModel = fundService.fundTransaction(fundModel);
            logger.debug("raise fundModel after insert:" + fundModel.toString());
            AccountFundCurrent fundCurrent = new AccountFundCurrent();
            fundCurrent.setId(fundModel.getAccountFundCurrentId());
            fundCurrent.setAccountId(fundModel.getAccountId());
            accountFundCurrent.setTableName(getStockInfo(fundModel.getStockinfoId()).getTableFundCurrent());
            fundCurrent = accountFundCurrentService.selectByPrimaryKey(fundModel.getAccountFundCurrentId());
            logger.debug("99999999" + fundCurrent.toString());
            if (activeStatus.equals("no"))
            {// 未激活
             // 发邮件验证
                msgRecordService.sendActiveRaiseEmail(account.getEmail().toString(), fundCurrent, lang);
            }
            if (activeStatus.equals("no"))
            {// 未激活
                fundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_WAITING_EMAIL_CONFIRM);
            }
            else
            {// 已激活
                fundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING);
            }
            logger.debug("000000000000" + fundCurrent.toString());
            accountFundCurrent.setTableName(getStockInfo(fundModel.getStockinfoId()).getTableFundCurrent());
            accountFundCurrentService.updateByPrimaryKey(fundCurrent);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new BusinessException(FundEnums.ERROR_WITHDRAW_FAIL);
        }
    }
    
    @Override
    public void doRechargeERC20(FundModel fundModel, Long blockTransConfirmERC20Id) throws BusinessException
    {
        // 1.读取区块确认信息
        BlockTransConfirmERC20 blockTransConfirmERC20 = null;
        try
        {
            blockTransConfirmERC20 = blockTransConfirmERC20Service.selectByIdForUpdate(blockTransConfirmERC20Id);
        }
        catch (Exception e)
        {
            throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
        }
        // 2.最小充值额度判断
        StockInfo stockInfo = getStockInfo(fundModel.getStockinfoId());
        if (!StringUtils.equalsIgnoreCase(stockInfo.getCanRecharge(), FundConsts.PUBLIC_STATUS_YES))
        {
            logger.info(stockInfo.getStockCode() + "[" + stockInfo.getTokenContactAddr() + "]充值开关已关闭");
            throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
        }
        // 大于最小充值额度
        if (stockInfo.getSmallDepositFeeValue() == null || stockInfo.getSmallDepositStandardValue() == null) { throw new BusinessException("充值SDF有问题"); }
        // sdf<=充值金额<sdf上限
        if (stockInfo.getSmallDepositFeeValue().compareTo(BigDecimal.ZERO) > 0 && fundModel.getAmount().compareTo(stockInfo.getSmallDepositFeeValue()) > 0
                && fundModel.getAmount().compareTo(stockInfo.getSmallDepositStandardValue()) < 0)
        {
            // 3.充值资产处理
            fundService.fundTransaction(fundModel);
            // 4.充值手续费处理扣除 并 转移给超级用户
            FundModel accountFeeFundModel = new FundModel();
            accountFeeFundModel.setOriginalBusinessId(blockTransConfirmERC20Id);
            accountFeeFundModel.setAccountId(fundModel.getAccountId());
            accountFeeFundModel.setStockinfoId(fundModel.getStockinfoId());
            accountFeeFundModel.setStockinfoIdEx(fundModel.getStockinfoId());
            accountFeeFundModel.setAmount(stockInfo.getSmallDepositFeeValue());
            accountFeeFundModel.setAmountEx(stockInfo.getSmallDepositFeeValue());
            accountFeeFundModel.setFee(stockInfo.getSmallDepositFeeValue());// 充值手续费
            accountFeeFundModel.setTransId(fundModel.getTransId());
            accountFeeFundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETRECHARGE_SDF);
            fundService.fundTransaction(accountFeeFundModel);
            // 5.区块信息处理
            blockTransConfirmERC20.setStatus("confirm"); // 状态调整为已确认
            blockTransConfirmERC20Service.updateByPrimaryKey(blockTransConfirmERC20);
        }
        // 充值金额>=sdf上限
        else if (stockInfo.getSmallDepositStandardValue().compareTo(BigDecimal.ZERO) > 0 && fundModel.getAmount().compareTo(stockInfo.getSmallDepositStandardValue()) >= 0)
        {
            // 3.充值资产处理
            fundService.fundTransaction(fundModel);
            // 5.区块信息处理
            blockTransConfirmERC20.setStatus("confirm"); // 状态调整为已确认
            blockTransConfirmERC20Service.updateByPrimaryKey(blockTransConfirmERC20);
        }
        else
        {
            throw new BusinessException("充值金额小于等于SDF！不能入账处理！");
        }
        // 刷新缓存
        stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        List<StockInfo> list = stockInfoService.findList(stockInfo);
        for (StockInfo stockInfo1 : list)
        {
            // 刷新缓存
            clearAccountAssetCache(fundModel.getAccountId(), stockInfo1.getId(), stockInfo1.getId());
        }
    }
    
    protected void clearAccountAssetCache(Long accountId, Long exchangePairVCoin, Long exchangePairMoney)
    {
        String key = new StringBuilder(keyPrefix).append(accountId).append(BitmsConst.SEPARATOR).append(exchangePairMoney).toString(); // changescan_fundCurrent_[acctid]
        RedisUtils.del(key);
    }
    
    @Override
    public AccountFundWithdraw doApplyWithdraw(String lang, FundModel fundModel, String activeStatus, String certStatus) throws BusinessException
    {
        // 提币出金地址判断
        if (StringUtils.isBlank(fundModel.getAddress())) { throw new BusinessException(FundEnums.ERROR_WITHDRAW_ADDRESS_NOT_EXIST); }
        // 提币出金数量判断
        if (null == fundModel.getAmount()
                || fundModel.getAmount().compareTo(BigDecimal.ZERO) <= 0) { throw new BusinessException(FundEnums.ERROR_WITHDRAW_AMOUNT_GREATER_ZEOR); }
        // 提币出金费用判断
        if (null == fundModel.getFee()) { throw new BusinessException(FundEnums.ERROR_WITHDRAW_FEE_NOT_EXIST); }
        // 证券信息ID判断
        if (null == fundModel.getStockinfoId()) { throw new BusinessException(FundEnums.ERROR_STOCKINFOID_NOT_EXIST); }
        if (!BitcoinUtils.ValidateBitcoinAddress(fundModel.getAddress()))
        {
            // 判断提币地址是否有效
            throw new BusinessException(CommonEnums.ERROR_INVALID_ADDRESS);
        }
        // 检查完毕 进行des加密 存入current
        fundModel.setAddress(EncryptUtils.desEncrypt(fundModel.getAddress()));
        // 查询当日上限
        AccountCertification tempCertification = accountCertificationService.findByAccountId(fundModel.getAccountId());
        boolean certificationStatus = false;
        if (null != tempCertification)
        {
            if (tempCertification.getStatus().intValue() == 1)
            {
                certificationStatus = true;
            }
        }
        logger.debug("certificationStatus=" + certificationStatus);
        StockInfo info = stockInfoService.selectByPrimaryKey(fundModel.getStockinfoId());
        BigDecimal upLine = BigDecimal.ZERO;
        if (certificationStatus)
        {
            upLine = info.getAuthedUserWithdrawDayUpper();
        }
        // 未认证
        else
        {
            upLine = info.getUnauthUserWithdrawDayUpper();
        }
        logger.debug("raise upLine = " + upLine);
        // 当日已提现
        AccountWithdrawRecord accountWithdrawRecord = new AccountWithdrawRecord();
        accountWithdrawRecord.setAccountId(fundModel.getAccountId());
        accountWithdrawRecord.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        String dateStr = CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMD);
        accountWithdrawRecord.setTimeStart(dateStr + " 00:00:00");
        accountWithdrawRecord.setTimeEnd(dateStr + " 23:59:59");
        accountWithdrawRecord.setTableName(getStockInfo(fundModel.getStockinfoId()).getTableFundCurrent());
        java.math.BigDecimal usedAmt = accountWithdrawRecordService.findSumAmtByAccount(accountWithdrawRecord);
        logger.debug("already raised = " + usedAmt);
        if (upLine.setScale(12, BigDecimal.ROUND_HALF_UP).subtract(usedAmt).compareTo(fundModel.getAmount()) < 0)
        {
            logger.debug("已经超过当天最大额度");
            throw new BusinessException(CommonEnums.ERROR_GT_DAY_MAX_AMT);
        }
        try
        {
            fundModel.setAccountId(fundModel.getAccountId());
            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
            fundModel.setCreateBy(fundModel.getAccountId());
            logger.debug("raise fundModel:" + fundModel.toString());
            // 封装 提币临时
            AccountFundWithdraw entity = new AccountFundWithdraw();
            entity.setAccountId(fundModel.getAccountId());
            entity.setCreateBy(fundModel.getCreateBy());
            entity.setCreateDate(new Timestamp(System.currentTimeMillis()));
            entity.setNetFee(fundModel.getFee());
            entity.setStatus(FundConsts.FUND_WITHDRAW_APPLY_STATUS_CONFIRMING);
            entity.setStockinfoId(fundModel.getStockinfoId());
            entity.setWithdrawAddr(fundModel.getAddress());
            entity.setWithdrawAddrAuth(certStatus);
            entity.setWithdrawDate(new Timestamp(System.currentTimeMillis()));
            entity.setWithdrawAmt(fundModel.getAmount().setScale(8, RoundingMode.HALF_UP).add(fundModel.getFee()));
            entity = accountFundWithdrawService.insertAccountFundWithdraw(entity);
            return entity;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new BusinessException(FundEnums.ERROR_WITHDRAW_FAIL);
        }
    }
    
    @Override
    public void doComfirmWithdraw(AccountFundWithdraw accountFundWithdraw, String confirmCode, String lang) throws BusinessException
    {
        if (!org.apache.commons.lang3.StringUtils.equals(accountFundWithdraw.getConfirmCode(), confirmCode))
        {
            if (org.apache.commons.lang3.StringUtils.isBlank(confirmCode)) { throw new BusinessException(CommonEnums.ERROR_LOGIN_CAPTCHA); }
        }
        if (!StringUtils.equalsIgnoreCase(accountFundWithdraw.getStatus(), FundConsts.FUND_WITHDRAW_APPLY_STATUS_CONFIRMING))
        {// 必须是待确认状态的数据才可以确认
            logger.debug("状态错误");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        Account account = accountService.selectByPrimaryKey(accountFundWithdraw.getAccountId());
        // checkAccountDataValidate(account);
        // 提币出金地址判断
        if (StringUtils.isBlank(accountFundWithdraw.getWithdrawAddr())) { throw new BusinessException(FundEnums.ERROR_WITHDRAW_ADDRESS_NOT_EXIST); }
        // 提币出金数量判断
        if (null == accountFundWithdraw.getWithdrawAmt().setScale(12, BigDecimal.ROUND_HALF_UP).subtract(accountFundWithdraw.getNetFee())
                || accountFundWithdraw.getWithdrawAmt().setScale(12, BigDecimal.ROUND_HALF_UP).subtract(accountFundWithdraw.getNetFee()).compareTo(BigDecimal.ZERO) <= 0)
        {
            logger.debug("提币数量有异常");
            throw new BusinessException(FundEnums.ERROR_WITHDRAW_AMOUNT_GREATER_ZEOR);
        }
        // 提币出金费用判断
        if (null == accountFundWithdraw.getNetFee()
                || accountFundWithdraw.getNetFee().compareTo(BigDecimal.ZERO) <= 0) { throw new BusinessException(FundEnums.ERROR_WITHDRAW_FEE_NOT_EXIST); }
        // 证券信息ID判断
        if (null == accountFundWithdraw.getStockinfoId()) { throw new BusinessException(FundEnums.ERROR_STOCKINFOID_NOT_EXIST); }
        if (!BitcoinUtils.ValidateBitcoinAddress(EncryptUtils.desDecrypt(accountFundWithdraw.getWithdrawAddr())))
        {
            // 判断提币地址是否有效
            throw new BusinessException(CommonEnums.ERROR_INVALID_ADDRESS);
        }
        AccountCertification tempCertification = accountCertificationService.findByAccountId(accountFundWithdraw.getAccountId());
        boolean certificationStatus = false;
        if (null != tempCertification)
        {
            if (tempCertification.getStatus().intValue() == 1)
            {
                certificationStatus = true;
            }
        }
        logger.debug("certificationStatus=" + certificationStatus);
        StockInfo info = stockInfoService.selectByPrimaryKey(accountFundWithdraw.getStockinfoId());
        BigDecimal upLine = BigDecimal.ZERO;
        if (certificationStatus)
        {
            upLine = info.getAuthedUserWithdrawDayUpper();
        }
        // 未认证
        else
        {
            upLine = info.getUnauthUserWithdrawDayUpper();
        }
        logger.debug("raise upLine = " + upLine);
        // 当日已提现
        AccountWithdrawRecord accountWithdrawRecord = new AccountWithdrawRecord();
        accountWithdrawRecord.setAccountId(accountFundWithdraw.getAccountId());
        accountWithdrawRecord.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        String dateStr = CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMD);
        accountWithdrawRecord.setTimeStart(dateStr + " 00:00:00");
        accountWithdrawRecord.setTimeEnd(dateStr + " 23:59:59");
        accountWithdrawRecord.setTableName(getStockInfo(accountFundWithdraw.getStockinfoId()).getTableFundCurrent());
        java.math.BigDecimal usedAmt = accountWithdrawRecordService.findSumAmtByAccount(accountWithdrawRecord);
        logger.debug("already raised = " + usedAmt);
        if (upLine.setScale(12, BigDecimal.ROUND_HALF_UP).subtract(usedAmt)
                .compareTo(accountFundWithdraw.getWithdrawAmt().setScale(12, BigDecimal.ROUND_HALF_UP).subtract(accountFundWithdraw.getNetFee())) < 0)
        {
            logger.debug("已经超过当天最大额度");
            throw new BusinessException(CommonEnums.ERROR_GT_DAY_MAX_AMT);
        }
        if (!StringUtils.equalsIgnoreCase(accountFundWithdraw.getWithdrawAddrAuth(), FundConsts.WALLET_AUTH_STATUS_AUTH))
        {// 未激活
         // 发邮件验证
            msgRecordService.sendActiveWithdrawEmail(account.getEmail().toString(), accountFundWithdraw, lang);
        }
        if (!StringUtils.equalsIgnoreCase(accountFundWithdraw.getWithdrawAddrAuth(), FundConsts.WALLET_AUTH_STATUS_AUTH))
        {// 未激活
         // 认证过的地址
            accountFundWithdraw.setStatus(FundConsts.FUND_WITHDRAW_APPLY_STATUS_ACTIVATING);// 待邮件激活activating
            accountFundWithdrawService.updateByPrimaryKey(accountFundWithdraw);
        }
        else
        {// 已激活
            FundModel fundModel = new FundModel();
            fundModel.setAccountId(accountFundWithdraw.getAccountId());
            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
            fundModel.setCreateBy(accountFundWithdraw.getAccountId());
            fundModel.setAddress(accountFundWithdraw.getWithdrawAddr());
            fundModel.setAmount(accountFundWithdraw.getWithdrawAmt().setScale(12, BigDecimal.ROUND_HALF_UP).subtract(accountFundWithdraw.getNetFee()));
            fundModel.setCreateBy(accountFundWithdraw.getAccountId());
            fundModel.setFee(accountFundWithdraw.getNetFee());
            fundModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
            fundModel.setStockinfoIdEx(FundConsts.WALLET_BTC2USD_TYPE);
            logger.debug("raise fundModel:" + fundModel.toString());
            fundModel = fundService.fundTransaction(fundModel);
            logger.debug("raise fundModel after insert:" + fundModel.toString());
            AccountFundCurrent fundCurrent = new AccountFundCurrent();
            fundCurrent.setId(fundModel.getAccountFundCurrentId());
            fundCurrent.setAccountId(fundModel.getAccountId());
            fundCurrent.setTableName(getStockInfo(fundModel.getStockinfoId()).getTableFundCurrent());
            fundCurrent = accountFundCurrentService.selectByPrimaryKey(getStockInfo(fundModel.getStockinfoId()).getTableFundCurrent(), fundModel.getAccountFundCurrentId());
            logger.debug("insert " + fundCurrent.toString());
            fundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING);
            logger.debug("" + fundCurrent.toString());
            fundCurrent.setTableName(getStockInfo(fundModel.getStockinfoId()).getTableFundCurrent());
            accountFundCurrentService.updateByPrimaryKey(fundCurrent);
            // 认证过的地址
            accountFundWithdraw.setStatus(FundConsts.FUND_WITHDRAW_APPLY_STATUS_DONE);// 已处理done
            accountFundWithdrawService.updateByPrimaryKey(accountFundWithdraw);
        }
    }
    
    @Override
    public AccountFundWithdraw doApplyWithdrawERC20(String lang, FundModel fundModel, String activeStatus, String certStatus2) throws BusinessException
    {
        // 提币出金地址判断
        if (StringUtils.isBlank(fundModel.getAddress())) { throw new BusinessException(FundEnums.ERROR_WITHDRAW_ADDRESS_NOT_EXIST); }
        // 提币出金数量判断
        if (null == fundModel.getAmount()
                || fundModel.getAmount().compareTo(BigDecimal.ZERO) <= 0) { throw new BusinessException(FundEnums.ERROR_WITHDRAW_AMOUNT_GREATER_ZEOR); }
        // 提币出金费用判断
        if (null == fundModel.getFee()) { throw new BusinessException(FundEnums.ERROR_WITHDRAW_FEE_NOT_EXIST); }
        // 证券信息ID判断
        if (null == fundModel.getStockinfoId()) { throw new BusinessException(FundEnums.ERROR_STOCKINFOID_NOT_EXIST); }
        // 查询当日上限
        AccountCertification tempCertification = accountCertificationService.findByAccountId(fundModel.getAccountId());
        boolean certStatus = false;
        if (null != tempCertification)
        {
            if (tempCertification.getStatus().intValue() == 1)
            {
                certStatus = true;
            }
        }
        AccountWithdrawRecordERC20 accountWithdrawRecord = new AccountWithdrawRecordERC20();
        accountWithdrawRecord.setAccountId(fundModel.getAccountId());
        accountWithdrawRecord.setStockinfoId(fundModel.getStockinfoId());
        String dateStr = CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMD);
        accountWithdrawRecord.setTimeStart(dateStr + " 00:00:00");
        accountWithdrawRecord.setTimeEnd(dateStr + " 23:59:59");
        logger.debug("AccountWithdrawRecordERC20:" + accountWithdrawRecord.toString());
        java.math.BigDecimal usedAmt = accountWithdrawRecordERC20Service.findSumAmtByAccount(accountWithdrawRecord);
        BigDecimal limitAmount = BigDecimal.ZERO;
        StockInfo info = stockInfoService.selectByPrimaryKey(fundModel.getStockinfoId());
        // 已认证
        if (certStatus)
        {
            limitAmount = info.getAuthedUserWithdrawDayUpper().subtract(usedAmt);
        }
        // 未认证
        else
        {
            limitAmount = info.getUnauthUserWithdrawDayUpper().subtract(usedAmt);
        }
        if (limitAmount.setScale(12, BigDecimal.ROUND_HALF_UP).compareTo(fundModel.getAmount()) < 0)
        {
            logger.debug("已经超过当天最大额度");
            throw new BusinessException(CommonEnums.ERROR_GT_DAY_MAX_AMT);
        }
        try
        {
            fundModel.setAccountId(fundModel.getAccountId());
            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
            fundModel.setCreateBy(fundModel.getAccountId());
            logger.debug("raise fundModel:" + fundModel.toString());
            // 封装 提币临时
            AccountFundWithdraw entity = new AccountFundWithdraw();
            entity.setAccountId(fundModel.getAccountId());
            entity.setCreateBy(fundModel.getCreateBy());
            entity.setCreateDate(new Timestamp(System.currentTimeMillis()));
            entity.setNetFee(fundModel.getFee());
            entity.setStatus(FundConsts.FUND_WITHDRAW_APPLY_STATUS_CONFIRMING);
            entity.setStockinfoId(fundModel.getStockinfoId());
            entity.setWithdrawAddr(fundModel.getAddress());
            entity.setWithdrawAddrAuth(FundConsts.WALLET_AUTH_STATUS_AUTH);
            entity.setWithdrawDate(new Timestamp(System.currentTimeMillis()));
            entity.setWithdrawAmt(fundModel.getAmount().setScale(8, RoundingMode.HALF_UP).add(fundModel.getFee()));
            entity = accountFundWithdrawService.insertAccountFundWithdraw(entity);
            return entity;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new BusinessException(FundEnums.ERROR_WITHDRAW_FAIL);
        }
    }
    
    @Override
    public void doComfirmWithdrawERC20(AccountFundWithdraw accountFundWithdraw, String confirmCode, String lang) throws BusinessException
    {
        if (!org.apache.commons.lang3.StringUtils.equals(accountFundWithdraw.getConfirmCode(), confirmCode))
        {
            if (org.apache.commons.lang3.StringUtils.isBlank(confirmCode)) { throw new BusinessException(CommonEnums.ERROR_LOGIN_CAPTCHA); }
        }
        if (!StringUtils.equalsIgnoreCase(accountFundWithdraw.getStatus(), FundConsts.FUND_WITHDRAW_APPLY_STATUS_CONFIRMING))
        {// 必须是待确认状态的数据才可以确认
            logger.debug("状态错误");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        Account account = accountService.selectByPrimaryKey(accountFundWithdraw.getAccountId());
        // checkAccountDataValidate(account);
        // 提币出金地址判断
        if (StringUtils.isBlank(accountFundWithdraw.getWithdrawAddr())) { throw new BusinessException(FundEnums.ERROR_WITHDRAW_ADDRESS_NOT_EXIST); }
        // 提币出金数量判断
        if (null == accountFundWithdraw.getWithdrawAmt().setScale(12, BigDecimal.ROUND_HALF_UP).subtract(accountFundWithdraw.getNetFee())
                || accountFundWithdraw.getWithdrawAmt().setScale(12, BigDecimal.ROUND_HALF_UP).subtract(accountFundWithdraw.getNetFee()).compareTo(BigDecimal.ZERO) <= 0)
        {
            logger.debug("提币数量有异常");
            throw new BusinessException(FundEnums.ERROR_WITHDRAW_AMOUNT_GREATER_ZEOR);
        }
        // 提币出金费用判断
        if (null == accountFundWithdraw.getNetFee()
                || accountFundWithdraw.getNetFee().compareTo(BigDecimal.ZERO) <= 0) { throw new BusinessException(FundEnums.ERROR_WITHDRAW_FEE_NOT_EXIST); }
        // 证券信息ID判断
        if (null == accountFundWithdraw.getStockinfoId()) { throw new BusinessException(FundEnums.ERROR_STOCKINFOID_NOT_EXIST); }
        AccountCertification tempCertification = accountCertificationService.findByAccountId(accountFundWithdraw.getAccountId());
        boolean certStatus = false;
        if (null != tempCertification)
        {
            if (tempCertification.getStatus().intValue() == 1)
            {
                certStatus = true;
            }
        }
        AccountWithdrawRecordERC20 accountWithdrawRecord = new AccountWithdrawRecordERC20();
        accountWithdrawRecord.setAccountId(accountFundWithdraw.getAccountId());
        accountWithdrawRecord.setStockinfoId(accountFundWithdraw.getStockinfoId());
        String dateStr = CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMD);
        accountWithdrawRecord.setTimeStart(dateStr + " 00:00:00");
        accountWithdrawRecord.setTimeEnd(dateStr + " 23:59:59");
        logger.debug("AccountWithdrawRecordERC20:" + accountWithdrawRecord.toString());
        java.math.BigDecimal usedAmt = accountWithdrawRecordERC20Service.findSumAmtByAccount(accountWithdrawRecord);
        BigDecimal limitAmount = BigDecimal.ZERO;
        StockInfo info = stockInfoService.selectByPrimaryKey(accountFundWithdraw.getStockinfoId());
        // 已认证
        if (certStatus)
        {
            limitAmount = info.getAuthedUserWithdrawDayUpper().subtract(usedAmt);
        }
        // 未认证
        else
        {
            limitAmount = info.getUnauthUserWithdrawDayUpper().subtract(usedAmt);
        }
        if (limitAmount.setScale(12, BigDecimal.ROUND_HALF_UP).compareTo(accountFundWithdraw.getWithdrawAmt().subtract(accountFundWithdraw.getNetFee())) < 0)
        {
            logger.debug("已经超过当天最大额度");
            throw new BusinessException(CommonEnums.ERROR_GT_DAY_MAX_AMT);
        }
        FundModel fundModel = new FundModel();
        fundModel.setAccountId(accountFundWithdraw.getAccountId());
        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        fundModel.setCreateBy(accountFundWithdraw.getAccountId());
        fundModel.setAddress(accountFundWithdraw.getWithdrawAddr());
        fundModel.setAmount(accountFundWithdraw.getWithdrawAmt().setScale(12, BigDecimal.ROUND_HALF_UP).subtract(accountFundWithdraw.getNetFee()));
        fundModel.setCreateBy(accountFundWithdraw.getAccountId());
        fundModel.setFee(accountFundWithdraw.getNetFee());
        fundModel.setStockinfoId(accountFundWithdraw.getStockinfoId());
        fundModel.setStockinfoIdEx(accountFundWithdraw.getStockinfoId());
        logger.debug("raise fundModel:" + fundModel.toString());
        fundModel = fundService.fundTransaction(fundModel);
        logger.debug("raise fundModel after insert:" + fundModel.toString());
        AccountWithdrawRecordERC20 accountWithdrawRecordERC20 = new AccountWithdrawRecordERC20();
        accountWithdrawRecordERC20.setAccountId(fundModel.getAccountId());
        accountWithdrawRecordERC20.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        accountWithdrawRecordERC20.setAccountAssetId(fundModel.getOriginalBusinessId());
        accountWithdrawRecordERC20.setCurrentDate(new Date());
        accountWithdrawRecordERC20.setBusinessFlag(fundModel.getBusinessFlag());
        accountWithdrawRecordERC20.setStockinfoId(fundModel.getStockinfoId());
        accountWithdrawRecordERC20.setRelatedStockinfoId(fundModel.getStockinfoId());
        accountWithdrawRecordERC20.setContractAmt(BigDecimal.ZERO);
        accountWithdrawRecordERC20.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);
        accountWithdrawRecordERC20.setOccurAmt(fundModel.getAmount().add(fundModel.getFee()));
        accountWithdrawRecordERC20.setOrgAmt(BigDecimal.ZERO);
        accountWithdrawRecordERC20.setLastAmt(BigDecimal.ZERO);
        accountWithdrawRecordERC20.setForzenOrgAmt(BigDecimal.ZERO);
        accountWithdrawRecordERC20.setOccurForzenAmt(BigDecimal.ZERO);
        accountWithdrawRecordERC20.setForzenLastAmt(BigDecimal.ZERO);
        accountWithdrawRecordERC20.setConfirmStatus(FundConsts.WALLET_TRANS_STATUS_UNCONFIRM);
        accountWithdrawRecordERC20.setTransId(null);
        accountWithdrawRecordERC20.setChargeAddr(null);
        accountWithdrawRecordERC20.setWithdrawAddr(fundModel.getAddress());
        accountWithdrawRecordERC20.setNetFee(BigDecimal.ZERO);
        accountWithdrawRecordERC20.setFee(fundModel.getFee());
        accountWithdrawRecordERC20.setStatus(FundConsts.ACCOUNT_FUND_CURRENT_EFFECTIVE);
        accountWithdrawRecordERC20.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING);
        accountWithdrawRecordERC20.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_UNTRANSFER);
        accountWithdrawRecordERC20.setCollectStatus(FundConsts.WALLET_COLLECT_STATUS_NO);
        accountWithdrawRecordERC20.setCreateBy(fundModel.getAccountId());
        accountWithdrawRecordERC20.setCreateDate(new Timestamp(System.currentTimeMillis()));
        accountWithdrawRecordERC20.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountWithdrawRecordERC20.setCurrentDate(new Date());
        logger.debug("raise accountWithdrawRecordERC20:" + accountWithdrawRecordERC20.toString());
        accountWithdrawRecordERC20Service.insert(accountWithdrawRecordERC20);
        AccountFundCurrent fundCurrent = new AccountFundCurrent();
        fundCurrent.setId(fundModel.getAccountFundCurrentId());
        fundCurrent.setAccountId(fundModel.getAccountId());
        fundCurrent.setTableName(getStockInfo(fundModel.getStockinfoId()).getTableFundCurrent());
        fundCurrent = accountFundCurrentService.selectByPrimaryKey(getStockInfo(fundModel.getStockinfoId()).getTableFundCurrent(), fundModel.getAccountFundCurrentId());
        logger.debug("insert " + fundCurrent.toString());
        fundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING);
        logger.debug("" + fundCurrent.toString());
        fundCurrent.setTableName(getStockInfo(fundModel.getStockinfoId()).getTableFundCurrent());
        accountFundCurrentService.updateByPrimaryKey(fundCurrent);
        // 认证过的地址
        accountFundWithdraw.setStatus(FundConsts.FUND_WITHDRAW_APPLY_STATUS_DONE);// 已处理done
        accountFundWithdrawService.updateByPrimaryKey(accountFundWithdraw);
    }
    
    @Override
    public void doActiveWithdraw(AccountFundWithdraw accountFundWithdraw) throws BusinessException
    {
        if (!StringUtils.equalsIgnoreCase(accountFundWithdraw.getStatus(), FundConsts.FUND_WITHDRAW_APPLY_STATUS_ACTIVATING))
        {// 必须是待激活状态的数据才可以激活
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        Account account = accountService.selectByPrimaryKey(accountFundWithdraw.getAccountId());
        checkAccountDataValidate(account);
        // 提币出金地址判断
        if (StringUtils.isBlank(accountFundWithdraw.getWithdrawAddr())) { throw new BusinessException(FundEnums.ERROR_WITHDRAW_ADDRESS_NOT_EXIST); }
        // 提币出金数量判断
        if (null == accountFundWithdraw.getWithdrawAmt().setScale(12, BigDecimal.ROUND_HALF_UP).subtract(accountFundWithdraw.getNetFee())
                || accountFundWithdraw.getWithdrawAmt().setScale(12, BigDecimal.ROUND_HALF_UP).subtract(accountFundWithdraw.getNetFee())
                        .compareTo(BigDecimal.ZERO) <= 0) { throw new BusinessException(FundEnums.ERROR_WITHDRAW_AMOUNT_GREATER_ZEOR); }
        // 提币出金费用判断
        if (null == accountFundWithdraw.getNetFee()
                || accountFundWithdraw.getNetFee().compareTo(BigDecimal.ZERO) <= 0) { throw new BusinessException(FundEnums.ERROR_WITHDRAW_FEE_NOT_EXIST); }
        // 证券信息ID判断
        if (null == accountFundWithdraw.getStockinfoId()) { throw new BusinessException(FundEnums.ERROR_STOCKINFOID_NOT_EXIST); }
        if (!BitcoinUtils.ValidateBitcoinAddress(EncryptUtils.desDecrypt(accountFundWithdraw.getWithdrawAddr())))
        {
            // 判断提币地址是否有效
            throw new BusinessException(CommonEnums.ERROR_INVALID_ADDRESS);
        }
        // 查询当日上限
        String paramName = "";
        if (accountFundWithdraw.getStockinfoId().equals(FundConsts.WALLET_BTC_TYPE))
        {
            paramName = ParamConsts.WITHDRAW_TANSFER_BTC_DAY_QUOTAUPPER;
        }
        if (accountFundWithdraw.getStockinfoId().equals(FundConsts.WALLET_LTC_TYPE))
        {
            paramName = ParamConsts.WITHDRAW_TANSFER_LTC_DAY_QUOTAUPPER;
        }
        if (accountFundWithdraw.getStockinfoId().equals(FundConsts.WALLET_ETH_TYPE))
        {
            paramName = ParamConsts.WITHDRAW_TANSFER_ETH_DAY_QUOTAUPPER;
        }
        SysParameter params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(paramName);
        params = sysParameterService.getSysParameterByEntity(params);
        if (params == null) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        BigDecimal upLine = BigDecimal.valueOf(Double.parseDouble(params.getValue().toString()));
        logger.debug("raise upLine = " + upLine);
        // 当日已提现
        AccountFundCurrent accountFundCurrent = new AccountFundCurrent();
        accountFundCurrent.setAccountId(accountFundWithdraw.getAccountId());
        accountFundCurrent.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        String dateStr = CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMD);
        accountFundCurrent.setTimeStart(dateStr + " 00:00:00");
        accountFundCurrent.setTimeEnd(dateStr + " 23:59:59");
        accountFundCurrent.setTableName(getStockInfo(accountFundWithdraw.getStockinfoId()).getTableFundCurrent());
        java.math.BigDecimal usedAmt = accountFundCurrentService.findSumAmtByAccount(accountFundCurrent);
        logger.debug("already raised = " + usedAmt);
        if (upLine.setScale(12, BigDecimal.ROUND_HALF_UP).subtract(usedAmt)
                .compareTo(accountFundWithdraw.getWithdrawAmt().setScale(12, BigDecimal.ROUND_HALF_UP).subtract(accountFundWithdraw.getNetFee())) < 0)
        {
            logger.debug("已经超过当天最大额度");
            throw new BusinessException(CommonEnums.ERROR_GT_DAY_MAX_AMT);
        }
        FundModel fundModel = new FundModel();
        fundModel.setAccountId(accountFundWithdraw.getAccountId());
        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        fundModel.setCreateBy(accountFundWithdraw.getAccountId());
        fundModel.setAddress(accountFundWithdraw.getWithdrawAddr());
        fundModel.setAmount(accountFundWithdraw.getWithdrawAmt().setScale(12, BigDecimal.ROUND_HALF_UP).subtract(accountFundWithdraw.getNetFee()));
        fundModel.setCreateBy(accountFundWithdraw.getAccountId());
        fundModel.setFee(accountFundWithdraw.getNetFee());
        fundModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        logger.debug("raise fundModel:" + fundModel.toString());
        fundModel = fundService.fundTransaction(fundModel);
        logger.debug("raise fundModel after insert:" + fundModel.toString());
        AccountFundCurrent fundCurrent = new AccountFundCurrent();
        fundCurrent.setId(fundModel.getAccountFundCurrentId());
        fundCurrent.setAccountId(fundModel.getAccountId());
        fundCurrent.setTableName(getStockInfo(fundModel.getStockinfoId()).getTableFundCurrent());
        fundCurrent = accountFundCurrentService.selectByPrimaryKey(fundModel.getAccountFundCurrentId());
        logger.debug("insert " + fundCurrent.toString());
        fundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING);
        logger.debug("" + fundCurrent.toString());
        accountFundCurrentService.updateByPrimaryKey(fundCurrent);
        // 认证过的地址
        accountFundWithdraw.setStatus(FundConsts.FUND_WITHDRAW_APPLY_STATUS_DONE);// 已处理done
        accountFundWithdrawService.updateByPrimaryKey(accountFundWithdraw);
    }
    
    @Override
    public void doWithdrawCancel(Long id, Long accountId, Long exchangePairMoney) throws BusinessException
    {
        StockInfo stockInfo = getStockInfo(exchangePairMoney);
        // 现金货币取消提币
        if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_CASHCOIN))
        {
            AccountCashWithdraw accountCashWithdraw = null;
            try
            {
                accountCashWithdraw = accountCashWithdrawService.selectByIdForUpdate(id);
            }
            catch (Exception e)
            {
                throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
            }
            if (accountCashWithdraw == null) { throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED); }
            if (accountCashWithdraw.getAccountId().longValue() != accountId) { throw new BusinessException("用户越权！"); }
            if (!accountCashWithdraw.getApproveStatus()
                    .equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING)) { throw new BusinessException(CommonEnums.ERROR_LOGIN_TIMEOUT); }
            accountCashWithdraw.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CANCEL);
            FundModel fundModel = new FundModel();
            fundModel.setAccountId(accountId);
            fundModel.setStockinfoId(accountCashWithdraw.getStockinfoId());
            fundModel.setAmount(accountCashWithdraw.getOccurAmt().setScale(12, BigDecimal.ROUND_HALF_UP).subtract(accountCashWithdraw.getFee()));
            fundModel.setFee(accountCashWithdraw.getFee());
            fundModel.setCreateBy(accountId);
            fundModel.setAddress(accountCashWithdraw.getWithdrawCardNo());
            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW_CANCEL);
            fundService.fundTransaction(fundModel);
            int ret = accountCashWithdrawService.updateByPrimaryKey(accountCashWithdraw);
            if (ret == 0) { throw new BusinessException(CommonEnums.FAIL); }
        }
        // ETH ERC20
        else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_ERC20_TOKEN))
        {
            AccountWithdrawRecordERC20 accountWithdrawRecordERC20 = null;
            try
            {
                accountWithdrawRecordERC20 = accountWithdrawRecordERC20Service.selectByIdForUpdate(id);
            }
            catch (Exception e)
            {
                throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
            }
            if (accountWithdrawRecordERC20 == null) { throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED); }
            if (accountWithdrawRecordERC20.getAccountId().longValue() != accountId) { throw new BusinessException("用户越权！"); }
            if (!accountWithdrawRecordERC20.getApproveStatus()
                    .equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING)) { throw new BusinessException(CommonEnums.ERROR_LOGIN_TIMEOUT); }
            accountWithdrawRecordERC20.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CANCEL);
            FundModel fundModel = new FundModel();
            fundModel.setAccountId(accountId);
            fundModel.setStockinfoId(accountWithdrawRecordERC20.getStockinfoId());
            fundModel.setAmount(accountWithdrawRecordERC20.getOccurAmt().setScale(12, BigDecimal.ROUND_HALF_UP).subtract(accountWithdrawRecordERC20.getFee()));
            fundModel.setFee(accountWithdrawRecordERC20.getFee());
            fundModel.setCreateBy(accountId);
            fundModel.setAddress(accountWithdrawRecordERC20.getWithdrawAddr());
            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW_CANCEL);
            fundService.fundTransaction(fundModel);
            int ret = accountWithdrawRecordERC20Service.updateByPrimaryKey(accountWithdrawRecordERC20);
            if (ret == 0) { throw new BusinessException(CommonEnums.FAIL); }
        }
        // 数字货币取消提币
        else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_DIGITALCOIN))
        {
            AccountWithdrawRecord oldAccountFundCurrent = accountWithdrawRecordService.selectByPrimaryKey(getStockInfo(exchangePairMoney).getTableFundCurrent(), id);
            // checkFundCurrentDataValidate(oldAccountFundCurrent);
            if (oldAccountFundCurrent.getAccountId().longValue() != accountId) { throw new BusinessException("用户越权！"); }
            // 加锁
            String lock = new StringBuffer(CacheConst.LOCK_PERFIX).append(BitmsConst.OP_FUND_CURRENT)// 加入模块标识
                    .append(oldAccountFundCurrent.getId()).toString();
            RedisLock redisLock = new RedisLock(redisTemplate, lock);
            if (redisLock.lock())
            {
                try
                {
                    if (oldAccountFundCurrent.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING)
                            || oldAccountFundCurrent.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_WAITING_EMAIL_CONFIRM))
                    {
                    }
                    else
                    {
                        throw new BusinessException(CommonEnums.ERROR_LOGIN_TIMEOUT);
                    }
                    oldAccountFundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CANCEL);
                    FundModel fundModel = new FundModel();
                    fundModel.setAccountId(accountId);
                    fundModel.setStockinfoId(oldAccountFundCurrent.getStockinfoId());
                    fundModel.setAmount(oldAccountFundCurrent.getOccurAmt().setScale(12, BigDecimal.ROUND_HALF_UP).subtract(oldAccountFundCurrent.getNetFee()));
                    fundModel.setFee(oldAccountFundCurrent.getNetFee());
                    fundModel.setCreateBy(accountId);
                    fundModel.setAddress(oldAccountFundCurrent.getWithdrawAddr());
                    fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW_CANCEL);
                    fundService.fundTransaction(fundModel);
                    oldAccountFundCurrent.setTableName(getStockInfo(fundModel.getStockinfoId()).getTableFundCurrent());
                    int ret = accountWithdrawRecordService.updateByPrimaryKey(oldAccountFundCurrent);
                    if (ret == 0) { throw new BusinessException(CommonEnums.FAIL); }
                }
                catch (BusinessException e)
                {
                    logger.error("doRaiseCancel错误:" + e.getMessage(), e);
                    throw new BusinessException(e.getErrorCode());
                }
                finally
                {
                    redisLock.unlock();
                }
            }
        }
        else
        {
            throw new BusinessException("error stockType:" + stockInfo.getStockType());
        }
    }
    
    @Override
    public void doWithdrawAddrAdd(String lang, AccountCollectAddr accountCollectAddr) throws BusinessException
    {
        AccountCollectAddr entity = new AccountCollectAddr();
        entity.setAccountId(accountCollectAddr.getAccountId());
        List<AccountCollectAddr> list = accountCollectAddrService.findList(entity);
        for (AccountCollectAddr accountCollectAddr1 : list)
        {
            accountCollectAddrService.remove(accountCollectAddr1.getId());
        }
        Account account = accountService.selectByPrimaryKey(accountCollectAddr.getAccountId());
        if (!StringUtils.equalsIgnoreCase(accountCollectAddr.getCertStatus(), FundConsts.WALLET_AUTH_STATUS_AUTH))
        {
            accountCollectAddr.setCertStatus(FundConsts.WALLET_AUTH_STATUS_UNAUTH);
        }
        if (!BitcoinUtils.ValidateBitcoinAddress(accountCollectAddr.getCollectAddr()))
        {
            // 判断提币地址是否有效
            throw new BusinessException(CommonEnums.ERROR_INVALID_ADDRESS);
        }
        // 检查完毕加密
        accountCollectAddr.setCollectAddr(EncryptUtils.desEncrypt(accountCollectAddr.getCollectAddr()));
        // 邮箱验证
        accountCollectAddr.setCreateDate(new Timestamp(System.currentTimeMillis()));
        // 审核状态 默认为no
        // accountCollectAddr.setStatus(FundConsts.PUBLIC_STATUS_NO);
        accountCollectAddr.setStatus(FundConsts.PUBLIC_STATUS_YES);
        accountCollectAddr.setIsActivate(FundConsts.PUBLIC_STATUS_YES);
        if (!StringUtils.equalsIgnoreCase(accountCollectAddr.getCertStatus(), FundConsts.WALLET_AUTH_STATUS_AUTH))
        {// 未认证
            accountCollectAddr.setIsActivate(FundConsts.PUBLIC_STATUS_YES);
            accountCollectAddrService.insertAccountCollectAddr(accountCollectAddr);
        }
        else
        {// 已认证
            accountCollectAddr.setIsActivate(FundConsts.PUBLIC_STATUS_YES);
            // 发邮件验证
            accountCollectAddr.setId(accountCollectAddrService.insertAccountCollectAddr(accountCollectAddr));
            // msgRecordService.sendActiveCollectAddrEmail(account.getEmail(), accountCollectAddr, lang);
        }
    }
    
    @Override
    public void doSaveAccountFundAdjust(AccountFundAdjust accountFundAdjust, FundModel fundModel) throws BusinessException
    {
        UserInfo user = userInfoService.selectByPrimaryKey(fundModel.getCreateBy());
        if (user == null) { throw new BusinessException("用户越权！"); }
        String lock = new StringBuffer(CacheConst.LOCK_PERFIX).append(BitmsConst.OP_FUND_ADJUST)// 加入模块标识
                .append(accountFundAdjust.getCreateBy()).toString();
        RedisLock redisLock = new RedisLock(redisTemplate, lock);
        if (redisLock.lock())
        {
            try
            {
                Long id = SerialnoUtils.buildPrimaryKey();
                if (null == accountFundAdjust.getId())
                {
                    accountFundAdjust.setId(id);
                }
                fundModel.setOriginalBusinessId(id);
                // 非超级账户要进行账户数据验签
                if (!FundConsts.SYSTEM_ACCOUNT_ID.equals(fundModel.getAccountId()))
                {
                    Account account = accountService.selectByPrimaryKey(fundModel.getAccountId());
                    checkAccountDataValidate(account);
                }
                if (StringUtils.equalsIgnoreCase(accountFundAdjust.getNeedLock(), FundConsts.PUBLIC_STATUS_YES))
                {
                    accountFundAdjust.setLockStatus(FundConsts.PUBLIC_STATUS_YES);
                }
                else
                {
                    fundService.fundTransaction(fundModel);
                    accountFundAdjust.setLockStatus(FundConsts.PUBLIC_STATUS_NO);
                }
                logger.debug("插入强增信息前：" + accountFundAdjust.toString());
                accountFundAdjustService.insert(accountFundAdjust);
            }
            catch (BusinessException e)
            {
                logger.error("adjust 错误:" + e.getMessage(), e);
                throw new BusinessException(e.getErrorCode());
            }
            finally
            {
                redisLock.unlock();
            }
        }
        else
        {
            logger.error("adjust acquireLock failed");
            throw new BusinessException(CommonEnums.FAIL);
        }
    }
    
    @Override
    public void doAllot(String ids, Long accountId)
    {
        String lock = new StringBuffer(CacheConst.LOCK_PERFIX).append(BitmsConst.OP_FUND_CURRENT)// 加入模块标识
                .append(accountId).toString();
        RedisLock redisLock = new RedisLock(redisTemplate, lock);
        if (redisLock.lock())
        {
            try
            {
                String[] split = ids.split(",");
                for (String id : split)
                {
                    BitpayKeychain bitpayKeychain = new BitpayKeychain();
                    bitpayKeychain.setType(2);
                    List<BitpayKeychain> list = bitpayKeychainService.findList(bitpayKeychain);
                    if (list.size() < 1)
                    {
                        // 缺少系统钱包id
                        throw new BusinessException(CommonEnums.FAIL);
                    }
                    bitpayKeychain = list.get(0);
                    AccountFundTransfer trans = new AccountFundTransfer();
                    AccountFundCurrent fund = accountCurrentService.selectByPrimaryKey(Long.parseLong(id));
                    // checkFundCurrentDataValidate(fund);
                    Account account = accountService.selectByPrimaryKey(fund.getAccountId());
                    checkAccountDataValidate(account);
                    if (StringUtils.isBlank(fund.getWithdrawAddr()))
                    {
                        // 目标地址空
                        throw new BusinessException(CommonEnums.FAIL);
                    }
                    trans.setOriginalCurrentId(fund.getId());
                    trans.setAccountId(fund.getAccountId());
                    trans.setUserinfoId(accountId);
                    trans.setStockinfoId(fund.getStockinfoId());
                    // 源地址 系统
                    trans.setSrcWalletId(bitpayKeychain.getWalletId());
                    // trans.setSrcWalletAddr(systemWallet.getWalletAddr());
                    trans.setTargetWalletAddr(fund.getWithdrawAddr());
                    trans.setTransferTime(new Timestamp(System.currentTimeMillis()));
                    trans.setTransferAmt(fund.getOccurAmt().subtract(fund.getNetFee()));
                    trans.setTransferFee(fund.getNetFee());
                    trans.setTransId(fund.getTransId());
                    trans.setConfirmStatus(FundConsts.WALLET_TRANS_STATUS_UNCONFIRM); // 未确认
                    trans.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFERPENDING); // 待划拨
                    trans.setRemark("");
                    accountFundTransferService.insert(trans);
                    accountCurrentService.updateByPrimaryKeySelective(fund);
                }
            }
            catch (BusinessException e)
            {
                logger.error("allot 错误:" + e.getMessage(), e);
                throw new BusinessException(e.getErrorCode());
            }
            finally
            {
                redisLock.unlock();
            }
        }
        else
        {
            logger.error("alot acquireLock failed");
            throw new BusinessException(CommonEnums.FAIL);
        }
    }
    
    @Override
    public void doApproval(AccountWithdrawRecord accountWithdrawRecord, Long bossUserId, String otp) throws BusinessException
    {
        String lock = new StringBuffer(CacheConst.LOCK_PERFIX).append(BitmsConst.OP_FUND_CURRENT)// 加入模块标识
                .append(accountWithdrawRecord.getId()).toString();
        RedisLock redisLock = new RedisLock(redisTemplate, lock);
        if (redisLock.lock())
        {
            try
            {
                AccountWithdrawRecord curr = accountWithdrawRecordService.selectByPrimaryKey(getStockInfo(accountWithdrawRecord.getStockinfoId()).getTableFundCurrent(),
                        accountWithdrawRecord.getId());
                curr.setTableName(getStockInfo(accountWithdrawRecord.getStockinfoId()).getTableFundCurrent());
                checkFundCurrentDataValidate(curr);
                if (!curr.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING)
                        && !curr.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKPENDING)) { throw new BusinessException("state error"); }
                if (accountWithdrawRecord.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH)
                        || accountWithdrawRecord.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKREJECT))
                {// 复核人员记录
                    curr.setCheckBy(bossUserId);
                    curr.setCheckDate(new Timestamp(System.currentTimeMillis()));
                }
                else
                {// 非复核人员
                    curr.setAuditBy(bossUserId);
                    curr.setAuditDate(new Timestamp(System.currentTimeMillis()));
                }
                if (accountWithdrawRecord.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKREJECT)
                        || accountWithdrawRecord.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPREJECT))
                {
                    // 如果以后 采用复核和划拨 此行删除 -------start-------
                    curr.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPREJECT);
                    // 如果以后 采用复核和划拨 此行删除 -------end-------
                    // 审批拒绝或审核拒绝，调用接口
                    if (StringUtils.isBlank(curr.getWithdrawAddr()))
                    {
                        // 目标地址空
                        throw new BusinessException(CommonEnums.FAIL);
                    }
                    FundModel fundModel = new FundModel();
                    fundModel.setAccountId(curr.getAccountId());
                    fundModel.setAddress(curr.getWithdrawAddr());
                    fundModel.setAmount(curr.getOccurAmt().subtract(curr.getNetFee()));
                    fundModel.setFee(curr.getNetFee());
                    fundModel.setCreateBy(bossUserId);
                    fundModel.setStockinfoId(curr.getStockinfoId());
                    fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW_REJECT);
                    // 做业务取消
                    fundService.fundTransaction(fundModel);
                }
                // ------------------------------------直接划拨 如果以后恢复【复核和划拨】 这一段删掉 start------------------------------
                if (accountWithdrawRecord.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH)
                        || accountWithdrawRecord.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKPENDING))
                {
                    BitpayKeychain bitpayKeychain = new BitpayKeychain();
                    bitpayKeychain.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
                    bitpayKeychain.setType(2);
                    List<BitpayKeychain> list = bitpayKeychainService.findList(bitpayKeychain);
                    if (list.size() < 1)
                    {
                        // 缺少系统钱包id
                        throw new BusinessException(CommonEnums.FAIL);
                    }
                    bitpayKeychain = list.get(0);
                    checkFundCurrentDataValidate(curr);
                    Account account = accountService.selectByPrimaryKey(curr.getAccountId());
                    checkAccountDataValidate(account);
                    if (StringUtils.isBlank(curr.getWithdrawAddr()))
                    {
                        // 目标地址空
                        throw new BusinessException(CommonEnums.FAIL);
                    }
                    Long id = SerialnoUtils.buildPrimaryKey();
                    AccountFundTransfer trans = new AccountFundTransfer();
                    trans.setId(id);
                    trans.setOriginalCurrentId(curr.getId());
                    trans.setAccountId(curr.getAccountId());
                    trans.setUserinfoId(bossUserId);
                    trans.setStockinfoId(curr.getStockinfoId());
                    // 源地址 系统
                    trans.setSrcWalletId(bitpayKeychain.getWalletId());
                    // trans.setSrcWalletAddr(systemWallet.getWalletAddr());
                    trans.setTargetWalletAddr(curr.getWithdrawAddr());
                    trans.setTransferTime(new Timestamp(System.currentTimeMillis()));
                    trans.setTransferAmt(curr.getOccurAmt().subtract(curr.getNetFee()));
                    trans.setTransferFee(curr.getNetFee());
                    trans.setTransId(curr.getTransId());
                    trans.setConfirmStatus(FundConsts.WALLET_TRANS_STATUS_UNCONFIRM); // 未确认
                    trans.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFERPENDING); // 待划拨
                    trans.setRemark("");
                    accountFundTransferService.insert(trans);
                    curr.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH);
                    curr.setCollectStatus(FundConsts.WALLET_COLLECT_STATUS_NO);
                    curr.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFERPENDING); // 待划拨
                    // -----------------------------------调用单笔发送 start--------------------------------------------
                    String password = EncryptUtils.desDecrypt(bitpayKeychain.getSystemPass());
                    BitPayModel bitPayModel = bitGoRemoteV2Service.sendCoins(bitpayKeychain.getWalletId(), EncryptUtils.desDecrypt(trans.getTargetWalletAddr().toString()),
                            chargAmount(trans.getTransferAmt()), bitpayKeychain.getCoin(), bitpayKeychain.getToken(), password, bitpayKeychain.getFeeTxConfirmTarget(),
                            otp);
                    logger.debug("bitpayModel=" + bitPayModel.toString());
                    if (bitPayModel.getPendingApproval() != null)
                    {
                        // 回填划款状态
                        trans.setRemark(bitPayModel.getPendingApproval());
                        trans.setPendingApproval(bitPayModel.getPendingApproval());
                        trans.setConfirmStatus(FundConsts.WALLET_TRANS_STATUS_UNCONFIRM);
                        trans.setTransId(bitPayModel.getHash());
                        trans.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFERPENDING);
                        // 回填划款资金流水状态以及交易ID
                        curr.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFERPENDING);
                        curr.setTableName(getStockInfo(FundConsts.WALLET_BTC_TYPE).getTableFundCurrent());
                        curr.setTransId(trans.getTransId());
                    }
                    if (bitPayModel.getHash() != null)
                    {
                        // 回填划款状态
                        trans.setRemark(bitPayModel.getPendingApproval());
                        trans.setPendingApproval(bitPayModel.getPendingApproval());
                        trans.setConfirmStatus(FundConsts.WALLET_TRANS_STATUS_UNCONFIRM);
                        trans.setTransId(bitPayModel.getHash());
                        trans.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER);
                        // 回填划款资金流水状态以及交易ID
                        curr.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER);
                        curr.setTableName(getStockInfo(FundConsts.WALLET_BTC_TYPE).getTableFundCurrent());
                        curr.setTransId(trans.getTransId());
                    }
                    accountFundTransferService.updateByPrimaryKey(trans);
                    // -----------------------------------调用单笔发送 end--------------------------------------------
                }
                // ------------------------------------直接划拨 如果以后恢复【复核和划拨】 这一段删掉 end------------------------------
                accountWithdrawRecordService.updateByPrimaryKey(curr);
            }
            catch (BusinessException e)
            {
                logger.error("approval 错误:" + e.getLocalizedMessage(), e);
                throw e;
            }
            finally
            {
                redisLock.unlock();
            }
        }
        else
        {
            logger.error("approval acquireLock failed");
            throw new BusinessException(CommonEnums.FAIL);
        }
    }
    
    protected Long chargAmount(BigDecimal amount)
    {
        Long btcAmount = 0L;
        if (null != amount)
        {
            btcAmount = amount.multiply(new BigDecimal(100000000)).longValue();
        }
        return btcAmount;
    }
    
    /**
     * 现金提现申请
     * @param fundModel
     */
    public void doApplyCashWithdraw(FundModel fundModel, AccountCashWithdraw accountCashWithdraw) throws BusinessException
    {
        // 提币出金地址判断
        if (StringUtils.isBlank(fundModel.getAddress())) { throw new BusinessException(FundEnums.ERROR_WITHDRAW_ADDRESS_NOT_EXIST); }
        // 提币出金数量判断
        if (null == fundModel.getAmount()
                || fundModel.getAmount().compareTo(BigDecimal.ZERO) <= 0) { throw new BusinessException(FundEnums.ERROR_WITHDRAW_AMOUNT_GREATER_ZEOR); }
        // 提币出金费用判断
        if (null == fundModel.getFee()) { throw new BusinessException(FundEnums.ERROR_WITHDRAW_FEE_NOT_EXIST); }
        // 证券信息ID判断
        if (null == fundModel.getStockinfoId()) { throw new BusinessException(FundEnums.ERROR_STOCKINFOID_NOT_EXIST); }
        AccountCashWithdraw accountWithdrawRecord = new AccountCashWithdraw();
        accountWithdrawRecord.setAccountId(fundModel.getAccountId());
        accountWithdrawRecord.setStockinfoId(fundModel.getStockinfoId());
        String dateStr = CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMD);
        accountWithdrawRecord.setTimeStart(dateStr + " 00:00:00");
        accountWithdrawRecord.setTimeEnd(dateStr + " 23:59:59");
        logger.debug("AccountWithdrawRecordERC20:" + accountWithdrawRecord.toString());
        java.math.BigDecimal usedAmt = accountCashWithdrawService.findSumAmtByAccount(accountWithdrawRecord);
        logger.debug("已使用提款额度：" + usedAmt);
        if ((usedAmt.add(accountCashWithdraw.getOccurAmt().subtract(accountCashWithdraw.getFee())))
                .compareTo(getStockInfo(fundModel.getStockinfoId()).getAuthedUserWithdrawDayUpper()) > 0)
        {
            logger.debug("已经超过当天最大额度");
            throw new BusinessException(CommonEnums.ERROR_GT_DAY_MAX_AMT);
        }
        try
        {
            fundModel.setAccountId(fundModel.getAccountId());
            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
            fundModel.setCreateBy(fundModel.getAccountId());
            logger.debug("raise fundModel:" + fundModel.toString());
            fundModel = fundService.fundTransaction(fundModel);
            accountCashWithdraw.setAccountId(fundModel.getAccountId());
            accountCashWithdraw.setCreateBy(fundModel.getCreateBy());
            accountCashWithdraw.setCreateDate(new Timestamp(System.currentTimeMillis()));
            accountCashWithdraw.setFee(fundModel.getFee());
            accountCashWithdraw.setOccurAmt(accountCashWithdraw.getOccurAmt().add(accountCashWithdraw.getFee()));
            accountCashWithdraw.setStockinfoId(fundModel.getStockinfoId());
            accountCashWithdraw.setWithdrawCardNo(fundModel.getAddress());
            accountCashWithdraw.setAccountAssetId(fundModel.getOriginalBusinessId());
            accountCashWithdraw.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
            accountCashWithdraw.setCurrentDate(new Date());
            accountCashWithdraw.setRelatedStockinfoId(fundModel.getStockinfoId());
            accountCashWithdraw.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING);
            accountCashWithdraw.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_UNTRANSFER);
            accountCashWithdraw.setOriginalBusinessId(fundModel.getOriginalBusinessId());
            accountCashWithdraw.setCreateBy(fundModel.getAccountId());
            accountCashWithdraw.setCurrentDate(new Date());
            accountCashWithdraw.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);
            Long id = SerialnoUtils.buildPrimaryKey();
            accountCashWithdraw.setId(id);
            accountCashWithdraw.setTransId("待回填:" + id);
            logger.debug("raise accountCashWithdraw:" + accountCashWithdraw.toString());
            accountCashWithdrawService.insert(accountCashWithdraw);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new BusinessException(FundEnums.ERROR_WITHDRAW_FAIL);
        }
    }
    
    @Override
    /**
     * 提币审核
     * @param accountCashWithdraw
     * @param bossUserId
     */
    public void doCashApproval(AccountCashWithdraw accountCashWithdraw, Long bossUserId) throws BusinessException
    {
        try
        {
            AccountCashWithdraw curr = accountCashWithdrawService.selectByIdForUpdate(accountCashWithdraw.getId());
            checkCashWithdrawDataValidate(curr);
            if (!curr.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING)
                    && !curr.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKPENDING)) { throw new BusinessException("state error"); }
            curr.setAuditBy(bossUserId);
            curr.setAuditDate(new Timestamp(System.currentTimeMillis()));
            curr.setCheckBy(bossUserId);
            curr.setCheckDate(new Timestamp(System.currentTimeMillis()));
            if (accountCashWithdraw.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKREJECT)
                    || accountCashWithdraw.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPREJECT))
            {
                // 如果以后 采用复核和划拨 此行删除 -------start-------
                curr.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPREJECT);
                // 如果以后 采用复核和划拨 此行删除 -------end-------
                // 审批拒绝或审核拒绝，调用接口
                if (StringUtils.isBlank(curr.getWithdrawCardNo()))
                {
                    // 目标地址空
                    throw new BusinessException(CommonEnums.FAIL);
                }
                FundModel fundModel = new FundModel();
                fundModel.setAccountId(curr.getAccountId());
                fundModel.setAddress(curr.getWithdrawCardNo());
                fundModel.setAmount(curr.getOccurAmt().subtract(curr.getFee()));
                fundModel.setFee(curr.getFee());
                fundModel.setCreateBy(bossUserId);
                fundModel.setStockinfoId(curr.getStockinfoId());
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW_REJECT);
                // 做业务取消
                fundService.fundTransaction(fundModel);
            }
            if (accountCashWithdraw.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH)
                    || accountCashWithdraw.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKPENDING))
            {
                Account account = accountService.selectByPrimaryKey(curr.getAccountId());
                checkAccountDataValidate(account);
                curr.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH);
                curr.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFERPENDING); // 待划拨
            }
            // 更新accountCashWithdraw
            accountCashWithdrawService.updateByPrimaryKey(curr);
        }
        catch (BusinessException e)
        {
            logger.error("approval 错误:" + e.getLocalizedMessage(), e);
            throw e;
        }
    }
    
    @Override
    public void doApprovalERC20(AccountWithdrawRecordERC20 accountWithdrawRecordERC20, Long bossUserId, String signedTransactionData) throws BusinessException
    {
        try
        {
            AccountWithdrawRecordERC20 curr = accountWithdrawRecordERC20Service.selectByIdForUpdate(accountWithdrawRecordERC20.getId());
            checkCashWithdrawDataValidate(curr);
            if (!curr.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING)
                    && !curr.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKPENDING)) { throw new BusinessException("state error"); }
            curr.setAuditBy(bossUserId);
            curr.setAuditDate(new Timestamp(System.currentTimeMillis()));
            curr.setCheckBy(bossUserId);
            curr.setCheckDate(new Timestamp(System.currentTimeMillis()));
            if (accountWithdrawRecordERC20.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKREJECT)
                    || accountWithdrawRecordERC20.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPREJECT))
            {
                // 如果以后 采用复核和划拨 此行删除 -------start-------
                curr.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPREJECT);
                // 如果以后 采用复核和划拨 此行删除 -------end-------
                // 审批拒绝或审核拒绝，调用接口
                if (StringUtils.isBlank(curr.getWithdrawAddr()))
                {
                    // 目标地址空
                    throw new BusinessException(CommonEnums.FAIL);
                }
                FundModel fundModel = new FundModel();
                fundModel.setAccountId(curr.getAccountId());
                fundModel.setAddress(curr.getWithdrawAddr());
                fundModel.setAmount(curr.getOccurAmt().subtract(curr.getFee()));
                fundModel.setFee(curr.getFee());
                fundModel.setCreateBy(bossUserId);
                fundModel.setStockinfoId(curr.getStockinfoId());
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW_REJECT);
                // 做业务取消
                fundService.fundTransaction(fundModel);
            }
            // ------------------------------------直接划拨 start------------------------------
            if (accountWithdrawRecordERC20.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH)
                    || accountWithdrawRecordERC20.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKPENDING))
            {
                checkWithdrawERC20DataValidate(curr);
                Account account = accountService.selectByPrimaryKey(curr.getAccountId());
                checkAccountDataValidate(account);
                if (StringUtils.isBlank(curr.getWithdrawAddr()))
                {
                    // 目标地址空
                    throw new BusinessException(CommonEnums.FAIL);
                }
                curr.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH);
                curr.setCollectStatus(FundConsts.WALLET_COLLECT_STATUS_NO);
                curr.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFERPENDING); // 待划拨
                // -----------------------------------调用单笔发送 start--------------------------------------------
                String hash = "";
                // 小额直接汇出
                StockInfo stockInfo = stockInfoService.selectByPrimaryKey(accountWithdrawRecordERC20.getStockinfoId());
                BigDecimal amount = curr.getOccurAmt().subtract(curr.getFee());
                if ((curr.getOccurAmt().subtract(curr.getFee())).compareTo(stockInfo.getSmallWithdrawHotSignValue()) <= 0)
                {
                    // 获取ERC20 TOKEN的付款钱包
                    BitpayKeychainERC20 bitpayKeychainERC20 = new BitpayKeychainERC20();
                    bitpayKeychainERC20.setWalletType(Integer.valueOf(1));// 热钱包
                    bitpayKeychainERC20.setStockinfoId(FundConsts.WALLET_ETH_TYPE.toString());
                    List<BitpayKeychainERC20> listBitpay = bitpayKeychainERC20Service.findList(bitpayKeychainERC20);
                    if (listBitpay.size() == 0) { throw new BusinessException("付款钱包不存在"); }
                    bitpayKeychainERC20 = listBitpay.get(0);
                    String password = EncryptUtils.desDecrypt(bitpayKeychainERC20.getWalletPwd());
                    if (curr.getStockinfoId().longValue() == FundConsts.WALLET_ETH_TYPE.longValue())
                    {
                        BigDecimal shengyu = ethLocalService.eth_getBalance(bitpayKeychainERC20.getWalletId(), "latest");
                        if (shengyu.compareTo(amount) >= 0)
                        {
                            hash = ethLocalService.eth_sendTransaction(bitpayKeychainERC20.getWalletId(), password,
                                    EncryptUtils.desDecrypt(curr.getWithdrawAddr().toString()), curr.getOccurAmt().subtract(curr.getFee()));
                        }
                        else
                        {
                            throw new BusinessException("钱包可用余额不足");
                        }
                    }
                    else
                    {
                        BigDecimal shengyu = erc20TokenLocalService.erc20_balanceOf(bitpayKeychainERC20.getWalletId(), stockInfo.getTokenContactAddr(), "latest");
                        if (shengyu.compareTo(amount) >= 0)
                        {
                            hash = erc20TokenLocalService.erc20_transfer(stockInfo.getTokenContactAddr(), bitpayKeychainERC20.getWalletId(), password,
                                    EncryptUtils.desDecrypt(curr.getWithdrawAddr()), curr.getOccurAmt().subtract(curr.getFee()));
                        }
                        else
                        {
                            throw new BusinessException("钱包可用余额不足");
                        }
                    }
                }
                // 大额需要签名
                else
                {
                    // 获取ERC20 TOKEN的付款钱包
                    BitpayKeychainERC20 bitpayKeychainERC20 = new BitpayKeychainERC20();
                    bitpayKeychainERC20.setWalletType(Integer.valueOf(2));// 冷钱包
                    bitpayKeychainERC20.setStockinfoId(FundConsts.WALLET_ETH_TYPE.toString());
                    List<BitpayKeychainERC20> listBitpay = bitpayKeychainERC20Service.findList(bitpayKeychainERC20);
                    if (listBitpay.size() == 0) { throw new BusinessException("付款钱包不存在"); }
                    bitpayKeychainERC20 = listBitpay.get(0);
                    if (curr.getStockinfoId().longValue() == FundConsts.WALLET_ETH_TYPE.longValue())
                    {
                        BigDecimal shengyu = ethLocalService.eth_getBalance(bitpayKeychainERC20.getWalletId(), "latest");
                        if (shengyu.compareTo(amount) >= 0)
                        {
                            hash = ethLocalService.eth_sendRawTransaction(signedTransactionData);
                        }
                        else
                        {
                            throw new BusinessException("钱包可用余额不足");
                        }
                    }
                    else
                    {
                        BigDecimal shengyu = erc20TokenLocalService.erc20_balanceOf(bitpayKeychainERC20.getWalletId(), stockInfo.getTokenContactAddr(), "latest");
                        if (shengyu.compareTo(amount) >= 0)
                        {
                            hash = erc20TokenLocalService.erc20_sendRawTransaction(signedTransactionData);
                        }
                        else
                        {
                            throw new BusinessException("钱包可用余额不足");
                        }
                    }
                }
                if (StringUtils.isNotBlank(hash))
                {
                    // 回填划款资金流水状态以及交易ID
                    curr.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER);
                    curr.setTransId(hash);
                }
                else
                {
                    throw new BusinessException("接口调用失败");
                }
            }
            // 更新accountCashWithdraw
            accountWithdrawRecordERC20Service.updateByPrimaryKey(curr);
        }
        catch (BusinessException e)
        {
            logger.error("approval 错误:" + e.getLocalizedMessage(), e);
            throw e;
        }
    }
    
    /**
     * 提币确认
     * @param accountCashWithdraw
     * @param bossUserId
     */
    @Override
    public void doCashConfirm(AccountCashWithdraw accountCashWithdraw, Long bossUserId) throws BusinessException
    {
        try
        {
            AccountCashWithdraw curr = accountCashWithdrawService.selectByIdForUpdate(accountCashWithdraw.getId());
            checkCashWithdrawDataValidate(curr);
            curr.setRealFee(curr.getFee());
            curr.setTransId(accountCashWithdraw.getTransId());
            curr.setRemark((curr.getRemark() == null ? "" : curr.getRemark()) + "\n" + DateUtils.formatDate(new Date(), DateConst.DATE_FORMAT_YMDHMS) + ":"
                    + accountCashWithdraw.getRemark());
            curr.setRealFee(curr.getFee());
            curr.setCheckBy(bossUserId);
            curr.setCheckDate(new Timestamp(System.currentTimeMillis()));
            if (StringUtils.isBlank(curr.getTransId()) || StringUtils.isBlank(curr.getRemark())) { throw new BusinessException("form need remark or transId!"); }
            if (!curr.getTransferStatus().equals(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFERPENDING)) { throw new BusinessException("state error"); }
            if (accountCashWithdraw.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKREJECT)
                    || accountCashWithdraw.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPREJECT))
            {
                // curr.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPREJECT);
                curr.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER_REJECTED);
                FundModel fundModel = new FundModel();
                fundModel.setAccountId(curr.getAccountId());
                fundModel.setAddress(curr.getWithdrawCardNo());
                fundModel.setAmount(curr.getOccurAmt().subtract(curr.getFee()));
                fundModel.setFee(curr.getFee());
                fundModel.setCreateBy(bossUserId);
                fundModel.setStockinfoId(curr.getStockinfoId());
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW_REJECT);
                // 做业务取消
                fundService.fundTransaction(fundModel);
            }
            if (accountCashWithdraw.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH)
                    || accountCashWithdraw.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKPENDING))
            {
                // 现金充值审核通过，平台自动增加外部台帐
                String lock = new StringBuffer(CacheConst.LOCK_PERFIX).append(BitmsConst.OP_FUND_ASSET).append("autoWalletCashTransferCurrentDeal").toString();
                RedisLock redisLock = new RedisLock(redisTemplate, lock);
                if (redisLock.lock())
                {
                    try
                    {
                        // 现金提现确认处理
                        Account account = accountService.selectByPrimaryKey(curr.getAccountId());
                        checkAccountDataValidate(account);
                        curr.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH);
                        curr.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER);
                        if (curr.getRealFee().compareTo(BigDecimal.ZERO) <= 0) { throw new BusinessException("fee amount error!"); }
                        // 超级用户区块费用资产处理
                        AccountWalletAsset accountWalletAsset95 = accountWalletAssetService.selectForUpdate(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_NETFEE_ID,
                                FundConsts.WALLET_BTC_TYPE);
                        if (null == accountWalletAsset95) { throw new BusinessException("super admin asset doesn't exist"); }
                        FundModel fundModel = new FundModel();
                        fundModel.setStockinfoId(curr.getStockinfoId());
                        fundModel.setFee(curr.getRealFee());
                        fundModel.setBusinessFlag("doCashWithdrawConfirm");
                        fundModel.setAddress("");
                        AccountWalletAsset accountWalletAsset = new AccountWalletAsset();
                        accountWalletAsset.setStockinfoId(curr.getStockinfoId());
                        accountWalletAsset.setRelatedStockinfoId(curr.getStockinfoId());
                        fundService.superAdminNetFee(fundModel, accountWalletAsset, FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);
                        // 确认成功后 普通用户 在途提现资金要减少，成功提现金额要增加
                        AccountWalletAsset userWalletAsset = accountWalletAssetService.selectForUpdate(curr.getAccountId(), curr.getStockinfoId());
                        userWalletAsset.setWithdrawedTotal(userWalletAsset.getWithdrawedTotal().add(curr.getOccurAmt().subtract(curr.getFee())));
                        userWalletAsset.setWithdrawingTotal(userWalletAsset.getWithdrawingTotal().subtract(curr.getOccurAmt().subtract(curr.getFee())));
                        accountWalletAssetService.updateByPrimaryKey(userWalletAsset);
                        // 现金提现确认成功，平台自动减少外部台帐
                        BigDecimal orgAmt = BigDecimal.ZERO;
                        WalletCashTransferCurrent walletCashTransferCurrentDB = walletCashTransferCurrentService.getLastEntity();
                        if (null != walletCashTransferCurrentDB)
                        {
                            orgAmt = walletCashTransferCurrentDB.getLastAmt();
                        }
                        WalletCashTransferCurrent walletCashTransferCurrent = new WalletCashTransferCurrent();
                        walletCashTransferCurrent.setCurrentDate(new Timestamp(System.currentTimeMillis()));
                        walletCashTransferCurrent.setStockinfoId(curr.getStockinfoId());
                        walletCashTransferCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE); // 减少
                        walletCashTransferCurrent.setOrgAmt(orgAmt);
                        walletCashTransferCurrent.setOccurAmt(curr.getOccurAmt());
                        walletCashTransferCurrent.setLastAmt(orgAmt.subtract(walletCashTransferCurrent.getOccurAmt())); // 减少
                        walletCashTransferCurrent.setFee(BigDecimal.ZERO);
                        walletCashTransferCurrent.setTransId(curr.getTransId());
                        walletCashTransferCurrent.setRemark("现金提现确认成功，平台自动减少外部台帐！");
                        walletCashTransferCurrent.setCreateDate(new Timestamp(System.currentTimeMillis()));
                        walletCashTransferCurrent.setCreateBy(bossUserId);
                        walletCashTransferCurrentService.insert(walletCashTransferCurrent);
                    }
                    catch (BusinessException e)
                    {
                        logger.error("现金提现确认错误:" + e.getMessage(), e);
                        throw new BusinessException(e.getErrorCode());
                    }
                    finally
                    {
                        redisLock.unlock();
                    }
                }
                else
                {
                    logger.error("现金提现确认错误");
                    throw new BusinessException(CommonEnums.FAIL);
                }
            }
            // 最后更新 accountCashWithdraw
            accountCashWithdrawService.updateByPrimaryKey(curr);
        }
        catch (BusinessException e)
        {
            logger.error("approval 错误:" + e.getLocalizedMessage(), e);
            throw e;
        }
    }
    
    @Override
    public void doTradexWithdrawERC20(FundModel fundModel) throws BusinessException
    {
        Account account = accountService.selectByPrimaryKey(fundModel.getAccountId());
        // checkAccountDataValidate(account);
        // 证券信息ID判断
        if (null == fundModel.getStockinfoId()) { throw new BusinessException(FundEnums.ERROR_STOCKINFOID_NOT_EXIST); }
        AccountWithdrawRecordERC20 accountWithdrawRecord = new AccountWithdrawRecordERC20();
        accountWithdrawRecord.setAccountId(fundModel.getAccountId());
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
        if (usedCnt.intValue() >= cnt)
        {
            logger.debug("已经超过当日最大提现次数");
            throw new BusinessException(CommonEnums.ERROR_GT_DAY_MAX_CNT);
        }
        // 单笔最高ETH
        params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(ParamConsts.ERC20TOKEN_WITHDRAW_UP_ETH_FOR_ONE);
        params = sysParameterService.getSysParameterByEntity(params);
        BigDecimal upLine = BigDecimal.valueOf(Double.valueOf(params.getValue()));
        if (fundModel.getStockinfoId().longValue() != FundConsts.WALLET_ETH_TYPE.longValue())
        {
            StockInfo stockInfo = stockInfoService.selectByPrimaryKey(fundModel.getStockinfoId());
            stockInfo = stockInfoService.findByContractAddr(stockInfo.getTokenContactAddr());
            BigDecimal platPrice = BigDecimal.ZERO;
            RtQuotationInfo rtQuotationInfo = rtQuotationInfoService.queryRtQuotationInfo(stockInfo.getTradeStockinfoId(), stockInfo.getId());
            if (null != rtQuotationInfo)
            {
                platPrice = rtQuotationInfo.getPlatPrice();
            }
            if (platPrice == null)
            {
                upLine = BigDecimal.ZERO;
            }
            else
            {
                upLine = upLine.divide(platPrice).setScale(4, BigDecimal.ROUND_DOWN);
            }
        }
        if (upLine.setScale(12, BigDecimal.ROUND_HALF_UP).compareTo(fundModel.getAmount()) < 0)
        {
            logger.debug("已经超过单笔最大额度");
            throw new BusinessException(CommonEnums.ERROR_GT_ONCE_MAX_AMT);
        }
        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        fundModel.setCreateBy(fundModel.getAccountId());
        fundModel.setAddress(fundModel.getAddress());
        logger.debug("raise fundModel:" + fundModel.toString());
        fundModel = fundService.fundTransaction(fundModel);
        logger.debug("raise fundModel after insert:" + fundModel.toString());
        Long id = SerialnoUtils.buildPrimaryKey();
        AccountWithdrawRecordERC20 accountWithdrawRecordERC20 = new AccountWithdrawRecordERC20();
        accountWithdrawRecordERC20.setId(id);
        accountWithdrawRecordERC20.setAccountId(fundModel.getAccountId());
        accountWithdrawRecordERC20.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        accountWithdrawRecordERC20.setAccountAssetId(fundModel.getOriginalBusinessId());
        accountWithdrawRecordERC20.setCurrentDate(new Date());
        accountWithdrawRecordERC20.setBusinessFlag(fundModel.getBusinessFlag());
        accountWithdrawRecordERC20.setStockinfoId(fundModel.getStockinfoId());
        accountWithdrawRecordERC20.setRelatedStockinfoId(fundModel.getStockinfoId());
        accountWithdrawRecordERC20.setContractAmt(BigDecimal.ZERO);
        accountWithdrawRecordERC20.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);
        accountWithdrawRecordERC20.setOccurAmt(fundModel.getAmount().add(fundModel.getFee()));
        accountWithdrawRecordERC20.setOrgAmt(BigDecimal.ZERO);
        accountWithdrawRecordERC20.setLastAmt(BigDecimal.ZERO);
        accountWithdrawRecordERC20.setForzenOrgAmt(BigDecimal.ZERO);
        accountWithdrawRecordERC20.setOccurForzenAmt(BigDecimal.ZERO);
        accountWithdrawRecordERC20.setForzenLastAmt(BigDecimal.ZERO);
        accountWithdrawRecordERC20.setConfirmStatus(FundConsts.WALLET_TRANS_STATUS_UNCONFIRM);
        accountWithdrawRecordERC20.setTransId(null);
        accountWithdrawRecordERC20.setChargeAddr(null);
        accountWithdrawRecordERC20.setWithdrawAddr(fundModel.getAddress());
        accountWithdrawRecordERC20.setNetFee(BigDecimal.ZERO);
        accountWithdrawRecordERC20.setFee(fundModel.getFee());
        accountWithdrawRecordERC20.setStatus(FundConsts.ACCOUNT_FUND_CURRENT_EFFECTIVE);
        accountWithdrawRecordERC20.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING);
        accountWithdrawRecordERC20.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_UNTRANSFER);
        accountWithdrawRecordERC20.setCollectStatus(FundConsts.WALLET_COLLECT_STATUS_NO);
        accountWithdrawRecordERC20.setCreateBy(fundModel.getAccountId());
        accountWithdrawRecordERC20.setCreateDate(new Timestamp(System.currentTimeMillis()));
        accountWithdrawRecordERC20.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountWithdrawRecordERC20.setCurrentDate(new Date());
        logger.debug("raise accountWithdrawRecordERC20:" + accountWithdrawRecordERC20.toString());
        accountWithdrawRecordERC20Service.insert(accountWithdrawRecordERC20);
        // AccountFundCurrent fundCurrent = new AccountFundCurrent();
        // fundCurrent.setId(fundModel.getAccountFundCurrentId());
        // fundCurrent.setAccountId(fundModel.getAccountId());
        // fundCurrent.setTableName(getStockInfo(fundModel.getStockinfoId()).getTableFundCurrent());
        // fundCurrent = accountFundCurrentService.selectByPrimaryKey(getStockInfo(fundModel.getStockinfoId()).getTableFundCurrent(), fundModel.getAccountFundCurrentId());
        // 这里调用接口处理 2018-04-10 潘总确认 需要冷签 同时开放提币取消功能
        // accountWithdrawRecordERC20.setId(id);
        // accountWithdrawRecordERC20.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKPENDING);
        // doTradexApprovalERC20(accountWithdrawRecordERC20, fundModel.getAccountId(), null);
    }
    
    public void doTradexApprovalERC20(AccountWithdrawRecordERC20 accountWithdrawRecordERC20, Long bossUserId, String signedTransactionData) throws BusinessException
    {
        try
        {
            AccountWithdrawRecordERC20 curr = accountWithdrawRecordERC20Service.selectByIdForUpdate(accountWithdrawRecordERC20.getId());
            checkCashWithdrawDataValidate(curr);
            if (!curr.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING)
                    && !curr.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKPENDING)) { throw new BusinessException("state error"); }
            curr.setAuditBy(bossUserId);
            curr.setAuditDate(new Timestamp(System.currentTimeMillis()));
            curr.setCheckBy(bossUserId);
            curr.setCheckDate(new Timestamp(System.currentTimeMillis()));
            if (accountWithdrawRecordERC20.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKREJECT)
                    || accountWithdrawRecordERC20.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPREJECT))
            {
                // 如果以后 采用复核和划拨 此行删除 -------start-------
                curr.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPREJECT);
                // 如果以后 采用复核和划拨 此行删除 -------end-------
                // 审批拒绝或审核拒绝，调用接口
                if (StringUtils.isBlank(curr.getWithdrawAddr()))
                {
                    // 目标地址空
                    throw new BusinessException(CommonEnums.FAIL);
                }
                FundModel fundModel = new FundModel();
                fundModel.setAccountId(curr.getAccountId());
                fundModel.setAddress(curr.getWithdrawAddr());
                fundModel.setAmount(curr.getOccurAmt().subtract(curr.getFee()));
                fundModel.setFee(curr.getFee());
                fundModel.setCreateBy(bossUserId);
                fundModel.setStockinfoId(curr.getStockinfoId());
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW_REJECT);
                // 做业务取消
                fundService.fundTransaction(fundModel);
            }
            // ------------------------------------直接划拨 start------------------------------
            if (accountWithdrawRecordERC20.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH)
                    || accountWithdrawRecordERC20.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKPENDING))
            {
                checkWithdrawERC20DataValidate(curr);
                Account account = accountService.selectByPrimaryKey(curr.getAccountId());
                checkAccountDataValidate(account);
                if (StringUtils.isBlank(curr.getWithdrawAddr()))
                {
                    // 目标地址空
                    throw new BusinessException(CommonEnums.FAIL);
                }
                curr.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH);
                curr.setCollectStatus(FundConsts.WALLET_COLLECT_STATUS_NO);
                curr.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFERPENDING); // 待划拨
                // -----------------------------------调用单笔发送 start--------------------------------------------
                String hash = "";
                // 小额直接汇出
                StockInfo stockInfo = stockInfoService.selectByPrimaryKey(accountWithdrawRecordERC20.getStockinfoId());
                BigDecimal amount = curr.getOccurAmt().subtract(curr.getFee());
                // 获取ERC20 TOKEN的付款钱包
                BitpayKeychainERC20 bitpayKeychainERC20 = new BitpayKeychainERC20();
                bitpayKeychainERC20.setWalletType(Integer.valueOf(1));// 热钱包
                bitpayKeychainERC20.setStockinfoId(FundConsts.WALLET_ETH_TYPE.toString());
                List<BitpayKeychainERC20> listBitpay = bitpayKeychainERC20Service.findList(bitpayKeychainERC20);
                if (listBitpay.size() == 0) { throw new BusinessException("付款钱包不存在"); }
                bitpayKeychainERC20 = listBitpay.get(0);
                String password = EncryptUtils.desDecrypt(bitpayKeychainERC20.getWalletPwd());
                if (curr.getStockinfoId().longValue() == FundConsts.WALLET_ETH_TYPE.longValue())
                {
                    BigDecimal shengyu = ethLocalService.eth_getBalance(bitpayKeychainERC20.getWalletId(), "latest");
                    if (shengyu.compareTo(amount) >= 0)
                    {
                        hash = ethLocalService.eth_sendTransaction(bitpayKeychainERC20.getWalletId(), password, EncryptUtils.desDecrypt(curr.getWithdrawAddr().toString()),
                                curr.getOccurAmt().subtract(curr.getFee()));
                    }
                    else
                    {
                        throw new BusinessException("钱包可用余额不足");
                    }
                }
                else
                {
                    BigDecimal shengyu = erc20TokenLocalService.erc20_balanceOf(bitpayKeychainERC20.getWalletId(), stockInfo.getTokenContactAddr(), "latest");
                    if (shengyu.compareTo(amount) >= 0)
                    {
                        hash = erc20TokenLocalService.erc20_transfer(stockInfo.getTokenContactAddr(), bitpayKeychainERC20.getWalletId(), password,
                                EncryptUtils.desDecrypt(curr.getWithdrawAddr()), curr.getOccurAmt().subtract(curr.getFee()));
                    }
                    else
                    {
                        throw new BusinessException("钱包可用余额不足");
                    }
                }
                if (StringUtils.isNotBlank(hash))
                {
                    // 回填划款资金流水状态以及交易ID
                    curr.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER);
                    curr.setTransId(hash);
                }
                else
                {
                    throw new BusinessException("接口调用失败");
                }
            }
            // 更新accountCashWithdraw
            accountWithdrawRecordERC20Service.updateByPrimaryKey(curr);
        }
        catch (BusinessException e)
        {
            logger.error("approval 错误:" + e.getLocalizedMessage(), e);
            throw e;
        }
    }
    
    private void checkAccountDataValidate(Account account) throws BusinessException
    {
        if (null == account) { throw new BusinessException(CommonEnums.ERROR_LOGIN_ACCOUNT); }
        if (account.getStatus().intValue() != AccountConsts.ACCOUNT_STATUS_NORMAL.intValue()) { throw new BusinessException(CommonEnums.ERROR_LOGIN_LOCK); }
        if (null != account && !account.verifySignature())
        {// 校验数据
            logger.info("账户信息 数据校验失败");
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        // (存储过程执行 returnCode：0 执行成功，-1 执行失败; 检查结果 chekResult 1.正常 ，小于0 异常; 提示信息 chekMsg)
        AcctAssetChkResult result = acctAssetChkService.doAcctAssetChk(account.getId());
        if (result != null && result.getReturnCode() != null && result.getChekResult() != null)
        {
            if (result.getReturnCode().intValue() != 1 || result.getChekResult().intValue() != 1)
            {
                String msg = result.getChekMsg() + "  accountId=" + account.getId();
                throw new BusinessException(msg);
            }
        }
    }
    
    @Override
    public void doChargeAward(Long accountId) throws BusinessException
    {
        String lock = new StringBuffer(CacheConst.LOCK_PERFIX).append("charge_award_")// 加入模块标识
                .append(accountId).toString();
        RedisLock redisLock = new RedisLock(redisTemplate, lock);
        if (redisLock.lock())
        {
            try
            {
                // 1.查询用户 检查是否已奖励
                Account account = accountService.selectByPrimaryKey(accountId);
                if(account == null)
                {
                    throw new BusinessException(CommonEnums.ERROR_LOGIN_ACCOUNT);
                }
                if (account.getChargeAward().intValue() == 1)
                {
                    // 不能重复领取。
                    throw new BusinessException("You can only collect free candy once.");
                }
                // 2.检查账户 是否符合奖励的标准
                List<BlockTransConfirmERC20> addrIdsList = blockTransConfirmERC20Service.findUnsignList(accountId);
                if (addrIdsList.size() == 0)
                {
                    // 未充值或付款钱包已使用。
                    throw new BusinessException("You have never deposited ETH before, or your address has already been used to collect free candy with other account.");
                }
                List<Long> addrIds = new ArrayList<Long>();//  跟账户相关的充值过来的地址id
                List<Long> addrRightIds = new ArrayList<Long>(); // 有效的充值过来的地址ID
                for(BlockTransConfirmERC20 blockTransConfirmERC20:addrIdsList)
                {
                    addrIds.add(blockTransConfirmERC20.getId());
                    // 判断550万块之前有没交易 有交易才算数
                    int cnt = etherscanRemoteService.eth_txlist(blockTransConfirmERC20.getFromAddress(),0L,5500000L,1,10);
                    logger.debug(blockTransConfirmERC20.getFromAddress()+"存在最早交易笔数:"+cnt);
                    if(cnt > 0 )
                    {
                        addrRightIds.add(blockTransConfirmERC20.getId());
                    }
                }
                if (addrRightIds.size() == 0)
                {
                    // 未充值或付款钱包已使用。
                    throw new BusinessException("You have never deposited ETH before, or your address has already been used to collect free candy with other account.");
                }
                // 3.行锁
                List<BlockTransConfirmERC20> addrs = null;
                try
                {
                    addrs = blockTransConfirmERC20Service.getByIdsForUpdate(addrIds.toArray(new Long[]{}));
                }
                catch (Exception e)
                {
                    throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
                }
                if (addrs.size() == 0)
                {
                    // 未充值或付款钱包已使用。
                    throw new BusinessException("You have never deposited ETH before, or your address has already been used to collect free candy with other account.");
                }
                // 4.如果条件2满足 则调用充值奖励
                FundModel fundModel = new FundModel();
                fundModel.setAccountId(accountId);
                fundModel.setStockinfoIdEx(FundConsts.WALLET_BIEX_TYPE);
                fundModel.setStockinfoId(FundConsts.WALLET_BIEX_TYPE);
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_TRADE_RECHARGE_AWARD);
                fundModel.setAmount(BigDecimal.valueOf(1000));
                fundService.fundTransaction(fundModel);
                // 5.修改 上述B 按ID修改奖励的状态
                List<String> list = new ArrayList<String>();
                for (int i=0; i<addrIdsList.size(); i++) {
                    if(!list.contains(addrIdsList.get(i).getFromAddress())) {
                        list.add(addrIdsList.get(i).getFromAddress().toString());
                    }
                }
                for(String address:list)
                {
                    EthAddrs ethAddrs = new EthAddrs();
                    ethAddrs.setIsCollect(FundConsts.PUBLIC_STATUS_YES);
                    ethAddrs.setAddr(address);
                    ethAddrs.setEthBalance(BigDecimal.ZERO);
                    ethAddrs.setBlockHeight(1L);
                    ethAddrs.setCreateDate(new Date());
                    ethAddrsService.save(ethAddrs);
                }
                // 6.修改该账户充值奖励=1 已奖励过
                account.setChargeAward(1);
                accountService.updateByPrimaryKey(account);
            }
            catch (BusinessException e)
            {
                logger.error("充值奖励处理错误:" + e.getMessage(), e);
                throw new BusinessException(e.getErrorCode());
            }
            finally
            {
                redisLock.unlock();
            }
        }
        else
        {
            logger.error("充值奖励并发错误错误");
            throw new BusinessException(CommonEnums.FAIL);
        }
    }
    
    private void checkFundCurrentDataValidate(AccountWithdrawRecord accountWithdrawRecord) throws BusinessException
    {
        if (null == accountWithdrawRecord) { throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR); }
    }
    
    private void checkCashWithdrawDataValidate(AccountCashWithdraw accountCashWithdraw) throws BusinessException
    {
        if (null == accountCashWithdraw) { throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR); }
    }
    
    private void checkWithdrawERC20DataValidate(AccountWithdrawRecordERC20 accountWithdrawRecordERC20) throws BusinessException
    {
        if (null == accountWithdrawRecordERC20) { throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR); }
    }
    
    private void checkCashWithdrawDataValidate(AccountWithdrawRecordERC20 accountCashWithdraw) throws BusinessException
    {
        if (null == accountCashWithdraw) { throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR); }
    }
    
    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
}

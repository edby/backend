/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;

import com.blocain.bitms.boss.common.consts.ParamConsts;
import com.blocain.bitms.boss.common.entity.SysParameter;
import com.blocain.bitms.boss.common.service.SysParameterService;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.quotation.entity.RtQuotationInfo;
import com.blocain.bitms.quotation.service.RtQuotationInfoService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.DateUtils;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountDebitAsset;
import com.blocain.bitms.trade.fund.entity.AccountDebitAssetDetail;
import com.blocain.bitms.trade.fund.mapper.AccountDebitAssetMapper;
import com.blocain.bitms.trade.fund.model.AccountAssetModel;
import com.blocain.bitms.trade.fund.model.DebitAssetModel;
import com.blocain.bitms.trade.fund.model.FundModel;
import com.blocain.bitms.trade.risk.model.EnableModel;
import com.blocain.bitms.trade.risk.service.EnableService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;

/**
 * 账户借贷资产表 服务实现类
 * <p>File：AccountDebitAssetServiceImpl.java </p>
 * <p>Title: AccountDebitAssetServiceImpl </p>
 * <p>Description:AccountDebitAssetServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountDebitAssetServiceImpl extends GenericServiceImpl<AccountDebitAsset> implements AccountDebitAssetService
{
    protected AccountDebitAssetMapper accountDebitAssetMapper;
    
    @Autowired
    public AccountDebitAssetServiceImpl(AccountDebitAssetMapper accountDebitAssetMapper)
    {
        super(accountDebitAssetMapper);
        this.accountDebitAssetMapper = accountDebitAssetMapper;
    }
    
    @Autowired
    AccountContractAssetService    accountContractAssetService;
    
    @Autowired
    AccountSpotAssetService        accountSpotAssetService;
    
    @Autowired
    AccountDebitAssetService       accountDebitAssetService;
    
    @Autowired
    EnableService                  enableService;
    
    @Autowired
    SysParameterService            sysParameterService;
    
    @Autowired(required = false)
    RtQuotationInfoService         rtQuotationInfoService;
    
    @Autowired(required = false)
    StockInfoService               stockInfoService;
    
    @Autowired(required = false)
    FundService                    fundService;
    
    @Autowired(required = false)
    AccountAssetService            accountAssetService;
    
    @Autowired(required = false)
    AccountDebitAssetDetailService accountDebitAssetDetailService;
    
    // 账户信息KEY: platscan_fundCurrent_[acctid]
    private static final String    keyPrefix = new StringBuffer(CacheConst.REDIS_PLATSCAN_PREFIX).append(BitmsConst.SEPARATOR).append(BitmsConst.OP_FUND_ASSET)
            .append(BitmsConst.SEPARATOR).toString();
    
    public void clearAccountAssetCache(Long accountId, Long exchangePairMoney)
    {
        String key = new StringBuilder(keyPrefix).append(accountId).append(BitmsConst.SEPARATOR).append(exchangePairMoney).toString(); // changescan_fundCurrent_[acctid]
        logger.debug("getAccountFundAsset key=" + key);
        RedisUtils.del(key);
    }
    
    /**
     * 自动计息
     * @return
     */
    @Override
    public void autoAccountDebitAssetInterest() throws BusinessException
    {
        StockInfo stockInfoSelect = new StockInfo();
        stockInfoSelect.setCanBorrow(FundConsts.PUBLIC_STATUS_YES);
        List<StockInfo> listStock = stockInfoService.findList(stockInfoSelect);
        for (StockInfo stockInfo : listStock)
        {
            logger.debug("关联证券ID=" + stockInfo.getId() + " stocktype=" + stockInfo.getStockType());
            AccountDebitAsset accountDebitAssetSelect = new AccountDebitAsset();
            accountDebitAssetSelect.setTableName(stockInfo.getTableDebitAsset());
            //accountDebitAssetSelect.setStockinfoId(stockInfo.getCapitalStockinfoId());
            accountDebitAssetSelect.setLastInterestDay(Long.parseLong(DateUtils.getDateFormat(System.currentTimeMillis(), "yyyyMMdd")));
            List<AccountDebitAsset> AccountDebitAssetList = accountDebitAssetService.findListForDebit(accountDebitAssetSelect);
            if (AccountDebitAssetList.size() == 0)
            {
                logger.debug("目前还没有借款的用户");
            }
            else
            {
                for (AccountDebitAsset record : AccountDebitAssetList)
                {
                    AccountAssetModel model = new AccountAssetModel();
                    if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_CONTRACTSPOT))
                    {
                        model.setAccountId(record.getBorrowerAccountId());
                        model.setStockInfoId(record.getStockinfoId());
                        model.setRelatedStockinfoId(record.getRelatedStockinfoId());
                        model.setStockType(stockInfo.getStockType());
                        model = accountAssetService.findAssetAndDebitForAccount(model);
                        if (record.getBorrowerAccountId().longValue() > FundConsts.SYSTEM_ACCOUNT_ID.longValue())
                        {
                            accountDebitAssetService.doInterestRate(record, model, stockInfo.getId());
                        }
                    }
                    else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_LEVERAGEDSPOT))
                    {
                        model.setAccountId(record.getBorrowerAccountId());
                        model.setStockInfoId(record.getStockinfoId());
                        model.setRelatedStockinfoId(stockInfo.getId());
                        model.setStockType(stockInfo.getStockType());
                        model = accountAssetService.findAssetAndDebitForAccount(model);
                        if (record.getBorrowerAccountId().longValue() > FundConsts.SYSTEM_ACCOUNT_ID.longValue())
                        {
                            accountDebitAssetService.doInterestRate(record, model, stockInfo.getId());
                        }
                    }
                    else
                    {
                        logger.debug("错误的证券类型不处理");
                        continue;
                    }
                }
            }
        }
    }
    
    @Override
    public void doInterestRate(AccountDebitAsset record, AccountAssetModel model, Long exchangePairMoney) throws BusinessException
    {
        // 行锁
        record = accountDebitAssetService.selectByPrimaryKeyForUpdate(getStockInfo(record.getRelatedStockinfoId()).getTableDebitAsset(), record.getId());
        Long currDate = Long.parseLong(DateUtils.getDateFormat(System.currentTimeMillis(), "yyyyMMdd"));
        if (record.getLastInterestDay().longValue() >= currDate.longValue())
        {
            logger.error("借款：已经计息过，不能重复计息。");
            return;
        }
        BigDecimal rate = BigDecimal.ZERO;
        StockInfo stockInfo = getStockInfo(exchangePairMoney);
        if (record.getStockinfoId().longValue() != record.getRelatedStockinfoId().longValue())
        {
            rate = stockInfo.getDigitBorrowDayRate();
        }
        else
        {
            rate = stockInfo.getLegalBorrowDayRate();
        }
        logger.debug("借款ID:" + record.getId() + " 借款信息：" + record.toString());
        logger.debug("借款ID:" + record.getId() + " 利率：" + rate);
        logger.debug("借款ID:" + record.getId() + " 资产信息：" + model.toString());
        Long ratedate = Long.parseLong(DateUtils.getDateFormat(System.currentTimeMillis(), "yyyyMMdd"));
        if (ratedate.longValue() > record.getLastInterestDay().longValue())
        {
            // 计算利息
            BigDecimal effectiveDebitAmt = record.getDebitAmt().subtract(model.getAmount());
            // BigDecimal effectiveDebitAmt= record.getDebitAmt().subtract(record.getAccumulateInterest()).subtract(model.getAmount());
            logger.debug("借款ID:" + record.getId() + " 有效借款：" + effectiveDebitAmt);
            if (effectiveDebitAmt.compareTo(BigDecimal.ZERO) > 0)
            {
                BigDecimal interest = effectiveDebitAmt.multiply(rate).setScale(12, BigDecimal.ROUND_HALF_UP);
                // 修改借款表
                record.setDebitAmt(record.getDebitAmt().add(interest));
                record.setAccumulateInterest(record.getAccumulateInterest().add(interest));
                record.setLastInterestDay(ratedate);
                record.setTableName(getStockInfo(record.getRelatedStockinfoId()).getTableDebitAsset());
                accountDebitAssetMapper.updateByPrimaryKey(record);
                // 利息处理
                FundModel fundModel = new FundModel();
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_INTEREST);
                fundModel.setStockinfoId(record.getStockinfoId());
                fundModel.setStockinfoIdEx(exchangePairMoney);
                fundModel.setAmount(record.getDebitAmt().subtract(interest));
                fundModel.setFee(interest);
                fundModel.setAccountId(record.getBorrowerAccountId());
                fundModel.setCreateBy(record.getBorrowerAccountId());
                fundModel.setOriginalBusinessId(record.getId());
                fundService.fundTransaction(fundModel);
                // 计息记录
                Long id = SerialnoUtils.buildPrimaryKey();
                AccountDebitAssetDetail detail = new AccountDebitAssetDetail();
                detail.setId(id);
                detail.setBorrowerAccountId(record.getBorrowerAccountId());
                detail.setLenderAccountId(record.getLenderAccountId());
                detail.setStockinfoId(record.getStockinfoId());
                detail.setRelatedStockinfoId(record.getRelatedStockinfoId());
                detail.setDebitAmt(record.getDebitAmt());
                detail.setEffectiveDebitAmt(effectiveDebitAmt);
                detail.setBorrowDayRate(rate);
                detail.setDayInterest(interest);
                detail.setAccumulateInterest(record.getAccumulateInterest());
                detail.setUpdateDate(new Date());
                detail.setTableName(stockInfo.getTableDebitAssetDetail());
                accountDebitAssetDetailService.insert(detail);
                // 利息计给超级用户
                if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_LEVERAGEDSPOT))
                {
                    // 超级用户资产处理 增加一笔利息累计
                    fundModel = new FundModel();
                    fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_INTEREST);
                    fundModel.setStockinfoId(record.getStockinfoId());
                    fundModel.setStockinfoIdEx(exchangePairMoney);
                    fundModel.setAmount(interest);
                    fundModel.setFee(interest);
                    fundModel.setAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_DEBIT_WEALTH_ID);
                    fundModel.setCreateBy(record.getBorrowerAccountId());
                    fundModel.setOriginalBusinessId(id);
                    fundService.doSuperAdminDebitInterest(fundModel, FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
                }
                // 清除用户缓存
                clearAccountAssetCache(record.getBorrowerAccountId(), exchangePairMoney);
            }
            else
            {
                logger.debug("借款ID:" + record.getId() + "不产生利息 有效借款：" + effectiveDebitAmt);
            }
        }
        else
        {
            logger.debug("借款ID:" + record.getId() + "今日已计息 借款信息：" + record.toString());
        }
    }
    
    @Override
    public AccountDebitAsset selectByPrimaryKeyForUpdate(String tableName, Long id) throws BusinessException
    {
        AccountDebitAsset accountDebitAsset;
        try
        {
            accountDebitAsset = accountDebitAssetMapper.selectByPrimaryKeyForUpdate(tableName, id);
        }
        catch (CannotAcquireLockException ex)
        {
            throw new BusinessException(ex.getLocalizedMessage());
        }
        return accountDebitAsset;
    }
    
    /**
     * 向平台自动借款
     * @param debitAssetModel
     * @return
     */
    @Override
    public DebitAssetModel doDebitBorrowFromPlat(DebitAssetModel debitAssetModel) throws BusinessException
    {
        if (debitAssetModel.getStockinfoId() == null || debitAssetModel.getRelatedStockinfoId() == null
                || debitAssetModel.getBorrowerAccountId() == null) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        if (debitAssetModel.getDebitAmt().compareTo(BigDecimal.ZERO) <= 0)
        {
            logger.debug("借款数量或金额不在范围内");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(debitAssetModel.getRelatedStockinfoId());
        logger.debug("交易标的证券ID：" + stockInfo.getTradeStockinfoId());
        boolean isVCoin = stockInfo.getTradeStockinfoId().longValue() == debitAssetModel.getStockinfoId().longValue();
        logger.debug("是否数字货币标的：" + isVCoin);
        // 合约资产交易
        if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_CONTRACTSPOT))
        {
            // 行所
            try
            {
                accountContractAssetService.selectByPrimaryKeyOnRowLock(debitAssetModel.getBorrowerAccountId(), debitAssetModel.getRelatedStockinfoId(),
                        debitAssetModel.getRelatedStockinfoId(), getStockInfo(debitAssetModel.getRelatedStockinfoId()).getTableAsset());
                accountContractAssetService.selectByPrimaryKeyOnRowLock(debitAssetModel.getBorrowerAccountId(), debitAssetModel.getStockinfoId(),
                        debitAssetModel.getRelatedStockinfoId(), getStockInfo(debitAssetModel.getRelatedStockinfoId()).getTableAsset());
            }
            catch (Exception e)
            {
                logger.debug(e.getStackTrace().toString());
                throw new BusinessException(CommonEnums.ERROR_SYS_BUSY);
            }
            RtQuotationInfo rtQuotationInfo = rtQuotationInfoService.queryRtQuotationInfo(debitAssetModel.getStockinfoId(), debitAssetModel.getRelatedStockinfoId());
            BigDecimal platPrice = BigDecimal.ZERO;
            if (rtQuotationInfo != null)
            {
                platPrice = rtQuotationInfo.getPlatPrice();
            }
            logger.debug("USDX借款：BTC对应的USDX行情=" + platPrice);
            // 借法定货币
            if (FundConsts.BORROW_STOCKINFO_TYPE_LEGAL_MONEY == debitAssetModel.getStockinfoType())
            {
                // 数字货币可最大可借数量
                EnableModel enableModel = new EnableModel();
                enableModel.setAccountId(debitAssetModel.getBorrowerAccountId());
                enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT);
                enableModel.setStockinfoId(debitAssetModel.getStockinfoId());// 数字货币
                enableModel.setRelatedStockinfoId(debitAssetModel.getRelatedStockinfoId());// 法定货币
                enableModel.setMaxLongLever(debitAssetModel.getMaxLongLever());
                enableModel.setMaxShortLever(debitAssetModel.getMaxShortLever());
                enableModel = enableService.entrustTerminalEnable(enableModel);
                logger.debug("法定货币借款：数字货币最大可借数量=" + enableModel.getEnableAmount());
                if (enableModel.getEnableAmount().compareTo(BigDecimal.ZERO) <= 0)
                {
                    logger.debug("法定货币借款：数字货币最大可借数量不足");
                    throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
                }
                // 法定货币最大可借数量
                BigDecimal maxBorrow = isVCoin ? enableModel.getEnableAmount().multiply(platPrice) : enableModel.getEnableAmount();
                logger.debug("法定货币借款：法定货币最大可借数量=" + maxBorrow);
                if (debitAssetModel.getDebitAmt().compareTo(BigDecimal.ZERO) <= 0 || debitAssetModel.getDebitAmt().compareTo(maxBorrow) > 0)
                {
                    logger.debug("法定货币借款：借款范围错误");
                    throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
                }
                AccountDebitAsset recordEntity = new AccountDebitAsset();
                recordEntity.setStockinfoId(debitAssetModel.getRelatedStockinfoId());// 法定货币
                recordEntity.setRelatedStockinfoId(debitAssetModel.getRelatedStockinfoId());// 法定货币
                recordEntity.setTableName(getStockInfo(debitAssetModel.getRelatedStockinfoId()).getTableDebitAsset());
                BigDecimal sumDebitAmt = accountDebitAssetService.findSumDebitAmt(recordEntity);
                BigDecimal upLine = isVCoin ? stockInfo.getCapitalDebitTotal() : stockInfo.getTradeDebitTotal();
                if (sumDebitAmt.add(debitAssetModel.getDebitAmt()).compareTo(upLine) < 0)
                {
                    logger.debug("法定货币借款：放贷余额不足");
                    throw new BusinessException(CommonEnums.RISK_ENABLE_DEBIT_NOTAVAILABLE);
                }
                // 查询已借款的记录
                AccountDebitAsset record = new AccountDebitAsset();
                record.setStockinfoId(debitAssetModel.getRelatedStockinfoId());// 法定货币
                record.setRelatedStockinfoId(debitAssetModel.getRelatedStockinfoId());// 法定货币
                record.setBorrowerAccountId(debitAssetModel.getBorrowerAccountId());
                record.setTableName(getStockInfo(debitAssetModel.getRelatedStockinfoId()).getTableDebitAsset());
                List<AccountDebitAsset> list = accountDebitAssetService.findList(record);
                if (list.size() > 0)
                {
                    logger.debug("法定货币借款：已经存在借款 更新借款");
                    AccountDebitAsset accountDebitAsset = list.get(0);
                    // 行锁要取最新记录
                    accountDebitAsset = accountDebitAssetService.selectByPrimaryKeyForUpdate(getStockInfo(accountDebitAsset.getRelatedStockinfoId()).getTableDebitAsset(),
                            accountDebitAsset.getId());
                    logger.debug("法定货币借款：已经存在借款信息 " + accountDebitAsset.toString());
                    accountDebitAsset.setDebitAmt(accountDebitAsset.getDebitAmt().setScale(12, BigDecimal.ROUND_HALF_UP).add(debitAssetModel.getDebitAmt()));
                    accountDebitAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                    accountDebitAsset.setTableName(getStockInfo(debitAssetModel.getRelatedStockinfoId()).getTableDebitAsset());
                    accountDebitAssetService.updateByPrimaryKey(accountDebitAsset);
                    // 资产处理
                    FundModel fundModel = new FundModel();
                    fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT);
                    fundModel.setStockinfoId(debitAssetModel.getRelatedStockinfoId());// 法定货币
                    fundModel.setStockinfoIdEx(debitAssetModel.getRelatedStockinfoId());// 法定货币
                    fundModel.setAmount(debitAssetModel.getDebitAmt());
                    fundModel.setFee(BigDecimal.ZERO);
                    fundModel.setAccountId(debitAssetModel.getBorrowerAccountId());
                    fundModel.setCreateBy(debitAssetModel.getBorrowerAccountId());
                    fundService.fundTransaction(fundModel);
                    // 返回对象
                    debitAssetModel.setDebitAmtSum(accountDebitAsset.getDebitAmt());
                    debitAssetModel.setDebitRecordId(accountDebitAsset.getId());
                }
                else
                {
                    logger.debug("法定货币借款：不存在借款 插入借款");
                    Long id = SerialnoUtils.buildPrimaryKey();
                    AccountDebitAsset accountDebitAsset = new AccountDebitAsset();
                    accountDebitAsset.setId(id);
                    accountDebitAsset.setBorrowerAccountId(debitAssetModel.getBorrowerAccountId());
                    accountDebitAsset.setLenderAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
                    accountDebitAsset.setStockinfoId(debitAssetModel.getRelatedStockinfoId());// 法定货币
                    accountDebitAsset.setRelatedStockinfoId(debitAssetModel.getRelatedStockinfoId());// 法定货币
                    accountDebitAsset.setTableName(getStockInfo(debitAssetModel.getRelatedStockinfoId()).getTableDebitAsset());
                    accountDebitAsset.setDebitAmt(debitAssetModel.getDebitAmt());
                    accountDebitAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                    accountDebitAsset.setAccumulateInterest(BigDecimal.ZERO);
                    accountDebitAsset.setLastInterestDay(0L);
                    accountDebitAssetService.insert(accountDebitAsset);
                    logger.debug("法定货币借款：不存在借款 插入借款 " + accountDebitAsset.toString());
                    // 资产处理
                    FundModel fundModel = new FundModel();
                    fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT);
                    fundModel.setStockinfoId(debitAssetModel.getRelatedStockinfoId());// 法定货币
                    fundModel.setStockinfoIdEx(debitAssetModel.getRelatedStockinfoId());// 法定货币
                    fundModel.setAmount(debitAssetModel.getDebitAmt());
                    fundModel.setFee(BigDecimal.ZERO);
                    fundModel.setAccountId(debitAssetModel.getBorrowerAccountId());
                    fundModel.setCreateBy(debitAssetModel.getBorrowerAccountId());
                    fundService.fundTransaction(fundModel);
                    // 返回对象
                    debitAssetModel.setDebitAmtSum(accountDebitAsset.getDebitAmt());
                    debitAssetModel.setDebitRecordId(id);
                }
            }
            // 借数字货币
            else if (FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH == debitAssetModel.getStockinfoType())
            {
                // BTC保证金可用判断
                EnableModel enableModel = new EnableModel();
                enableModel.setAccountId(debitAssetModel.getBorrowerAccountId());
                enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT);
                enableModel.setStockinfoId(debitAssetModel.getStockinfoId());// 数字货币
                enableModel.setRelatedStockinfoId(debitAssetModel.getRelatedStockinfoId());// 法定货币
                enableModel.setMaxLongLever(debitAssetModel.getMaxLongLever());
                enableModel.setMaxShortLever(debitAssetModel.getMaxShortLever());
                enableModel = enableService.entrustTerminalEnable(enableModel);
                logger.debug("数字货币借款：数字货币最大可借数量=" + enableModel.getEnableAmount());
                if (enableModel.getEnableAmount().compareTo(BigDecimal.ZERO) <= 0)
                {
                    logger.debug("数字货币借款：数字货币最大可借数量不足");
                    throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
                }
                BigDecimal maxBorrow = isVCoin ? enableModel.getEnableAmount() : enableModel.getEnableAmount().multiply(platPrice);
                if (debitAssetModel.getDebitAmt().compareTo(BigDecimal.ZERO) <= 0 || debitAssetModel.getDebitAmt().compareTo(maxBorrow) > 0)
                {
                    logger.debug("数字货币借款：借款范围错误");
                    throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
                }
                AccountDebitAsset recordEntity = new AccountDebitAsset();
                recordEntity.setStockinfoId(debitAssetModel.getStockinfoId());// 法定货币
                recordEntity.setRelatedStockinfoId(debitAssetModel.getRelatedStockinfoId());// 法定货币
                recordEntity.setTableName(getStockInfo(debitAssetModel.getRelatedStockinfoId()).getTableDebitAsset());
                BigDecimal sumDebitAmt = accountDebitAssetService.findSumDebitAmt(recordEntity);
                BigDecimal upLine = isVCoin ? stockInfo.getTradeDebitTotal() : stockInfo.getCapitalDebitTotal();
                if (sumDebitAmt.add(debitAssetModel.getDebitAmt()).compareTo(upLine) < 0)
                {
                    logger.debug("数字货币借款：放贷余额不足");
                    throw new BusinessException(CommonEnums.RISK_ENABLE_DEBIT_NOTAVAILABLE);
                }
                // 查询已借款的记录
                AccountDebitAsset record = new AccountDebitAsset();
                record.setStockinfoId(debitAssetModel.getStockinfoId());// 数字货币
                record.setRelatedStockinfoId(debitAssetModel.getRelatedStockinfoId());// 法定货币
                record.setBorrowerAccountId(debitAssetModel.getBorrowerAccountId());
                record.setTableName(getStockInfo(debitAssetModel.getRelatedStockinfoId()).getTableDebitAsset());
                List<AccountDebitAsset> list = accountDebitAssetService.findList(record);
                if (list.size() > 0)
                {
                    logger.debug("数字货币借款：已经存在借款 更新借款");
                    AccountDebitAsset accountDebitAsset = list.get(0);
                    // 行锁要取最新记录
                    accountDebitAsset = accountDebitAssetService.selectByPrimaryKeyForUpdate(getStockInfo(accountDebitAsset.getRelatedStockinfoId()).getTableDebitAsset(),
                            accountDebitAsset.getId());
                    logger.debug("数字货币借款：已经存在借款信息 " + accountDebitAsset.toString());
                    accountDebitAsset.setDebitAmt(accountDebitAsset.getDebitAmt().setScale(12, BigDecimal.ROUND_HALF_UP).add(debitAssetModel.getDebitAmt()));
                    accountDebitAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                    accountDebitAsset.setTableName(getStockInfo(accountDebitAsset.getRelatedStockinfoId()).getTableDebitAsset());
                    accountDebitAssetService.updateByPrimaryKey(accountDebitAsset);
                    // 资产处理
                    FundModel fundModel = new FundModel();
                    fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT);
                    fundModel.setStockinfoId(debitAssetModel.getStockinfoId());// 数字货币
                    fundModel.setStockinfoIdEx(debitAssetModel.getRelatedStockinfoId());// 法定货币
                    fundModel.setAmount(debitAssetModel.getDebitAmt());
                    fundModel.setFee(BigDecimal.ZERO);
                    fundModel.setAccountId(debitAssetModel.getBorrowerAccountId());
                    fundModel.setCreateBy(debitAssetModel.getBorrowerAccountId());
                    fundService.fundTransaction(fundModel);
                    // 返回对象
                    debitAssetModel.setDebitAmtSum(accountDebitAsset.getDebitAmt());
                    debitAssetModel.setDebitRecordId(accountDebitAsset.getId());
                }
                else
                {
                    logger.debug("数字货币借款：不存在借款 插入借款");
                    Long id = SerialnoUtils.buildPrimaryKey();
                    AccountDebitAsset accountDebitAsset = new AccountDebitAsset();
                    accountDebitAsset.setId(id);
                    accountDebitAsset.setBorrowerAccountId(debitAssetModel.getBorrowerAccountId());
                    accountDebitAsset.setLenderAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
                    accountDebitAsset.setStockinfoId(debitAssetModel.getStockinfoId());
                    accountDebitAsset.setRelatedStockinfoId(debitAssetModel.getRelatedStockinfoId());
                    accountDebitAsset.setDebitAmt(debitAssetModel.getDebitAmt());
                    accountDebitAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                    accountDebitAsset.setAccumulateInterest(BigDecimal.ZERO);
                    accountDebitAsset.setLastInterestDay(0L);
                    accountDebitAsset.setTableName(getStockInfo(debitAssetModel.getRelatedStockinfoId()).getTableDebitAsset());
                    accountDebitAssetService.insert(accountDebitAsset);
                    logger.debug("数字货币借款：不存在借款 插入借款 " + accountDebitAsset.toString());
                    // 资产处理
                    FundModel fundModel = new FundModel();
                    fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT);
                    fundModel.setStockinfoId(debitAssetModel.getStockinfoId());// 数字货币
                    fundModel.setStockinfoIdEx(debitAssetModel.getRelatedStockinfoId());// 法定货币
                    fundModel.setAmount(debitAssetModel.getDebitAmt());
                    fundModel.setFee(BigDecimal.ZERO);
                    fundModel.setAccountId(debitAssetModel.getBorrowerAccountId());
                    fundModel.setCreateBy(debitAssetModel.getBorrowerAccountId());
                    fundService.fundTransaction(fundModel);
                    // 返回对象
                    debitAssetModel.setDebitAmtSum(accountDebitAsset.getDebitAmt());
                    debitAssetModel.setDebitRecordId(id);
                }
            }
            else
            {
                logger.debug("不支持的货币借款类型：" + debitAssetModel.getStockinfoId());
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
        }
        // 杠杆现货交易
        else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_LEVERAGEDSPOT))
        {
            // 行所
            try
            {
                accountSpotAssetService.selectForUpdate(debitAssetModel.getBorrowerAccountId(), stockInfo.getTradeStockinfoId(),
                        getStockInfo(debitAssetModel.getRelatedStockinfoId()).getCapitalStockinfoId());
                accountSpotAssetService.selectForUpdate(debitAssetModel.getBorrowerAccountId(), stockInfo.getCapitalStockinfoId(),
                        getStockInfo(debitAssetModel.getRelatedStockinfoId()).getCapitalStockinfoId());
            }
            catch (Exception e)
            {
                logger.debug(e.getStackTrace().toString());
                throw new BusinessException(CommonEnums.ERROR_SYS_BUSY);
            }
            RtQuotationInfo rtQuotationInfo = rtQuotationInfoService.queryRtQuotationInfo(debitAssetModel.getStockinfoId(), debitAssetModel.getRelatedStockinfoId());
            BigDecimal platPrice = BigDecimal.ZERO;
            if (rtQuotationInfo != null)
            {
                platPrice = rtQuotationInfo.getPlatPrice();
            }
            logger.debug("行情=" + platPrice);
            // 数字货币可最大可借数量
            EnableModel enableModel = new EnableModel();
            enableModel.setAccountId(debitAssetModel.getBorrowerAccountId());
            enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT);
            enableModel.setStockinfoId(debitAssetModel.getStockinfoId());
            enableModel.setRelatedStockinfoId(debitAssetModel.getRelatedStockinfoId());
            enableModel.setMaxLongLever(debitAssetModel.getMaxLongLever());
            enableModel.setMaxShortLever(debitAssetModel.getMaxShortLever());
            enableModel = enableService.entrustTerminalEnable(enableModel);
            logger.debug("杠杆现货借款：数字货币最大可借数量=" + enableModel.getEnableAmount());
            if (enableModel.getEnableAmount().compareTo(BigDecimal.ZERO) <= 0)
            {
                logger.debug("杠杆现货借款：数字货币最大可借数量不足");
                throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
            }
            // 法定货币最大可借数量
            isVCoin = stockInfo.getTradeStockinfoId().longValue() != debitAssetModel.getStockinfoId().longValue();
            BigDecimal maxBorrow = isVCoin ? enableModel.getEnableAmount().multiply(platPrice) : enableModel.getEnableAmount();
            logger.debug("杠杆现货借款：法定货币最大可借数量=" + maxBorrow);
            if (debitAssetModel.getDebitAmt().compareTo(BigDecimal.ZERO) <= 0 || debitAssetModel.getDebitAmt().compareTo(maxBorrow) > 0)
            {
                logger.debug("杠杆现货借款：借款范围错误");
                throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
            }
            StockInfo stock = stockInfoService.selectByPrimaryKey(debitAssetModel.getStockinfoId());
            AccountDebitAsset recordEntity = new AccountDebitAsset();
            recordEntity.setTableName(getStockInfo(debitAssetModel.getRelatedStockinfoId()).getTableDebitAsset());
            recordEntity.setStockinfoId(debitAssetModel.getStockinfoId());
            recordEntity.setRelatedStockinfoId(stockInfo.getCapitalStockinfoId());
            BigDecimal sumDebitAmt = accountDebitAssetService.findSumDebitAmt(recordEntity);
            BigDecimal upLine = stock.getTradeDebitTotal();
            logger.debug("upLine=" + upLine);
            logger.debug("sumDebitAmt=" + sumDebitAmt);
            logger.debug("debitAmt=" + debitAssetModel.getDebitAmt());
            if (sumDebitAmt.add(debitAssetModel.getDebitAmt()).compareTo(upLine) > 0)
            {
                logger.debug("数字货币借款：放贷余额不足");
                throw new BusinessException(CommonEnums.RISK_ENABLE_DEBIT_NOTAVAILABLE);
            }
            // 查询已借款的记录
            AccountDebitAsset record = new AccountDebitAsset();
            record.setTableName(getStockInfo(debitAssetModel.getRelatedStockinfoId()).getTableDebitAsset());
            record.setStockinfoId(debitAssetModel.getStockinfoId());
            record.setRelatedStockinfoId(stockInfo.getCapitalStockinfoId());
            record.setBorrowerAccountId(debitAssetModel.getBorrowerAccountId());
            List<AccountDebitAsset> list = accountDebitAssetService.findList(record);
            if (list.size() > 0)
            {
                logger.debug("杠杆现货借款：已经存在借款 更新借款");
                AccountDebitAsset accountDebitAsset = list.get(0);
                // 行锁要取最新记录
                accountDebitAsset = accountDebitAssetService.selectByPrimaryKeyForUpdate(getStockInfo(accountDebitAsset.getRelatedStockinfoId()).getTableDebitAsset(),
                        accountDebitAsset.getId());
                logger.debug("杠杆现货借款：已经存在借款信息 " + accountDebitAsset.toString());
                accountDebitAsset.setDebitAmt(accountDebitAsset.getDebitAmt().setScale(12, BigDecimal.ROUND_HALF_UP).add(debitAssetModel.getDebitAmt()));
                accountDebitAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                accountDebitAsset.setTableName(getStockInfo(accountDebitAsset.getRelatedStockinfoId()).getTableDebitAsset());
                accountDebitAssetService.updateByPrimaryKey(accountDebitAsset);
                // 资产处理
                FundModel fundModel = new FundModel();
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT);
                fundModel.setStockinfoId(debitAssetModel.getStockinfoId());
                fundModel.setStockinfoIdEx(debitAssetModel.getRelatedStockinfoId());
                fundModel.setAmount(debitAssetModel.getDebitAmt());
                fundModel.setFee(BigDecimal.ZERO);
                fundModel.setAccountId(debitAssetModel.getBorrowerAccountId());
                fundModel.setCreateBy(debitAssetModel.getBorrowerAccountId());
                fundService.fundTransaction(fundModel);
                // 返回对象
                debitAssetModel.setDebitAmtSum(accountDebitAsset.getDebitAmt());
                debitAssetModel.setDebitRecordId(accountDebitAsset.getId());
            }
            else
            {
                logger.debug("杠杆现货借款：不存在借款 插入借款");
                Long id = SerialnoUtils.buildPrimaryKey();
                AccountDebitAsset accountDebitAsset = new AccountDebitAsset();
                accountDebitAsset.setId(id);
                accountDebitAsset.setBorrowerAccountId(debitAssetModel.getBorrowerAccountId());
                accountDebitAsset.setLenderAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
                accountDebitAsset.setTableName(getStockInfo(debitAssetModel.getRelatedStockinfoId()).getTableDebitAsset());
                accountDebitAsset.setStockinfoId(debitAssetModel.getStockinfoId());// 法定货币
                accountDebitAsset.setRelatedStockinfoId(stockInfo.getCapitalStockinfoId());
                accountDebitAsset.setDebitAmt(debitAssetModel.getDebitAmt());
                accountDebitAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                accountDebitAsset.setAccumulateInterest(BigDecimal.ZERO);
                accountDebitAsset.setLastInterestDay(0L);
                logger.debug("杠杆现货借款 插入借款 " + accountDebitAsset.toString());
                accountDebitAssetService.insert(accountDebitAsset);
                // 资产处理
                FundModel fundModel = new FundModel();
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT);
                fundModel.setStockinfoId(debitAssetModel.getStockinfoId());
                fundModel.setStockinfoIdEx(debitAssetModel.getRelatedStockinfoId());
                fundModel.setAmount(debitAssetModel.getDebitAmt());
                fundModel.setFee(BigDecimal.ZERO);
                fundModel.setAccountId(debitAssetModel.getBorrowerAccountId());
                fundModel.setCreateBy(debitAssetModel.getBorrowerAccountId());
                fundService.fundTransaction(fundModel);
                // 返回对象
                debitAssetModel.setDebitAmtSum(accountDebitAsset.getDebitAmt());
                debitAssetModel.setDebitRecordId(id);
            }
        }
        else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_PURESPOT))
        {
            logger.debug("不支持的货币借款类型：" + debitAssetModel.getStockinfoId());
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        else
        {
            logger.debug("证券信息类型错误");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        return debitAssetModel;
    }
    
    /**
     * 超级用户向自己借款
     * @param debitAssetModel
     * @return
     */
    @Override
    public DebitAssetModel doDebitSuperAdminBorrowFromPlat(DebitAssetModel debitAssetModel) throws BusinessException
    {
        if (debitAssetModel.getStockinfoId() == null || debitAssetModel.getRelatedStockinfoId() == null
                || debitAssetModel.getBorrowerAccountId() == null) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        if (debitAssetModel.getDebitAmt().compareTo(BigDecimal.ZERO) <= 0)
        {
            logger.debug("借款数量或金额不在范围内");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        // 查询已借款的记录
        AccountDebitAsset record = new AccountDebitAsset();
        record.setStockinfoId(debitAssetModel.getStockinfoId());
        record.setRelatedStockinfoId(debitAssetModel.getRelatedStockinfoId());
        record.setBorrowerAccountId(debitAssetModel.getBorrowerAccountId());
        record.setTableName(getStockInfo(debitAssetModel.getRelatedStockinfoId()).getTableDebitAsset());
        List<AccountDebitAsset> list = accountDebitAssetService.findList(record);
        if (list.size() > 0)
        {
            logger.debug("法定货币借款：已经存在借款 更新借款");
            AccountDebitAsset accountDebitAsset = list.get(0);
            logger.debug("法定货币借款：已经存在借款信息 " + accountDebitAsset.toString());
            accountDebitAsset.setDebitAmt(accountDebitAsset.getDebitAmt().setScale(12, BigDecimal.ROUND_HALF_UP).add(debitAssetModel.getDebitAmt()));
            accountDebitAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            accountDebitAsset.setTableName(getStockInfo(debitAssetModel.getRelatedStockinfoId()).getTableDebitAsset());
            accountDebitAssetService.updateByPrimaryKey(accountDebitAsset);
            // 资产处理
            FundModel fundModel = new FundModel();
            if (debitAssetModel.getStockinfoId().longValue() == FundConsts.WALLET_BTC_TYPE)
            {
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_AUTO_BORROW_VCOIN);
            }
            if (debitAssetModel.getStockinfoId().longValue() == FundConsts.WALLET_BTC2USDX_TYPE)
            {
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_AUTO_BORROW_MONEY);
            }
            fundModel.setStockinfoId(debitAssetModel.getStockinfoId());
            fundModel.setStockinfoIdEx(debitAssetModel.getRelatedStockinfoId());
            fundModel.setAmount(debitAssetModel.getDebitAmt());
            fundModel.setFee(BigDecimal.ZERO);
            fundModel.setAccountId(debitAssetModel.getBorrowerAccountId());
            fundModel.setCreateBy(debitAssetModel.getBorrowerAccountId());
            fundService.fundTransaction(fundModel);
            // 返回对象
            debitAssetModel.setDebitAmtSum(accountDebitAsset.getDebitAmt());
            debitAssetModel.setDebitRecordId(accountDebitAsset.getId());
        }
        else
        {
            logger.debug("法定货币借款：不存在借款 插入借款");
            Long id = SerialnoUtils.buildPrimaryKey();
            AccountDebitAsset accountDebitAsset = new AccountDebitAsset();
            accountDebitAsset.setId(id);
            accountDebitAsset.setBorrowerAccountId(debitAssetModel.getBorrowerAccountId());
            accountDebitAsset.setLenderAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
            accountDebitAsset.setStockinfoId(debitAssetModel.getStockinfoId());
            accountDebitAsset.setRelatedStockinfoId(debitAssetModel.getRelatedStockinfoId());
            accountDebitAsset.setDebitAmt(debitAssetModel.getDebitAmt());
            accountDebitAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            accountDebitAsset.setAccumulateInterest(BigDecimal.ZERO);
            accountDebitAsset.setLastInterestDay(0L);
            accountDebitAsset.setTableName(getStockInfo(debitAssetModel.getRelatedStockinfoId()).getTableDebitAsset());
            accountDebitAssetService.insert(accountDebitAsset);
            logger.debug("法定货币借款：不存在借款 插入借款 " + accountDebitAsset.toString());
            // 资产处理
            FundModel fundModel = new FundModel();
            if (debitAssetModel.getStockinfoId().longValue() == FundConsts.WALLET_BTC_TYPE)
            {
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_AUTO_BORROW_VCOIN);
            }
            if (debitAssetModel.getStockinfoId().longValue() == FundConsts.WALLET_BTC2USDX_TYPE)
            {
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_AUTO_BORROW_MONEY);
            }
            fundModel.setStockinfoId(debitAssetModel.getStockinfoId());
            fundModel.setStockinfoIdEx(debitAssetModel.getRelatedStockinfoId());
            fundModel.setAmount(debitAssetModel.getDebitAmt());
            fundModel.setFee(BigDecimal.ZERO);
            fundModel.setAccountId(debitAssetModel.getBorrowerAccountId());
            fundModel.setCreateBy(debitAssetModel.getBorrowerAccountId());
            fundService.fundTransaction(fundModel);
            // 返回对象
            debitAssetModel.setDebitAmtSum(accountDebitAsset.getDebitAmt());
            debitAssetModel.setDebitRecordId(id);
        }
        return debitAssetModel;
    }
    
    /**
     * 向平台自动还款
     * @return
     */
    @Override
    public void autoDebitRepaymentToPlat(StockInfo stockInfo)
    {
        String stockinfoType = "";
        if (checkSwitch())// 交易开关判断
        {
            AccountDebitAsset entity = new AccountDebitAsset();
            entity.setRelatedStockinfoId(stockInfo.getCapitalStockinfoId());
            entity.setTableName(stockInfo.getTableDebitAsset());
            List<AccountDebitAsset> list = accountDebitAssetService.findListForDebit(entity);
            if (list.size() == 0)
            {
                logger.debug("目前还没有借款的用户");
            }
            for (AccountDebitAsset record : list)
            {
                if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_CONTRACTSPOT))
                {
                    boolean isYes = stockInfo.getTradeStockinfoId().longValue() != stockInfo.getId().longValue();
                    if (isYes)
                    {
                        boolean isVCoin = stockInfo.getTradeStockinfoId().longValue() == record.getStockinfoId().longValue();
                        stockinfoType = (isVCoin ? FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH : FundConsts.BORROW_STOCKINFO_TYPE_LEGAL_MONEY);
                    }
                    else
                    {
                        boolean isVCoin = stockInfo.getTradeStockinfoId().longValue() == record.getStockinfoId().longValue();
                        stockinfoType = (isVCoin ? FundConsts.BORROW_STOCKINFO_TYPE_LEGAL_MONEY : FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH);
                    }
                }
                else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_LEVERAGEDSPOT))
                {
                    stockinfoType = FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH;
                }
                else
                {
                    logger.debug("证券信息类型错误");
                    throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
                }
                // 每个用户的还款处理（带有行锁）
                try
                {
                    logger.debug("正常开始 借款ID=" + record.getId() + " 用户ID=" + record.getBorrowerAccountId() + record.toString());
                    accountDebitAssetService.doAccountDebitRepaymentToPlat(stockinfoType, record.getStockinfoId(), stockInfo.getId(), record.getBorrowerAccountId());
                    logger.debug("正常结束 借款ID=" + record.getId() + " 用户ID=" + record.getBorrowerAccountId() + record.toString());
                }
                catch (Exception e)
                {
                    logger.debug("异常结束 借款ID=" + record.getId() + " 用户ID=" + record.getBorrowerAccountId() + record.toString());
                    continue;
                }
            }
        }
    }
    
    /**
     * 用户向平台还款
     * @param stockinfoId 借款stockinfoId
     * @param relatedStockinfoId 查询余额关联stockinfoId
     * @param accountId 用户id
     * @return
     */
    @Override
    public void doAccountDebitRepaymentToPlat(String stockinfoType, Long stockinfoId, Long relatedStockinfoId, Long accountId) throws BusinessException
    {
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(relatedStockinfoId);
        if (stockInfo == null)
        {
            logger.debug("证券ID不存在，非法下单");
            throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
        }
        // 合约资产交易还款
        if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_CONTRACTSPOT))
        {
            accountContractAssetService.selectByPrimaryKeyOnRowLock(accountId, stockinfoId, relatedStockinfoId, getStockInfo(relatedStockinfoId).getTableAsset());
            accountContractAssetService.selectByPrimaryKeyOnRowLock(accountId, relatedStockinfoId, relatedStockinfoId, getStockInfo(relatedStockinfoId).getTableAsset());
            AccountDebitAsset entity = new AccountDebitAsset();
            entity.setStockinfoId(stockinfoId);
            entity.setRelatedStockinfoId(relatedStockinfoId);
            entity.setTableName(getStockInfo(relatedStockinfoId).getTableDebitAsset());
            entity.setBorrowerAccountId(accountId);
            List<AccountDebitAsset> list = accountDebitAssetService.findListForDebit(entity);
            if (list.size() > 0)
            {
                AccountDebitAsset record = new AccountDebitAsset();
                record.setDebitAmt(BigDecimal.ZERO);
                try
                {
                    record = list.get(0);
                    // 行锁要取最新记录集
                    record = accountDebitAssetService.selectByPrimaryKeyForUpdate(getStockInfo(record.getRelatedStockinfoId()).getTableDebitAsset(), record.getId());
                    logger.debug("正常开始 读行锁 借款ID=" + record.getId() + " 用户ID=" + record.getBorrowerAccountId() + record.toString());
                    accountContractAssetService.selectByPrimaryKeyOnRowLock(accountId, stockinfoId, relatedStockinfoId, getStockInfo(relatedStockinfoId).getTableAsset());
                    accountContractAssetService.selectByPrimaryKeyOnRowLock(accountId, relatedStockinfoId, relatedStockinfoId,
                            getStockInfo(relatedStockinfoId).getTableAsset());
                    logger.debug("正常结束 读行锁 借款ID=" + record.getId() + " 用户ID=" + record.getBorrowerAccountId() + record.toString());
                }
                catch (Exception e)
                {
                    logger.debug("异常结束 读行锁 借款ID=" + record.getId() + " 用户ID=" + record.getBorrowerAccountId() + record.toString());
                    logger.debug(e.getStackTrace().toString());
                    logger.debug(e.getMessage());
                    logger.debug(e.getLocalizedMessage());
                    logger.debug(e.getStackTrace().toString());
                    throw new BusinessException(CommonEnums.ERROR_SYS_BUSY);
                }
                if (record.getDebitAmt().compareTo(BigDecimal.ZERO) > 0)
                {
                    logger.debug("进入还款 借款ID=" + record.getId() + " 用户ID=" + record.getBorrowerAccountId());
                    // 查询可用
                    EnableModel enableModel = new EnableModel();
                    enableModel.setAccountId(record.getBorrowerAccountId());
                    if (StringUtils.equalsIgnoreCase(stockinfoType, FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH))
                    {
                        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT);
                    }
                    else if (StringUtils.equalsIgnoreCase(stockinfoType, FundConsts.BORROW_STOCKINFO_TYPE_LEGAL_MONEY))
                    {
                        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT);
                    }
                    else
                    {
                        logger.debug("错误的证券类别");
                        throw new BusinessException("错误的证券类别");
                    }
                    enableModel.setStockinfoId(stockinfoId);
                    enableModel.setRelatedStockinfoId(relatedStockinfoId);
                    enableModel = enableService.entrustTerminalEnable(enableModel);
                    logger.debug("可用余额" + enableModel.toString());
                    logger.debug("可用余额" + enableModel.getEnableAmount() + " 贷款金额=" + record.getDebitAmt());
                    if (enableModel.getEnableAmount().compareTo(record.getDebitAmt()) < 0)
                    { // 借款大于可用余额
                        logger.debug("进入还款 借款大于可用 部分还款 借款ID=" + record.getId() + " 用户ID=" + record.getBorrowerAccountId());
                        if (enableModel.getEnableAmount().compareTo(BigDecimal.ZERO) > 0)
                        { // 有余额进行资产处理
                            FundModel fundModel = new FundModel();
                            if (StringUtils.equalsIgnoreCase(stockinfoType, FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH))
                            {
                                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT);
                            }
                            else if (StringUtils.equalsIgnoreCase(stockinfoType, FundConsts.BORROW_STOCKINFO_TYPE_LEGAL_MONEY))
                            {
                                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT);
                            }
                            else
                            {
                                logger.debug("错误的证券类别");
                                throw new BusinessException("错误的证券类别");
                            }
                            fundModel.setStockinfoId(stockinfoId);
                            fundModel.setStockinfoIdEx(relatedStockinfoId);
                            fundModel.setAmount(enableModel.getEnableAmount());
                            fundModel.setAmountEx(record.getDebitAmt());
                            fundModel.setOriginalBusinessId(record.getId());
                            fundModel.setFee(BigDecimal.ZERO);
                            fundModel.setAccountId(record.getBorrowerAccountId());
                            fundModel.setCreateBy(record.getBorrowerAccountId());
                            fundService.fundTransaction(fundModel);
                            // 扣除可用
                            record.setDebitAmt(record.getDebitAmt().subtract(enableModel.getEnableAmount()));
                            record.setAccumulateInterest((record.getAccumulateInterest().subtract(enableModel.getEnableAmount())).compareTo(BigDecimal.ZERO) >= 0
                                    ? (record.getAccumulateInterest().subtract(enableModel.getEnableAmount()))
                                    : BigDecimal.ZERO);
                            record.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                            record.setTableName(getStockInfo(relatedStockinfoId).getTableDebitAsset());
                            accountDebitAssetService.updateByPrimaryKey(record);
                            logger.debug("进入还款 借款大于可用 部分还款 借款ID=" + record.getId() + " 用户ID=" + record.getBorrowerAccountId() + fundModel.toString());
                        }
                        else
                        { // 可用余额为0
                            StringBuilder sb = new StringBuilder("accountId=");
                            sb.append(record.getBorrowerAccountId());
                            sb.append("debitRecordId=");
                            sb.append(record.getId());
                            sb.append("余额不足不能还款");
                            logger.debug(sb.toString());
                        }
                    }
                    else
                    {
                        logger.debug("进入还款 可用足够 全部还款 借款ID=" + record.getId() + " 用户ID=" + record.getBorrowerAccountId());
                        // 可用余额大于借款 资产处理
                        FundModel fundModel = new FundModel();
                        if (StringUtils.equalsIgnoreCase(stockinfoType, FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH))
                        {
                            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT);
                        }
                        else if (StringUtils.equalsIgnoreCase(stockinfoType, FundConsts.BORROW_STOCKINFO_TYPE_LEGAL_MONEY))
                        {
                            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT);
                        }
                        else
                        {
                            logger.debug("错误的证券类别");
                            throw new BusinessException("错误的证券类别");
                        }
                        fundModel.setStockinfoId(stockinfoId);
                        fundModel.setStockinfoIdEx(relatedStockinfoId);
                        fundModel.setAmount(record.getDebitAmt());
                        fundModel.setAmountEx(record.getDebitAmt());
                        fundModel.setOriginalBusinessId(record.getId());
                        fundModel.setFee(BigDecimal.ZERO);
                        fundModel.setAccountId(record.getBorrowerAccountId());
                        fundModel.setCreateBy(record.getBorrowerAccountId());
                        logger.debug("进入还款 可用足够 全部还款 借款ID=" + record.getId() + " 用户ID=" + record.getBorrowerAccountId() + fundModel.toString());
                        fundService.fundTransaction(fundModel);
                        // 成功的用户 借款清零
                        record.setDebitAmt(BigDecimal.ZERO);
                        record.setAccumulateInterest(BigDecimal.ZERO);
                        record.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                        record.setTableName(getStockInfo(relatedStockinfoId).getTableDebitAsset());
                        logger.debug("还款后 借款情况：" + record.toString());
                        accountDebitAssetService.updateByPrimaryKey(record);
                    }
                }
                else
                {
                    logger.debug("正常结束 无借款 借款ID=" + record.getId() + " 用户ID=" + record.getBorrowerAccountId());
                }
            }
        }
        // 杠杆现货还款处理
        else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_LEVERAGEDSPOT))
        {
            // accountSpotAsset行锁
            accountSpotAssetService.selectForUpdate(accountId, stockinfoId, getStockInfo(relatedStockinfoId).getCapitalStockinfoId());
            AccountDebitAsset entity = new AccountDebitAsset();
            entity.setStockinfoId(stockinfoId);
            entity.setRelatedStockinfoId(getStockInfo(relatedStockinfoId).getCapitalStockinfoId());
            entity.setBorrowerAccountId(accountId);
            entity.setTableName(getStockInfo(relatedStockinfoId).getTableDebitAsset());
            List<AccountDebitAsset> list = accountDebitAssetService.findListForDebit(entity);
            if (list.size() > 0)
            {
                AccountDebitAsset record = new AccountDebitAsset();
                record.setDebitAmt(BigDecimal.ZERO);
                try
                {
                    record = list.get(0);
                    // 行锁取最新的记录集
                    record = accountDebitAssetService.selectByPrimaryKeyForUpdate(getStockInfo(record.getRelatedStockinfoId()).getTableDebitAsset(), record.getId());
                    logger.debug("正常开始 读行锁 借款ID=" + record.getId() + " 用户ID=" + record.getBorrowerAccountId() + record.toString());
                }
                catch (Exception e)
                {
                    logger.debug("异常结束 读行锁 借款ID=" + record.getId() + " 用户ID=" + record.getBorrowerAccountId() + record.toString());
                    logger.debug(e.getStackTrace().toString());
                    logger.debug(e.getMessage());
                    logger.debug(e.getLocalizedMessage());
                    logger.debug(e.getStackTrace().toString());
                    throw new BusinessException(CommonEnums.ERROR_SYS_BUSY);
                }
                if (record.getDebitAmt().compareTo(BigDecimal.ZERO) > 0)
                {
                    logger.debug("进入还款 借款ID=" + record.getId() + " 用户ID=" + record.getBorrowerAccountId());
                    // 查询可用
                    EnableModel enableModel = new EnableModel();
                    enableModel.setAccountId(record.getBorrowerAccountId());
                    enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT);
                    enableModel.setStockinfoId(stockinfoId);
                    enableModel.setRelatedStockinfoId(relatedStockinfoId);
                    enableModel = enableService.entrustTerminalEnable(enableModel);
                    logger.debug("可用余额" + enableModel.toString());
                    logger.debug("可用余额" + enableModel.getEnableAmount() + " 贷款金额=" + record.getDebitAmt());
                    if (enableModel.getEnableAmount().compareTo(record.getDebitAmt()) < 0)
                    { // 借款大于可用余额
                        logger.debug("进入还款 借款大于可用 部分还款 借款ID=" + record.getId() + " 用户ID=" + record.getBorrowerAccountId());
                        if (enableModel.getEnableAmount().compareTo(BigDecimal.ZERO) > 0)
                        { // 有余额进行资产处理
                            FundModel fundModel = new FundModel();
                            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT);
                            fundModel.setStockinfoId(stockinfoId);
                            fundModel.setStockinfoIdEx(relatedStockinfoId);
                            fundModel.setAmount(enableModel.getEnableAmount());
                            fundModel.setAmountEx(record.getDebitAmt());
                            fundModel.setOriginalBusinessId(record.getId());
                            fundModel.setFee(BigDecimal.ZERO);
                            fundModel.setAccountId(record.getBorrowerAccountId());
                            fundModel.setCreateBy(record.getBorrowerAccountId());
                            fundService.fundTransaction(fundModel);
                            // 扣除可用
                            record.setDebitAmt(record.getDebitAmt().subtract(enableModel.getEnableAmount()));
                            record.setAccumulateInterest((record.getAccumulateInterest().subtract(enableModel.getEnableAmount())).compareTo(BigDecimal.ZERO) >= 0
                                    ? (record.getAccumulateInterest().subtract(enableModel.getEnableAmount()))
                                    : BigDecimal.ZERO);
                            record.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                            record.setTableName(getStockInfo(relatedStockinfoId).getTableDebitAsset());
                            accountDebitAssetService.updateByPrimaryKey(record);
                            logger.debug("进入还款 借款大于可用 部分还款 借款ID=" + record.getId() + " 用户ID=" + record.getBorrowerAccountId() + fundModel.toString());
                        }
                        else
                        { // 可用余额为0
                            StringBuilder sb = new StringBuilder("accountId=");
                            sb.append(record.getBorrowerAccountId());
                            sb.append("debitRecordId=");
                            sb.append(record.getId());
                            sb.append("余额不足不能还款");
                            logger.debug(sb.toString());
                        }
                    }
                    else
                    {
                        logger.debug("进入还款 可用足够 全部还款 借款ID=" + record.getId() + " 用户ID=" + record.getBorrowerAccountId());
                        // 可用余额大于借款 资产处理
                        FundModel fundModel = new FundModel();
                        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT);
                        fundModel.setStockinfoId(stockinfoId);
                        fundModel.setStockinfoIdEx(relatedStockinfoId);
                        fundModel.setAmount(record.getDebitAmt());
                        fundModel.setAmountEx(record.getDebitAmt());
                        fundModel.setOriginalBusinessId(record.getId());
                        fundModel.setFee(BigDecimal.ZERO);
                        fundModel.setAccountId(record.getBorrowerAccountId());
                        fundModel.setCreateBy(record.getBorrowerAccountId());
                        logger.debug("进入还款 可用足够 全部还款 借款ID=" + record.getId() + " 用户ID=" + record.getBorrowerAccountId() + fundModel.toString());
                        fundService.fundTransaction(fundModel);
                        // 成功的用户 借款清零
                        record.setDebitAmt(BigDecimal.ZERO);
                        record.setAccumulateInterest(BigDecimal.ZERO);
                        record.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                        record.setTableName(getStockInfo(relatedStockinfoId).getTableDebitAsset());
                        logger.debug("还款后 借款情况：" + record.toString());
                        accountDebitAssetService.updateByPrimaryKey(record);
                    }
                }
                else
                {
                    logger.debug("正常结束 无借款 借款ID=" + record.getId() + " 用户ID=" + record.getBorrowerAccountId());
                }
            }
        }
        else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_PURESPOT))
        {
            logger.debug("证券信息类型错误");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        else
        {
            logger.debug("证券信息类型错误");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        // 清除用户缓存
        clearAccountAssetCache(accountId, relatedStockinfoId);
    }
    
    /**
     * 强制还款给平台(不够用BTC折算成USDX还款给平台)
     * @param stockinfoType 货币类型：数字货币 法定货币
     * @param stockinfoId 借款stockinfoId
     * @param relatedStockinfoId 查询余额关联stockinfoId
     * @param deductionStockinfoId 抵扣关联stockinfoId
     * @return
     */
    @Override
    public void doDebitRepaymentPowerByPlat(String stockinfoType, Long stockinfoId, Long relatedStockinfoId, Long deductionStockinfoId) throws BusinessException
    {
        // 结算价
        BigDecimal settlementPrice = getSettlementPrice(relatedStockinfoId);
        logger.debug("结算价：" + settlementPrice);
        StockInfo stockInfo = getStockInfo(relatedStockinfoId);
        Long clearStockinfoId = stockInfo.getClearStockinfoId();
        Long tradeStockinfoId = stockInfo.getTradeStockinfoId();
        logger.debug("清算标的证券ID:" + clearStockinfoId);
        logger.debug("交易标的证券ID:" + tradeStockinfoId);
        // 清算标的证券ID不等于交易标的证券ID 需要将法币兑换成数字货币
        boolean isVCoin = clearStockinfoId.longValue() != tradeStockinfoId.longValue();
        logger.debug("是否交易数字货币：" + isVCoin);
        AccountDebitAsset entity = new AccountDebitAsset();
        entity.setStockinfoId(stockinfoId);
        entity.setRelatedStockinfoId(relatedStockinfoId);
        entity.setTableName(getStockInfo(relatedStockinfoId).getTableDebitAsset());
        List<AccountDebitAsset> list = accountDebitAssetService.findListForDebit(entity);
        for (AccountDebitAsset record : list)
        {
            try
            {
                record = accountDebitAssetService.selectByPrimaryKeyForUpdate(getStockInfo(record.getRelatedStockinfoId()).getTableDebitAsset(), record.getId());
            }
            catch (Exception e)
            {
                logger.debug(e.getStackTrace().toString());
                logger.debug(e.getMessage());
                logger.debug(e.getLocalizedMessage());
                logger.debug(e.getStackTrace().toString());
                throw new BusinessException(CommonEnums.ERROR_SYS_BUSY);
            }
            // 如果是法定货币
            if (StringUtils.equalsIgnoreCase(stockinfoType, FundConsts.BORROW_STOCKINFO_TYPE_LEGAL_MONEY))
            { // 法定货币强制还款
                if (record.getDebitAmt().compareTo(BigDecimal.ZERO) > 0)
                {
                    // 查询可用
                    EnableModel enableModel = new EnableModel();
                    enableModel.setAccountId(record.getBorrowerAccountId());
                    enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT);
                    enableModel.setStockinfoId(stockinfoId);
                    enableModel.setRelatedStockinfoId(relatedStockinfoId);
                    enableModel = enableService.entrustTerminalEnable(enableModel);
                    BigDecimal usdxEnable = enableModel.getEnableAmount();
                    logger.debug("借款ID：" + record.getId() + " 法定货币可用余额" + enableModel.getEnableAmount() + " 贷款金额=" + record.getDebitAmt());
                    if (enableModel.getEnableAmount().compareTo(record.getDebitAmt()) < 0)
                    {// 需要借款的还款处理
                        StringBuilder sb = new StringBuilder("借款ID：" + record.getId() + "accountId=");
                        sb.append(record.getBorrowerAccountId());
                        sb.append("debitRecordId=");
                        sb.append(record.getId());
                        sb.append("法定货币余额不足进入抵扣还款");
                        logger.debug(sb.toString());
                        enableModel = new EnableModel();
                        enableModel.setAccountId(record.getBorrowerAccountId());
                        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT);
                        enableModel.setStockinfoId(deductionStockinfoId);
                        enableModel.setRelatedStockinfoId(relatedStockinfoId);
                        enableModel = enableService.entrustTerminalEnable(enableModel);
                        BigDecimal btcEnable = enableModel.getEnableAmount();
                        BigDecimal needUsdx = record.getDebitAmt().subtract(usdxEnable);
                        logger.debug("借款ID：" + record.getId() + "还需法定货币数量：" + needUsdx);
                        BigDecimal needBtc = isVCoin ? needUsdx.divide(settlementPrice, 12, BigDecimal.ROUND_HALF_UP) : needUsdx.multiply(settlementPrice);
                        logger.debug("借款ID：" + record.getId() + " 结算价=" + settlementPrice);
                        logger.debug("借款ID：" + record.getId() + "还需法定货币折合数字货币=" + needBtc);
                        logger.debug("借款ID：" + record.getId() + "数字货币账户可用余额：" + enableModel.getEnableAmount());
                        logger.debug("借款ID：" + record.getId() + "去处理两笔资产");
                        // BTC足够的时候全还
                        if (btcEnable.compareTo(needBtc) >= 0)
                        {
                            logger.debug("借款ID：" + record.getId() + "数字货币数量足够，可以还清借款");
                            // 资产处理--法定货币减少
                            FundModel fundModel = new FundModel();
                            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT);
                            fundModel.setStockinfoId(stockinfoId);
                            fundModel.setStockinfoIdEx(relatedStockinfoId);
                            fundModel.setAmount(usdxEnable);
                            fundModel.setFee(BigDecimal.ZERO);
                            fundModel.setAccountId(record.getBorrowerAccountId());
                            fundModel.setCreateBy(record.getBorrowerAccountId());
                            if (usdxEnable.compareTo(BigDecimal.ZERO) > 0)// 法定货币完全没有余额 不需要扣除和记录流水
                            {
                                fundService.fundTransaction(fundModel);
                            }
                            else
                            {
                                logger.debug("没有足够的货币");
                            }
                            // 资产处理--数字货币减少
                            FundModel fundModelBtc = new FundModel();
                            fundModelBtc.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT);
                            fundModelBtc.setStockinfoId(deductionStockinfoId);
                            fundModelBtc.setStockinfoIdEx(relatedStockinfoId);
                            fundModelBtc.setAmount(needBtc);
                            fundModelBtc.setAmountEx(needUsdx);
                            fundModelBtc.setFee(BigDecimal.ZERO);
                            fundModelBtc.setAccountId(record.getBorrowerAccountId());
                            fundModelBtc.setCreateBy(record.getBorrowerAccountId());
                            if (btcEnable.compareTo(BigDecimal.ZERO) > 0)// 有可用余额时处理
                            {
                                fundService.fundTransaction(fundModelBtc);
                            }
                            else
                            {
                                logger.debug("没有足够的可抵用货币");
                            }
                            // 成功的用户 借款清零
                            record.setDebitAmt(BigDecimal.ZERO);
                            record.setAccumulateInterest(BigDecimal.ZERO);
                            record.setTableName(getStockInfo(relatedStockinfoId).getTableDebitAsset());
                            accountDebitAssetService.updateByPrimaryKey(record);
                        }
                        else
                        {
                            logger.debug("借款ID：" + record.getId() + " 数字货币数量不足，部分还款：");
                            // 资产处理--法定货币减少
                            FundModel fundModel = new FundModel();
                            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT);
                            fundModel.setStockinfoId(stockinfoId);
                            fundModel.setStockinfoIdEx(relatedStockinfoId);
                            fundModel.setAmount(usdxEnable);
                            fundModel.setFee(BigDecimal.ZERO);
                            fundModel.setAccountId(record.getBorrowerAccountId());
                            fundModel.setCreateBy(record.getBorrowerAccountId());
                            if (usdxEnable.compareTo(BigDecimal.ZERO) > 0)// 法定货币完全没有余额 不需要扣除和记录流水
                            {
                                fundService.fundTransaction(fundModel);
                            }
                            else
                            {
                                logger.debug("没有足够的货币");
                            }
                            // 资产处理--BTC减少
                            FundModel fundModelBtc = new FundModel();
                            fundModelBtc.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT);
                            fundModelBtc.setStockinfoId(deductionStockinfoId);
                            fundModelBtc.setStockinfoIdEx(relatedStockinfoId);
                            fundModelBtc.setAmount(btcEnable);
                            fundModelBtc.setAmountEx(btcEnable.multiply(settlementPrice));
                            fundModelBtc.setFee(BigDecimal.ZERO);
                            fundModelBtc.setAccountId(record.getBorrowerAccountId());
                            fundModelBtc.setCreateBy(record.getBorrowerAccountId());
                            if (btcEnable.compareTo(BigDecimal.ZERO) > 0)// 数字货币完全没有余额 不需要扣除和记录流水
                            {
                                fundService.fundTransaction(fundModelBtc);
                            }
                            else
                            {
                                logger.debug("没有足够的可抵货币");
                            }
                            // 成功的用户 借款减少（数字货币折合多少 减去多少）
                            BigDecimal subUsdx = btcEnable.multiply(settlementPrice);
                            record.setDebitAmt(record.getDebitAmt().subtract(subUsdx).subtract(usdxEnable));
                            record.setAccumulateInterest((record.getAccumulateInterest().subtract(subUsdx).subtract(usdxEnable)).compareTo(BigDecimal.ZERO) >= 0
                                    ? (record.getAccumulateInterest().subtract(subUsdx).subtract(usdxEnable))
                                    : BigDecimal.ZERO);
                            record.setTableName(getStockInfo(relatedStockinfoId).getTableDebitAsset());
                            accountDebitAssetService.updateByPrimaryKey(record);
                        }
                    }
                    else
                    {
                        // 不需要借款的资产处理
                        FundModel fundModel = new FundModel();
                        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT);
                        fundModel.setStockinfoId(stockinfoId);
                        fundModel.setStockinfoIdEx(relatedStockinfoId);
                        fundModel.setAmount(record.getDebitAmt());
                        fundModel.setFee(BigDecimal.ZERO);
                        fundModel.setAccountId(record.getBorrowerAccountId());
                        fundModel.setCreateBy(record.getBorrowerAccountId());
                        fundService.fundTransaction(fundModel);
                        // 成功的用户 借款清零
                        record.setDebitAmt(BigDecimal.ZERO);
                        record.setAccumulateInterest(BigDecimal.ZERO);
                        record.setTableName(getStockInfo(relatedStockinfoId).getTableDebitAsset());
                        accountDebitAssetService.updateByPrimaryKey(record);
                    }
                }
            }
            else if (StringUtils.equalsIgnoreCase(stockinfoType, FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH))
            { // 数字货币强制还款
                if (record.getDebitAmt().compareTo(BigDecimal.ZERO) > 0)
                {
                    // 查询可用
                    EnableModel enableModel = new EnableModel();
                    enableModel.setAccountId(record.getBorrowerAccountId());
                    enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT);
                    enableModel.setStockinfoId(stockinfoId);
                    enableModel.setRelatedStockinfoId(relatedStockinfoId);
                    enableModel = enableService.entrustTerminalEnable(enableModel);
                    BigDecimal btcEnable = enableModel.getEnableAmount();
                    logger.debug("借款ID：" + record.getId() + " 数字货币可用余额" + enableModel.getEnableAmount() + " 贷款金额=" + record.getDebitAmt());
                    if (btcEnable.compareTo(record.getDebitAmt()) < 0)
                    { // 需要借款的还款处理
                        StringBuilder sb = new StringBuilder("借款ID：" + record.getId() + "accountId=");
                        sb.append(record.getBorrowerAccountId());
                        sb.append("debitRecordId=");
                        sb.append(record.getId());
                        sb.append("数字货币余额进入抵扣还款");
                        logger.debug(sb.toString());
                        enableModel = new EnableModel();
                        enableModel.setAccountId(record.getBorrowerAccountId());
                        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT);
                        enableModel.setStockinfoId(deductionStockinfoId);
                        enableModel.setRelatedStockinfoId(relatedStockinfoId);
                        enableModel = enableService.entrustTerminalEnable(enableModel);
                        BigDecimal usdxEnable = enableModel.getEnableAmount();
                        BigDecimal needBtc = record.getDebitAmt().subtract(btcEnable);
                        logger.debug("借款ID：" + record.getId() + "还需数字货币数量：" + needBtc);
                        BigDecimal needUsdx = isVCoin ? needBtc.multiply(settlementPrice) : needBtc.divide(settlementPrice, 12, BigDecimal.ROUND_HALF_UP);
                        logger.debug("借款ID：" + record.getId() + " 结算价=" + settlementPrice);
                        logger.debug("借款ID：" + record.getId() + "还需数字货币折合法定货币=" + needUsdx);
                        logger.debug("借款ID：" + record.getId() + "法定货币账户可用余额：" + enableModel.getEnableAmount());
                        logger.debug("借款ID：" + record.getId() + "去处理两笔资产");
                        // USDX足够的时候全还
                        if (usdxEnable.compareTo(needUsdx) >= 0)
                        {
                            logger.debug("借款ID：" + record.getId() + "法定货币数量足够，可以还清借款");
                            // 资产处理--数字货币减少
                            FundModel fundModel = new FundModel();
                            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT);
                            fundModel.setStockinfoId(stockinfoId);
                            fundModel.setStockinfoIdEx(relatedStockinfoId);
                            fundModel.setAmount(btcEnable);
                            fundModel.setFee(BigDecimal.ZERO);
                            fundModel.setAccountId(record.getBorrowerAccountId());
                            fundModel.setCreateBy(record.getBorrowerAccountId());
                            if (btcEnable.compareTo(BigDecimal.ZERO) > 0)// 数字货币完全没有余额 不需要扣除和记录流水
                            {
                                fundService.fundTransaction(fundModel);
                            }
                            // 资产处理--法定货币减少
                            FundModel fundModelUsdx = new FundModel();
                            fundModelUsdx.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT);
                            fundModelUsdx.setStockinfoId(deductionStockinfoId);
                            fundModelUsdx.setStockinfoIdEx(relatedStockinfoId);
                            fundModelUsdx.setAmount(needUsdx);
                            fundModelUsdx.setAmountEx(needBtc);
                            fundModelUsdx.setFee(BigDecimal.ZERO);
                            fundModelUsdx.setAccountId(record.getBorrowerAccountId());
                            fundModelUsdx.setCreateBy(record.getBorrowerAccountId());
                            fundService.fundTransaction(fundModelUsdx);
                            // 成功的用户 借款清零
                            record.setDebitAmt(BigDecimal.ZERO);
                            record.setAccumulateInterest(BigDecimal.ZERO);
                            record.setTableName(getStockInfo(relatedStockinfoId).getTableDebitAsset());
                            accountDebitAssetService.updateByPrimaryKey(record);
                        }
                        else
                        {
                            logger.debug("借款ID：" + record.getId() + " 法定货币数量不足，部分还款：");
                            // 资产处理--数字货币减少
                            FundModel fundModel = new FundModel();
                            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT);
                            fundModel.setStockinfoId(stockinfoId);
                            fundModel.setStockinfoIdEx(relatedStockinfoId);
                            fundModel.setAmount(btcEnable);
                            fundModel.setFee(BigDecimal.ZERO);
                            fundModel.setAccountId(record.getBorrowerAccountId());
                            fundModel.setCreateBy(record.getBorrowerAccountId());
                            if (btcEnable.compareTo(BigDecimal.ZERO) > 0)// 数字货币完全没有余额 不需要扣除和记录流水
                            {
                                fundService.fundTransaction(fundModel);
                            }
                            else
                            {
                                logger.debug("没有足够的货币");
                            }
                            // 成功的用户 借款减少（法定货币折合多少 减去多少）
                            BigDecimal subBtc = usdxEnable.divide(settlementPrice, 12, BigDecimal.ROUND_HALF_UP);
                            // 资产处理--法定货币减少
                            FundModel fundModelUsdx = new FundModel();
                            fundModelUsdx.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT);
                            fundModelUsdx.setStockinfoId(deductionStockinfoId);
                            fundModelUsdx.setStockinfoIdEx(relatedStockinfoId);
                            fundModelUsdx.setAmount(usdxEnable);
                            fundModelUsdx.setAmountEx(subBtc);
                            fundModelUsdx.setFee(BigDecimal.ZERO);
                            fundModelUsdx.setAccountId(record.getBorrowerAccountId());
                            fundModelUsdx.setCreateBy(record.getBorrowerAccountId());
                            if (usdxEnable.compareTo(BigDecimal.ZERO) > 0)// 有可用余额时处理
                            {
                                fundService.fundTransaction(fundModelUsdx);
                            }
                            else
                            {
                                logger.debug("没有足够的可抵用货币");
                            }
                            record.setDebitAmt(record.getDebitAmt().subtract(subBtc).subtract(btcEnable));
                            record.setAccumulateInterest((record.getAccumulateInterest().subtract(subBtc).subtract(btcEnable)).compareTo(BigDecimal.ZERO) >= 0
                                    ? (record.getAccumulateInterest().subtract(subBtc).subtract(btcEnable))
                                    : BigDecimal.ZERO);
                            record.setTableName(getStockInfo(relatedStockinfoId).getTableDebitAsset());
                            accountDebitAssetService.updateByPrimaryKey(record);
                        }
                    }
                    else
                    {
                        // 不需要借款的还款资产处理
                        FundModel fundModel = new FundModel();
                        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT);
                        fundModel.setStockinfoId(stockinfoId);
                        fundModel.setStockinfoIdEx(relatedStockinfoId);
                        fundModel.setAmount(record.getDebitAmt());
                        fundModel.setFee(BigDecimal.ZERO);
                        fundModel.setAccountId(record.getBorrowerAccountId());
                        fundModel.setCreateBy(record.getBorrowerAccountId());
                        fundService.fundTransaction(fundModel);
                        // 成功的用户 借款清零
                        record.setDebitAmt(BigDecimal.ZERO);
                        record.setAccumulateInterest(BigDecimal.ZERO);
                        record.setTableName(getStockInfo(relatedStockinfoId).getTableDebitAsset());
                        accountDebitAssetService.updateByPrimaryKey(record);
                    }
                }
            }
            else
            {
                logger.debug("不支持强制货币类型：" + stockinfoType);
            }
            // 清除用户缓存
            clearAccountAssetCache(record.getBorrowerAccountId(), relatedStockinfoId);
        }
    }
    
    /**
     * 查询未还借款信息
     * @return
     */
    @Override
    public List<AccountDebitAsset> findListForDebit(AccountDebitAsset accountDebitAsset)
    {
        return accountDebitAssetMapper.findListForDebit(accountDebitAsset);
    }
    
    /**
     * 查询
     * @param  ids 逗号分割
     * @return
     */
    @Override
    public List<AccountDebitAsset> findListByIds(String ids)
    {
        String idss[] = ids.split(",");
        Long[] id = new Long[idss.length];
        for (int i = 0; i < idss.length; i++)
        {
            id[i] = Long.parseLong(idss[i]);
        }
        return accountDebitAssetMapper.findListByIds(id);
    }
    
    /**
     * 借款转移给超级用户
     * @param record
     * @param superAccountId
     * @throws BusinessException
     */
    @Override
    public void doDebtMoveToPlat(AccountDebitAsset record, Long superAccountId, String businessFlag) throws BusinessException
    {
        Long relatedStockinfoId = record.getRelatedStockinfoId();
        logger.debug(record.toString());
        // 查询超级账户已贷款的记录
        AccountDebitAsset recordSuper = new AccountDebitAsset();
        recordSuper.setStockinfoId(record.getStockinfoId());
        recordSuper.setRelatedStockinfoId(getStockInfo(record.getRelatedStockinfoId()).getCapitalStockinfoId());
        recordSuper.setBorrowerAccountId(superAccountId);
        recordSuper.setTableName(getStockInfo(record.getRelatedStockinfoId()).getTableDebitAsset());
        List<AccountDebitAsset> list2 = accountDebitAssetService.findList(recordSuper);
        if (list2.size() > 0)
        {
            logger.debug("超级用户借款记录存在");
            recordSuper = list2.get(0);
            recordSuper.setLenderAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
            recordSuper.setDebitAmt(record.getDebitAmt().add(recordSuper.getDebitAmt()));
            recordSuper.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            recordSuper.setTableName(getStockInfo(record.getRelatedStockinfoId()).getTableDebitAsset());
            accountDebitAssetService.updateByPrimaryKey(recordSuper); // 调整超级用户借款
            logger.debug("已修改超级用户借款 借款金额新增：" + record.getDebitAmt());
            // 调用资产处理
            logger.debug("借款转移插入资金流水开始");
            fundService.fundDebitMoveToPlatAsset(record, superAccountId, businessFlag);
            logger.debug("借款转移插入资金流水结束");
            record.setDebitAmt(BigDecimal.ZERO);
            record.setAccumulateInterest(BigDecimal.ZERO);
            record.setRelatedStockinfoId(getStockInfo(record.getRelatedStockinfoId()).getCapitalStockinfoId());
            record.setTableName(getStockInfo(record.getRelatedStockinfoId()).getTableDebitAsset());
            accountDebitAssetService.updateByPrimaryKey(record);// 当前用户借款清零
            logger.debug("当前用户借款清零");
        }
        else
        {
            logger.debug("超级用户借款记录不存在");
            recordSuper = new AccountDebitAsset();
            recordSuper.setDebitAmt(record.getDebitAmt());
            recordSuper.setStockinfoId(record.getStockinfoId());
            recordSuper.setRelatedStockinfoId(getStockInfo(record.getRelatedStockinfoId()).getCapitalStockinfoId());
            recordSuper.setDebitAmt(record.getDebitAmt());
            recordSuper.setLenderAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
            recordSuper.setBorrowerAccountId(superAccountId);
            recordSuper.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            recordSuper.setLastPrice(BigDecimal.ZERO);
            recordSuper.setAccumulateInterest(BigDecimal.ZERO);
            recordSuper.setTableName(getStockInfo(record.getRelatedStockinfoId()).getTableDebitAsset());
            recordSuper.setLastInterestDay(0L);
            logger.debug("准备插入借款表的数据：" + recordSuper.toString());
            accountDebitAssetService.insert(recordSuper); // 调整超级用户借款
            logger.debug("已新增超级用户借款 借款金额新增：" + record.getDebitAmt());
            // 调用资产处理
            logger.debug("借款转移插入资金流水开始");
            fundService.fundDebitMoveToPlatAsset(record, superAccountId, businessFlag);
            logger.debug("借款转移插入资金流水结束");
            record.setDebitAmt(BigDecimal.ZERO);
            record.setAccumulateInterest(BigDecimal.ZERO);
            record.setRelatedStockinfoId(getStockInfo(record.getRelatedStockinfoId()).getCapitalStockinfoId());
            record.setTableName(getStockInfo(record.getRelatedStockinfoId()).getTableDebitAsset());
            accountDebitAssetService.updateByPrimaryKey(record);// 当前用户借款清零
            logger.debug("当前用户借款清零");
        }
        // 清除用户缓存
        clearAccountAssetCache(record.getBorrowerAccountId(), relatedStockinfoId);
    }
    
    @Override
    public void settlementDebitMoveToPlatAfterPowerRepay(Long relatedStockinfoId, Long stockinfoId, Long superAccountId)
    {
        AccountDebitAsset accountDebitAsset = new AccountDebitAsset();
        accountDebitAsset.setRelatedStockinfoId(relatedStockinfoId);
        accountDebitAsset.setTableName(getStockInfo(relatedStockinfoId).getTableDebitAsset());
        List<AccountDebitAsset> list = accountDebitAssetService.findListForDebit(accountDebitAsset);
        for (AccountDebitAsset record : list)
        {
            logger.debug("借款ID:" + record.getId() + ".....start..................");
            logger.debug(
                    "借款ID:" + record.getId() + " 借款用户：" + record.getBorrowerAccountId() + " 借款证券ID：" + record.getStockinfoId() + " 账户：" + record.getBorrowerAccountName());
            if (record.getBorrowerAccountId().longValue() != FundConsts.SYSTEM_ACCOUNT_ID)// 其它用户都转移给超级用户
            {
                // 调用债务转移资金流水处理
                settlementDebtMoveToPlat(record, stockinfoId, FundConsts.SYSTEM_ACCOUNT_ID);
                // 清除用户缓存
                clearAccountAssetCache(record.getBorrowerAccountId(), relatedStockinfoId);
            }
            else
            {
                logger.debug("超级用户：" + superAccountId + " 债务不需要转移");
            }
            logger.debug("借款ID:" + record.getId() + ".....end..................");
        }
    }
    
    /**
     * 查询保证金情况
     * @param pagin
     * @param accountDebitAsset
     * @return
     */
    @Override
    public PaginateResult<AccountDebitAsset> findMarginList(Pagination pagin, AccountDebitAsset accountDebitAsset)
    {
        accountDebitAsset.setPagin(pagin);
        accountDebitAsset.setTableName(getStockInfo(accountDebitAsset.getRelatedStockinfoId()).getTableDebitAsset());
        List<AccountDebitAsset> fundCurrentList = accountDebitAssetMapper.findMarginList(accountDebitAsset);
        return new PaginateResult<>(pagin, fundCurrentList);
    }
    
    /**
     * 交割过程中未还清的借款转移给超级用户
     * @param record
     * @param superAccountId
     * @throws BusinessException
     */
    public void settlementDebtMoveToPlat(AccountDebitAsset record, Long stockinfoId, Long superAccountId) throws BusinessException
    {
        record = accountDebitAssetService.selectByPrimaryKeyForUpdate(getStockInfo(record.getRelatedStockinfoId()).getTableDebitAsset(), record.getId());
        logger.debug(record.toString());
        // 查询超级账户已贷款的记录
        AccountDebitAsset recordSuper = new AccountDebitAsset();
        recordSuper.setStockinfoId(record.getStockinfoId());
        recordSuper.setRelatedStockinfoId(record.getRelatedStockinfoId());
        recordSuper.setBorrowerAccountId(superAccountId);
        recordSuper.setTableName(getStockInfo(record.getRelatedStockinfoId()).getTableDebitAsset());
        List<AccountDebitAsset> list2 = accountDebitAssetService.findList(recordSuper);
        if (list2.size() > 0)
        {
            recordSuper = list2.get(0);
            logger.debug("超级用户借款变动前：" + recordSuper.toString());
            recordSuper.setLenderAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
            recordSuper.setDebitAmt(record.getDebitAmt().add(recordSuper.getDebitAmt()));
            recordSuper.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            recordSuper.setTableName(getStockInfo(record.getRelatedStockinfoId()).getTableDebitAsset());
            accountDebitAssetService.updateByPrimaryKey(recordSuper); // 调整超级用户借款
            logger.debug("超级用户借款变动后：" + recordSuper.toString());
            // 调用资产处理
            String businessFlag = "";
            if (record.getStockinfoId().longValue() == stockinfoId)
            {
                businessFlag = FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_VCOIN_TRANSFER;
            }
            else if (record.getStockinfoId().longValue() == record.getRelatedStockinfoId().longValue())
            {
                businessFlag = FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_MONEY_TRANSFER;
            }
            else
            {
                logger.debug("错误的证券ID");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            logger.debug("借款转移插入资金流水开始" + businessFlag);
            fundService.fundDebitMoveToPlatAsset(record, superAccountId, businessFlag);
            logger.debug("借款转移插入资金流水结束" + businessFlag);
            logger.debug("普通用户借款变动前：" + record.toString());
            record.setDebitAmt(BigDecimal.ZERO);
            record.setAccumulateInterest(BigDecimal.ZERO);
            record.setTableName(getStockInfo(record.getRelatedStockinfoId()).getTableDebitAsset());
            accountDebitAssetService.updateByPrimaryKey(record);// 当前用户借款清零
            logger.debug("普通用户借款变动后：" + record.toString());
        }
        else
        {
            logger.debug("超级用户借款记录不存在");
            recordSuper = new AccountDebitAsset();
            logger.debug("超级用户借款变动前：" + recordSuper.toString());
            recordSuper.setDebitAmt(record.getDebitAmt());
            recordSuper.setStockinfoId(record.getStockinfoId());
            recordSuper.setRelatedStockinfoId(record.getRelatedStockinfoId());
            recordSuper.setDebitAmt(record.getDebitAmt());
            recordSuper.setLenderAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
            recordSuper.setBorrowerAccountId(superAccountId);
            recordSuper.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            recordSuper.setLastPrice(BigDecimal.ZERO);
            recordSuper.setAccumulateInterest(BigDecimal.ZERO);
            recordSuper.setTableName(getStockInfo(record.getRelatedStockinfoId()).getTableDebitAsset());
            recordSuper.setLastInterestDay(0L);
            logger.debug("超级用户借款变动准备数据：" + recordSuper.toString());
            accountDebitAssetService.insert(recordSuper); // 调整超级用户借款
            logger.debug("超级用户借款变动后：" + recordSuper.toString());
            // 调用资产处理
            String businessFlag = "";
            if (record.getStockinfoId().longValue() == stockinfoId)
            {
                businessFlag = FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_VCOIN_TRANSFER;
            }
            else if (record.getStockinfoId().longValue() == record.getRelatedStockinfoId().longValue())
            {
                businessFlag = FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_MONEY_TRANSFER;
            }
            else
            {
                logger.debug("错误的证券ID");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            logger.debug("借款转移插入资金流水开始" + businessFlag);
            fundService.fundDebitMoveToPlatAsset(record, superAccountId, businessFlag);
            logger.debug("借款转移插入资金流水结束" + businessFlag);
            logger.debug("普通用户借款变动前：" + record.toString());
            record.setDebitAmt(BigDecimal.ZERO);
            record.setAccumulateInterest(BigDecimal.ZERO);
            record.setTableName(getStockInfo(record.getRelatedStockinfoId()).getTableDebitAsset());
            accountDebitAssetService.updateByPrimaryKey(record);// 当前用户借款清零
            logger.debug("普通用户借款变动后：" + record.toString());
        }
    }
    
    private boolean checkSwitch()
    {
        SysParameter params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(ParamConsts.MATCHTRADE_SWITCH);
        params = sysParameterService.getSysParameterByEntity(params);
        if (params == null)
        {
            logger.debug("===========开关值空==========");
            return false;
        }
        if (!com.blocain.bitms.tools.utils.StringUtils.isBlank(params.getValue()))
        {
            if (!params.getValue().equals("yes"))
            {
                logger.debug("===========开关已关闭==========");
                return false;
            }
            else
            {
                logger.debug("===========开关已打开==========");
                return true;
            }
        }
        else
        {
            logger.debug("===========开关值不存在==========");
            return false;
        }
    }
    
    /**
     * 公共方法 获取结算价
     * @param stockInfoId
     * @return
     * @throws BusinessException
     */
    public BigDecimal getSettlementPrice(Long stockInfoId) throws BusinessException
    {
        BigDecimal ret = BigDecimal.ZERO;
        List<StockInfo> list = stockInfoService.findListByIds(stockInfoId.toString());
        if (list.size() > 0)
        {
            StockInfo info = list.get(0);
            if (info.getSettlementPrice() == null)
            {
                logger.debug("结算价不存在");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            ret = info.getSettlementPrice();
        }
        else
        {
            ret = BigDecimal.ZERO;
            logger.debug("结算价不存在");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        return ret;
    }
    
    /**
     * 按条件查找总负债
     * @param accountDebitAsset
     * @return
     */
    @Override
    public BigDecimal findSumDebitAmt(AccountDebitAsset accountDebitAsset)
    {
        return accountDebitAssetMapper.findSumDebitAmt(accountDebitAsset);
    }
    
    @Override
    public PaginateResult<AccountDebitAsset> debitSumData(Pagination pagination, AccountDebitAsset accountDebitAsset)
    {
        accountDebitAsset.setPagin(pagination);
        List<AccountDebitAsset> fundCurrentList = accountDebitAssetMapper.debitSumData(accountDebitAsset);
        return new PaginateResult<>(pagination, fundCurrentList);
    }
    
    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.settlement.service;

import com.blocain.bitms.monitor.consts.MonitorConst;
import com.blocain.bitms.monitor.entity.MonitorBalance;
import com.blocain.bitms.monitor.service.MonitorBalanceService;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountContractAsset;
import com.blocain.bitms.trade.fund.entity.AccountDebitAsset;
import com.blocain.bitms.trade.fund.model.ContractAssetModel;
import com.blocain.bitms.trade.fund.model.FundModel;
import com.blocain.bitms.trade.fund.service.AccountContractAssetService;
import com.blocain.bitms.trade.fund.service.AccountDebitAssetService;
import com.blocain.bitms.trade.fund.service.FundService;
import com.blocain.bitms.trade.risk.model.EnableModel;
import com.blocain.bitms.trade.risk.service.EnableService;
import com.blocain.bitms.trade.settlement.entity.SettlementAccountAsset;
import com.blocain.bitms.trade.settlement.entity.SettlementProcessLog;
import com.blocain.bitms.trade.settlement.entity.SettlementRecord;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.trade.entity.EntrustVCoinMoney;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import com.blocain.bitms.trade.trade.model.EntrustModel;
import com.blocain.bitms.trade.trade.service.EntrustVCoinMoneyService;
import com.blocain.bitms.trade.trade.service.TradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 交割核心处理
 * <p>File：SettlementServiceImpl.java</p>
 * <p>Title: SettlementServiceImpl</p>
 * <p>Description:SettlementServiceImpl</p>
 * <p>Copyright: Copyright (c) 2017年8月17日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Service
public class SettlementServiceImpl implements SettlementService
{
    public static final Logger                  logger = LoggerFactory.getLogger(SettlementServiceImpl.class);
    
    @Autowired(required = false)
    private EntrustVCoinMoneyService            entrustVCoinMoneyService;
    
    @Autowired(required = false)
    private EnableService                       enableService;
    
    @Autowired(required = false)
    private SettlementAccountAssetService       settlementAccountAssetService;
    
    @Autowired(required = false)
    private AccountContractAssetService         accountContractAssetService;
    
    @Autowired(required = false)
    private FundService                         fundService;
    
    @Autowired(required = false)
    private SettlementRecordService             settlementRecordService;
    
    @Autowired(required = false)
    private TradeService                        tradeService;
    
    @Autowired
    private MonitorBalanceService               monitorBalanceService;
    
    @Autowired
    private StockInfoService                    stockInfoService;
    
    @Autowired(required = false)
    private AccountDebitAssetService accountDebitAssetService;
    
    @Autowired(required = false)
    private SettlementProcessLogService         settlementProcessLogService;
    
    @Autowired(required = false)
    private SettlementService                   settlementService;
    
    @Override
    /**
     * 平台交割委托撤单
     */
    public void withdrawByPlatSettlement(Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        List<EntrustVCoinMoney> entrustVCoinMoneyList = entrustVCoinMoneyService.findAllInEntrust(getStockInfo(exchangePairMoney).getTableEntrust());
        for (EntrustVCoinMoney entrustVCoinMoney : entrustVCoinMoneyList)
        {
            logger.debug("未撤销的委托：" + entrustVCoinMoney.getId() + " " + entrustVCoinMoney.toString());
            EntrustModel entrustModel = new EntrustModel();
            entrustModel.setEntrustId(entrustVCoinMoney.getId());
            entrustModel.setAccountId(entrustVCoinMoney.getAccountId());
            entrustModel.setTableName(getStockInfo(exchangePairMoney).getTableEntrust());
            entrustModel.setStockinfoIdEx(exchangePairMoney);
            entrustModel.setStockinfoId(exchangePairVCoin);
            tradeService.entrustWithdrawX(entrustModel);
            logger.debug("已撤销的委托：" + entrustVCoinMoney.getId());
        }
    }
    
    @Override
    /**
     * 交割分摊操作
     */
    public void doFenTanByPlatSettlement(Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        BigDecimal superUsdxDebit = BigDecimal.ZERO;
        AccountDebitAsset accountDebitAsset = new AccountDebitAsset();
        accountDebitAsset.setBorrowerAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
        accountDebitAsset.setStockinfoId(exchangePairMoney);
        accountDebitAsset.setRelatedStockinfoId(exchangePairMoney);
        accountDebitAsset.setTableName(getStockInfo(exchangePairMoney).getTableDebitAsset());
        List<AccountDebitAsset> debitList = accountDebitAssetService.findList(accountDebitAsset);
        if (debitList.size() > 0)
        {
            accountDebitAsset = debitList.get(0);
            superUsdxDebit = accountDebitAsset.getDebitAmt();
        }
        BigDecimal superBtcDebit = BigDecimal.ZERO;
        accountDebitAsset = new AccountDebitAsset();
        accountDebitAsset.setBorrowerAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
        accountDebitAsset.setStockinfoId(exchangePairVCoin);
        accountDebitAsset.setRelatedStockinfoId(exchangePairMoney);
        accountDebitAsset.setTableName(getStockInfo(exchangePairMoney).getTableDebitAsset());
        debitList = accountDebitAssetService.findList(accountDebitAsset);
        if (debitList.size() > 0)
        {
            accountDebitAsset = debitList.get(0);
            superBtcDebit = accountDebitAsset.getDebitAmt();
        }
        BigDecimal settlementPrice = getSettlementPrice(exchangePairMoney);
        BigDecimal superBtcAsset = BigDecimal.ZERO;
        AccountContractAsset accountContractAsset = new AccountContractAsset();
        accountContractAsset.setAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
        accountContractAsset.setStockinfoId(exchangePairVCoin);
        accountContractAsset.setRelatedStockinfoId(exchangePairMoney);
        accountContractAsset.setTableName(getStockInfo(exchangePairMoney).getTableAsset());
        List<AccountContractAsset> list = accountContractAssetService.findList(accountContractAsset);
        if (list.size() > 0)
        {
            superBtcAsset = list.get(0).getAmount();
        }
        BigDecimal superUsdxAsset = BigDecimal.ZERO;
        accountContractAsset = new AccountContractAsset();
        accountContractAsset.setAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
        accountContractAsset.setStockinfoId(exchangePairMoney);
        accountContractAsset.setRelatedStockinfoId(exchangePairMoney);
        accountContractAsset.setTableName(getStockInfo(exchangePairMoney).getTableAsset());
        list = accountContractAssetService.findList(accountContractAsset);
        if (list.size() > 0)
        {
            superUsdxAsset = list.get(0).getAmount();
        }
        SettlementRecord settlementRecord = new SettlementRecord();
        BigDecimal superYingLi = BigDecimal.ZERO;
        ContractAssetModel modelPersonAll = accountContractAssetService.findAccountSumContractAsset(exchangePairVCoin, exchangePairMoney, null);
        superYingLi = modelPersonAll.getSumProfit();
        // 扣除准备金
        BigDecimal reserveFund = BigDecimal.ZERO;
        accountContractAsset = new AccountContractAsset();
        accountContractAsset.setAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_RESERVE_ALLOCATION_ID);
        accountContractAsset.setRelatedStockinfoId(exchangePairMoney);
        accountContractAsset.setStockinfoId(exchangePairVCoin);
        accountContractAsset.setTableName(getStockInfo(exchangePairMoney).getTableAsset());
        List<AccountContractAsset> asstList = accountContractAssetService.findList(accountContractAsset);
        logger.debug("查询全市场- 数字货币盈利:" + superYingLi);
        BigDecimal needBtc = superBtcAsset.subtract(superBtcDebit).add((superUsdxAsset.subtract(superUsdxDebit)).divide(settlementPrice, 12, BigDecimal.ROUND_HALF_UP));
        logger.debug("盈亏情况：" + needBtc);
        boolean result = needBtc.compareTo(BigDecimal.ZERO) >= 0;
        logger.debug("比较的结果：" + result);
        if (result)
        {
            needBtc = BigDecimal.ZERO;
            logger.debug("需分摊数字货币:" + needBtc);
            logger.debug("交割记录开始。。。。。。。。。。。。。。。。。。。。");
            if (asstList.size() > 0)
            {
                AccountContractAsset asset = asstList.get(0);
                reserveFund = asset.getAmount();
                logger.debug("存在准备金：" + reserveFund);
            }
            if (superYingLi.compareTo(BigDecimal.ZERO) == 0)
            {
                settlementRecord.setAssessmentRate(BigDecimal.ZERO);
            }
            else
            {
                settlementRecord.setAssessmentRate(needBtc.divide(superYingLi, 12, BigDecimal.ROUND_HALF_UP));
            }
            settlementRecord.setRelatedStockinfoId(exchangePairMoney);
            settlementRecord.setSettlementPrice(settlementPrice);
            settlementRecord.setSettlementTime(new Timestamp(System.currentTimeMillis()));
            settlementRecord.setSettlementType(1);
            settlementRecord.setStockinfoId(exchangePairVCoin);
            settlementRecord.setWearingSharingLossesAmt(needBtc);
            settlementRecord.setRemark("没有亏损 不存在分摊");
            settlementRecord.setReserveOrgAmt(reserveFund);
            settlementRecord.setReserveAllocatAmt(BigDecimal.ZERO);// 使用准备金
            settlementRecord.setReserveLastAmt(reserveFund);// 剩余准备金
            settlementRecordService.insert(settlementRecord);
            // 批量插入用户交割日志表
            SettlementAccountAsset settlementAccountAsset = new SettlementAccountAsset();
            settlementAccountAsset.setStockinfoId(exchangePairVCoin);
            settlementAccountAsset.setRelatedStockinfoId(exchangePairMoney);
            settlementAccountAsset.setSettlementType(1);
            settlementAccountAsset.setSettlementTime(settlementRecord.getSettlementTime());
            settlementAccountAsset.setSettlementPrice(settlementRecord.getSettlementPrice());
            settlementAccountAsset.setPeriodAssessmentAmt(BigDecimal.ZERO);
            settlementAccountAsset.setPeriodAssessmentRate(BigDecimal.ZERO);
            settlementAccountAsset.setRemark(settlementRecord.getRemark());
            settlementAccountAsset.setTableAsset(getStockInfo(exchangePairMoney).getTableAsset());
            settlementAccountAssetService.insertFromAsset(settlementAccountAsset);
            logger.debug("交割记录结束。。。。。。。。。。。。。。。。。。。。");
        }
        else
        {
            // 比较结果 如果是false 则亏损
            needBtc = needBtc.abs();
            if (asstList.size() > 0)
            {
                AccountContractAsset asset = asstList.get(0);
                reserveFund = asset.getAmount();
                logger.debug("存在准备金：" + reserveFund);
            }
            else
            {
                logger.debug("不存在准备金：" + reserveFund);
            }
            settlementRecord.setReserveOrgAmt(reserveFund);// 原始准备金
            if (reserveFund.compareTo(BigDecimal.ZERO) > 0)
            {
                logger.debug("准备金有余额：" + reserveFund);
                if (reserveFund.compareTo(needBtc) >= 0)
                {// 准备金足够分摊
                    settlementRecord.setReserveAllocatAmt(needBtc);// 使用准备金
                    settlementRecord.setReserveLastAmt(reserveFund.subtract(needBtc));// 剩余准备金
                    logger.debug("准备金足够分摊");
                    FundModel fundModel = new FundModel();
                    fundModel.setStockinfoId(exchangePairVCoin);
                    fundModel.setStockinfoIdEx(exchangePairMoney);
                    fundModel.setAmount(needBtc);
                    fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_VCOIN_CONTRIBUTION_QUOTA_DEDUCTION_DECREASE);
                    fundModel.setAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_RESERVE_ALLOCATION_ID);
                    fundModel.setOriginalBusinessId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_RESERVE_ALLOCATION_ID);
                    fundModel.setCreateBy(FundConsts.SYSTEM_ACCOUNT_ID);// 转移给超级用户
                    fundService.fundTransaction(fundModel);
                    needBtc = BigDecimal.ZERO;
                }
                else
                {
                    logger.debug("准备金部分分摊");
                    settlementRecord.setReserveAllocatAmt(reserveFund);// 使用准备金
                    settlementRecord.setReserveLastAmt(BigDecimal.ZERO);// 剩余准备金
                    FundModel fundModel = new FundModel();
                    fundModel.setStockinfoId(exchangePairVCoin);
                    fundModel.setStockinfoIdEx(exchangePairMoney);
                    fundModel.setAmount(reserveFund);
                    fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_VCOIN_CONTRIBUTION_QUOTA_DEDUCTION_DECREASE);
                    fundModel.setAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_RESERVE_ALLOCATION_ID);
                    fundModel.setOriginalBusinessId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_RESERVE_ALLOCATION_ID);
                    fundModel.setCreateBy(FundConsts.SYSTEM_ACCOUNT_ID);// 转移给超级用户
                    fundService.fundTransaction(fundModel);
                    needBtc = needBtc.subtract(reserveFund);
                }
            }
            else
            {
                logger.debug("准备金无余额：" + reserveFund);
                settlementRecord.setReserveOrgAmt(BigDecimal.ZERO);// 原始准备金
                settlementRecord.setReserveAllocatAmt(BigDecimal.ZERO);// 使用准备金
                settlementRecord.setReserveLastAmt(BigDecimal.ZERO);// 剩余准备金
            }
            // 分摊处理
            if (needBtc.compareTo(BigDecimal.ZERO) > 0)
            {
                logger.debug("存在分摊。。。。");
                logger.debug("查询全市场- BTC盈利:" + superYingLi);
                logger.debug("交割记录开始。。。。。。。。。。。。。。。。。。。。");
                if (superYingLi.compareTo(BigDecimal.ZERO) == 0)
                {
                    settlementRecord.setAssessmentRate(BigDecimal.ZERO);
                }
                else
                {
                    settlementRecord.setAssessmentRate(needBtc.divide(superYingLi, 12, BigDecimal.ROUND_HALF_UP));
                }
                settlementRecord.setRelatedStockinfoId(exchangePairMoney);
                settlementRecord.setSettlementPrice(settlementPrice);
                settlementRecord.setSettlementTime(new Timestamp(System.currentTimeMillis()));
                settlementRecord.setSettlementType(1);
                settlementRecord.setStockinfoId(exchangePairVCoin);
                settlementRecord.setWearingSharingLossesAmt(needBtc);
                settlementRecord.setRemark("准备金不足用户分摊");
                // 这里插入交割记录
                settlementRecordService.insert(settlementRecord);
                SettlementAccountAsset settlementAccountAsset = new SettlementAccountAsset();
                settlementAccountAsset.setStockinfoId(exchangePairVCoin);
                settlementAccountAsset.setRelatedStockinfoId(exchangePairMoney);
                settlementAccountAsset.setSettlementType(1);
                settlementAccountAsset.setSettlementTime(settlementRecord.getSettlementTime());
                settlementAccountAsset.setSettlementPrice(settlementRecord.getSettlementPrice());
                settlementAccountAsset.setPeriodAssessmentRate(settlementRecord.getAssessmentRate());
                settlementAccountAsset.setRemark(settlementRecord.getRemark());
                settlementAccountAsset.setTableAsset(getStockInfo(exchangePairMoney).getTableAsset());
                settlementAccountAssetService.insertFromAsset(settlementAccountAsset);
                List<ContractAssetModel> ContractAssetModelList = accountContractAssetService.findAccountContractAssetGtZreo(exchangePairVCoin, exchangePairMoney);
                for (ContractAssetModel modelPerson : ContractAssetModelList)
                {
                    if (modelPerson.getAccountId().longValue() != FundConsts.SYSTEM_ACCOUNT_ID
                            && modelPerson.getAccountId().longValue() != FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_FEE_ID
                            && modelPerson.getAccountId().longValue() != FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_RESERVE_ALLOCATION_ID
                            && modelPerson.getAccountId().longValue() != FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID
                            && modelPerson.getAccountId().longValue() != FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID)
                    {
                        // 当前账户-当前数字货币持仓数量 tableName已在服务层处理
                        // 交割过程到这一步 法定货币借款等于0 数字货币借款等于0 法定货币资产和冻结等于0 数字货币冻结等于0 数字货币净值=数字货币账户资产
                        BigDecimal allBtcOfPersion = modelPerson.getSumAmount();
                        logger.debug("#222222#" + allBtcOfPersion + " accountId=" + modelPerson.getAccountId());
                        BigDecimal allBtcSumInitialOfPersion = modelPerson.getSumInitialAmt();
                        BigDecimal allInOfPersion = modelPerson.getSumFlowInAmt();
                        logger.debug("当前账户" + modelPerson.getAccountId() + "--总流入数字货币数量:" + allInOfPersion);
                        BigDecimal allOutOfPersion = modelPerson.getSumFlowOutAmt();
                        logger.debug("当前用户" + modelPerson.getAccountId() + "-总流出数字货币数量:" + allOutOfPersion);
                        BigDecimal persionYingLi = allBtcOfPersion.subtract(allBtcSumInitialOfPersion).subtract(allInOfPersion).add(allOutOfPersion);
                        logger.debug("##2##账户：" + modelPerson.getAccountId() + " 盈利情况：" + persionYingLi + " asset：" + allBtcOfPersion + " assetinit："
                                + allBtcSumInitialOfPersion + " in：" + allInOfPersion + " out：" + allOutOfPersion);
                        logger.debug("当前用户" + modelPerson.getAccountId() + "- 数字货币盈利比例:" + persionYingLi.divide(superYingLi, 12, BigDecimal.ROUND_HALF_UP));
                        BigDecimal fenTanBtc = persionYingLi.divide(superYingLi, 12, BigDecimal.ROUND_HALF_UP).multiply(needBtc);
                        logger.debug("当前用户" + modelPerson.getAccountId() + "- 需分摊数字货币:" + fenTanBtc);
                        if (persionYingLi.compareTo(BigDecimal.ZERO) > 0)
                        {
                            logger.debug("=======参与分摊用户：" + modelPerson.getAccountId() + ":" + persionYingLi);
                            logger.debug("=======参与分摊用户：" + modelPerson.getAccountId() + " 盈利情况：" + persionYingLi + " asset：" + allBtcOfPersion + " assetinit："
                                    + allBtcSumInitialOfPersion + " in：" + allInOfPersion + " out：" + allOutOfPersion);
                            if (!result && needBtc.compareTo(BigDecimal.ZERO) > 0 && fenTanBtc.compareTo(BigDecimal.ZERO) > 0)// 存在亏损 需要分摊
                            {
                                EnableModel enableModel = new EnableModel();
                                enableModel.setAccountId(modelPerson.getAccountId());
                                enableModel.setStockinfoId(exchangePairVCoin);
                                enableModel.setRelatedStockinfoId(exchangePairMoney);
                                enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_PLAT_SHARE_OF_LOSSES_VCOIN_MONEY);
                                enableModel = enableService.entrustTerminalEnable(enableModel);
                                if (enableModel.getEnableAmount().compareTo(fenTanBtc) >= 0)
                                {
                                    logger.debug("余额充足，账户id=" + modelPerson.getAccountId());
                                    // 交割BTC转移
                                    if (fenTanBtc.compareTo(BigDecimal.ZERO) > 0)
                                    {
                                        FundModel fundModel = new FundModel();
                                        fundModel.setAmount(fenTanBtc);
                                        fundModel.setAccountId(modelPerson.getAccountId());
                                        fundModel.setStockinfoId(exchangePairVCoin);
                                        fundModel.setStockinfoIdEx(exchangePairMoney);
                                        fundService.doSettlementVCoinMove(fundModel);
                                    }
                                    // 强制超级用户还款
                                    accountDebitAssetService.doDebitRepaymentPowerByPlat(FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH, exchangePairVCoin,
                                            exchangePairMoney, exchangePairMoney);
                                }
                                else
                                {
                                    logger.debug("余额不足，不能转移数字货币 ，账户id=" + modelPerson.getAccountId());
                                    throw new BusinessException("余额不足，不能转移数字货币 ，账户id=" + modelPerson.getAccountId());
                                }
                            }
                        }
                    }
                }
                logger.debug("交割记录结束。。。。。。。。。。。。。。。。。。。。");
            }
            else
            {
                logger.debug(" 不存在分摊金额 ");
                logger.debug("交割记录开始。。。。。。。。。。。。。。。。。。。。");
                if (reserveFund.compareTo(BigDecimal.ZERO) > 0)// 分摊金充足
                {
                    settlementRecord.setAssessmentRate(BigDecimal.ZERO);
                    settlementRecord.setRelatedStockinfoId(exchangePairMoney);
                    settlementRecord.setSettlementPrice(settlementPrice);
                    settlementRecord.setSettlementTime(new Timestamp(System.currentTimeMillis()));
                    settlementRecord.setSettlementType(1);
                    settlementRecord.setStockinfoId(exchangePairVCoin);
                    settlementRecord.setWearingSharingLossesAmt(needBtc);
                    settlementRecord.setRemark("准备金充足用不不需要分摊");
                    settlementRecordService.insert(settlementRecord);
                    SettlementAccountAsset settlementAccountAsset = new SettlementAccountAsset();
                    settlementAccountAsset.setStockinfoId(exchangePairVCoin);
                    settlementAccountAsset.setRelatedStockinfoId(exchangePairMoney);
                    settlementAccountAsset.setSettlementType(1);
                    settlementAccountAsset.setSettlementTime(settlementRecord.getSettlementTime());
                    settlementAccountAsset.setSettlementPrice(settlementRecord.getSettlementPrice());
                    settlementAccountAsset.setPeriodAssessmentAmt(BigDecimal.ZERO);
                    settlementAccountAsset.setPeriodAssessmentRate(BigDecimal.ZERO);
                    settlementAccountAsset.setRemark(settlementRecord.getRemark());
                    settlementAccountAsset.setTableAsset(getStockInfo(exchangePairMoney).getTableAsset());
                    settlementAccountAssetService.insertFromAsset(settlementAccountAsset);
                }
                else// 没有分摊金 则此时分摊金额为0
                {
                    settlementRecord.setAssessmentRate(BigDecimal.ZERO);
                    settlementRecord.setRelatedStockinfoId(exchangePairMoney);
                    settlementRecord.setSettlementPrice(settlementPrice);
                    settlementRecord.setSettlementTime(new Timestamp(System.currentTimeMillis()));
                    settlementRecord.setSettlementType(1);
                    settlementRecord.setStockinfoId(exchangePairVCoin);
                    settlementRecord.setWearingSharingLossesAmt(BigDecimal.ZERO);
                    settlementRecord.setRemark("没有亏损不存在分摊");
                    settlementRecordService.insert(settlementRecord);
                    SettlementAccountAsset settlementAccountAsset = new SettlementAccountAsset();
                    settlementAccountAsset.setStockinfoId(exchangePairVCoin);
                    settlementAccountAsset.setRelatedStockinfoId(exchangePairMoney);
                    settlementAccountAsset.setSettlementType(1);
                    settlementAccountAsset.setSettlementTime(settlementRecord.getSettlementTime());
                    settlementAccountAsset.setSettlementPrice(settlementRecord.getSettlementPrice());
                    settlementAccountAsset.setPeriodAssessmentAmt(BigDecimal.ZERO);
                    settlementAccountAsset.setPeriodAssessmentRate(BigDecimal.ZERO);
                    settlementAccountAsset.setRemark(settlementRecord.getRemark());
                    settlementAccountAsset.setTableAsset(getStockInfo(exchangePairMoney).getTableAsset());
                    settlementAccountAssetService.insertFromAsset(settlementAccountAsset);
                }
                logger.debug("交割记录结束。。。。。。。。。。。。。。。。。。。。");
            }
        }
        logger.debug("超级用户资产和负债清零开始。。。。。。。。。。。。。。。。。。。。");
        // 超级账户资产清零开始
        AccountDebitAsset usdxDebitEntity = new AccountDebitAsset();
        superUsdxDebit = BigDecimal.ZERO;
        accountDebitAsset = new AccountDebitAsset();
        accountDebitAsset.setBorrowerAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
        accountDebitAsset.setStockinfoId(exchangePairMoney);
        accountDebitAsset.setRelatedStockinfoId(exchangePairMoney);
        accountDebitAsset.setTableName(getStockInfo(exchangePairMoney).getTableDebitAsset());
        debitList = accountDebitAssetService.findList(accountDebitAsset);
        if (debitList.size() > 0)
        {
            usdxDebitEntity = debitList.get(0);
            superUsdxDebit = usdxDebitEntity.getDebitAmt();
        }
        logger.debug("超级用户资产和负债清零 法定货币负债:" + usdxDebitEntity.toString());
        AccountDebitAsset btcDebitEntity = new AccountDebitAsset();
        superBtcDebit = BigDecimal.ZERO;
        accountDebitAsset = new AccountDebitAsset();
        accountDebitAsset.setBorrowerAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
        accountDebitAsset.setStockinfoId(exchangePairVCoin);
        accountDebitAsset.setRelatedStockinfoId(exchangePairMoney);
        accountDebitAsset.setTableName(getStockInfo(exchangePairMoney).getTableDebitAsset());
        debitList = accountDebitAssetService.findList(accountDebitAsset);
        if (debitList.size() > 0)
        {
            btcDebitEntity = debitList.get(0);
            superBtcDebit = btcDebitEntity.getDebitAmt();
        }
        logger.debug("超级用户资产和负债清零 数字货币负债:" + btcDebitEntity.toString());
        settlementPrice = getSettlementPrice(exchangePairMoney);
        logger.debug("超级用户资产和负债清零 结算价:" + settlementPrice);
        AccountContractAsset btcAssetEntity = new AccountContractAsset();
        superBtcAsset = BigDecimal.ZERO;
        accountContractAsset = new AccountContractAsset();
        accountContractAsset.setAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
        accountContractAsset.setStockinfoId(exchangePairVCoin);
        accountContractAsset.setRelatedStockinfoId(exchangePairMoney);
        accountContractAsset.setTableName(getStockInfo(exchangePairMoney).getTableAsset());
        list = accountContractAssetService.findList(accountContractAsset);
        if (list.size() > 0)
        {
            btcAssetEntity = list.get(0);
            superBtcAsset = btcAssetEntity.getAmount();
        }
        logger.debug("超级用户资产和负债清零 数字货币资产:" + btcAssetEntity.toString());
        AccountContractAsset usdxAssetEntity = new AccountContractAsset();
        superUsdxAsset = BigDecimal.ZERO;
        accountContractAsset = new AccountContractAsset();
        accountContractAsset.setAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
        accountContractAsset.setStockinfoId(exchangePairMoney);
        accountContractAsset.setRelatedStockinfoId(exchangePairMoney);
        accountContractAsset.setTableName(getStockInfo(exchangePairMoney).getTableAsset());
        list = accountContractAssetService.findList(accountContractAsset);
        if (list.size() > 0)
        {
            usdxAssetEntity = list.get(0);
            superUsdxAsset = usdxAssetEntity.getAmount();
        }
        logger.debug("超级用户资产和负债清零 法定货币资产:" + usdxAssetEntity.toString());
        needBtc = superBtcAsset.subtract(superBtcDebit).add((superUsdxAsset.subtract(superUsdxDebit)).divide(settlementPrice, 12, BigDecimal.ROUND_HALF_UP));
        logger.debug("盈亏情况：" + needBtc);
        if (needBtc.compareTo(BigDecimal.ZERO) >= 0)
        {
            result = true;
        }
        else
        {
            result = needBtc.abs().compareTo(BigDecimal.valueOf(0.001)) <= 0;
        }
        logger.debug("比较的结果：" + result);
        if (result)
        {
            FundModel fundModel = new FundModel();
            // 1.数字货币资产清零
            if (btcAssetEntity != null && btcAssetEntity.getAmount() != null && btcAssetEntity.getAmount().compareTo(BigDecimal.ZERO) > 0)
            {
                fundModel = new FundModel();
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_SUPERACCOUNT_VCOIN_ASSET_TO_ZREO);
                fundModel.setStockinfoId(btcAssetEntity.getStockinfoId());
                fundModel.setStockinfoIdEx(btcAssetEntity.getRelatedStockinfoId());
                fundModel.setAmount(btcAssetEntity.getAmount());
                fundModel.setAccountId(btcAssetEntity.getAccountId());
                fundModel.setOriginalBusinessId(btcAssetEntity.getId());
                fundModel.setCreateBy(FundConsts.SYSTEM_ACCOUNT_ID);
                fundModel.setFee(BigDecimal.ZERO);
                logger.debug("超级用户资产和负债清零 数字货币清零 准备:" + fundModel.toString());
                fundService.fundTransaction(fundModel);
            }
            else
            {
                logger.debug("超级用户资产和负债清零 数字资产已经清零");
            }
            // 2.数字货币资产清零
            if (usdxAssetEntity != null && usdxAssetEntity.getAmount() != null && usdxAssetEntity.getAmount().compareTo(BigDecimal.ZERO) > 0)
            {
                fundModel = new FundModel();
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_SUPERACCOUNT_MONEY_ASSET_TO_ZREO);
                fundModel.setStockinfoId(usdxAssetEntity.getStockinfoId());
                fundModel.setStockinfoIdEx(usdxAssetEntity.getRelatedStockinfoId());
                fundModel.setAmount(usdxAssetEntity.getAmount());
                fundModel.setAccountId(usdxAssetEntity.getAccountId());
                fundModel.setOriginalBusinessId(usdxAssetEntity.getId());
                fundModel.setCreateBy(FundConsts.SYSTEM_ACCOUNT_ID);
                fundModel.setFee(BigDecimal.ZERO);
                logger.debug("超级用户资产和负债清零 法定货币清零 准备:" + fundModel.toString());
                fundService.fundTransaction(fundModel);
            }
            else
            {
                logger.debug("超级用户资产和负债清零 法定资产已经清零");
            }
            // 3.法定货币借款清零
            if (superUsdxDebit != null && usdxDebitEntity.getId() != null && superUsdxDebit.compareTo(BigDecimal.ZERO) > 0)
            {
                usdxDebitEntity.setDebitAmt(BigDecimal.ZERO);
                usdxDebitEntity.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                usdxDebitEntity.setTableName(getStockInfo(exchangePairMoney).getTableDebitAsset());
                accountDebitAssetService.updateByPrimaryKey(usdxDebitEntity);
                // 资产处理
                fundModel = new FundModel();
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_SUPERACCOUNT_VCOIN_DEBIT_TO_ZREO);
                fundModel.setStockinfoId(usdxDebitEntity.getStockinfoId());
                fundModel.setStockinfoIdEx(usdxDebitEntity.getRelatedStockinfoId());
                fundModel.setAmount(superUsdxDebit);
                fundModel.setFee(BigDecimal.ZERO);
                fundModel.setAccountId(usdxDebitEntity.getBorrowerAccountId());
                fundModel.setCreateBy(usdxDebitEntity.getBorrowerAccountId());
                fundModel.setOriginalBusinessId(usdxDebitEntity.getId());
                logger.debug("超级用户资产和负债清零 法定货币借款清零 准备:" + fundModel.toString());
                fundService.fundTransaction(fundModel);
            }
            else
            {
                logger.debug("超级用户资产和负债清零 法定货币借款已经清零");
            }
            // 4.数字货币借款清零
            if (superBtcDebit != null && btcDebitEntity.getId() != null && superBtcDebit.compareTo(BigDecimal.ZERO) > 0)
            {
                btcDebitEntity.setDebitAmt(BigDecimal.ZERO);
                btcDebitEntity.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                btcDebitEntity.setTableName(getStockInfo(exchangePairMoney).getTableDebitAsset());
                accountDebitAssetService.updateByPrimaryKey(btcDebitEntity);
                // 资产处理
                fundModel = new FundModel();
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_SUPERACCOUNT_MONEY_DEBIT_TO_ZREO);
                fundModel.setStockinfoId(btcDebitEntity.getStockinfoId());
                fundModel.setStockinfoIdEx(btcDebitEntity.getRelatedStockinfoId());
                fundModel.setAmount(superBtcDebit);
                fundModel.setFee(BigDecimal.ZERO);
                fundModel.setAccountId(btcDebitEntity.getBorrowerAccountId());
                fundModel.setCreateBy(btcDebitEntity.getBorrowerAccountId());
                fundModel.setOriginalBusinessId(btcDebitEntity.getId());
                logger.debug("超级用户资产和负债清零 数字货币借款清零 准备:" + fundModel.toString());
                fundService.fundTransaction(fundModel);
            }
            else
            {
                logger.debug("超级用户资产和负债清零 数字货币借款已经清零");
            }
        }
        else
        {
            logger.debug("超级用户资产和负债清零 误差大于千分之一 ");
            throw new BusinessException("误差大于千分之一，超级账户清除负债和资产失败！");
        }
        logger.debug("超级用户资产和负债清零结束。。。。。。。。。。。。。。。。。。。。");
        logger.debug("修改所有账户的期初值开始。。。。。。。。。。。。。。。。。。。。");
        // tableName 已在服务层处理
        accountContractAssetService.updateContractAssetInitialAmt(exchangePairMoney);
        logger.debug("修改所有账户的期初值结束。。。。。。。。。。。。。。。。。。。。");
        logger.debug("调用监控修改所有账户的期初值开始。。。。。。。。。。。。。。。。。。。。");
        List<MonitorBalance> monitorBalancesList = new ArrayList<MonitorBalance>();
        MonitorBalance monitorBalance = new MonitorBalance();
        Timestamp time = new Timestamp(System.currentTimeMillis());
        accountContractAsset = new AccountContractAsset();
        accountContractAsset.setRelatedStockinfoId(exchangePairMoney);
        accountContractAsset.setTableName(getStockInfo(exchangePairMoney).getTableAsset());
        List<AccountContractAsset> assetList = accountContractAssetService.findList(accountContractAsset);
        for (AccountContractAsset asset : assetList)
        {
            monitorBalance = new MonitorBalance();
            monitorBalance.setAccountId(asset.getAccountId());
            monitorBalance.setStockinfoId(asset.getStockinfoId());
            monitorBalance.setBizCategoryId(asset.getRelatedStockinfoId());
            monitorBalance.setAcctAssetType(MonitorConst.MONITOR_ASSETTYPE_CONTRACT);
            // 期初日期
            monitorBalance.setBusinessDate(time);
            monitorBalance.setBeginBal(asset.getAmount());
            monitorBalance.setBeginFrozenBal(BigDecimal.ZERO);
            monitorBalance.setBeginFeeBal(BigDecimal.ZERO);
            monitorBalance.setEndBal(BigDecimal.ZERO);
            monitorBalance.setEndFrozenBal(BigDecimal.ZERO);
            monitorBalance.setEndFeeBal(BigDecimal.ZERO);
            // 生成日期
            monitorBalance.setCreateDate(time);
            monitorBalancesList.add(monitorBalance);
        }
        int iResult = monitorBalanceService.createInitialBalance(monitorBalancesList);
        System.out.println(iResult);
        logger.debug("调用监控修改所有账户的期初值结束。。。。。。。。。。。。。。。。。。。。");
    }
    
    @Override
    public void doSuperAssetMoveSettlement(Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        AccountContractAsset accountContractAsset = new AccountContractAsset();
        accountContractAsset.setRelatedStockinfoId(exchangePairMoney);
        // tableName已在服务层处理
        List<AccountContractAsset> list = accountContractAssetService.selectSuperAdminAsset(accountContractAsset, FundConsts.SYSTEM_ACCOUNT_ID,
                FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID, FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID);
        for (AccountContractAsset asset : list)
        {
            if (asset.getAccountId().longValue() != FundConsts.SYSTEM_ACCOUNT_ID)
            {
                logger.debug("账户：" + asset.getAccountId() + " 资金情况：" + asset.toString());
                if (asset.getAmount().compareTo(asset.getFrozenAmt()) <= 0)
                {
                    logger.debug("账户：" + asset.getAccountId() + " 没有可用余额");
                }
                else
                {
                    logger.debug("账户：" + asset.getAccountId() + " 有可用余额");
                    FundModel fundModel = new FundModel();
                    fundModel.setAmount(asset.getAmount().subtract(asset.getFrozenAmt()));
                    fundModel.setAccountId(asset.getAccountId());
                    fundModel.setStockinfoId(asset.getStockinfoId());
                    fundModel.setStockinfoIdEx(asset.getRelatedStockinfoId());
                    if (asset.getStockinfoId().longValue() == exchangePairVCoin)
                    {
                        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_MOVE_VCOIN);
                        fundService.doSettlementAssetMove(fundModel);
                    }
                    else if (asset.getStockinfoId().longValue() == exchangePairMoney)
                    {
                        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_MOVE_MONEY);
                        fundService.doSettlementAssetMove(fundModel);
                    }
                    else
                    {
                        logger.debug("账户：" + asset.getAccountId() + " 不支持该证券ID资产转移：" + asset.getStockinfoId());
                    }
                }
            }
            else
            {
                logger.debug("超级用户的资产不能转移");
            }
        }
    }
    
    /**
     * 从db中查找合约账户资产记录
     * @param accountId
     * @param stockinfoId
     * @return
     * @author zcx 2017-09-19 15:52:34
     */
    private AccountContractAsset findAccountContractAssetFormDB(Long accountId, Long stockinfoId, Long relatedStockinfoId)
    {
        AccountContractAsset accountContractAsset = new AccountContractAsset();
        accountContractAsset.setAccountId(accountId);
        accountContractAsset.setStockinfoId(stockinfoId);
        accountContractAsset.setRelatedStockinfoId(relatedStockinfoId);
        accountContractAsset.setTableName(getStockInfo(relatedStockinfoId).getTableAsset());
        List<AccountContractAsset> accountWalletAssetList;
        try
        {
            accountWalletAssetList = accountContractAssetService.findList(accountContractAsset);
            if (accountWalletAssetList.size() > 0)
            {
                accountContractAsset = accountWalletAssetList.get(0);
                logger.debug("从db中查找合约账户资产记录 accountContractAsset:" + accountContractAsset.toString());
            }
            else
            {
                accountContractAsset = new AccountContractAsset();
                accountContractAsset.setRelatedStockinfoId(relatedStockinfoId);
                accountContractAsset.setStockinfoId(stockinfoId);
                accountContractAsset.setAccountId(accountId);
                accountContractAsset.setAmount(BigDecimal.ZERO);
                accountContractAsset.setFrozenAmt(BigDecimal.ZERO);
                accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                accountContractAsset.setId(SerialnoUtils.buildPrimaryKey());
                accountContractAsset.setTableName(getStockInfo(relatedStockinfoId).getTableAsset());
                accountContractAssetService.insert(accountContractAsset);
                logger.debug("从db中查找合约账户资产记录 accountContractAsset:" + accountContractAsset.toString());
            }
        }
        catch (Exception e)
        {
            accountContractAsset = null;
            logger.debug("从db中查找合约账户资产记录 error:" + e.getMessage());
        }
        return accountContractAsset;
    }
    
    @Override
    public void doCalcReserveFund(Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(exchangePairMoney);
        Long clearStockinfoId = stockInfo.getClearStockinfoId();
        Long tradeStockinfoId = stockInfo.getTradeStockinfoId();
        logger.debug("清算标的证券ID:"+clearStockinfoId);
        logger.debug("交易标的证券ID:"+tradeStockinfoId);

        // 清算标的证券ID不等于交易标的证券ID 需要将法币兑换成数字货币
        boolean isVCoin = clearStockinfoId.longValue()!=tradeStockinfoId.longValue();
        logger.debug("是否交易数字货币："+isVCoin);

        // 锁表操作
        accountContractAssetService.selectByPrimaryKeyOnRowLock(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_RESERVE_ALLOCATION_ID, exchangePairVCoin,
                exchangePairMoney, getStockInfo(exchangePairMoney).getTableAsset());
        accountContractAssetService.selectByPrimaryKeyOnRowLock(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_RESERVE_ALLOCATION_ID, exchangePairMoney,
                exchangePairMoney, getStockInfo(exchangePairMoney).getTableAsset());
        accountContractAssetService.selectByPrimaryKeyOnRowLock(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID, exchangePairVCoin, exchangePairMoney,
                getStockInfo(exchangePairMoney).getTableAsset());
        accountContractAssetService.selectByPrimaryKeyOnRowLock(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID, exchangePairMoney, exchangePairMoney,
                getStockInfo(exchangePairMoney).getTableAsset());
        accountContractAssetService.selectByPrimaryKeyOnRowLock(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID, exchangePairVCoin, exchangePairMoney,
                getStockInfo(exchangePairMoney).getTableAsset());
        accountContractAssetService.selectByPrimaryKeyOnRowLock(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID, exchangePairMoney, exchangePairMoney,
                getStockInfo(exchangePairMoney).getTableAsset());
        BigDecimal shortReserveAllocation = entrustVCoinMoneyService.findSumShortReserveAllocation(getStockInfo(exchangePairMoney).getTableEntrust());
        BigDecimal longReserveAllocation = entrustVCoinMoneyService.findSumLongReserveAllocation(getStockInfo(exchangePairMoney).getTableEntrust());

        AccountContractAsset accountContractAssetDB = this.findAccountContractAssetFormDB(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID, exchangePairMoney,
                exchangePairMoney);
        logger.debug("多头爆仓 超级用户挂单的盈利情况 法定货币账户可用：" + accountContractAssetDB.getAmount().subtract(accountContractAssetDB.getFrozenAmt()));
        if (longReserveAllocation.compareTo((accountContractAssetDB.getAmount().subtract(accountContractAssetDB.getFrozenAmt()))) > 0)
        {
            longReserveAllocation = accountContractAssetDB.getAmount().subtract(accountContractAssetDB.getFrozenAmt());
        }
        accountContractAssetDB = this.findAccountContractAssetFormDB(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID, exchangePairMoney, exchangePairMoney);
        logger.debug("空头爆仓 超级用户挂单的盈利情况 法定货币账户可用：" + accountContractAssetDB.getAmount().subtract(accountContractAssetDB.getFrozenAmt()));
        if (shortReserveAllocation.compareTo((accountContractAssetDB.getAmount().subtract(accountContractAssetDB.getFrozenAmt()))) > 0)
        {
            shortReserveAllocation = accountContractAssetDB.getAmount().subtract(accountContractAssetDB.getFrozenAmt());
        }
        logger.debug("多头爆仓 超级用户挂单的盈利情况：" + longReserveAllocation);
        logger.debug("空头爆仓 超级用户挂单的盈利情况：" + shortReserveAllocation);
        if (longReserveAllocation.compareTo(BigDecimal.ZERO) > 0)
        {
            FundModel fundModel = new FundModel();
            fundModel.setStockinfoId(exchangePairMoney);
            fundModel.setStockinfoIdEx(exchangePairMoney);
            fundModel.setAmount(longReserveAllocation);
            fundModel.setAmountEx(longReserveAllocation);
            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_MONEY_CONTRIBUTION_QUOTA_MOVE_DECREASE);
            fundModel.setAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID);
            fundModel.setOriginalBusinessId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID);
            fundModel.setCreateBy(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_RESERVE_ALLOCATION_ID);// 转移给准备金用户
            logger.debug("多头爆仓 超级用户挂单的盈利情况：准备数据：" + fundModel.toString());
            fundService.fundTransaction(fundModel);
            logger.debug("多头爆仓 超级用户挂单的盈利情况：准备金转移完毕：" + fundModel.toString());
        }
        else
        {
            logger.debug("多头爆仓 超级用户挂单的盈利情况：没有盈利");
        }
        if (shortReserveAllocation.compareTo(BigDecimal.ZERO) > 0)
        {
            FundModel fundModel = new FundModel();
            fundModel.setStockinfoId(exchangePairMoney);
            fundModel.setStockinfoIdEx(exchangePairMoney);
            fundModel.setAmount(shortReserveAllocation);
            fundModel.setAmountEx(shortReserveAllocation);
            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_MONEY_CONTRIBUTION_QUOTA_MOVE_DECREASE);
            fundModel.setAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID);
            fundModel.setOriginalBusinessId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID);
            fundModel.setCreateBy(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_RESERVE_ALLOCATION_ID);// 转移给准备金用户
            logger.debug("空头爆仓 超级用户挂单的盈利情况：准备数据：" + fundModel.toString());
            fundService.fundTransaction(fundModel);
            logger.debug("空头爆仓 超级用户挂单的盈利情况：准备金转移完毕：" + fundModel.toString());
        }
        else
        {
            logger.debug("空头爆仓 超级用户挂单的盈利情况：没有盈利");
        }
        // 清算标的证券ID不等于交易标的证券ID 需要将法币兑换成数字货币
        if(isVCoin)
        {
            logger.debug("兑换开始。。。。。。。。。。。。。。。。。。。。。。。。");
            BigDecimal settlementPrice = BigDecimal.ZERO;
            settlementPrice = getSettlementPrice(exchangePairMoney);
            AccountContractAsset accountContractAsset = new AccountContractAsset();
            accountContractAsset.setStockinfoId(exchangePairMoney);
            accountContractAsset.setRelatedStockinfoId(exchangePairMoney);
            accountContractAsset.setAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_RESERVE_ALLOCATION_ID);
            accountContractAsset.setTableName(getStockInfo(exchangePairMoney).getTableAsset());
            List<AccountContractAsset> list = accountContractAssetService.findList(accountContractAsset);
            if (list.size() > 0)
            {
                accountContractAsset = list.get(0);
                if (accountContractAsset.getAmount().subtract(accountContractAsset.getFrozenAmt()).compareTo(BigDecimal.ZERO) > 0)
                {
                    logger.debug("USDX可用余额大于0 用户：" + accountContractAsset.getAccountId() + accountContractAsset.getAccountName() + " 有USDX可用余额！");
                    fundService.superSettlementMoneyExchangeVCoin(accountContractAsset, settlementPrice);
                }
                else
                {
                    logger.debug("USDX可用余额小于0 用户：" + accountContractAsset.getAccountId() + accountContractAsset.getAccountName() + " 没有USDX可用余额！");
                }
            }
            logger.debug("兑换结束。。。。。。。。。。。。。。。。。。。。。。。。");
        }
        else
        {
            logger.debug("清算标的证券ID等于交易标的证券ID 不需要将法币兑换成数字货币");
        }
    }
    
    /**
     * 交割挂单
     */
    @Override
    public void doSettlementEntrustVCoinMoney(Long relatedStockinfoId, Long stockinfoId) throws BusinessException
    {
        BigDecimal settlementPrice = BigDecimal.ZERO;
        settlementPrice = getSettlementPrice(relatedStockinfoId);
        // 查找负债大于0的借款记录(全部)-- 批量自动还款
        AccountDebitAsset accountDebitAsset = new AccountDebitAsset();
        accountDebitAsset.setRelatedStockinfoId(relatedStockinfoId);
        List<AccountDebitAsset> list ;
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(relatedStockinfoId);
        Long clearStockinfoId = stockInfo.getClearStockinfoId();
        Long tradeStockinfoId = stockInfo.getTradeStockinfoId();
        logger.debug("清算标的证券ID:"+clearStockinfoId);
        logger.debug("交易标的证券ID:"+tradeStockinfoId);

        // 清算标的证券ID不等于交易标的证券ID 需要将法币兑换成数字货币
        boolean isVCoin = clearStockinfoId.longValue()!=tradeStockinfoId.longValue();
        logger.debug("是否交易数字货币："+isVCoin);

        // 清算标的证券ID不等于交易标的证券ID 买卖数字货币
        if(isVCoin)
        {
            // 查找负债大于0的借款记录(USDX)-- 批量委托
            accountDebitAsset = new AccountDebitAsset();
            accountDebitAsset.setRelatedStockinfoId(relatedStockinfoId);
            accountDebitAsset.setStockinfoId(relatedStockinfoId);
            accountDebitAsset.setTableName(getStockInfo(relatedStockinfoId).getTableDebitAsset());
            list = accountDebitAssetService.findListForDebit(accountDebitAsset);
            for (AccountDebitAsset record : list)
            {
                BigDecimal entrustAmt = record.getDebitAmt().divide(settlementPrice, 4, BigDecimal.ROUND_DOWN);
                entrustAmt = BigDecimal.valueOf(entrustAmt.doubleValue());
                if (record.getBorrowerAccountId().longValue() == FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID
                        || record.getBorrowerAccountId().longValue() == FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID)
                {
                    // 判断数字货币可用
                    EnableModel enableModel = new EnableModel();
                    enableModel.setStockinfoId(stockinfoId);
                    enableModel.setRelatedStockinfoId(relatedStockinfoId);
                    enableModel.setAccountId(record.getBorrowerAccountId());
                    enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTSELL_ENTRUST);
                    enableModel = enableService.entrustTerminalEnable(enableModel);
                    if (enableModel.getEnableAmount().compareTo(BigDecimal.ZERO) < 0)
                    {
                        logger.debug("交割撮合交易 数字货币可用余额小于0不能下单");
                        throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
                    }
                    if (enableModel.getEnableAmount().compareTo(entrustAmt) < 0)
                    {
                        BigDecimal enableBtc = enableModel.getEnableAmount().setScale(8, BigDecimal.ROUND_DOWN);
                        entrustAmt = BigDecimal.valueOf(enableBtc.doubleValue());
                    }
                }

                double amount = entrustAmt.setScale(stockInfo.getSellAmountPrecision(), BigDecimal.ROUND_DOWN).doubleValue();
                entrustAmt = BigDecimal.valueOf(amount);

                // 负债usdx 卖出btc
                EntrustModel entrustModel = new EntrustModel();
                entrustModel.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
                entrustModel.setEntrustAmt(entrustAmt);
                entrustModel.setEntrustPrice(settlementPrice);
                entrustModel.setEntrustAmtEx(entrustAmt.multiply(settlementPrice));
                entrustModel.setFee(BigDecimal.ZERO);
                entrustModel.setTradeType(TradeEnums.TRADE_TYPE_SETTLEMENTTRADE.getCode());// 交割交易
                entrustModel.setAccountId(record.getBorrowerAccountId());
                entrustModel.setEntrustDirect(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode());// 委托卖出BTC
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 7);
                entrustModel.setEntrustEndDate(calendar.getTimeInMillis());
                entrustModel.setStockinfoId(stockinfoId);
                entrustModel.setStockinfoIdEx(relatedStockinfoId);
                entrustModel.setFee(BigDecimal.ZERO);
                entrustModel.setFeeRate(BigDecimal.ZERO);
                entrustModel.setEntrustAccountType(true);
                entrustModel.setTableName(getStockInfo(relatedStockinfoId).getTableEntrust());
                logger.debug("借款委托：" + entrustModel.toString());
                // 委托服务
                if (entrustAmt.compareTo(BigDecimal.ZERO) > 0)
                {
                    tradeService.entrust(entrustModel);
                }
            }
            // 查找资产列表(USDX)
            AccountContractAsset accountContractAsset = new AccountContractAsset();
            accountContractAsset.setStockinfoId(relatedStockinfoId);
            accountContractAsset.setRelatedStockinfoId(relatedStockinfoId);
            accountContractAsset.setTableName(getStockInfo(relatedStockinfoId).getTableAsset());
            List<AccountContractAsset> assetList = accountContractAssetService.findList(accountContractAsset);
            for (AccountContractAsset asset : assetList)
            {
                if (asset.getAmount().compareTo(BigDecimal.ZERO) > 0)
                {
                    BigDecimal entrustAmt = asset.getAmount().divide(settlementPrice, 8, BigDecimal.ROUND_DOWN);
                    entrustAmt = BigDecimal.valueOf(entrustAmt.doubleValue());
                    double amount = entrustAmt.setScale(stockInfo.getBuyAmountPrecision(), BigDecimal.ROUND_DOWN).doubleValue();
                    entrustAmt = BigDecimal.valueOf(amount);

                    // 持有usdx资产 买入btc
                    EntrustModel entrustModel = new EntrustModel();
                    entrustModel.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
                    entrustModel.setEntrustAmt(entrustAmt);
                    entrustModel.setEntrustPrice(settlementPrice);
                    entrustModel.setEntrustAmtEx(entrustAmt.multiply(settlementPrice));
                    entrustModel.setFee(BigDecimal.ZERO);
                    entrustModel.setFeeRate(BigDecimal.ZERO);
                    entrustModel.setTradeType(TradeEnums.TRADE_TYPE_SETTLEMENTTRADE.getCode());// 撮合交易
                    entrustModel.setAccountId(asset.getAccountId());
                    entrustModel.setEntrustDirect(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode());// 现货买入
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, 7);
                    entrustModel.setEntrustEndDate(calendar.getTimeInMillis());
                    entrustModel.setStockinfoId(stockinfoId);
                    entrustModel.setStockinfoIdEx(relatedStockinfoId);
                    entrustModel.setFee(BigDecimal.ZERO);
                    entrustModel.setTableName(getStockInfo(relatedStockinfoId).getTableEntrust());
                    entrustModel.setEntrustAccountType(true);
                    logger.debug("资产委托：" + entrustModel.toString());
                    // 委托服务
                    if (entrustAmt.compareTo(BigDecimal.ZERO) > 0)
                    {
                        tradeService.entrust(entrustModel);
                    }
                }
            }
        }
        else  // 清算标的证券ID不等于交易标的证券ID 买卖法定货币
        {
            // 查找负债大于0的借款记录(BTC)-- 批量委托
            accountDebitAsset = new AccountDebitAsset();
            accountDebitAsset.setRelatedStockinfoId(stockinfoId);
            accountDebitAsset.setStockinfoId(relatedStockinfoId);
            accountDebitAsset.setTableName(getStockInfo(relatedStockinfoId).getTableDebitAsset());
            list = accountDebitAssetService.findListForDebit(accountDebitAsset);
            for (AccountDebitAsset record : list)
            {
                BigDecimal entrustAmt = record.getDebitAmt().divide(settlementPrice, 4, BigDecimal.ROUND_DOWN);
                entrustAmt = BigDecimal.valueOf(entrustAmt.doubleValue());
                if (record.getBorrowerAccountId().longValue() == FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID
                        || record.getBorrowerAccountId().longValue() == FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID)
                {
                    // 判断法币货币可用
                    EnableModel enableModel = new EnableModel();
                    enableModel.setStockinfoId(relatedStockinfoId);
                    enableModel.setRelatedStockinfoId(relatedStockinfoId);
                    enableModel.setAccountId(record.getBorrowerAccountId());
                    enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTSELL_ENTRUST);
                    enableModel = enableService.entrustTerminalEnable(enableModel);
                    if (enableModel.getEnableAmount().compareTo(BigDecimal.ZERO) < 0)
                    {
                        logger.debug("交割撮合交易 数字货币可用余额小于0不能下单");
                        throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
                    }
                    if (enableModel.getEnableAmount().compareTo(entrustAmt) < 0)
                    {
                        BigDecimal enableBtc = enableModel.getEnableAmount().setScale(8, BigDecimal.ROUND_DOWN);
                        entrustAmt = BigDecimal.valueOf(enableBtc.doubleValue());
                    }
                }

                double amount = entrustAmt.setScale(stockInfo.getSellAmountPrecision(), BigDecimal.ROUND_DOWN).doubleValue();
                entrustAmt = BigDecimal.valueOf(amount);
                // 负债btc 卖出usdz
                EntrustModel entrustModel = new EntrustModel();
                entrustModel.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
                entrustModel.setEntrustAmt(entrustAmt);
                entrustModel.setEntrustPrice(settlementPrice);
                entrustModel.setEntrustAmtEx(entrustAmt.multiply(settlementPrice));
                entrustModel.setFee(BigDecimal.ZERO);
                entrustModel.setTradeType(TradeEnums.TRADE_TYPE_SETTLEMENTTRADE.getCode());// 交割交易
                entrustModel.setAccountId(record.getBorrowerAccountId());
                entrustModel.setEntrustDirect(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode());// 委托卖出USDZ
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 7);
                entrustModel.setEntrustEndDate(calendar.getTimeInMillis());
                entrustModel.setStockinfoId(stockinfoId);
                entrustModel.setStockinfoIdEx(relatedStockinfoId);
                entrustModel.setFee(BigDecimal.ZERO);
                entrustModel.setFeeRate(BigDecimal.ZERO);
                entrustModel.setEntrustAccountType(true);
                entrustModel.setTableName(getStockInfo(relatedStockinfoId).getTableEntrust());
                logger.debug("借款委托：" + entrustModel.toString());
                // 委托服务
                if (entrustAmt.compareTo(BigDecimal.ZERO) > 0)
                {
                    tradeService.entrust(entrustModel);
                }
            }
            // 查找资产列表(btc)
            AccountContractAsset accountContractAsset = new AccountContractAsset();
            accountContractAsset.setStockinfoId(stockinfoId);
            accountContractAsset.setRelatedStockinfoId(relatedStockinfoId);
            accountContractAsset.setTableName(getStockInfo(relatedStockinfoId).getTableAsset());
            List<AccountContractAsset> assetList = accountContractAssetService.findList(accountContractAsset);
            for (AccountContractAsset asset : assetList)
            {
                if (asset.getAmount().compareTo(BigDecimal.ZERO) > 0)
                {
                    BigDecimal entrustAmt = asset.getAmount().divide(settlementPrice, 8, BigDecimal.ROUND_DOWN);
                    entrustAmt = BigDecimal.valueOf(entrustAmt.doubleValue());
                    double amount = entrustAmt.setScale(stockInfo.getBuyAmountPrecision(), BigDecimal.ROUND_DOWN).doubleValue();
                    entrustAmt = BigDecimal.valueOf(amount);
                    // 持有btc资产 买入usdz
                    EntrustModel entrustModel = new EntrustModel();
                    entrustModel.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
                    entrustModel.setEntrustAmt(entrustAmt);
                    entrustModel.setEntrustPrice(settlementPrice);
                    entrustModel.setEntrustAmtEx(entrustAmt.multiply(settlementPrice));
                    entrustModel.setFee(BigDecimal.ZERO);
                    entrustModel.setFeeRate(BigDecimal.ZERO);
                    entrustModel.setTradeType(TradeEnums.TRADE_TYPE_SETTLEMENTTRADE.getCode());// 撮合交易
                    entrustModel.setAccountId(asset.getAccountId());
                    entrustModel.setEntrustDirect(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode());// 现货买入
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, 7);
                    entrustModel.setEntrustEndDate(calendar.getTimeInMillis());
                    entrustModel.setStockinfoId(stockinfoId);
                    entrustModel.setStockinfoIdEx(relatedStockinfoId);
                    entrustModel.setFee(BigDecimal.ZERO);
                    entrustModel.setTableName(getStockInfo(relatedStockinfoId).getTableEntrust());
                    entrustModel.setEntrustAccountType(true);
                    logger.debug("资产委托：" + entrustModel.toString());
                    // 委托服务
                    if (entrustAmt.compareTo(BigDecimal.ZERO) > 0)
                    {
                        tradeService.entrust(entrustModel);
                    }
                }
            }
        }

    }
    
    /**
     * 法定货币残渣清零
     */
    @Override
    public void settlementAssetAndDebitToZero(Long relatedStockinfoId) throws BusinessException
    {
        // 查找负债大于0的借款记录
        AccountDebitAsset accountDebitAsset = new AccountDebitAsset();
        accountDebitAsset.setRelatedStockinfoId(relatedStockinfoId);
        accountDebitAsset.setStockinfoId(relatedStockinfoId);
        accountDebitAsset.setTableName(getStockInfo(relatedStockinfoId).getTableDebitAsset());
        List<AccountDebitAsset> list = accountDebitAssetService.findListForDebit(accountDebitAsset);
        for (AccountDebitAsset record : list)
        {
            record.setDebitAmt(BigDecimal.ZERO);
            record.setUpdateDate(new Date());
            accountDebitAssetService.updateByPrimaryKey(record);
        }
        // 查找资产列表
        AccountContractAsset accountContractAsset = new AccountContractAsset();
        accountContractAsset.setStockinfoId(relatedStockinfoId);
        accountContractAsset.setRelatedStockinfoId(relatedStockinfoId);
        accountContractAsset.setTableName(getStockInfo(relatedStockinfoId).getTableAsset());
        List<AccountContractAsset> assetList = accountContractAssetService.findList(accountContractAsset);
        for (AccountContractAsset asset : assetList)
        {
            if (asset.getAmount().compareTo(BigDecimal.ZERO) > 0)
            {
                asset.setAmount(BigDecimal.ZERO);
                accountContractAsset.setTableName(getStockInfo(relatedStockinfoId).getTableAsset());
                accountContractAssetService.updateByPrimaryKey(asset);
            }
        }
    }
    
    @Override
    public void stepOperator(Integer step, Long exchangePairMoney, Long exchangePairVCoin,Long accountId) throws BusinessException
    {
        StockInfo info = stockInfoService.selectByPrimaryKey(exchangePairMoney);
        logger.debug("按钮步骤：" + step + " 已操作步骤：" + info.getSettlementStep());
        if (!StringUtils.equalsIgnoreCase(info.getSettlementStep().toString(), step.toString())) { throw new BusinessException("错误的步骤，请按顺序执行！"); }
        boolean result = false;
        switch (step)
        {
            case 1:// 关闭交易开关
                logger.debug("1111开始流程第一步1111");
                result = updateSwitch("no", step, exchangePairMoney);
                if (!result)
                {
                    insertSettlementProcessLog(step.longValue(), -1, "交易开关关闭开关", "不可以重复打开交易开关",accountId);
                    throw new BusinessException(CommonEnums.FAIL);
                }
                else
                {
                    insertSettlementProcessLog(step.longValue(), 1, "交易开关关闭开关", "操作成功",accountId);
                }
                logger.debug("1111结束流程第一步1111");
                break;
            case 2:
                logger.debug("2222开始流程第二步2222");
                try
                {
                    withdrawByPlatSettlement(exchangePairMoney, exchangePairVCoin);
                    updateStep((step + 1), exchangePairMoney);
                    insertSettlementProcessLog(step.longValue(), 1, "撤销委托中的全部交易", "操作成功",accountId);
                }
                catch (BusinessException e)
                {
                    insertSettlementProcessLog(step.longValue(), -1, "撤销委托中的全部交易", "操作失败",accountId);
                    throw new BusinessException(e.getErrorCode());
                }
                logger.debug("2222结束流程第二步2222");
                break;
            case 3:
                logger.debug("3333开始流程第三步3333");
                try
                {
                    // 准备金分摊提取
                    doCalcReserveFund(exchangePairMoney, exchangePairVCoin);
                    updateStep((step + 1), exchangePairMoney);
                    insertSettlementProcessLog(step.longValue(), 1, "准备金分摊提取", "操作成功",accountId);
                }
                catch (BusinessException e)
                {
                    insertSettlementProcessLog(step.longValue(), -1, "准备金分摊提取", "操作失败",accountId);
                    throw new BusinessException(e.getErrorCode());
                }
                logger.debug("3333结束流程第三步3333");
                break;
            case 4:
                logger.debug("4444开始流程第四步4444");
                try{
                    //自动还款
                    accountDebitAssetService.autoDebitRepaymentToPlat(getStockInfo(exchangePairMoney));
                    accountDebitAssetService.autoDebitRepaymentToPlat(getStockInfo(exchangePairMoney));
                    // 交割挂单
                    doSettlementEntrustVCoinMoney(exchangePairMoney, exchangePairVCoin);
                    updateStep((step + 1), exchangePairMoney);
                    insertSettlementProcessLog(step.longValue(), 1, "交割挂单", "操作成功",accountId);
                }
                catch (BusinessException e)
                {
                    insertSettlementProcessLog(step.longValue(), -1, "交割挂单", "操作失败",accountId);
                    throw new BusinessException(e.getErrorCode());
                }
                logger.debug("4444结束流程第四步4444");
                break;
            case 5:
                logger.debug("5555开始流程第五步5555");
                try
                {
                    // 先撤单
                    withdrawByPlatSettlement(exchangePairMoney, exchangePairVCoin);
                    // 借款STOCKINFOID 可用关联STOCKINFOID 还款不够 可以用来抵扣的STOCKINFOID
                    accountDebitAssetService.doDebitRepaymentPowerByPlat(FundConsts.BORROW_STOCKINFO_TYPE_LEGAL_MONEY, exchangePairMoney, exchangePairMoney,
                            exchangePairVCoin);
                    // 借款STOCKINFOID 可用关联STOCKINFOID 还款不够 可以用来抵扣的STOCKINFOID
                    accountDebitAssetService.doDebitRepaymentPowerByPlat(FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH, exchangePairVCoin, exchangePairMoney,
                            exchangePairMoney);
                    // 法定货币残渣清零
                    // settlementService.settlementAssetAndDebitToZero(FundConsts.WALLET_BTC2USDX_TYPE);
                    updateStep((step + 1), exchangePairMoney);
                    insertSettlementProcessLog(step.longValue(), 1, "撤单并强制还款", "操作成功",accountId);
                }
                catch (BusinessException e)
                {
                    insertSettlementProcessLog(step.longValue(), -1, "撤单并强制还款", "操作失败",accountId);
                    throw new BusinessException(e.getErrorCode());
                }
                logger.debug("5555结束流程第五步5555");
                break;
            case 6:
                logger.debug("6666开始流程第六步6666");
                try
                {
                    // 借款STOCKINFOID 可用关联STOCKINFOID 还款不够 可以用来抵扣的STOCKINFOID
                    // accountDebitAssetService.doDebitRepaymentPowerByPlat(FundConsts.WALLET_BTC2USDX_TYPE, FundConsts.WALLET_BTC2USDX_TYPE, FundConsts.WALLET_BTC_TYPE);
                    // 借款STOCKINFOID 可用关联STOCKINFOID 还款不够 可以用来抵扣的STOCKINFOID
                    // accountDebitAssetService.doDebitRepaymentPowerByPlat(FundConsts.WALLET_BTC_TYPE, FundConsts.WALLET_BTC2USDX_TYPE, FundConsts.WALLET_BTC2USDX_TYPE);
                    // 剩余债务的转移
                    accountDebitAssetService.settlementDebitMoveToPlatAfterPowerRepay(exchangePairMoney, exchangePairVCoin, FundConsts.SYSTEM_ACCOUNT_ID);
                    updateStep((step + 1), exchangePairMoney);
                    insertSettlementProcessLog(step.longValue(), 1, "债务转移", "操作成功",accountId);
                }
                catch (BusinessException e)
                {
                    insertSettlementProcessLog(step.longValue(), -1, "债务转移", "操作失败",accountId);
                    throw new BusinessException(e.getErrorCode());
                }
                logger.debug("6666结束流程第六步6666");
                break;
            case 7:
                logger.debug("7777开始流程第七步7777");
                try
                {
                    // 超级用户用数字货币兑换用户法定货币 用数字货币折算成法定货币还款给平台
                    fundService.doSettlementMoneyExchangeVCoin(exchangePairMoney, exchangePairVCoin);
                    // 借款STOCKINFOID 可用关联STOCKINFOID 还款不够 可以用来抵扣的STOCKINFOID
                    accountDebitAssetService.doDebitRepaymentPowerByPlat(FundConsts.BORROW_STOCKINFO_TYPE_LEGAL_MONEY, exchangePairMoney, exchangePairMoney,
                            exchangePairVCoin);
                    // 借款STOCKINFOID 可用关联STOCKINFOID 还款不够 可以用来抵扣的STOCKINFOID
                    accountDebitAssetService.doDebitRepaymentPowerByPlat(FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH, exchangePairVCoin, exchangePairMoney,
                            exchangePairMoney);
                    updateStep((step + 1), exchangePairMoney);
                    insertSettlementProcessLog(step.longValue(), 1, "普通法定货币平台购回", "操作成功",accountId);
                }
                catch (BusinessException e)
                {
                    insertSettlementProcessLog(step.longValue(), -1, "普通法定货币平台购回", "操作失败",accountId);
                    throw new BusinessException(e.getErrorCode());
                }
                logger.debug("7777结束流程第七步7777");
                break;
            case 8:
                logger.debug("8888开始流程第八步8888");
                try
                {
                    // 多空超级用户数字货币转移
                    doSuperAssetMoveSettlement(exchangePairMoney, exchangePairVCoin);
                    updateStep((step + 1), exchangePairMoney);
                    insertSettlementProcessLog(step.longValue(), 1, "多空超级用户数字货币转移", "操作成功",accountId);
                }
                catch (BusinessException e)
                {
                    insertSettlementProcessLog(step.longValue(), -1, "多空超级用户数字货币转移", "操作失败",accountId);
                    throw new BusinessException(e.getErrorCode());
                }
                logger.debug("8888结束流程第八步8888");
                break;
            case 9:
                logger.debug("aaaa开始流程第九步aaaa");
                try
                {
                    // 分摊+超级账户账户资产和借款清零
                    settlementService.doFenTanByPlatSettlement(exchangePairMoney, exchangePairVCoin);
                    updateStep((step + 1), exchangePairMoney);
                    insertSettlementProcessLog(step.longValue(), 1, "市场盈亏和分摊确认", "操作成功",accountId);
                    insertSettlementProcessLog(step.longValue(), 1, "超级账户账户资产和借款清零", "操作成功",accountId);
                }
                catch (BusinessException e)
                {
                    insertSettlementProcessLog(step.longValue(), -1, "市场盈亏和分摊确认", "操作失败",accountId);
                    insertSettlementProcessLog(step.longValue(), -1, "超级账户账户资产和借款清零", "操作失败",accountId);
                    throw new BusinessException(e.getErrorCode());
                }
                logger.debug("aaaa结束流程第九步aaaa");
                break;
            case 10:// 打开开关
                result = updateSwitch("yes", step, exchangePairMoney);
                if (!result)
                {
                    insertSettlementProcessLog(step.longValue(), -1, "打开交易开关", "不可以重复关闭交易开关",accountId);
                    throw new BusinessException(CommonEnums.FAIL);
                }
                else
                {
                    insertSettlementProcessLog(step.longValue(), 1, "打开交易开关", "操作成功",accountId);
                }
                break;
            default:// 错误步骤
                insertSettlementProcessLog(0l, -1, "调用错误的步骤", "参数非法",accountId);
                throw new BusinessException(CommonEnums.FAIL);
        }
    }
    
    /**
     * 公共-修改开关
     * @param status
     * @return
     */
    public boolean updateSwitch(String status, Integer step, Long stockinfoId)
    {
        if (step == 1)
        {
            step = 2;
        }
        else
        {
            step = 1;
        }
        //StockInfo stockInfo = stockInfoService.selectByPrimaryKey(stockinfoId);
        StockInfo stockInfo = new StockInfo();
        stockInfo.setId(stockinfoId);
        stockInfo.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        stockInfo.setSettlementStep(step);
        stockInfo.setCanTrade(status);
        stockInfoService.updateByPrimaryKeySelective(stockInfo);
        return true;
    }
    
    /**
     * 公共-修改操作步骤
     * @param value
     */
    public void updateStep(int value, Long stockinfoId)
    {
        List<StockInfo> list = stockInfoService.findListByIds(stockinfoId.toString());
        if (list.size() > 0)
        {
            StockInfo info = list.get(0);
            if (info.getSettlementPrice() == null)
            {
                logger.debug("不存在");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            info.setSettlementStep(value);
            stockInfoService.updateByPrimaryKey(info);
        }
        else
        {
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
    }
    
    /**
     * 公用-插入流程清算日志
     * @param step
     * @param status
     * @param processName
     */
    public void insertSettlementProcessLog(Long step, int status, String processName, String remark,Long accountId)
    {
        SettlementProcessLog log = new SettlementProcessLog();
        log.setCreateBy(accountId);
        log.setCreateDate(new Timestamp(System.currentTimeMillis()));
        log.setProcessId(1001L);
        log.setStatus(BigDecimal.valueOf(status));
        log.setProcessName(step + "." + processName);
        log.setRemark(remark);
        settlementProcessLogService.insert(log);
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
    
    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
}

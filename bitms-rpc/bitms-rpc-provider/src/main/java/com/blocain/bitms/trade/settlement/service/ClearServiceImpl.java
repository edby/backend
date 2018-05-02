/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.settlement.service;

import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountContractAsset;
import com.blocain.bitms.trade.fund.model.ContractAssetModel;
import com.blocain.bitms.trade.fund.model.FundModel;
import com.blocain.bitms.trade.fund.service.AccountContractAssetService;
import com.blocain.bitms.trade.fund.service.AccountDebitAssetService;
import com.blocain.bitms.trade.fund.service.FundService;
import com.blocain.bitms.trade.risk.model.EnableModel;
import com.blocain.bitms.trade.risk.service.EnableService;
import com.blocain.bitms.trade.settlement.entity.SettlementAccountAsset;
import com.blocain.bitms.trade.settlement.entity.SettlementRecord;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.trade.entity.EntrustVCoinMoney;
import com.blocain.bitms.trade.trade.service.EntrustVCoinMoneyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 清算结算核心处理
 * <p>File：ClearServiceImpl.java</p>
 * <p>Title: ClearServiceImpl</p>
 * <p>Description:ClearServiceImpl</p>
 * <p>Copyright: Copyright (c) 2017年8月17日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Service
public class ClearServiceImpl implements ClearService
{
    public static final Logger                  logger = LoggerFactory.getLogger(ClearServiceImpl.class);

    @Autowired
    StockInfoService                            stockInfoService;

    @Autowired
    private EnableService                       enableService;

    @Autowired
    private EntrustVCoinMoneyService            entrustVCoinMoneyService;

    @Autowired
    private AccountContractAssetService         accountContractAssetService;

    @Autowired
    private SettlementRecordService             settlementRecordService;

    @Autowired
    private SettlementAccountAssetService       settlementAccountAssetService;

    @Autowired
    private SettlementService                   settlementService;

    @Autowired
    private FundService                         fundService;

    @Autowired
    private AccountDebitAssetService accountDebitAssetService;

    /**
     * 清算结算核心业务处理入口
     */
    @Override
    public void clear(Long exchangePairMoney, Long exchangePairVCoin, BigDecimal clearPrice) throws BusinessException
    {
        // 分摊准备金计算
        settlementService.doCalcReserveFund(exchangePairMoney, exchangePairVCoin);

        // 第1步 检测市场上是否有未成交的爆仓单子（多空超级用户的委托）
        List<EntrustVCoinMoney> superAccountUndealEntrusetList = entrustVCoinMoneyService.findAllInEntrust(getStockInfo(exchangePairMoney).getTableEntrust());

        // 有
        if(superAccountUndealEntrusetList.size() > 0){
            // 按照多空超级账户两个角度来分摊
            // 按照结算价格统计市场未成交爆仓单子造成的系统亏损
            BigDecimal spuerLossAmt = BigDecimal.ZERO;
            // 多头
            spuerLossAmt = entrustVCoinMoneyService.clearCalcLongSuperAccountLossAmt(getStockInfo(exchangePairMoney).getTableEntrust(), clearPrice, exchangePairMoney);
            logger.debug("spuerLongLossAmt：" + spuerLossAmt);
            if (spuerLossAmt.compareTo(BigDecimal.ZERO) > 0){
                // 多头超级账户亏损分摊
                this.superAccountLossDeal(exchangePairMoney, exchangePairVCoin, clearPrice, FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID, spuerLossAmt);

                // 多头撤单并挂单
            }

            // 空头
            spuerLossAmt = entrustVCoinMoneyService.clearCalcShortSuperAccountLossAmt(getStockInfo(exchangePairMoney).getTableEntrust(), clearPrice, exchangePairMoney);
            logger.debug("spuerShortLossAmt：" + spuerLossAmt);
            if (spuerLossAmt.compareTo(BigDecimal.ZERO) > 0){
                // 空头超级账户亏损分摊
                this.superAccountLossDeal(exchangePairMoney, exchangePairVCoin, clearPrice, FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID, spuerLossAmt);

                // 空头撤单并挂单
            }
        } else { // 无
            // 直接析出 所有普通账户 盈利 亏损（清零）
            this.clearDeal(exchangePairMoney, exchangePairVCoin, clearPrice);
        }

        // 记录结算操作日志
        settlementService.insertSettlementProcessLog(1002l, 2, "清算结算处理", "操作成功", FundConsts.SYSTEM_ACCOUNT_ID);
    }

    private void clearDeal(Long exchangePairMoney, Long exchangePairVCoin, BigDecimal clearPrice)
    {
        // 取出当前分摊基金超级用户的分摊基金数量
        BigDecimal reserveFund = BigDecimal.ZERO;
        AccountContractAsset accountContractAsset = new AccountContractAsset();
        accountContractAsset.setAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_RESERVE_ALLOCATION_ID);
        accountContractAsset.setRelatedStockinfoId(exchangePairMoney);
        accountContractAsset.setStockinfoId(exchangePairVCoin);
        accountContractAsset.setTableName(getStockInfo(exchangePairMoney).getTableAsset());
        List<AccountContractAsset> asstList = accountContractAssetService.findList(accountContractAsset);
        if (asstList.size() > 0)
        {
            reserveFund = asstList.get(0).getAmount();
            logger.debug("存在分摊准备金，数量为：" + reserveFund);
        }
        logger.debug("最终分摊准备金，数量为：" + reserveFund);

        SettlementRecord settlementRecord = new SettlementRecord();
        settlementRecord.setStockinfoId(exchangePairVCoin);
        settlementRecord.setRelatedStockinfoId(exchangePairMoney);
        settlementRecord.setSettlementType(2);
        settlementRecord.setSettlementTime(new Timestamp(System.currentTimeMillis()));
        settlementRecord.setSettlementPrice(clearPrice);
        settlementRecord.setReserveOrgAmt(reserveFund); // 分摊基金原始数量
        settlementRecord.setReserveAllocatAmt(BigDecimal.ZERO);// 分摊基金分摊数量
        settlementRecord.setReserveLastAmt(reserveFund);// 分摊基金最新数量
        settlementRecord.setWearingSharingLossesAmt(BigDecimal.ZERO); // 穿仓用户亏损分摊数量
        settlementRecord.setAssessmentRate(BigDecimal.ZERO);
        settlementRecord.setRemark("没有未成交的爆仓单子（多空超级用户的委托） 直接析出 所有普通账户 盈利 亏损（清零）!");
        settlementRecordService.insert(settlementRecord);

        // 批量插入结算账户资产表
        logger.debug("批量插入用户结算日志表开始!");
        SettlementAccountAsset settlementAccountAsset = new SettlementAccountAsset();
        settlementAccountAsset.setStockinfoId(exchangePairVCoin);
        settlementAccountAsset.setRelatedStockinfoId(exchangePairMoney);
        settlementAccountAsset.setSettlementType(2);
        settlementAccountAsset.setSettlementTime(settlementRecord.getSettlementTime());
        settlementAccountAsset.setSettlementPrice(settlementRecord.getSettlementPrice());
        settlementAccountAsset.setPeriodAssessmentAmt(BigDecimal.ZERO);
        settlementAccountAsset.setPeriodAssessmentRate(BigDecimal.ZERO);
        settlementAccountAsset.setRemark(settlementRecord.getRemark());
        settlementAccountAsset.setTableAsset(getStockInfo(exchangePairMoney).getTableAsset());
        settlementAccountAssetService.insertFromAsset(settlementAccountAsset);
        logger.debug("批量插入用户结算日志表结束!");

        // 批量重置账户交易资产表
        accountContractAssetService.updateContractAssetInitialAmt(exchangePairMoney);
    }

    /**
     * 多空超级账户结算亏损数量 分摊处理
     * @param exchangePairMoney
     * @param exchangePairVCoin
     ** @param clearPrice
     ** @param superAccountId
     */
    private void superAccountLossDeal(Long exchangePairMoney, Long exchangePairVCoin, BigDecimal clearPrice, Long superAccountId, BigDecimal spuerLossAmt)
    {
        // 取出当前分摊基金超级用户的分摊基金数量
        BigDecimal reserveFundAmt = BigDecimal.ZERO;
        AccountContractAsset accountContractAsset = new AccountContractAsset();
        accountContractAsset.setAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_RESERVE_ALLOCATION_ID);
        accountContractAsset.setRelatedStockinfoId(exchangePairMoney);
        accountContractAsset.setStockinfoId(exchangePairVCoin);
        accountContractAsset.setTableName(getStockInfo(exchangePairMoney).getTableAsset());
        List<AccountContractAsset> accountContractAssetList = accountContractAssetService.findList(accountContractAsset);
        if (accountContractAssetList.size() > 0)
        {
            reserveFundAmt = accountContractAssetList.get(0).getAmount();
            logger.debug("存在分摊准备金，数量为：" + reserveFundAmt);
        }
        logger.debug("最终分摊准备金，数量为：" + reserveFundAmt);

        // 检测风险准备金的量 优先使用  够分摊走3  如不够分摊走2-3
        // 够分摊
        if(spuerLossAmt.compareTo(reserveFundAmt) <= 0){
            // 就提取部分分摊准备金 spuerLoss
            this.reserveFundDeal(exchangePairMoney, exchangePairVCoin, clearPrice, reserveFundAmt, superAccountId, spuerLossAmt);

            SettlementRecord settlementRecord = new SettlementRecord();
            settlementRecord.setStockinfoId(exchangePairVCoin);
            settlementRecord.setRelatedStockinfoId(exchangePairMoney);
            settlementRecord.setSettlementPrice(clearPrice);
            settlementRecord.setSettlementTime(new Timestamp(System.currentTimeMillis()));
            settlementRecord.setSettlementType(2);
            settlementRecord.setReserveOrgAmt(reserveFundAmt); // 分摊基金原始数量
            settlementRecord.setReserveAllocatAmt(spuerLossAmt);// 分摊基金分摊数量
            settlementRecord.setReserveLastAmt(reserveFundAmt.subtract(spuerLossAmt));// 分摊基金最新数量
            settlementRecord.setWearingSharingLossesAmt(BigDecimal.ZERO); // 穿仓用户亏损分摊数量
            settlementRecord.setAssessmentRate(BigDecimal.ZERO);
            settlementRecord.setRemark("准备金不足用户分摊");
            settlementRecordService.insert(settlementRecord);

            SettlementAccountAsset settlementAccountAsset = new SettlementAccountAsset();
            settlementAccountAsset.setStockinfoId(exchangePairVCoin);
            settlementAccountAsset.setRelatedStockinfoId(exchangePairMoney);
            settlementAccountAsset.setSettlementType(2);
            settlementAccountAsset.setSettlementTime(settlementRecord.getSettlementTime());
            settlementAccountAsset.setSettlementPrice(settlementRecord.getSettlementPrice());
            settlementAccountAsset.setPeriodAssessmentRate(settlementRecord.getAssessmentRate());
            settlementAccountAsset.setRemark(settlementRecord.getRemark());
            settlementAccountAsset.setTableAsset(getStockInfo(exchangePairMoney).getTableAsset());
            settlementAccountAssetService.insertFromAsset(settlementAccountAsset);

            // 批量重置账户交易资产表
            accountContractAssetService.updateContractAssetInitialAmt(exchangePairMoney);

        } else { // 不够分摊
            // 先提取全部分摊准备金 reserveFund
            this.reserveFundDeal(exchangePairMoney, exchangePairVCoin, clearPrice, reserveFundAmt, superAccountId, reserveFundAmt);

            // 不够部分=spuerLoss.subtract(reserveFund) 计算盈利盘规模 按比例分摊
            this.profitAccountReserveDeal(exchangePairMoney, exchangePairVCoin, clearPrice, reserveFundAmt, spuerLossAmt.subtract(reserveFundAmt));

            // 批量重置账户交易资产表
            accountContractAssetService.updateContractAssetInitialAmt(exchangePairMoney);
        }
    }

    /**
     * 分摊准备金提取
     * @param exchangePairMoney
     * @param exchangePairVCoin
     * @param clearPrice
     * @param reserveFundAmt
     * @param spuerLossAmt
     */
    private void reserveFundDeal(Long exchangePairMoney, Long exchangePairVCoin, BigDecimal clearPrice, BigDecimal reserveFundAmt, Long superAccountId, BigDecimal spuerLossAmt){
        // 分摊准备金超级账户  减少分摊准备金
        AccountContractAsset accountContractAsset = new AccountContractAsset();
        accountContractAsset.setAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_RESERVE_ALLOCATION_ID);
        accountContractAsset.setRelatedStockinfoId(exchangePairMoney);
        accountContractAsset.setStockinfoId(exchangePairVCoin);
        accountContractAsset.setTableName(getStockInfo(exchangePairMoney).getTableAsset());
        List<AccountContractAsset> accountContractAssetList = accountContractAssetService.findList(accountContractAsset);

        AccountContractAsset accountContractAssetReserve = accountContractAssetList.get(0);
        accountContractAssetReserve.setAmount(accountContractAssetReserve.getAmount().subtract(spuerLossAmt));
        logger.debug("accountContractAssetReserve：" + accountContractAssetReserve);
        accountContractAssetService.updateByPrimaryKey(accountContractAssetReserve);

        // 多空超级账户  增加分摊来的资产
        accountContractAsset = new AccountContractAsset();
        accountContractAsset.setAccountId(superAccountId);
        accountContractAsset.setRelatedStockinfoId(exchangePairMoney);
        accountContractAsset.setStockinfoId(exchangePairVCoin);
        accountContractAsset.setTableName(getStockInfo(exchangePairMoney).getTableAsset());
        accountContractAssetList = accountContractAssetService.findList(accountContractAsset);

        AccountContractAsset accountContractAssetSuper = accountContractAssetList.get(0);
        accountContractAssetSuper.setAmount(accountContractAssetSuper.getAmount().add(spuerLossAmt));
        logger.debug("accountContractAssetSuper：" + accountContractAssetSuper);
        accountContractAssetService.updateByPrimaryKey(accountContractAssetSuper);
    }

    /**
     * 不够部分  计算盈利盘规模 按比例分摊
     * @param exchangePairMoney
     * @param exchangePairVCoin
     * @param clearPrice
     * @param reserveFundAmt
     * @param spuerLossAmt
     */
    private void profitAccountReserveDeal(Long exchangePairMoney, Long exchangePairVCoin, BigDecimal clearPrice, BigDecimal reserveFundAmt, BigDecimal spuerLossAmt){
        // 分摊处理
        if (spuerLossAmt.compareTo(BigDecimal.ZERO) > 0)
        {
            logger.debug("存在分摊。。。。");
            BigDecimal superYingLi = BigDecimal.ZERO;
            ContractAssetModel modelPersonAll = accountContractAssetService.findAccountSumContractAsset(exchangePairVCoin, exchangePairMoney, null);
            superYingLi = modelPersonAll.getSumProfit();
            logger.debug("查询全市场- BTC盈利:" + superYingLi);
            logger.debug("交割记录开始。。。。。。。。。。。。。。。。。。。。");
            SettlementRecord settlementRecord = new SettlementRecord();
            if (superYingLi.compareTo(BigDecimal.ZERO) == 0)
            {
                settlementRecord.setAssessmentRate(BigDecimal.ZERO);
            } else {
                settlementRecord.setAssessmentRate(spuerLossAmt.divide(superYingLi, 12, BigDecimal.ROUND_HALF_UP));
            }
            settlementRecord.setStockinfoId(exchangePairVCoin);
            settlementRecord.setRelatedStockinfoId(exchangePairMoney);
            settlementRecord.setSettlementPrice(clearPrice);
            settlementRecord.setSettlementTime(new Timestamp(System.currentTimeMillis()));
            settlementRecord.setSettlementType(2);
            settlementRecord.setReserveOrgAmt(reserveFundAmt); // 分摊基金原始数量
            settlementRecord.setReserveAllocatAmt(BigDecimal.ZERO);// 分摊基金分摊数量
            settlementRecord.setReserveLastAmt(reserveFundAmt);// 分摊基金最新数量
            settlementRecord.setWearingSharingLossesAmt(spuerLossAmt); // 穿仓用户亏损分摊数量
            settlementRecord.setRemark("准备金不足用户分摊");
            settlementRecordService.insert(settlementRecord);

            SettlementAccountAsset settlementAccountAsset = new SettlementAccountAsset();
            settlementAccountAsset.setStockinfoId(exchangePairVCoin);
            settlementAccountAsset.setRelatedStockinfoId(exchangePairMoney);
            settlementAccountAsset.setSettlementType(2);
            settlementAccountAsset.setSettlementTime(settlementRecord.getSettlementTime());
            settlementAccountAsset.setSettlementPrice(settlementRecord.getSettlementPrice());
            settlementAccountAsset.setPeriodAssessmentRate(settlementRecord.getAssessmentRate());
            settlementAccountAsset.setRemark(settlementRecord.getRemark());
            settlementAccountAsset.setTableAsset(getStockInfo(exchangePairMoney).getTableAsset());
            settlementAccountAssetService.insertFromAsset(settlementAccountAsset);

            // 查询全市场盈利账户列表
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
                    BigDecimal fenTanBtc = persionYingLi.divide(superYingLi, 12, BigDecimal.ROUND_HALF_UP).multiply(spuerLossAmt);
                    logger.debug("当前用户" + modelPerson.getAccountId() + "- 需分摊数字货币:" + fenTanBtc);
                    if (persionYingLi.compareTo(BigDecimal.ZERO) > 0)
                    {
                        logger.debug("=======参与分摊用户：" + modelPerson.getAccountId() + ":" + persionYingLi);
                        logger.debug("=======参与分摊用户：" + modelPerson.getAccountId() + " 盈利情况：" + persionYingLi + " asset：" + allBtcOfPersion + " assetinit："
                                + allBtcSumInitialOfPersion + " in：" + allInOfPersion + " out：" + allOutOfPersion);
                        if (spuerLossAmt.compareTo(BigDecimal.ZERO) > 0 && fenTanBtc.compareTo(BigDecimal.ZERO) > 0)// 存在亏损 需要分摊
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
    }

    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
}

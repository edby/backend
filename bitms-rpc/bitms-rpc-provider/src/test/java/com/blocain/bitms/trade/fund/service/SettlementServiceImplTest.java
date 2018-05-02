package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.boss.common.service.SysParameterService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountContractAsset;
import com.blocain.bitms.trade.fund.model.FundModel;
import com.blocain.bitms.trade.settlement.entity.SettlementRecord;
import com.blocain.bitms.trade.settlement.service.SettlementRecordService;
import com.blocain.bitms.trade.settlement.service.SettlementService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import com.blocain.bitms.trade.trade.service.EntrustVCoinMoneyService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by admin on 2017/10/26.
 */
public class SettlementServiceImplTest extends AbstractBaseTest
{
    @Autowired SettlementService settlementService;
    
    @Autowired
    EntrustVCoinMoneyService                    entrustVCoinMoneyService;
    
    @Autowired
    FundService                        fundService;
    
    @Autowired
    SysParameterService                sysParameterService;
    
    @Autowired
    AccountContractAssetService        accountContractAssetService;

    @Autowired SettlementRecordService settlementRecordService;
    
    public static final Logger  logger = LoggerFactory.getLogger(SettlementServiceImplTest.class);

    @Autowired(required = false)
    private StockInfoService stockInfoService;

    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
    
    @Test
    // 多空超级用户数字货币转移操作
    public void doSuperAssetMoveSettlement() throws Exception
    {
        settlementService.doSuperAssetMoveSettlement(FundConsts.WALLET_BTC2USDX_TYPE,FundConsts.WALLET_BTC_TYPE);
    }

    /**
     *测试插入数据
     */
    @Test
    public void testInsertSettlementRecord()
    {
        SettlementRecord settlementRecord = new SettlementRecord();
        settlementRecord.setAssessmentRate(BigDecimal.ZERO);
        settlementRecord.setRelatedStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
        settlementRecord.setReserveAllocatAmt(BigDecimal.ZERO);
        settlementRecord.setSettlementPrice(BigDecimal.valueOf(6000));
        settlementRecord.setSettlementTime(new Timestamp(System.currentTimeMillis()));
        settlementRecord.setSettlementType(1);
        settlementRecord.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        settlementRecord.setWearingSharingLossesAmt(BigDecimal.ZERO);
        settlementRecord.setRemark("没有亏损不存在分摊");
        settlementRecordService.insert(settlementRecord);
    }
    
    /**
     * 准备金
     * @throws Exception
     */
    @Test
    public void doContributionQuota() throws Exception
    {
        BigDecimal shortReserveAllocation = entrustVCoinMoneyService.findSumShortReserveAllocation(getStockInfo(FundConsts.WALLET_BTC2USDX_TYPE).getTableEntrust());
        BigDecimal longReserveAllocation = entrustVCoinMoneyService.findSumLongReserveAllocation(getStockInfo(FundConsts.WALLET_BTC2USDX_TYPE).getTableEntrust());
        logger.debug("多头爆仓 超级用户挂单的盈利情况：" + longReserveAllocation);
        logger.debug("空头爆仓 超级用户挂单的盈利情况：" + shortReserveAllocation);
        if (shortReserveAllocation.compareTo(BigDecimal.ZERO) > 0)
        {
            FundModel fundModel = new FundModel();
            fundModel.setStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
            fundModel.setStockinfoIdEx(FundConsts.WALLET_BTC2USDX_TYPE);
            fundModel.setAmount(shortReserveAllocation);
            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_MONEY_CONTRIBUTION_QUOTA_MOVE_DECREASE);
            fundModel.setAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID);
            fundModel.setOriginalBusinessId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID);
            fundService.fundTransaction(fundModel);
        }
        else
        {
            logger.debug("空头爆仓 超级用户挂单的盈利情况：没有盈利");
        }
        if (longReserveAllocation.compareTo(BigDecimal.ZERO) > 0)
        {
            FundModel fundModel = new FundModel();
            fundModel.setStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
            fundModel.setStockinfoIdEx(FundConsts.WALLET_BTC2USDX_TYPE);
            fundModel.setAmount(longReserveAllocation);
            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_MONEY_CONTRIBUTION_QUOTA_MOVE_DECREASE);
            fundModel.setAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID);
            fundModel.setOriginalBusinessId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID);
            fundService.fundTransaction(fundModel);
        }
        else
        {
            logger.debug("多头爆仓 超级用户挂单的盈利情况：没有盈利");
        }
        logger.debug("兑换开始。。。。。。。。。。。。。。。。。。。。。。。。");
        BigDecimal settlementPrice = BigDecimal.ONE;
        AccountContractAsset accountContractAsset = new AccountContractAsset();
        accountContractAsset.setStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
        accountContractAsset.setRelatedStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
        accountContractAsset.setAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_RESERVE_ALLOCATION_ID);
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
}
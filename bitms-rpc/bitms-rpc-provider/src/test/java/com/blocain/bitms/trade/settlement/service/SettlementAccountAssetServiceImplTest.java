package com.blocain.bitms.trade.settlement.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountFundCurrent;
import com.blocain.bitms.trade.fund.model.ContractAssetModel;
import com.blocain.bitms.trade.fund.service.AccountContractAssetService;
import com.blocain.bitms.trade.fund.service.AccountFundCurrentService;
import com.blocain.bitms.trade.settlement.entity.SettlementAccountAsset;
import com.blocain.bitms.trade.settlement.entity.SettlementRecord;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by admin on 2017/10/31.
 */
public class SettlementAccountAssetServiceImplTest extends AbstractBaseTest
{
    @Autowired
    SettlementRecordService       settlementRecordService;
    
    @Autowired
    AccountContractAssetService   accountContractAssetService;
    
    @Autowired
    AccountService                accountService;
    
    @Autowired
    AccountFundCurrentService     accountFundCurrentService;
    
    @Autowired
    SettlementAccountAssetService settlementAccountAssetService;

    @Autowired(required = false)
    private StockInfoService stockInfoService;

    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }

    /**
     * 交割账户资产
     */
    @Test
    public void setSettlementAccountAsset()
    {
        //界面传入 start
        Pagination pagination = new Pagination();
        pagination.setRows(10);
        pagination.setPage(1);
        SettlementAccountAsset settlementAccountAsset = new SettlementAccountAsset();
        settlementAccountAsset.setAccountName("207");//搜索用户
        settlementAccountAsset.setTimeStart("2017-10-01 14:04:14");
        settlementAccountAsset.setTimeEnd("2017-11-01 14:04:28");
        //界面传入 end

        settlementAccountAssetService.search(pagination,settlementAccountAsset);
    }

    /**
     * 交割结算记录
     */
    @Test
    public void setSettlementRecord()
    {
        //界面传入 start
        Pagination pagination = new Pagination();
        pagination.setRows(10);
        pagination.setPage(1);
        SettlementRecord settlementAccountAsset = new SettlementRecord();
        //界面传入 end

        settlementRecordService.search(pagination,settlementAccountAsset);
    }
    /**
     * 批量插入日志
     * @throws Exception
     */
    @Test
    public void testInsertAccountAssetSettle() throws Exception
    {
        BigDecimal settlementPrice = BigDecimal.ONE;
        SettlementRecord settlementRecord = new SettlementRecord();
        settlementRecord.setAssessmentRate(BigDecimal.ZERO);
        settlementRecord.setRelatedStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
        settlementRecord.setReserveOrgAmt(BigDecimal.TEN);
        settlementRecord.setReserveAllocatAmt(BigDecimal.ONE);
        settlementRecord.setReserveLastAmt(BigDecimal.TEN.subtract(BigDecimal.ONE));
        settlementRecord.setSettlementPrice(settlementPrice);
        settlementRecord.setSettlementTime(new Timestamp(System.currentTimeMillis()));
        settlementRecord.setSettlementType(1);
        settlementRecord.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        settlementRecord.setWearingSharingLossesAmt(BigDecimal.ZERO);
        settlementRecord.setRemark("没有亏损不存在分摊2017-11-01测试00001");
        settlementRecordService.insert(settlementRecord);
        List<Account> accounts = accountService.selectAll();
        for (Account account : accounts)
        {
            if (account.getId().longValue() != FundConsts.SYSTEM_ACCOUNT_ID && account.getId().longValue() != FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID
                    && account.getId().longValue() != FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID
                    && account.getId().longValue() != FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_FEE_ID)
            {
                // 当前账户-当前BTC持仓数量
                ContractAssetModel modelPerson = accountContractAssetService.findAccountSumContractAsset(FundConsts.WALLET_BTC_TYPE, FundConsts.WALLET_BTC2USDX_TYPE,
                        account.getId());
                BigDecimal allBtcOfPersion = modelPerson.getSumAmount();
                BigDecimal allBtcSumInitialOfPersion = modelPerson.getSumInitialAmt();
                // 当前账户-总流入BTC数量
                AccountFundCurrent accountFundCurrent = new AccountFundCurrent();
                accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);// 合约资产
                accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);// 增长
                accountFundCurrent.setStockinfoId(FundConsts.WALLET_BTC_TYPE);// BTC
                accountFundCurrent.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_CONTRACT);
                accountFundCurrent.setAccountId(account.getId());
                accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
                BigDecimal allInOfPersion = accountFundCurrentService.findCurrentWeekSumAmtByAccount(accountFundCurrent);
                // 当前账户--总流出BTC数量
                // 当前账户-总流出BTC数量
                accountFundCurrent = new AccountFundCurrent();
                accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);// 合约资产
                accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);// 增长
                accountFundCurrent.setStockinfoId(FundConsts.WALLET_BTC_TYPE);// BTC
                accountFundCurrent.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_CONTRACT_2_WALLET);
                accountFundCurrent.setAccountId(account.getId());
                accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
                BigDecimal allOutOfPersion = accountFundCurrentService.findCurrentWeekSumAmtByAccount(accountFundCurrent);
                BigDecimal persionYingLi = allBtcOfPersion.subtract(allBtcSumInitialOfPersion).subtract(allInOfPersion).add(allOutOfPersion);
                SettlementAccountAsset settlementAccountAsset = new SettlementAccountAsset();
                settlementAccountAsset.setAccountId(account.getId());
                settlementAccountAsset.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
                settlementAccountAsset.setRelatedStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
                settlementAccountAsset.setSettlementType(1);
                settlementAccountAsset.setSettlementTime(settlementRecord.getSettlementTime());
                settlementAccountAsset.setSettlementPrice(settlementRecord.getSettlementPrice());
                settlementAccountAsset.setPeriodInitAmt(modelPerson.getSumInitialAmt());
                settlementAccountAsset.setPeriodInflowAmt(allInOfPersion);
                settlementAccountAsset.setPeriodOutflowAmt(allOutOfPersion);
                settlementAccountAsset.setPeriodLastAmt(allBtcOfPersion);
                settlementAccountAsset.setPeriodAssessmentAmt(BigDecimal.ZERO);
                settlementAccountAsset.setPeriodAssessmentRate(BigDecimal.ZERO);
                settlementAccountAsset.setRemark(settlementRecord.getRemark());
                settlementAccountAssetService.insert(settlementAccountAsset);
            }
        }
    }

    @Test
    public void insertFromAsset() throws Exception
    {
        BigDecimal settlementPrice = BigDecimal.ONE;
        SettlementRecord settlementRecord = new SettlementRecord();
        settlementRecord.setAssessmentRate(BigDecimal.ZERO);
        settlementRecord.setRelatedStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
        settlementRecord.setReserveOrgAmt(BigDecimal.TEN);
        settlementRecord.setReserveAllocatAmt(BigDecimal.ONE);
        settlementRecord.setReserveLastAmt(BigDecimal.TEN.subtract(BigDecimal.ONE));
        settlementRecord.setSettlementPrice(settlementPrice);
        settlementRecord.setSettlementTime(new Timestamp(System.currentTimeMillis()));
        settlementRecord.setSettlementType(1);
        settlementRecord.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        settlementRecord.setWearingSharingLossesAmt(BigDecimal.ZERO);
        settlementRecord.setRemark("没有亏损不存在分摊2017-11-01测试00001");
        settlementRecordService.insert(settlementRecord);

        SettlementAccountAsset settlementAccountAsset = new SettlementAccountAsset();
        settlementAccountAsset.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        settlementAccountAsset.setRelatedStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
        settlementAccountAsset.setSettlementType(1);
        settlementAccountAsset.setSettlementTime(settlementRecord.getSettlementTime());
        settlementAccountAsset.setSettlementPrice(settlementRecord.getSettlementPrice());
        settlementAccountAsset.setPeriodAssessmentAmt(BigDecimal.ZERO);
        settlementAccountAsset.setPeriodAssessmentRate(BigDecimal.ZERO);
        settlementAccountAsset.setRemark(settlementRecord.getRemark());
        settlementAccountAsset.setTableAsset(getStockInfo(FundConsts.WALLET_BTC2USDX_TYPE).getTableAsset());
        settlementAccountAssetService.insertFromAsset(settlementAccountAsset);
    }
}
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.DateConst;
import com.blocain.bitms.tools.utils.DateUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountFundCurrent;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by admin on 2017/9/25.
 */
public class AccountFundCurrentServiceImplTest extends AbstractBaseTest
{
    @Autowired(required = false)
    private StockInfoService stockInfoService;

    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }

    @Test
    public void getChangeCurrentListByTimestamp() throws Exception
    {
        Timestamp timestamp= new Timestamp(System.currentTimeMillis()-1000*3600);
        //accountFundCurrentService.getChangeCurrentListByTimestamp(timestamp);
    }


    @Test
    //交割平仓 剩余借款-已还数据
    public void findDebitRepaySettlemenet() throws Exception
    {
        Pagination pagination = new Pagination();
        pagination.setPage(1);
        pagination.setRows(20);
        AccountFundCurrent entity = new AccountFundCurrent();
        entity.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
        entity.setTimeStart("2017-10-25 15:46:14");
        accountFundCurrentService.findDebitRepaySettlemenet(pagination, entity,
                FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT,
                FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT);
    }

    @Autowired
    AccountFundCurrentService accountFundCurrentService;
    @Test
    public void findCurrentWeekSumAmtByAccount() throws Exception
    {
        //全部
        AccountFundCurrent accountFundCurrent = new AccountFundCurrent();
        BigDecimal all = BigDecimal.ZERO;
        accountFundCurrent = new AccountFundCurrent();
        accountFundCurrent.setTimeStart(DateUtils.getDateFormat(DateUtils.getThisWeekMonday(), DateConst.DATE_FORMAT_YMDHMS));//从本周一0点0分0秒开始
        accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);//合约资产
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);//增长
        accountFundCurrent.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        accountFundCurrent.setAccountId(300000061678L);
        accountFundCurrent.setTableName(getStockInfo(FundConsts.WALLET_BTC2USDX_TYPE).getTableFundCurrent());
        all = accountFundCurrentService.findCurrentWeekSumAmtByAccount(accountFundCurrent);
        System.out.println("个人全部流入"+all);

        accountFundCurrent = new AccountFundCurrent();
        accountFundCurrent.setTimeStart(DateUtils.getDateFormat(DateUtils.getThisWeekMonday(), DateConst.DATE_FORMAT_YMDHMS));//从本周一0点0分0秒开始
        accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);//合约资产
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);//增长
        accountFundCurrent.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        accountFundCurrent.setAccountId(300000061678L);
        accountFundCurrent.setTableName(getStockInfo(FundConsts.WALLET_BTC2USDX_TYPE).getTableFundCurrent());
        all = accountFundCurrentService.findCurrentWeekSumAmtByAccount(accountFundCurrent);
        System.out.println("个人全部流出"+all);
    }
}
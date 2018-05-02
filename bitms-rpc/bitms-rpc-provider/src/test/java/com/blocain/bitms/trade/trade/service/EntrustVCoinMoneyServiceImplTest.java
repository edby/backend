package com.blocain.bitms.trade.trade.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.trade.entity.EntrustVCoinMoney;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * Created by admin on 2017/9/20.
 */
public class EntrustVCoinMoneyServiceImplTest extends AbstractBaseTest
{
    @Autowired
    EntrustVCoinMoneyService entrustVCoinMoneyService;

    @Autowired(required = false)
    private StockInfoService stockInfoService;

    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }

    //账户委托查询
    @Test
    public void enturstData() throws Exception
    {
        // 来自界面 start
        Pagination pagination = new Pagination();
        pagination.setRows(10);
        pagination.setPage(1);
        EntrustVCoinMoney entrustVCoinMoney = new EntrustVCoinMoney();
        entrustVCoinMoney.setAccountName("207");//账户搜索 其它的搜索参考实体类
        entrustVCoinMoney.setTimeStart("2017-11-01 09:29:49");//委托开始时间 注意格式
        entrustVCoinMoney.setTimeEnd("2017-11-01 09:29:49");//委托结束时间  注意格式
        // 来自界面 end
        entrustVCoinMoneyService.search(pagination,entrustVCoinMoney);
    }

    //爆仓委托查询
    @Test
    public void closePositionData() throws Exception
    {
        // 来自界面 start
        Pagination pagination = new Pagination();
        pagination.setRows(10);
        pagination.setPage(1);
        EntrustVCoinMoney entrustVCoinMoney = new EntrustVCoinMoney();
        entrustVCoinMoney.setAccountName("207");//账户搜索 其它的搜索参考实体类
        entrustVCoinMoney.setTimeStart("2017-11-01 09:29:49");//委托开始时间 注意格式
        entrustVCoinMoney.setTimeEnd("2017-11-01 09:29:49");//委托结束时间  注意格式
        // 来自界面 end
        entrustVCoinMoney.setEntrustAccountType(true);//系统下单  固定传入
        entrustVCoinMoney.setEntrustType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());//撮合交易 固定传入
        entrustVCoinMoneyService.search(pagination,entrustVCoinMoney);
    }

    /**
     * 空头爆仓 超级用户挂单的盈利情况
     * @return
     */
    @Test
    public void findSumShortReserveAllocation() throws Exception
    {
        entrustVCoinMoneyService.findSumShortReserveAllocation(getStockInfo(FundConsts.WALLET_BTC2USDX_TYPE).getTableEntrust());
    }
    /**
     * 多头爆仓 超级用户挂单的盈利情况
     * @return
     */
    @Test
    public void findSumLongReserveAllocation() throws Exception
    {
        entrustVCoinMoneyService.findSumLongReserveAllocation(getStockInfo(FundConsts.WALLET_BTC2USDX_TYPE).getTableEntrust());
    }

    @Test
    public void findAccountInEntrust() throws Exception
    {
        entrustVCoinMoneyService.findAccountInEntrust(getStockInfo(FundConsts.WALLET_BTC2USDX_TYPE).getTableEntrust(),2174374631182336l);
    }
    
    @Test
    public void findSumMatchEntrustVCoinMoneyAmtByAccount() throws Exception
    {
        BigDecimal ret = entrustVCoinMoneyService.findSumMatchEntrustVCoinMoneyAmtByAccount(2174374631182336l, FundConsts.WALLET_BTC2USDX_TYPE);
        System.out.println("2174374631182336l的USDX买入委托数量和卖出委托数量之和=" + ret);
    }

    @Test
    public void findAllInEntrust() throws Exception
    {
        entrustVCoinMoneyService.findAllInEntrust(getStockInfo(FundConsts.WALLET_BTC2USDX_TYPE).getTableEntrust());
    }

    @Test
    public void findWithdrawBySysEntrust() throws Exception
    {
        Pagination pagin= new Pagination();
        pagin.setPage(1);
        pagin.setRows(20);
        EntrustVCoinMoney entrustVCoinMoney = new EntrustVCoinMoney();
        entrustVCoinMoneyService.findWithdrawBySysEntrust(pagin,entrustVCoinMoney);
    }

    /**
     * 获取用户正在委托中的委托
     */
    @Test
    public void getAccountDoingEntrustVCoinMoneyList()
    {
        //参数：用户id，委托stockinfoId,委托类型 限价or市价
        Long accountId = 12029184939397120L;
        Pagination pagination = new Pagination();
        pagination.setRows(10);
        pagination.setPage(1);

        EntrustVCoinMoney entrustVCoinMoney = new EntrustVCoinMoney();
        entrustVCoinMoney.setAccountId(accountId);
        entrustVCoinMoney.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
        entrustVCoinMoney.setEntrustStockinfoId(FundConsts.WALLET_BTC_TYPE);
        entrustVCoinMoney.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());
        entrustVCoinMoneyService.getAccountDoingEntrustVCoinMoneyList(entrustVCoinMoney);

    }

    /**
     * 获取用户正在委托中的委托 数量
     */
    @Test
    public void getAccountDoingEntrustVCoinMoneyCnt() throws Exception
    {
        Long accountId = 300000062004L;
        Long cnt = entrustVCoinMoneyService.getAccountDoingEntrustVCoinMoneyCnt(accountId,FundConsts.WALLET_BTC2USDX_TYPE);
        System.out.println(cnt);
    }

    /**
     * 获取用户历史委托
     */
    @Test
    public void getAccountHistoryEntrustVCoinMoneyList()
    {
        //参数：用户id，委托stockinfoId,委托类型 限价or市价
        Long accountId = 12029184939397120L;
        Pagination pagination = new Pagination();
        pagination.setRows(10);
        pagination.setPage(1);
        EntrustVCoinMoney entrustVCoinMoney = new EntrustVCoinMoney();
        entrustVCoinMoney.setAccountId(accountId);
        entrustVCoinMoney.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
        entrustVCoinMoney.setEntrustStockinfoId(FundConsts.WALLET_BTC_TYPE);
        entrustVCoinMoney.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());
        entrustVCoinMoney.setTableName(getStockInfo(FundConsts.WALLET_BTC2USDX_TYPE).getTableEntrust());
        entrustVCoinMoneyService.getAccountHistoryEntrustVCoinMoneyList(pagination,entrustVCoinMoney);
    }

    //上一个交割日之后的全部已成交的系统下单
    @Test
    public void findListAfterPreSettlement() throws Exception
    {
        Pagination pagination = new Pagination();
        pagination.setRows(10);
        pagination.setPage(1);

        EntrustVCoinMoney entrustVCoinMoney = new EntrustVCoinMoney();
        entrustVCoinMoney.setStatus(TradeEnums.DEAL_STATUS_ALLDEAL.getCode());
        entrustVCoinMoney.setEntrustAccountType(true);//系统下单
        entrustVCoinMoneyService.findListAfterPreSettlement(pagination,entrustVCoinMoney);
    }

    //查询所有委托表数据
    @Test
    public void searchAll() throws Exception
    {
        Pagination pagination = new Pagination();
        pagination.setRows(10);
        pagination.setPage(1);
        EntrustVCoinMoney entrustVCoinMoney = new EntrustVCoinMoney();
        entrustVCoinMoney.setAccountId(12029184939397120L);
        entrustVCoinMoneyService.searchAll(pagination,entrustVCoinMoney);
    }

}
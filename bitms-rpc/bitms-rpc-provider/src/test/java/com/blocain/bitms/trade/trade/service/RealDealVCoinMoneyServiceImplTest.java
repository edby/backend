package com.blocain.bitms.trade.trade.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.trade.entity.RealDealVCoinMoney;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by admin on 2017/11/1.
 */
public class RealDealVCoinMoneyServiceImplTest extends AbstractBaseTest
{

    @Autowired
    RealDealVCoinMoneyService realDealVCoinMoneyService;

    @Autowired(required = false)
    private StockInfoService stockInfoService;
    
    // 成交记录查询
    @Test
    public void getRealDealVCoinMoneyList()
    {
        // 来自界面 start
        Pagination pagination = new Pagination();
        pagination.setRows(10);
        pagination.setPage(1);
        RealDealVCoinMoney realDealVCoinMoney = new RealDealVCoinMoney();
        // 其它查询条件 参照实体类
        realDealVCoinMoney.setBuyAccountName("207");// 买方帐号搜索
        realDealVCoinMoney.setSellAccountName("2000");// 卖方帐号搜索
        realDealVCoinMoney.setTimeStart("2017-10-01 09:29:49");// 成交开始时间 注意格式
        realDealVCoinMoney.setTimeEnd("2017-11-01 09:29:49");// 成交结束时间 注意格式
        // 来自界面 end
        realDealVCoinMoneyService.search(pagination, realDealVCoinMoney);
    }

    @Test
    public void findRealDealListByEntrustId() throws Exception
    {
        Pagination pagination = new Pagination();
        pagination.setRows(10);
        pagination.setPage(1);
        RealDealVCoinMoney realDealVCoinMoney = new RealDealVCoinMoney();
        realDealVCoinMoney.setEntrustId(30835584092082176L);
        realDealVCoinMoney.setTableName(getStockInfo(FundConsts.WALLET_BTC2USD_TYPE).getTableRealDeal());
        realDealVCoinMoney.setEnturstTableName(getStockInfo(FundConsts.WALLET_BTC2USD_TYPE).getTableEntrust());
        // 来自界面 end
        realDealVCoinMoneyService.findRealDealListByEntrustId(pagination,realDealVCoinMoney);
    }


    @Test
    public void testInsert()
    {
        RealDealVCoinMoney realDealVCoinMoney = new RealDealVCoinMoney();
        realDealVCoinMoney.setBuyFee(BigDecimal.ZERO);
        realDealVCoinMoney.setBuyAccountId(1L); // 普通用户买入
        realDealVCoinMoney.setBuyEntrustId(1L);
        realDealVCoinMoney.setSellAccountId(1L);// 超级用户卖出
        realDealVCoinMoney.setSellEntrustId(1L);
        realDealVCoinMoney.setBuyFeeType(FundConsts.WALLET_BMS_TYPE);
        realDealVCoinMoney.setSellFeeType(FundConsts.WALLET_BMS_TYPE);
        realDealVCoinMoney.setDealDirect(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode()); // 现货卖出
        realDealVCoinMoney.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_ASSET_TRANSFER); // 强平法定货币
        realDealVCoinMoney.setDealAmt(BigDecimal.ONE);
        realDealVCoinMoney.setDealPrice(BigDecimal.ONE);
        realDealVCoinMoney.setDealBalance(realDealVCoinMoney.getDealAmt().multiply(realDealVCoinMoney.getDealPrice()));
        realDealVCoinMoney.setTradeType(TradeEnums.TRADE_TYPE_CLOSEPOSITIONTRADE.getCode()); // 爆仓强平成交
        realDealVCoinMoney.setDealStockinfoId(FundConsts.WALLET_BMS_TYPE);
        realDealVCoinMoney.setDealTime(new Timestamp(System.currentTimeMillis()));
        realDealVCoinMoney.setRemark("内部成交 数字货币1个");
        realDealVCoinMoney.setSellFee(BigDecimal.ZERO);
        realDealVCoinMoney.setId(SerialnoUtils.buildPrimaryKey());
        realDealVCoinMoney.setTableName(getStockInfo(FundConsts.WALLET_BTC2USD_TYPE).getTableRealDeal());
        realDealVCoinMoneyService.insert(realDealVCoinMoney);
    }

    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }

}
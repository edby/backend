package com.blocain.bitms.trade.trade.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import com.blocain.bitms.trade.trade.entity.EntrustVCoinMoney;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import com.blocain.bitms.trade.trade.model.EntrustModel;

/**
 * 合约资产交易 用法定货币 买卖 数字货币
 * 例：买卖BTC 用USDX
 * Created by admin on 2017/9/20.
 */
public class Trade702ServiceImplTest extends AbstractBaseTest
{
    @Autowired(required = false)
    private TradeService             tradeService;
    
    @Autowired(required = false)
    private EntrustVCoinMoneyService entrustVCoinMoneyService;
    
    public static final Logger       logger = LoggerFactory.getLogger(Trade702ServiceImplTest.class);
    
    @Autowired(required = false)
    private StockInfoService         stockInfoService;
    
    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
    
    @Test
    /**
     * 买入BTC
     */
    public void entrustVCoinMoney702BuyBTC() throws Exception
    {
        EntrustModel entrustModel = new EntrustModel();
        // 来自界面 只接收数量和价格
        entrustModel.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());// 委托类型 限价或市价
        entrustModel.setEntrustAmt(BigDecimal.valueOf(2));
        entrustModel.setEntrustPrice(BigDecimal.valueOf(8888));
        entrustModel.setEntrustAmtEx(entrustModel.getEntrustAmt().multiply(entrustModel.getEntrustPrice()));
        entrustModel.setEntrustAmtEx(entrustModel.getEntrustAmt().setScale(12, BigDecimal.ROUND_HALF_UP).multiply(entrustModel.getEntrustPrice()));
        entrustModel.setFee(BigDecimal.ZERO);
        // 进入
        logger.debug("/matchTrade/doPushSellMatch page form = " + entrustModel.toString());
        entrustModel.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());// 撮合交易
        entrustModel.setAccountId(300000067890L);
        entrustModel.setEntrustDirect(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode());// 现货买入
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        entrustModel.setEntrustEndDate(calendar.getTimeInMillis());
        entrustModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        entrustModel.setStockinfoIdEx(FundConsts.WALLET_BTC2USDX_TYPE);
        entrustModel.setFee(BigDecimal.ZERO);
        entrustModel.setFeeRate(BigDecimal.valueOf(0.01));
        entrustModel.setTableName(getStockInfo(FundConsts.WALLET_BTC2USDX_TYPE).getTableEntrust());
        // 委托服务
        tradeService.entrust(entrustModel);
    }
    
    @Test
    /**
     * 卖出BTC
     */
    public void entrustVCoinMoney702SellBTC() throws Exception
    {
        EntrustModel entrustModel = new EntrustModel();
        // 来自界面 只接收数量和价格
        entrustModel.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());// 委托类型 限价或市价
        entrustModel.setEntrustAmt(BigDecimal.valueOf(5));
        entrustModel.setEntrustPrice(BigDecimal.valueOf(8888));
        entrustModel.setEntrustAmtEx(entrustModel.getEntrustAmt().multiply(entrustModel.getEntrustPrice()));
        entrustModel.setFee(BigDecimal.ZERO);
        entrustModel.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());// 撮合交易
        entrustModel.setAccountId(300000067890L);
        entrustModel.setEntrustDirect(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode());// 委托卖出BTC
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        entrustModel.setEntrustEndDate(calendar.getTimeInMillis());
        entrustModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        entrustModel.setStockinfoIdEx(FundConsts.WALLET_BTC2USDX_TYPE);
        entrustModel.setFee(BigDecimal.ZERO);
        entrustModel.setFeeRate(BigDecimal.valueOf(0.03));
        entrustModel.setTableName(getStockInfo(FundConsts.WALLET_BTC2USDX_TYPE).getTableEntrust());
        // 委托服务
        tradeService.entrust(entrustModel);
    }
    
    @Test
    public void entrustVCoinMoney702Withdraw() throws Exception
    {
        EntrustVCoinMoney entrustVCoinMoney = new EntrustVCoinMoney();
        entrustVCoinMoney.setAccountId(300000067890L);
        entrustVCoinMoney.setEntrustRelatedStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
        entrustVCoinMoney.setTableName(getStockInfo(FundConsts.WALLET_BTC2USDX_TYPE).getTableEntrust());
        List<EntrustVCoinMoney> list = entrustVCoinMoneyService.getAccountDoingEntrustVCoinMoneyList(entrustVCoinMoney);
        for (EntrustVCoinMoney entity : list)
        {
            Long createBy = 300000067890L;
            EntrustModel entrustModel = new EntrustModel();
            entrustModel.setEntrustId(entity.getId());
            entrustModel.setAccountId(createBy);
            entrustModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
            entrustModel.setTableName(getStockInfo(FundConsts.WALLET_BTC2USDX_TYPE).getTableEntrust());
            entrustModel.setStockinfoIdEx(FundConsts.WALLET_BTC2USDX_TYPE);
            tradeService.entrustWithdrawX(entrustModel);
        }
    }
}
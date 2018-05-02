package com.blocain.bitms.trade.trade.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.risk.service.RiskService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.trade.entity.EntrustVCoinMoney;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import com.blocain.bitms.trade.trade.model.EntrustModel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

/**
 * 杠杆现货交易 待修正
 * Created by admin on 2017/9/20.
 */
public class Trade502ServiceImplTest extends AbstractBaseTest
{
    @Autowired(required = false)
    private TradeService             tradeService;
    
    public static final Logger       logger = LoggerFactory.getLogger(Trade502ServiceImplTest.class);
    
    @Autowired(required = false)
    private StockInfoService         stockInfoService;
    
    @Autowired(required = false)
    private EntrustVCoinMoneyService entrustVCoinMoneyService;

    @Autowired(required = false)
    private RiskService              riskService;
    
    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
    
    @Test
    /**
     * 买入BTC
     */
    public void entrustVCoinMoney502BuyBTC() throws Exception
    {
        EntrustModel entrustModel = new EntrustModel();
        // 来自界面 只接收数量和价格
        entrustModel.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());// 委托类型 限价或市价
        entrustModel.setEntrustAmt(BigDecimal.valueOf(3));
        entrustModel.setEntrustPrice(BigDecimal.valueOf(102));
        entrustModel.setEntrustAmtEx(entrustModel.getEntrustAmt().multiply(entrustModel.getEntrustPrice()));
        entrustModel.setFee(BigDecimal.ZERO);
        // 进入
        entrustModel.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());// 撮合交易
        entrustModel.setAccountId(300000060010L);
        entrustModel.setEntrustDirect(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode());// 现货买入
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        entrustModel.setEntrustEndDate(calendar.getTimeInMillis());
        entrustModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        entrustModel.setStockinfoIdEx(FundConsts.WALLET_BTC2USD_TYPE);
        entrustModel.setFee(BigDecimal.ZERO);
        entrustModel.setFeeRate(BigDecimal.valueOf(0.01));
        entrustModel.setTableName(getStockInfo(FundConsts.WALLET_BTC2USD_TYPE).getTableEntrust());


        riskService.entrustRisk(stockInfoService.selectByPrimaryKey(FundConsts.WALLET_BTC2USD_TYPE), 300000060010L, entrustModel.getEntrustDirect(),
                entrustModel.getEntrustPrice(), entrustModel.getEntrustType(), FundConsts.WALLET_BTC_TYPE, FundConsts.WALLET_BTC2USD_TYPE);

        // 委托服务
        tradeService.entrust(entrustModel);
    }
    
    @Test
    /**
     * 卖出BTC
     */
    public void entrustVCoinMoney502SellBTC() throws Exception
    {
        EntrustModel entrustModel = new EntrustModel();
        // 来自界面 只接收数量和价格
        entrustModel.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());// 委托类型 限价或市价
        entrustModel.setEntrustAmt(BigDecimal.valueOf(4));
        entrustModel.setEntrustPrice(BigDecimal.valueOf(2));
        entrustModel.setEntrustAmtEx(entrustModel.getEntrustAmt().multiply(entrustModel.getEntrustPrice()));
        entrustModel.setFee(BigDecimal.ZERO);
        entrustModel.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());// 撮合交易
        entrustModel.setAccountId(300000060010L);
        entrustModel.setEntrustDirect(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode());// 委托卖出BTC
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        entrustModel.setEntrustEndDate(calendar.getTimeInMillis());
        entrustModel.setStockinfoId(FundConsts.WALLET_BIEX_TYPE);
        entrustModel.setStockinfoIdEx(FundConsts.WALLET_BTC2USD_TYPE);
        entrustModel.setFee(BigDecimal.ZERO);
        entrustModel.setFeeRate(BigDecimal.valueOf(0.02));
        entrustModel.setTableName(getStockInfo(FundConsts.WALLET_BTC2USD_TYPE).getTableEntrust());
        // 委托服务
        tradeService.entrust(entrustModel);
    }
    
    @Test
    public void entrustVCoinMoney502Withdraw() throws Exception
    {
        EntrustVCoinMoney entrustVCoinMoney = new EntrustVCoinMoney();
        entrustVCoinMoney.setAccountId(300000060010L);
        entrustVCoinMoney.setEntrustRelatedStockinfoId(FundConsts.WALLET_BTC2USD_TYPE);
        entrustVCoinMoney.setTableName(getStockInfo(FundConsts.WALLET_BTC2USD_TYPE).getTableEntrust());
        List<EntrustVCoinMoney> list = entrustVCoinMoneyService.getAccountDoingEntrustVCoinMoneyList(entrustVCoinMoney);
        for (EntrustVCoinMoney entity : list)
        {
            Long createBy = 300000060010L;
            EntrustModel entrustModel = new EntrustModel();
            entrustModel.setEntrustId(entity.getId());
            entrustModel.setAccountId(createBy);
            entrustModel.setStockinfoId(FundConsts.WALLET_BIEX_TYPE);
            entrustModel.setTableName(getStockInfo(FundConsts.WALLET_BTC2USD_TYPE).getTableEntrust());
            entrustModel.setStockinfoIdEx(FundConsts.WALLET_BTC2USD_TYPE);
            tradeService.entrustWithdrawX(entrustModel);
        }
    }
}
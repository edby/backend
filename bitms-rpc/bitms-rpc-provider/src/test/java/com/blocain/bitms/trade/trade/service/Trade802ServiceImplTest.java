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
 * 合约资产交易 用数字货币 买卖 法定货币
 * 例：买卖USDX 用BTC
 */
public class Trade802ServiceImplTest extends AbstractBaseTest
{
    @Autowired(required = false)
    private TradeService             tradeService;
    
    @Autowired(required = false)
    private EntrustVCoinMoneyService entrustVCoinMoneyService;
    
    public static final Logger       logger = LoggerFactory.getLogger(Trade802ServiceImplTest.class);
    
    @Autowired(required = false)
    private StockInfoService         stockInfoService;
    
    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
    
    @Test
    /**
     * 买入USDX
     */
    public void entrustVCoinMoney802BuyUSDX() throws Exception
    {
        EntrustModel entrustModel = new EntrustModel();
        // 来自界面 只接收数量和价格
        entrustModel.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());// 委托类型 限价或市价
        // 市价
        if (StringUtils.equalsIgnoreCase(entrustModel.getEntrustType(), TradeEnums.ENTRUST_X_ENTRUST_TYPE_MARKETPRICE.getCode()))
        {
            entrustModel.setEntrustAmt(BigDecimal.ZERO);
            entrustModel.setEntrustPrice(BigDecimal.ZERO);
            entrustModel.setEntrustAmtEx(BigDecimal.valueOf(22 * 20));
            // ---------------------------入参判断 start ------------------------------------------
            // ---------------------------入参判断 end ------------------------------------------
        }
        // 限价
        if (StringUtils.equalsIgnoreCase(entrustModel.getEntrustType(), TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode()))
        {
            entrustModel.setEntrustAmt(BigDecimal.valueOf(4));
            entrustModel.setEntrustPrice(BigDecimal.valueOf(2000));
            entrustModel.setEntrustAmtEx(entrustModel.getEntrustAmt().multiply(entrustModel.getEntrustPrice()));
            // ---------------------------入参判断 ------------------------------------------
            // ---------------------------入参判断 end ------------------------------------------
            entrustModel.setEntrustAmtEx(entrustModel.getEntrustAmt().setScale(12, BigDecimal.ROUND_HALF_UP).multiply(entrustModel.getEntrustPrice()));
            // ---------------------------正在委托的数量判断 start--------------------------------------
            // ---------------------------正在委托的数量判断 end--------------------------------------
        }
        entrustModel.setFee(BigDecimal.ZERO);
        entrustModel.setFeeRate(BigDecimal.valueOf(0.01));
        // 进入
        logger.debug("/matchTrade/doPushSellMatch page form = " + entrustModel.toString());
        entrustModel.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());// 撮合交易
        entrustModel.setAccountId(300000066666L);
        entrustModel.setEntrustDirect(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode());// 现货买入
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        entrustModel.setEntrustEndDate(calendar.getTimeInMillis());
        entrustModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        entrustModel.setStockinfoIdEx(FundConsts.WALLET_USDZ2BTC_TYPE);
        entrustModel.setFee(BigDecimal.ZERO);
        entrustModel.setTableName(getStockInfo(FundConsts.WALLET_USDZ2BTC_TYPE).getTableEntrust());
        // 委托服务
        tradeService.entrust(entrustModel);
    }
    
    @Test
    /**
     * 卖出USDX
     */
    public void entrustVCoinMoney802SellUSDX() throws Exception
    {
        EntrustModel entrustModel = new EntrustModel();
        // 来自界面 只接收数量和价格
        entrustModel.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());// 委托类型 限价或市价
        // 市价
        if (StringUtils.equalsIgnoreCase(entrustModel.getEntrustType(), TradeEnums.ENTRUST_X_ENTRUST_TYPE_MARKETPRICE.getCode()))
        {
            entrustModel.setEntrustAmt(BigDecimal.valueOf(44));
            entrustModel.setEntrustPrice(BigDecimal.ZERO);
            entrustModel.setEntrustAmtEx(BigDecimal.ZERO);
            entrustModel.setFee(BigDecimal.ZERO);
            // ---------------------------入参判断 start ------------------------------------------
            // ---------------------------入参判断 end ------------------------------------------
        }
        // 限价
        if (StringUtils.equalsIgnoreCase(entrustModel.getEntrustType(), TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode()))
        {
            entrustModel.setEntrustAmt(BigDecimal.valueOf(7));
            entrustModel.setEntrustPrice(BigDecimal.valueOf(2000));
            entrustModel.setEntrustAmtEx(entrustModel.getEntrustAmt().multiply(entrustModel.getEntrustPrice()));
            entrustModel.setFee(BigDecimal.ZERO);
            // ---------------------------入参判断 start ------------------------------------------
            // ---------------------------入参判断 end ------------------------------------------
            // ---------------------------判断委托价格 start--------------------------------------
            // ---------------------------判断委托价格 end--------------------------------------
            // ---------------------------正在委托的数量判断 start--------------------------------------
            // ---------------------------正在委托的数量判断 end--------------------------------------
        }
        entrustModel.setFeeRate(BigDecimal.valueOf(0.03));
        logger.debug("/matchTrade/doMatchSellBtc page form = " + entrustModel.toString());
        // ---------------------------判断可用 start ------------------------------------------
        // ---------------------------判断可用 end ------------------------------------------
        entrustModel.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());// 撮合交易
        entrustModel.setAccountId(300000066666L);
        entrustModel.setEntrustDirect(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode());// 委托卖出BTC
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        entrustModel.setEntrustEndDate(calendar.getTimeInMillis());
        entrustModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        entrustModel.setStockinfoIdEx(FundConsts.WALLET_USDZ2BTC_TYPE);
        entrustModel.setFee(BigDecimal.ZERO);
        entrustModel.setTableName(getStockInfo(FundConsts.WALLET_USDZ2BTC_TYPE).getTableEntrust());
        // 委托服务
        tradeService.entrust(entrustModel);
    }
    
    @Test
    public void entrustVCoinMoney802Withdraw() throws Exception
    {
        EntrustVCoinMoney entrustVCoinMoney = new EntrustVCoinMoney();
        entrustVCoinMoney.setAccountId(300000066666L);
        entrustVCoinMoney.setEntrustRelatedStockinfoId(FundConsts.WALLET_USDZ2BTC_TYPE);
        entrustVCoinMoney.setTableName(getStockInfo(FundConsts.WALLET_USDZ2BTC_TYPE).getTableEntrust());
        List<EntrustVCoinMoney> list = entrustVCoinMoneyService.getAccountDoingEntrustVCoinMoneyList(entrustVCoinMoney);
        for (EntrustVCoinMoney entity : list)
        {
            Long createBy = 300000066666L;
            EntrustModel entrustModel = new EntrustModel();
            entrustModel.setEntrustId(entity.getId());
            entrustModel.setAccountId(createBy);
            entrustModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
            entrustModel.setTableName(getStockInfo(FundConsts.WALLET_USDZ2BTC_TYPE).getTableEntrust());
            entrustModel.setStockinfoIdEx(FundConsts.WALLET_USDZ2BTC_TYPE);
            tradeService.entrustWithdrawX(entrustModel);
        }
    }
}
package com.blocain.bitms.trade.trade.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import com.blocain.bitms.trade.risk.service.RiskService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.trade.entity.EntrustVCoinMoney;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import com.blocain.bitms.trade.trade.model.EntrustModel;

/**
 * 纯现货交易 用BTC 买卖 MMM
 * Created by admin on 2017/9/20.
 */
public class Trade402ServiceImplTest extends AbstractBaseTest
{
    @Autowired(required = false)
    private TradeService             tradeService;
    
    public static final Logger       logger = LoggerFactory.getLogger(Trade402ServiceImplTest.class);
    
    @Autowired(required = false)
    private StockInfoService         stockInfoService;

    @Autowired(required = false)
    private RiskService              riskService;

    @Autowired(required = false)
    private EntrustVCoinMoneyService entrustVCoinMoneyService;
    
    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
    
    @Test
    /**
     * 买入MMM
     */
    public void entrustVCoinMoney402BuyMMM() throws Exception
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
        }
        // 限价
        if (StringUtils.equalsIgnoreCase(entrustModel.getEntrustType(), TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode()))
        {
            entrustModel.setEntrustAmt(BigDecimal.valueOf(2));
            entrustModel.setEntrustPrice(BigDecimal.valueOf(100));
            entrustModel.setEntrustAmtEx(entrustModel.getEntrustAmt().multiply(entrustModel.getEntrustPrice()));
            entrustModel.setEntrustAmtEx(entrustModel.getEntrustAmt().setScale(12, BigDecimal.ROUND_HALF_UP).multiply(entrustModel.getEntrustPrice()));
        }
        entrustModel.setFee(BigDecimal.ZERO);
        entrustModel.setFeeRate(BigDecimal.valueOf(0.01));
        // 进入
        entrustModel.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());// 撮合交易
        entrustModel.setAccountId(300000067890L);
        entrustModel.setEntrustDirect(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode());// 现货买入
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        entrustModel.setEntrustEndDate(calendar.getTimeInMillis());
        entrustModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        entrustModel.setStockinfoIdEx(FundConsts.WALLET_BTC2EUR_TYPE);
        entrustModel.setFee(BigDecimal.ZERO);
        entrustModel.setTableName(getStockInfo(FundConsts.WALLET_BTC2EUR_TYPE).getTableEntrust());

        riskService.entrustRisk(stockInfoService.selectByPrimaryKey(FundConsts.WALLET_BTC2EUR_TYPE), 300000067890L, entrustModel.getEntrustDirect(),
                entrustModel.getEntrustPrice(), entrustModel.getEntrustType(), FundConsts.WALLET_BTC_TYPE, FundConsts.WALLET_BTC2EUR_TYPE);

        // 委托服务
        tradeService.entrust(entrustModel);
    }
    
    @Test
    /**
     * 卖出MMM
     */
    public void entrustVCoinMoney402SellMMM() throws Exception
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
        }
        // 限价
        if (StringUtils.equalsIgnoreCase(entrustModel.getEntrustType(), TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode()))
        {
            entrustModel.setEntrustAmt(BigDecimal.valueOf(1));
            entrustModel.setEntrustPrice(BigDecimal.valueOf(100));
            entrustModel.setEntrustAmtEx(entrustModel.getEntrustAmt().multiply(entrustModel.getEntrustPrice()));
            entrustModel.setFee(BigDecimal.ZERO);
        }
        entrustModel.setFeeRate(BigDecimal.valueOf(0.02));
        entrustModel.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());// 撮合交易
        entrustModel.setAccountId(300000067890L);
        entrustModel.setEntrustDirect(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode());// 委托卖出BTC
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        entrustModel.setEntrustEndDate(calendar.getTimeInMillis());
        entrustModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        entrustModel.setStockinfoIdEx(FundConsts.WALLET_BTC2EUR_TYPE);
        entrustModel.setFee(BigDecimal.ZERO);
        entrustModel.setTableName(getStockInfo(FundConsts.WALLET_BTC2EUR_TYPE).getTableEntrust());
        // 委托服务
        tradeService.entrust(entrustModel);
    }
    
    @Test
    public void entrustVCoinMoney402Withdraw() throws Exception
    {
        EntrustVCoinMoney entrustVCoinMoney = new EntrustVCoinMoney();
        entrustVCoinMoney.setAccountId(300000067890L);
        entrustVCoinMoney.setEntrustRelatedStockinfoId(FundConsts.WALLET_BTC2EUR_TYPE);
        entrustVCoinMoney.setTableName(getStockInfo(FundConsts.WALLET_BTC2EUR_TYPE).getTableEntrust());
        List<EntrustVCoinMoney> list = entrustVCoinMoneyService.getAccountDoingEntrustVCoinMoneyList(entrustVCoinMoney);
        for (EntrustVCoinMoney entity : list)
        {
            Long createBy = 300000067890L;
            EntrustModel entrustModel = new EntrustModel();
            entrustModel.setEntrustId(entity.getId());
            entrustModel.setAccountId(createBy);
            entrustModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
            entrustModel.setTableName(getStockInfo(FundConsts.WALLET_BTC2EUR_TYPE).getTableEntrust());
            entrustModel.setStockinfoIdEx(FundConsts.WALLET_BTC2EUR_TYPE);
            tradeService.entrustWithdrawX(entrustModel);
        }
    }
}
package com.blocain.bitms.platscan.thread;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blocain.bitms.platscan.config.PlatScanConfig;
import com.blocain.bitms.platscan.thread.quotation.*;
import com.blocain.bitms.quotation.consts.QuotationConsts;
import com.blocain.bitms.quotation.entity.Quotation;
import com.blocain.bitms.quotation.model.QuotationParam;
import com.blocain.bitms.quotation.service.QuotationService;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.CalendarUtils;
import com.blocain.bitms.tools.utils.ListUtils;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.google.common.collect.Lists;

/**
 * 抓取外部指数行情服务线程
 * QuotationThread Introduce
 * <p>File：QuotationThread.java</p>
 * <p>Title: QuotationThread</p>
 * <p>Description: QuotationThread</p>
 * <p>Copyright: Copyright (c) 2017/9/19</p>
 * <p>Company: BloCain</p>
 *
 * @author sunbiao
 * @version 1.0
 */
@Component
public class QuotationThread implements Runnable
{
    private static Logger        logger    = LoggerFactory.getLogger(QuotationThread.class);
    
    @Autowired
    private QuotationService     quotationService;
    
    private List<QuotationParam> remoteQuotations;
    
    private boolean              isRunning = true;
    
    @Override
    public void run()
    {
        while (isRunning)
        {
            LoggerUtils.logInfo(logger, "开始抓取外部指数行情轮询任务");
            try
            {
                // 获取报价
                remoteQuotations = pullQuotation();
                List<Quotation> quoteLists = dealRemoteQuotations(remoteQuotations);
                quotationService.insertBatch(quoteLists);
                Thread.sleep(PlatScanConfig.SLEEP_DEFAULT_TINE);
            }
            catch (Exception e)
            {
                LoggerUtils.logError(logger, "抓取外部指数行情轮询任务失败：{}", e.getLocalizedMessage());
            }
            LoggerUtils.logInfo(logger, "结束抓取外部指数行情轮询任务");
        }
    }
    
    /**
     * 获取报价
     * @return {@link List <  QuotationParam  >}
     * @throws BusinessException
     */
    private List<QuotationParam> pullQuotation() throws BusinessException
    {
        List<QuotationParam> bitfinexQuotations;
        List<QuotationParam> bitstampQuotations;
        List<QuotationParam> coinbaseQuotations;
        List<QuotationParam> krakenQuotations;
        List<QuotationParam> internalConversionQuotations;
        List<QuotationParam> datas = Lists.newArrayList();
        LoggerUtils.logInfo(logger, "开始获取Bitfinex原始报价");
        AbstractQuotation bitfinex = new BitfinexQuotation();// 摘取Bitfinex报价
        bitfinexQuotations = bitfinex.getQuotation();
        LoggerUtils.logInfo(logger, "开始获取Bitstamp原始报价");
        AbstractQuotation bitstamp = new BitstampQuotation();
        bitstampQuotations = bitstamp.getQuotation();
        LoggerUtils.logInfo(logger, "开始获取Coinbase原始报价");
        AbstractQuotation coinbase = new CoinbaseQuotation();
        coinbaseQuotations = coinbase.getQuotation();
        LoggerUtils.logInfo(logger, "开始获取Kraken原始报价");
        AbstractQuotation kraken = new KrakenQuotation();
        krakenQuotations = kraken.getQuotation();
        LoggerUtils.logInfo(logger, "开始获取InternalConversion原始报价");
        AbstractQuotation internalConversion = new InternalConversionQuotation();
        internalConversionQuotations = internalConversion.getQuotation();
        if (ListUtils.isNotNull(bitfinexQuotations)) datas.addAll(bitfinexQuotations);
        if (ListUtils.isNotNull(bitstampQuotations)) datas.addAll(bitstampQuotations);
        if (ListUtils.isNotNull(coinbaseQuotations)) datas.addAll(coinbaseQuotations);
        if (ListUtils.isNotNull(krakenQuotations)) datas.addAll(krakenQuotations);
        if (ListUtils.isNotNull(internalConversionQuotations)) datas.addAll(internalConversionQuotations);
        return datas;
    }
    
    private List<Quotation> dealRemoteQuotations(List<QuotationParam> remoteQuotations)
    {
        if (ListUtils.isNull(remoteQuotations)) return null;
        List<Quotation> quotations = Lists.newArrayList();
        Long currentTime = CalendarUtils.getCurrentLong();
        for (QuotationParam data : remoteQuotations)
        {
            Quotation quotation = new Quotation();
            if (QuotationConsts.CURRECNCY_USD2BTC.equals(data.getCurrency())) quotation.setStockId(FundConsts.WALLET_BTC2USDX_TYPE);
            if (QuotationConsts.CURRECNCY_LTCX2BTC.equals(data.getCurrency())) quotation.setStockId(FundConsts.WALLET_BTC2LTCX_TYPE);
            if (QuotationConsts.CURRECNCY_ETHX2BTC.equals(data.getCurrency())) quotation.setStockId(FundConsts.WALLET_BTC2ETHX_TYPE);
            if (QuotationConsts.CURRECNCY_USD2LTC.equals(data.getCurrency())) quotation.setStockId(FundConsts.WALLET_USDX2LTC_TYPE);
            if (QuotationConsts.CURRECNCY_USD2ETH.equals(data.getCurrency())) quotation.setStockId(FundConsts.WALLET_USDX2ETH_TYPE);
            if (QuotationConsts.CURRECNCY_XXBTZUSD.equals(data.getCurrency())) quotation.setStockId(FundConsts.WALLET_BTC2USDX_TYPE);
            if (QuotationConsts.CURRECNCY_XETHZUSD.equals(data.getCurrency())) quotation.setStockId(FundConsts.WALLET_USDX2ETH_TYPE);
            if (QuotationConsts.CURRECNCY_XLTCZUSD.equals(data.getCurrency())) quotation.setStockId(FundConsts.WALLET_USDX2LTC_TYPE);
            quotation.setId(SerialnoUtils.buildPrimaryKey());
            quotation.setIdxPrice(data.getUsd());
            quotation.setDateTime(currentTime);
            quotation.setChannel(data.getChannel());
            quotations.add(quotation);
        }
        return quotations;
    }
    
    public void setRunning(boolean running)
    {
        this.isRunning = running;
    }
    
    public static void main(String[] args)
    {
        QuotationThread t = new QuotationThread();
        t.run();
    }
}

package com.blocain.bitms.platscan.thread.quotation;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.orm.utils.SpringContext;
import com.blocain.bitms.quotation.consts.QuotationConsts;
import com.blocain.bitms.quotation.entity.Quotation;
import com.blocain.bitms.quotation.model.QuotationParam;
import com.blocain.bitms.quotation.service.QuotationService;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.CalendarUtils;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

/**
 * InternalConversion Introduce
 * <p>File：InternalConversionQuotation.java</p>
 * <p>Title: InternalConversionQuotation</p>
 * <p>Description: InternalConversionQuotation</p>
 * <p>Copyright: Copyright (c) 2017/6/28</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class InternalConversionQuotation extends AbstractQuotation
{
    public static final Logger logger = LoggerFactory.getLogger(InternalConversionQuotation.class);

    public static long quotationSearchId = 0l;

    private QuotationService quotationService = SpringContext.getBean(QuotationService.class);

    @Override
    public List<QuotationParam> getQuotation() throws BusinessException
    {
        Quotation quotationSearch = new Quotation();
        quotationSearch.setChannel(QuotationConsts.RATES_CHANNEL_BITFINEX);
        quotationSearch.setStockId(FundConsts.WALLET_BTC2USDX_TYPE);
        quotationSearch.setBitfienexId(0l);
        quotationSearch.setBitstampId(0l);
        quotationSearch.setId(quotationSearchId);
        Quotation usd2BtcQuotation = quotationService.findQuotationByLastTime(quotationSearch);

        quotationSearch.setChannel(QuotationConsts.RATES_CHANNEL_BITFINEX);
        quotationSearch.setStockId(FundConsts.WALLET_USDX2LTC_TYPE);
        quotationSearch.setBitfienexId(0l);
        quotationSearch.setBitstampId(0l);
        quotationSearch.setId(quotationSearchId);
        Quotation usd2LtcQuotation = quotationService.findQuotationByLastTime(quotationSearch);

        quotationSearch.setChannel(QuotationConsts.RATES_CHANNEL_BITFINEX);
        quotationSearch.setStockId(FundConsts.WALLET_USDX2ETH_TYPE);
        quotationSearch.setBitfienexId(0l);
        quotationSearch.setBitstampId(0l);
        quotationSearch.setId(quotationSearchId);
        Quotation usd2EthQuotation = quotationService.findQuotationByLastTime(quotationSearch);

        // 记录quotationSearchId
        quotationSearchId  = usd2EthQuotation.getId();

        List<QuotationParam> quotations = Lists.newArrayList();
        QuotationParam quotation;
        if(null != usd2LtcQuotation && usd2LtcQuotation.getIdxPrice().compareTo(BigDecimal.ZERO) != 0){
            quotation = new QuotationParam(QuotationConsts.RATES_CHANNEL_INTERNAL_CONVERSION, QuotationConsts.CURRECNCY_LTCX2BTC);
            quotation.setUsd(usd2BtcQuotation.getIdxPrice().divide(usd2LtcQuotation.getIdxPrice(),8, BigDecimal.ROUND_HALF_UP));
            quotation.setDate(CalendarUtils.getCurrentLong());
            LoggerUtils.logInfo(logger, "CURRECNCY_LTCX2BTC:"+ quotation.toString());
            quotations.add(quotation);
        }

        if(null != usd2LtcQuotation && usd2LtcQuotation.getIdxPrice().compareTo(BigDecimal.ZERO) != 0){
            quotation = new QuotationParam(QuotationConsts.RATES_CHANNEL_INTERNAL_CONVERSION, QuotationConsts.CURRECNCY_ETHX2BTC);
            quotation.setUsd(usd2BtcQuotation.getIdxPrice().divide(usd2EthQuotation.getIdxPrice(),8, BigDecimal.ROUND_HALF_UP));
            quotation.setDate(CalendarUtils.getCurrentLong());
            LoggerUtils.logInfo(logger, "CURRECNCY_ETHX2BTC:"+ quotation.toString());
            quotations.add(quotation);
        }

        return quotations;
    }
    
    public static void main(String[] args) throws BusinessException
    {
        InternalConversionQuotation quotation = new InternalConversionQuotation();
        String json = JSON.toJSONString(quotation.getQuotation());
        System.out.println(json);
    }
}

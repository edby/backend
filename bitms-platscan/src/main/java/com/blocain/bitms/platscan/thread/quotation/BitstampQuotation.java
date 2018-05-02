package com.blocain.bitms.platscan.thread.quotation;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.quotation.consts.QuotationConsts;
import com.blocain.bitms.quotation.model.BitstampIndex;
import com.blocain.bitms.quotation.model.QuotationParam;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.CalendarUtils;
import com.blocain.bitms.tools.utils.HttpUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.google.common.collect.Lists;
import org.apache.http.impl.client.CloseableHttpClient;

import java.math.BigDecimal;
import java.util.List;

/**
 * BitstampQuotation Introduce
 * <p>File：BitstampQuotation.java</p>
 * <p>Title: BitstampQuotation</p>
 * <p>Description: BitstampQuotation</p>
 * <p>Copyright: Copyright (c) 2017/6/28</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class BitstampQuotation extends AbstractQuotation
{
    public static final String BITSTAMP_URL = properties.getProperty("bitstamp.url");
    
    @Override
    public List<QuotationParam> getQuotation() throws BusinessException
    {
        List<QuotationParam> quotations = Lists.newArrayList();
        String currency = "btcusd"; // 比特币对美元
        CloseableHttpClient client = HttpUtils.getHttpClient2();
        QuotationParam quotation = getRemoteQuotation(client, currency);
        if (null != quotation) quotations.add(quotation);
        currency = "ltcusd"; // 莱特币对美元
        quotation = getRemoteQuotation(client, currency);
        if (null != quotation) quotations.add(quotation);
        HttpUtils.releaseHttpClient(client);
        return quotations;
    }
    
    /**
     * 取远端行情
     * @param currency
     * @return {@link QuotationParam}
     * @throws BusinessException
     */
    private QuotationParam getRemoteQuotation(CloseableHttpClient client, String currency) throws BusinessException
    {
        StringBuffer url = new StringBuffer(BITSTAMP_URL).append("/").append(currency);
        String content = HttpUtils.get(client, url.toString());
        if (StringUtils.isBlank(content)) return null;
        if ("btcusd".equalsIgnoreCase(currency)) currency = QuotationConsts.CURRECNCY_USD2BTC;
        if ("ltcusd".equalsIgnoreCase(currency)) currency = QuotationConsts.CURRECNCY_USD2LTC;
        BitstampIndex bitstamp = JSON.parseObject(content, BitstampIndex.class);
        QuotationParam quotation = new QuotationParam(QuotationConsts.RATES_CHANNEL_BITSTAMP, currency);
        quotation.setUsd(new BigDecimal(bitstamp.getLast()));
        quotation.setDate(CalendarUtils.getCurrentLong());
        return quotation;
    }
    
    public static void main(String[] args) throws BusinessException
    {
        BitstampQuotation quotation = new BitstampQuotation();
        String json = JSON.toJSONString(quotation.getQuotation());
        System.out.println(json);
    }
}

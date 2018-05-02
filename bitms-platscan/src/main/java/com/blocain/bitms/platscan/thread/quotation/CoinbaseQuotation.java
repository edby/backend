package com.blocain.bitms.platscan.thread.quotation;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.quotation.consts.QuotationConsts;
import com.blocain.bitms.quotation.model.CoinbaseIndex;
import com.blocain.bitms.quotation.model.QuotationParam;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.CalendarUtils;
import com.blocain.bitms.tools.utils.HttpUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.http.impl.client.CloseableHttpClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * CoinbaseQuotation Introduce
 * <p>File：CoinbaseQuotation.java</p>
 * <p>Title: CoinbaseQuotation</p>
 * <p>Description: CoinbaseQuotation</p>
 * <p>Copyright: Copyright (c) 2017/6/28</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class CoinbaseQuotation extends AbstractQuotation
{
    public static final String COINBASE_URL = properties.getProperty("coinbase.url");
    
    @Override
    public List<QuotationParam> getQuotation() throws BusinessException
    {
        CloseableHttpClient client = HttpUtils.getHttpClient2();
        List<QuotationParam> quotations = Lists.newArrayList();
        Map<String, String> map = Maps.newLinkedHashMap();
        map.put("currency", "BTC");
        QuotationParam quotation = getRemoteQuotation(client, map);
        if (null != quotation) quotations.add(quotation);
        map = Maps.newLinkedHashMap();
        map.put("currency", "LTC");
        quotation = getRemoteQuotation(client, map);
        if (null != quotation) quotations.add(quotation);
        map = Maps.newLinkedHashMap();
        map.put("currency", "ETH");
        quotation = getRemoteQuotation(client, map);
        if (null != quotation) quotations.add(quotation);
        HttpUtils.releaseHttpClient(client);
        return quotations;
    }
    
    /**
     * 取远端行情
     * @param map
     * @return {@link QuotationParam}
     * @throws BusinessException
     */
    private QuotationParam getRemoteQuotation(CloseableHttpClient client, Map<String, String> map) throws BusinessException
    {
        String content = HttpUtils.get(client, COINBASE_URL, map);
        if (StringUtils.isBlank(content)) return null;
        CoinbaseIndex coinbase = JSON.parseObject(content, CoinbaseIndex.class);
        String currency = coinbase.getData().getCurrency();
        if ("BTC".equalsIgnoreCase(currency)) currency = QuotationConsts.CURRECNCY_USD2BTC;
        if ("LTC".equalsIgnoreCase(currency)) currency = QuotationConsts.CURRECNCY_USD2LTC;
        if ("ETH".equalsIgnoreCase(currency)) currency = QuotationConsts.CURRECNCY_USD2ETH;
        QuotationParam quotation = new QuotationParam(QuotationConsts.RATES_CHANNEL_COINBASE, currency);
        quotation.setDate(CalendarUtils.getCurrentLong());
        quotation.setUsd(new BigDecimal(coinbase.getData().getRates().getUSD()));
        return quotation;
    }
    
    public static void main(String[] args) throws BusinessException
    {
        CoinbaseQuotation quotation = new CoinbaseQuotation();
        String json = JSON.toJSONString(quotation.getQuotation());
        System.out.println(json);
    }
}

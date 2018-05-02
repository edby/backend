package com.blocain.bitms.platscan.thread.quotation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.quotation.consts.QuotationConsts;
import com.blocain.bitms.quotation.model.KrakenIndex;
import com.blocain.bitms.quotation.model.QuotationParam;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;

public class KrakenQuotation extends AbstractQuotation
{
    public static final Logger logger     = LoggerFactory.getLogger(KrakenQuotation.class);
    
    public static final String KRAKEN_URL = properties.getProperty("kraken.url");
    
    @Override
    public List<QuotationParam> getQuotation() throws BusinessException
    {
        CloseableHttpClient client = HttpUtils.getHttpClient2();
        Map<String, String> map = Maps.newLinkedHashMap();
        // 传入要抓取的参数
        map.put("pair", "XBTUSD,LTCUSD,ETHUSD");
        List<KrakenIndex> remotes = getRemoteQuotation(client, map);
        HttpUtils.releaseHttpClient(client);
        if (ListUtils.isNull(remotes)) return null;
        List<QuotationParam> quotations = Lists.newArrayList();
        QuotationParam quotation;
        for (KrakenIndex kraken : remotes)
        {
            quotation = new QuotationParam(QuotationConsts.RATES_CHANNEL_KRAKEN, kraken.getPair());
            quotation.setUsd(kraken.getLast());
            quotation.setDate(CalendarUtils.getCurrentLong());
            quotations.add(quotation);
        }
        return quotations;
    }
    
    /**
     * 取远端行情
     * <pair_name> = pair name
     * a = ask array(<price>, <whole lot volume>, <lot volume>),
     * b = bid array(<price>, <whole lot volume>, <lot volume>),
     * c = last trade closed array(<price>, <lot volume>),
     * v = volume array(<today>, <last 24 hours>),
     * p = volume weighted average price array(<today>, <last 24 hours>),
     * t = number of trades array(<today>, <last 24 hours>),
     * l = low array(<today>, <last 24 hours>),
     * h = high array(<today>, <last 24 hours>),
     * o = today's opening price
     *
     * @param map
     * @return {@link KrakenIndex}
     */
    private List<KrakenIndex> getRemoteQuotation(CloseableHttpClient client, Map<String, String> map)
    {
        String content = HttpUtils.get(client, KRAKEN_URL, map);
        if (StringUtils.isBlank(content)) return null;
        // LoggerUtils.logInfo(logger,"原始响应内容：" + content);
        List<KrakenIndex> krakens = Lists.newArrayList();
        KrakenIndex kraken = null;
        JSONObject json1 = null;
        JSONObject json2 = null;
        JSONObject json3 = null;
        try
        {
            // 开始解析响应体
            json1 = (JSONObject) JSON.parse(content);
            if (json1.containsKey("result"))
            {
                String s1 = json1.getString("result");
                json2 = (JSONObject) JSON.parse(s1);
                if (json2.containsKey(QuotationConsts.CURRECNCY_XXBTZUSD))
                {
                    kraken = new KrakenIndex(QuotationConsts.CURRECNCY_XXBTZUSD);
                    json3 = (JSONObject) JSON.parse(json2.getString(QuotationConsts.CURRECNCY_XXBTZUSD));
                    krakens.add(doSet(kraken, json3));
                }
                if (json2.containsKey(QuotationConsts.CURRECNCY_XETHZUSD))
                {
                    kraken = new KrakenIndex(QuotationConsts.CURRECNCY_XETHZUSD);
                    json3 = (JSONObject) JSON.parse(json2.getString(QuotationConsts.CURRECNCY_XETHZUSD));
                    krakens.add(doSet(kraken, json3));
                }
                if (json2.containsKey(QuotationConsts.CURRECNCY_XLTCZUSD))
                {
                    kraken = new KrakenIndex(QuotationConsts.CURRECNCY_XLTCZUSD);
                    json3 = (JSONObject) JSON.parse(json2.getString(QuotationConsts.CURRECNCY_XLTCZUSD));
                    krakens.add(doSet(kraken, json3));
                }
            }
        }
        catch (Exception e)
        {
            LoggerUtils.logError(logger, "获取Kraken报价失败：{}", e.getLocalizedMessage());
        }
        return krakens;
    }
    
    /**
     * 设值
     * @param kraken
     * @param json
     */
    private KrakenIndex doSet(KrakenIndex kraken, JSONObject json)
    {
        // setLast
        JSONArray lastArr = JSON.parseArray(json.getString("c"));
        kraken.setLast(lastArr.getBigDecimal(0));
        // setHigh
        JSONArray highArr = JSON.parseArray(json.getString("h"));
        kraken.setHigh(highArr.getBigDecimal(1));
        // setLow
        JSONArray lowArr = JSON.parseArray(json.getString("l"));
        kraken.setLow(lowArr.getBigDecimal(1));
        // setVolume
        JSONArray volumeArr = JSON.parseArray(json.getString("v"));
        kraken.setVolume(volumeArr.getBigDecimal(1));
        // setWeighted
        JSONArray weightedArr = JSON.parseArray(json.getString("p"));
        kraken.setWeighted_avg(weightedArr.getBigDecimal(1));
        // LoggerUtils.logInfo(logger, kraken.toString());
        return kraken;
    }
}

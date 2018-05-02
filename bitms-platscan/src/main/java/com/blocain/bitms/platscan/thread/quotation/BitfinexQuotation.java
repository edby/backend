package com.blocain.bitms.platscan.thread.quotation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.blocain.bitms.quotation.consts.QuotationConsts;
import com.blocain.bitms.quotation.model.BitfinexIndex;
import com.blocain.bitms.quotation.model.QuotationParam;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * BitfinexQuotation Introduce
 * <p>File：BitfinexQuotation.java</p>
 * <p>Title: BitfinexQuotation</p>
 * <p>Description: BitfinexQuotation</p>
 * <p>Copyright: Copyright (c) 2017/6/28</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class BitfinexQuotation extends AbstractQuotation
{
    public static final Logger logger = LoggerFactory.getLogger(BitfinexQuotation.class);
    
    @Override
    public List<QuotationParam> getQuotation() throws BusinessException
    {
        CloseableHttpClient client = HttpUtils.getHttpClient2();
        Map<String, String> map = Maps.newLinkedHashMap();
        map.put("symbols", "tBTCUSD,tLTCUSD,tETHUSD");
        List<BitfinexIndex> remotes = getRemoteQuotation(client, map);
        HttpUtils.releaseHttpClient(client);
        if (ListUtils.isNull(remotes)) return null;
        List<QuotationParam> quotations = Lists.newArrayList();
        QuotationParam quotation;
        for (BitfinexIndex bitfinex : remotes)
        {
            quotation = new QuotationParam(QuotationConsts.RATES_CHANNEL_BITFINEX, bitfinex.getSYMBOL());
            quotation.setUsd(bitfinex.getLAST_PRICE());
            quotation.setDate(CalendarUtils.getCurrentLong());
            quotations.add(quotation);
        }
        return quotations;
    }
    
    /**
     * 取远端行情
     * @param map
     * @return {@link BitfinexIndex}
     */
    private List<BitfinexIndex> getRemoteQuotation(CloseableHttpClient client, Map<String, String> map)
    {
        String content = HttpUtils.get(client, properties.getProperty("bitfinex.url"), map);
        if (StringUtils.isBlank(content)) return null;
        List<BitfinexIndex> bitfinexes = Lists.newArrayList();
        BitfinexIndex bitfinex;
        try
        {
            JSONArray jsonArray = JSON.parseArray(content);
            for (Object json : jsonArray)
            {
                JSONArray array = JSON.parseArray(json.toString());
                String currency = array.getString(0);
                if ("tBTCUSD".equalsIgnoreCase(currency)) currency = QuotationConsts.CURRECNCY_USD2BTC;
                if ("tLTCUSD".equalsIgnoreCase(currency)) currency = QuotationConsts.CURRECNCY_USD2LTC;
                if ("tETHUSD".equalsIgnoreCase(currency)) currency = QuotationConsts.CURRECNCY_USD2ETH;
                bitfinex = new BitfinexIndex(currency);
                // bitfinex.setBID(new BigDecimal(array.getString(1)));
                // bitfinex.setBID_SIZE(new BigDecimal(array.getString(2)));
                // bitfinex.setASK(new BigDecimal(array.getString(3)));
                // bitfinex.setASK_SIZE(new BigDecimal(array.getString(4)));
                // bitfinex.setDAILY_CHANGE(new BigDecimal(array.getString(5)));
                // bitfinex.setDAILY_CHANGE_PERC(new BigDecimal(array.getString(6)));
                bitfinex.setLAST_PRICE(new BigDecimal(array.getString(7)));
                // bitfinex.setVOLUME(new BigDecimal(array.getString(8)));
                // bitfinex.setHIGH(new BigDecimal(array.getString(9)));
                // bitfinex.setLOW(new BigDecimal(array.getString(10)));
                bitfinexes.add(bitfinex);
            }
        }
        catch (Exception e)
        {
            LoggerUtils.logError(logger, "获取Bitfinex报价失败：{}", e.getLocalizedMessage());
        }
        return bitfinexes;
    }
    
    public static void main(String[] args) throws BusinessException
    {
        BitfinexQuotation quotation = new BitfinexQuotation();
        String json = JSON.toJSONString(quotation.getQuotation());
        System.out.println(json);
    }
}

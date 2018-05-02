package com.blocain.bitms.api.consts;

import java.util.concurrent.ConcurrentHashMap;

public class TopicQuotationConst
{
    // 将redis监听到的最新的消息保存在下列map中，map中topic为key,对应的消息体为value。
    public static ConcurrentHashMap<String, String> rtQuotationMap = new ConcurrentHashMap<String, String>();
    
    public static ConcurrentHashMap<String, String> deepPriceMap   = new ConcurrentHashMap<String, String>();
    
    public static ConcurrentHashMap<String, String> realdealMap    = new ConcurrentHashMap<String, String>();
    
    public static ConcurrentHashMap<String, String> kLineMap       = new ConcurrentHashMap<String, String>();
    
    // 定义下列常量，用于匹配请求url
    public static final String                      KLINE_1M       = "kline1m";
    
    public static final String                      KLINE_5M       = "kline5m";
    
    public static final String                      KLINE_15M      = "kline15m";
    
    public static final String                      KLINE_30M      = "kline30m";
    
    public static final String                      KLINE_HOUR     = "klineHour";
    
    public static final String                      KLINE_DAY      = "klineDay";
    
    public static final String                      KLINE_WEEK     = "klineWeek";
    
    public static final String                      KLINE_MONTH    = "klineMonth";
}

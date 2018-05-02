package com.blocain.bitms.quotation.consts;

import com.blocain.bitms.tools.utils.PropertiesUtils;

/**
 * TopicConst Introduce
 * <p>File：TopicConst.java</p>
 * <p>Title: TopicConst</p>
 * <p>Description: TopicConst</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class TopicConst
{
    private TopicConst()
    {
    }
    //币对标识
    public String TOPIC_EXCHANGEPAIR;
    // 1分钟 K线
    public String TOPIC_KLINE_1M;
    
    // 5分钟 K线
    public String TOPIC_KLINE_5M;
    
    // 15分钟 K线
    public String TOPIC_KLINE_15M;
    
    // 30分钟 K线
    public String TOPIC_KLINE_30M;
    
    // 时钟 K线
    public String TOPIC_KLINE_HOUR;
    
    // 日线 K线
    public String TOPIC_KLINE_DAY;
    
    // 周线 K线
    public String TOPIC_KLINE_WEEK;
    
    // 月线 K线
    public String TOPIC_KLINE_MONTH;
    
    // 最新撮合行情
    public String TOPIC_RTQUOTATION_PRICE;
    
    // 委托盘口深度行情
    public String TOPIC_ENTRUST_DEEPPRICE;
    
    // 撮合成交流水
    public String TOPIC_REALDEAL_TRANSACTION;
    
    // 全行情
    public String TOPIC_ALLRTQUOTATION;
    
    public TopicConst(String props)
    {
        PropertiesUtils properties = new PropertiesUtils(props);
        //币种标识
        TOPIC_EXCHANGEPAIR = properties.getProperty("topic.exchangePair");
        // 1分钟 K线
        TOPIC_KLINE_1M = properties.getProperty("topic.kline.1m");
        // 5分钟 K线
        TOPIC_KLINE_5M = properties.getProperty("topic.kline.5m");
        // 15分钟 K线
        TOPIC_KLINE_15M = properties.getProperty("topic.kline.15m");
        // 30分钟 K线
        TOPIC_KLINE_30M = properties.getProperty("topic.kline.30m");
        // 时钟 K线
        TOPIC_KLINE_HOUR = properties.getProperty("topic.kline.hour");
        // 日线 K线
        TOPIC_KLINE_DAY = properties.getProperty("topic.kline.day");
        // 周线 K线
        TOPIC_KLINE_WEEK = properties.getProperty("topic.kline.week");
        // 月线 K线
        TOPIC_KLINE_MONTH = properties.getProperty("topic.kline.month");
        // 最新撮合行情
        TOPIC_RTQUOTATION_PRICE = properties.getProperty("topic.rtquotation.price");
        // 委托盘口深度行情
        TOPIC_ENTRUST_DEEPPRICE = properties.getProperty("topic.entrust.deepprice");
        // 撮合成交流水
        TOPIC_REALDEAL_TRANSACTION = properties.getProperty("topic.realdeal.transaction");
        // 全行情
        TOPIC_ALLRTQUOTATION = properties.getProperty("topic.allRtQuotation");
    }
}

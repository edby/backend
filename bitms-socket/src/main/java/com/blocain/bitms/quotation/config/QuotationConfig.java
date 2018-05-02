package com.blocain.bitms.quotation.config;

import com.blocain.bitms.quotation.consts.SessionConst;
import com.blocain.bitms.quotation.consts.TopicConst;
import com.blocain.bitms.quotation.listener.*;
import com.blocain.bitms.quotation.topic.*;
import com.blocain.bitms.quotation.websocket.QuotationWebSocketHandler;
import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * 注解版本行情配置 Introduce
 * <p>Title: QuotationConfig</p>
 * <p>File：QuotationConfig.java</p>
 * <p>Description: QuotationConfig</p>
 * <p>Copyright: Copyright (c) 2018/4/16</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
// @Configuration
public class QuotationConfig
{
    @Bean
    TopicConst topicConst()
    {// 消息订阅
        return new TopicConst("quotation/133333333302.properties");
    }
    
    @Bean
    SessionConst sessionConst()
    {// 会话常量
        return new SessionConst();
    }
    
    @Bean
    QuotationWebSocketHandler webSocketHandler(TopicConst topicConst, SessionConst sessionConst)
    { // 会话处理
        QuotationWebSocketHandler messageListener = new QuotationWebSocketHandler();
        messageListener.setTopicConst(topicConst);
        messageListener.setSessionConst(sessionConst);
        return messageListener;
    }
    
    @Bean
    KLine1MinMessageListener kLine1MinMessageListener(QuotationWebSocketHandler webSocketHandler)
    {
        KLine1MinMessageListener messageListener = new KLine1MinMessageListener();
        messageListener.setQuotationWebSocketHandler(webSocketHandler);
        return messageListener;
    }
    
    @Bean
    KLine5MinMessageListener kLine5MinMessageListener(QuotationWebSocketHandler webSocketHandler)
    {
        KLine5MinMessageListener messageListener = new KLine5MinMessageListener();
        messageListener.setQuotationWebSocketHandler(webSocketHandler);
        return messageListener;
    }
    
    @Bean
    KLine15MinMessageListener kLine15MinMessageListener(QuotationWebSocketHandler webSocketHandler)
    {
        KLine15MinMessageListener messageListener = new KLine15MinMessageListener();
        messageListener.setQuotationWebSocketHandler(webSocketHandler);
        return messageListener;
    }
    
    @Bean
    KLine30MinMessageListener kLine30MinMessageListener(QuotationWebSocketHandler webSocketHandler)
    {
        KLine30MinMessageListener messageListener = new KLine30MinMessageListener();
        messageListener.setQuotationWebSocketHandler(webSocketHandler);
        return messageListener;
    }
    
    @Bean
    KLineHourMessageListener kLineHourMessageListener(QuotationWebSocketHandler webSocketHandler)
    {
        KLineHourMessageListener messageListener = new KLineHourMessageListener();
        messageListener.setQuotationWebSocketHandler(webSocketHandler);
        return messageListener;
    }
    
    @Bean
    KLineDayMessageListener kLineDayMessageListener(QuotationWebSocketHandler webSocketHandler)
    {
        KLineDayMessageListener messageListener = new KLineDayMessageListener();
        messageListener.setQuotationWebSocketHandler(webSocketHandler);
        return messageListener;
    }
    
    @Bean
    KLineWeekMessageListener kLineWeekMessageListener(QuotationWebSocketHandler webSocketHandler)
    {
        KLineWeekMessageListener messageListener = new KLineWeekMessageListener();
        messageListener.setQuotationWebSocketHandler(webSocketHandler);
        return messageListener;
    }
    
    @Bean
    KLineMonthMessageListener kLineMonthMessageListener(QuotationWebSocketHandler webSocketHandler)
    {
        KLineMonthMessageListener messageListener = new KLineMonthMessageListener();
        messageListener.setQuotationWebSocketHandler(webSocketHandler);
        return messageListener;
    }
    
    @Bean
    DeepPriceMessageListener deepPriceMessageListener(QuotationWebSocketHandler webSocketHandler)
    {
        DeepPriceMessageListener messageListener = new DeepPriceMessageListener();
        messageListener.setQuotationWebSocketHandler(webSocketHandler);
        return messageListener;
    }
    
    @Bean
    RtQuotationMessageListener rtQuotationMessageListener(QuotationWebSocketHandler webSocketHandler)
    {
        RtQuotationMessageListener messageListener = new RtQuotationMessageListener();
        messageListener.setQuotationWebSocketHandler(webSocketHandler);
        return messageListener;
    }
    
    @Bean
    RealDealMessageListener realDealMessageListener(QuotationWebSocketHandler webSocketHandler)
    {
        RealDealMessageListener messageListener = new RealDealMessageListener();
        messageListener.setQuotationWebSocketHandler(webSocketHandler);
        return messageListener;
    }
    
    @Bean
    AllRtQuotationMessageListener allRtQuotationMessageListener(QuotationWebSocketHandler webSocketHandler)
    {
        AllRtQuotationMessageListener messageListener = new AllRtQuotationMessageListener();
        messageListener.setQuotationWebSocketHandler(webSocketHandler);
        return messageListener;
    }
    
    @Bean
    RedisMessageListenerContainer listenerContainer(//
            KLine1MinMessageListener kLine1MinMessageListener, KLine1MinChannelTopic kLine1MinChannelTopic, //
            KLine5MinMessageListener kLine5MinMessageListener, KLine5MinChannelTopic kLine5MinChannelTopic, //
            KLine15MinMessageListener kLine15MinMessageListener, KLine15MinChannelTopic kLine15MinChannelTopic, //
            KLine30MinMessageListener kLine30MinMessageListener, KLine30MinChannelTopic kLine30MinChannelTopic, //
            KLineHourMessageListener kLineHourMessageListener, KLineHourChannelTopic kLineHourChannelTopic, //
            KLineDayMessageListener kLineDayMessageListener, KLineDayChannelTopic kLineDayChannelTopic, //
            KLineWeekMessageListener kLineWeekMessageListener, KLineWeekChannelTopic kLineWeekChannelTopic, //
            KLineMonthMessageListener kLineMonthMessageListener, KLineMonthChannelTopic kLineMonthChannelTopic, //
            DeepPriceMessageListener deepPriceMessageListener, DeepPriceChannelTopic deepPriceChannelTopic, //
            RtQuotationMessageListener rtQuotationMessageListener, RtQuotationChannelTopic rtQuotationChannelTopic, //
            RealDealMessageListener realDealMessageListener, RealDealChannelTopic realDealChannelTopic, //
            AllRtQuotationMessageListener allRtQuotationMessageListener, AllRtQuotationChannelTopic allRtQuotationChannelTopic, //
            JedisConnectionFactory jedisConnectionFactory)
    {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        container.addMessageListener(kLine1MinMessageListener, kLine1MinChannelTopic);
        container.addMessageListener(kLine5MinMessageListener, kLine5MinChannelTopic);
        container.addMessageListener(kLine15MinMessageListener, kLine15MinChannelTopic);
        container.addMessageListener(kLine30MinMessageListener, kLine30MinChannelTopic);
        container.addMessageListener(kLineHourMessageListener, kLineHourChannelTopic);
        container.addMessageListener(kLineDayMessageListener, kLineWeekChannelTopic);
        container.addMessageListener(kLineWeekMessageListener, kLineDayChannelTopic);
        container.addMessageListener(kLineMonthMessageListener, kLineMonthChannelTopic);
        container.addMessageListener(deepPriceMessageListener, deepPriceChannelTopic);
        container.addMessageListener(rtQuotationMessageListener, rtQuotationChannelTopic);
        container.addMessageListener(realDealMessageListener, realDealChannelTopic);
        container.addMessageListener(allRtQuotationMessageListener, allRtQuotationChannelTopic);
        return container;
    }
    
    @Bean
    KLine1MinChannelTopic kLine1MinChannelTopic(TopicConst topicConst)
    {
        return new KLine1MinChannelTopic(topicConst);
    }
    
    @Bean
    KLine5MinChannelTopic kLine5MinChannelTopic(TopicConst topicConst)
    {
        return new KLine5MinChannelTopic(topicConst);
    }
    
    @Bean
    KLine15MinChannelTopic kLine15MinChannelTopic(TopicConst topicConst)
    {
        return new KLine15MinChannelTopic(topicConst);
    }
    
    @Bean
    KLine30MinChannelTopic kLine30MinChannelTopic(TopicConst topicConst)
    {
        return new KLine30MinChannelTopic(topicConst);
    }
    
    @Bean
    KLineHourChannelTopic kLineHourChannelTopic(TopicConst topicConst)
    {
        return new KLineHourChannelTopic(topicConst);
    }
    
    @Bean
    KLineDayChannelTopic kLineDayChannelTopic(TopicConst topicConst)
    {
        return new KLineDayChannelTopic(topicConst);
    }
    
    @Bean
    KLineWeekChannelTopic kLineWeekChannelTopic(TopicConst topicConst)
    {
        return new KLineWeekChannelTopic(topicConst);
    }
    
    @Bean
    KLineMonthChannelTopic kLineMonthChannelTopic(TopicConst topicConst)
    {
        return new KLineMonthChannelTopic(topicConst);
    }
    
    @Bean
    DeepPriceChannelTopic deepPriceChannelTopic(TopicConst topicConst)
    {
        return new DeepPriceChannelTopic(topicConst);
    }
    
    @Bean
    RtQuotationChannelTopic rtQuotationChannelTopic(TopicConst topicConst)
    {
        return new RtQuotationChannelTopic(topicConst);
    }
    
    @Bean
    RealDealChannelTopic realDealChannelTopic(TopicConst topicConst)
    {
        return new RealDealChannelTopic(topicConst);
    }
    
    @Bean
    AllRtQuotationChannelTopic allRtQuotationChannelTopic(TopicConst topicConst)
    {
        return new AllRtQuotationChannelTopic(topicConst);
    }
}

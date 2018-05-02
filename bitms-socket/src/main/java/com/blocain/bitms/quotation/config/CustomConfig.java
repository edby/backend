package com.blocain.bitms.quotation.config;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;

import com.blocain.bitms.quotation.consts.SessionConst;
import com.blocain.bitms.quotation.consts.TopicConst;
import com.blocain.bitms.quotation.listener.*;
import com.blocain.bitms.quotation.topic.*;
import com.blocain.bitms.quotation.websocket.QuotationWebSocketHandler;
import com.google.common.collect.Maps;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

/**
 * 动态注册交易对下的MQ消费服务 Introduce
 * <p>Title: CustomConfig</p>
 * <p>File：CustomConfig.java</p>
 * <p>Description: CustomConfig</p>
 * <p>Copyright: Copyright (c) 2018/4/16</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@EnableWebSocket
public class CustomConfig implements BeanDefinitionRegistryPostProcessor
{
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
    {
    }
    
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException
    {
        // 通过此次动态生成币对对应的MQ消费端
        Map<String, String> quotations = Maps.newHashMap();
        // quotations.put("/ws/302", "quotation/133333333302.properties");
        // quotations.put("/ws/402", "quotation/144444444402.properties");
        // quotations.put("/ws/403", "quotation/144444444403.properties");
        quotations.put("/ws/leveragedSpot502", "quotation/155555555502.properties");
        for (Map.Entry<String, String> entry : quotations.entrySet())
        {
            // MQ config
            TopicConst topicConst = new TopicConst(entry.getValue());
            QuotationWebSocketHandler webSocketHandler = webSocketHandler(topicConst, new SessionConst());
            Map<MessageListener, Collection<Topic>> listenerTopics = Maps.newConcurrentMap();
            listenerTopics.put(kLine1MinMessageListener(webSocketHandler), Collections.singleton(kLine1MinChannelTopic(topicConst)));
            listenerTopics.put(kLine5MinMessageListener(webSocketHandler), Collections.singleton(kLine5MinChannelTopic(topicConst)));
            listenerTopics.put(kLine15MinMessageListener(webSocketHandler), Collections.singleton(kLine15MinChannelTopic(topicConst)));
            listenerTopics.put(kLine30MinMessageListener(webSocketHandler), Collections.singleton(kLine30MinChannelTopic(topicConst)));
            listenerTopics.put(kLineHourMessageListener(webSocketHandler), Collections.singleton(kLineHourChannelTopic(topicConst)));
            listenerTopics.put(kLineDayMessageListener(webSocketHandler), Collections.singleton(kLineDayChannelTopic(topicConst)));
            listenerTopics.put(kLineWeekMessageListener(webSocketHandler), Collections.singleton(kLineWeekChannelTopic(topicConst)));
            listenerTopics.put(kLineMonthMessageListener(webSocketHandler), Collections.singleton(kLineMonthChannelTopic(topicConst)));
            listenerTopics.put(deepPriceMessageListener(webSocketHandler), Collections.singleton(deepPriceChannelTopic(topicConst)));
            listenerTopics.put(rtQuotationMessageListener(webSocketHandler), Collections.singleton(rtQuotationChannelTopic(topicConst)));
            listenerTopics.put(realDealMessageListener(webSocketHandler), Collections.singleton(realDealChannelTopic(topicConst)));
            listenerTopics.put(allRtQuotationMessageListener(webSocketHandler), Collections.singleton(allRtQuotationChannelTopic(topicConst)));
            // add RedisMessageListenerContainer
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(RedisMessageListenerContainer.class);
            beanDefinitionBuilder.addPropertyValue("messageListeners", listenerTopics);
            beanDefinitionBuilder.addPropertyReference("connectionFactory", "jedisConnectionFactory");
            String redisMessageListenerContainerName = "redisMessageListenerContainer" + entry.getKey().hashCode();// 各个币对的服务名称不可重复
            registry.registerBeanDefinition(redisMessageListenerContainerName, beanDefinitionBuilder.getBeanDefinition());
            // add WebSocket
            beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(SocketConfig.class);
            beanDefinitionBuilder.addConstructorArgValue(entry.getKey());
            beanDefinitionBuilder.addConstructorArgValue(webSocketHandler);
            String webSocketName = "socketConfig"+ entry.getKey().hashCode();
            registry.registerBeanDefinition(webSocketName, beanDefinitionBuilder.getBeanDefinition());
        }
    }
    
    QuotationWebSocketHandler webSocketHandler(TopicConst topicConst, SessionConst sessionConst)
    { // 会话处理
        QuotationWebSocketHandler messageListener = new QuotationWebSocketHandler();
        messageListener.setTopicConst(topicConst);
        messageListener.setSessionConst(sessionConst);
        return messageListener;
    }
    
    KLine1MinMessageListener kLine1MinMessageListener(QuotationWebSocketHandler webSocketHandler)
    {
        KLine1MinMessageListener messageListener = new KLine1MinMessageListener();
        messageListener.setQuotationWebSocketHandler(webSocketHandler);
        return messageListener;
    }
    
    KLine5MinMessageListener kLine5MinMessageListener(QuotationWebSocketHandler webSocketHandler)
    {
        KLine5MinMessageListener messageListener = new KLine5MinMessageListener();
        messageListener.setQuotationWebSocketHandler(webSocketHandler);
        return messageListener;
    }
    
    KLine15MinMessageListener kLine15MinMessageListener(QuotationWebSocketHandler webSocketHandler)
    {
        KLine15MinMessageListener messageListener = new KLine15MinMessageListener();
        messageListener.setQuotationWebSocketHandler(webSocketHandler);
        return messageListener;
    }
    
    KLine30MinMessageListener kLine30MinMessageListener(QuotationWebSocketHandler webSocketHandler)
    {
        KLine30MinMessageListener messageListener = new KLine30MinMessageListener();
        messageListener.setQuotationWebSocketHandler(webSocketHandler);
        return messageListener;
    }
    
    KLineHourMessageListener kLineHourMessageListener(QuotationWebSocketHandler webSocketHandler)
    {
        KLineHourMessageListener messageListener = new KLineHourMessageListener();
        messageListener.setQuotationWebSocketHandler(webSocketHandler);
        return messageListener;
    }
    
    KLineDayMessageListener kLineDayMessageListener(QuotationWebSocketHandler webSocketHandler)
    {
        KLineDayMessageListener messageListener = new KLineDayMessageListener();
        messageListener.setQuotationWebSocketHandler(webSocketHandler);
        return messageListener;
    }
    
    KLineWeekMessageListener kLineWeekMessageListener(QuotationWebSocketHandler webSocketHandler)
    {
        KLineWeekMessageListener messageListener = new KLineWeekMessageListener();
        messageListener.setQuotationWebSocketHandler(webSocketHandler);
        return messageListener;
    }
    
    KLineMonthMessageListener kLineMonthMessageListener(QuotationWebSocketHandler webSocketHandler)
    {
        KLineMonthMessageListener messageListener = new KLineMonthMessageListener();
        messageListener.setQuotationWebSocketHandler(webSocketHandler);
        return messageListener;
    }
    
    DeepPriceMessageListener deepPriceMessageListener(QuotationWebSocketHandler webSocketHandler)
    {
        DeepPriceMessageListener messageListener = new DeepPriceMessageListener();
        messageListener.setQuotationWebSocketHandler(webSocketHandler);
        return messageListener;
    }
    
    RtQuotationMessageListener rtQuotationMessageListener(QuotationWebSocketHandler webSocketHandler)
    {
        RtQuotationMessageListener messageListener = new RtQuotationMessageListener();
        messageListener.setQuotationWebSocketHandler(webSocketHandler);
        return messageListener;
    }
    
    RealDealMessageListener realDealMessageListener(QuotationWebSocketHandler webSocketHandler)
    {
        RealDealMessageListener messageListener = new RealDealMessageListener();
        messageListener.setQuotationWebSocketHandler(webSocketHandler);
        return messageListener;
    }
    
    AllRtQuotationMessageListener allRtQuotationMessageListener(QuotationWebSocketHandler webSocketHandler)
    {
        AllRtQuotationMessageListener messageListener = new AllRtQuotationMessageListener();
        messageListener.setQuotationWebSocketHandler(webSocketHandler);
        return messageListener;
    }
    
    KLine1MinChannelTopic kLine1MinChannelTopic(TopicConst topicConst)
    {
        return new KLine1MinChannelTopic(topicConst);
    }
    
    KLine5MinChannelTopic kLine5MinChannelTopic(TopicConst topicConst)
    {
        return new KLine5MinChannelTopic(topicConst);
    }
    
    KLine15MinChannelTopic kLine15MinChannelTopic(TopicConst topicConst)
    {
        return new KLine15MinChannelTopic(topicConst);
    }
    
    KLine30MinChannelTopic kLine30MinChannelTopic(TopicConst topicConst)
    {
        return new KLine30MinChannelTopic(topicConst);
    }
    
    KLineHourChannelTopic kLineHourChannelTopic(TopicConst topicConst)
    {
        return new KLineHourChannelTopic(topicConst);
    }
    
    KLineDayChannelTopic kLineDayChannelTopic(TopicConst topicConst)
    {
        return new KLineDayChannelTopic(topicConst);
    }
    
    KLineWeekChannelTopic kLineWeekChannelTopic(TopicConst topicConst)
    {
        return new KLineWeekChannelTopic(topicConst);
    }
    
    KLineMonthChannelTopic kLineMonthChannelTopic(TopicConst topicConst)
    {
        return new KLineMonthChannelTopic(topicConst);
    }
    
    DeepPriceChannelTopic deepPriceChannelTopic(TopicConst topicConst)
    {
        return new DeepPriceChannelTopic(topicConst);
    }
    
    RtQuotationChannelTopic rtQuotationChannelTopic(TopicConst topicConst)
    {
        return new RtQuotationChannelTopic(topicConst);
    }
    
    RealDealChannelTopic realDealChannelTopic(TopicConst topicConst)
    {
        return new RealDealChannelTopic(topicConst);
    }
    
    AllRtQuotationChannelTopic allRtQuotationChannelTopic(TopicConst topicConst)
    {
        return new AllRtQuotationChannelTopic(topicConst);
    }
}

package com.blocain.bitms.quotation.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.quotation.model.SocketMessage;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.quotation.consts.SessionConst;
import com.blocain.bitms.quotation.consts.TopicConst;

/**
 * QuotationWebSocketHandler Introduce
 * <p>File：QuotationWebSocketHandler.java</p>
 * <p>Title: QuotationWebSocketHandler</p>
 * <p>Description: QuotationWebSocketHandler</p>
 * <p>Copyright: Copyright (c) 2017/7/5</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class QuotationWebSocketHandler extends BinaryWebSocketHandler
{
    public static final Logger logger = LoggerFactory.getLogger(QuotationWebSocketHandler.class);
    
    private SessionConst       sessionConst;
    
    private TopicConst         topicConst;
    
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception
    {
        SocketMessage socketMessage = getMessage(message.getPayload().toString());
        if (null == socketMessage) return;
        if (topicConst.TOPIC_KLINE_MONTH.equals(socketMessage.getTopic()))
        {// 月线图
            cleanKLineSession(session);
            sessionConst.getkLineMonthSessions().add(session);
            sessionConst.getkLineMonthSessions_tmp().add(session);
            RedisUtils.send(topicConst.TOPIC_EXCHANGEPAIR, topicConst.TOPIC_KLINE_MONTH);
        }
        if (topicConst.TOPIC_KLINE_WEEK.equals(socketMessage.getTopic()))
        {// 周线图
            cleanKLineSession(session);
            sessionConst.getkLineWeekSessions().add(session);
            sessionConst.getkLineWeekSessions_tmp().add(session);
            RedisUtils.send(topicConst.TOPIC_EXCHANGEPAIR, topicConst.TOPIC_KLINE_WEEK);
        }
        if (topicConst.TOPIC_KLINE_DAY.equals(socketMessage.getTopic()))
        {// 日线图
            cleanKLineSession(session);
            sessionConst.getkLineDaySessions().add(session);
            sessionConst.getkLineDaySessions_tmp().add(session);
            RedisUtils.send(topicConst.TOPIC_EXCHANGEPAIR, topicConst.TOPIC_KLINE_DAY);
        }
        if (topicConst.TOPIC_KLINE_HOUR.equals(socketMessage.getTopic()))
        {// 时线图
            cleanKLineSession(session);
            sessionConst.getkLineHourSessions().add(session);
            sessionConst.getkLineHourSessions_tmp().add(session);
            RedisUtils.send(topicConst.TOPIC_EXCHANGEPAIR, topicConst.TOPIC_KLINE_HOUR);
        }
        if (topicConst.TOPIC_KLINE_30M.equals(socketMessage.getTopic()))
        {// 30分钟线图
            cleanKLineSession(session);
            sessionConst.getkLine30MinSessions().add(session);
            sessionConst.getkLine30MinSessions_tmp().add(session);
            RedisUtils.send(topicConst.TOPIC_EXCHANGEPAIR, topicConst.TOPIC_KLINE_30M);
        }
        if (topicConst.TOPIC_KLINE_15M.equals(socketMessage.getTopic()))
        {// 15分钟线图
            cleanKLineSession(session);
            sessionConst.getkLine15MinSessions().add(session);
            sessionConst.getkLine15MinSessions_tmp().add(session);
            RedisUtils.send(topicConst.TOPIC_EXCHANGEPAIR, topicConst.TOPIC_KLINE_15M);
        }
        if (topicConst.TOPIC_KLINE_5M.equals(socketMessage.getTopic()))
        {// 5分钟线图
            cleanKLineSession(session);
            sessionConst.getkLine5MinSessions().add(session);
            sessionConst.getkLine5MinSessions_tmp().add(session);
            RedisUtils.send(topicConst.TOPIC_EXCHANGEPAIR, topicConst.TOPIC_KLINE_5M);
        }
        if (topicConst.TOPIC_KLINE_1M.equals(socketMessage.getTopic()))
        {// 1分钟线图
            cleanKLineSession(session);
            sessionConst.getkLine1MinSessions().add(session);
            sessionConst.getkLine1MinSessions_tmp().remove(session);
            sessionConst.getkLine1MinSessions_tmp().add(session);
            RedisUtils.send(topicConst.TOPIC_EXCHANGEPAIR, topicConst.TOPIC_KLINE_1M);
        }
        if (topicConst.TOPIC_ENTRUST_DEEPPRICE.equals(socketMessage.getTopic()))
        {// 委托深度行情
            cleanDeepPriceSession_tmp(session);
            sessionConst.getDeepPriceSessions_tmp().add(session);
            RedisUtils.send(topicConst.TOPIC_EXCHANGEPAIR, topicConst.TOPIC_ENTRUST_DEEPPRICE);
        }
        if (topicConst.TOPIC_REALDEAL_TRANSACTION.equals(socketMessage.getTopic()))
        {// 成交流水
            cleanRealDealSession_tmp(session);
            sessionConst.getRealDealSessions_tmp().add(session);
            RedisUtils.send(topicConst.TOPIC_EXCHANGEPAIR, topicConst.TOPIC_REALDEAL_TRANSACTION);
        }
        if (topicConst.TOPIC_RTQUOTATION_PRICE.equals(socketMessage.getTopic()))
        {// 撮合行情
            cleanRtQuotationSession_tmp(session);
            sessionConst.getRtQuotationSessions_tmp().add(session);
            RedisUtils.send(topicConst.TOPIC_EXCHANGEPAIR, topicConst.TOPIC_RTQUOTATION_PRICE);
        }
        if (topicConst.TOPIC_ALLRTQUOTATION.equals(socketMessage.getTopic()))
        {// 全行情
            cleanAllRtQuotationSession_tmp(session);
            sessionConst.getAllRtQuotationSessions_tmp().add(session);
            String allPushKey = new StringBuilder(topicConst.TOPIC_ALLRTQUOTATION).append(BitmsConst.DOPUSH).toString();
            RedisUtils.send(allPushKey, topicConst.TOPIC_ALLRTQUOTATION);
        }
    }
    
    // 连接建立后处理MQ接收线程
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception
    {
        sessionConst.getkLine1MinSessions().add(session);// 默认加入1分钟订阅
        sessionConst.getDeepPriceSessions().add(session); // 默认深度0买入 行情
        sessionConst.getRealDealSessions().add(session); // 交易流水
        sessionConst.getRtQuotationSessions().add(session);// 行情展示
        sessionConst.getAllRtQuotationSessions().add(session);// 全行情展示
        sessionConst.getkLine1MinSessions_tmp().add(session);// 默认加入1分钟订阅
        sessionConst.getDeepPriceSessions_tmp().add(session); // 默认深度0买入 行情
        sessionConst.getRealDealSessions_tmp().add(session); // 交易流水
        sessionConst.getRtQuotationSessions_tmp().add(session);// 行情展示
        sessionConst.getAllRtQuotationSessions_tmp().add(session);// 全行情展示
        RedisUtils.send(topicConst.TOPIC_EXCHANGEPAIR, topicConst.TOPIC_KLINE_1M);
        RedisUtils.send(topicConst.TOPIC_EXCHANGEPAIR, topicConst.TOPIC_ENTRUST_DEEPPRICE);
        RedisUtils.send(topicConst.TOPIC_EXCHANGEPAIR, topicConst.TOPIC_REALDEAL_TRANSACTION);
        RedisUtils.send(topicConst.TOPIC_EXCHANGEPAIR, topicConst.TOPIC_RTQUOTATION_PRICE);
        String allPushKey = new StringBuilder(topicConst.TOPIC_ALLRTQUOTATION).append(BitmsConst.DOPUSH).toString();
        RedisUtils.send(allPushKey, topicConst.TOPIC_ALLRTQUOTATION);
    }
    
    // 抛出异常时处理
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception
    {
        logger.info("session:" + session.getRemoteAddress().getHostName());
        logger.info("exception:" + exception.getLocalizedMessage());
        if (session.isOpen())
        {
            session.close();
        }
        cleanSession(session);
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception
    {
        logger.info("session:" + session.getRemoteAddress().getHostName());
        logger.info("closeStatus getCode:" + closeStatus.getCode() + "closeStatus getReason:" + closeStatus.getReason());
        cleanSession(session);
        session.close();
    }
    
    // 从所有的监听会话列表中清除当前会话
    void cleanSession(WebSocketSession session)
    {
        cleanKLineSession(session);
        cleanDeepPriceSession(session);
        cleanRealDealSession(session);
        cleanRtQuotationSession(session);
        cleanAllRtQuotationSession(session);
        cleanKLineSession_tmp(session);
        cleanDeepPriceSession_tmp(session);
        cleanRealDealSession_tmp(session);
        cleanRtQuotationSession_tmp(session);
        cleanAllRtQuotationSession_tmp(session);
    }
    
    // 从K线的监听会话列表清除当前会话
    void cleanKLineSession(WebSocketSession session)
    {
        sessionConst.getkLineMonthSessions().remove(session);
        sessionConst.getkLineWeekSessions().remove(session);
        sessionConst.getkLineDaySessions().remove(session);
        sessionConst.getkLineHourSessions().remove(session);
        sessionConst.getkLine30MinSessions().remove(session);
        sessionConst.getkLine15MinSessions().remove(session);
        sessionConst.getkLine5MinSessions().remove(session);
        sessionConst.getkLine1MinSessions().remove(session);
    }
    
    // 从K线的监听会话列表清除当前会话
    void cleanKLineSession_tmp(WebSocketSession session)
    {
        sessionConst.getkLineMonthSessions_tmp().remove(session);
        sessionConst.getkLineWeekSessions_tmp().remove(session);
        sessionConst.getkLineDaySessions_tmp().remove(session);
        sessionConst.getkLineHourSessions_tmp().remove(session);
        sessionConst.getkLine30MinSessions_tmp().remove(session);
        sessionConst.getkLine15MinSessions_tmp().remove(session);
        sessionConst.getkLine5MinSessions_tmp().remove(session);
        sessionConst.getkLine1MinSessions_tmp().remove(session);
    }
    
    // 从深度行情的监听会话列表清除当前会话
    void cleanDeepPriceSession(WebSocketSession session)
    {
        sessionConst.getDeepPriceSessions().remove(session);
    }
    
    // 从成交流水的监听会话列表清除当前会话
    void cleanRealDealSession(WebSocketSession session)
    {
        sessionConst.getRealDealSessions().remove(session);
    }
    
    // 从撮合行情的监听会话列表清除当前会话
    void cleanRtQuotationSession(WebSocketSession session)
    {
        sessionConst.getRtQuotationSessions().remove(session);
    }
    
    // 从全行情的监听会话列表清除当前会话
    void cleanAllRtQuotationSession(WebSocketSession session)
    {
        sessionConst.getAllRtQuotationSessions().remove(session);
    }
    
    // 从深度行情的监听会话列表清除当前会话
    void cleanDeepPriceSession_tmp(WebSocketSession session)
    {
        sessionConst.getDeepPriceSessions_tmp().remove(session);
    }
    
    // 从成交流水的监听会话列表清除当前会话
    void cleanRealDealSession_tmp(WebSocketSession session)
    {
        sessionConst.getRealDealSessions_tmp().remove(session);
    }
    
    // 从撮合行情的监听会话列表清除当前会话
    void cleanRtQuotationSession_tmp(WebSocketSession session)
    {
        sessionConst.getRtQuotationSessions_tmp().remove(session);
    }
    
    // 从全行情的监听会话列表清除当前会话
    void cleanAllRtQuotationSession_tmp(WebSocketSession session)
    {
        sessionConst.getAllRtQuotationSessions_tmp().remove(session);
    }
    
    /**
     * 将文件消息转换成对象消息
     *
     * @param content
     * @return
     */
    protected SocketMessage getMessage(String content)
    {
        if (StringUtils.isBlank(content)) return null;
        SocketMessage message = null;
        try
        {
            message = JSON.parseObject(content, SocketMessage.class);
        }
        catch (RuntimeException e)
        {
            logger.error("message convent fail!");
        }
        return message;
    }
    
    public void setSessionConst(SessionConst sessionConst)
    {
        this.sessionConst = sessionConst;
    }
    
    public void setTopicConst(TopicConst topicConst)
    {
        this.topicConst = topicConst;
    }
    
    public SessionConst getSessionConst()
    {
        return sessionConst;
    }
}
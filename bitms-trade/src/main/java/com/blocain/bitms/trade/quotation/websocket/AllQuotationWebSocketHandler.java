package com.blocain.bitms.trade.quotation.websocket;

import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.trade.quotation.consts.AllQuotationTopicConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.quotation.model.SocketMessage;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.quotation.consts.SessionConst;

public class AllQuotationWebSocketHandler extends BinaryWebSocketHandler
{
    public static final Logger     logger = LoggerFactory.getLogger(AllQuotationWebSocketHandler.class);
    
    private SessionConst           sessionConst;
    
    private AllQuotationTopicConst topicConst;
    
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception
    {
        SocketMessage socketMessage = getMessage(message.getPayload().toString());
        if (null == socketMessage) return;
        if (topicConst.TOPIC_ALLRTQUOTATION.equals(socketMessage.getTopic()))
        {// 全行情
            cleanAllRtQuotationSession_tmp(session);
            sessionConst.getAllRtQuotationSessions_tmp().add(session);
            String allPushKey = new StringBuilder(topicConst.TOPIC_ALLRTQUOTATION).append(BitmsConst.DOPUSH).toString();
            RedisUtils.send(allPushKey,topicConst.TOPIC_ALLRTQUOTATION );
        }
    }
    
    // 连接建立后处理MQ接收线程
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception
    {
        sessionConst.getAllRtQuotationSessions().add(session);// 全行情展示
        sessionConst.getAllRtQuotationSessions_tmp().add(session);// 全行情展示
        String allPushKey = new StringBuilder(topicConst.TOPIC_ALLRTQUOTATION).append(BitmsConst.DOPUSH).toString();
        RedisUtils.send(allPushKey,topicConst.TOPIC_ALLRTQUOTATION );
    }
    
    // 抛出异常时处理
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception
    {
        if (session.isOpen())
        {
            session.close();
        }
        cleanSession(session);
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception
    {
        cleanSession(session);
        session.close();
    }
    
    // 从所有的监听会话列表中清除当前会话
    void cleanSession(WebSocketSession session)
    {
        cleanAllRtQuotationSession(session);
    }
    
    // 从全行情的监听会话列表清除当前会话
    void cleanAllRtQuotationSession(WebSocketSession session)
    {
        sessionConst.getAllRtQuotationSessions().remove(session);
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
    
    public void setTopicConst(AllQuotationTopicConst topicConst)
    {
        this.topicConst = topicConst;
    }
    
    public SessionConst getSessionConst()
    {
        return sessionConst;
    }
}
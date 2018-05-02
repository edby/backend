package com.blocain.bitms.trade.quotation.listener;

import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import com.blocain.bitms.tools.utils.ListUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.quotation.websocket.QuotationWebSocketHandler;

/**
 * 最新成交消息监听器
 * <p>File：RealDealMessageListener.java</p>
 * <p>Title: RealDealMessageListener</p>
 * <p>Description: RealDealMessageListener</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class RealDealMessageListener extends BaseMessageListener
{
    private QuotationWebSocketHandler quotationWebSocketHandler;
    
    public static final Logger        logger = LoggerFactory.getLogger(RealDealMessageListener.class);
    
    @Override
    public void onMessage(Message message, byte[] pattern)
    {
        String content = (String) serializer.deserialize(message.getBody());
        if (StringUtils.isNotBlank(content))
        {
            if (content.hashCode() != quotationWebSocketHandler.getSessionConst().getRealDealContent().hashCode())
            {
                // 如果数据更新，向全体广播
                if (ListUtils.isNotNull(quotationWebSocketHandler.getSessionConst().getRealDealSessions()))
                {
                    BinaryMessage binaryMessage = new BinaryMessage(content.getBytes());
                    for (WebSocketSession session : quotationWebSocketHandler.getSessionConst().getRealDealSessions())
                    {
                        try
                        {
                            if (session.isOpen()) session.sendMessage(binaryMessage);
                        }
                        catch (Exception e)
                        {
                            // logger.warn("消息发送失败！{}", e.getLocalizedMessage());
                        }
                    }
                }
                quotationWebSocketHandler.getSessionConst().setRealDealContent(content);
            }
            // 为主动拉数据的用户推送消息
            if (ListUtils.isNotNull(quotationWebSocketHandler.getSessionConst().getRealDealSessions_tmp()))
            {
                Set<WebSocketSession> tmp = quotationWebSocketHandler.getSessionConst().getRealDealSessions_tmp();
                BinaryMessage binaryMessage = new BinaryMessage(content.getBytes());
                Iterator<WebSocketSession> it = tmp.iterator();
                while (it.hasNext())
                {
                    try
                    {
                        WebSocketSession session = it.next();
                        if (session.isOpen()) session.sendMessage(binaryMessage);
                        it.remove();
                    }
                    catch (Exception e)
                    {
                        // logger.warn("消息发送失败！{}", e.getLocalizedMessage());
                    }
                }
            }
        }
    }
    
    public void setQuotationWebSocketHandler(QuotationWebSocketHandler quotationWebSocketHandler)
    {
        this.quotationWebSocketHandler = quotationWebSocketHandler;
    }
}

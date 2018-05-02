package com.blocain.bitms.quotation.listener;

import java.util.Iterator;
import java.util.Set;

import org.springframework.data.redis.connection.Message;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import com.blocain.bitms.tools.utils.ListUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.quotation.websocket.QuotationWebSocketHandler;

/**
 * 30分钟线消息监听器
 * <p>File：KLine30MinMessageListener.java</p>
 * <p>Title: KLine30MinMessageListener</p>
 * <p>Description: KLine30MinMessageListener</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class KLine30MinMessageListener extends BaseMessageListener
{
    private QuotationWebSocketHandler quotationWebSocketHandler;
    
    @Override
    public void onMessage(Message message, byte[] pattern)
    {
        String content = (String) serializer.deserialize(message.getBody());
        if (StringUtils.isNotBlank(content))
        {
            if (content.hashCode() != quotationWebSocketHandler.getSessionConst().getKline30minContent().hashCode())
            {
                // 如果数据更新，向全体广播
                if (ListUtils.isNotNull(quotationWebSocketHandler.getSessionConst().getkLine30MinSessions()))
                {
                    BinaryMessage binaryMessage = new BinaryMessage(content.getBytes());
                    for (WebSocketSession session : quotationWebSocketHandler.getSessionConst().getkLine30MinSessions())
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
                quotationWebSocketHandler.getSessionConst().setKline30minContent(content);
            }
            // 为主动拉数据的用户推送消息
            if (ListUtils.isNotNull(quotationWebSocketHandler.getSessionConst().getkLine30MinSessions_tmp()))
            {
                Set<WebSocketSession> tmp = quotationWebSocketHandler.getSessionConst().getkLine30MinSessions_tmp();
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

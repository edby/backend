package com.blocain.bitms.basic.webscoket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.quotation.model.SocketMessage;
import com.blocain.bitms.tools.utils.StringUtils;

/**
 * BitmsWebScoket Introduce
 * <p>File：QuotationWebscoket.java</p>
 * <p>Title: QuotationWebscoket</p>
 * <p>Description: QuotationWebscoket</p>
 * <p>Copyright: Copyright (c) 2017/7/5</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class BaseWebSocketHandler extends TextWebSocketHandler
{
    public static final Logger          logger                = LoggerFactory.getLogger(BaseWebSocketHandler.class);

    /**
     * 将文件消息转换成对象消息
     * @param content
     * @return
     */
    public SocketMessage getMessage(String content)
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
}
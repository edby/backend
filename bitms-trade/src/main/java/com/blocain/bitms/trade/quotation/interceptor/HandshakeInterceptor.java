package com.blocain.bitms.trade.quotation.interceptor;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * Webscoket消息拦截 Introduce
 * <p>File：HandshakeInterceptor.java</p>
 * <p>Title: HandshakeInterceptor</p>
 * <p>Description: HandshakeInterceptor</p>
 * <p>Copyright: Copyright (c) 2017/7/5</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor
{
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception
    {
        // 解决The extension [x-webkit-deflate-frame] is not supported问题
        if (request.getHeaders().containsKey("Sec-WebSocket-Extensions"))
        {
            request.getHeaders().set("Sec-WebSocket-Extensions", "permessage-deflate");
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }
    
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex)
    {
        super.afterHandshake(request, response, wsHandler, ex);
    }
}

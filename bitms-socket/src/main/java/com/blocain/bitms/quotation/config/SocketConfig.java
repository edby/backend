package com.blocain.bitms.quotation.config;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.blocain.bitms.quotation.interceptor.HandshakeInterceptor;
import com.blocain.bitms.quotation.websocket.QuotationWebSocketHandler;

/**
 * 动态启用WebSocket Introduce
 * <p>Title: SocketConfig</p>
 * <p>File：SocketConfig.java</p>
 * <p>Description: SocketConfig</p>
 * <p>Copyright: Copyright (c) 2018/4/16</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class SocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer
{
    private String                    socketPath;
    
    private HandshakeInterceptor      handshakeInterceptor = new HandshakeInterceptor();
    
    private QuotationWebSocketHandler quotationWebSocketHandler;
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
    {
        registry.addHandler(quotationWebSocketHandler, socketPath).setAllowedOrigins("*").addInterceptors(handshakeInterceptor);
    }
    
    public SocketConfig(String socketPath, QuotationWebSocketHandler quotationWebSocketHandler)
    {
        this.socketPath = socketPath;
        this.quotationWebSocketHandler = quotationWebSocketHandler;
    }
}

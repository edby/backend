package com.blocain.bitms.quotation.model;

import java.io.Serializable;

/**
 * WebSocket 消息对象
 * <p>File：SocketMessage.java</p>
 * <p>Title: SocketMessage</p>
 * <p>Description: SocketMessage</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class SocketMessage implements Serializable
{
    private static final long serialVersionUID = -9093804044957952623L;
    
    // 消息主题
    private String            topic;
    
    // 开始时间
    private Long              from;
    
    public String getTopic()
    {
        return topic;
    }
    
    public void setTopic(String topic)
    {
        this.topic = topic;
    }
    
    public Long getFrom()
    {
        return from;
    }
    
    public void setFrom(Long from)
    {
        this.from = from;
    }
}

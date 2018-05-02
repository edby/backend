package com.blocain.bitms.quotation.model;

import java.io.Serializable;

/**
 * BaseMessage Introduce
 * <p>File：BaseMessage.java</p>
 * <p>Title: BaseMessage</p>
 * <p>Description: BaseMessage</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class BaseMessage implements Serializable
{
    /**
     * 消息类型
     */
    private String msgType;
    
    public BaseMessage(String msgType)
    {
        this.msgType = msgType;
    }
    
    public String getMsgType()
    {
        return msgType;
    }
    
    public void setMsgType(String msgType)
    {
        this.msgType = msgType;
    }
}

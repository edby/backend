package com.blocain.bitms.apps.basic.beans;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * APP返回报文 Introduce
 * <p>Title: AppsMessage</p>
 * <p>File：AppsMessage.java</p>
 * <p>Description: AppsMessage</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class AppsMessage implements Serializable
{
    private static final long serialVersionUID = -9004996113801045379L;
    
    /**
     * 网关返回码
     */
    @JSONField(name = "code")
    private Integer           code;
    
    /**
     * 网关返回码描述
     */
    @JSONField(name = "message")
    private String            message;
    
    /**
     * 业务返回对象
     */
    @JSONField(name = "data")
    private Object            data;
    
    public Integer getCode()
    {
        return code;
    }
    
    public void setCode(Integer code)
    {
        this.code = code;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    public Object getData()
    {
        return data;
    }
    
    public void setData(Object data)
    {
        this.data = data;
    }
}

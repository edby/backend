package com.blocain.bitms.boss.common.enums;

import com.blocain.bitms.tools.bean.EnumDescribable;

/**
 * 消息类常用枚举
 * <p>File：MessageEnums.java </p>
 * <p>Title: MessageEnums </p>
 * <p>Description: MessageEnums </p>
 * <p>Copyright: Copyright (c) 15/9/15</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public enum MessageEnums implements EnumDescribable
{
    ERROR_TEMPLATE_NOTEXISTS(4001, "The message template does not exist"),//消息模版不存在
    ;
    public Integer code;
    
    public String  message;
    
    private MessageEnums(Integer code, String message)
    {
        this.code = code;
        this.message = message;
    }
    
    /**
     * 根据状态码获取状态码描述
     * @param code 状态码
     * @return String 状态码描述
     */
    public static String getMessage(Integer code)
    {
        String result = null;
        for (MessageEnums c : MessageEnums.values())
        {
            if (c.code.equals(code))
            {
                result = c.message;
                break;
            }
        }
        return result;
    }
    
    @Override
    public Integer getCode()
    {
        return this.code;
    }
    
    public void setCode(Integer code)
    {
        this.code = code;
    }
    
    @Override
    public String getMessage()
    {
        return this.message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
}

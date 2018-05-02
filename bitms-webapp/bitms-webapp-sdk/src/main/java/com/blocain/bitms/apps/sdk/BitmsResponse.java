package com.blocain.bitms.apps.sdk;

import java.io.Serializable;
import java.util.Map;

import com.blocain.bitms.apps.sdk.internal.mapping.ApiField;
import com.blocain.bitms.apps.sdk.internal.util.StringUtils;

/**
 * API基础响应信息。
 * 
 * @author fengsheng
 */
public class BitmsResponse implements Serializable
{
    private static final long   serialVersionUID = 5014379068811962022L;
    
    @ApiField("code")
    private String              code;
    
    @ApiField("msg")
    private String              msg;

    private String              body;

    private Map<String, String> params;

    /**
     * 废弃方法，请使用getCode替换
     *
     * @return
     */
    @Deprecated
    public String getErrorCode()
    {
        return this.getCode();
    }

    /**
     * 废弃方法，请使用setCode替换
     *
     * @param errorCode
     */
    @Deprecated
    public void setErrorCode(String errorCode)
    {
        this.setCode(errorCode);
    }

    /**
     * Getter method for property <tt>code</tt>.
     *
     * @return property value of code
     */
    public String getCode()
    {
        return code;
    }

    /**
     * Setter method for property <tt>code</tt>.
     *
     * @param code value to be assigned to property code
     */
    public void setCode(String code)
    {
        this.code = code;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }


    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public Map<String, String> getParams()
    {
        return params;
    }

    public void setParams(Map<String, String> params)
    {
        this.params = params;
    }

}

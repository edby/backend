package com.blocain.bitms.apps.basic.beans;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 基础请求对象 Introduce
 * <p>Title: RequestModel</p>
 * <p>File：RequestModel.java</p>
 * <p>Description: RequestModel</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class RequestModel implements Serializable
{
    private static final long serialVersionUID = -7234735503602496819L;
    
    @NotNull
    @JSONField(name = "format")
    private String            format;
    
    @NotNull
    @JSONField(name = "charset")
    private String            charset;
    
    @NotNull
    @JSONField(name = "sign")
    private String            sign;
    
    @NotNull
    @JSONField(name = "sign_type")
    private String            sign_type;
    
    @JSONField(name = "encrypt_key")
    private String            encrypt_key;
    
    @NotNull
    @JSONField(name = "timestamp")
    private String            timestamp;
    
    @NotNull
    @JSONField(name = "version")
    private String            version;
    
    @JSONField(name = "auth_token")
    private String            auth_token;
    
    @JSONField(name = "content")
    private String            content;
    
    public String getFormat()
    {
        return format;
    }
    
    public void setFormat(String format)
    {
        this.format = format;
    }
    
    public String getCharset()
    {
        return charset;
    }
    
    public void setCharset(String charset)
    {
        this.charset = charset;
    }
    
    public String getSign()
    {
        return sign;
    }
    
    public void setSign(String sign)
    {
        this.sign = sign;
    }
    
    public String getSign_type()
    {
        return sign_type;
    }
    
    public void setSign_type(String sign_type)
    {
        this.sign_type = sign_type;
    }
    
    public String getEncrypt_key()
    {
        return encrypt_key;
    }
    
    public void setEncrypt_key(String encrypt_key)
    {
        this.encrypt_key = encrypt_key;
    }
    
    public String getTimestamp()
    {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }
    
    public String getVersion()
    {
        return version;
    }
    
    public void setVersion(String version)
    {
        this.version = version;
    }
    
    public String getAuth_token()
    {
        return auth_token;
    }
    
    public void setAuth_token(String auth_token)
    {
        this.auth_token = auth_token;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
}

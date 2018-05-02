package com.blocain.bitms.tools.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Aliyun参数对象 Introduce
 * <p>File：AliyunModel.java</p>
 * <p>Title: AliyunModel</p>
 * <p>Description: AliyunModel</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "Aliyun参数对象")
public class AliyunModel
{
    @ApiModelProperty(value = "会话ID")
    private String csessionid;
    
    @ApiModelProperty(value = "口令")
    private String token;
    
    @ApiModelProperty(value = "密文")
    private String scene;
    
    @ApiModelProperty(value = "会话组")
    private String sig;
    
    @ApiModelProperty(hidden = true)
    private String csrf;
    
    public String getCsessionid()
    {
        return csessionid;
    }
    
    public void setCsessionid(String csessionid)
    {
        this.csessionid = csessionid;
    }
    
    public String getToken()
    {
        return token;
    }
    
    public void setToken(String token)
    {
        this.token = token;
    }
    
    public String getScene()
    {
        return scene;
    }
    
    public void setScene(String scene)
    {
        this.scene = scene;
    }
    
    public String getSig()
    {
        return sig;
    }
    
    public void setSig(String sig)
    {
        this.sig = sig;
    }
    
    public String getCsrf()
    {
        return csrf;
    }
    
    public void setCsrf(String csrf)
    {
        this.csrf = csrf;
    }
}

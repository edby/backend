package com.blocain.bitms.apps.account.beans;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 凭证参数 Introduce
 * <p>Title: AccessRequest</p>
 * <p>File：AccessRequest.java</p>
 * <p>Description: AccessRequest</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class AccessRequest implements Serializable
{
    private static final long serialVersionUID = 4065292846946232849L;
    
    @NotNull(message = "二次验证凭证不可为空")
    @JSONField(name = "access_token")
    private String            accessToken;
    
    public String getAccessToken()
    {
        return accessToken;
    }
    
    public void setAccessToken(String accessToken)
    {
        this.accessToken = accessToken;
    }
}

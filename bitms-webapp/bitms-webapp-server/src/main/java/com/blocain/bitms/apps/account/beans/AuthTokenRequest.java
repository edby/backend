package com.blocain.bitms.apps.account.beans;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

import javax.validation.constraints.NotNull;

/**
 * 用户授权基本对象 Introduce
 * <p>Title: AuthTokenRequest</p>
 * <p>File：AuthTokenRequest.java</p>
 * <p>Description: AuthTokenRequest</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class AuthTokenRequest implements Serializable
{
    private static final long serialVersionUID = -8079600253932601034L;

    @NotNull
    @JSONField(name = "auth_token")
    protected String            authToken;


    public String getAuthToken()
    {
        return authToken;
    }
    
    public void setAuthToken(String authToken)
    {
        this.authToken = authToken;
    }
}

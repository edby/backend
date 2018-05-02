package com.blocain.bitms.apps.account.beans;

import com.blocain.bitms.apps.sdk.BitmsObject;
import com.blocain.bitms.apps.sdk.internal.mapping.ApiField;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * 帐户请求对象 Introduce
 * <p>Title: LoginRequest</p>
 * <p>File：LoginRequest.java</p>
 * <p>Description: LoginRequest</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class LoginRequest  extends BitmsObject implements Serializable
{
    private static final long serialVersionUID = 6425331005714925843L;
    
    @NotNull
    @ApiField("username")
    private String            username;
    
    @NotNull
    @ApiField("password")
    private String            password;
    
    public String getUsername()
    {
        return username;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
}

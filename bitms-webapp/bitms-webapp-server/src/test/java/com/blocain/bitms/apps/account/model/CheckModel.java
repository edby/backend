package com.blocain.bitms.apps.account.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.blocain.bitms.apps.sdk.BitmsObject;
import com.blocain.bitms.apps.sdk.internal.mapping.ApiField;

import java.io.Serializable;

/**
 * Created by admin on 2018/3/22.
 */
public class CheckModel extends BitmsObject implements Serializable
{
    private static final long serialVersionUID = 6425331005714925843L;
    
    @ApiField("code")
    private String            code             = "971125";
    
    @ApiField("password")
    private String            password         = "Duanyi01";
    
    @JSONField(name = "access_token")
    private String            accessToken      = "pMGXqqNSWe+iOMN7GiJ4rGK6P7oEu2KQ";
    
    public String getCode()
    {
        return code;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public String getAccessToken()
    {
        return accessToken;
    }
    
    public void setAccessToken(String accessToken)
    {
        this.accessToken = accessToken;
    }
}

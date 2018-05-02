package com.blocain.bitms.apps.account.beans;

import java.io.Serializable;

/**
 * 会话对象 Introduce
 * <p>Title: SessionInfo</p>
 * <p>File：SessionInfo.java</p>
 * <p>Description: SessionInfo</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class SessionInfo implements Serializable
{
    private static final long serialVersionUID = -6500777089648373034L;
    
    // 用户ID
    protected Long            id;
    
    // 帐户名
    private String            accountName;
    
    // 国家
    private String            country;
    
    // 登陆时间
    private Long              loginDate;
    
    // 过期时间
    private Long              expireDate;
    
    // 手机
    private String            mobile;
    
    // 语言
    private String            lang;
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getAccountName()
    {
        return accountName;
    }
    
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    
    public String getCountry()
    {
        return country;
    }
    
    public void setCountry(String country)
    {
        this.country = country;
    }
    
    public Long getLoginDate()
    {
        return loginDate;
    }
    
    public void setLoginDate(Long loginDate)
    {
        this.loginDate = loginDate;
    }
    
    public Long getExpireDate()
    {
        return expireDate;
    }
    
    public void setExpireDate(Long expireDate)
    {
        this.expireDate = expireDate;
    }
    
    public String getMobile()
    {
        return mobile;
    }
    
    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }
    
    public String getLang()
    {
        return lang;
    }
    
    public void setLang(String lang)
    {
        this.lang = lang;
    }
}

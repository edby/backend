package com.blocain.bitms.apps.account.beans;

import java.io.Serializable;

/**
 * 帐户信息返回对象 Introduce
 * <p>Title: AccountResponse</p>
 * <p>File：AccountResponse.java</p>
 * <p>Description: AccountResponse</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class AccountResponse implements Serializable
{
    private static final long serialVersionUID = -6902326376398445788L;
    
    private Long              unid;
    
    private String            accountName;
    
    private String            email;
    
    private String            country;
    
    private String            mobNo;
    
    private String            lang;
    
    private Long              invitCode;
    
    // 身份领证
    private boolean           idState;
    
    // GA绑定
    private boolean           gaState;
    
    // 手机绑定
    private boolean           phState;
    
    // 自动借贷默认策略不开启0开启1
    private Integer           autoDebit;
    
    public Long getUnid()
    {
        return unid;
    }
    
    public void setUnid(Long unid)
    {
        this.unid = unid;
    }
    
    public String getAccountName()
    {
        return accountName;
    }
    
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    
    public String getEmail()
    {
        return email;
    }
    
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    public String getCountry()
    {
        return country;
    }
    
    public void setCountry(String country)
    {
        this.country = country;
    }
    
    public String getMobNo()
    {
        return mobNo;
    }
    
    public void setMobNo(String mobNo)
    {
        this.mobNo = mobNo;
    }
    
    public String getLang()
    {
        return lang;
    }
    
    public void setLang(String lang)
    {
        this.lang = lang;
    }
    
    public Long getInvitCode()
    {
        return invitCode;
    }
    
    public void setInvitCode(Long invitCode)
    {
        this.invitCode = invitCode;
    }
    
    public boolean isIdState()
    {
        return idState;
    }
    
    public void setIdState(boolean idState)
    {
        this.idState = idState;
    }
    
    public boolean isGaState()
    {
        return gaState;
    }
    
    public void setGaState(boolean gaState)
    {
        this.gaState = gaState;
    }
    
    public boolean isPhState()
    {
        return phState;
    }
    
    public void setPhState(boolean phState)
    {
        this.phState = phState;
    }
    
    public Integer getAutoDebit()
    {
        return autoDebit;
    }
    
    public void setAutoDebit(Integer autoDebit)
    {
        this.autoDebit = autoDebit;
    }
}

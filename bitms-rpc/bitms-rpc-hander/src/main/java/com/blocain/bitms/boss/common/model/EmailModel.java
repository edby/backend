package com.blocain.bitms.boss.common.model;

import java.io.Serializable;

/**
 * EmailModel 介绍
 * <p>File：EmailModel.java </p>
 * <p>Title: EmailModel </p>
 * <p>Description:EmailModel </p>
 * <p>Copyright: Copyright (c) May 2017/12/28 </p>
 * <p>Company: BloCain</p>
 *
 * @author playguy
 * @version 1.0
 */
public class EmailModel implements Serializable
{
    private static final long serialVersionUID = -610255992906369700L;
    
    private String            email;
    
    private String            invitCode;
    
    private String            randomKey;
    
    private String            requestIp;
    
    public EmailModel()
    {
    }
    
    public EmailModel(String email, String invitCode, String randomKey, String requestIp)
    {
        this.email = email;
        this.invitCode = invitCode;
        this.randomKey = randomKey;
        this.requestIp = requestIp;
    }
    
    public String getEmail()
    {
        return email;
    }
    
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    public String getInvitCode()
    {
        return invitCode;
    }
    
    public void setInvitCode(String invitCode)
    {
        this.invitCode = invitCode;
    }
    
    public String getRandomKey()
    {
        return randomKey;
    }
    
    public void setRandomKey(String randomKey)
    {
        this.randomKey = randomKey;
    }
    
    public String getRequestIp()
    {
        return requestIp;
    }
    
    public void setRequestIp(String requestIp)
    {
        this.requestIp = requestIp;
    }
}

package com.blocain.bitms.apps.account.beans;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;

/**
 * RegisterRequest Introduce
 * <p>Title: RegisterRequest</p>
 * <p>File：RegisterRequest.java</p>
 * <p>Description: RegisterRequest</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class RegisterRequest implements Serializable
{
    private static final long serialVersionUID = 4992592698125256677L;
    
    @Email
    @NotNull
    private String            email;
    
    public String getEmail()
    {
        return email;
    }
    
    public void setEmail(String email)
    {
        this.email = email;
    }
}

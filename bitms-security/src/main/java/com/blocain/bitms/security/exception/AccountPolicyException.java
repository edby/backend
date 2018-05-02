package com.blocain.bitms.security.exception;

import org.apache.shiro.authc.AuthenticationException;

public class AccountPolicyException extends AuthenticationException
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -8544606640817367976L;

    public AccountPolicyException()
    {
        super();
    }
    
    public AccountPolicyException(String message)
    {
        super(message);
    }
    
    public AccountPolicyException(Throwable cause)
    {
        super(cause);
    }
    
    public AccountPolicyException(String message, Throwable cause)
    {
        super(message, cause);
    }
}

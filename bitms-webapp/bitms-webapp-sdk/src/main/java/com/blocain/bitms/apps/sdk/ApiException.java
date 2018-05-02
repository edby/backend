/**
 * blocain.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.blocain.bitms.apps.sdk;

/**
 * 
 * @author playguy
 */
public class ApiException extends RuntimeException
{
    private static final long serialVersionUID = -238091758285157331L;
    
    private String            errCode;
    
    private String            errMsg;
    
    public ApiException()
    {
        super();
    }
    
    public ApiException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public ApiException(String message)
    {
        super(message);
    }
    
    public ApiException(Throwable cause)
    {
        super(cause);
    }
    
    public ApiException(String errCode, String errMsg)
    {
        super(errCode + ":" + errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
    
    public String getErrCode()
    {
        return this.errCode;
    }
    
    public String getErrMsg()
    {
        return this.errMsg;
    }
}
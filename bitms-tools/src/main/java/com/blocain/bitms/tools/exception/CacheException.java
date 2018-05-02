/*
 * @(#)CacheException.java 2014-2-27 下午9:05:01
 * Copyright 2014 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.exception;

/**
 * <p>File：CacheException.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2014 2014-2-27 下午9:05:01</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class CacheException extends RuntimeException
{
    private static final long serialVersionUID = -3224325662080509881L;
    
    public CacheException()
    {
        super();
    }
    
    public CacheException(String message)
    {
        super(message);
    }
    
    public CacheException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public CacheException(Throwable cause)
    {
        super(cause);
    }
}

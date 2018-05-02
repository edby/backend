/*
 * @(#)NoByteException.java 2014-2-27 下午8:56:07
 * Copyright 2014 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.exception;

/**
 * <p>File：NoByteException.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2014 2014-2-27 下午8:56:07</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class NoByteException extends Throwable
{
    private static final long serialVersionUID = -8601145084304417269L;
    
    public NoByteException()
    {
        super();
    }
    
    public NoByteException(String msg)
    {
        super(msg);
    }
    
    public NoByteException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
    
    public NoByteException(Throwable cause)
    {
        super(cause);
    }
}

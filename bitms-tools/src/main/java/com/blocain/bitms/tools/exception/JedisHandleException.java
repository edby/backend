/*
 * @(#)JedisHandleException.java 2015-4-24 上午11:01:33
 * Copyright 2015 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.exception;

/**
 * <p>File：JedisHandleException.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2015 2015-4-24 上午11:01:33</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class JedisHandleException extends RuntimeException
{
    //
    private static final long serialVersionUID = 8149861602527033152L;
    
    public JedisHandleException()
    {
        super();
    }
    
    public JedisHandleException(String s)
    {
        super(s);
    }
    
    public JedisHandleException(Throwable e)
    {
        super(e);
    }
}

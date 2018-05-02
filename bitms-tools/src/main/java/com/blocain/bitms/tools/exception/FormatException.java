/*
 * @(#)FormatException.java 2014-3-6 上午9:09:13
 * Copyright 2014 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.exception;

/**
 * <p>File：FormatException.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2014 2014-3-6 上午9:09:13</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class FormatException extends Exception
{
    private static final long serialVersionUID = 214744146962152342L;
    
    public FormatException()
    {
        super();
    }
    
    public FormatException(String message)
    {
        super(message);
    }
    
    public FormatException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public FormatException(Throwable cause)
    {
        super(cause);
    }
}

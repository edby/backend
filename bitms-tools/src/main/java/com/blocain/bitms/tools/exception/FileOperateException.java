/*
 * @(#)FileOperateException.java 2014-3-18 下午2:31:31
 * Copyright 2014 鲍建明, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.exception;

import com.blocain.bitms.tools.bean.EnumDescribable;

/**
 * <p>File：FileOperateException.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2014 2014-3-18 下午2:31:31</p>
 * <p>Company: BloCain</p>
 *
 * @author 鲍建明
 * @version 1.0
 */
public class FileOperateException extends BusinessException
{
    //
    private static final long serialVersionUID = 1L;
    
    /**
     * @param codeDescribable
     */
    public FileOperateException(EnumDescribable codeDescribable)
    {
        super(codeDescribable);
    }
    
    /**
     * @param code
     * @param message
     */
    public FileOperateException(Integer code, String message)
    {
        super(code, message);
    }
}

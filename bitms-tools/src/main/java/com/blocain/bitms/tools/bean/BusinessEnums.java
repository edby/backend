/*
 * @(#)ErrorCodeDescribable.java 2014年3月6日 下午3:06:33
 * Copyright 2014 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.bean;

import java.io.Serializable;

/**
 * 业务枚举
 * 
 * <p>File：BusinessEnums.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017-07-24 17：02</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface BusinessEnums extends Serializable
{
    /**
     * 获取异常代码
     * @return
     * @author Playguy
     */
    String getCode();
    
    /**
     * 获取异常代码描述
     * @return
     * @author Playguy
     */
    String getMessage();
}

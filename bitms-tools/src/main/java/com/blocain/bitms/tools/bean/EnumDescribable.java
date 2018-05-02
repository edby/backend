/*
 * @(#)ErrorCodeDescribable.java 2014年3月6日 下午3:06:33
 * Copyright 2014 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.bean;

import java.io.Serializable;

/**
 * 错误码描述接口
 * 
 * <p>File：ErrorCodeDescribable.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2014 2014年3月6日 下午3:06:33</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface EnumDescribable extends Serializable
{
    /**
     * 获取异常代码
     * @return
     * @author Playguy
     */
    Integer getCode();
    
    /**
     * 获取异常代码描述
     * @return
     * @author Playguy
     */
    String getMessage();
}

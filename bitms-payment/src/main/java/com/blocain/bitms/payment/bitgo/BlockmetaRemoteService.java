/*
 * @(#)BlockmetaRemote.java 2017年7月7日 下午2:37:18
 * Copyright 2017 施建波, Inc. All rights reserved. BloCain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.payment.bitgo;

import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.payment.bitgo.model.BitPayModel;

/**
 * <p>File：BlockmetaRemote.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月7日 下午2:37:18</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
public interface BlockmetaRemoteService
{
    /**
     * 单笔交易查询
     * @param transId   交易ID
     * @return
     * @author 施建波  2017年7月7日 下午1:55:49
     */
    public BitPayModel transQuery(String transId) throws BusinessException;
}

/*
 * @(#)BtcRemote.java 2017年7月7日 下午1:52:47
 * Copyright 2017 施建波, Inc. All rights reserved. BloCain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.payment.bitgo;

import java.util.List;

import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.payment.bitgo.model.BitPayModel;

/**
 * <p>File：BtcRemote.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月7日 下午1:52:47</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
public interface BtcRemoteService
{
    /**
     * 单笔交易查询
     * @param transId   交易ID
     * @return
     * @author 施建波  2017年7月7日 下午1:55:49
     */
    public BitPayModel transQuery(String transId) throws BusinessException;
    
    /**
     * 地址查询
     * @param address   钱包地址
     * @return
     * @throws BusinessException
     * @author 施建波  2017年7月7日 下午5:26:05
     */
    public BitPayModel addressQuery(String address) throws BusinessException;
    
    /**
     * 地址交易列表查询
     * @param address   钱包地址
     * @param page      页码
     * @param pagesize  分页大小
     * @return
     * @throws BusinessException
     * @author 施建波  2017年7月10日 上午8:54:59
     */
    public List<BitPayModel> addressTransQuery(String address, Integer page, Integer pagesize) throws BusinessException;
}

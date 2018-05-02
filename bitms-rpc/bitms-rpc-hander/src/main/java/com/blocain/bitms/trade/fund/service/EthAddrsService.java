/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.trade.fund.entity.EthAddrs;

import java.util.List;

/**
 * EthAddrs 服务接口
 * <p>File：EthAddrsService.java </p>
 * <p>Title: EthAddrsService </p>
 * <p>Description:EthAddrsService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface EthAddrsService extends GenericService<EthAddrs>{

    /**
     * 获取 ETH地址表中 跟用户充值相关联的 打款地址的ID
     * @param accountId
     * @return
     */
    List<Long> getChargeFromAddress(Long accountId);

    /**
     * 按ID查询并加行锁
     * @param ids
     * @return
     */
    List<EthAddrs> getByIdsForUpdate(Long ... ids);

    /**
     * 查询高度字段最小值记录findMinHeight
     * @return
     */
    List<EthAddrs> findMinHeight();
}

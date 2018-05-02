/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao ;
import com.blocain.bitms.trade.fund.entity.EthAddrs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * EthAddrs 持久层接口
 * <p>File：EthAddrsMapper.java </p>
 * <p>Title: EthAddrsMapper </p>
 * <p>Description:EthAddrsMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface EthAddrsMapper extends GenericMapper<EthAddrs>
{
    /**
     * 查询高度字段最小值记录findMaxHeight
     * @return
     */
    List<EthAddrs> findMinHeight();
    /**
     * 获取 ETH地址表中 跟用户充值相关联的 打款地址的ID
     * @param accountId
     * @return
     */
    List<Long> getChargeFromAddress(@Param("accountId") Long accountId);

    /**
     * 按ID获取 并加行锁
     * @param ids
     * @return
     */
    List<EthAddrs> getByIdsForUpdate(@Param("ids") Long[] ids);
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.trade.mapper;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.trade.trade.entity.RealDealVCoinMoney;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

/**
 * 成交表X 持久层接口
 * <p>File：RealDealVCoinMoneyMapper.java </p>
 * <p>Title: RealDealVCoinMoneyMapper </p>
 * <p>Description:RealDealVCoinMoneyMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface RealDealVCoinMoneyMapper extends GenericMapper<RealDealVCoinMoney>
{
    RealDealVCoinMoney queryRealDealVCoinMoney(@Param("tableName") String tableName);

    /**
     * 通过委托ID查询成交信息和费用
     * @param entity
     * @return
     */
    List<RealDealVCoinMoney> findRealDealListByEntrustId(RealDealVCoinMoney entity) ;

    /**
     * 获取最近一段时间内的成交变动账户
     * @param currentdate  查询的起始时间
     * @return
     */
    List<Long> getChangeAcctListByTimestamp(@Param("currentdate")Timestamp currentdate, @Param("tableName")String tableName);
}

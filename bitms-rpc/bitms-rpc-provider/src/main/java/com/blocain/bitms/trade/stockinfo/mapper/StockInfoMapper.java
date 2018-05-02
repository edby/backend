/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.stockinfo.mapper;

import com.blocain.bitms.orm.core.GenericMapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;

/**
 * 证券信息表 持久层接口
 * <p>File：StockInfoDao.java </p>
 * <p>Title: StockInfoDao </p>
 * <p>Description:StockInfoDao </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface StockInfoMapper extends GenericMapper<StockInfo>
{
    /**
     * 按id获取证券信息（多个id 逗号分割）
     * @param ids
     * @return
     */
    List<StockInfo> findListByIds(@Param("ids") String[] ids);

    /**
     * 查询所有交易对的计价信息
     * @return
     */
    List<StockInfo> findLIstByDistinctCaptalId();

    /**
     * 按类型查找
     * @param type
     * @return
     */
    List<StockInfo> findListByTypes(@Param("type") String[] type);

    /**
     * 按token地址查找
     * @param addr
     * @return
     */
    StockInfo findByContractAddr(@Param("addr")String addr);

    /**
     * 查找一个未激活的证券信息
     * @return
     */
    StockInfo findOneNotActive();

    /**
     * 查询所有类型证券
     * @return
     */
    List<StockInfo> findAll();
}

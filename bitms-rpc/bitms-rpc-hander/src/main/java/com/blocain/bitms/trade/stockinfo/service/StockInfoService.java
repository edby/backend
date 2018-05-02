/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.stockinfo.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 证券信息表 服务接口
 * <p>File：StockInfoService.java </p>
 * <p>Title: StockInfoService </p>
 * <p>Description:StockInfoService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface StockInfoService extends GenericService<StockInfo>
{
    /**
     * 按id获取证券信息（多个id 逗号分割）
     * @param ids
     * @return
     */
    List<StockInfo> findListByIds(@Param("ids") String ids) throws BusinessException;


    /**
     * 只取交易对的计价市场
     * @return
     * @throws BusinessException
     */
    List<StockInfo> findLIstByDistinctCaptalId() throws BusinessException;

    /**
     * 按类型查找
     * @param type
     * @return
     */
    List<StockInfo> findListByTypes(@Param("type") String ... type) throws BusinessException;

    /**
     * 按token地址查找
     * @param addr
     * @return
     */
    StockInfo findByContractAddr(String addr);

    /**
     * 查找一个未激活的证券信息
     * @return
     */
    StockInfo findOneNotActive();

    /**
     * 查询所有证券
     * @return
     */
    List<StockInfo> findAll();
}

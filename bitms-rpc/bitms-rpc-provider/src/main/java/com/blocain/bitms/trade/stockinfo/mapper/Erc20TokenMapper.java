/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.stockinfo.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao ;
import com.blocain.bitms.trade.stockinfo.entity.Erc20Token;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ERC20 TOKEN 持久层接口
 * <p>File：Erc20TokenMapper.java </p>
 * <p>Title: Erc20TokenMapper </p>
 * <p>Description:Erc20TokenMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface Erc20TokenMapper extends GenericMapper<Erc20Token>
{
    Erc20Token getErc20Token(@Param("contractAddr") String contractAddr, @Param("pair")String pair);

    Erc20Token selectByPrimaryKeyForUpdate(Long id);

    /**
     * 我的邀请
     * @param token
     * @return
     */
    List<Erc20Token> findListForAward(Erc20Token token);

    /**
     * 按条件查询
     * @param symbol
     * @param contractAddr
     * @return
     */
    List<Erc20Token> searchByKey(@Param("symbol") String symbol, @Param("contractAddr") String contractAddr);

    /**
     * 查询高度字段最小值记录findMaxHeight
     * @return
     */
    List<Erc20Token> findMinHeight();
}

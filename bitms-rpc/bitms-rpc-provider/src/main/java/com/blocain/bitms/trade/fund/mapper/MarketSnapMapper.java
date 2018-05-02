/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao ;
import com.blocain.bitms.trade.fund.entity.MarketSnap;
import org.apache.ibatis.annotations.Param;

/**
 * 行情快照表 持久层接口
 * <p>File：MarketSnapMapper.java </p>
 * <p>Title: MarketSnapMapper </p>
 * <p>Description:MarketSnapMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface MarketSnapMapper extends GenericMapper<MarketSnap>
{
    MarketSnap selectLastOne(@Param("pairStockinfoId") Long pairStockinfoId);
}

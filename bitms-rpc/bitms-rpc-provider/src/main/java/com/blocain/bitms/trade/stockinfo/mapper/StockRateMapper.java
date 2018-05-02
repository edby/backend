/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.stockinfo.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.trade.stockinfo.entity.StockRate;

/**
 * 证券费率表 持久层接口
 * <p>File：StockRateDao.java </p>
 * <p>Title: StockRateDao </p>
 * <p>Description:StockRateDao </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface StockRateMapper extends GenericMapper<StockRate>
{
}

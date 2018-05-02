/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.trade.fund.entity.MarketSnap;
/**
 * 行情快照表 服务接口
 * <p>File：MarketSnapService.java </p>
 * <p>Title: MarketSnapService </p>
 * <p>Description:MarketSnapService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface MarketSnapService extends GenericService<MarketSnap>{

    MarketSnap selectLastOne(Long pairStockinfoId);
}

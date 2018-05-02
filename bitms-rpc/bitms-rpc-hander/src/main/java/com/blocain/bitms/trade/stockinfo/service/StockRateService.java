/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.stockinfo.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.trade.stockinfo.entity.StockRate;

/**
 * 证券费率表 服务接口
 * <p>File：StockRateService.java </p>
 * <p>Title: StockRateService </p>
 * <p>Description:StockRateService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface StockRateService extends GenericService<StockRate>
{
    /**
     *  通过行情修改提现费率
     */
    void fiexWithdrawFeeRateFromQuotation();
}

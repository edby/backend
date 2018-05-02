/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.settlement.service;

import com.blocain.bitms.tools.exception.BusinessException;

import java.math.BigDecimal;

/**
 * 清算结算核心业务处理
 * <p>File：ClearService.java</p>
 * <p>Title: ClearService</p>
 * <p>Description:ClearService</p>
 * <p>Copyright: Copyright (c) 2017年8月17日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
public interface ClearService
{

    /**
     * 清算结算核心业务处理入口
     */
    void clear(Long exchangePairMoney,Long exchangePairVCoin, BigDecimal clearPrice) throws BusinessException;

}

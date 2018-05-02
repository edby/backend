/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.risk.service;

import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;

import java.math.BigDecimal;

/**
 * 风控统一服务接口
 * <p>File：RiskService.java </p>
 * <p>Title: RiskService </p>
 * <p>Description:RiskService </p>
 * <p>Copyright: Copyright 2017-11-22</p>
 * <p>Company: BloCain</p>
 * @author zhangchunxi
 * @version 1.0
 */
public interface RiskService
{
    /**
     * 委托下单统一服务
     * @param stockInfo 证券信息
     * @param accountId 账户ID
     * @param entrustDirect 委托方向
     * @param entrustPrice 委托价格
     * @param entrustType 委托类型
     * @param exchangePairVCoin 数字货币
     * @param exchangePairMoney 法定货币
     * @author zhangchunxi 2017-11-22 10:57:20
     * @throws BusinessException
     */
    void entrustRisk(StockInfo stockInfo, Long accountId, String entrustDirect, BigDecimal entrustPrice, String entrustType,
            Long exchangePairVCoin, Long exchangePairMoney) throws BusinessException;
}

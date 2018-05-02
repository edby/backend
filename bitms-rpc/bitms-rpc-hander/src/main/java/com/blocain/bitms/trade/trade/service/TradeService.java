/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.trade.service;

import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.trade.model.EntrustModel;

/**
 * 交易类统一服务接口
 * <p>File：TradeServices.java </p>
 * <p>Title: TradeService </p>
 * <p>Description:TradeService </p>
 * <p>Copyright: Copyright (c) May 26, 2017</p>
 * <p>Company: BloCain</p>
 * @author sunbiao
 * @version 1.0
 */
public interface TradeService
{
    /**
     * 委托服务
     * @param entrustModel    entrustModel
     * @return 委托ID （目前只有撮合委托会返回，其它返回为0）
     * @throws BusinessException
     * @author sunbiao  2017年7月10日 上午10:38:51
     */
    Long entrust(EntrustModel entrustModel) throws BusinessException;
    
    /**
     * 撮合交易委托撤单服务
     * @param entrustModel    entrustModel
     * @return
     * @throws BusinessException
     * @author zcx 2017-09-20 13:28:51
     */
    int entrustWithdrawX(EntrustModel entrustModel) throws BusinessException;
    
    /**
     * 内部委托和成交
     * @param entrustModel    entrustModel
     * @return
     * @throws BusinessException
     * @author zcx  2017-10-11
     */
    void innerEntrustAndRealDeal(EntrustModel entrustModel, Long superAccountId) throws BusinessException;
}

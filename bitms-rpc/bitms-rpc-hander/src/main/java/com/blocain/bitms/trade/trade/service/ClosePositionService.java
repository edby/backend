/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.trade.service;

import com.blocain.bitms.monitor.entity.MonitorMargin;
import com.blocain.bitms.tools.exception.BusinessException;

/**
 * 强制平仓业务处理
 * <p>File：ClosePositionService.java</p>
 * <p>Title: ClosePositionService</p>
 * <p>Description:ClosePositionService</p>
 * <p>Copyright: Copyright (c) 2017年8月17日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
public interface ClosePositionService
{

    /**
     * 自动平仓
     * @return
     */
    void autoClosePosition() throws BusinessException;

    /**
     * 强制平仓
     * @param accountIds 账户ID 逗号分割
     * @param stockinfoId 数字货币证券ID
     * @param relatedStockinfoId 查询余额关联stockinfoId 法定货币证券ID
     * @return
     */
    void doClosePositionSelect(String accountIds, Long stockinfoId, Long relatedStockinfoId, MonitorMargin monitorMargin) throws Exception;

}

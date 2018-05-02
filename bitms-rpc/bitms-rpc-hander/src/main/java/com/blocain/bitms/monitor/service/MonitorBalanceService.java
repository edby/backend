/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.entity.MonitorBalance;
import com.blocain.bitms.orm.core.GenericService;

import java.util.List;

public interface MonitorBalanceService extends GenericService<MonitorBalance>
{
    /**
     * 生成期初余额
     * @param list
     * @return
     */
    int createInitialBalance(List<MonitorBalance> list);
}

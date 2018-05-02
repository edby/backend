/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.entity.MonitorNegativeAsset;
import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;

public interface MonitorNegativeAssetService extends GenericService<MonitorNegativeAsset>
{
    // 查询负资产监控记录
    PaginateResult<MonitorNegativeAsset> findMonitorNegativeAssetList(Pagination pagin, MonitorNegativeAsset monitorNegativeAsset);
}

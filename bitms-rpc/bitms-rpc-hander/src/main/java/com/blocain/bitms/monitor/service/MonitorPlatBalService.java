/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.entity.MonitorPlatBal;
import com.blocain.bitms.orm.annotation.SlaveDataSource;
import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;

/**
 * MonitorPlatBal 服务接口
 * <p>File：MonitorPlatBalService.java </p>
 * <p>Title: MonitorPlatBalService </p>
 * <p>Description:MonitorPlatBalService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface MonitorPlatBalService extends GenericService<MonitorPlatBal>{

    PaginateResult<MonitorPlatBal> findMonitorPlatBalList(Pagination pagin, MonitorPlatBal entity);

    MonitorPlatBal findRiskInfo();
}

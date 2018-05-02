/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.entity.MonitorLogs;
import com.blocain.bitms.orm.annotation.SlaveDataSource;
import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;

/**
 * 监控日志表 服务接口
 * <p>File：MonitorLogsService.java </p>
 * <p>Title: MonitorLogsService </p>
 * <p>Description:MonitorLogsService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface MonitorLogsService extends GenericService<MonitorLogs>
{
    // 查询监控日志
    PaginateResult<MonitorLogs> findMonitorLogsList(Pagination pagin, MonitorLogs logs);
}

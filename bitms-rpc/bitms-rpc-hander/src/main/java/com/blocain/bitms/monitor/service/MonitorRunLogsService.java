/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.entity.MonitorRunLogs;
import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;

/**
 * 监控服务运行日志 服务接口
 * <p>File：MonitorRunLogsService.java </p>
 * <p>Title: MonitorRunLogsService </p>
 * <p>Description:MonitorRunLogsService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author fzk
 * @version 1.0
 */
public interface MonitorRunLogsService extends GenericService<MonitorRunLogs>{

    PaginateResult<MonitorRunLogs> findMonitorRunLogsList(Pagination pagin, MonitorRunLogs entity);
}

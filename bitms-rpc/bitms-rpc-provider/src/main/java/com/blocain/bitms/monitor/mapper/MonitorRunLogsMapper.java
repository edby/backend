/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.mapper;

import com.blocain.bitms.monitor.entity.MonitorRunLogs;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;

import java.util.List;

/**
 * 监控服务运行日志 持久层接口
 * <p>File：MonitorRunLogsMapper.java </p>
 * <p>Title: MonitorRunLogsMapper </p>
 * <p>Description:MonitorRunLogsMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author fzk
 * @version 1.0
 */
@MyBatisDao
public interface MonitorRunLogsMapper extends GenericMapper<MonitorRunLogs>
{

    List<MonitorRunLogs> findMonitorRunLogsList(MonitorRunLogs entity);
}

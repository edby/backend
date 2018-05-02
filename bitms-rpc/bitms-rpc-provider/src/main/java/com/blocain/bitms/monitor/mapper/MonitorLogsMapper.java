/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.mapper;

import com.blocain.bitms.orm.core.GenericMapper;

import java.util.List;

import com.blocain.bitms.monitor.entity.MonitorLogs;
import com.blocain.bitms.orm.annotation.MyBatisDao;

/**
 * 监控日志表 持久层接口
 * <p>File：MonitorLogsMapper.java </p>
 * <p>Title: MonitorLogsMapper </p>
 * <p>Description:MonitorLogsMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface MonitorLogsMapper extends GenericMapper<MonitorLogs>
{
    /**
     * 监控日志列表
     * @param monitorLogs
     * @return
     */
    List<MonitorLogs> findMonitorLogsList(MonitorLogs monitorLogs);
}

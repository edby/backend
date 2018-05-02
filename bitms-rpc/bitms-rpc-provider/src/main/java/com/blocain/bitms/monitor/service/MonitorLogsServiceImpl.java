/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.entity.MonitorLogs;
import com.blocain.bitms.monitor.mapper.MonitorLogsMapper;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;

import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 监控日志表 服务实现类
 * <p>File：MonitorLogsServiceImpl.java </p>
 * <p>Title: MonitorLogsServiceImpl </p>
 * <p>Description:MonitorLogsServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class MonitorLogsServiceImpl extends GenericServiceImpl<MonitorLogs> implements MonitorLogsService
{
    protected MonitorLogsMapper monitorLogsMapper;
    
    @Autowired
    public MonitorLogsServiceImpl(MonitorLogsMapper monitorLogsMapper)
    {
        super(monitorLogsMapper);
        this.monitorLogsMapper = monitorLogsMapper;
    }
    
    @Override
    public PaginateResult<MonitorLogs> findMonitorLogsList(Pagination pagin, MonitorLogs logs)
    {
        logs.setPagin(pagin);
        List<MonitorLogs> list = monitorLogsMapper.findMonitorLogsList(logs);
        return new PaginateResult<>(pagin, list);
    }
}

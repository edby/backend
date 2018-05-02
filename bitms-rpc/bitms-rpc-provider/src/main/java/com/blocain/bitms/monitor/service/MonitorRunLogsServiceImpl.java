/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;


import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocain.bitms.monitor.entity.MonitorRunLogs;
import com.blocain.bitms.monitor.mapper.MonitorRunLogsMapper;
import com.blocain.bitms.orm.core.GenericServiceImpl;

import java.util.List;

/**
 * 监控服务运行日志 服务实现类
 * <p>File：MonitorRunLogsServiceImpl.java </p>
 * <p>Title: MonitorRunLogsServiceImpl </p>
 * <p>Description:MonitorRunLogsServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author fzk
 * @version 1.0
 */
@Service
public class MonitorRunLogsServiceImpl extends GenericServiceImpl<MonitorRunLogs> implements MonitorRunLogsService
{

    protected MonitorRunLogsMapper monitorRunLogsMapper;

    @Autowired
    public MonitorRunLogsServiceImpl(MonitorRunLogsMapper monitorRunLogsMapper)
    {
        super(monitorRunLogsMapper);
        this.monitorRunLogsMapper = monitorRunLogsMapper;
    }

    @Override
    public PaginateResult<MonitorRunLogs> findMonitorRunLogsList(Pagination pagin, MonitorRunLogs entity) {
        entity.setPagin(pagin);
        List<MonitorRunLogs> list = monitorRunLogsMapper.findMonitorRunLogsList(entity);
        return new PaginateResult<>(pagin, list);
    }
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;


import com.blocain.bitms.monitor.entity.MonitorPlatBal;
import com.blocain.bitms.monitor.mapper.MonitorPlatBalMapper;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * MonitorPlatBal 服务实现类
 * <p>File：MonitorPlatBalServiceImpl.java </p>
 * <p>Title: MonitorPlatBalServiceImpl </p>
 * <p>Description:MonitorPlatBalServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class MonitorPlatBalServiceImpl extends GenericServiceImpl<MonitorPlatBal> implements MonitorPlatBalService
{

    protected MonitorPlatBalMapper monitorPlatBalMapper;

    @Autowired
    public MonitorPlatBalServiceImpl(MonitorPlatBalMapper monitorPlatBalMapper)
    {
        super(monitorPlatBalMapper);
        this.monitorPlatBalMapper = monitorPlatBalMapper;
    }

    @Override
    public PaginateResult<MonitorPlatBal> findMonitorPlatBalList(Pagination pagin, MonitorPlatBal entity) {
        entity.setPagin(pagin);
        List<MonitorPlatBal> list = monitorPlatBalMapper.findMonitorPlatBalList(entity);
        return new PaginateResult<>(pagin, list);
    }

    @Override
    public MonitorPlatBal findRiskInfo() {
        return monitorPlatBalMapper.findRiskInfo();
    }
}

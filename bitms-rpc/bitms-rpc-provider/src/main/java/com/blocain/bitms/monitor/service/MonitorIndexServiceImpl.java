/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocain.bitms.monitor.entity.MonitorIndex;
import com.blocain.bitms.monitor.mapper.MonitorIndexMapper;
import com.blocain.bitms.orm.core.GenericServiceImpl;

/**
 * 监控指标表 服务实现类
 * <p>File：MonitorIndexServiceImpl.java </p>
 * <p>Title: MonitorIndexServiceImpl </p>
 * <p>Description:MonitorIndexServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class MonitorIndexServiceImpl extends GenericServiceImpl<MonitorIndex> implements MonitorIndexService
{

    protected MonitorIndexMapper monitorIndexMapper;

    @Autowired
    public MonitorIndexServiceImpl(MonitorIndexMapper monitorIndexMapper)
    {
        super(monitorIndexMapper);
        this.monitorIndexMapper = monitorIndexMapper;
    }
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocain.bitms.monitor.entity.MonitorBlockNum;
import com.blocain.bitms.monitor.mapper.MonitorBlockNumMapper;
import com.blocain.bitms.orm.core.GenericServiceImpl;

/**
 * MonitorBlockNum 服务实现类
 * <p>File：MonitorBlockNumServiceImpl.java </p>
 * <p>Title: MonitorBlockNumServiceImpl </p>
 * <p>Description:MonitorBlockNumServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class MonitorBlockNumServiceImpl extends GenericServiceImpl<MonitorBlockNum> implements MonitorBlockNumService
{

    protected MonitorBlockNumMapper monitorBlockNumMapper;

    @Autowired
    public MonitorBlockNumServiceImpl(MonitorBlockNumMapper monitorBlockNumMapper)
    {
        super(monitorBlockNumMapper);
        this.monitorBlockNumMapper = monitorBlockNumMapper;
    }

    @Override
    public MonitorBlockNum findRiskInfo() {
        return monitorBlockNumMapper.findRiskInfo();
    }
}

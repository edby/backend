/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;


import com.blocain.bitms.monitor.entity.MonitorParam;
import com.blocain.bitms.monitor.mapper.MonitorParamMapper;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 监控参数表 服务实现类
 * <p>File：MonitorParamServiceImpl.java </p>
 * <p>Title: MonitorParamServiceImpl </p>
 * <p>Description:MonitorParamServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class MonitorParamServiceImpl extends GenericServiceImpl<MonitorParam> implements MonitorParamService
{

    protected MonitorParamMapper monitorParamMapper;

    @Autowired
    public MonitorParamServiceImpl(MonitorParamMapper monitorParamMapper)
    {
        super(monitorParamMapper);
        this.monitorParamMapper = monitorParamMapper;
    }


    @Override
    public List<MonitorParam> findRelatedList() {
        return monitorParamMapper.findRelatedList();
    }
}

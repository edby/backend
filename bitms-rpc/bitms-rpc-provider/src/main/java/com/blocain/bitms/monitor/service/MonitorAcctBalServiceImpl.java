/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.entity.MonitorAcctBal;
import com.blocain.bitms.monitor.mapper.MonitorAcctBalMapper;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 账户余额监控表 服务实现类
 * <p>File：MonitorAcctBalServiceImpl.java </p>
 * <p>Title: MonitorAcctBalServiceImpl </p>
 * <p>Description:MonitorAcctBalServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class MonitorAcctBalServiceImpl extends GenericServiceImpl<MonitorAcctBal> implements MonitorAcctBalService
{
    protected MonitorAcctBalMapper monitorAcctBalMapper;
    
    @Autowired
    public MonitorAcctBalServiceImpl(MonitorAcctBalMapper monitorAcctBalMapper)
    {
        //super(monitorAcctBalMapper);
        this.monitorAcctBalMapper = monitorAcctBalMapper;
    }
    
    @Override
    public PaginateResult<MonitorAcctBal> findMonitorAcctBalList(Pagination pagin, MonitorAcctBal monitorAcctBal)
    {
        monitorAcctBal.setPagin(pagin);
        List<MonitorAcctBal> list = monitorAcctBalMapper.findMonitorAcctBalList(monitorAcctBal);
        return new PaginateResult<>(pagin, list);
    }

}

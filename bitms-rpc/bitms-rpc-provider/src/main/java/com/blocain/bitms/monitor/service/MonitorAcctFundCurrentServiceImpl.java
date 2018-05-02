/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.entity.MonitorAcctFundCurrent;
import com.blocain.bitms.monitor.mapper.MonitorAcctFundCurrentMapper;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;

import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 资金流水监控表 服务实现类
 * <p>File：MonitorAcctFundCurrentServiceImpl.java </p>
 * <p>Title: MonitorAcctFundCurrentServiceImpl </p>
 * <p>Description:MonitorAcctFundCurrentServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class MonitorAcctFundCurrentServiceImpl extends GenericServiceImpl<MonitorAcctFundCurrent> implements MonitorAcctFundCurrentService
{
    protected MonitorAcctFundCurrentMapper monitorAcctFundCurrentMapper;
    
    @Autowired
    public MonitorAcctFundCurrentServiceImpl(MonitorAcctFundCurrentMapper monitorAcctFundCurrentMapper)
    {
        super(monitorAcctFundCurrentMapper);
        this.monitorAcctFundCurrentMapper = monitorAcctFundCurrentMapper;
    }
    
    @Override
    public PaginateResult<MonitorAcctFundCurrent> findMonitorAcctFundCurrentList(Pagination pagin, MonitorAcctFundCurrent monitorAcctFundCur)
    {
        monitorAcctFundCur.setPagin(pagin);
        List<MonitorAcctFundCurrent> list = monitorAcctFundCurrentMapper.findAcctFundCurList(monitorAcctFundCur);
        return new PaginateResult<>(pagin, list);
    }
}

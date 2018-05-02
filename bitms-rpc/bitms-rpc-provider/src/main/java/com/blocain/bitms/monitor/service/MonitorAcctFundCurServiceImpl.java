/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;


import com.blocain.bitms.monitor.entity.MonitorAcctFundCur;
import com.blocain.bitms.monitor.mapper.MonitorAcctFundCurMapper;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 账户资金流水监控表 服务实现类
 * <p>File：MonitorAcctFundCurServiceImpl.java </p>
 * <p>Title: MonitorAcctFundCurServiceImpl </p>
 * <p>Description:MonitorAcctFundCurServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class MonitorAcctFundCurServiceImpl extends GenericServiceImpl<MonitorAcctFundCur> implements MonitorAcctFundCurService
{

    protected MonitorAcctFundCurMapper monitorAcctFundCurMapper;

    @Autowired
    public MonitorAcctFundCurServiceImpl(MonitorAcctFundCurMapper monitorAcctFundCurMapper)
    {
        super(monitorAcctFundCurMapper);
        this.monitorAcctFundCurMapper = monitorAcctFundCurMapper;
    }

    @Override
    public PaginateResult<MonitorAcctFundCur> findAcctFundCurList(Pagination pagin, MonitorAcctFundCur entity) {
        entity.setPagin(pagin);
        List<MonitorAcctFundCur> list = monitorAcctFundCurMapper.findMonitorAcctFundCurList(entity);
        return new PaginateResult<>(pagin, list);
    }

    @Override
    public PaginateResult<MonitorAcctFundCur> findRelatedList(Pagination pagin, MonitorAcctFundCur entity) {
        entity.setPagin(pagin);
        List<MonitorAcctFundCur> list = monitorAcctFundCurMapper.findRelatedList(entity);
        return new PaginateResult<>(pagin, list);
    }

    @Override
    public MonitorAcctFundCur findRiskInfo() {
        return monitorAcctFundCurMapper.findRiskInfo();
    }
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.entity.MonitorBalance;
import com.blocain.bitms.monitor.mapper.MonitorBalanceMapper;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 账户余额表 服务实现类
 * <p>File：MonitorBalanceServiceImpl.java </p>
 * <p>Title: MonitorBalanceServiceImpl </p>
 * <p>Description:MonitorBalanceServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class MonitorBalanceServiceImpl extends GenericServiceImpl<MonitorBalance> implements MonitorBalanceService
{
    protected MonitorBalanceMapper monitorBalanceMapper;
    
    @Autowired
    public MonitorBalanceServiceImpl(MonitorBalanceMapper monitorBalanceMapper)
    {
        super(monitorBalanceMapper);
        this.monitorBalanceMapper = monitorBalanceMapper;
    }

    @Override
    public int createInitialBalance(List<MonitorBalance> list)
    {
        int iResult = 0;

        if(CollectionUtils.isEmpty(list)) return iResult;

        iResult = monitorBalanceMapper.insertBatch(list);

        return iResult;
    }
}

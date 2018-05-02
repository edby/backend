/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;

import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocain.bitms.monitor.entity.MonitorERC20Bal;
import com.blocain.bitms.monitor.mapper.MonitorERC20BalMapper;
import com.blocain.bitms.orm.core.GenericServiceImpl;

import java.util.List;

/**
 * MonitorERC20Bal 服务实现类
 * <p>File：MonitorERC20BalServiceImpl.java </p>
 * <p>Title: MonitorERC20BalServiceImpl </p>
 * <p>Description:MonitorERC20BalServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author fzk
 * @version 1.0
 */
@Service
public class MonitorERC20BalServiceImpl extends GenericServiceImpl<MonitorERC20Bal> implements MonitorERC20BalService
{
    protected MonitorERC20BalMapper monitorERC20BalMapper;
    
    @Autowired
    public MonitorERC20BalServiceImpl(MonitorERC20BalMapper monitorERC20BalMapper)
    {
        super(monitorERC20BalMapper);
        this.monitorERC20BalMapper = monitorERC20BalMapper;
    }
    
    @Override
    public PaginateResult<MonitorERC20Bal> findJoinList(Pagination pagin, MonitorERC20Bal entity)
    {
        entity.setPagin(pagin);
        List<MonitorERC20Bal> list = monitorERC20BalMapper.findJoinList(entity);
        return new PaginateResult<>(pagin, list);
    }

    @Override
    public MonitorERC20Bal findRiskInfo() {
        return monitorERC20BalMapper.findRiskInfo();
    }


}

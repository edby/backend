/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;


import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocain.bitms.monitor.entity.MonitorLimitParam;
import com.blocain.bitms.monitor.mapper.MonitorLimitParamMapper;
import com.blocain.bitms.orm.core.GenericServiceImpl;

import java.util.List;

/**
 * MonitorLimitParam 服务实现类
 * <p>File：MonitorLimitParamServiceImpl.java </p>
 * <p>Title: MonitorLimitParamServiceImpl </p>
 * <p>Description:MonitorLimitParamServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class MonitorLimitParamServiceImpl extends GenericServiceImpl<MonitorLimitParam> implements MonitorLimitParamService
{

    protected MonitorLimitParamMapper monitorLimitParamMapper;

    @Autowired
    public MonitorLimitParamServiceImpl(MonitorLimitParamMapper monitorLimitParamMapper)
    {
        super(monitorLimitParamMapper);
        this.monitorLimitParamMapper = monitorLimitParamMapper;
    }


//    @Override
//    public MonitorLimitParam findByIdxAndStock(Long idxid, Long stockinfoId) {
//        return monitorLimitParamMapper.findByIdxAndStock(idxid,stockinfoId);
//    }

    @Override
    public PaginateResult<MonitorLimitParam> findJoinList(Pagination pagin, MonitorLimitParam entity) {
        entity.setPagin(pagin);
        List<MonitorLimitParam> list = monitorLimitParamMapper.findJoinList(entity);
        return new PaginateResult<>(pagin, list);
    }
}

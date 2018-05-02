/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.entity.MonitorNegativeAsset;
import com.blocain.bitms.monitor.mapper.MonitorNegativeAssetMapper;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;

import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 账户负资产监控表 服务实现类
 * <p>File：MonitorNegativeAssetServiceImpl.java </p>
 * <p>Title: MonitorNegativeAssetServiceImpl </p>
 * <p>Description:MonitorNegativeAssetServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class MonitorNegativeAssetServiceImpl extends GenericServiceImpl<MonitorNegativeAsset> implements MonitorNegativeAssetService
{
    protected MonitorNegativeAssetMapper monitorNegativeAssetMapper;
    
    @Autowired
    public MonitorNegativeAssetServiceImpl(MonitorNegativeAssetMapper monitorNegativeAssetMapper)
    {
        super(monitorNegativeAssetMapper);
        this.monitorNegativeAssetMapper = monitorNegativeAssetMapper;
    }
    
    @Override
    public PaginateResult<MonitorNegativeAsset> findMonitorNegativeAssetList(Pagination pagin, MonitorNegativeAsset monitorNegativeAsset)
    {
        monitorNegativeAsset.setPagin(pagin);
        List<MonitorNegativeAsset> list = monitorNegativeAssetMapper.findMonitorNegativeAssetList(monitorNegativeAsset);
        return new PaginateResult<>(pagin, list);
    }
}

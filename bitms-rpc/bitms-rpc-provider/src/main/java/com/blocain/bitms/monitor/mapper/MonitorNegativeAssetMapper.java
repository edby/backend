/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.mapper;

import com.blocain.bitms.monitor.entity.MonitorNegativeAsset;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;

import java.util.List;

/**
 * 账户负资产监控表 持久层接口
 * <p>File：MonitorNegativeAssetMapper.java </p>
 * <p>Title: MonitorNegativeAssetMapper </p>
 * <p>Description:MonitorNegativeAssetMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface MonitorNegativeAssetMapper extends GenericMapper<MonitorNegativeAsset>
{
    /**
     * 负资产账户监控列表
     * @param monitorNegativeAsset
     * @return
     */
    List<MonitorNegativeAsset> findMonitorNegativeAssetList(MonitorNegativeAsset monitorNegativeAsset);

}

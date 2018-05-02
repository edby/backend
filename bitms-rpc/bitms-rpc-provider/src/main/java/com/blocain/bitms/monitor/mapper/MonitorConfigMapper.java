/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.mapper;

import com.blocain.bitms.monitor.entity.MonitorConfig;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;

import java.util.List;

/**
 * MonitorConfig 持久层接口
 * <p>File：MonitorConfigMapper.java </p>
 * <p>Title: MonitorConfigMapper </p>
 * <p>Description:MonitorConfigMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface MonitorConfigMapper extends GenericMapper<MonitorConfig>
{

    List<MonitorConfig> findRelatedList(String id);

    List<MonitorConfig> findJoinList(MonitorConfig entity);
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.mapper;

import com.blocain.bitms.monitor.entity.MonitorPlatBal;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;

import java.util.List;

/**
 * MonitorPlatBal 持久层接口
 * <p>File：MonitorPlatBalMapper.java </p>
 * <p>Title: MonitorPlatBalMapper </p>
 * <p>Description:MonitorPlatBalMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface MonitorPlatBalMapper extends GenericMapper<MonitorPlatBal>
{

    List<MonitorPlatBal> findMonitorPlatBalList(MonitorPlatBal entity);

    MonitorPlatBal findRiskInfo();
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.mapper;

import com.blocain.bitms.monitor.entity.MonitorParam;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;

import java.util.List;

/**
 * 监控参数表 持久层接口
 * <p>File：MonitorParamMapper.java </p>
 * <p>Title: MonitorParamMapper </p>
 * <p>Description:MonitorParamMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface MonitorParamMapper extends GenericMapper<MonitorParam>
{

    List<MonitorParam> findRelatedList();
}

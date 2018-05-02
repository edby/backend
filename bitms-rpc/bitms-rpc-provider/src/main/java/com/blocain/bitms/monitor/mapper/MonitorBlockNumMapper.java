/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.mapper;

import com.blocain.bitms.monitor.entity.MonitorBlockNum;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;

/**
 * MonitorBlockNum 持久层接口
 * <p>File：MonitorBlockNumMapper.java </p>
 * <p>Title: MonitorBlockNumMapper </p>
 * <p>Description:MonitorBlockNumMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface MonitorBlockNumMapper extends GenericMapper<MonitorBlockNum>
{

    MonitorBlockNum findRiskInfo();
}

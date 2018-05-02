/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.entity.MonitorBlockNum;
import com.blocain.bitms.orm.core.GenericService;

/**
 * MonitorBlockNum 服务接口
 * <p>File：MonitorBlockNumService.java </p>
 * <p>Title: MonitorBlockNumService </p>
 * <p>Description:MonitorBlockNumService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface MonitorBlockNumService extends GenericService<MonitorBlockNum>{

    MonitorBlockNum findRiskInfo();
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.entity.MonitorParam;
import com.blocain.bitms.orm.annotation.SlaveDataSource;
import com.blocain.bitms.orm.core.GenericService;

import java.util.List;

/**
 * 监控参数表 服务接口
 * <p>File：MonitorParamService.java </p>
 * <p>Title: MonitorParamService </p>
 * <p>Description:MonitorParamService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface MonitorParamService extends GenericService<MonitorParam>{

    List<MonitorParam> findRelatedList();

}

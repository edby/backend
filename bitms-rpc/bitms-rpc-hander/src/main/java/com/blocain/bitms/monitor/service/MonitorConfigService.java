/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.entity.MonitorConfig;
import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;

import java.util.List;

/**
 * MonitorConfig 服务接口
 * <p>File：MonitorConfigService.java </p>
 * <p>Title: MonitorConfigService </p>
 * <p>Description:MonitorConfigService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface MonitorConfigService extends GenericService<MonitorConfig>{


    List<MonitorConfig> buildConfigList();


    List<MonitorConfig> findRelatedList(String id);

    PaginateResult<MonitorConfig> findJoinList(Pagination pagin, MonitorConfig entity);
}

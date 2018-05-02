/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.entity.MonitorERC20Bal;
import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;

/**
 * MonitorERC20Bal 服务接口
 * <p>File：MonitorERC20BalService.java </p>
 * <p>Title: MonitorERC20BalService </p>
 * <p>Description:MonitorERC20BalService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface MonitorERC20BalService extends GenericService<MonitorERC20Bal>{

    PaginateResult<MonitorERC20Bal> findJoinList(Pagination pagin, MonitorERC20Bal entity);

    MonitorERC20Bal findRiskInfo();
}

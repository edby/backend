/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.entity.MonitorAcctBal;
import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;

/**
 * 账户余额监控表 服务接口
 * <p>File：MonitorAcctBalService.java </p>
 * <p>Title: MonitorAcctBalService </p>
 * <p>Description:MonitorAcctBalService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface MonitorAcctBalService extends GenericService<MonitorAcctBal>
{
    // 查询余额监控记录
    PaginateResult<MonitorAcctBal> findMonitorAcctBalList(Pagination pagin, MonitorAcctBal monitorAcctBal);
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.entity.MonitorAcctFundCurrent;
import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;

/**
 * 资金流水监控表 服务接口
 * <p>File：MonitorAcctFundCurrentService.java </p>
 * <p>Title: MonitorAcctFundCurrentService </p>
 * <p>Description:MonitorAcctFundCurrentService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface MonitorAcctFundCurrentService extends GenericService<MonitorAcctFundCurrent>
{
    // 查询资金监控记录
    PaginateResult<MonitorAcctFundCurrent> findMonitorAcctFundCurrentList(Pagination pagin, MonitorAcctFundCurrent monitorAcctFundCur);
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.entity.MonitorAcctFundCur;
import com.blocain.bitms.orm.annotation.SlaveDataSource;
import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;

/**
 * 账户资金流水监控表 服务接口
 * <p>File：MonitorAcctFundCurService.java </p>
 * <p>Title: MonitorAcctFundCurService </p>
 * <p>Description:MonitorAcctFundCurService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface MonitorAcctFundCurService extends GenericService<MonitorAcctFundCur>{

    PaginateResult<MonitorAcctFundCur> findAcctFundCurList(Pagination pagin, MonitorAcctFundCur entity);

    PaginateResult<MonitorAcctFundCur> findRelatedList(Pagination pagin, MonitorAcctFundCur entity);

    MonitorAcctFundCur findRiskInfo();
}

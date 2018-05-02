/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.mapper;

import com.blocain.bitms.monitor.entity.MonitorAcctFundCur;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;

import java.util.List;

/**
 * 账户资金流水监控表 持久层接口
 * <p>File：MonitorAcctFundCurMapper.java </p>
 * <p>Title: MonitorAcctFundCurMapper </p>
 * <p>Description:MonitorAcctFundCurMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface MonitorAcctFundCurMapper extends GenericMapper<MonitorAcctFundCur>
{

    List<MonitorAcctFundCur> findMonitorAcctFundCurList(MonitorAcctFundCur entity);

    List<MonitorAcctFundCur> findRelatedList(MonitorAcctFundCur entity);

    MonitorAcctFundCur findRiskInfo();
}

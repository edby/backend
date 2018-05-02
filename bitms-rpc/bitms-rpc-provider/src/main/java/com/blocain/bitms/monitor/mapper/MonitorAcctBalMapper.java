/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.mapper;

import com.blocain.bitms.monitor.entity.MonitorAcctBal;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;

import java.util.List;

/**
 * 账户余额监控表 持久层接口
 * <p>File：MonitorAcctBalMapper.java </p>
 * <p>Title: MonitorAcctBalMapper </p>
 * <p>Description:MonitorAcctBalMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface MonitorAcctBalMapper extends GenericMapper<MonitorAcctBal>
{
    /**
     * 余额监控列表
     * @param monitorAcctBal
     * @return
     */
    List<MonitorAcctBal> findMonitorAcctBalList(MonitorAcctBal monitorAcctBal);

}

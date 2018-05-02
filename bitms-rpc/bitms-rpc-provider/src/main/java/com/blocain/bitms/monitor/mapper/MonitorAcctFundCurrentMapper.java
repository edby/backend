/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.mapper;

import com.blocain.bitms.monitor.entity.MonitorAcctFundCurrent;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;

import java.util.List;

/**
 * 资金流水监控表 持久层接口
 * <p>File：MonitorAcctFundCurrentMapper.java </p>
 * <p>Title: MonitorAcctFundCurrentMapper </p>
 * <p>Description:MonitorAcctFundCurrentMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface MonitorAcctFundCurrentMapper extends GenericMapper<MonitorAcctFundCurrent>
{
    /**
     * 资金监控列表
     * @param monitorAcctFundCurrent
     * @return
     */
    List<MonitorAcctFundCurrent> findAcctFundCurList(MonitorAcctFundCurrent monitorAcctFundCurrent);

}

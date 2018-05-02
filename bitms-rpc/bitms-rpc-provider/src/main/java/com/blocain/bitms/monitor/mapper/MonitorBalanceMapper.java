/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.monitor.entity.MonitorBalance;
import com.blocain.bitms.orm.annotation.MyBatisDao;

/**
 * 账户余额表 持久层接口
 * <p>File：MonitorBalanceMapper.java </p>
 * <p>Title: MonitorBalanceMapper </p>
 * <p>Description:MonitorBalanceMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface MonitorBalanceMapper extends GenericMapper<MonitorBalance>
{

}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.mapper;

import com.blocain.bitms.monitor.entity.MonitorERC20Bal;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;

import java.util.List;

/**
 * MonitorERC20Bal 持久层接口
 * <p>File：MonitorERC20BalMapper.java </p>
 * <p>Title: MonitorERC20BalMapper </p>
 * <p>Description:MonitorERC20BalMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author fzk
 * @version 1.0
 */
@MyBatisDao
public interface MonitorERC20BalMapper extends GenericMapper<MonitorERC20Bal>
{

    List<MonitorERC20Bal> findJoinList(MonitorERC20Bal entity);

    MonitorERC20Bal findRiskInfo();
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.robot.mapper;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.trade.robot.entity.GridRobotConfig;

import java.util.List;

/**
 * GridRobotConfig 持久层接口
 * <p>File：GridRobotConfigMapper.java </p>
 * <p>Title: GridRobotConfigMapper </p>
 * <p>Description:GridRobotConfigMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface GridRobotConfigMapper extends GenericMapper<GridRobotConfig>
{

    List<GridRobotConfig> selectByAccountId(Long accountId);
}

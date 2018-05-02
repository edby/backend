/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.robot.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.trade.robot.entity.GridRobotConfig;
import com.blocain.bitms.trade.robot.entity.RobotModel;
import com.blocain.bitms.trade.robot.entity.RobotMultiInfo;

import java.util.List;

/**
 * GridRobotConfig 服务接口
 * <p>File：GridRobotConfigService.java </p>
 * <p>Title: GridRobotConfigService </p>
 * <p>Description:GridRobotConfigService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface GridRobotConfigService extends GenericService<GridRobotConfig>{

    List<GridRobotConfig> selectByAccountId(Long accountId);

}

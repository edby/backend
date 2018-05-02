/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.robot.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.fund.model.FundChangeModel;
import com.blocain.bitms.trade.robot.cache.RobotCache;
import com.blocain.bitms.trade.robot.entity.GridRobotConfig;
import com.blocain.bitms.trade.robot.entity.RobotModel;
import com.blocain.bitms.trade.robot.entity.RobotMultiInfo;
import com.blocain.bitms.trade.robot.mapper.GridRobotConfigMapper;
import com.blocain.bitms.trade.robot.thread.AutoTradeThread;

/**
 * GridRobotConfig 服务实现类
 * <p>File：GridRobotConfigServiceImpl.java </p>
 * <p>Title: GridRobotConfigServiceImpl </p>
 * <p>Description:GridRobotConfigServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class GridRobotConfigServiceImpl extends GenericServiceImpl<GridRobotConfig> implements GridRobotConfigService
{
    @Autowired(required = false)
    private GridRobotService        gridRobotService;
    
    protected GridRobotConfigMapper gridRobotConfigMapper;
    
    @Autowired
    public GridRobotConfigServiceImpl(GridRobotConfigMapper gridRobotConfigMapper)
    {
        super(gridRobotConfigMapper);
        this.gridRobotConfigMapper = gridRobotConfigMapper;
    }
    
    @Override
    public List<GridRobotConfig> selectByAccountId(Long accountId)
    {
        return gridRobotConfigMapper.selectByAccountId(accountId);
    }
    

    

    

}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.settlement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.settlement.entity.ClosePositionLog;
import com.blocain.bitms.trade.settlement.mapper.ClosePositionLogMapper;

/**
 * 平仓操作日志表 服务实现类
 * <p>File：ClosePositionLogServiceImpl.java </p>
 * <p>Title: ClosePositionLogServiceImpl </p>
 * <p>Description:ClosePositionLogServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class ClosePositionLogServiceImpl extends GenericServiceImpl<ClosePositionLog> implements ClosePositionLogService
{
    protected ClosePositionLogMapper closePositionLogMapper;
    
    @Autowired
    public ClosePositionLogServiceImpl(ClosePositionLogMapper closePositionLogMapper)
    {
        super(closePositionLogMapper);
        this.closePositionLogMapper = closePositionLogMapper;
    }
}

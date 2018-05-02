/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.settlement.mapper;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.trade.settlement.entity.ClosePositionLog;

/**
 * 平仓操作日志表 持久层接口
 * <p>File：ClosePositionLogMapper.java </p>
 * <p>Title: ClosePositionLogMapper </p>
 * <p>Description:ClosePositionLogMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface ClosePositionLogMapper extends GenericMapper<ClosePositionLog>
{

}

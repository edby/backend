/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.settlement.mapper;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.trade.settlement.entity.SettlementProcess;

/**
 * 清算交割流程表 持久层接口
 * <p>File：SettlementProcessMapper.java </p>
 * <p>Title: SettlementProcessMapper </p>
 * <p>Description:SettlementProcessMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface SettlementProcessMapper extends GenericMapper<SettlementProcess>
{

}

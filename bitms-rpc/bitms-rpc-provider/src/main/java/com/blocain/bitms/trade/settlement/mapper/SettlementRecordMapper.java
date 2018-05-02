/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.settlement.mapper;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.trade.settlement.entity.SettlementRecord;

/**
 * 交割结算记录表 持久层接口
 * <p>File：SettlementRecordMapper.java </p>
 * <p>Title: SettlementRecordMapper </p>
 * <p>Description:SettlementRecordMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface SettlementRecordMapper extends GenericMapper<SettlementRecord>
{

}

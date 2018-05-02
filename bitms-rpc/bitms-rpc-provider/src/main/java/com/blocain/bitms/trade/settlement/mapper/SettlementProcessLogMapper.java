/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.settlement.mapper;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.trade.settlement.entity.SettlementProcessLog;

import java.util.List;

/**
 * 清算交割流程记录表 持久层接口
 * <p>File：SettlementProcessLogMapper.java </p>
 * <p>Title: SettlementProcessLogMapper </p>
 * <p>Description:SettlementProcessLogMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface SettlementProcessLogMapper extends GenericMapper<SettlementProcessLog>
{

    /**
     * 查询当次操作的日志
     * @param settlementProcessLog
     * @return
     */
    List<SettlementProcessLog> findDoingLogList(SettlementProcessLog settlementProcessLog);
}

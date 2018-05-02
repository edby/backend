/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.settlement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.settlement.entity.SettlementRecord;
import com.blocain.bitms.trade.settlement.mapper.SettlementRecordMapper;

/**
 * 交割结算记录表 服务实现类
 * <p>File：SettlementRecordServiceImpl.java </p>
 * <p>Title: SettlementRecordServiceImpl </p>
 * <p>Description:SettlementRecordServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class SettlementRecordServiceImpl extends GenericServiceImpl<SettlementRecord> implements SettlementRecordService
{
    protected SettlementRecordMapper settlementRecordMapper;
    
    @Autowired
    public SettlementRecordServiceImpl(SettlementRecordMapper settlementRecordMapper)
    {
        super(settlementRecordMapper);
        this.settlementRecordMapper = settlementRecordMapper;
    }
}

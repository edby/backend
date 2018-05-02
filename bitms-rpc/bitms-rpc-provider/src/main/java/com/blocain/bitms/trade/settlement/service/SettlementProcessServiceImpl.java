/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.settlement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.settlement.entity.SettlementProcess;
import com.blocain.bitms.trade.settlement.mapper.SettlementProcessMapper;

/**
 * 清算交割流程表 服务实现类
 * <p>File：SettlementProcessServiceImpl.java </p>
 * <p>Title: SettlementProcessServiceImpl </p>
 * <p>Description:SettlementProcessServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class SettlementProcessServiceImpl extends GenericServiceImpl<SettlementProcess> implements SettlementProcessService
{
    protected SettlementProcessMapper settlementProcessMapper;
    
    @Autowired
    public SettlementProcessServiceImpl(SettlementProcessMapper settlementProcessMapper)
    {
        super(settlementProcessMapper);
        this.settlementProcessMapper = settlementProcessMapper;
    }
}

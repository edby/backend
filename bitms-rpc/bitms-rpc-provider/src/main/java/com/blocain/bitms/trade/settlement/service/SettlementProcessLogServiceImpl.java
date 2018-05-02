/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.settlement.service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.settlement.entity.SettlementProcessLog;
import com.blocain.bitms.trade.settlement.mapper.SettlementProcessLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 清算交割流程记录表 服务实现类
 * <p>File：SettlementProcessLogServiceImpl.java </p>
 * <p>Title: SettlementProcessLogServiceImpl </p>
 * <p>Description:SettlementProcessLogServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class SettlementProcessLogServiceImpl extends GenericServiceImpl<SettlementProcessLog> implements SettlementProcessLogService
{
    protected SettlementProcessLogMapper settlementProcessLogMapper;
    
    @Autowired
    public SettlementProcessLogServiceImpl(SettlementProcessLogMapper settlementProcessLogMapper)
    {
        super(settlementProcessLogMapper);
        this.settlementProcessLogMapper = settlementProcessLogMapper;
    }
    
    @Override
    public PaginateResult<SettlementProcessLog> findDoingLogList(Pagination pagin, SettlementProcessLog settlementProcessLog) throws BusinessException
    {
        settlementProcessLog.setPagin(pagin);
        List<SettlementProcessLog> list = settlementProcessLogMapper.findDoingLogList(settlementProcessLog);
        return new PaginateResult<>(pagin, list);
    }
}

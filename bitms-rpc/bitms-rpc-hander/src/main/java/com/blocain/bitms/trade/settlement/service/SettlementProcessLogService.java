/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.settlement.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.settlement.entity.SettlementProcessLog;

/**
 * 清算交割流程记录表 服务接口
 * <p>File：SettlementProcessLogService.java </p>
 * <p>Title: SettlementProcessLogService </p>
 * <p>Description:SettlementProcessLogService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface SettlementProcessLogService extends GenericService<SettlementProcessLog>{

    /**
     * 查询当次操作的日志
     * @param settlementProcessLog
     * @return
     */
    PaginateResult<SettlementProcessLog> findDoingLogList(Pagination pagin, SettlementProcessLog settlementProcessLog) throws BusinessException;
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.trade.service;

import com.blocain.bitms.orm.annotation.SlaveDataSource;
import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.trade.entity.RealDealVCoinMoney;

import java.sql.Timestamp;
import java.util.List;

/**
 * 成交表X 服务接口
 * <p>File：RealDealVCoinMoneyService.java </p>
 * <p>Title: RealDealVCoinMoneyService </p>
 * <p>Description:RealDealVCoinMoneyService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface RealDealVCoinMoneyService extends GenericService<RealDealVCoinMoney>{

    /**
     * 通过委托ID查询成交信息和费用
     * @param entity
     * @return
     */
    @SlaveDataSource()
    PaginateResult<RealDealVCoinMoney> findRealDealListByEntrustId(Pagination pagination,RealDealVCoinMoney entity) throws BusinessException;

    /**
     * 获取最近一段时间内的成交变动账户
     * @param currentdate 查询时间
     * @return
     */
    List<Long> getChangeAcctListByTimestamp(Timestamp currentdate, String tableName);
}

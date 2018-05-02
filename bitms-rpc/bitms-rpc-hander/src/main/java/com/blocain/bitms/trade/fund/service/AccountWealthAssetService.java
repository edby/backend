/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.annotation.SlaveDataSource;
import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.entity.AccountWealthAsset;
/**
 * 账户理财资产表 服务接口
 * <p>File：AccountWealthAssetService.java </p>
 * <p>Title: AccountWealthAssetService </p>
 * <p>Description:AccountWealthAssetService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface AccountWealthAssetService extends GenericService<AccountWealthAsset>{

    /**
     * 自动计息
     * @return
     */
    void autoAccountWealthAssetInterest() throws BusinessException;

    /**
     * 单个计息
     * @param record
     * @param exchangePairMoney
     * @throws BusinessException
     */
    void doInterestRate(AccountWealthAsset record,  Long exchangePairMoney) throws BusinessException;

    /**
     * 行锁
     * @param id
     * @return
     */
    AccountWealthAsset selectByPrimaryKeyForUpdate( Long id) throws BusinessException;

    /**
     * 支撑系统综合查询-钱包账户资产
     * @param entity
     * @return
     * @throws BusinessException
     */
    @SlaveDataSource()
    PaginateResult<AccountWealthAsset> selectAll(Pagination pagination, AccountWealthAsset entity) throws BusinessException;
}

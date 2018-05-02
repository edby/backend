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
import com.blocain.bitms.trade.fund.entity.AccountSpotAsset;

/**
 * 现货账户资产表 服务接口
 * <p>File：AccountSpotAssetService.java </p>
 * <p>Title: AccountSpotAssetService </p>
 * <p>Description:AccountSpotAssetService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface AccountSpotAssetService extends GenericService<AccountSpotAsset>
{

    /**
     * 支撑系统综合查询-钱包账户资产
     * @param entity
     * @return
     * @throws BusinessException
     */
    @SlaveDataSource()
    PaginateResult<AccountSpotAsset> selectAll(Pagination pagination, AccountSpotAsset entity) throws BusinessException;

    /**
     * 查询账户钱包资产 并加行锁
     * @param accountId
     * @param stockinfoId
     * @return
     */
    AccountSpotAsset selectForUpdate(Long accountId, Long stockinfoId, Long relatedStockinfoId) throws BusinessException;

}

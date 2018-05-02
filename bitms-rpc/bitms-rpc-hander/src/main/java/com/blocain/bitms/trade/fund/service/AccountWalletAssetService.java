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
import com.blocain.bitms.trade.fund.entity.AccountWalletAsset;

import java.math.BigDecimal;

/**
 * 钱包账户资产表 服务接口
 * <p>File：AccountWalletAssetService.java </p>
 * <p>Title: AccountWalletAssetService </p>
 * <p>Description:AccountWalletAssetService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface AccountWalletAssetService extends GenericService<AccountWalletAsset>
{
    /**
     * 支撑系统综合查询-钱包账户资产
     * @param entity
     * @return
     * @throws BusinessException
     */
    @SlaveDataSource()
    PaginateResult<AccountWalletAsset> selectAll(Pagination pagination, AccountWalletAsset entity) throws BusinessException;

    /**
     * tradeX 查询列表
     * @param entity
     * @return
     * @throws BusinessException
     */
    @SlaveDataSource()
    PaginateResult<AccountWalletAsset> tradeXFindList(Pagination pagination, AccountWalletAsset entity) throws BusinessException;

    /**
     * 查询账户钱包资产 并加行锁
     * @param accountId
     * @param stockinfoId
     * @return
     */
    AccountWalletAsset selectForUpdate(Long accountId, Long stockinfoId) throws BusinessException;

    /**
     *  获取平台某个币种的累计充值
     * @param stockinfoId
     * @return
     */
    BigDecimal getPlatSumCoinByStockInfoId(Long stockinfoId);

    /**
     *  币种的累计充值
     * @return
     */
    void autoCheckPlatSumCoin() throws BusinessException;

    /**
     *  检查平台某个币种的累计充值
     * @param stockinfoId
     * @return
     */
    boolean doCheckPlatSumCoinByStockInfoId(Long stockinfoId) throws BusinessException;

}

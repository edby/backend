/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.trade.fund.entity.AccountWalletAsset;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 钱包账户资产表 持久层接口
 * <p>File：AccountWalletAssetDao.java </p>
 * <p>Title: AccountWalletAssetDao </p>
 * <p>Description:AccountWalletAssetDao </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface AccountWalletAssetMapper extends GenericMapper<AccountWalletAsset>
{
    AccountWalletAsset findWalletAsset(AccountWalletAsset accountWalletAsset);
    
    /**
     * 查询所有数据列表
     * @return
     */
    List<AccountWalletAsset> selectAll(AccountWalletAsset accountWalletAsset);
    
    /**
     * TRADEX查询所有数据列表
     * @return
     */
    List<AccountWalletAsset> tradeXFindList(AccountWalletAsset accountWalletAsset);
    
    /**
     * 查询账户钱包资产 并加行锁
     * @param accountId
     * @param stockinfoId
     * @return
     */
    AccountWalletAsset selectForUpdate(@Param("accountId") Long accountId, @Param("stockinfoId") Long stockinfoId);

    /**
     *  获取全平台某币种的总数量
     * @param stockinfoId
     * @return
     */
    BigDecimal getPlatSumCoinByStockInfoId(@Param("stockinfoId") Long stockinfoId);
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.trade.fund.entity.AccountSpotAsset;
import org.apache.ibatis.annotations.Param;

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
public interface AccountSpotAssetMapper extends GenericMapper<AccountSpotAsset>
{
    AccountSpotAsset findWalletAsset(AccountSpotAsset accountSpotAsset);
    
    /**
     * 查询所有数据列表
     * @return
     */
    List<AccountSpotAsset> selectAll(AccountSpotAsset accountSpotAsset);

    /**
     * 查询账户钱包资产 并加行锁
     * @param accountId
     * @param stockinfoId
     * @return
     */
    AccountSpotAsset selectForUpdate(@Param("accountId") Long accountId, @Param("stockinfoId") Long stockinfoId, @Param("relatedStockinfoId") Long relatedStockinfoId );
}

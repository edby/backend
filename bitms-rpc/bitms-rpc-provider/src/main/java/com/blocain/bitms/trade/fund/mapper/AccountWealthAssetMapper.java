/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao ;
import com.blocain.bitms.trade.fund.entity.AccountWealthAsset;

import java.util.List;

/**
 * 账户理财资产表 持久层接口
 * <p>File：AccountWealthAssetMapper.java </p>
 * <p>Title: AccountWealthAssetMapper </p>
 * <p>Description:AccountWealthAssetMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface AccountWealthAssetMapper extends GenericMapper<AccountWealthAsset>
{
    AccountWealthAsset selectByPrimaryKeyForUpdate(Long id);

    List<AccountWealthAsset> findListForWealth(AccountWealthAsset accountWealthAsset);
}

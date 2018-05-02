/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.trade.fund.entity.AccountWealthDebitAsset;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;

/**
 * 账户理财负债资产表 持久层接口
 * <p>File：AccountWealthDebitAssetMapper.java </p>
 * <p>Title: AccountWealthDebitAssetMapper </p>
 * <p>Description:AccountWealthDebitAssetMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface AccountWealthDebitAssetMapper extends GenericMapper<AccountWealthDebitAsset>
{
    AccountWealthDebitAsset selectByPrimaryKeyForUpdate(Long id);
}

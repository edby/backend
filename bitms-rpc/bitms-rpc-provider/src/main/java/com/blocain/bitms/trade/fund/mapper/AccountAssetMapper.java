/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.core.GenericMapper;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.trade.fund.entity.AccountFundCurrent;
import com.blocain.bitms.trade.fund.model.AccountAssetModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 账户资产
 * <p>File：AccountAssetMapper.java</p>
 * <p>Title: AccountAssetMapper</p>
 * <p>Description:AccountAssetMapper</p>
 * <p>Copyright: Copyright (c) 2017年7月19日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@MyBatisDao
public interface AccountAssetMapper extends GenericMapper<AccountAssetModel>
{
    List<AccountAssetModel> findAssetList(@Param("accountAssetModel")AccountAssetModel accountAssetModel,@Param("tableNames") List<Map<String,Object>> tableNames);

    /**
     * 获取用户单个借款和对应的资产
     * @param accountAssetModel
     * @return
     */
    AccountAssetModel findAssetAndDebitForAccount(AccountAssetModel accountAssetModel);
}

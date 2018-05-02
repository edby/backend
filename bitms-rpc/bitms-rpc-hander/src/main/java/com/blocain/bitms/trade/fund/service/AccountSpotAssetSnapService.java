/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.trade.fund.entity.AccountSpotAssetSnap;
/**
 * 现货账户资产快照表 服务接口
 * <p>File：AccountSpotAssetSnapService.java </p>
 * <p>Title: AccountSpotAssetSnapService </p>
 * <p>Description:AccountSpotAssetSnapService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface AccountSpotAssetSnapService extends GenericService<AccountSpotAssetSnap>{

    Long deleteAll();

    Long insertSpotAsset();
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.trade.fund.entity.AccountWalletAssetSnap;
/**
 * 钱包账户资产快照表 服务接口
 * <p>File：AccountWalletAssetSnapService.java </p>
 * <p>Title: AccountWalletAssetSnapService </p>
 * <p>Description:AccountWalletAssetSnapService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface AccountWalletAssetSnapService extends GenericService<AccountWalletAssetSnap>{

    Long deleteAll();

    Long insertWalletAsset();
}

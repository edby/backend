/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.trade.fund.entity.AccountDebitAssetSnap;
/**
 * 账户借贷资产快照表 服务接口
 * <p>File：AccountDebitAssetSnapService.java </p>
 * <p>Title: AccountDebitAssetSnapService </p>
 * <p>Description:AccountDebitAssetSnapService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface AccountDebitAssetSnapService extends GenericService<AccountDebitAssetSnap>{

    Long deleteAll();

    Long insertSpotDebit();

}

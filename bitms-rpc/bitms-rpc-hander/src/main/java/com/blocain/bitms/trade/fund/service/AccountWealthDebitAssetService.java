/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;
import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.entity.AccountWealthDebitAsset;

/**
 * 账户理财负债资产表 服务接口
 * <p>File：AccountDebitAssetService.java </p>
 * <p>Title: AccountDebitAssetService </p>
 * <p>Description:AccountDebitAssetService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface AccountWealthDebitAssetService extends GenericService<AccountWealthDebitAsset>{

    AccountWealthDebitAsset selectByPrimaryKeyForUpdate(Long id) throws BusinessException;

}

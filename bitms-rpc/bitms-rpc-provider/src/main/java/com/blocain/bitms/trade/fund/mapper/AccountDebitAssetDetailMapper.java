/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao ;
import com.blocain.bitms.trade.fund.entity.AccountDebitAssetDetail;

/**
 * 账户借贷资产明细表 持久层接口
 * <p>File：AccountDebitAssetDetailMapper.java </p>
 * <p>Title: AccountDebitAssetDetailMapper </p>
 * <p>Description:AccountDebitAssetDetailMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface AccountDebitAssetDetailMapper extends GenericMapper<AccountDebitAssetDetail>
{

}

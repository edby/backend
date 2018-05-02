/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao ;
import com.blocain.bitms.trade.fund.entity.AccountCollectBank;

/**
 * 账户收款银行表 持久层接口
 * <p>File：AccountCollectBankMapper.java </p>
 * <p>Title: AccountCollectBankMapper </p>
 * <p>Description:AccountCollectBankMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface AccountCollectBankMapper extends GenericMapper<AccountCollectBank>
{

}

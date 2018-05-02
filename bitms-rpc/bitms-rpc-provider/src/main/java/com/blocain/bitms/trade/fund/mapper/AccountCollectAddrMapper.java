/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.trade.fund.entity.AccountCollectAddr;

/**
 * 账户收款地址表 持久层接口
 * <p>File：AccountCollectAddrDao.java </p>
 * <p>Title: AccountCollectAddrDao </p>
 * <p>Description:AccountCollectAddrDao </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface AccountCollectAddrMapper extends GenericMapper<AccountCollectAddr>
{
    AccountCollectAddr findAccountCollectAddr(AccountCollectAddr accountCollectAddr);
}

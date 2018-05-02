/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao ;
import com.blocain.bitms.trade.fund.entity.AccountCollectAddrERC20CheckLog;

/**
 * ERC20账户收款地址审核日志表 持久层接口
 * <p>File：AccountCollectAddrERC20CheckLogMapper.java </p>
 * <p>Title: AccountCollectAddrERC20CheckLogMapper </p>
 * <p>Description:AccountCollectAddrERC20CheckLogMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface AccountCollectAddrERC20CheckLogMapper extends GenericMapper<AccountCollectAddrERC20CheckLog>
{

}

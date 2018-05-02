/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao ;
import com.blocain.bitms.trade.fund.entity.SystemWalletERC20;

/**
 * 系统ERC20钱包表 持久层接口
 * <p>File：SystemWalletERC20Mapper.java </p>
 * <p>Title: SystemWalletERC20Mapper </p>
 * <p>Description:SystemWalletERC20Mapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface SystemWalletERC20Mapper extends GenericMapper<SystemWalletERC20>
{
    SystemWalletERC20 findWallet(SystemWalletERC20 wallet);
}

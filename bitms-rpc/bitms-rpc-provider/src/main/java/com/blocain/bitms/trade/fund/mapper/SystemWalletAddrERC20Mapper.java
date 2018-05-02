/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao ;
import com.blocain.bitms.trade.fund.entity.SystemWalletAddrERC20;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * 系统ERC20钱包地址表 持久层接口
 * <p>File：SystemWalletAddrERC20Mapper.java </p>
 * <p>Title: SystemWalletAddrERC20Mapper </p>
 * <p>Description:SystemWalletAddrERC20Mapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface SystemWalletAddrERC20Mapper extends GenericMapper<SystemWalletAddrERC20>
{

    Set<String> findAccountInterAddrsByParams(String[] item);
}

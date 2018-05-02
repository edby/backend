/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.trade.fund.entity.SystemWalletERC20;
/**
 * 系统ERC20钱包表 服务接口
 * <p>File：SystemWalletERC20Service.java </p>
 * <p>Title: SystemWalletERC20Service </p>
 * <p>Description:SystemWalletERC20Service </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface SystemWalletERC20Service extends GenericService<SystemWalletERC20>{

    SystemWalletERC20 findWallet(SystemWalletERC20 systemWallet);

    int doRemoveSysWalletERC20AndAddr(String ids);
}

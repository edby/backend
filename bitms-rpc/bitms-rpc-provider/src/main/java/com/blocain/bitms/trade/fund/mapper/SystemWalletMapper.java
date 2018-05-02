/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.trade.fund.entity.SystemWallet;

/**
 * 系统钱包表 持久层接口
 * <p>File：SystemWalletDao.java </p>
 * <p>Title: SystemWalletDao </p>
 * <p>Description:SystemWalletDao </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface SystemWalletMapper extends GenericMapper<SystemWallet>
{
    SystemWallet findWallet(SystemWallet wallet);
    
    /**
     * 查询钱包和钱包的地址
     * @param wallet
     * @return
     */
    SystemWallet findWalletAndAddr(SystemWallet wallet);
}

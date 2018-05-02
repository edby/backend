/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.core.GenericMapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.trade.fund.entity.SystemWalletAddr;

/**
 * 系统钱包地址表 持久层接口
 * <p>File：SystemWalletAddrDao.java </p>
 * <p>Title: SystemWalletAddrDao </p>
 * <p>Description:SystemWalletAddrDao </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface SystemWalletAddrMapper extends GenericMapper<SystemWalletAddr>
{
    List<SystemWalletAddr> findWalletAddrList(@Param("map") Map<String, Object> addrMap);
    
    SystemWalletAddr findWalletAddr(SystemWalletAddr systemWalletAddr);
    
    int deleteByWalletIdId(@Param("walletId") String walletIdId, @Param("stockinfoId") Long stockinfoId);
}

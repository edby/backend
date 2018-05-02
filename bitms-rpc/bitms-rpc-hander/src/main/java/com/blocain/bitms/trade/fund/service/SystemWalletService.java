/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.entity.SystemWallet;

import java.math.BigDecimal;

/**
 * 系统钱包表 服务接口
 * <p>File：SystemWalletService.java </p>
 * <p>Title: SystemWalletService </p>
 * <p>Description:SystemWalletService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface SystemWalletService extends GenericService<SystemWallet>
{
    /**
     * 创建钱包
     * @param walletName    钱包名称
     * @param createBy      创建人
     * @return
     * @throws BusinessException
     * @author 施建波  2017年7月10日 上午10:38:51
     */
    SystemWallet createBtcWallet(String walletName, Long createBy) throws BusinessException;
    
    /**
     * 获取钱包信息
     * @param systemWallet  钱包实体
     * @return
     * @author 施建波  2017年7月10日 下午12:48:08
     */
    SystemWallet findWallet(SystemWallet systemWallet);
    
    /**
     * 查询钱包和钱包的地址
     * @param wallet
     * @return
     */
    SystemWallet findWalletAndAddr(SystemWallet wallet);
    
    /**
     * 删除系统钱包和钱包地址
     * @param ids
     * @return
     * @throws BusinessException
     */
    int doRemoveSysWalletAndAddr(String ids) throws BusinessException;
}

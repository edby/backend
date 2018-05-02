/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.entity.SystemWalletAddr;
import com.blocain.bitms.trade.fund.entity.SystemWalletAddrERC20;

import java.util.Set;

/**
 * 系统ERC20钱包地址表 服务接口
 * <p>File：SystemWalletAddrERC20Service.java </p>
 * <p>Title: SystemWalletAddrERC20Service </p>
 * <p>Description:SystemWalletAddrERC20Service </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface SystemWalletAddrERC20Service extends GenericService<SystemWalletAddrERC20>
{
    /**
     *  创建ERC20以及ETH 充值地址
     * @param accountId
     * @param createBy
     * @return
     * @throws BusinessException
     */
    SystemWalletAddrERC20 createERC20WalletAddress(Long accountId, Long createBy,Long stockinfoId) throws BusinessException;

    /**
     *  从数据库中获取所有erc20充值地址
     *
     * @param extAddress
     * @return
     */
    Set<String> getAccountInterAddrsByParams(String[] extAddress);
}

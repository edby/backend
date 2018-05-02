/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;


import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.fund.entity.SystemWalletERC20;
import com.blocain.bitms.trade.fund.mapper.SystemWalletERC20Mapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 系统ERC20钱包表 服务实现类
 * <p>File：SystemWalletERC20ServiceImpl.java </p>
 * <p>Title: SystemWalletERC20ServiceImpl </p>
 * <p>Description:SystemWalletERC20ServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class SystemWalletERC20ServiceImpl extends GenericServiceImpl<SystemWalletERC20> implements SystemWalletERC20Service
{

    protected SystemWalletERC20Mapper systemWalletERC20Mapper;

    @Autowired
    private SystemWalletERC20Service     systemWalletERC20Service;

    @Autowired
    public SystemWalletERC20ServiceImpl(SystemWalletERC20Mapper systemWalletERC20Mapper)
    {
        super(systemWalletERC20Mapper);
        this.systemWalletERC20Mapper = systemWalletERC20Mapper;
    }

    @Override
    public SystemWalletERC20 findWallet(SystemWalletERC20 systemWallet)
    {
        return systemWalletERC20Mapper.findWallet(systemWallet);
    }

    @Override
    public int doRemoveSysWalletERC20AndAddr(String ids) {
        String id[] = ids.split(",");
        for (int i = 0; i < id.length; i++)
        {
            SystemWalletERC20 systemWallet = systemWalletERC20Service.selectByPrimaryKey(Long.parseLong(id[i]));
            // walletAddrService.deleteByWalletIdId(systemWallet.getWalletId(), systemWallet.getStockinfoId());
            systemWalletERC20Service.remove(Long.parseLong(id[i]));
        }
        return 0;

    }


}

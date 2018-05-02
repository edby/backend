/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;


import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.fund.entity.AccountWalletAssetSnap;
import com.blocain.bitms.trade.fund.mapper.AccountWalletAssetSnapMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 钱包账户资产快照表 服务实现类
 * <p>File：AccountWalletAssetSnapServiceImpl.java </p>
 * <p>Title: AccountWalletAssetSnapServiceImpl </p>
 * <p>Description:AccountWalletAssetSnapServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountWalletAssetSnapServiceImpl extends GenericServiceImpl<AccountWalletAssetSnap> implements AccountWalletAssetSnapService
{

    protected AccountWalletAssetSnapMapper accountWalletAssetSnapMapper;

    @Autowired
    public AccountWalletAssetSnapServiceImpl(AccountWalletAssetSnapMapper accountWalletAssetSnapMapper)
    {
        super(accountWalletAssetSnapMapper);
        this.accountWalletAssetSnapMapper = accountWalletAssetSnapMapper;
    }

    @Override public Long deleteAll()
    {
        return accountWalletAssetSnapMapper.deleteAll();
    }

    @Override public Long insertWalletAsset()
    {
        return accountWalletAssetSnapMapper.insertWalletAsset();
    }
}

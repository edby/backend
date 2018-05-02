/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.fund.entity.AccountSpotAssetSnap;
import com.blocain.bitms.trade.fund.mapper.AccountSpotAssetSnapMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 现货账户资产快照表 服务实现类
 * <p>File：AccountSpotAssetSnapServiceImpl.java </p>
 * <p>Title: AccountSpotAssetSnapServiceImpl </p>
 * <p>Description:AccountSpotAssetSnapServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountSpotAssetSnapServiceImpl extends GenericServiceImpl<AccountSpotAssetSnap> implements AccountSpotAssetSnapService
{
    protected AccountSpotAssetSnapMapper accountSpotAssetSnapMapper;
    
    @Autowired
    public AccountSpotAssetSnapServiceImpl(AccountSpotAssetSnapMapper accountSpotAssetSnapMapper)
    {
        super(accountSpotAssetSnapMapper);
        this.accountSpotAssetSnapMapper = accountSpotAssetSnapMapper;
    }
    
    @Override
    public Long deleteAll()
    {
        return accountSpotAssetSnapMapper.deleteAll();
    }
    
    @Override
    public Long insertSpotAsset()
    {
        return accountSpotAssetSnapMapper.insertSpotAsset();
    }
}

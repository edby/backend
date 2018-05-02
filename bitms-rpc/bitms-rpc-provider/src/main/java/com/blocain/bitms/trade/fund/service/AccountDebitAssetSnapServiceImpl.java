/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.fund.entity.AccountDebitAssetSnap;
import com.blocain.bitms.trade.fund.mapper.AccountDebitAssetSnapMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 账户借贷资产快照表 服务实现类
 * <p>File：AccountDebitAssetSnapServiceImpl.java </p>
 * <p>Title: AccountDebitAssetSnapServiceImpl </p>
 * <p>Description:AccountDebitAssetSnapServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountDebitAssetSnapServiceImpl extends GenericServiceImpl<AccountDebitAssetSnap> implements AccountDebitAssetSnapService
{
    protected AccountDebitAssetSnapMapper accountDebitAssetSnapMapper;
    
    @Autowired
    public AccountDebitAssetSnapServiceImpl(AccountDebitAssetSnapMapper accountDebitAssetSnapMapper)
    {
        super(accountDebitAssetSnapMapper);
        this.accountDebitAssetSnapMapper = accountDebitAssetSnapMapper;
    }
    
    @Override
    public Long deleteAll()
    {
        return accountDebitAssetSnapMapper.deleteAll();
    }
    
    @Override
    public Long insertSpotDebit()
    {
        return accountDebitAssetSnapMapper.insertSpotDebit();
    }
}

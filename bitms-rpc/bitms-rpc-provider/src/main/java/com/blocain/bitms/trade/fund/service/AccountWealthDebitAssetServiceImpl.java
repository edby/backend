/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.entity.AccountWealthDebitAsset;
import com.blocain.bitms.trade.fund.mapper.AccountWealthDebitAssetMapper;

/**
 * 账户理财负债资产表 服务实现类
 * <p>File：AccountWealthDebitAssetServiceImpl.java </p>
 * <p>Title: AccountWealthDebitAssetServiceImpl </p>
 * <p>Description:AccountWealthDebitAssetServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountWealthDebitAssetServiceImpl extends GenericServiceImpl<AccountWealthDebitAsset> implements AccountWealthDebitAssetService
{
    protected AccountWealthDebitAssetMapper accountWealthDebitAssetMapper;
    
    @Autowired
    public AccountWealthDebitAssetServiceImpl(AccountWealthDebitAssetMapper accountWealthDebitAssetMapper)
    {
        super(accountWealthDebitAssetMapper);
        this.accountWealthDebitAssetMapper = accountWealthDebitAssetMapper;
    }
    
    @Override
    public AccountWealthDebitAsset selectByPrimaryKeyForUpdate(Long id) throws BusinessException
    {
        AccountWealthDebitAsset accountWealthDebitAsset;
        try
        {
            accountWealthDebitAsset = accountWealthDebitAssetMapper.selectByPrimaryKeyForUpdate(id);
        }
        catch (CannotAcquireLockException ex)
        {
            throw new BusinessException(ex.getLocalizedMessage());
        }
        return accountWealthDebitAsset;
    }
}

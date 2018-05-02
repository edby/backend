/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.entity.AccountSpotAsset;
import com.blocain.bitms.trade.fund.mapper.AccountSpotAssetMapper;

/**
 * 现货账户资产表 服务实现类
 * <p>File：AccountSpotAssetServiceImpl.java </p>
 * <p>Title: AccountSpotAssetServiceImpl </p>
 * <p>Description:AccountSpotAssetServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountSpotAssetServiceImpl extends GenericServiceImpl<AccountSpotAsset> implements AccountSpotAssetService
{
    AccountSpotAssetMapper accountSpotAssetMapper;
    
    @Autowired
    public AccountSpotAssetServiceImpl(AccountSpotAssetMapper accountSpotAssetMapper)
    {
        super(accountSpotAssetMapper);
        this.accountSpotAssetMapper = accountSpotAssetMapper;
    }
    
    @Override
    public PaginateResult<AccountSpotAsset> selectAll(Pagination pagination, AccountSpotAsset entity) throws BusinessException
    {
        if (null == pagination) pagination = new Pagination();
        entity.setPagin(pagination);
        List<AccountSpotAsset> data = accountSpotAssetMapper.findList(entity);
        return new PaginateResult<>(pagination, data);
    }
    
    @Override
    public AccountSpotAsset selectForUpdate(Long accountId, Long stockinfoId, Long relatedStockinfoId) throws BusinessException
    {
        AccountSpotAsset accountSpotAsset;
        try
        {
            accountSpotAsset = accountSpotAssetMapper.selectForUpdate(accountId, stockinfoId, relatedStockinfoId);
        }
        catch (CannotAcquireLockException ex)
        {
            throw new BusinessException(ex.getLocalizedMessage());
        }
        return accountSpotAsset;
    }
}

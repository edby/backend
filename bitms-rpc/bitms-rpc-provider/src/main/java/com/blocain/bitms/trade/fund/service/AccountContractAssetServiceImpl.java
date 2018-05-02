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
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.entity.AccountContractAsset;
import com.blocain.bitms.trade.fund.mapper.AccountContractAssetMapper;
import com.blocain.bitms.trade.fund.model.ContractAssetModel;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.google.common.collect.Lists;

/**
 * 合约账户资产表 服务实现类
 * <p>File：AccountContractAsset.java </p>
 * <p>Title: AccountContractAsset </p>
 * <p>Description:AccountContractAsset </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountContractAssetServiceImpl extends GenericServiceImpl<AccountContractAsset> implements AccountContractAssetService
{
    private AccountContractAssetMapper accountContractAssetMapper;
    
    @Autowired(required = false)
    private StockInfoService           stockInfoService;
    
    @Autowired
    public AccountContractAssetServiceImpl(AccountContractAssetMapper accountContractAssetMapper)
    {
        super(accountContractAssetMapper);
        this.accountContractAssetMapper = accountContractAssetMapper;
    }
    
    @Override
    public AccountContractAsset selectByPrimaryKeyOnRowLock(Long accountId, Long stockinfoId, Long relatedStockinfoId, String tableName) throws BusinessException
    {
        if (null == accountId || null == stockinfoId || null == relatedStockinfoId) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        AccountContractAsset accountContractAsset;
        try
        {
            accountContractAsset = accountContractAssetMapper.selectByPrimaryKeyOnRowLock(accountId, stockinfoId, relatedStockinfoId, tableName);
        }
        catch (CannotAcquireLockException ex)
        {
            throw new BusinessException(ex.getLocalizedMessage());
        }
        return accountContractAsset;
    }
    
    @Override
    public AccountContractAsset findAccountContractAsset(AccountContractAsset accountContractAsset)
    {
        return accountContractAssetMapper.findContractAsset(accountContractAsset);
    }
    
    @Override
    public ContractAssetModel findAccountSumContractAsset(Long stockinfoId, Long relatedStockinfoId, Long accountId)
    {
        return accountContractAssetMapper.findAccountSumContractAsset(stockinfoId, relatedStockinfoId, accountId, getStockInfo(relatedStockinfoId).getTableAsset());
    }
    
    @Override
    public List<ContractAssetModel> findAccountContractAssetGtZreo(Long stockinfoId, Long relatedStockinfoId)
    {
        return accountContractAssetMapper.findAccountContractAssetGtZreo(stockinfoId, relatedStockinfoId, getStockInfo(relatedStockinfoId).getTableAsset());
    }
    
    @Override
    public PaginateResult<AccountContractAsset> selectAll(Pagination pagin, AccountContractAsset entity) throws BusinessException
    {
        if (null == pagin) pagin = new Pagination();
        entity.setPagin(pagin);
        List<AccountContractAsset> data = accountContractAssetMapper.selectAll(entity);
        return new PaginateResult<>(pagin, data);
    }
    
    @Override
    public PaginateResult<AccountContractAsset> selectSuperAdminAsset(Pagination pagin, AccountContractAsset entity, Long ... ids) throws BusinessException
    {
        List<Long> filter = Lists.newArrayList(ids);
        if (null == pagin) pagin = new Pagination();
        entity.setPagin(pagin);
        List<AccountContractAsset> data = accountContractAssetMapper.selectSuperAdminAsset(entity, filter.toArray(new Long[]{}));
        return new PaginateResult<>(pagin, data);
    }
    
    @Override
    public List<AccountContractAsset> selectSuperAdminAsset(AccountContractAsset entity, Long ... ids) throws BusinessException
    {
        List<Long> filter = Lists.newArrayList(ids);
        // TradeEnums.valueOf("TABLE_ACCOUNTCONTRACTASSET_" + entity.getRelatedStockinfoId()
        entity.setTableName(getStockInfo(entity.getRelatedStockinfoId()).getTableAsset());
        List<AccountContractAsset> data = accountContractAssetMapper.selectSuperAdminAsset(entity, filter.toArray(new Long[]{}));
        return data;
    }
    
    /**
     * 根据关联证券ID 更新期初值
     * @param relatedStockinfoId
     */
    @Override
    public void updateContractAssetInitialAmt(Long relatedStockinfoId)
    {
        accountContractAssetMapper.updateContractAssetInitialAmt(relatedStockinfoId, getStockInfo(relatedStockinfoId).getTableAsset());
    }
    
    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
}

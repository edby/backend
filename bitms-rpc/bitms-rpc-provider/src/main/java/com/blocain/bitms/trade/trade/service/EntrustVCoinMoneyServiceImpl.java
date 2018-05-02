/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.trade.service;

import java.math.BigDecimal;
import java.util.List;

import com.blocain.bitms.trade.trade.model.FeeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.trade.entity.EntrustVCoinMoney;
import com.blocain.bitms.trade.trade.mapper.EntrustVCoinMoneyMapper;

/**
 * 委托表X 服务实现类
 * <p>File：EntrustVCoinMoneyServiceImpl.java </p>
 * <p>Title: EntrustVCoinMoneyServiceImpl </p>
 * <p>Description:EntrustVCoinMoneyServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class EntrustVCoinMoneyServiceImpl extends GenericServiceImpl<EntrustVCoinMoney> implements EntrustVCoinMoneyService
{
    public static final String        CACHE_KEY = "cache|get|money|doing|entrust|vcoin|money|cnt|";
    
    protected EntrustVCoinMoneyMapper entrustVCoinMoneyMapper;
    
    @Autowired(required = false)
    private StockInfoService          stockInfoService;
    
    @Autowired
    public EntrustVCoinMoneyServiceImpl(EntrustVCoinMoneyMapper entrustVCoinMoneyMapper)
    {
        super(entrustVCoinMoneyMapper);
        this.entrustVCoinMoneyMapper = entrustVCoinMoneyMapper;
    }
    
    @Override
    public EntrustVCoinMoney selectByPrimaryKey(String tableName, Long id) throws BusinessException
    {
        return entrustVCoinMoneyMapper.selectByPrimaryKey(tableName, id);
    }
    
    @Override
    public EntrustVCoinMoney selectByPrimaryKeyOnRowLock(String tableName, Long id) throws BusinessException
    {
        EntrustVCoinMoney entrustVCoinMoney;
        try
        {
            entrustVCoinMoney = entrustVCoinMoneyMapper.selectByPrimaryKeyOnRowLock(tableName, id);
        }
        catch (CannotAcquireLockException ex)
        {
            throw new BusinessException(ex.getLocalizedMessage());
        }
        return entrustVCoinMoney;
    }
    
    @Override
    public BigDecimal findSumMatchEntrustVCoinMoneyAmtByAccount(Long accountId, Long entrustStockinfoId) throws BusinessException
    {
        return entrustVCoinMoneyMapper.findSumMatchEntrustVCoinMoneyAmtByAccount(accountId, entrustStockinfoId);
    }
    
    @Override
    public List<EntrustVCoinMoney> findAllInEntrust(String tableName) throws BusinessException
    {
        EntrustVCoinMoney entrustVCoinMoney = new EntrustVCoinMoney();
        entrustVCoinMoney.setTableName(tableName);
        return entrustVCoinMoneyMapper.findAllInEntrust(entrustVCoinMoney);
    }
    
    @Override
    public List<EntrustVCoinMoney> findAccountInEntrust(String tableName, Long accountId) throws BusinessException
    {
        return entrustVCoinMoneyMapper.findAccountInEntrust(tableName, accountId);
    }
    
    @Override
    public PaginateResult<EntrustVCoinMoney> findAllInEntrust(Pagination pagin, EntrustVCoinMoney entrustVCoinMoney) throws BusinessException
    {
        entrustVCoinMoney.setPagin(pagin);
        List<EntrustVCoinMoney> entrustVCoinMoneyList = entrustVCoinMoneyMapper.findAllInEntrust(entrustVCoinMoney);
        return new PaginateResult<>(pagin, entrustVCoinMoneyList);
    }
    
    @Override
    public PaginateResult<EntrustVCoinMoney> findWithdrawBySysEntrust(Pagination pagin, EntrustVCoinMoney entrustVCoinMoney) throws BusinessException
    {
        entrustVCoinMoney.setPagin(pagin);
        List<EntrustVCoinMoney> entrustVCoinMoneyList = entrustVCoinMoneyMapper.findWithdrawBySysEntrust(entrustVCoinMoney);
        return new PaginateResult<>(pagin, entrustVCoinMoneyList);
    }
    
    @Override
    public List<EntrustVCoinMoney> getAccountDoingEntrustVCoinMoneyList(EntrustVCoinMoney entrustVCoinMoney) throws BusinessException
    {
        List<EntrustVCoinMoney> entrustVCoinMoneyList = entrustVCoinMoneyMapper.getAccountDoingEntrustVCoinMoneyList(entrustVCoinMoney);
        return entrustVCoinMoneyList;
    }

    @Override
    public List<EntrustVCoinMoney> getAccountDoingEntrustVCoinMoneyListByPrice(EntrustVCoinMoney entrustVCoinMoney) throws BusinessException {
        List<EntrustVCoinMoney> entrustVCoinMoneyList = entrustVCoinMoneyMapper.getAccountDoingEntrustVCoinMoneyListByPrice(entrustVCoinMoney);
        return entrustVCoinMoneyList;
    }

    @Override
    public  PaginateResult<EntrustVCoinMoney>  getAccountDoingEntrustVCoinMoneyPagin(Pagination pagin,EntrustVCoinMoney entrustVCoinMoney) throws BusinessException
    {
        entrustVCoinMoney.setPagin(pagin);
        List<EntrustVCoinMoney> entrustVCoinMoneyList = entrustVCoinMoneyMapper.getAccountDoingEntrustVCoinMoneyList(entrustVCoinMoney);
        return new PaginateResult<>(pagin, entrustVCoinMoneyList);
    }

    @Override
    public PaginateResult<EntrustVCoinMoney> getAccountHistoryEntrustVCoinMoneyList(Pagination pagin, EntrustVCoinMoney entrustVCoinMoney) throws BusinessException
    {
        entrustVCoinMoney.setPagin(pagin);
        List<EntrustVCoinMoney> entrustVCoinMoneyList = entrustVCoinMoneyMapper.getAccountHistoryEntrustVCoinMoneyList(entrustVCoinMoney);
        return new PaginateResult<>(pagin, entrustVCoinMoneyList);
    }
    
    @Override
    public BigDecimal findSumShortReserveAllocation(String tableName) throws BusinessException
    {
        return entrustVCoinMoneyMapper.findSumShortReserveAllocation(tableName);
    }
    
    @Override
    public BigDecimal findSumLongReserveAllocation(String tableName) throws BusinessException
    {
        return entrustVCoinMoneyMapper.findSumLongReserveAllocation(tableName);
    }
    
    @Override
    public PaginateResult<EntrustVCoinMoney> findListAfterPreSettlement(Pagination pagin, EntrustVCoinMoney entrustVCoinMoney) throws BusinessException
    {
        entrustVCoinMoney.setPagin(pagin);
        List<EntrustVCoinMoney> entrustVCoinMoneyList = entrustVCoinMoneyMapper.findListAfterPreSettlement(entrustVCoinMoney);
        return new PaginateResult<>(pagin, entrustVCoinMoneyList);
    }
    
    @Override
    public PaginateResult<EntrustVCoinMoney> findAdminEnturstList(Pagination pagin, EntrustVCoinMoney entrustVCoinMoney) throws BusinessException
    {
        entrustVCoinMoney.setPagin(pagin);
        List<EntrustVCoinMoney> entrustVCoinMoneyList = entrustVCoinMoneyMapper.findAdminEnturstList(entrustVCoinMoney);
        return new PaginateResult<>(pagin, entrustVCoinMoneyList);
    }
    
    @Override
    public PaginateResult<EntrustVCoinMoney> searchAll(Pagination pagin, EntrustVCoinMoney entrustVCoinMoney) throws BusinessException
    {
        entrustVCoinMoney.setPagin(pagin);
        List<EntrustVCoinMoney> entrustVCoinMoneyList = entrustVCoinMoneyMapper.searchAll(entrustVCoinMoney);
        return new PaginateResult<>(pagin, entrustVCoinMoneyList);
    }
    
    @Override
    public Long getAccountDoingEntrustVCoinMoneyCnt(Long accountId, Long exchangePairMoney)
    {
        String tableName = getStockInfo(exchangePairMoney).getTableEntrust();
        return entrustVCoinMoneyMapper.getAccountDoingEntrustVCoinMoneyCnt(accountId, tableName);
    }
    
    @Override
    public Long getMoneyDoingEntrustVCoinMoneyCnt(Long exchangePairMoney)
    {
        String doingEntrust_key = new StringBuffer(CACHE_KEY).append(exchangePairMoney).toString();
        Long value = (Long) RedisUtils.getObject(doingEntrust_key);
        if (null == value)
        {
            value = entrustVCoinMoneyMapper.getMoneyDoingEntrustVCoinMoneyCnt(getStockInfo(exchangePairMoney).getTableEntrust());
            RedisUtils.putObject(doingEntrust_key, value, CacheConst.TWO_SECONDS_CACHE_TIME);
        }
        return value;
    }
    
    @Override
    public BigDecimal clearCalcLongSuperAccountLossAmt(String tableName, BigDecimal clearPrice, Long relatedStockinfoId) throws BusinessException
    {
        return entrustVCoinMoneyMapper.clearCalcLongSuperAccountLossAmt(tableName, clearPrice, relatedStockinfoId);
    }
    
    @Override
    public BigDecimal clearCalcShortSuperAccountLossAmt(String tableName, BigDecimal clearPrice, Long relatedStockinfoId) throws BusinessException
    {
        return entrustVCoinMoneyMapper.clearCalcShortSuperAccountLossAmt(tableName, clearPrice, relatedStockinfoId);
    }
    
    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }

    @Override
    public List<FeeModel> selectSumFeeNeedAward(String tableName,String yestoday,String today)
    {
        return entrustVCoinMoneyMapper.selectSumFeeNeedAward(tableName,yestoday,today);
    }
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import java.math.BigDecimal;
import java.util.List;

import com.blocain.bitms.payment.eth.Erc20TokenLocalService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.stockinfo.entity.Erc20Token;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.Erc20TokenService;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.entity.AccountWalletAsset;
import com.blocain.bitms.trade.fund.mapper.AccountWalletAssetMapper;

/**
 * 钱包账户资产表 服务实现类
 * <p>File：AccountWalletAssetServiceImpl.java </p>
 * <p>Title: AccountWalletAssetServiceImpl </p>
 * <p>Description:AccountWalletAssetServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountWalletAssetServiceImpl extends GenericServiceImpl<AccountWalletAsset> implements AccountWalletAssetService
{
    AccountWalletAssetMapper  accountWalletAssetMapper;
    
    @Autowired
    Erc20TokenLocalService    erc20TokenLocalService;

    @Autowired
    AccountWalletAssetService accountWalletAssetService;

    @Autowired
    StockInfoService          stockInfoService;

    @Autowired
    Erc20TokenService         erc20TokenService;

    @Autowired
    public AccountWalletAssetServiceImpl(AccountWalletAssetMapper accountWalletAssetMapper)
    {
        super(accountWalletAssetMapper);
        this.accountWalletAssetMapper = accountWalletAssetMapper;
    }

    @Override
    public PaginateResult<AccountWalletAsset> selectAll(Pagination pagination, AccountWalletAsset entity) throws BusinessException
    {
        if (null == pagination) pagination = new Pagination();
        entity.setPagin(pagination);
        List<AccountWalletAsset> data = accountWalletAssetMapper.findList(entity);
        return new PaginateResult<>(pagination, data);
    }

    @Override
    public PaginateResult<AccountWalletAsset> tradeXFindList(Pagination pagination, AccountWalletAsset entity) throws BusinessException
    {
        if (null == pagination) pagination = new Pagination();
        entity.setPagin(pagination);
        List<AccountWalletAsset> data = accountWalletAssetMapper.tradeXFindList(entity);
        return new PaginateResult<>(pagination, data);
    }

    @Override
    public AccountWalletAsset selectForUpdate(Long accountId, Long stockinfoId) throws BusinessException
    {
        AccountWalletAsset accountWalletAsset;
        try
        {
            accountWalletAsset = accountWalletAssetMapper.selectForUpdate(accountId, stockinfoId);
        }
        catch (CannotAcquireLockException ex)
        {
            throw new BusinessException(ex.getLocalizedMessage());
        }
        return accountWalletAsset;
    }

    @Override
    public BigDecimal getPlatSumCoinByStockInfoId(Long stockinfoId)
    {
        return accountWalletAssetMapper.getPlatSumCoinByStockInfoId(stockinfoId);
    }

    @Override
    public void autoCheckPlatSumCoin()
    {
        List<StockInfo> list = stockInfoService.findListByTypes(FundConsts.STOCKTYPE_ERC20_TOKEN);
        for (StockInfo entity : list)
        {
            if (entity.getId().longValue() != FundConsts.WALLET_ETH_TYPE.longValue())
            {
                try
                {
                    accountWalletAssetService.doCheckPlatSumCoinByStockInfoId(entity.getTradeStockinfoId());
                }
                catch (Exception e)
                {
                    logger.debug("检查stockinfo=" + entity.getId() + "失败：" + e.getLocalizedMessage());
                }
            }
        }
    }

    @Override
    public boolean doCheckPlatSumCoinByStockInfoId(Long stockinfoId) throws BusinessException
    {
        if (stockinfoId.longValue() == FundConsts.WALLET_ETH_TYPE.longValue()) { return true; }
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(stockinfoId);
        BigDecimal amt = accountWalletAssetMapper.getPlatSumCoinByStockInfoId(stockinfoId);
        logger.debug("全平台共有：" + stockinfoId + "=" + amt);
        BigDecimal totalSupply = BigDecimal.valueOf(0);
        try
        {
            totalSupply = erc20TokenLocalService.erc20_totalSupply(stockInfo.getTokenContactAddr());
            logger.debug("获取外部发行总量：" + stockinfoId + "=" + totalSupply);
        }
        catch (Exception e)
        {
            logger.debug("获取外部发行总量失败：" + stockinfoId + "=" + totalSupply);
            logger.debug(e.getLocalizedMessage());
            return false;
        }
        if (totalSupply == null)
        {
            logger.debug("获取外部发行总量失败：" + stockinfoId + "=" + totalSupply);
            return false;
        }
        if (amt.compareTo(totalSupply) >= 0)
        {
            // 修改交易对 停止交易 可提币
            StockInfo stockPair = stockInfoService.findByContractAddr(stockInfo.getTokenContactAddr());
            stockPair.setCanTrade(FundConsts.PUBLIC_STATUS_NO);
            stockPair.setCanRecharge(FundConsts.PUBLIC_STATUS_NO);
            stockPair.setCanWithdraw(FundConsts.PUBLIC_STATUS_NO);
            stockPair.setIsActive(FundConsts.PUBLIC_STATUS_NO);
            stockInfoService.updateByPrimaryKeySelective(stockPair);
            // 修改币种 停止交易 可提币
            stockInfo.setCanTrade(FundConsts.PUBLIC_STATUS_NO);
            stockInfo.setCanRecharge(FundConsts.PUBLIC_STATUS_NO);
            stockInfo.setIsActive(FundConsts.PUBLIC_STATUS_NO);
            stockInfo.setCanWithdraw(FundConsts.PUBLIC_STATUS_NO);
            stockInfoService.updateByPrimaryKeySelective(stockInfo);
            // 修改token
            Erc20Token erc20Token = erc20TokenService.getErc20Token(stockInfo.getTokenContactAddr(), null);
            erc20Token.setIsActive(FundConsts.PUBLIC_STATUS_NO);
            erc20TokenService.updateByPrimaryKey(erc20Token);
            return false;
        }
        else
        {
            return true;
        }
    }
}

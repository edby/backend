/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.DateUtils;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountWealthAsset;
import com.blocain.bitms.trade.fund.entity.AccountWealthAssetDetail;
import com.blocain.bitms.trade.fund.mapper.AccountWealthAssetMapper;
import com.blocain.bitms.trade.fund.model.FundModel;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;

/**
 * 账户理财资产表 服务实现类
 * <p>File：AccountWealthAssetServiceImpl.java </p>
 * <p>Title: AccountWealthAssetServiceImpl </p>
 * <p>Description:AccountWealthAssetServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountWealthAssetServiceImpl extends GenericServiceImpl<AccountWealthAsset> implements AccountWealthAssetService
{
    protected AccountWealthAssetMapper accountWealthAssetMapper;
    
    @Autowired
    StockInfoService                   stockInfoService;
    
    @Autowired
    AccountWealthAssetService          accountWealthAssetService;
    
    @Autowired
    FundService                        fundService;
    
    @Autowired
    AccountWealthAssetDetailService    accountWealthAssetDetailService;
    
    @Autowired
    public AccountWealthAssetServiceImpl(AccountWealthAssetMapper accountWealthAssetMapper)
    {
        super(accountWealthAssetMapper);
        this.accountWealthAssetMapper = accountWealthAssetMapper;
    }
    
    /**
     * 自动计息
     * @return
     */
    @Override
    public void autoAccountWealthAssetInterest() throws BusinessException
    {
        StockInfo stockInfoSelect = new StockInfo();
        stockInfoSelect.setCanBorrow(FundConsts.PUBLIC_STATUS_YES);
        stockInfoSelect.setStockType(FundConsts.STOCKTYPE_LEVERAGEDSPOT);
        List<StockInfo> listStock = stockInfoService.findList(stockInfoSelect);
        for (StockInfo stockInfo : listStock)
        {
            logger.debug("关联证券ID=" + stockInfo.getId() + " stocktype=" + stockInfo.getStockType());
            AccountWealthAsset accountWealthAssetSelect = new AccountWealthAsset();
            accountWealthAssetSelect.setLastInterestDay(Long.parseLong(DateUtils.getDateFormat(System.currentTimeMillis(), "yyyyMMdd")));
            List<AccountWealthAsset> AccountWealthAssetList = accountWealthAssetMapper.findListForWealth(accountWealthAssetSelect);
            if (AccountWealthAssetList.size() == 0)
            {
                logger.debug("目前还没有借款的用户");
            }
            else
            {
                for (AccountWealthAsset record : AccountWealthAssetList)
                {
                    if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_LEVERAGEDSPOT))
                    {
                        accountWealthAssetService.doInterestRate(record, stockInfo.getId());
                    }
                    else
                    {
                        logger.debug("错误的证券类型不处理");
                        continue;
                    }
                }
            }
        }
    }
    
    @Override
    public void doInterestRate(AccountWealthAsset record, Long exchangePairMoney) throws BusinessException
    {
        // 行锁
        record = accountWealthAssetService.selectByPrimaryKeyForUpdate(record.getId());
        Long currDate = Long.parseLong(DateUtils.getDateFormat(System.currentTimeMillis(), "yyyyMMdd"));
        if (record.getLastInterestDay().longValue() >= currDate.longValue())
        {
            logger.error("理财：已经计息过，不能重复计息。");
            return;
        }
        StockInfo stockInfo = getStockInfo(exchangePairMoney);
        BigDecimal rate = stockInfo.getWealthDayRate();
        logger.debug("理财ID:" + record.getId() + " 理财信息：" + record.toString());
        logger.debug("理财ID:" + record.getId() + " 理财利率：" + rate);
        Long ratedate = Long.parseLong(DateUtils.getDateFormat(System.currentTimeMillis(), "yyyyMMdd"));
        if (ratedate.longValue() > record.getLastInterestDay().longValue())
        {
            // 计算利息
            BigDecimal effectiveWealthAmt = record.getWealthAmt();
            logger.debug("借款ID:" + record.getId() + " 有效借款：" + effectiveWealthAmt);
            if (effectiveWealthAmt.compareTo(BigDecimal.ZERO) > 0)
            {
                BigDecimal interest = effectiveWealthAmt.multiply(rate).setScale(12, BigDecimal.ROUND_HALF_UP);
                // 修改借款表
                record.setWealthAmt(record.getWealthAmt().add(interest));
                record.setAccumulateInterest(record.getAccumulateInterest().add(interest));
                record.setLastInterestDay(ratedate);
                accountWealthAssetMapper.updateByPrimaryKey(record);
                // 利息处理
                FundModel fundModel = new FundModel();
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_AUTO_WEALTH_INTEREST);
                fundModel.setStockinfoId(record.getStockinfoId());
                fundModel.setStockinfoIdEx(exchangePairMoney);
                fundModel.setAmount(record.getWealthAmt().subtract(interest));
                fundModel.setFee(interest);
                fundModel.setAccountId(record.getWealthAccountId());
                fundModel.setCreateBy(record.getWealthAccountId());
                fundModel.setOriginalBusinessId(record.getId());
                fundService.fundTransaction(fundModel);
                // 计息记录
                Long id = SerialnoUtils.buildPrimaryKey();
                AccountWealthAssetDetail detail = new AccountWealthAssetDetail();
                detail.setId(id);
                detail.setWealthAccountId(record.getWealthAccountId());
                detail.setIssuerAccountId(record.getIssuerAccountId());
                detail.setStockinfoId(record.getStockinfoId());
                detail.setRelatedStockinfoId(record.getRelatedStockinfoId());
                detail.setWealthAmt(record.getWealthAmt());
                detail.setEffectiveWealthAmt(effectiveWealthAmt);
                detail.setWealthDayRate(rate);
                detail.setDayInterest(interest);
                detail.setAccumulateInterest(record.getAccumulateInterest());
                detail.setUpdateDate(new Date());
                accountWealthAssetDetailService.insert(detail);
                // 利息计给超级用户
                if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_LEVERAGEDSPOT))
                {
                    // 超级用户资产处理 增加一笔利息累计
                    fundModel = new FundModel();
                    fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_AUTO_WEALTH_INTEREST);
                    fundModel.setStockinfoId(record.getStockinfoId());
                    fundModel.setStockinfoIdEx(exchangePairMoney);
                    fundModel.setAmount(interest);
                    fundModel.setFee(interest);
                    fundModel.setAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_DEBIT_WEALTH_ID);
                    fundModel.setCreateBy(record.getWealthAccountId());
                    fundModel.setOriginalBusinessId(id);
                    fundService.doSuperAdminDebitInterest(fundModel, FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
                }
            }
            else
            {
                logger.debug("借款ID:" + record.getId() + "不产生利息 有效借款：" + effectiveWealthAmt);
            }
        }
        else
        {
            logger.debug("借款ID:" + record.getId() + "今日已计息 借款信息：" + record.toString());
        }
    }
    
    @Override
    public AccountWealthAsset selectByPrimaryKeyForUpdate(Long id) throws BusinessException
    {
        AccountWealthAsset accountWealthAsset;
        try
        {
            accountWealthAsset = accountWealthAssetMapper.selectByPrimaryKeyForUpdate(id);
        }
        catch (CannotAcquireLockException ex)
        {
            throw new BusinessException(ex.getLocalizedMessage());
        }
        return accountWealthAsset;
    }
    
    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
    
    @Override
    public PaginateResult<AccountWealthAsset> selectAll(Pagination pagination, AccountWealthAsset entity) throws BusinessException
    {
        if (null == pagination) pagination = new Pagination();
        entity.setPagin(pagination);
        List<AccountWealthAsset> data = accountWealthAssetMapper.findList(entity);
        return new PaginateResult<>(pagination, data);
    }
}

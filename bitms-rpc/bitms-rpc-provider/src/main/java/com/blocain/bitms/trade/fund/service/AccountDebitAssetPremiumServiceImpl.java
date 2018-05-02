/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.quotation.entity.RtQuotationInfo;
import com.blocain.bitms.quotation.service.RtQuotationInfoService;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountDebitAssetPremium;
import com.blocain.bitms.trade.fund.entity.AccountSpotAssetSnap;
import com.blocain.bitms.trade.fund.entity.MarketSnap;
import com.blocain.bitms.trade.fund.mapper.AccountDebitAssetPremiumMapper;
import com.blocain.bitms.trade.fund.model.AccountPremiumAssetModel;
import com.blocain.bitms.trade.fund.model.FundModel;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 账户持仓调节(溢价费)记录表 服务实现类
 * <p>File：AccountDebitAssetPremiumServiceImpl.java </p>
 * <p>Title: AccountDebitAssetPremiumServiceImpl </p>
 * <p>Description:AccountDebitAssetPremiumServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountDebitAssetPremiumServiceImpl extends GenericServiceImpl<AccountDebitAssetPremium> implements AccountDebitAssetPremiumService
{
    protected AccountDebitAssetPremiumMapper accountDebitAssetPremiumMapper;
    
    @Autowired
    RtQuotationInfoService                   rtQuotationInfoService;
    
    @Autowired
    MarketSnapService                        marketSnapService;
    
    @Autowired
    AccountSpotAssetSnapService              accountSpotAssetSnapService;
    
    @Autowired
    AccountWalletAssetSnapService            accountWalletAssetSnapService;
    
    @Autowired
    AccountDebitAssetSnapService             accountDebitAssetSnapService;
    
    @Autowired
    AccountDebitAssetPremiumService          accountDebitAssetPremiumService;
    
    @Autowired
    FundService                              fundService;
    
    @Autowired
    public AccountDebitAssetPremiumServiceImpl(AccountDebitAssetPremiumMapper accountDebitAssetPremiumMapper)
    {
        super(accountDebitAssetPremiumMapper);
        this.accountDebitAssetPremiumMapper = accountDebitAssetPremiumMapper;
    }
    
    @Override
    public List<AccountPremiumAssetModel> getPremiumLongAccountAsset(Long relatedStockinfoId, Long stockinfoId)
    {
        return accountDebitAssetPremiumMapper.getPremiumLongAccountAsset(relatedStockinfoId, stockinfoId);
    }
    
    @Override
    public List<AccountPremiumAssetModel> getPremiumShortAccountAsset(Long relatedStockinfoId, Long stockinfoId)
    {
        return accountDebitAssetPremiumMapper.getPremiumShortAccountAsset(relatedStockinfoId, stockinfoId);
    }
    
    /**
     *  行情快照
     */
    @Override
    public void doMarketSnap()
    {
        List<MarketSnap> list = marketSnapService.selectAll();
        for (MarketSnap entity : list)
        {
            marketSnapService.delete(entity.getId());
        }
        MarketSnap marketSnap = new MarketSnap();
        RtQuotationInfo rtQuotationInfo = rtQuotationInfoService.queryRtQuotationInfoFromCache(FundConsts.WALLET_USD_TYPE, FundConsts.WALLET_BTC2USD_TYPE);
        marketSnap.setIndexPrice(rtQuotationInfo.getIdxAvgPrice());
        marketSnap.setMarketTimestamp(new Timestamp(System.currentTimeMillis()));
        marketSnap.setPairStockinfoId(FundConsts.WALLET_BTC2USD_TYPE);
        marketSnap.setPaltformPrice(rtQuotationInfo.getPlatPrice());
        marketSnap.setPremiumPrice(rtQuotationInfo.getPlatPrice().subtract(rtQuotationInfo.getIdxAvgPrice()));
        marketSnap.setPremiumRate(
                (rtQuotationInfo.getPlatPrice().subtract(rtQuotationInfo.getIdxAvgPrice())).divide(rtQuotationInfo.getPlatPrice(), 12, BigDecimal.ROUND_HALF_UP));
        marketSnap.setId(SerialnoUtils.buildPrimaryKey());
        marketSnapService.insert(marketSnap);
    }
    
    /**
     * 杠杆现货资产负债快照
     */
    @Override
    public void doLeveragedSpotAssetAndDebitSnap()
    {
        accountSpotAssetSnapService.deleteAll();
        accountSpotAssetSnapService.insertSpotAsset();
        accountDebitAssetSnapService.deleteAll();
        accountDebitAssetSnapService.insertSpotDebit();
    }

    /**
     * 纯正现货资产快照
     */
    @Override
    public void doPureSpotAssetSnap()
    {
        accountWalletAssetSnapService.deleteAll();
        accountWalletAssetSnapService.insertWalletAsset();
    }
    
    @Override
    public void autoPremium()
    {
        MarketSnap marketSnap = marketSnapService.selectLastOne(FundConsts.WALLET_BTC2USD_TYPE);
        if (marketSnap == null)
        {
            logger.error("没有查到十二小时内的行情快照，溢价率有问题！");
        }
        else
        {
            if (marketSnap.getPremiumRate() == null)
            {
                logger.error("没有查到十二小时内的行情快照，溢价率有问题！");
            }
            else
            {
                // 多头用户
                List<AccountPremiumAssetModel> longList = accountDebitAssetPremiumMapper.getPremiumLongAccountAsset(FundConsts.WALLET_USD_TYPE, FundConsts.WALLET_USD_TYPE);
                // 空头用户
                List<AccountPremiumAssetModel> shortList = accountDebitAssetPremiumMapper.getPremiumShortAccountAsset(FundConsts.WALLET_USD_TYPE,
                        FundConsts.WALLET_USD_TYPE);
                // 正溢价
                if (marketSnap.getPremiumRate().compareTo(BigDecimal.valueOf(0.01)) > 0)
                {
                    if (marketSnap.getPremiumRate().compareTo(BigDecimal.valueOf(0.1)) > 0)
                    {
                        marketSnap.setPremiumRate(BigDecimal.valueOf(0.1));
                    }
                    logger.debug("正溢价 溢价率=" + marketSnap.getPremiumRate().multiply(BigDecimal.valueOf(100)) + "%");
                    // 多头账户债务增加 92超级用户资产增加
                    for (AccountPremiumAssetModel model : longList)
                    {
                        BigDecimal premiumfee = (model.getAmount().subtract(model.getDebitAmt())).multiply(marketSnap.getPremiumRate()).divide(BigDecimal.valueOf(10))
                                .abs();
                        premiumfee = premiumfee.setScale(4, BigDecimal.ROUND_UP);
                        if (premiumfee.compareTo(BigDecimal.ZERO) > 0)
                        {
                            accountDebitAssetPremiumService.doPremium(marketSnap, model, -1, premiumfee);
                        }
                        else
                        {
                            logger.debug("用户：" + model.getAccountId() + " 收取的费用太小 不收取");
                        }
                    }
                    // 空头账户资产增加 92超级用户资产减少
                    for (AccountPremiumAssetModel model : shortList)
                    {
                        logger.debug(model.toString());
                        BigDecimal premiumfee = (model.getAmount().subtract(model.getDebitAmt())).multiply(marketSnap.getPremiumRate()).divide(BigDecimal.valueOf(10))
                                .abs();
                        premiumfee = premiumfee.setScale(4, BigDecimal.ROUND_DOWN);
                        if (premiumfee.compareTo(BigDecimal.ZERO) > 0)
                        {
                            accountDebitAssetPremiumService.doPremium(marketSnap, model, 1, premiumfee);
                        }
                        else
                        {
                            logger.debug("用户：" + model.getAccountId() + " 收取的费用太小 不收取");
                        }
                    }
                }
                // 负溢价
                else if (marketSnap.getPremiumRate().compareTo(BigDecimal.valueOf(-0.01)) < 0)
                {
                    if (marketSnap.getPremiumRate().compareTo(BigDecimal.valueOf(-0.1)) < 0)
                    {
                        marketSnap.setPremiumRate(BigDecimal.valueOf(-0.1));
                    }
                    logger.debug("负溢价 溢价率=" + marketSnap.getPremiumRate().multiply(BigDecimal.valueOf(100)) + "%");
                    // 空头账户负债增加 92超级用户资产增加
                    for (AccountPremiumAssetModel model : shortList)
                    {
                        BigDecimal premiumfee = (model.getAmount().subtract(model.getDebitAmt())).multiply(marketSnap.getPremiumRate()).divide(BigDecimal.valueOf(10))
                                .abs();
                        premiumfee = premiumfee.setScale(4, BigDecimal.ROUND_UP);
                        if (premiumfee.compareTo(BigDecimal.ZERO) > 0)
                        {
                            accountDebitAssetPremiumService.doPremium(marketSnap, model, -1, premiumfee);
                        }
                        else
                        {
                            logger.debug("用户：" + model.getAccountId() + " 收取的费用太小 不收取");
                        }
                    }
                    // 多头账户资产增加 92超级用户资产减少
                    for (AccountPremiumAssetModel model : longList)
                    {
                        BigDecimal premiumfee = (model.getAmount().subtract(model.getDebitAmt())).multiply(marketSnap.getPremiumRate()).divide(BigDecimal.valueOf(10))
                                .abs();
                        premiumfee = premiumfee.setScale(4, BigDecimal.ROUND_DOWN);
                        if (premiumfee.compareTo(BigDecimal.ZERO) > 0)
                        {
                            accountDebitAssetPremiumService.doPremium(marketSnap, model, 1, premiumfee);
                        }
                        else
                        {
                            logger.debug("用户：" + model.getAccountId() + " 收取的费用太小 不收取");
                        }
                    }
                }
                else
                {
                    logger.debug("溢价率=" + marketSnap.getPremiumRate().multiply(BigDecimal.valueOf(100)) + "%");
                }
            }
        }
    }
    
    @Override
    public void doPremium(MarketSnap marketSnap, AccountPremiumAssetModel accountPremiumAssetModel, int direction, BigDecimal premiumfee)
    {
        logger.debug("进入调节 账户：" + accountPremiumAssetModel.getAccountId() + " 方向： " + direction);
        logger.debug(accountPremiumAssetModel.toString());
        try
        {
            AccountSpotAssetSnap accountSpotAssetSnap = accountSpotAssetSnapService.selectByPrimaryKey(accountPremiumAssetModel.getId());
            if (accountSpotAssetSnap == null) { throw new BusinessException("资产快照id错误" + accountPremiumAssetModel.getId()); }
            // 更新状态
            accountSpotAssetSnap.setPremiumDealFlag(BigDecimal.ONE);
            int cnt = accountSpotAssetSnapService.updateByPrimaryKey(accountSpotAssetSnap);
            if (cnt == 0) { throw new BusinessException("资产快照更新状态错误" + accountPremiumAssetModel.getId()); }
            // 资产或负债处理
            FundModel fundModel = new FundModel();
            fundModel.setDirection(direction);
            fundModel.setAccountId(accountPremiumAssetModel.getAccountId());
            fundModel.setStockinfoId(accountPremiumAssetModel.getStockinfoId());
            fundModel.setStockinfoIdEx(accountPremiumAssetModel.getRelatedStockinfoId());
            fundModel.setAmount(premiumfee);
            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_POSITION_PREMINM_FEE);
            fundService.fundTransaction(fundModel);
            if (direction == -1)
            {
                premiumfee = BigDecimal.ZERO.subtract(premiumfee);
            }
            AccountDebitAssetPremium accountDebitAssetPremium = new AccountDebitAssetPremium();
            accountDebitAssetPremium.setAccountId(accountPremiumAssetModel.getAccountId());
            accountDebitAssetPremium.setStockinfoId(accountPremiumAssetModel.getStockinfoId());
            accountDebitAssetPremium.setRelatedStockinfoId(accountPremiumAssetModel.getRelatedStockinfoId());
            accountDebitAssetPremium.setAssetAmt(accountPremiumAssetModel.getAmount());
            accountDebitAssetPremium.setDebitAmt(accountPremiumAssetModel.getDebitAmt());
            accountDebitAssetPremium.setPositionAmt(accountPremiumAssetModel.getAmount().subtract(accountPremiumAssetModel.getDebitAmt()));
            accountDebitAssetPremium.setPaltformPrice(marketSnap.getPaltformPrice());
            accountDebitAssetPremium.setIndexPrice(marketSnap.getIndexPrice());
            accountDebitAssetPremium.setPremiumPrice(marketSnap.getPaltformPrice().subtract(marketSnap.getIndexPrice()));
            accountDebitAssetPremium.setPremiumRate(marketSnap.getPremiumRate());
            accountDebitAssetPremium.setPremiumFeeRate(marketSnap.getPremiumRate().divide(BigDecimal.valueOf(10)));
            accountDebitAssetPremium.setPremiumFee(premiumfee);
            accountDebitAssetPremium.setUpdateDate(new Date());
            logger.debug("accountDebitAssetPremium:" + accountDebitAssetPremium.toString());
            accountDebitAssetPremiumService.insert(accountDebitAssetPremium);
        }
        catch (Exception e)
        {
            logger.error("持仓调节失败：" + e.getLocalizedMessage());
        }
    }
}

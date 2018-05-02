/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.trade.fund.entity.AccountDebitAssetPremium;
import com.blocain.bitms.trade.fund.entity.MarketSnap;
import com.blocain.bitms.trade.fund.model.AccountPremiumAssetModel;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户持仓调节(溢价费)记录表 服务接口
 * <p>File：AccountDebitAssetPremiumService.java </p>
 * <p>Title: AccountDebitAssetPremiumService </p>
 * <p>Description:AccountDebitAssetPremiumService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface AccountDebitAssetPremiumService extends GenericService<AccountDebitAssetPremium>
{
    /**
     * 获取多头用户资产和负债
     * @param relatedStockinfoId
     * @param stockinfoId
     * @return
     */
    List<AccountPremiumAssetModel> getPremiumLongAccountAsset(Long relatedStockinfoId, Long stockinfoId);
    
    /**
     * 获取空头用户资产和负债
     * @param relatedStockinfoId
     * @param stockinfoId
     * @return
     */
    List<AccountPremiumAssetModel> getPremiumShortAccountAsset(Long relatedStockinfoId, Long stockinfoId);
    
    /**
     * 行情快照
     */
    void doMarketSnap();
    
    /**
     * 杠杆现货资产负债快照
     */
    void doLeveragedSpotAssetAndDebitSnap();

    /**
     * 纯正现货资产快照
     */
    void doPureSpotAssetSnap();

    /**
     * 自动计算溢价
     */
    void autoPremium();

    /**
     * 计算溢价
     */
    void doPremium(MarketSnap marketSnap ,AccountPremiumAssetModel accountPremiumAssetModel,int direction,BigDecimal premiumfee);
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.risk.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>File：EnableModel.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月22日 上午10:35:44</p>
 * <p>Company: BloCain</p>
 * @author sunbiao
 * @version 1.0
 */
public class EnableModel implements Serializable
{
    //
    private static final long serialVersionUID = 6541545936709826211L;
    
    // 帐户ID
    public Long               accountId;
    
    // 业务类别
    public String             businessFlag;
    
    // 证券信息ID
    public Long               stockinfoId;
    
    // 关联证券信息ID
    private Long              relatedStockinfoId;
    
    // 当前可用数量(真实可用)
    public BigDecimal         enableAmount;
    
    // 当前冻结数量
    public BigDecimal         frozenAmt;
    
    // 做多最大杠杆
    private BigDecimal        maxLongLever;
    
    // 做空最大杠杆
    private BigDecimal        maxShortLever;
    
    // 当前可用数量（余额-冻结）
    public BigDecimal         enableAmountEx;
    
    private Long              originalBusinessId;
    
    public Long getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public String getBusinessFlag()
    {
        return businessFlag;
    }
    
    public void setBusinessFlag(String businessFlag)
    {
        this.businessFlag = businessFlag;
    }
    
    public Long getStockinfoId()
    {
        return stockinfoId;
    }
    
    public void setStockinfoId(Long stockinfoId)
    {
        this.stockinfoId = stockinfoId;
    }
    
    public Long getRelatedStockinfoId()
    {
        return relatedStockinfoId;
    }
    
    public void setRelatedStockinfoId(Long relatedStockinfoId)
    {
        this.relatedStockinfoId = relatedStockinfoId;
    }
    
    public BigDecimal getEnableAmount()
    {
        return enableAmount;
    }
    
    public void setEnableAmount(BigDecimal enableAmount)
    {
        this.enableAmount = enableAmount;
    }
    
    public BigDecimal getFrozenAmt()
    {
        return frozenAmt;
    }
    
    public void setFrozenAmt(BigDecimal frozenAmt)
    {
        this.frozenAmt = frozenAmt;
    }
    
    public BigDecimal getMaxLongLever()
    {
        return maxLongLever;
    }
    
    public void setMaxLongLever(BigDecimal maxLongLever)
    {
        this.maxLongLever = maxLongLever;
    }
    
    public BigDecimal getMaxShortLever()
    {
        return maxShortLever;
    }
    
    public void setMaxShortLever(BigDecimal maxShortLever)
    {
        this.maxShortLever = maxShortLever;
    }
    
    public BigDecimal getEnableAmountEx()
    {
        return enableAmountEx;
    }
    
    public void setEnableAmountEx(BigDecimal enableAmountEx)
    {
        this.enableAmountEx = enableAmountEx;
    }
    
    public Long getOriginalBusinessId()
    {
        return originalBusinessId;
    }
    
    public void setOriginalBusinessId(Long originalBusinessId)
    {
        this.originalBusinessId = originalBusinessId;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("EnableModel{");
        sb.append("accountId=").append(accountId);
        sb.append(", businessFlag='").append(businessFlag).append('\'');
        sb.append(", stockinfoId=").append(stockinfoId);
        sb.append(", relatedStockinfoId=").append(relatedStockinfoId);
        sb.append(", enableAmount=").append(enableAmount);
        sb.append(", frozenAmt=").append(frozenAmt);
        sb.append(", maxLongLever=").append(maxLongLever);
        sb.append(", maxShortLever=").append(maxShortLever);
        sb.append(", enableAmountEx=").append(enableAmountEx);
        sb.append('}');
        return sb.toString();
    }
}

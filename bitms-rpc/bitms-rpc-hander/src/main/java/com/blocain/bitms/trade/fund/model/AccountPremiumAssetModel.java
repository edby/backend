/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.model;

import com.blocain.bitms.orm.core.GenericEntity;

import java.math.BigDecimal;

/**
 * 用户账户资产和负债实体 (快照)
 * <p>File：AccountPremiumAssetModel.java</p>
 * <p>Title: AccountPremiumAssetModel</p>
 * <p>Description:AccountPremiumAssetModel</p>
 * <p>Copyright: Copyright (c) 2017年7月19日</p>
 * <p>Company: BloCain</p>
 *
 * @version 1.0
 */
public class AccountPremiumAssetModel extends GenericEntity
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    
    private Long              id;                   // 资产表ID
    
    private Long              accountId;
    
    private Long              stockinfoId;
    
    private Long              relatedStockinfoId;
    
    private BigDecimal        amount;
    
    private BigDecimal        frozenAmt;
    
    private BigDecimal        debitAmt;
    
    @Override
    public Long getId()
    {
        return id;
    }
    
    @Override
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public Long getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
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
    
    public BigDecimal getAmount()
    {
        return amount;
    }
    
    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }
    
    public BigDecimal getFrozenAmt()
    {
        return frozenAmt;
    }
    
    public void setFrozenAmt(BigDecimal frozenAmt)
    {
        this.frozenAmt = frozenAmt;
    }
    
    public BigDecimal getDebitAmt()
    {
        return debitAmt;
    }
    
    public void setDebitAmt(BigDecimal debitAmt)
    {
        this.debitAmt = debitAmt;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("AccountPremiumAssetModel{");
        sb.append("id=").append(id);
        sb.append(", accountId=").append(accountId);
        sb.append(", stockinfoId=").append(stockinfoId);
        sb.append(", relatedStockinfoId=").append(relatedStockinfoId);
        sb.append(", amount=").append(amount);
        sb.append(", frozenAmt=").append(frozenAmt);
        sb.append(", debitAmt=").append(debitAmt);
        sb.append('}');
        return sb.toString();
    }
}

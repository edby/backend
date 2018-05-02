/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>File：DealModel.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月22日 上午10:35:44</p>
 * <p>Company: BloCain</p>
 * @author sunbiao
 * @version 1.0
 */
public class ContractAssetModel implements Serializable
{
    //
    private static final long serialVersionUID = 6541545936709826221L;
    
    private Long              accountId;
    
    /** 仅用于查个人时为个人账户余额 */
    public BigDecimal         sumAmount;
    
    /** 仅用于查个人时为个人账户冻结 */
    public BigDecimal         sumFrozenAmt;
    
    /** 仅用于查个人时为个人账户期初余额 */
    public BigDecimal         sumInitialAmt;
    
    /** 仅用于查个人时为个人账户流入金额 */
    public BigDecimal         sumFlowInAmt;
    
    /** 仅用于查个人时为个人账户流出金额 */
    public BigDecimal         sumFlowOutAmt;
    
    /**
     * 仅用于查询全局用户(超级用户除外)总盈利 只累计正数
     */
    public BigDecimal         sumProfit;
    
    public BigDecimal getSumAmount()
    {
        return sumAmount;
    }
    
    public void setSumAmount(BigDecimal sumAmount)
    {
        this.sumAmount = sumAmount;
    }
    
    public BigDecimal getSumFrozenAmt()
    {
        return sumFrozenAmt;
    }
    
    public void setSumFrozenAmt(BigDecimal sumFrozenAmt)
    {
        this.sumFrozenAmt = sumFrozenAmt;
    }
    
    public BigDecimal getSumInitialAmt()
    {
        return sumInitialAmt;
    }
    
    public void setSumInitialAmt(BigDecimal sumInitialAmt)
    {
        this.sumInitialAmt = sumInitialAmt;
    }
    
    public BigDecimal getSumFlowInAmt()
    {
        return sumFlowInAmt;
    }
    
    public void setSumFlowInAmt(BigDecimal sumFlowInAmt)
    {
        this.sumFlowInAmt = sumFlowInAmt;
    }
    
    public BigDecimal getSumFlowOutAmt()
    {
        return sumFlowOutAmt;
    }
    
    public void setSumFlowOutAmt(BigDecimal sumFlowOutAmt)
    {
        this.sumFlowOutAmt = sumFlowOutAmt;
    }
    
    public BigDecimal getSumProfit()
    {
        return sumProfit;
    }
    
    public void setSumProfit(BigDecimal sumProfit)
    {
        this.sumProfit = sumProfit;
    }
    
    public Long getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("ContractAssetModel{");
        sb.append("accountId=").append(accountId);
        sb.append(", sumAmount=").append(sumAmount);
        sb.append(", sumFrozenAmt=").append(sumFrozenAmt);
        sb.append(", sumInitialAmt=").append(sumInitialAmt);
        sb.append(", sumFlowInAmt=").append(sumFlowInAmt);
        sb.append(", sumFlowOutAmt=").append(sumFlowOutAmt);
        sb.append(", sumProfit=").append(sumProfit);
        sb.append('}');
        return sb.toString();
    }
}

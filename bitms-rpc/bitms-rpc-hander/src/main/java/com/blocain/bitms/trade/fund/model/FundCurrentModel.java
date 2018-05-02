/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>File：FundCurrentModel.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月21日 上午10:35:44</p>
 * <p>Company: BloCain</p>
 * @author sunbiao
 * @version 1.0
 */
public class FundCurrentModel implements Serializable
{
    //
    private static final long serialVersionUID = 6541545936709826208L;
    
    // 帐户ID
    public Long               accountId;
    
    // 账户资产类型(钱包账户资产、合约账户资产)
    private String            accountAssetType;
    
    // 账户资产ID(钱包账户资产或合约账户资产对应的ID)
    private Long            accountAssetId;
    
    // 业务类别
    public String             businessFlag;
    
    // 证券信息ID
    public Long               stockinfoId;
    
    // 原资产数量余额
    private BigDecimal        orgAmt;
    
    // 发生数量
    private BigDecimal        occurAmt;
    
    // 发生数量对应费用
    private BigDecimal        fee;
    
    public Long getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public String getAccountAssetType()
    {
        return accountAssetType;
    }
    
    public void setAccountAssetType(String accountAssetType)
    {
        this.accountAssetType = accountAssetType;
    }
    
    public Long getAccountAssetId()
    {
        return accountAssetId;
    }
    
    public void setAccountAssetId(Long accountAssetId)
    {
        this.accountAssetId = accountAssetId;
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
    
    public BigDecimal getOrgAmt()
    {
        return orgAmt;
    }
    
    public void setOrgAmt(BigDecimal orgAmt)
    {
        this.orgAmt = orgAmt;
    }
    
    public BigDecimal getOccurAmt()
    {
        return occurAmt;
    }
    
    public void setOccurAmt(BigDecimal occurAmt)
    {
        this.occurAmt = occurAmt;
    }
    
    public BigDecimal getFee()
    {
        return fee;
    }
    
    public void setFee(BigDecimal fee)
    {
        this.fee = fee;
    }

    @Override public String toString()
    {
        final StringBuilder sb = new StringBuilder("FundCurrentModel{");
        sb.append("accountId=").append(accountId);
        sb.append(", accountAssetType='").append(accountAssetType).append('\'');
        sb.append(", accountAssetId=").append(accountAssetId);
        sb.append(", businessFlag='").append(businessFlag).append('\'');
        sb.append(", stockinfoId=").append(stockinfoId);
        sb.append(", orgAmt=").append(orgAmt);
        sb.append(", occurAmt=").append(occurAmt);
        sb.append(", fee=").append(fee);
        sb.append('}');
        return sb.toString();
    }
}

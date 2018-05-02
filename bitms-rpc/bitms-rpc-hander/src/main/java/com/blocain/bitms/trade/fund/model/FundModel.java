/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>File：FundModel.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月11日 上午10:35:44</p>
 * <p>Company: BloCain</p>
 * @author sunbiao
 * @version 1.0
 */
public class FundModel implements Serializable
{
    //
    private static final long serialVersionUID = 6541545936709826208L;
    
    // 帐户ID
    public Long               accountId;
    
    // 业务类别
    public String             businessFlag;
    
    // 证券信息ID
    public Long               stockinfoId;
    
    // 交易数量
    @Min(0)
    @Max(999999)
    public BigDecimal         amount;
    
    // 手续费
    @Min(0)
    @Max(999)
    public BigDecimal         fee;
    
    // 交易ID
    public String             transId;
    
    // 钱包地址
    public String             address;
    
    // 创建人
    public Long               createBy;
    
    // 证券信息ID扩展
    public Long               stockinfoIdEx;
    
    // 交易数量扩展
    @Min(0)
    @Max(999999)
    public BigDecimal         amountEx;
    
    // 账户资金流水记录id
    public Long               accountFundCurrentId;
    
    /** 持仓手续费方向 */
    public int                direction;
    
    // 原始业务id
    public Long               originalBusinessId;
    
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
    
    public BigDecimal getAmount()
    {
        return amount;
    }
    
    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }
    
    public BigDecimal getFee()
    {
        return fee;
    }
    
    public void setFee(BigDecimal fee)
    {
        this.fee = fee;
    }
    
    public String getTransId()
    {
        return transId;
    }
    
    public void setTransId(String transId)
    {
        this.transId = transId;
    }
    
    public String getAddress()
    {
        return address;
    }
    
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    public Long getCreateBy()
    {
        return createBy;
    }
    
    public void setCreateBy(Long createBy)
    {
        this.createBy = createBy;
    }
    
    public Long getStockinfoIdEx()
    {
        return stockinfoIdEx;
    }
    
    public void setStockinfoIdEx(Long stockinfoIdEx)
    {
        this.stockinfoIdEx = stockinfoIdEx;
    }
    
    public BigDecimal getAmountEx()
    {
        return amountEx;
    }
    
    public void setAmountEx(BigDecimal amountEx)
    {
        this.amountEx = amountEx;
    }
    
    public Long getAccountFundCurrentId()
    {
        return accountFundCurrentId;
    }
    
    public void setAccountFundCurrentId(Long accountFundCurrentId)
    {
        this.accountFundCurrentId = accountFundCurrentId;
    }
    
    public Long getOriginalBusinessId()
    {
        return originalBusinessId;
    }
    
    public void setOriginalBusinessId(Long originalBusinessId)
    {
        this.originalBusinessId = originalBusinessId;
    }
    
    public int getDirection()
    {
        return direction;
    }
    
    public void setDirection(int direction)
    {
        this.direction = direction;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("FundModel{");
        sb.append("accountId=").append(accountId);
        sb.append(", businessFlag='").append(businessFlag).append('\'');
        sb.append(", stockinfoId=").append(stockinfoId);
        sb.append(", amount=").append(amount);
        sb.append(", fee=").append(fee);
        sb.append(", transId='").append(transId).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", createBy=").append(createBy);
        sb.append(", stockinfoIdEx=").append(stockinfoIdEx);
        sb.append(", amountEx=").append(amountEx);
        sb.append(", accountFundCurrentId=").append(accountFundCurrentId);
        sb.append(", originalBusinessId=").append(originalBusinessId);
        sb.append('}');
        return sb.toString();
    }
}

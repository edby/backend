/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.entity;

import com.blocain.bitms.orm.core.GenericEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 现货账户资产表 实体对象
 * <p>File：AccountSpotAsset.java</p>
 * <p>Title: AccountSpotAsset</p>
 * <p>Description:AccountSpotAsset</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class AccountSpotAsset extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**账户ID*/
    @NotNull(message = "账户ID不可为空")
    private Long                 accountId;
    
    /**证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "证券信息id 对应Stockinfo表中的ID字段不可为空")
    private Long                 stockinfoId;
    
    /**关联证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "关联证券信息id 对应Stockinfo表中的ID字段")
    private Long                 relatedStockinfoId;
    
    /**方向(多头Long,空头Short)*/
    @NotNull(message = "方向不可为空")
    private String               direction        = "Long";
    
    /**价格*/
    @NotNull(message = "价格不可为空")
    private java.math.BigDecimal price            = BigDecimal.ONE;
    
    /**数量*/
    @NotNull(message = "数量不可为空")
    private java.math.BigDecimal amount;
    
    /**冻结数量*/
    @NotNull(message = "冻结数量不可为空")
    private java.math.BigDecimal frozenAmt        = BigDecimal.ZERO;
    
    /**备注*/
    private String               remark;
    
    /**修改时间*/
    @NotNull(message = "修改时间不可为空")
    private java.sql.Timestamp   updateDate;
    
    private String               stockCode;
    
    private String               accountName;
    
    /**能否充值(yes可以 no不能)*/
    private java.lang.String     canRecharge;
    
    /**能否提现(yes可以 no不能)*/
    private java.lang.String     canWithdraw;
    
    /**能否交易(yes可以 no不能)*/
    private java.lang.String     canTrade;
    
    /**能否可借(yes可以 no不能)*/
    private java.lang.String     canBorrow;
    
    /**是否是交易对(yes是 no不是)*/
    private java.lang.String     isExchange;
    
    /**能否互转(yes可以 no不能)*/
    private java.lang.String     canConversion;
    
    /**借贷数量或金额*/
    @ApiModelProperty(value = "借贷数量或金额", required = true)
    private java.math.BigDecimal debitAmt;
    
    /**累计利息*/
    @ApiModelProperty(value = "累计利息", required = true)
    private java.math.BigDecimal accumulateInterest;
    
    public Long getAccountId()
    {
        return this.accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public Long getStockinfoId()
    {
        return this.stockinfoId;
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
    
    public String getDirection()
    {
        return direction;
    }
    
    public void setDirection(String direction)
    {
        this.direction = direction;
    }
    
    public java.math.BigDecimal getPrice()
    {
        return price;
    }
    
    public void setPrice(java.math.BigDecimal price)
    {
        this.price = price;
    }
    
    public java.math.BigDecimal getAmount()
    {
        return this.amount;
    }
    
    public void setAmount(java.math.BigDecimal amount)
    {
        this.amount = amount;
    }
    
    public java.math.BigDecimal getFrozenAmt()
    {
        return this.frozenAmt;
    }
    
    public void setFrozenAmt(java.math.BigDecimal frozenAmt)
    {
        this.frozenAmt = frozenAmt;
    }
    
    public String getRemark()
    {
        return this.remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public Timestamp getUpdateDate()
    {
        return updateDate;
    }
    
    public void setUpdateDate(Timestamp updateDate)
    {
        this.updateDate = updateDate;
    }
    
    public String getAccountName()
    {
        return accountName;
    }
    
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    
    public String getStockCode()
    {
        return stockCode;
    }
    
    public void setStockCode(String stockCode)
    {
        this.stockCode = stockCode;
    }
    
    public String getCanRecharge()
    {
        return canRecharge;
    }
    
    public void setCanRecharge(String canRecharge)
    {
        this.canRecharge = canRecharge;
    }
    
    public String getCanWithdraw()
    {
        return canWithdraw;
    }
    
    public void setCanWithdraw(String canWithdraw)
    {
        this.canWithdraw = canWithdraw;
    }
    
    public String getCanTrade()
    {
        return canTrade;
    }
    
    public void setCanTrade(String canTrade)
    {
        this.canTrade = canTrade;
    }
    
    public String getCanBorrow()
    {
        return canBorrow;
    }
    
    public void setCanBorrow(String canBorrow)
    {
        this.canBorrow = canBorrow;
    }
    
    public String getIsExchange()
    {
        return isExchange;
    }
    
    public void setIsExchange(String isExchange)
    {
        this.isExchange = isExchange;
    }
    
    public String getCanConversion()
    {
        return canConversion;
    }
    
    public void setCanConversion(String canConversion)
    {
        this.canConversion = canConversion;
    }
    
    public BigDecimal getDebitAmt()
    {
        return debitAmt;
    }
    
    public void setDebitAmt(BigDecimal debitAmt)
    {
        this.debitAmt = debitAmt;
    }
    
    public BigDecimal getAccumulateInterest()
    {
        return accumulateInterest;
    }
    
    public void setAccumulateInterest(BigDecimal accumulateInterest)
    {
        this.accumulateInterest = accumulateInterest;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("AccountSpotAsset{");
        sb.append("id=").append(id);
        sb.append(", accountId=").append(accountId);
        sb.append(", stockinfoId=").append(stockinfoId);
        sb.append(", relatedStockinfoId=").append(relatedStockinfoId);
        sb.append(", direction='").append(direction).append('\'');
        sb.append(", price=").append(price);
        sb.append(", amount=").append(amount);
        sb.append(", frozenAmt=").append(frozenAmt);
        sb.append(", remark='").append(remark).append('\'');
        sb.append(", updateDate=").append(updateDate);
        sb.append(", stockCode='").append(stockCode).append('\'');
        sb.append(", accountName='").append(accountName).append('\'');
        sb.append(", canRecharge='").append(canRecharge).append('\'');
        sb.append(", canWithdraw='").append(canWithdraw).append('\'');
        sb.append(", canTrade='").append(canTrade).append('\'');
        sb.append(", canBorrow='").append(canBorrow).append('\'');
        sb.append(", isExchange='").append(isExchange).append('\'');
        sb.append(", canConversion='").append(canConversion).append('\'');
        sb.append(", debitAmt=").append(debitAmt);
        sb.append(", accumulateInterest=").append(accumulateInterest);
        sb.append('}');
        return sb.toString();
    }
}

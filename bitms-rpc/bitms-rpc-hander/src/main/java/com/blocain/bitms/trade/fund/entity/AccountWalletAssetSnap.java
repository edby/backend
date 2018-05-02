/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.blocain.bitms.orm.core.GenericEntity;
import javax.validation.constraints.NotNull;

/**
 * 钱包账户资产快照表 实体对象
 * <p>File：AccountWalletAssetSnap.java</p>
 * <p>Title: AccountWalletAssetSnap</p>
 * <p>Description:AccountWalletAssetSnap</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "钱包账户资产快照表")
public class AccountWalletAssetSnap extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**账户ID*/
    @NotNull(message = "账户ID不可为空")
    @ApiModelProperty(value = "账户ID", required = true)
    private Long                 accountId;
    
    /**证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "证券信息id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "证券信息id 对应Stockinfo表中的ID字段", required = true)
    private Long                 stockinfoId;
    
    /**关联证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "关联证券信息id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "关联证券信息id 对应Stockinfo表中的ID字段", required = true)
    private Long                 relatedStockinfoId;
    
    /**方向(多头Long,空头Short)*/
    @NotNull(message = "方向(多头Long,空头Short)不可为空")
    @ApiModelProperty(value = "方向(多头Long,空头Short)", required = true)
    private String               direction;
    
    /**价格*/
    @NotNull(message = "价格不可为空")
    @ApiModelProperty(value = "价格", required = true)
    private java.math.BigDecimal price;
    
    /**当前数量*/
    @NotNull(message = "当前数量不可为空")
    @ApiModelProperty(value = "当前数量", required = true)
    private java.math.BigDecimal amount;
    
    /**冻结数量*/
    @NotNull(message = "冻结数量不可为空")
    @ApiModelProperty(value = "冻结数量", required = true)
    private java.math.BigDecimal frozenAmt;
    
    /**已成功充值累计数量*/
    @ApiModelProperty(value = "已成功充值累计数量")
    private java.math.BigDecimal chargedTotal;
    
    /**已成功提币累计数量*/
    @ApiModelProperty(value = "已成功提币累计数量")
    private java.math.BigDecimal withdrawedTotal;
    
    /**提币处理中累计数量*/
    @ApiModelProperty(value = "提币处理中累计数量")
    private java.math.BigDecimal withdrawingTotal;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String               remark;
    
    /**修改时间*/
    @NotNull(message = "修改时间不可为空")
    @ApiModelProperty(value = "修改时间", required = true)
    private java.util.Date       updateDate;
    
    private String               accountName;
    
    private String               stockCode;
    
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
        return this.relatedStockinfoId;
    }
    
    public void setRelatedStockinfoId(Long relatedStockinfoId)
    {
        this.relatedStockinfoId = relatedStockinfoId;
    }
    
    public String getDirection()
    {
        return this.direction;
    }
    
    public void setDirection(String direction)
    {
        this.direction = direction;
    }
    
    public java.math.BigDecimal getPrice()
    {
        return this.price;
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
    
    public java.math.BigDecimal getChargedTotal()
    {
        return this.chargedTotal;
    }
    
    public void setChargedTotal(java.math.BigDecimal chargedTotal)
    {
        this.chargedTotal = chargedTotal;
    }
    
    public java.math.BigDecimal getWithdrawedTotal()
    {
        return this.withdrawedTotal;
    }
    
    public void setWithdrawedTotal(java.math.BigDecimal withdrawedTotal)
    {
        this.withdrawedTotal = withdrawedTotal;
    }
    
    public java.math.BigDecimal getWithdrawingTotal()
    {
        return this.withdrawingTotal;
    }
    
    public void setWithdrawingTotal(java.math.BigDecimal withdrawingTotal)
    {
        this.withdrawingTotal = withdrawingTotal;
    }
    
    public String getRemark()
    {
        return this.remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public java.util.Date getUpdateDate()
    {
        return this.updateDate;
    }
    
    public void setUpdateDate(java.util.Date updateDate)
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
}

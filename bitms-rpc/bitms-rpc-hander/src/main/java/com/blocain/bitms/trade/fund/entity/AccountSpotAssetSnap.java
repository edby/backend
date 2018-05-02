/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.blocain.bitms.orm.core.GenericEntity;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 现货账户资产快照表 实体对象
 * <p>File：AccountSpotAssetSnap.java</p>
 * <p>Title: AccountSpotAssetSnap</p>
 * <p>Description:AccountSpotAssetSnap</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "现货账户资产快照表")
public class AccountSpotAssetSnap extends GenericEntity
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
    
    /**持仓调节处理标志(0未处理 1已处理)*/
    @NotNull(message = "持仓调节处理标志(0未处理 1已处理)不可为空")
    @ApiModelProperty(value = "持仓调节处理标志(0未处理 1已处理)", required = true)
    private java.math.BigDecimal premiumDealFlag;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String               remark;
    
    /**修改时间*/
    @NotNull(message = "修改时间不可为空")
    @ApiModelProperty(value = "修改时间", required = true)
    private java.util.Date       updateDate;
    
    private String               accountName;
    
    private String               stockCode;
    
    private String               areaCode;
    
    private BigDecimal           debitAmt;
    
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
    
    public java.math.BigDecimal getPremiumDealFlag()
    {
        return this.premiumDealFlag;
    }
    
    public void setPremiumDealFlag(java.math.BigDecimal premiumDealFlag)
    {
        this.premiumDealFlag = premiumDealFlag;
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
    
    public String getAreaCode()
    {
        return areaCode;
    }
    
    public void setAreaCode(String areaCode)
    {
        this.areaCode = areaCode;
    }
    
    public BigDecimal getDebitAmt()
    {
        return debitAmt;
    }
    
    public void setDebitAmt(BigDecimal debitAmt)
    {
        this.debitAmt = debitAmt;
    }
}

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
 * 账户持仓调节(溢价费)记录表 实体对象
 * <p>File：AccountDebitAssetPremium.java</p>
 * <p>Title: AccountDebitAssetPremium</p>
 * <p>Description:AccountDebitAssetPremium</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "账户持仓调节(溢价费)记录表")
public class AccountDebitAssetPremium extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**账户ID*/
    @NotNull(message = "账户ID不可为空")
    @ApiModelProperty(value = "账户ID", required = true)
    private Long                 accountId;
    
    private String               accountName;
    
    /**持仓调节证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "持仓调节证券信息id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "持仓调节证券信息id 对应Stockinfo表中的ID字段", required = true)
    private Long                 stockinfoId;
    
    /**持仓调节证券信息关联id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "持仓调节证券信息关联id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "持仓调节证券信息关联id 对应Stockinfo表中的ID字段", required = true)
    private Long                 relatedStockinfoId;
    
    /**资产数量或金额(总资产)*/
    @NotNull(message = "资产数量或金额(总资产)不可为空")
    @ApiModelProperty(value = "资产数量或金额(总资产)", required = true)
    private java.math.BigDecimal assetAmt;
    
    /**借贷数量或金额(总负债)*/
    @NotNull(message = "借贷数量或金额(总负债)不可为空")
    @ApiModelProperty(value = "借贷数量或金额(总负债)", required = true)
    private java.math.BigDecimal debitAmt;
    
    /**持仓数量或金额(总资产-总负债)*/
    @NotNull(message = "持仓数量或金额(总资产-总负债)不可为空")
    @ApiModelProperty(value = "持仓数量或金额(总资产-总负债)", required = true)
    private java.math.BigDecimal positionAmt;
    
    /**内部平台价格*/
    @NotNull(message = "内部平台价格不可为空")
    @ApiModelProperty(value = "内部平台价格", required = true)
    private java.math.BigDecimal paltformPrice;
    
    /**外部指数价格*/
    @NotNull(message = "外部指数价格不可为空")
    @ApiModelProperty(value = "外部指数价格", required = true)
    private java.math.BigDecimal indexPrice;
    
    /**溢价*/
    @NotNull(message = "溢价不可为空")
    @ApiModelProperty(value = "溢价", required = true)
    private java.math.BigDecimal premiumPrice;
    
    /**溢价率*/
    @NotNull(message = "溢价率不可为空")
    @ApiModelProperty(value = "溢价率", required = true)
    private java.math.BigDecimal premiumRate;
    
    /**溢价费率*/
    @NotNull(message = "溢价费率不可为空")
    @ApiModelProperty(value = "溢价费率", required = true)
    private java.math.BigDecimal premiumFeeRate;
    
    /**溢价费*/
    @NotNull(message = "溢价费不可为空")
    @ApiModelProperty(value = "溢价费", required = true)
    private java.math.BigDecimal premiumFee;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String               remark;
    
    /**修改时间*/
    @NotNull(message = "修改时间不可为空")
    @ApiModelProperty(value = "修改时间", required = true)
    private java.util.Date       updateDate;
    
    /**用户界面传值 查询开始时间 */
    private String               timeStart;
    
    /**用户界面传值 查询结束时间 */
    private String               timeEnd;
    
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
    
    public java.math.BigDecimal getAssetAmt()
    {
        return this.assetAmt;
    }
    
    public void setAssetAmt(java.math.BigDecimal assetAmt)
    {
        this.assetAmt = assetAmt;
    }
    
    public java.math.BigDecimal getDebitAmt()
    {
        return this.debitAmt;
    }
    
    public void setDebitAmt(java.math.BigDecimal debitAmt)
    {
        this.debitAmt = debitAmt;
    }
    
    public java.math.BigDecimal getPositionAmt()
    {
        return this.positionAmt;
    }
    
    public void setPositionAmt(java.math.BigDecimal positionAmt)
    {
        this.positionAmt = positionAmt;
    }
    
    public java.math.BigDecimal getPaltformPrice()
    {
        return this.paltformPrice;
    }
    
    public void setPaltformPrice(java.math.BigDecimal paltformPrice)
    {
        this.paltformPrice = paltformPrice;
    }
    
    public java.math.BigDecimal getIndexPrice()
    {
        return this.indexPrice;
    }
    
    public void setIndexPrice(java.math.BigDecimal indexPrice)
    {
        this.indexPrice = indexPrice;
    }
    
    public java.math.BigDecimal getPremiumPrice()
    {
        return this.premiumPrice;
    }
    
    public void setPremiumPrice(java.math.BigDecimal premiumPrice)
    {
        this.premiumPrice = premiumPrice;
    }
    
    public java.math.BigDecimal getPremiumRate()
    {
        return this.premiumRate;
    }
    
    public void setPremiumRate(java.math.BigDecimal premiumRate)
    {
        this.premiumRate = premiumRate;
    }
    
    public java.math.BigDecimal getPremiumFeeRate()
    {
        return this.premiumFeeRate;
    }
    
    public void setPremiumFeeRate(java.math.BigDecimal premiumFeeRate)
    {
        this.premiumFeeRate = premiumFeeRate;
    }
    
    public java.math.BigDecimal getPremiumFee()
    {
        return this.premiumFee;
    }
    
    public void setPremiumFee(java.math.BigDecimal premiumFee)
    {
        this.premiumFee = premiumFee;
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
    
    public String getTimeStart()
    {
        return timeStart;
    }
    
    public void setTimeStart(String timeStart)
    {
        this.timeStart = timeStart;
    }
    
    public String getTimeEnd()
    {
        return timeEnd;
    }
    
    public void setTimeEnd(String timeEnd)
    {
        this.timeEnd = timeEnd;
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

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
 * 账户糖果流水表 实体对象
 * <p>File：AccountCandyRecord.java</p>
 * <p>Title: AccountCandyRecord</p>
 * <p>Description:AccountCandyRecord</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "账户糖果流水表")
public class AccountCandyRecord extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**账户ID*/
    @NotNull(message = "账户ID不可为空")
    @ApiModelProperty(value = "账户ID", required = true)
    private Long                 accountId;
    
    /**账户资产类型(钱包账户资产、合约账户资产)*/
    @NotNull(message = "账户资产类型(钱包账户资产、合约账户资产)不可为空")
    @ApiModelProperty(value = "账户资产类型(钱包账户资产、合约账户资产)", required = true)
    private String               accountAssetType;
    
    /**账户资产ID(钱包账户资产或合约账户资产对应的ID)*/
    @NotNull(message = "账户资产ID(钱包账户资产或合约账户资产对应的ID)不可为空")
    @ApiModelProperty(value = "账户资产ID(钱包账户资产或合约账户资产对应的ID)", required = true)
    private Long                 accountAssetId;
    
    /**流水时间戳*/
    @NotNull(message = "流水时间戳不可为空")
    @ApiModelProperty(value = "流水时间戳", required = true)
    private java.util.Date       currentDate;
    
    /**业务类别(糖果发放等)*/
    @NotNull(message = "业务类别(糖果发放等)不可为空")
    @ApiModelProperty(value = "业务类别(糖果发放等)", required = true)
    private String               businessFlag;
    
    /**证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "证券信息id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "证券信息id 对应Stockinfo表中的ID字段", required = true)
    private Long                 stockinfoId;
    
    /**关联证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "关联证券信息id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "关联证券信息id 对应Stockinfo表中的ID字段", required = true)
    private Long                 relatedStockinfoId;
    
    /**资产发生方向(增加increase、减少decrease、冻结frozen、解冻unfrozen、解冻并减少frozenDecrease)*/
    @NotNull(message = "资产发生方向(增加increase、减少decrease、冻结frozen、解冻unfrozen、解冻并减少frozenDecrease)不可为空")
    @ApiModelProperty(value = "资产发生方向(增加increase、减少decrease、冻结frozen、解冻unfrozen、解冻并减少frozenDecrease)", required = true)
    private String               occurDirect;
    
    /**原资产当前数量余额*/
    @NotNull(message = "原资产当前数量余额不可为空")
    @ApiModelProperty(value = "原资产当前数量余额", required = true)
    private java.math.BigDecimal orgAmt;
    
    /**资产增加减少发生数量*/
    @NotNull(message = "资产增加减少发生数量不可为空")
    @ApiModelProperty(value = "资产增加减少发生数量", required = true)
    private java.math.BigDecimal occurAmt;
    
    /**最新资产当前数量余额*/
    @NotNull(message = "最新资产当前数量余额不可为空")
    @ApiModelProperty(value = "最新资产当前数量余额", required = true)
    private java.math.BigDecimal lastAmt;
    
    /**状态(有效effective、无效invalid)*/
    @NotNull(message = "状态(有效effective、无效invalid)不可为空")
    @ApiModelProperty(value = "状态(有效effective、无效invalid)", required = true)
    private String               status;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String               remark;
    
    private String               accountName;
    
    private String               stockCode;
    
    private String               timeStart;
    
    private String               timeEnd;
    
    public Long getAccountId()
    {
        return this.accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public String getAccountAssetType()
    {
        return this.accountAssetType;
    }
    
    public void setAccountAssetType(String accountAssetType)
    {
        this.accountAssetType = accountAssetType;
    }
    
    public Long getAccountAssetId()
    {
        return this.accountAssetId;
    }
    
    public void setAccountAssetId(Long accountAssetId)
    {
        this.accountAssetId = accountAssetId;
    }
    
    public java.util.Date getCurrentDate()
    {
        return this.currentDate;
    }
    
    public void setCurrentDate(java.util.Date currentDate)
    {
        this.currentDate = currentDate;
    }
    
    public String getBusinessFlag()
    {
        return this.businessFlag;
    }
    
    public void setBusinessFlag(String businessFlag)
    {
        this.businessFlag = businessFlag;
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
    
    public String getOccurDirect()
    {
        return this.occurDirect;
    }
    
    public void setOccurDirect(String occurDirect)
    {
        this.occurDirect = occurDirect;
    }
    
    public java.math.BigDecimal getOrgAmt()
    {
        return this.orgAmt;
    }
    
    public void setOrgAmt(java.math.BigDecimal orgAmt)
    {
        this.orgAmt = orgAmt;
    }
    
    public java.math.BigDecimal getOccurAmt()
    {
        return this.occurAmt;
    }
    
    public void setOccurAmt(java.math.BigDecimal occurAmt)
    {
        this.occurAmt = occurAmt;
    }
    
    public java.math.BigDecimal getLastAmt()
    {
        return this.lastAmt;
    }
    
    public void setLastAmt(java.math.BigDecimal lastAmt)
    {
        this.lastAmt = lastAmt;
    }
    
    public String getStatus()
    {
        return this.status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public String getRemark()
    {
        return this.remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
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
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.settlement.entity;

import com.blocain.bitms.orm.core.GenericEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 交割结算记录表 实体对象
 * <p>File：SettlementRecord.java</p>
 * <p>Title: SettlementRecord</p>
 * <p>Description:SettlementRecord</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "交割结算记录表")
public class SettlementRecord extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**本身证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "本身证券信息id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "本身证券信息id 对应Stockinfo表中的ID字段", required = true)
    private Long                 stockinfoId;
    
    private String               stockCode;
    
    /**关联证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "关联证券信息id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "关联证券信息id 对应Stockinfo表中的ID字段", required = true)
    private Long                 relatedStockinfoId;
    
    /**交割结算类型 1交割、2结算*/
    @ApiModelProperty(value = "交割结算类型 1交割、2结算")
    private Integer              settlementType   = 1;
    
    /**交割结算时间*/
    @NotNull(message = "交割结算时间不可为空")
    @ApiModelProperty(value = "交割结算时间", required = true)
    private java.util.Date       settlementTime;
    
    /**交割结算价格*/
    @NotNull(message = "交割结算价格不可为空")
    @ApiModelProperty(value = "交割结算价格", required = true)
    private java.math.BigDecimal settlementPrice;
    
    /**准备金分摊数量*/
    @NotNull(message = "准备金分摊数量不可为空")
    @ApiModelProperty(value = "准备金分摊数量", required = true)
    private java.math.BigDecimal reserveAllocatAmt;
    
    /**准备金原始数量*/
    @NotNull(message = "准备金原始数量不可为空")
    @ApiModelProperty(value = "准备金原始数量", required = true)
    private java.math.BigDecimal reserveOrgAmt;
    
    /**准备金最新数量*/
    @NotNull(message = "准备金最新数量不可为空")
    @ApiModelProperty(value = "准备金最新数量数量", required = true)
    private java.math.BigDecimal reserveLastAmt;
    
    /**穿仓用户亏损分摊数量*/
    @NotNull(message = "穿仓用户亏损分摊数量不可为空")
    @ApiModelProperty(value = "穿仓用户亏损分摊数量", required = true)
    private java.math.BigDecimal wearingSharingLossesAmt;
    
    /**分摊比例*/
    @NotNull(message = "分摊比例不可为空")
    @ApiModelProperty(value = "分摊比例", required = true)
    private java.math.BigDecimal assessmentRate;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String               remark;
    
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
    
    public Integer getSettlementType()
    {
        return this.settlementType;
    }
    
    public void setSettlementType(Integer settlementType)
    {
        this.settlementType = settlementType;
    }
    
    public java.util.Date getSettlementTime()
    {
        return this.settlementTime;
    }
    
    public void setSettlementTime(java.util.Date settlementTime)
    {
        this.settlementTime = settlementTime;
    }
    
    public java.math.BigDecimal getSettlementPrice()
    {
        return this.settlementPrice;
    }
    
    public void setSettlementPrice(java.math.BigDecimal settlementPrice)
    {
        this.settlementPrice = settlementPrice;
    }
    
    public java.math.BigDecimal getReserveAllocatAmt()
    {
        return this.reserveAllocatAmt;
    }
    
    public void setReserveAllocatAmt(java.math.BigDecimal reserveAllocatAmt)
    {
        this.reserveAllocatAmt = reserveAllocatAmt;
    }
    
    public java.math.BigDecimal getWearingSharingLossesAmt()
    {
        return this.wearingSharingLossesAmt;
    }
    
    public void setWearingSharingLossesAmt(java.math.BigDecimal wearingSharingLossesAmt)
    {
        this.wearingSharingLossesAmt = wearingSharingLossesAmt;
    }
    
    public java.math.BigDecimal getAssessmentRate()
    {
        return this.assessmentRate;
    }
    
    public void setAssessmentRate(java.math.BigDecimal assessmentRate)
    {
        this.assessmentRate = assessmentRate;
    }
    
    public String getRemark()
    {
        return this.remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public String getStockCode()
    {
        return stockCode;
    }
    
    public void setStockCode(String stockCode)
    {
        this.stockCode = stockCode;
    }
    
    public BigDecimal getReserveOrgAmt()
    {
        return reserveOrgAmt;
    }
    
    public void setReserveOrgAmt(BigDecimal reserveOrgAmt)
    {
        this.reserveOrgAmt = reserveOrgAmt;
    }
    
    public BigDecimal getReserveLastAmt()
    {
        return reserveLastAmt;
    }
    
    public void setReserveLastAmt(BigDecimal reserveLastAmt)
    {
        this.reserveLastAmt = reserveLastAmt;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("SettlementRecord{");
        sb.append("id=").append(id);
        sb.append(", stockinfoId=").append(stockinfoId);
        sb.append(", stockCode='").append(stockCode).append('\'');
        sb.append(", relatedStockinfoId=").append(relatedStockinfoId);
        sb.append(", settlementType=").append(settlementType);
        sb.append(", settlementTime=").append(settlementTime);
        sb.append(", settlementPrice=").append(settlementPrice);
        sb.append(", reserveAllocatAmt=").append(reserveAllocatAmt);
        sb.append(", reserveOrgAmt=").append(reserveOrgAmt);
        sb.append(", reserveLastAmt=").append(reserveLastAmt);
        sb.append(", wearingSharingLossesAmt=").append(wearingSharingLossesAmt);
        sb.append(", assessmentRate=").append(assessmentRate);
        sb.append(", remark='").append(remark).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.settlement.entity;

import com.blocain.bitms.orm.core.GenericEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 交割结算账户资产表 实体对象
 * <p>File：SettlementAccountAsset.java</p>
 * <p>Title: SettlementAccountAsset</p>
 * <p>Description:SettlementAccountAsset</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "交割结算账户资产表")
public class SettlementAccountAsset extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**账户ID*/
    @NotNull(message = "账户ID不可为空")
    @ApiModelProperty(value = "账户ID", required = true)
    private Long                 accountId;
    
    /**本身证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "本身证券信息id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "本身证券信息id 对应Stockinfo表中的ID字段", required = true)
    private Long                 stockinfoId;
    
    /**关联证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "关联证券信息id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "关联证券信息id 对应Stockinfo表中的ID字段", required = true)
    private Long                 relatedStockinfoId;
    
    /**交割结算类型 1交割、2结算*/
    @ApiModelProperty(value = "交割结算类型 1交割、2结算")
    private Integer              settlementType;
    
    /**交割结算时间*/
    @NotNull(message = "交割结算时间不可为空")
    @ApiModelProperty(value = "交割结算时间", required = true)
    private java.util.Date       settlementTime;
    
    /**交割结算价格*/
    @NotNull(message = "交割结算价格不可为空")
    @ApiModelProperty(value = "交割结算价格", required = true)
    private java.math.BigDecimal settlementPrice;
    
    /**期间期初数量*/
    @NotNull(message = "期间期初数量不可为空")
    @ApiModelProperty(value = "期间期初数量", required = true)
    private java.math.BigDecimal periodInitAmt;
    
    /**期间流入数量*/
    @NotNull(message = "期间流入数量不可为空")
    @ApiModelProperty(value = "期间流入数量", required = true)
    private java.math.BigDecimal periodInflowAmt;
    
    /**期间流出数量*/
    @NotNull(message = "期间流出数量不可为空")
    @ApiModelProperty(value = "期间流出数量", required = true)
    private java.math.BigDecimal periodOutflowAmt;
    
    /**期间最后数量*/
    @NotNull(message = "期间最后数量不可为空")
    @ApiModelProperty(value = "期间最后数量", required = true)
    private java.math.BigDecimal periodLastAmt;
    
    /**期间分摊数量*/
    @NotNull(message = "期间分摊数量不可为空")
    @ApiModelProperty(value = "期间分摊数量", required = true)
    private java.math.BigDecimal periodAssessmentAmt;
    
    /**期间分摊比例*/
    @NotNull(message = "期间分摊比例不可为空")
    @ApiModelProperty(value = "期间分摊比例", required = true)
    private java.math.BigDecimal periodAssessmentRate;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String               remark;
    
    /**账户名*/
    private String               tableAsset;
    
    /**账户名*/
    private String               accountName;
    
    /**证券代码*/
    private String               stockCode;
    
    /**用户界面传值 查询开始时间 */
    private String               timeStart;
    
    /**用户界面传值 查询结束时间 */
    private String               timeEnd;
    
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
    
    public java.math.BigDecimal getPeriodInitAmt()
    {
        return this.periodInitAmt;
    }
    
    public void setPeriodInitAmt(java.math.BigDecimal periodInitAmt)
    {
        this.periodInitAmt = periodInitAmt;
    }
    
    public java.math.BigDecimal getPeriodInflowAmt()
    {
        return this.periodInflowAmt;
    }
    
    public void setPeriodInflowAmt(java.math.BigDecimal periodInflowAmt)
    {
        this.periodInflowAmt = periodInflowAmt;
    }
    
    public java.math.BigDecimal getPeriodOutflowAmt()
    {
        return this.periodOutflowAmt;
    }
    
    public void setPeriodOutflowAmt(java.math.BigDecimal periodOutflowAmt)
    {
        this.periodOutflowAmt = periodOutflowAmt;
    }
    
    public java.math.BigDecimal getPeriodLastAmt()
    {
        return this.periodLastAmt;
    }
    
    public void setPeriodLastAmt(java.math.BigDecimal periodLastAmt)
    {
        this.periodLastAmt = periodLastAmt;
    }
    
    public java.math.BigDecimal getPeriodAssessmentAmt()
    {
        return this.periodAssessmentAmt;
    }
    
    public void setPeriodAssessmentAmt(java.math.BigDecimal periodAssessmentAmt)
    {
        this.periodAssessmentAmt = periodAssessmentAmt;
    }
    
    public java.math.BigDecimal getPeriodAssessmentRate()
    {
        return this.periodAssessmentRate;
    }
    
    public void setPeriodAssessmentRate(java.math.BigDecimal periodAssessmentRate)
    {
        this.periodAssessmentRate = periodAssessmentRate;
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
    
    public String getTableAsset()
    {
        return tableAsset;
    }
    
    public void setTableAsset(String tableAsset)
    {
        this.tableAsset = tableAsset;
    }
}

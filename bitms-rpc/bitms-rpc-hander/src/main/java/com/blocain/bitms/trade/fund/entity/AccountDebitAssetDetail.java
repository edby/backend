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
 * 账户借贷资产明细表 实体对象
 * <p>File：AccountDebitAssetDetail.java</p>
 * <p>Title: AccountDebitAssetDetail</p>
 * <p>Description:AccountDebitAssetDetail</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "账户借贷资产明细表")
public class AccountDebitAssetDetail extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**借款人账户ID*/
    @NotNull(message = "借款人账户ID不可为空")
    @ApiModelProperty(value = "借款人账户ID", required = true)
    private Long                 borrowerAccountId;
    
    /**贷款人账户ID*/
    @NotNull(message = "贷款人账户ID不可为空")
    @ApiModelProperty(value = "贷款人账户ID", required = true)
    private Long                 lenderAccountId;
    
    /**借贷证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "借贷证券信息id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "借贷证券信息id 对应Stockinfo表中的ID字段", required = true)
    private Long                 stockinfoId;
    
    /**借贷证券信息关联id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "借贷证券信息关联id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "借贷证券信息关联id 对应Stockinfo表中的ID字段", required = true)
    private Long                 relatedStockinfoId;
    
    /**借贷数量或金额余额(总负债)*/
    @NotNull(message = "借贷数量或金额余额(总负债)不可为空")
    @ApiModelProperty(value = "借贷数量或金额余额(总负债)", required = true)
    private java.math.BigDecimal debitAmt;
    
    /**借贷数量或金额余额(有效负债)*/
    @NotNull(message = "借贷数量或金额余额(有效负债)不可为空")
    @ApiModelProperty(value = "借贷数量或金额余额(有效负债)", required = true)
    private java.math.BigDecimal effectiveDebitAmt;
    
    /**借贷日利率*/
    @NotNull(message = "借贷日利率不可为空")
    @ApiModelProperty(value = "借贷日利率", required = true)
    private java.math.BigDecimal borrowDayRate;
    
    /**当日利息*/
    @NotNull(message = "当日利息不可为空")
    @ApiModelProperty(value = "当日利息", required = true)
    private java.math.BigDecimal dayInterest;
    
    /**累计利息*/
    @NotNull(message = "累计利息不可为空")
    @ApiModelProperty(value = "累计利息", required = true)
    private java.math.BigDecimal accumulateInterest;
    
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
    
    /**对应表 */
    private String               tableName;
    
    /**证券代码 */
    private String               stockCode;
    
    /**交易对或专区 */
    private String               tradingZone;
    
    public Long getBorrowerAccountId()
    {
        return this.borrowerAccountId;
    }
    
    public void setBorrowerAccountId(Long borrowerAccountId)
    {
        this.borrowerAccountId = borrowerAccountId;
    }
    
    public Long getLenderAccountId()
    {
        return this.lenderAccountId;
    }
    
    public void setLenderAccountId(Long lenderAccountId)
    {
        this.lenderAccountId = lenderAccountId;
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
    
    public java.math.BigDecimal getDebitAmt()
    {
        return this.debitAmt;
    }
    
    public void setDebitAmt(java.math.BigDecimal debitAmt)
    {
        this.debitAmt = debitAmt;
    }
    
    public java.math.BigDecimal getEffectiveDebitAmt()
    {
        return this.effectiveDebitAmt;
    }
    
    public void setEffectiveDebitAmt(java.math.BigDecimal effectiveDebitAmt)
    {
        this.effectiveDebitAmt = effectiveDebitAmt;
    }
    
    public java.math.BigDecimal getBorrowDayRate()
    {
        return this.borrowDayRate;
    }
    
    public void setBorrowDayRate(java.math.BigDecimal borrowDayRate)
    {
        this.borrowDayRate = borrowDayRate;
    }
    
    public java.math.BigDecimal getDayInterest()
    {
        return this.dayInterest;
    }
    
    public void setDayInterest(java.math.BigDecimal dayInterest)
    {
        this.dayInterest = dayInterest;
    }
    
    public java.math.BigDecimal getAccumulateInterest()
    {
        return this.accumulateInterest;
    }
    
    public void setAccumulateInterest(java.math.BigDecimal accumulateInterest)
    {
        this.accumulateInterest = accumulateInterest;
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
    
    public String getTableName()
    {
        return tableName;
    }
    
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
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
    
    public String getTradingZone()
    {
        return tradingZone;
    }
    
    public void setTradingZone(String tradingZone)
    {
        this.tradingZone = tradingZone;
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

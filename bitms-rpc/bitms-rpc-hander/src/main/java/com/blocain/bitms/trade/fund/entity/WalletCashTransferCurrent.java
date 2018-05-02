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
 * 外部钱包现金转账流水表 实体对象
 * <p>File：WalletCashTransferCurrent.java</p>
 * <p>Title: WalletCashTransferCurrent</p>
 * <p>Description:WalletCashTransferCurrent</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "外部钱包现金转账流水表")
public class WalletCashTransferCurrent extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**流水时间戳*/
    @NotNull(message = "流水时间戳不可为空")
    @ApiModelProperty(value = "流水时间戳", required = true)
    private java.util.Date       currentDate;
    
    /**证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "证券信息id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "证券信息id 对应Stockinfo表中的ID字段", required = true)
    private Long                 stockinfoId;
    
    /**资产发生方向(增加increase、减少decrease)*/
    @NotNull(message = "资产发生方向(增加increase、减少decrease)不可为空")
    @ApiModelProperty(value = "资产发生方向(增加increase、减少decrease)", required = true)
    private String               occurDirect;
    
    /**原资产当前数量余额*/
    @NotNull(message = "原资产当前数量余额不可为空")
    @ApiModelProperty(value = "原资产当前数量余额", required = true)
    private java.math.BigDecimal orgAmt;
    
    /**资产发生数量*/
    @NotNull(message = "资产发生数量不可为空")
    @ApiModelProperty(value = "资产发生数量", required = true)
    private java.math.BigDecimal occurAmt;
    
    /**最新资产当前数量余额*/
    @NotNull(message = "最新资产当前数量余额不可为空")
    @ApiModelProperty(value = "最新资产当前数量余额", required = true)
    private java.math.BigDecimal lastAmt;
    
    /**交易ID*/
    @ApiModelProperty(value = "交易ID")
    private String               transId;
    
    /**转账费用*/
    @ApiModelProperty(value = "转账费用")
    private java.math.BigDecimal fee;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String               remark;
    
    /**创建人*/
    @ApiModelProperty(value = "创建人")
    private Long                 createBy;
    
    /**创建时间*/
    @NotNull(message = "创建时间不可为空")
    @ApiModelProperty(value = "创建时间", required = true)
    private java.util.Date       createDate;
    
    private String               accountName;
    
    private String               stockCode;
    
    public java.util.Date getCurrentDate()
    {
        return this.currentDate;
    }
    
    public void setCurrentDate(java.util.Date currentDate)
    {
        this.currentDate = currentDate;
    }
    
    public Long getStockinfoId()
    {
        return this.stockinfoId;
    }
    
    public void setStockinfoId(Long stockinfoId)
    {
        this.stockinfoId = stockinfoId;
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
    
    public String getTransId()
    {
        return this.transId;
    }
    
    public void setTransId(String transId)
    {
        this.transId = transId;
    }
    
    public java.math.BigDecimal getFee()
    {
        return this.fee;
    }
    
    public void setFee(java.math.BigDecimal fee)
    {
        this.fee = fee;
    }
    
    public String getRemark()
    {
        return this.remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public Long getCreateBy()
    {
        return this.createBy;
    }
    
    public void setCreateBy(Long createBy)
    {
        this.createBy = createBy;
    }
    
    public java.util.Date getCreateDate()
    {
        return this.createDate;
    }
    
    public void setCreateDate(java.util.Date createDate)
    {
        this.createDate = createDate;
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

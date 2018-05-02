/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.settlement.entity;

import javax.validation.constraints.NotNull;

import com.blocain.bitms.orm.core.GenericEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 平仓操作日志表 实体对象
 * <p>File：ClosePositionLog.java</p>
 * <p>Title: ClosePositionLog</p>
 * <p>Description:ClosePositionLog</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "平仓操作日志表")
public class ClosePositionLog extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**借款ID*/
    @NotNull(message = "借款ID不可为空")
    @ApiModelProperty(value = "借款ID", required = true)
    private Long                 debitId;
    
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
    
    /**借贷数量或金额*/
    @NotNull(message = "借贷数量或金额不可为空")
    @ApiModelProperty(value = "借贷数量或金额", required = true)
    private java.math.BigDecimal debitAmt;
    
    /**监控保证金比例(乘以100)*/
    @NotNull(message = "监控保证金比例(乘以100)不可为空")
    @ApiModelProperty(value = "监控保证金比例(乘以100)", required = true)
    private java.math.BigDecimal monitorMarginRatio;
    
    /**监控时间*/
    @NotNull(message = "监控时间不可为空")
    @ApiModelProperty(value = "监控时间", required = true)
    private java.util.Date       monitorDate;
    
    /**最新监控行情价格*/
    @ApiModelProperty(value = "最新监控行情价格")
    private java.math.BigDecimal monitorLastPrice;
    
    /**最新平仓行情价格*/
    @ApiModelProperty(value = "最新平仓行情价格")
    private java.math.BigDecimal lastPrice;
    
    /**平仓状态 0正常 1异常*/
    @NotNull(message = "平仓状态 0正常 1异常不可为空")
    @ApiModelProperty(value = "平仓状态 0正常 1异常", required = true)
    private Boolean              status;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String               remark;
    
    /**修改时间*/
    @NotNull(message = "修改时间不可为空")
    @ApiModelProperty(value = "修改时间", required = true)
    private java.util.Date       updateDate;
    
    public Long getDebitId()
    {
        return this.debitId;
    }
    
    public void setDebitId(Long debitId)
    {
        this.debitId = debitId;
    }
    
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
    
    public java.math.BigDecimal getDebitAmt()
    {
        return this.debitAmt;
    }
    
    public void setDebitAmt(java.math.BigDecimal debitAmt)
    {
        this.debitAmt = debitAmt;
    }
    
    public java.math.BigDecimal getMonitorMarginRatio()
    {
        return this.monitorMarginRatio;
    }
    
    public void setMonitorMarginRatio(java.math.BigDecimal monitorMarginRatio)
    {
        this.monitorMarginRatio = monitorMarginRatio;
    }
    
    public java.util.Date getMonitorDate()
    {
        return this.monitorDate;
    }
    
    public void setMonitorDate(java.util.Date monitorDate)
    {
        this.monitorDate = monitorDate;
    }
    
    public java.math.BigDecimal getMonitorLastPrice()
    {
        return this.monitorLastPrice;
    }
    
    public void setMonitorLastPrice(java.math.BigDecimal monitorLastPrice)
    {
        this.monitorLastPrice = monitorLastPrice;
    }
    
    public java.math.BigDecimal getLastPrice()
    {
        return this.lastPrice;
    }
    
    public void setLastPrice(java.math.BigDecimal lastPrice)
    {
        this.lastPrice = lastPrice;
    }
    
    public Boolean getStatus()
    {
        return this.status;
    }
    
    public void setStatus(Boolean status)
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
    
    public java.util.Date getUpdateDate()
    {
        return this.updateDate;
    }
    
    public void setUpdateDate(java.util.Date updateDate)
    {
        this.updateDate = updateDate;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("ClosePositionLog{");
        sb.append("id=").append(id);
        sb.append(", debitId=").append(debitId);
        sb.append(", borrowerAccountId=").append(borrowerAccountId);
        sb.append(", lenderAccountId=").append(lenderAccountId);
        sb.append(", stockinfoId=").append(stockinfoId);
        sb.append(", debitAmt=").append(debitAmt);
        sb.append(", monitorMarginRatio=").append(monitorMarginRatio);
        sb.append(", monitorDate=").append(monitorDate);
        sb.append(", monitorLastPrice=").append(monitorLastPrice);
        sb.append(", lastPrice=").append(lastPrice);
        sb.append(", status=").append(status);
        sb.append(", remark='").append(remark).append('\'');
        sb.append(", updateDate=").append(updateDate);
        sb.append('}');
        return sb.toString();
    }
}

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
 * 银行充值记录表 实体对象
 * <p>File：BankRecharge.java</p>
 * <p>Title: BankRecharge</p>
 * <p>Description:BankRecharge</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "银行充值记录表")
public class BankRecharge extends GenericEntity
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
    
    /**充值金额*/
    @NotNull(message = "充值金额不可为空")
    @ApiModelProperty(value = "充值金额", required = true)
    private java.math.BigDecimal amount;
    
    /**充值手续费*/
    @ApiModelProperty(value = "充值手续费")
    private java.math.BigDecimal fee;
    
    /**交易唯一ID*/
    @NotNull(message = "交易唯一ID不可为空")
    @ApiModelProperty(value = "交易唯一ID", required = true)
    private String               transId;
    
    /**状态()*/
    @NotNull(message = "状态()不可为空")
    @ApiModelProperty(value = "状态()", required = true)
    private String               status;
    
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
    
    /**审核人*/
    @ApiModelProperty(value = "审核人")
    private Long                 auditBy;
    
    /**审核时间*/
    @ApiModelProperty(value = "审核时间")
    private java.util.Date       auditDate;
    
    // 用户名称
    private String               accountName;
    
    private String               stockCode;
    
    private Long                 unid;

    /**姓氏*/
    private String               surname;

    /**名字*/
    private String               realname;
    
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
    
    public java.math.BigDecimal getAmount()
    {
        return this.amount;
    }
    
    public void setAmount(java.math.BigDecimal amount)
    {
        this.amount = amount;
    }
    
    public java.math.BigDecimal getFee()
    {
        return this.fee;
    }
    
    public void setFee(java.math.BigDecimal fee)
    {
        this.fee = fee;
    }
    
    public String getTransId()
    {
        return this.transId;
    }
    
    public void setTransId(String transId)
    {
        this.transId = transId;
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
    
    public Long getAuditBy()
    {
        return this.auditBy;
    }
    
    public void setAuditBy(Long auditBy)
    {
        this.auditBy = auditBy;
    }
    
    public java.util.Date getAuditDate()
    {
        return this.auditDate;
    }
    
    public void setAuditDate(java.util.Date auditDate)
    {
        this.auditDate = auditDate;
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
    
    public Long getUnid()
    {
        return unid;
    }
    
    public void setUnid(Long unid)
    {
        this.unid = unid;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("BankRecharge{");
        sb.append("id=").append(id);
        sb.append(", accountId=").append(accountId);
        sb.append(", stockinfoId=").append(stockinfoId);
        sb.append(", amount=").append(amount);
        sb.append(", fee=").append(fee);
        sb.append(", transId='").append(transId).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append(", remark='").append(remark).append('\'');
        sb.append(", createBy=").append(createBy);
        sb.append(", createDate=").append(createDate);
        sb.append(", auditBy=").append(auditBy);
        sb.append(", auditDate=").append(auditDate);
        sb.append(", accountName='").append(accountName).append('\'');
        sb.append(", stockCode='").append(stockCode).append('\'');
        sb.append(", unid=").append(unid);
        sb.append(", surname=").append(surname);
        sb.append(", realname=").append(realname);
        sb.append('}');
        return sb.toString();
    }
}

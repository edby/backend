/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.entity;

import com.blocain.bitms.orm.core.SignableEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;

/**
 * 账户资金提现记录表 实体对象
 * <p>File：AccountFundWithdraw.java</p>
 * <p>Title: AccountFundWithdraw</p>
 * <p>Description:AccountFundWithdraw</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "账户资金提现记录表")
public class AccountFundWithdraw extends SignableEntity
{
    @Override protected byte[] acquiresSignValue() throws UnsupportedEncodingException
    {
        return new byte[0];
    }

    private static final long serialVersionUID = 1L;
    
    /**账户ID*/
    @NotNull(message = "账户ID不可为空")
    @ApiModelProperty(value = "账户ID", required = true)
    private Long                 accountId;
    
    /**证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "证券信息id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "证券信息id 对应Stockinfo表中的ID字段", required = true)
    private Long                 stockinfoId;
    
    /**流水时间戳*/
    @NotNull(message = "流水时间戳不可为空")
    @ApiModelProperty(value = "流水时间戳", required = true)
    private java.sql.Timestamp       withdrawDate;
    
    /**提现地址*/
    @NotNull(message = "提现地址不可为空")
    @ApiModelProperty(value = "提现地址", required = true)
    private String               withdrawAddr;
    
    /**提现地址是否是认证地址(yes、no)*/
    @NotNull(message = "提现地址是否是认证地址(yes、no)不可为空")
    @ApiModelProperty(value = "提现地址是否是认证地址(yes、no)", required = true)
    private String               withdrawAddrAuth;
    
    /**提现数量(包括手续费)*/
    @NotNull(message = "提现数量(包括手续费)不可为空")
    @ApiModelProperty(value = "提现数量(包括手续费)", required = true)
    private java.math.BigDecimal withdrawAmt;
    
    /**区块转账费用*/
    @ApiModelProperty(value = "区块转账费用")
    private java.math.BigDecimal netFee;
    
    /**状态(待确认confirming、待邮件激活activating、已处理done)*/
    @NotNull(message = "状态(待确认confirming、待邮件激活activating、已处理done)不可为空")
    @ApiModelProperty(value = "状态(待确认confirming、待邮件激活activating、已处理done)", required = true)
    private String               status;
    
    /**确认码*/
    @NotNull(message = "确认码不可为空")
    @ApiModelProperty(value = "确认码", required = true)
    private String               confirmCode;
    
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
    
    public java.sql.Timestamp getWithdrawDate()
    {
        return this.withdrawDate;
    }
    
    public void setWithdrawDate(java.sql.Timestamp withdrawDate)
    {
        this.withdrawDate = withdrawDate;
    }
    
    public String getWithdrawAddr()
    {
        return this.withdrawAddr;
    }
    
    public void setWithdrawAddr(String withdrawAddr)
    {
        this.withdrawAddr = withdrawAddr;
    }
    
    public String getWithdrawAddrAuth()
    {
        return this.withdrawAddrAuth;
    }
    
    public void setWithdrawAddrAuth(String withdrawAddrAuth)
    {
        this.withdrawAddrAuth = withdrawAddrAuth;
    }
    
    public java.math.BigDecimal getWithdrawAmt()
    {
        return this.withdrawAmt;
    }
    
    public void setWithdrawAmt(java.math.BigDecimal withdrawAmt)
    {
        this.withdrawAmt = withdrawAmt;
    }
    
    public java.math.BigDecimal getNetFee()
    {
        return this.netFee;
    }
    
    public void setNetFee(java.math.BigDecimal netFee)
    {
        this.netFee = netFee;
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
    
    public String getConfirmCode()
    {
        return confirmCode;
    }
    
    public void setConfirmCode(String confirmCode)
    {
        this.confirmCode = confirmCode;
    }
}

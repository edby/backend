/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.entity;

import com.blocain.bitms.orm.core.SignableEntity;
import com.blocain.bitms.tools.consts.CharsetConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 账户资金划拨表 实体对象
 * <p>File：AccountFundTransfer.java</p>
 * <p>Title: AccountFundTransfer</p>
 * <p>Description:AccountFundTransfer</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "账户资金划拨表")
public class AccountFundTransfer extends SignableEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**Boss系统用户ID*/
    @NotNull(message = "Boss系统用户ID不可为空")
    @ApiModelProperty(value = "Boss系统用户ID", required = true)
    private Long                 userinfoId;
    
    /**证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "证券信息id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "证券信息id 对应Stockinfo表中的ID字段", required = true)
    private Long                 stockinfoId;
    
    /**源钱包ID*/
    @NotNull(message = "源钱包ID不可为空")
    @ApiModelProperty(value = "源钱包ID", required = true)
    private String               srcWalletId;
    
    /**源钱包地址*/
    @ApiModelProperty(value = "源钱包地址", required = true)
    private String               srcWalletAddr;
    
    /**目标钱包地址*/
    @NotNull(message = "目标钱包地址不可为空")
    @ApiModelProperty(value = "目标钱包地址", required = true)
    private String               targetWalletAddr;
    
    /**目标账户ID*/
    @NotNull(message = "目标账户ID不可为空")
    @ApiModelProperty(value = "目标账户ID", required = true)
    private Long                 accountId;
    
    /**目标账户*/
    private String               accountName;
    
    /**划拨时间*/
    @NotNull(message = "划拨时间不可为空")
    @ApiModelProperty(value = "划拨时间", required = true)
    private java.sql.Timestamp   transferTime;
    
    /**划拨数量*/
    @NotNull(message = "划拨数量不可为空")
    @ApiModelProperty(value = "划拨数量", required = true)
    private java.math.BigDecimal transferAmt;
    
    /**划拨费用*/
    @NotNull(message = "划拨费用不可为空")
    @ApiModelProperty(value = "划拨费用", required = true)
    private java.math.BigDecimal transferFee;

    /**实际划拨费用*/
    @ApiModelProperty(value = "实际划拨费用", required = true)
    private java.math.BigDecimal realTransferFee=BigDecimal.ZERO;
    
    /**划拨状态(noTransfer无需划拨unTransfer未划拨、Transfer已划拨)*/
    @NotNull(message = "划拨状态不可为空")
    @ApiModelProperty(value = "划拨状态(noTransfer无需划拨unTransfer未划拨、Transfer已划拨)", required = true)
    private String               transferStatus;
    
    /**区块交易ID*/
    @ApiModelProperty(value = "区块交易ID")
    private String               transId;
    
    /**区块确认状态（unconfirm未确认、confirm已确认）*/
    @NotNull(message = "区块确认状态不可为空")
    @ApiModelProperty(value = "区块确认状态（unconfirm未确认、confirm已确认）", required = true)
    private String               confirmStatus;
    
    /**原资金流水数据ID*/
    @NotNull(message = "原资金流水数据ID不可为空")
    private Long                 originalCurrentId;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String               remark;
    
    // pendingApproval
    private String               pendingApproval;
    
    @Override
    protected byte[] acquiresSignValue() throws UnsupportedEncodingException
    {
        StringBuffer signValue = new StringBuffer(String.valueOf(this.id)).append(this.accountId).append(this.stockinfoId);
        signValue.append(this.srcWalletId).append(this.targetWalletAddr);
        return signValue.toString().getBytes(CharsetConst.CHARSET_UT);
    }
    
    public Long getUserinfoId()
    {
        return this.userinfoId;
    }
    
    public void setUserinfoId(Long userinfoId)
    {
        this.userinfoId = userinfoId;
    }
    
    public Long getStockinfoId()
    {
        return this.stockinfoId;
    }
    
    public void setStockinfoId(Long stockinfoId)
    {
        this.stockinfoId = stockinfoId;
    }
    
    public String getSrcWalletId()
    {
        return this.srcWalletId;
    }
    
    public void setSrcWalletId(String srcWalletId)
    {
        this.srcWalletId = srcWalletId;
    }
    
    public String getSrcWalletAddr()
    {
        return this.srcWalletAddr;
    }
    
    public void setSrcWalletAddr(String srcWalletAddr)
    {
        this.srcWalletAddr = srcWalletAddr;
    }
    
    public String getTargetWalletAddr()
    {
        return this.targetWalletAddr;
    }
    
    public void setTargetWalletAddr(String targetWalletAddr)
    {
        this.targetWalletAddr = targetWalletAddr;
    }
    
    public Long getAccountId()
    {
        return this.accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public Timestamp getTransferTime()
    {
        return transferTime;
    }
    
    public void setTransferTime(Timestamp transferTime)
    {
        this.transferTime = transferTime;
    }
    
    public java.math.BigDecimal getTransferAmt()
    {
        return this.transferAmt;
    }
    
    public void setTransferAmt(java.math.BigDecimal transferAmt)
    {
        this.transferAmt = transferAmt;
    }
    
    public java.math.BigDecimal getTransferFee()
    {
        return this.transferFee;
    }
    
    public void setTransferFee(java.math.BigDecimal transferFee)
    {
        this.transferFee = transferFee;
    }
    
    public String getTransferStatus()
    {
        return this.transferStatus;
    }
    
    public void setTransferStatus(String transferStatus)
    {
        this.transferStatus = transferStatus;
    }
    
    public String getTransId()
    {
        return this.transId;
    }
    
    public void setTransId(String transId)
    {
        this.transId = transId;
    }
    
    public String getConfirmStatus()
    {
        return this.confirmStatus;
    }
    
    public void setConfirmStatus(String confirmStatus)
    {
        this.confirmStatus = confirmStatus;
    }
    
    public String getRemark()
    {
        return this.remark;
    }
    
    public Long getOriginalCurrentId()
    {
        return originalCurrentId;
    }
    
    public void setOriginalCurrentId(Long originalCurrentId)
    {
        this.originalCurrentId = originalCurrentId;
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
    
    public String getPendingApproval()
    {
        return pendingApproval;
    }
    
    public void setPendingApproval(String pendingApproval)
    {
        this.pendingApproval = pendingApproval;
    }

    public BigDecimal getRealTransferFee()
    {
        return realTransferFee;
    }

    public void setRealTransferFee(BigDecimal realTransferFee)
    {
        this.realTransferFee = realTransferFee;
    }
}

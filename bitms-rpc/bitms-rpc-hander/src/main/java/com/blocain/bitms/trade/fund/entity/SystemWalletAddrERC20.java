/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.entity;

import com.blocain.bitms.orm.core.SignableEntity;
import com.blocain.bitms.tools.consts.CharsetConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.blocain.bitms.orm.core.GenericEntity;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;

/**
 * 系统ERC20钱包地址表 实体对象
 * <p>File：SystemWalletAddrERC20.java</p>
 * <p>Title: SystemWalletAddrERC20</p>
 * <p>Description:SystemWalletAddrERC20</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "系统ERC20钱包地址表")
public class SystemWalletAddrERC20 extends SignableEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**证券信息id 对应Stockinfo表中的ID字段(erc20虚拟化)*/
    @NotNull(message = "证券信息id 对应Stockinfo表中的ID字段(erc20虚拟化)不可为空")
    @ApiModelProperty(value = "证券信息id 对应Stockinfo表中的ID字段(erc20虚拟化)", required = true)
    private Long                 stockinfoId;
    
    /**证券代码*/
    private String               stockCode;
    
    /**证券名称*/
    private String               stockName;
    
    /**证券类型*/
    private String               stockType;
    
    /**钱包ID*/
    @NotNull(message = "钱包ID不可为空")
    @ApiModelProperty(value = "钱包ID", required = true)
    private String               walletId;
    
    /**钱包地址*/
    @NotNull(message = "钱包地址不可为空")
    @ApiModelProperty(value = "钱包地址", required = true)
    private String               walletAddr;
    
    /**账户ID*/
    @NotNull(message = "账户ID不可为空")
    @ApiModelProperty(value = "账户ID", required = true)
    private String               accountId;

    /**钱包密码(加密)*/
    @NotNull(message = "钱包密码不可为空（加密）")
    @ApiModelProperty(value = "钱包密码（加密）", required = true)
    private String               walletPwd;
    
    /**总接收(包含未确认)*/
    @ApiModelProperty(value = "总接收(包含未确认)")
    private java.math.BigDecimal received;
    
    /**未确认总接收*/
    @ApiModelProperty(value = "未确认总接收")
    private java.math.BigDecimal unconfirmedReceived;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String               remark;
    
    /**创建人*/
    @NotNull(message = "创建人不可为空")
    @ApiModelProperty(value = "创建人", required = true)
    private Long                 createBy;
    
    /**创建时间*/
    @NotNull(message = "创建时间不可为空")
    @ApiModelProperty(value = "创建时间", required = true)
    private java.util.Date       createDate;
    
    /**钱包用途类型（chargeAccount充币转账、collectAccount充币归集、transferAccount提币划拨）*/
    private String               walletUsageType;
    
    /**帐号*/
    private String               accountName;
    
    /**钱包名称*/
    private String               walletName;

    public String getWalletPwd() {
        return walletPwd;
    }

    public void setWalletPwd(String walletPwd) {
        this.walletPwd = walletPwd;
    }

    public Long getStockinfoId()
    {
        return this.stockinfoId;
    }
    
    public void setStockinfoId(Long stockinfoId)
    {
        this.stockinfoId = stockinfoId;
    }
    
    public String getWalletId()
    {
        return this.walletId;
    }
    
    public void setWalletId(String walletId)
    {
        this.walletId = walletId;
    }
    
    public String getWalletAddr()
    {
        return this.walletAddr;
    }
    
    public void setWalletAddr(String walletAddr)
    {
        this.walletAddr = walletAddr;
    }
    
    public String getAccountId()
    {
        return this.accountId;
    }
    
    public void setAccountId(String accountId)
    {
        this.accountId = accountId;
    }
    
    public java.math.BigDecimal getReceived()
    {
        return this.received;
    }
    
    public void setReceived(java.math.BigDecimal received)
    {
        this.received = received;
    }
    
    public java.math.BigDecimal getUnconfirmedReceived()
    {
        return this.unconfirmedReceived;
    }
    
    public void setUnconfirmedReceived(java.math.BigDecimal unconfirmedReceived)
    {
        this.unconfirmedReceived = unconfirmedReceived;
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
    
    @Override
    protected byte[] acquiresSignValue() throws UnsupportedEncodingException
    {
        StringBuffer signValue = new StringBuffer(String.valueOf(this.id)).append(this.stockinfoId);
        signValue.append(this.walletId).append(this.walletAddr);
        signValue.append(this.accountId);
        return signValue.toString().getBytes(CharsetConst.CHARSET_UT);
    }
    
    public String getWalletUsageType()
    {
        return walletUsageType;
    }
    
    public void setWalletUsageType(String walletUsageType)
    {
        this.walletUsageType = walletUsageType;
    }
    
    public String getStockCode()
    {
        return stockCode;
    }
    
    public void setStockCode(String stockCode)
    {
        this.stockCode = stockCode;
    }
    
    public String getStockName()
    {
        return stockName;
    }
    
    public void setStockName(String stockName)
    {
        this.stockName = stockName;
    }
    
    public String getStockType()
    {
        return stockType;
    }
    
    public void setStockType(String stockType)
    {
        this.stockType = stockType;
    }
    
    public String getAccountName()
    {
        return accountName;
    }
    
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    
    public String getWalletName()
    {
        return walletName;
    }
    
    public void setWalletName(String walletName)
    {
        this.walletName = walletName;
    }

    @Override
    public String toString() {
        return "SystemWalletAddrERC20{" +
                "stockinfoId=" + stockinfoId +
                ", stockCode='" + stockCode + '\'' +
                ", stockName='" + stockName + '\'' +
                ", stockType='" + stockType + '\'' +
                ", walletId='" + walletId + '\'' +
                ", walletAddr='" + walletAddr + '\'' +
                ", accountId='" + accountId + '\'' +
                ", walletPwd='" + walletPwd + '\'' +
                ", received=" + received +
                ", unconfirmedReceived=" + unconfirmedReceived +
                ", remark='" + remark + '\'' +
                ", createBy=" + createBy +
                ", createDate=" + createDate +
                ", walletUsageType='" + walletUsageType + '\'' +
                ", accountName='" + accountName + '\'' +
                ", walletName='" + walletName + '\'' +
                '}';
    }
}

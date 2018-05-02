/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.entity;

import com.blocain.bitms.orm.core.SignableEntity;
import com.blocain.bitms.tools.consts.CharsetConst;

import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 系统钱包地址表 实体对象
 * <p>File：SystemWalletAddr.java</p>
 * <p>Title: SystemWalletAddr</p>
 * <p>Description:SystemWalletAddr</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class SystemWalletAddr extends SignableEntity
{
    private static final long  serialVersionUID = 1L;
    
    /**证券id*/
    private Long               stockinfoId;
    
    /**证券代码*/
    private String             stockCode;
    
    /**证券名称*/
    private String             stockName;
    
    /**证券类型*/
    private String             stockType;
    
    /**钱包ID*/
    @NotNull(message = "钱包ID不可为空")
    private String             walletId;
    
    /**钱包地址*/
    @NotNull(message = "钱包地址不可为空")
    private String             walletAddr;
    
    /**账户ID*/
    @NotNull(message = "账户ID不可为空")
    private Long               accountId;
    
    /**总接收*/
    private BigDecimal         received;
    
    /**未确认总接收*/
    private BigDecimal         unconfirmedReceived;
    
    /**备注*/
    private String             remark;
    
    /**创建人*/
    private Long               createBy;
    
    /**创建人*/
    private String             createByName;
    
    /**帐号*/
    private String             accountName;
    
    /**创建时间*/
    private java.sql.Timestamp createDate;
    
    /**钱包名称*/
    private String             walletName;
    
    /**钱包用途类型（chargeAccount充币转账、collectAccount充币归集、transferAccount提币划拨）*/
    private String             walletUsageType;
    
    @Override
    protected byte[] acquiresSignValue() throws UnsupportedEncodingException
    {
        StringBuffer signValue = new StringBuffer(String.valueOf(this.id)).append(this.stockinfoId);
        signValue.append(this.walletId).append(this.walletAddr);
        signValue.append(this.accountId);
        return signValue.toString().getBytes(CharsetConst.CHARSET_UT);
    }
    
    public Long getStockinfoId()
    {
        return stockinfoId;
    }
    
    public void setStockinfoId(Long stockinfoId)
    {
        this.stockinfoId = stockinfoId;
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
    
    public Long getAccountId()
    {
        return this.accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
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
    
    public Timestamp getCreateDate()
    {
        return createDate;
    }
    
    public void setCreateDate(Timestamp createDate)
    {
        this.createDate = createDate;
    }
    
    public String getCreateByName()
    {
        return createByName;
    }
    
    public void setCreateByName(String createByName)
    {
        this.createByName = createByName;
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
    
    public String getWalletUsageType()
    {
        return walletUsageType;
    }
    
    public void setWalletUsageType(String walletUsageType)
    {
        this.walletUsageType = walletUsageType;
    }
    
    public BigDecimal getReceived()
    {
        return received;
    }
    
    public void setReceived(BigDecimal received)
    {
        this.received = received;
    }
    
    public BigDecimal getUnconfirmedReceived()
    {
        return unconfirmedReceived;
    }
    
    public void setUnconfirmedReceived(BigDecimal unconfirmedReceived)
    {
        this.unconfirmedReceived = unconfirmedReceived;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("SystemWalletAddr{");
        sb.append("id=").append(id);
        sb.append(", stockinfoId=").append(stockinfoId);
        sb.append(", stockCode='").append(stockCode).append('\'');
        sb.append(", stockName='").append(stockName).append('\'');
        sb.append(", stockType='").append(stockType).append('\'');
        sb.append(", walletId='").append(walletId).append('\'');
        sb.append(", walletAddr='").append(walletAddr).append('\'');
        sb.append(", accountId=").append(accountId);
        sb.append(", received=").append(received);
        sb.append(", unconfirmedReceived=").append(unconfirmedReceived);
        sb.append(", remark='").append(remark).append('\'');
        sb.append(", createBy=").append(createBy);
        sb.append(", createByName='").append(createByName).append('\'');
        sb.append(", accountName='").append(accountName).append('\'');
        sb.append(", createDate=").append(createDate);
        sb.append(", walletName='").append(walletName).append('\'');
        sb.append(", walletUsageType='").append(walletUsageType).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

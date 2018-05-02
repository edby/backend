/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.entity;

import com.blocain.bitms.orm.core.SignableEntity;
import com.blocain.bitms.tools.consts.CharsetConst;

import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;

/**
 * 系统钱包表 实体对象
 * <p>File：SystemWallet.java</p>
 * <p>Title: SystemWallet</p>
 * <p>Description:SystemWallet</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class SystemWallet extends SignableEntity
{
    private static final long  serialVersionUID = 1L;
    
    /**证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "证券信息id 对应Stockinfo表中的ID字段不可为空")
    private Long               stockinfoId;
    
    /**walletId*/
    @NotNull(message = "walletId不可为空")
    private String             walletId;
    
    /**钱包名字*/
    @NotNull(message = "钱包名字不可为空")
    private String             walletName;
    
    /**备注*/
    private String             remark;
    
    /**创建人*/
    private Long               createBy;
    
    /**创建时间*/
    private java.sql.Timestamp createDate;
    
    /**钱包用途类型（chargeAccount充币转账、collectAccount充币归集、transferAccount提币划拨）*/
    @NotNull(message = "钱包用途类型")
    private String             walletUsageType;
    
    /**钱包地址*/
    private String             walletAddr;
    
    /**账户名id*/
    private Long               accountId;
    
    /**证券代码*/
    private String             stockCode;
    
    /**证券名称*/
    private String             stockName;
    
    /**证券类型*/
    private String             stockType;
    
    @Override
    protected byte[] acquiresSignValue() throws UnsupportedEncodingException
    {
        StringBuffer signValue = new StringBuffer(String.valueOf(this.id)).append(this.stockinfoId);
        signValue.append(this.walletId).append(this.walletName);
        signValue.append(this.walletUsageType);
        return signValue.toString().getBytes(CharsetConst.CHARSET_UT);
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
    
    public String getWalletName()
    {
        return this.walletName;
    }
    
    public void setWalletName(String walletName)
    {
        this.walletName = walletName;
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
    
    public String getWalletUsageType()
    {
        return walletUsageType;
    }
    
    public void setWalletUsageType(String walletUsageType)
    {
        this.walletUsageType = walletUsageType;
    }
    
    public String getWalletAddr()
    {
        return walletAddr;
    }
    
    public void setWalletAddr(String walletAddr)
    {
        this.walletAddr = walletAddr;
    }
    
    public Long getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
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
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("SystemWallet{");
        sb.append("id=").append(id);
        sb.append(", stockinfoId=").append(stockinfoId);
        sb.append(", walletId='").append(walletId).append('\'');
        sb.append(", walletName='").append(walletName).append('\'');
        sb.append(", remark='").append(remark).append('\'');
        sb.append(", createBy=").append(createBy);
        sb.append(", createDate=").append(createDate);
        sb.append(", walletUsageType='").append(walletUsageType).append('\'');
        sb.append(", walletAddr='").append(walletAddr).append('\'');
        sb.append(", accountId=").append(accountId);
        sb.append(", stockCode='").append(stockCode).append('\'');
        sb.append(", stockName='").append(stockName).append('\'');
        sb.append(", stockType='").append(stockType).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

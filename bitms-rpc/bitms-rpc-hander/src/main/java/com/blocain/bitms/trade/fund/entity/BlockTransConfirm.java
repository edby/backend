/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.entity;

import com.blocain.bitms.orm.core.GenericEntity;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * 区块交易确认表 实体对象
 * <p>File：BlockTransConfirm.java</p>
 * <p>Title: BlockTransConfirm</p>
 * <p>Description:BlockTransConfirm</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class BlockTransConfirm extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**证券id*/
    private Long                 stockinfoId;
    
    /**证券代码*/
    private String               stockCode;
    
    /**证券名称*/
    private String               stockName;
    
    /**证券类型*/
    private String               stockType;
    
    /**钱包ID*/
    private String               walletId;
    
    /**交易ID*/
    private String               transId;
    
    /**钱包地址*/
    @NotNull(message = "钱包地址不可为空")
    private Object               walletAddr;
    
    /**区块确认方(btc、okcoin、blockmeta)*/
    @NotNull(message = "区块确认方不可为空")
    private String               confirmSide;
    
    /**direct*/
    @NotNull(message = "direct不可为空")
    private String               direct;
    
    /**status*/
    @NotNull(message = "status不可为空")
    private String               status;
    
    /**remark*/
    private String               remark;
    
    /**createBy*/
    private Long                 createBy;
    
    /**createDate*/
    @NotNull(message = "createDate不可为空")
    private java.sql.Timestamp   createDate;
    
    /**帐号*/
    private Long                 accountId;
    
    /**帐号*/
    private String               accountName;
    
    /**钱包名称*/
    private Long                 walletName;
    
    /**发生额*/
    private java.math.BigDecimal amount;
    
    /**网络手续费*/
    private java.math.BigDecimal fee;
    
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
    
    public Object getWalletAddr()
    {
        return this.walletAddr;
    }
    
    public void setWalletAddr(Object walletAddr)
    {
        this.walletAddr = walletAddr;
    }
    
    public String getConfirmSide()
    {
        return this.confirmSide;
    }
    
    public void setConfirmSide(String confirmSide)
    {
        this.confirmSide = confirmSide;
    }
    
    public String getDirect()
    {
        return this.direct;
    }
    
    public void setDirect(String direct)
    {
        this.direct = direct;
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
    
    public Timestamp getCreateDate()
    {
        return createDate;
    }
    
    public void setCreateDate(Timestamp createDate)
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
    
    public Long getWalletName()
    {
        return walletName;
    }
    
    public void setWalletName(Long walletName)
    {
        this.walletName = walletName;
    }
    
    public String getWalletId()
    {
        return walletId;
    }
    
    public void setWalletId(String walletId)
    {
        this.walletId = walletId;
    }
    
    public String getTransId()
    {
        return transId;
    }
    
    public void setTransId(String transId)
    {
        this.transId = transId;
    }
    
    public Long getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public java.math.BigDecimal getAmount()
    {
        return amount;
    }
    
    public void setAmount(java.math.BigDecimal amount)
    {
        this.amount = amount;
    }
    
    public java.math.BigDecimal getFee()
    {
        return fee;
    }
    
    public void setFee(java.math.BigDecimal fee)
    {
        this.fee = fee;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("BlockTransConfirm{");
        sb.append("id=").append(id);
        sb.append(", stockinfoId=").append(stockinfoId);
        sb.append(", stockCode='").append(stockCode).append('\'');
        sb.append(", stockName='").append(stockName).append('\'');
        sb.append(", stockType='").append(stockType).append('\'');
        sb.append(", walletId='").append(walletId).append('\'');
        sb.append(", transId='").append(transId).append('\'');
        sb.append(", walletAddr=").append(walletAddr);
        sb.append(", confirmSide='").append(confirmSide).append('\'');
        sb.append(", direct='").append(direct).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append(", remark='").append(remark).append('\'');
        sb.append(", createBy=").append(createBy);
        sb.append(", createDate=").append(createDate);
        sb.append(", accountId=").append(accountId);
        sb.append(", accountName='").append(accountName).append('\'');
        sb.append(", walletName=").append(walletName);
        sb.append(", amount=").append(amount);
        sb.append(", fee=").append(fee);
        sb.append('}');
        return sb.toString();
    }
}

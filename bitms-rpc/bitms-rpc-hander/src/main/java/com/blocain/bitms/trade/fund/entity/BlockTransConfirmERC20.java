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
 * ERC20区块交易确认表 实体对象
 * <p>File：BlockTransConfirmERC20.java</p>
 * <p>Title: BlockTransConfirmERC20</p>
 * <p>Description:BlockTransConfirmERC20</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "ERC20区块交易确认表")
public class BlockTransConfirmERC20 extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**证券信息id 对应Stockinfo表中的ID字段(具体token,包括eth)*/
    @NotNull(message = "证券信息id 对应Stockinfo表中的ID字段(具体token,包括eth)不可为空")
    @ApiModelProperty(value = "证券信息id 对应Stockinfo表中的ID字段(具体token,包括eth)", required = true)
    private Long                 stockinfoId;
    
    /**钱包ID*/
    @NotNull(message = "钱包ID不可为空")
    @ApiModelProperty(value = "钱包ID", required = true)
    private String               walletId;
    
    /**钱包地址*/
    @NotNull(message = "钱包地址不可为空")
    @ApiModelProperty(value = "钱包地址", required = true)
    private String               walletAddr;
    
    /**交易ID*/
    @NotNull(message = "交易ID不可为空")
    @ApiModelProperty(value = "交易ID", required = true)
    private String               transId;
    
    /**区块确认方(btc、okcoin、blockmeta)*/
    @NotNull(message = "区块确认方(btc、okcoin、blockmeta)不可为空")
    @ApiModelProperty(value = "区块确认方(btc、okcoin、blockmeta)", required = true)
    private String               confirmSide;
    
    /**区块资产方向(Collect收款、Payment付款)*/
    @NotNull(message = "区块资产方向(Collect收款、Payment付款)不可为空")
    @ApiModelProperty(value = "区块资产方向(Collect收款、Payment付款)", required = true)
    private String               direct;
    
    /**区块确认状态(unconfirm未确认、confirm已确认)*/
    @NotNull(message = "区块确认状态(unconfirm未确认、confirm已确认)不可为空")
    @ApiModelProperty(value = "区块确认状态(unconfirm未确认、confirm已确认)", required = true)
    private String               status;
    
    /**归集状态*/
    @NotNull(message = "归集状态不可为空")
    @ApiModelProperty(value = "归集状态(unCollect未归集、collect已归集)", required = true)
    private String               collectStatus;
    
    /**发生额*/
    @ApiModelProperty(value = "发生额")
    private java.math.BigDecimal amount;
    
    /**网络手续费*/
    @ApiModelProperty(value = "网络手续费")
    private java.math.BigDecimal fee;
    
    /**该笔交易对应的块高度*/
    @ApiModelProperty(value = "该笔交易对应的块高度")
    private String               blockNumber;
    
    /**该笔交易对应的块Hash*/
    @ApiModelProperty(value = "该笔交易对应的块Hash")
    private String               blockHash;

    /**该笔交易对应的付款地址*/
    @ApiModelProperty(value = "该笔交易对应的付款地址")
    private String               fromAddress;
    
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
    
    /**证券代码*/
    private String               stockCode;
    
    /**证券名称*/
    private String               stockName;
    
    /**证券类型*/
    private String               stockType;
    
    /**帐号id*/
    private Long                 accountId;
    
    /**帐号名称*/
    private String               accountName;
    
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
    
    public Long getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public String getAccountName()
    {
        return accountName;
    }
    
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    
    public String getCollectStatus()
    {
        return collectStatus;
    }
    
    public void setCollectStatus(String collectStatus)
    {
        this.collectStatus = collectStatus;
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
    
    public String getTransId()
    {
        return this.transId;
    }
    
    public void setTransId(String transId)
    {
        this.transId = transId;
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
    
    public String getBlockNumber()
    {
        return this.blockNumber;
    }
    
    public void setBlockNumber(String blockNumber)
    {
        this.blockNumber = blockNumber;
    }
    
    public String getBlockHash()
    {
        return this.blockHash;
    }
    
    public void setBlockHash(String blockHash)
    {
        this.blockHash = blockHash;
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

    public String getFromAddress()
    {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress)
    {
        this.fromAddress = fromAddress;
    }

    @Override
    public String toString() {
        return "BlockTransConfirmERC20{" +
                "stockinfoId=" + stockinfoId +
                ", walletId='" + walletId + '\'' +
                ", walletAddr='" + walletAddr + '\'' +
                ", transId='" + transId + '\'' +
                ", confirmSide='" + confirmSide + '\'' +
                ", direct='" + direct + '\'' +
                ", status='" + status + '\'' +
                ", collectStatus='" + collectStatus + '\'' +
                ", amount=" + amount +
                ", fee=" + fee +
                ", blockNumber='" + blockNumber + '\'' +
                ", blockHash='" + blockHash + '\'' +
                ", fromAddress='" + fromAddress + '\'' +
                ", remark='" + remark + '\'' +
                ", createBy=" + createBy +
                ", createDate=" + createDate +
                ", stockCode='" + stockCode + '\'' +
                ", stockName='" + stockName + '\'' +
                ", stockType='" + stockType + '\'' +
                ", accountId=" + accountId +
                ", accountName='" + accountName + '\'' +
                '}';
    }
}

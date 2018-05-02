/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.entity;

import com.blocain.bitms.orm.core.GenericEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 合约账户资产表 实体对象
 * <p>File：AccountContractAsset.java</p>
 * <p>Title: AccountContractAsset</p>
 * <p>Description:AccountContractAsset</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class AccountContractAsset extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**账户ID*/
    @NotNull(message = "账户ID不可为空")
    private Long                 accountId;
    
    /**本身证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "本身证券信息id 对应Stockinfo表中的ID字段不可为空")
    private Long                 stockinfoId;
    
    /**关联证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "关联证券信息id 对应Stockinfo表中的ID字段")
    private Long                 relatedStockinfoId;
    
    /**方向(多头Long,空头Short)*/
    @NotNull(message = "方向不可为空")
    private String               direction        = "Long";
    
    /**价格*/
    @NotNull(message = "价格不可为空")
    private java.math.BigDecimal price            = BigDecimal.valueOf(1);
    
    /**数量*/
    @NotNull(message = "数量不可为空")
    private java.math.BigDecimal amount;
    
    /**冻结数量*/
    @NotNull(message = "冻结数量不可为空")
    private java.math.BigDecimal frozenAmt;
    
    /**期初数量*/
    @NotNull(message = "期初数量不可为空")
    private java.math.BigDecimal initialAmt       = BigDecimal.ZERO;
    
    /**本期流入数量*/
    private java.math.BigDecimal flowInAmt        = BigDecimal.ZERO;
    
    /**本期流出期初数量*/
    private java.math.BigDecimal flowOutAmt       = BigDecimal.ZERO;
    
    /**备注*/
    private String               remark;
    
    /**修改时间*/
    @NotNull(message = "修改时间不可为空")
    private java.sql.Timestamp   updateDate;
    
    private String               stockCode;
    
    /** 关联证券*/
    private String               relatedStockCode;
    
    private String               accountName;
    
    /** 与交易对对应的表名*/
    private String               tableName;
    
    /**借贷数量或金额*/
    @ApiModelProperty(value = "借贷数量或金额", required = true)
    private java.math.BigDecimal debitAmt;
    
    /** 与交易对对应的表名*/
    private String               tableDebitName;
    
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
    
    public Long getRelatedStockinfoId()
    {
        return relatedStockinfoId;
    }
    
    public void setRelatedStockinfoId(Long relatedStockinfoId)
    {
        this.relatedStockinfoId = relatedStockinfoId;
    }
    
    public String getDirection()
    {
        return direction;
    }
    
    public void setDirection(String direction)
    {
        this.direction = direction;
    }
    
    public java.math.BigDecimal getPrice()
    {
        return price;
    }
    
    public void setPrice(java.math.BigDecimal price)
    {
        this.price = price;
    }
    
    public java.math.BigDecimal getAmount()
    {
        return this.amount;
    }
    
    public void setAmount(java.math.BigDecimal amount)
    {
        this.amount = amount;
    }
    
    public java.math.BigDecimal getFrozenAmt()
    {
        return this.frozenAmt;
    }
    
    public void setFrozenAmt(java.math.BigDecimal frozenAmt)
    {
        this.frozenAmt = frozenAmt;
    }
    
    public String getRemark()
    {
        return this.remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public Timestamp getUpdateDate()
    {
        return updateDate;
    }
    
    public void setUpdateDate(Timestamp updateDate)
    {
        this.updateDate = updateDate;
    }
    
    public String getStockCode()
    {
        return stockCode;
    }
    
    public void setStockCode(String stockCode)
    {
        this.stockCode = stockCode;
    }
    
    public String getAccountName()
    {
        return accountName;
    }
    
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    
    public String getRelatedStockCode()
    {
        return relatedStockCode;
    }
    
    public void setRelatedStockCode(String relatedStockCode)
    {
        this.relatedStockCode = relatedStockCode;
    }
    
    public BigDecimal getInitialAmt()
    {
        return initialAmt;
    }
    
    public void setInitialAmt(BigDecimal initialAmt)
    {
        this.initialAmt = initialAmt;
    }
    
    public String getTableName()
    {
        return tableName;
    }
    
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }
    
    public BigDecimal getFlowInAmt()
    {
        return flowInAmt;
    }
    
    public void setFlowInAmt(BigDecimal flowInAmt)
    {
        this.flowInAmt = flowInAmt;
    }
    
    public BigDecimal getFlowOutAmt()
    {
        return flowOutAmt;
    }
    
    public void setFlowOutAmt(BigDecimal flowOutAmt)
    {
        this.flowOutAmt = flowOutAmt;
    }
    
    public BigDecimal getDebitAmt()
    {
        return debitAmt;
    }
    
    public void setDebitAmt(BigDecimal debitAmt)
    {
        this.debitAmt = debitAmt;
    }
    
    public String getTableDebitName()
    {
        return tableDebitName;
    }
    
    public void setTableDebitName(String tableDebitName)
    {
        this.tableDebitName = tableDebitName;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("AccountContractAsset{");
        sb.append("id=").append(id);
        sb.append(", accountId=").append(accountId);
        sb.append(", stockinfoId=").append(stockinfoId);
        sb.append(", relatedStockinfoId=").append(relatedStockinfoId);
        sb.append(", direction='").append(direction).append('\'');
        sb.append(", price=").append(price);
        sb.append(", amount=").append(amount);
        sb.append(", frozenAmt=").append(frozenAmt);
        sb.append(", initialAmt=").append(initialAmt);
        sb.append(", flowInAmt=").append(flowInAmt);
        sb.append(", flowOutAmt=").append(flowOutAmt);
        sb.append(", remark='").append(remark).append('\'');
        sb.append(", updateDate=").append(updateDate);
        sb.append(", stockCode='").append(stockCode).append('\'');
        sb.append(", relatedStockCode='").append(relatedStockCode).append('\'');
        sb.append(", accountName='").append(accountName).append('\'');
        sb.append(", tableName='").append(tableName).append('\'');
        sb.append(", debitAmt=").append(debitAmt);
        sb.append(", tableDebitName='").append(tableDebitName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.entity;

import java.math.BigDecimal;
import java.util.Arrays;
import javax.validation.constraints.NotNull;

import com.blocain.bitms.orm.core.GenericEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 账户理财负债资产表 实体对象
 * <p>File：AccountWealthDebitAsset.java</p>
 * <p>Title: AccountWealthDebitAsset</p>
 * <p>Description:AccountWealthDebitAsset</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "账户借贷记录表")
public class AccountWealthDebitAsset extends GenericEntity
{
    private static final long serialVersionUID = 1L;
    
    /**借款人账户ID*/
    @NotNull(message = "借款人账户ID不可为空")
    @ApiModelProperty(value = "借款人账户ID", required = true)
    private Long              wealthAccountId;
    
    /**贷款人账户ID*/
    @NotNull(message = "贷款人账户ID不可为空")
    @ApiModelProperty(value = "贷款人账户ID", required = true)
    private Long              lenderAccountId;
    
    /**借贷证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "借贷证券信息id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "借贷证券信息id 对应Stockinfo表中的ID字段", required = true)
    private Long              stockinfoId;
    
    /**关联证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "关联证券信息id 对应Stockinfo表中的ID字段")
    private Long              relatedStockinfoId;
    
    /**借贷数量或金额*/
    @NotNull(message = "借贷数量或金额不可为空")
    @ApiModelProperty(value = "借贷数量或金额", required = true)
    private BigDecimal        debitAmt;
    
    /**累计利息*/
    @NotNull(message = "累计利息不可为空")
    @ApiModelProperty(value = "累计利息", required = true)
    private BigDecimal        accumulateInterest;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String            remark;
    
    /**修改时间*/
    @NotNull(message = "修改时间不可为空")
    @ApiModelProperty(value = "修改时间", required = true)
    private java.util.Date    updateDate;
    
    /**借款人账户*/
    private String            borrowerAccountName;
    
    /**贷款人账户*/
    private String            lenderAccountName;
    
    /**证券代码*/
    private String            stockCode;
    
    /**关联证券代码*/
    private String            relatedStockCode;
    
    /**关联交易对*/
    private String            relatedStockName;
    
    private String[]          tableNames;
    
    /**最后计息日期 8位数字*/
    private Long              lastInterestDay;
    
    public Long getWealthAccountId()
    {
        return wealthAccountId;
    }
    
    public void setWealthAccountId(Long wealthAccountId)
    {
        this.wealthAccountId = wealthAccountId;
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
    
    public BigDecimal getDebitAmt()
    {
        return this.debitAmt;
    }
    
    public void setDebitAmt(BigDecimal debitAmt)
    {
        this.debitAmt = debitAmt;
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
    
    public String getLenderAccountName()
    {
        return lenderAccountName;
    }
    
    public void setLenderAccountName(String lenderAccountName)
    {
        this.lenderAccountName = lenderAccountName;
    }
    
    public String getStockCode()
    {
        return stockCode;
    }
    
    public void setStockCode(String stockCode)
    {
        this.stockCode = stockCode;
    }
    
    public String getBorrowerAccountName()
    {
        return borrowerAccountName;
    }
    
    public void setBorrowerAccountName(String borrowerAccountName)
    {
        this.borrowerAccountName = borrowerAccountName;
    }

    public Long getRelatedStockinfoId()
    {
        return relatedStockinfoId;
    }
    
    public void setRelatedStockinfoId(Long relatedStockinfoId)
    {
        this.relatedStockinfoId = relatedStockinfoId;
    }
    
    public String getRelatedStockCode()
    {
        return relatedStockCode;
    }
    
    public void setRelatedStockCode(String relatedStockCode)
    {
        this.relatedStockCode = relatedStockCode;
    }
    
    public BigDecimal getAccumulateInterest()
    {
        return accumulateInterest;
    }
    
    public void setAccumulateInterest(BigDecimal accumulateInterest)
    {
        this.accumulateInterest = accumulateInterest;
    }
    
    public Long getLastInterestDay()
    {
        return lastInterestDay;
    }
    
    public void setLastInterestDay(Long lastInterestDay)
    {
        this.lastInterestDay = lastInterestDay;
    }
    
    public String getRelatedStockName()
    {
        return relatedStockName;
    }
    
    public void setRelatedStockName(String relatedStockName)
    {
        this.relatedStockName = relatedStockName;
    }
    
    public String[] getTableNames()
    {
        return tableNames;
    }
    
    public void setTableNames(String[] tableNames)
    {
        this.tableNames = tableNames;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("AccountWealthDebitAsset{");
        sb.append("id=").append(id);
        sb.append(", wealthAccountId=").append(wealthAccountId);
        sb.append(", lenderAccountId=").append(lenderAccountId);
        sb.append(", stockinfoId=").append(stockinfoId);
        sb.append(", relatedStockinfoId=").append(relatedStockinfoId);
        sb.append(", debitAmt=").append(debitAmt);
        sb.append(", accumulateInterest=").append(accumulateInterest);
        sb.append(", remark='").append(remark).append('\'');
        sb.append(", updateDate=").append(updateDate);
        sb.append(", borrowerAccountName='").append(borrowerAccountName).append('\'');
        sb.append(", lenderAccountName='").append(lenderAccountName).append('\'');
        sb.append(", stockCode='").append(stockCode).append('\'');
        sb.append(", relatedStockCode='").append(relatedStockCode).append('\'');
        sb.append(", relatedStockName='").append(relatedStockName).append('\'');
        sb.append(", tableNames=").append(Arrays.toString(tableNames));
        sb.append(", lastInterestDay=").append(lastInterestDay);
        sb.append('}');
        return sb.toString();
    }
}

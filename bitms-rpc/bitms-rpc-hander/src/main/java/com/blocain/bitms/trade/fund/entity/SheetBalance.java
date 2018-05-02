/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.entity;

import com.blocain.bitms.tools.annotation.ExcelField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.blocain.bitms.orm.core.GenericEntity;
import javax.validation.constraints.NotNull;

/**
 * 报表资产负债表 实体对象
 * <p>File：SheetBalance.java</p>
 * <p>Title: SheetBalance</p>
 * <p>Description:SheetBalance</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "报表资产负债表")
public class SheetBalance extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**资产负债日(YYYYMMDD)*/
    @NotNull(message = "资产负债日(YYYYMMDD)不可为空")
    @ApiModelProperty(value = "资产负债日(YYYYMMDD)", required = true)
    private Integer              balanceDay;
    
    /**账户类型(walletAccount、spotAccount、wealthAccount)*/
    @NotNull(message = "账户类型(walletAccount、spotAccount、wealthAccount)不可为空")
    @ApiModelProperty(value = "账户类型(walletAccount、spotAccount、wealthAccount)", required = true)
    private String               accountType;
    
    /**账户ID*/
    @NotNull(message = "账户ID不可为空")
    @ApiModelProperty(value = "账户ID", required = true)
    private Long                 accountId;
    
    /**证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "证券信息id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "证券信息id 对应Stockinfo表中的ID字段", required = true)
    private Long                 stockinfoId;
    
    /**证券信息关联id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "证券信息关联id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "证券信息关联id 对应Stockinfo表中的ID字段", required = true)
    private Long                 relatedStockinfoId;
    
    /**资产数量*/
    @NotNull(message = "资产数量不可为空")
    @ApiModelProperty(value = "资产数量", required = true)
    private java.math.BigDecimal assetAmt;
    
    /**资产冻结数量*/
    @NotNull(message = "资产冻结数量不可为空")
    @ApiModelProperty(value = "资产冻结数量", required = true)
    private java.math.BigDecimal assetFrozenAmt;
    
    /**负债数量*/
    @NotNull(message = "负债数量不可为空")
    @ApiModelProperty(value = "负债数量", required = true)
    private java.math.BigDecimal debitAmt;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String               remark;
    
    /**修改时间*/
    @NotNull(message = "修改时间不可为空")
    @ApiModelProperty(value = "修改时间", required = true)
    private java.util.Date       updateDate;

    private String               stockName;
    
    private String               relatedStockName;
    
    private String               relatedStockCode;

    @ExcelField(title = "开始日期")
    private Long                 startDate        = 0L;

    @ExcelField(title = "结束日期")
    private Long                 endDate          = 0L;

    @ExcelField(title = "账户名")
    private String               accountName;

    @ExcelField(title = "证券代码")
    private String               stockCode;

    /**资产负债数量*/
    @ExcelField(title = "营收量")
    @NotNull(message = "资产负债数量不可为空")
    @ApiModelProperty(value = "资产负债数量", required = true)
    private java.math.BigDecimal balanceAmt;
    
    public Integer getBalanceDay()
    {
        return this.balanceDay;
    }
    
    public void setBalanceDay(Integer balanceDay)
    {
        this.balanceDay = balanceDay;
    }
    
    public String getAccountType()
    {
        return this.accountType;
    }
    
    public void setAccountType(String accountType)
    {
        this.accountType = accountType;
    }
    
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
        return this.relatedStockinfoId;
    }
    
    public void setRelatedStockinfoId(Long relatedStockinfoId)
    {
        this.relatedStockinfoId = relatedStockinfoId;
    }
    
    public java.math.BigDecimal getBalanceAmt()
    {
        return this.balanceAmt;
    }
    
    public void setBalanceAmt(java.math.BigDecimal balanceAmt)
    {
        this.balanceAmt = balanceAmt;
    }
    
    public java.math.BigDecimal getAssetAmt()
    {
        return this.assetAmt;
    }
    
    public void setAssetAmt(java.math.BigDecimal assetAmt)
    {
        this.assetAmt = assetAmt;
    }
    
    public java.math.BigDecimal getAssetFrozenAmt()
    {
        return this.assetFrozenAmt;
    }
    
    public void setAssetFrozenAmt(java.math.BigDecimal assetFrozenAmt)
    {
        this.assetFrozenAmt = assetFrozenAmt;
    }
    
    public java.math.BigDecimal getDebitAmt()
    {
        return this.debitAmt;
    }
    
    public void setDebitAmt(java.math.BigDecimal debitAmt)
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
    
    public String getAccountName()
    {
        return accountName;
    }
    
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    
    public String getStockName()
    {
        return stockName;
    }
    
    public void setStockName(String stockName)
    {
        this.stockName = stockName;
    }
    
    public String getStockCode()
    {
        return stockCode;
    }
    
    public void setStockCode(String stockCode)
    {
        this.stockCode = stockCode;
    }
    
    public String getRelatedStockName()
    {
        return relatedStockName;
    }
    
    public void setRelatedStockName(String relatedStockName)
    {
        this.relatedStockName = relatedStockName;
    }
    
    public String getRelatedStockCode()
    {
        return relatedStockCode;
    }
    
    public void setRelatedStockCode(String relatedStockCode)
    {
        this.relatedStockCode = relatedStockCode;
    }
    
    public Long getStartDate()
    {
        return startDate;
    }
    
    public void setStartDate(Long startDate)
    {
        this.startDate = startDate;
    }
    
    public Long getEndDate()
    {
        return endDate;
    }
    
    public void setEndDate(Long endDate)
    {
        this.endDate = endDate;
    }
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.stockinfo.entity;

import com.blocain.bitms.orm.core.GenericEntity;
import com.blocain.bitms.tools.annotation.ExcelField;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 证券费率表 实体对象
 * <p>File：StockRate.java</p>
 * <p>Title: StockRate</p>
 * <p>Description:StockRate</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class StockRate extends GenericEntity
{
    private static final long  serialVersionUID = 1L;
    
    /**证券内码,证券信息ID*/
    @NotNull(message = "证券内码,证券信息ID不可为空")
    private Long               stockinfoId;
    
    /**证券代码*/
    @ExcelField(title = "证券代码")
    private String             stockCode;
    
    /**证券名称*/
    @ExcelField(title = "证券名称")
    private String             stockName;
    
    /**费用类型(10交易手续费11持仓调节费12结算亏损分摊费13交割手续费)*/
    @NotNull(message = "费用类型不可为空")
    private String             rateType;
    
    /**费用类型(10交易手续费11持仓调节费12结算亏损分摊费13交割手续费)*/
    @ExcelField(title = "费用类型")
    private String             rateTypeName;
    
    /**值类型(1默认比例,2绝对值)*/
    @NotNull(message = "值类型(1默认比例,2绝对值)不可为空")
    private int                rateValueType;
    
    /**费率*/
    @NotNull(message = "费率不可为空")
    @ExcelField(title = "费率")
    private BigDecimal         rate;
    
    /**备注*/
    private String             remark;
    
    /**创建人*/
    private Long               createBy;
    
    /**创建人*/
    @ExcelField(title = "创建人")
    private String             createByName;
    
    /**创建时间*/
    private java.sql.Timestamp createDate;
    
    /**修改人*/
    private Long               updateBy;
    
    /**修改人*/
    @ExcelField(title = "修改人")
    private String             updateByName;
    
    /**修改时间*/
    private java.sql.Timestamp updateDate;
    
    public Long getStockinfoId()
    {
        return stockinfoId;
    }
    
    public void setStockinfoId(Long stockinfoId)
    {
        this.stockinfoId = stockinfoId;
    }
    
    public String getRateType()
    {
        return this.rateType;
    }
    
    public void setRateType(String rateType)
    {
        this.rateType = rateType;
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
    
    public Long getUpdateBy()
    {
        return this.updateBy;
    }
    
    public void setUpdateBy(Long updateBy)
    {
        this.updateBy = updateBy;
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
    
    public String getCreateByName()
    {
        return createByName;
    }
    
    public void setCreateByName(String createByName)
    {
        this.createByName = createByName;
    }
    
    public String getUpdateByName()
    {
        return updateByName;
    }
    
    public void setUpdateByName(String updateByName)
    {
        this.updateByName = updateByName;
    }
    
    public BigDecimal getRate()
    {
        return rate;
    }
    
    public void setRate(BigDecimal rate)
    {
        this.rate = rate;
    }
    
    public String getRateTypeName()
    {
        return this.rateTypeName;
    }
    
    public void setRateTypeName(String rateTypeName)
    {
        this.rateTypeName = rateTypeName;
    }
    
    public Timestamp getCreateDate()
    {
        return createDate;
    }
    
    public void setCreateDate(Timestamp createDate)
    {
        this.createDate = createDate;
    }
    
    public Timestamp getUpdateDate()
    {
        return updateDate;
    }
    
    public void setUpdateDate(Timestamp updateDate)
    {
        this.updateDate = updateDate;
    }
    
    public int getRateValueType()
    {
        return rateValueType;
    }
    
    public void setRateValueType(int rateValueType)
    {
        this.rateValueType = rateValueType;
    }
}

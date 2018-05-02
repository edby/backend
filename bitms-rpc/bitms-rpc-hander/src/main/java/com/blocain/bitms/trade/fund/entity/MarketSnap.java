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
 * 行情快照表 实体对象
 * <p>File：MarketSnap.java</p>
 * <p>Title: MarketSnap</p>
 * <p>Description:MarketSnap</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "行情快照表")
public class MarketSnap extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**交易对证券ID*/
    @NotNull(message = "交易对证券ID不可为空")
    @ApiModelProperty(value = "交易对证券ID", required = true)
    private Long                 pairStockinfoId;
    
    /**内部平台价格*/
    @NotNull(message = "内部平台价格不可为空")
    @ApiModelProperty(value = "内部平台价格", required = true)
    private java.math.BigDecimal paltformPrice;
    
    /**外部指数价格*/
    @NotNull(message = "外部指数价格不可为空")
    @ApiModelProperty(value = "外部指数价格", required = true)
    private java.math.BigDecimal indexPrice;
    
    /**溢价*/
    @NotNull(message = "溢价不可为空")
    @ApiModelProperty(value = "溢价", required = true)
    private java.math.BigDecimal premiumPrice;
    
    /**溢价率*/
    @NotNull(message = "溢价率不可为空")
    @ApiModelProperty(value = "溢价率", required = true)
    private java.math.BigDecimal premiumRate;
    
    /**行情时间戳*/
    @NotNull(message = "行情时间戳不可为空")
    @ApiModelProperty(value = "行情时间戳", required = true)
    private java.util.Date       marketTimestamp;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String               remark;
    
    public Long getPairStockinfoId()
    {
        return this.pairStockinfoId;
    }
    
    public void setPairStockinfoId(Long pairStockinfoId)
    {
        this.pairStockinfoId = pairStockinfoId;
    }
    
    public java.math.BigDecimal getPaltformPrice()
    {
        return this.paltformPrice;
    }
    
    public void setPaltformPrice(java.math.BigDecimal paltformPrice)
    {
        this.paltformPrice = paltformPrice;
    }
    
    public java.math.BigDecimal getIndexPrice()
    {
        return this.indexPrice;
    }
    
    public void setIndexPrice(java.math.BigDecimal indexPrice)
    {
        this.indexPrice = indexPrice;
    }
    
    public java.math.BigDecimal getPremiumPrice()
    {
        return this.premiumPrice;
    }
    
    public void setPremiumPrice(java.math.BigDecimal premiumPrice)
    {
        this.premiumPrice = premiumPrice;
    }
    
    public java.math.BigDecimal getPremiumRate()
    {
        return this.premiumRate;
    }
    
    public void setPremiumRate(java.math.BigDecimal premiumRate)
    {
        this.premiumRate = premiumRate;
    }
    
    public java.util.Date getMarketTimestamp()
    {
        return this.marketTimestamp;
    }
    
    public void setMarketTimestamp(java.util.Date marketTimestamp)
    {
        this.marketTimestamp = marketTimestamp;
    }
    
    public String getRemark()
    {
        return this.remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
}

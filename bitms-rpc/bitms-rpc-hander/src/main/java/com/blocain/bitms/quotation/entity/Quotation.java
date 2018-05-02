/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.quotation.entity;

import com.blocain.bitms.orm.core.GenericEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 行情信息 实体对象
 * <p>File：Quotation.java</p>
 * <p>Title: Quotation</p>
 * <p>Description:Quotation</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "行情信息")
public class Quotation extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**渠道（Bitfinex、Bitstamp、Coinbase）*/
    @ApiModelProperty(value = "渠道（Bitfinex、Bitstamp、Coinbase）")
    private java.lang.String     channel;
    
    /**证券信息id，证券内码(btc、ltc、eth)*/
    @ApiModelProperty(value = "证券信息id，证券内码(btc、ltc、eth)")
    private java.lang.Long       stockId;
    
    /**对应数字货币usd指数点位*/
    @ApiModelProperty(value = "对应数字货币usd指数点位")
    private java.math.BigDecimal idxPrice;
    
    /**对应加权指数*/
    private java.math.BigDecimal idxPriceAvg;
    
    /**对应bitfienex加权指数*/
    private java.math.BigDecimal bitfienexAvg;
    
    /**对应bitstamp加权指数*/
    private java.math.BigDecimal bitstampAvg;
    
    /**对应bitfienex来源ID*/
    private java.lang.Long       bitfienexId;
    
    /**对应bitstamp来源ID*/
    private java.lang.Long       bitstampId;
    
    /**时间*/
    @ApiModelProperty(value = "时间")
    private java.lang.Long       dateTime;
    
    public java.lang.String getChannel()
    {
        return this.channel;
    }
    
    public void setChannel(java.lang.String channel)
    {
        this.channel = channel;
    }
    
    public java.lang.Long getStockId()
    {
        return this.stockId;
    }
    
    public void setStockId(java.lang.Long stockId)
    {
        this.stockId = stockId;
    }
    
    public BigDecimal getIdxPriceAvg()
    {
        return idxPriceAvg;
    }
    
    public void setIdxPriceAvg(BigDecimal idxPriceAvg)
    {
        this.idxPriceAvg = idxPriceAvg;
    }
    
    public java.lang.Long getDateTime()
    {
        return this.dateTime;
    }
    
    public void setDateTime(java.lang.Long dateTime)
    {
        this.dateTime = dateTime;
    }
    
    public BigDecimal getBitfienexAvg()
    {
        return bitfienexAvg;
    }
    
    public void setBitfienexAvg(BigDecimal bitfienexAvg)
    {
        this.bitfienexAvg = bitfienexAvg;
    }
    
    public BigDecimal getBitstampAvg()
    {
        return bitstampAvg;
    }
    
    public void setBitstampAvg(BigDecimal bitstampAvg)
    {
        this.bitstampAvg = bitstampAvg;
    }
    
    public Long getBitfienexId()
    {
        return bitfienexId;
    }
    
    public void setBitfienexId(Long bitfienexId)
    {
        this.bitfienexId = bitfienexId;
    }
    
    public Long getBitstampId()
    {
        return bitstampId;
    }
    
    public void setBitstampId(Long bitstampId)
    {
        this.bitstampId = bitstampId;
    }
    
    public BigDecimal getIdxPrice()
    {
        return idxPrice;
    }
    
    public void setIdxPrice(BigDecimal idxPrice)
    {
        this.idxPrice = idxPrice;
    }
    
    @Override
    public String toString()
    {
        return "Quotation{" + "channel='" + channel + '\'' + ", stockId=" + stockId + ", idxPrice=" + idxPrice + ", idxPriceAvg=" + idxPriceAvg + ", bitfienexAvg="
                + bitfienexAvg + ", bitstampAvg=" + bitstampAvg + ", bitfienexId=" + bitfienexId + ", bitstampId=" + bitstampId + ", dateTime=" + dateTime + '}';
    }
}

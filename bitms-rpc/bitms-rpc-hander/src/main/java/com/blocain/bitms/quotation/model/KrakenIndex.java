package com.blocain.bitms.quotation.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class KrakenIndex implements Serializable
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 9007854761313652623L;
    
    public KrakenIndex(String pair)
    {
        this.pair = pair;
    }
    
    // currency
    private String     pair;
    
    // 最新报价
    private BigDecimal last;
    
    // 过去24小时最高价
    private BigDecimal high;
    
    // 过去24小时最低价
    private BigDecimal low;
    
    // 过去24小时volume
    private BigDecimal volume;
    
    // 过去24小时加权平均值
    private BigDecimal weighted_avg;
    
    public String getPair()
    {
        return pair;
    }
    
    public void setPair(String pair)
    {
        this.pair = pair;
    }
    
    public BigDecimal getLast()
    {
        return last;
    }
    
    public void setLast(BigDecimal last)
    {
        this.last = last;
    }
    
    public BigDecimal getHigh()
    {
        return high;
    }
    
    public void setHigh(BigDecimal high)
    {
        this.high = high;
    }
    
    public BigDecimal getLow()
    {
        return low;
    }
    
    public void setLow(BigDecimal low)
    {
        this.low = low;
    }
    
    public BigDecimal getVolume()
    {
        return volume;
    }
    
    public void setVolume(BigDecimal volume)
    {
        this.volume = volume;
    }
    
    public BigDecimal getWeighted_avg()
    {
        return weighted_avg;
    }
    
    public void setWeighted_avg(BigDecimal weighted_avg)
    {
        this.weighted_avg = weighted_avg;
    }
    
    @Override
    public String toString()
    {
        return "KrakenIndex{" + "pair='" + pair + '\'' + ", last=" + last + ", high=" + high + ", low=" + low + ", volume=" + volume + ", weighted_avg=" + weighted_avg
                + '}';
    }
}

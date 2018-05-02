package com.blocain.bitms.quotation.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 行情对象 Introduce
 * <p>File：QuotationParam.java</p>
 * <p>Title: QuotationParam</p>
 * <p>Description: QuotationParam</p>
 * <p>Copyright: Copyright (c) 2017/6/28</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class QuotationParam implements Serializable
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 469353538990194265L;
    
    /**
     * 渠道来源(如：Bitfinex、Bitstamp、Coinbase)
     */
    private String            channel;
    
    /**
     * 货币类型 (如:比特币、以太币、莱特币)
     */
    private String            currency;
    
    /**
     * 美元价格
     */
    private BigDecimal        usd;
    
    /**
     * 日期
     */
    private Long              date;
    
    public QuotationParam(String channel, String currency)
    {
        this.channel = channel;
        this.currency = currency;
    }
    
    public String getChannel()
    {
        return channel;
    }
    
    public void setChannel(String channel)
    {
        this.channel = channel;
    }
    
    public String getCurrency()
    {
        return currency;
    }
    
    public void setCurrency(String currency)
    {
        this.currency = currency;
    }
    
    public BigDecimal getUsd()
    {
        return usd;
    }
    
    public void setUsd(BigDecimal usd)
    {
        this.usd = usd;
    }
    
    public Long getDate()
    {
        return date;
    }
    
    public void setDate(Long date)
    {
        this.date = date;
    }
}

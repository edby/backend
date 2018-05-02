package com.blocain.bitms.quotation.model;

import java.io.Serializable;

/**
 * Bitstamp行情指数对象
 * <p>File：Bitstamp.java</p>
 * <p>Title: Bitstamp</p>
 * <p>Description:
 * https://www.bitstamp.net/api/v2/ticker/btcusd
 * </p>
 * <p>Copyright: Copyright (c) 2017/6/28</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class BitstampIndex implements Serializable
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 2827649262079693919L;
    
    // Last 24 hours price high.
    private String high;
    
    // Last BTC price.
    private String last;
    
    // Unix timestamp date and time.
    private String timestamp;
    
    // Highest buy order.
    private String bid;
    
    // Last 24 hours volume weighted average price.
    private String vwap;
    
    // Last 24 hours volume.
    private String volume;
    
    // Last 24 hours price low.
    private String low;
    
    // Lowest sell order.
    private String ask;
    
    // First price of the day.
    private String open;
    
    public String getHigh()
    {
        return high;
    }
    
    public void setHigh(String high)
    {
        this.high = high;
    }
    
    public String getLast()
    {
        return last;
    }
    
    public void setLast(String last)
    {
        this.last = last;
    }
    
    public String getTimestamp()
    {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }
    
    public String getBid()
    {
        return bid;
    }
    
    public void setBid(String bid)
    {
        this.bid = bid;
    }
    
    public String getVwap()
    {
        return vwap;
    }
    
    public void setVwap(String vwap)
    {
        this.vwap = vwap;
    }
    
    public String getVolume()
    {
        return volume;
    }
    
    public void setVolume(String volume)
    {
        this.volume = volume;
    }
    
    public String getLow()
    {
        return low;
    }
    
    public void setLow(String low)
    {
        this.low = low;
    }
    
    public String getAsk()
    {
        return ask;
    }
    
    public void setAsk(String ask)
    {
        this.ask = ask;
    }
    
    public String getOpen()
    {
        return open;
    }
    
    public void setOpen(String open)
    {
        this.open = open;
    }
}

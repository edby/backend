package com.blocain.bitms.quotation.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Bitfinex行情指数对象
 * <p>File：Bitfinex.java</p>
 * <p>Title: Bitfinex</p>
 * <p>Description:
 * 请用地址：
 *      https://api.bitfinex.com/v2/tickers?symbols=tBTCUSD,tLTCUSD,tETHUSD
 * </p>
 * <p>Copyright: Copyright (c) 2017/6/28</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class BitfinexIndex implements Serializable
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 6286606917691397823L;
    
    private String     SYMBOL;
    
    // Price of last highest bid
    private BigDecimal BID;
    
    // Bid period covered in days
    private BigDecimal BID_SIZE;
    
    // Price of last lowest ask
    private BigDecimal ASK;
    
    // Size of the last lowest ask
    private BigDecimal ASK_SIZE;
    
    // Amount that the last price has changed since yesterday
    private BigDecimal DAILY_CHANGE;
    
    // Amount that the price has changed expressed in percentage terms
    private BigDecimal DAILY_CHANGE_PERC;
    
    // Price of the last trade
    private BigDecimal LAST_PRICE;
    
    // Daily volume
    private BigDecimal VOLUME;
    
    // Daily high
    private BigDecimal HIGH;
    
    // Daily low
    private BigDecimal LOW;
    
    public BitfinexIndex()
    {
    }
    
    public BitfinexIndex(String SYMBOL)
    {
        this.SYMBOL = SYMBOL;
    }
    
    public String getSYMBOL()
    {
        return SYMBOL;
    }
    
    public void setSYMBOL(String SYMBOL)
    {
        this.SYMBOL = SYMBOL;
    }
    
    public BigDecimal getBID()
    {
        return BID;
    }
    
    public void setBID(BigDecimal BID)
    {
        this.BID = BID;
    }
    
    public BigDecimal getBID_SIZE()
    {
        return BID_SIZE;
    }
    
    public void setBID_SIZE(BigDecimal BID_SIZE)
    {
        this.BID_SIZE = BID_SIZE;
    }
    
    public BigDecimal getASK()
    {
        return ASK;
    }
    
    public void setASK(BigDecimal ASK)
    {
        this.ASK = ASK;
    }
    
    public BigDecimal getASK_SIZE()
    {
        return ASK_SIZE;
    }
    
    public void setASK_SIZE(BigDecimal ASK_SIZE)
    {
        this.ASK_SIZE = ASK_SIZE;
    }
    
    public BigDecimal getDAILY_CHANGE()
    {
        return DAILY_CHANGE;
    }
    
    public void setDAILY_CHANGE(BigDecimal DAILY_CHANGE)
    {
        this.DAILY_CHANGE = DAILY_CHANGE;
    }
    
    public BigDecimal getDAILY_CHANGE_PERC()
    {
        return DAILY_CHANGE_PERC;
    }
    
    public void setDAILY_CHANGE_PERC(BigDecimal DAILY_CHANGE_PERC)
    {
        this.DAILY_CHANGE_PERC = DAILY_CHANGE_PERC;
    }
    
    public BigDecimal getLAST_PRICE()
    {
        return LAST_PRICE;
    }
    
    public void setLAST_PRICE(BigDecimal LAST_PRICE)
    {
        this.LAST_PRICE = LAST_PRICE;
    }
    
    public BigDecimal getVOLUME()
    {
        return VOLUME;
    }
    
    public void setVOLUME(BigDecimal VOLUME)
    {
        this.VOLUME = VOLUME;
    }
    
    public BigDecimal getHIGH()
    {
        return HIGH;
    }
    
    public void setHIGH(BigDecimal HIGH)
    {
        this.HIGH = HIGH;
    }
    
    public BigDecimal getLOW()
    {
        return LOW;
    }
    
    public void setLOW(BigDecimal LOW)
    {
        this.LOW = LOW;
    }
}

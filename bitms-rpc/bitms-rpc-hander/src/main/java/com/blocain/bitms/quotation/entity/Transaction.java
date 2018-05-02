package com.blocain.bitms.quotation.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transaction implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Timestamp         dealTime;             // 成交时间

    private String        dealPrice;            // 成交价

    private String        dealAmt;              // 成交数量

    private String            Direct;               // 主动成交方向

    public Timestamp getDealTime()
    {
        return dealTime;
    }

    public void setDealTime(Timestamp dealTime)
    {
        this.dealTime = dealTime;
    }

    public String getDealPrice()
    {
        return dealPrice;
    }

    public void setDealPrice(String dealPrice)
    {
        this.dealPrice = dealPrice;
    }

    public String getDealAmt()
    {
        return dealAmt;
    }

    public void setDealAmt(String dealAmt)
    {
        this.dealAmt = dealAmt;
    }

    public String getDirect()
    {
        return Direct;
    }

    public void setDirect(String direct)
    {
        Direct = direct;
    }
}

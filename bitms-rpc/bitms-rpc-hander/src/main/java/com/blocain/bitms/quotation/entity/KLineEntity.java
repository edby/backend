package com.blocain.bitms.quotation.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * K线图
 */
public class KLineEntity implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    private Long       id;

    private Timestamp  displayTime; // 行情展示时间

    private Timestamp  quotationTime; //实际行情时间

    private BigDecimal highestPrice; // 最高价

    private BigDecimal lowestPrice; // 最低价

    private BigDecimal openPrice; // 开盘价

    private BigDecimal closePrice; // 收盘价

    private BigDecimal dealBal; // 成交金额

    private BigDecimal dealAmtSum; // 成交数量

    private BigDecimal accumulatedBal; //累计成交额

    private BigDecimal accumulatedAmt; //累计成交量

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Timestamp getDisplayTime()
    {
        return displayTime;
    }

    public void setDisplayTime(Timestamp displayTime)
    {
        this.displayTime = displayTime;
    }

    public Timestamp getQuotationTime()
    {
        return quotationTime;
    }

    public void setQuotationTime(Timestamp quotationTime)
    {
        this.quotationTime = quotationTime;
    }

    public BigDecimal getHighestPrice()
    {
        return highestPrice;
    }

    public void setHighestPrice(BigDecimal highestPrice)
    {
        this.highestPrice = highestPrice;
    }

    public BigDecimal getLowestPrice()
    {
        return lowestPrice;
    }

    public void setLowestPrice(BigDecimal lowestPrice)
    {
        this.lowestPrice = lowestPrice;
    }

    public BigDecimal getOpenPrice()
    {
        return openPrice;
    }

    public void setOpenPrice(BigDecimal openPrice)
    {
        this.openPrice = openPrice;
    }

    public BigDecimal getClosePrice()
    {
        return closePrice;
    }

    public void setClosePrice(BigDecimal closePrice)
    {
        this.closePrice = closePrice;
    }

    public BigDecimal getDealBal()
    {
        return dealBal;
    }

    public void setDealBal(BigDecimal dealBal)
    {
        this.dealBal = dealBal;
    }

    public BigDecimal getDealAmtSum()
    {
        return dealAmtSum;
    }

    public void setDealAmtSum(BigDecimal dealAmtSum)
    {
        this.dealAmtSum = dealAmtSum;
    }

    public BigDecimal getAccumulatedBal()
    {
        return accumulatedBal;
    }

    public void setAccumulatedBal(BigDecimal accumulatedBal)
    {
        this.accumulatedBal = accumulatedBal;
    }

    public BigDecimal getAccumulatedAmt()
    {
        return accumulatedAmt;
    }

    public void setAccumulatedAmt(BigDecimal accumulatedAmt)
    {
        this.accumulatedAmt = accumulatedAmt;
    }

    @Override public String toString()
    {
        return "KLineEntity{" + "id=" + id + ", displayTime=" + displayTime + ", quotationTime=" + quotationTime + ", highestPrice=" + highestPrice + ", lowestPrice="
                + lowestPrice + ", openPrice=" + openPrice + ", closePrice=" + closePrice + ", dealBal=" + dealBal + ", dealAmtSum=" + dealAmtSum + ", accumulatedBal="
                + accumulatedBal + ", accumulatedAmt=" + accumulatedAmt + '}';
    }
}

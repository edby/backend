package com.blocain.bitms.quotation.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Real time Quotation information
 * 实时行情信息
 */
public class RtQuotationInfo implements java.io.Serializable
{
    private static final long serialVersionUID = -5070399515553795258L;
    
    private BigDecimal        platPrice;                               // 最新平台行情
    
    private BigDecimal        idxPrice;                                // 指数价格
    
    private BigDecimal        idxAvgPrice;                             // 指数加权价
    
    private BigDecimal        entrustSellOne;                          // 委托卖1
    
    private BigDecimal        entrustBuyOne;                           // 委托买1
    
    private BigDecimal        buyHighestLimitPrice;                    // 大限价，最高买入
    
    private BigDecimal        buyLowestLimitPrice;                     // 大限价，最低买入
    
    private BigDecimal        sellHighestLimitPrice;                   // 大限价，最高卖出
    
    private BigDecimal        sellLowestLimitPrice;                    // 大限价，最低卖出
    
    private double            range;                                   // 涨跌幅
    
    private String            direct;                                  // 主动交易方向(控制行情的显示颜色 买入为涨，卖出为跌)
    
    private String            upDown;                                  // 涨跌幅方向 (up 为涨，down 为跌)
    
    private String            upDownIdx;                               // 指数涨跌方向 (up 为涨，down 为跌)
    
    private BigDecimal        vcoinAmtSum24h;                          // 24小时数字货币成交量
    
    private BigDecimal        highestPrice;                            // 最高成交价
    
    private BigDecimal        lowestPrice;                             // 最低成交价
    
    private Timestamp         quotationTime;                           // 行情时间
    
    private BigDecimal        dealAmt;                                 // 成交数量
    
    private BigDecimal        dealBalance;                             // 成交额
    
    private BigDecimal        premium;                                 // 溢价
    
    private BigDecimal        premiumRate;                             // 溢价率
    
    public BigDecimal getPremium()
    {
        return premium;
    }
    
    public void setPremium(BigDecimal premium)
    {
        this.premium = premium;
    }
    
    public BigDecimal getPremiumRate()
    {
        return premiumRate;
    }
    
    public void setPremiumRate(BigDecimal premiumRate)
    {
        this.premiumRate = premiumRate;
    }
    
    public BigDecimal getDealBalance()
    {
        return dealBalance;
    }
    
    public void setDealBalance(BigDecimal dealBalance)
    {
        this.dealBalance = dealBalance;
    }
    
    public BigDecimal getPlatPrice()
    {
        return platPrice;
    }
    
    public void setPlatPrice(BigDecimal platPrice)
    {
        this.platPrice = platPrice;
    }
    
    public BigDecimal getIdxPrice()
    {
        return idxPrice;
    }
    
    public void setIdxPrice(BigDecimal idxPrice)
    {
        this.idxPrice = idxPrice;
    }
    
    public BigDecimal getIdxAvgPrice()
    {
        return idxAvgPrice;
    }
    
    public void setIdxAvgPrice(BigDecimal idxAvgPrice)
    {
        this.idxAvgPrice = idxAvgPrice;
    }
    
    public BigDecimal getBuyHighestLimitPrice()
    {
        return buyHighestLimitPrice;
    }
    
    public void setBuyHighestLimitPrice(BigDecimal buyHighestLimitPrice)
    {
        this.buyHighestLimitPrice = buyHighestLimitPrice;
    }
    
    public BigDecimal getBuyLowestLimitPrice()
    {
        return buyLowestLimitPrice;
    }
    
    public void setBuyLowestLimitPrice(BigDecimal buyLowestLimitPrice)
    {
        this.buyLowestLimitPrice = buyLowestLimitPrice;
    }
    
    public BigDecimal getSellHighestLimitPrice()
    {
        return sellHighestLimitPrice;
    }
    
    public void setSellHighestLimitPrice(BigDecimal sellHighestLimitPrice)
    {
        this.sellHighestLimitPrice = sellHighestLimitPrice;
    }
    
    public BigDecimal getSellLowestLimitPrice()
    {
        return sellLowestLimitPrice;
    }
    
    public void setSellLowestLimitPrice(BigDecimal sellLowestLimitPrice)
    {
        this.sellLowestLimitPrice = sellLowestLimitPrice;
    }
    
    public double getRange()
    {
        return range;
    }
    
    public void setRange(double range)
    {
        this.range = range;
    }
    
    public String getDirect()
    {
        return direct;
    }
    
    public void setDirect(String direct)
    {
        this.direct = direct;
    }
    
    public String getUpDown()
    {
        return upDown;
    }
    
    public void setUpDown(String upDown)
    {
        this.upDown = upDown;
    }
    
    public String getUpDownIdx()
    {
        return upDownIdx;
    }
    
    public void setUpDownIdx(String upDownIdx)
    {
        this.upDownIdx = upDownIdx;
    }
    
    public BigDecimal getVcoinAmtSum24h()
    {
        return vcoinAmtSum24h;
    }
    
    public void setVcoinAmtSum24h(BigDecimal vcoinAmtSum24h)
    {
        this.vcoinAmtSum24h = vcoinAmtSum24h;
    }
    
    public BigDecimal getEntrustSellOne()
    {
        return entrustSellOne;
    }
    
    public void setEntrustSellOne(BigDecimal entrustSellOne)
    {
        this.entrustSellOne = entrustSellOne;
    }
    
    public BigDecimal getEntrustBuyOne()
    {
        return entrustBuyOne;
    }
    
    public void setEntrustBuyOne(BigDecimal entrustBuyOne)
    {
        this.entrustBuyOne = entrustBuyOne;
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
    
    public Timestamp getQuotationTime()
    {
        return quotationTime;
    }
    
    public void setQuotationTime(Timestamp quotationTime)
    {
        this.quotationTime = quotationTime;
    }
    
    public BigDecimal getDealAmt()
    {
        return dealAmt;
    }
    
    public void setDealAmt(BigDecimal dealAmt)
    {
        this.dealAmt = dealAmt;
    }

    @Override
    public String toString() {
        return "RtQuotationInfo{" +
                "platPrice=" + platPrice +
                ", idxPrice=" + idxPrice +
                ", idxAvgPrice=" + idxAvgPrice +
                ", entrustSellOne=" + entrustSellOne +
                ", entrustBuyOne=" + entrustBuyOne +
                ", buyHighestLimitPrice=" + buyHighestLimitPrice +
                ", buyLowestLimitPrice=" + buyLowestLimitPrice +
                ", sellHighestLimitPrice=" + sellHighestLimitPrice +
                ", sellLowestLimitPrice=" + sellLowestLimitPrice +
                ", range=" + range +
                ", direct='" + direct + '\'' +
                ", upDown='" + upDown + '\'' +
                ", upDownIdx='" + upDownIdx + '\'' +
                ", vcoinAmtSum24h=" + vcoinAmtSum24h +
                ", highestPrice=" + highestPrice +
                ", lowestPrice=" + lowestPrice +
                ", quotationTime=" + quotationTime +
                ", dealAmt=" + dealAmt +
                ", dealBalance=" + dealBalance +
                ", premium=" + premium +
                ", premiumRate=" + premiumRate +
                '}';
    }
}

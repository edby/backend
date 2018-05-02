package com.blocain.bitms.quotation.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Real time Quotation information
 * 实时行情信息
 */
public class RtQuotationInfoDto implements java.io.Serializable
{
    private static final long serialVersionUID = -5070399515553795258L;
    
    private String            platPrice;                               // 最新平台行情
    
    private String            idxPrice;                                // 指数价格
    
    private String            idxAvgPrice;                             // 指数加权价
    
    private String            premium;                                 // 溢价
    
    private String            premiumRate;                             // 溢价率
    
    private String            entrustSellOne;                          // 委托卖1
    
    private String            entrustBuyOne;                           // 委托买1
    
    private String            buyHighestLimitPrice;                    // 大限价，最高买入
    
    private String            buyLowestLimitPrice;                     // 大限价，最低买入
    
    private String            sellHighestLimitPrice;                   // 大限价，最高卖出
    
    private String            sellLowestLimitPrice;                    // 大限价，最低卖出
    
    private double            range;                                   // 涨跌幅
    
    private String            direct;                                  // 主动交易方向(控制行情的显示颜色 买入为涨，卖出为跌)
    
    private String            upDown;                                  // 涨跌幅方向 (up 为涨，down 为跌)
    
    private String            upDownIdx;                               // 指数涨跌方向 (up 为涨，down 为跌)
    
    private String            vcoinAmtSum24h;                          // 24小时数字货币成交量
    
    private String            highestPrice;                            // 最高成交价
    
    private String            lowestPrice;                             // 最低成交价
    
    private Timestamp         quotationTime;                           // 行情时间
    
    private String            dealAmt;                                 // 成交数量
    
    private String            dealBalance;                             // 成交额
    
    public String getPremium()
    {
        return premium;
    }
    
    public void setPremium(String premium)
    {
        this.premium = premium;
    }
    
    public String getPremiumRate()
    {
        return premiumRate;
    }
    
    public void setPremiumRate(String premiumRate)
    {
        this.premiumRate = premiumRate;
    }
    
    public String getPlatPrice()
    {
        return platPrice;
    }
    
    public void setPlatPrice(String platPrice)
    {
        this.platPrice = platPrice;
    }
    
    public String getIdxPrice()
    {
        return idxPrice;
    }
    
    public void setIdxPrice(String idxPrice)
    {
        this.idxPrice = idxPrice;
    }
    
    public String getIdxAvgPrice()
    {
        return idxAvgPrice;
    }
    
    public void setIdxAvgPrice(String idxAvgPrice)
    {
        this.idxAvgPrice = idxAvgPrice;
    }
    
    public String getEntrustSellOne()
    {
        return entrustSellOne;
    }
    
    public void setEntrustSellOne(String entrustSellOne)
    {
        this.entrustSellOne = entrustSellOne;
    }
    
    public String getEntrustBuyOne()
    {
        return entrustBuyOne;
    }
    
    public void setEntrustBuyOne(String entrustBuyOne)
    {
        this.entrustBuyOne = entrustBuyOne;
    }
    
    public String getBuyHighestLimitPrice()
    {
        return buyHighestLimitPrice;
    }
    
    public void setBuyHighestLimitPrice(String buyHighestLimitPrice)
    {
        this.buyHighestLimitPrice = buyHighestLimitPrice;
    }
    
    public String getBuyLowestLimitPrice()
    {
        return buyLowestLimitPrice;
    }
    
    public void setBuyLowestLimitPrice(String buyLowestLimitPrice)
    {
        this.buyLowestLimitPrice = buyLowestLimitPrice;
    }
    
    public String getSellHighestLimitPrice()
    {
        return sellHighestLimitPrice;
    }
    
    public void setSellHighestLimitPrice(String sellHighestLimitPrice)
    {
        this.sellHighestLimitPrice = sellHighestLimitPrice;
    }
    
    public String getSellLowestLimitPrice()
    {
        return sellLowestLimitPrice;
    }
    
    public void setSellLowestLimitPrice(String sellLowestLimitPrice)
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
    
    public String getVcoinAmtSum24h()
    {
        return vcoinAmtSum24h;
    }
    
    public void setVcoinAmtSum24h(String vcoinAmtSum24h)
    {
        this.vcoinAmtSum24h = vcoinAmtSum24h;
    }
    
    public String getHighestPrice()
    {
        return highestPrice;
    }
    
    public void setHighestPrice(String highestPrice)
    {
        this.highestPrice = highestPrice;
    }
    
    public String getLowestPrice()
    {
        return lowestPrice;
    }
    
    public void setLowestPrice(String lowestPrice)
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
    
    public String getDealAmt()
    {
        return dealAmt;
    }
    
    public void setDealAmt(String dealAmt)
    {
        this.dealAmt = dealAmt;
    }
    
    public String getDealBalance()
    {
        return dealBalance;
    }
    
    public void setDealBalance(String dealBalance)
    {
        this.dealBalance = dealBalance;
    }
    
    @Override
    public String toString()
    {
        return "RtQuotationInfoDto{" + "platPrice='" + platPrice + '\'' + ", idxPrice='" + idxPrice + '\'' + ", idxAvgPrice='" + idxAvgPrice + '\'' + ", premium='"
                + premium + '\'' + ", premiumRate='" + premiumRate + '\'' + ", entrustSellOne='" + entrustSellOne + '\'' + ", entrustBuyOne='" + entrustBuyOne + '\''
                + ", buyHighestLimitPrice='" + buyHighestLimitPrice + '\'' + ", buyLowestLimitPrice='" + buyLowestLimitPrice + '\'' + ", sellHighestLimitPrice='"
                + sellHighestLimitPrice + '\'' + ", sellLowestLimitPrice='" + sellLowestLimitPrice + '\'' + ", range=" + range + ", direct='" + direct + '\'' + ", upDown='"
                + upDown + '\'' + ", upDownIdx='" + upDownIdx + '\'' + ", vcoinAmtSum24h='" + vcoinAmtSum24h + '\'' + ", highestPrice='" + highestPrice + '\''
                + ", lowestPrice='" + lowestPrice + '\'' + ", quotationTime=" + quotationTime + ", dealAmt='" + dealAmt + '\'' + ", dealBalance='" + dealBalance + '\''
                + '}';
    }
}

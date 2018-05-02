package com.blocain.bitms.quotation.entity;

import java.math.BigDecimal;

/**
 * 行情关键信息类
 */
public class MainRtQuotationInfo implements java.io.Serializable, Comparable
{
    private static final long serialVersionUID = -4900608239740805674L;
    
    // 交易对id
    private String            id;
    
    // 交易对类型
    private String            stockType;
    
    // 交易对名称
    private String            stockName;
    
    // 备注
    private String            exchangePair;
    
    // 最新成交价
    private String            platPrice;
    
    // 涨跌幅
    private String            range;
    
    // 涨跌幅方向
    private String            upDown;
    
    // 24小时成交量
    private String            volume;
    
    // 24小时最低价
    private String            lowestPrice;
    
    // 24小时最高价
    private String            highestPrice;
    
    private String            market;
    
    // token 地址，用于页面跳转
    private String            tokencontactaddr;
    
    public String getMarket()
    {
        return market;
    }
    
    public void setMarket(String market)
    {
        this.market = market;
    }
    
    public String getLowestPrice()
    {
        return lowestPrice;
    }
    
    public void setLowestPrice(String lowestPrice)
    {
        this.lowestPrice = lowestPrice;
    }
    
    public String getHighestPrice()
    {
        return highestPrice;
    }
    
    public void setHighestPrice(String highestPrice)
    {
        this.highestPrice = highestPrice;
    }
    
    public String getExchangePair()
    {
        return exchangePair;
    }
    
    public void setExchangePair(String exchangePair)
    {
        this.exchangePair = exchangePair;
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getStockType()
    {
        return stockType;
    }
    
    public void setStockType(String stockType)
    {
        this.stockType = stockType;
    }
    
    public String getStockName()
    {
        return stockName;
    }
    
    public void setStockName(String stockName)
    {
        this.stockName = stockName;
    }
    
    public String getPlatPrice()
    {
        return platPrice;
    }
    
    public void setPlatPrice(String platPrice)
    {
        this.platPrice = platPrice;
    }
    
    public String getRange()
    {
        return range;
    }
    
    public void setRange(String range)
    {
        this.range = range;
    }
    
    public String getUpDown()
    {
        return upDown;
    }
    
    public void setUpDown(String upDown)
    {
        this.upDown = upDown;
    }
    
    public String getVolume()
    {
        return volume;
    }
    
    public void setVolume(String volume)
    {
        this.volume = volume;
    }
    
    public String getTokencontactaddr()
    {
        return tokencontactaddr;
    }
    
    public void setTokencontactaddr(String tokencontactaddr)
    {
        this.tokencontactaddr = tokencontactaddr;
    }
    
    @Override
    public String toString()
    {
        return "MainRtQuotationInfo{" + "id='" + id + '\'' + ", stockType='" + stockType + '\'' + ", stockName='" + stockName + '\'' + ", exchangePair='" + exchangePair
                + '\'' + ", platPrice='" + platPrice + '\'' + ", range='" + range + '\'' + ", upDown='" + upDown + '\'' + ", volume='" + volume + '\'' + ", lowestPrice='"
                + lowestPrice + '\'' + ", highestPrice='" + highestPrice + '\'' + ", market='" + market + '\'' + ", tokencontactaddr='" + tokencontactaddr + '\'' + '}';
    }
    
    @Override
    public int compareTo(Object o)
    {
        MainRtQuotationInfo info = (MainRtQuotationInfo) o;
        BigDecimal a = new BigDecimal(this.range);
        BigDecimal b = new BigDecimal(info.range);
        return b.compareTo(a);
    }
    // public static void main(String[] args) {
    // List<MainRtQuotationInfo> list = new ArrayList<MainRtQuotationInfo>();
    // MainRtQuotationInfo a = new MainRtQuotationInfo();
    // a.setRange("3");
    // MainRtQuotationInfo b = new MainRtQuotationInfo();
    // b.setRange("3");
    // list.add(a);
    // list.add(b);
    // Collections.sort(list);
    // System.out.println(list.get(0).getRange());
    // }
}

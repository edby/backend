package com.blocain.bitms.quotation.entity;

/**
 * K线图
 */
public class KLine implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    
    // private String quotationTime; // 行情时间

    private String            a;                    // 行情时间
    
    private String            b;                    // 开盘价
    
    private String            c;                    // 收盘价
    
    private String            d;                    // 最低价
    
    private String            e;                    // 最高价
    
    private String            f;                    // 成交金额
    
    private String            g;                    // 成交数量
    // private String highestPrice; // 最高价
    //
    // private String lowestPrice; // 最低价
    //
    // private String openPrice; // 开盘价
    //
    // private String closePrice; // 收盘价
    //
    // private String dealBal; // 成交金额
    //
    // private String dealAmtSum; // 成交数量
    
    public String getA()
    {
        return a;
    }
    
    public void setA(String a)
    {
        this.a = a;
    }
    
    public String getB()
    {
        return b;
    }
    
    public void setB(String b)
    {
        this.b = b;
    }
    
    public String getC()
    {
        return c;
    }
    
    public void setC(String c)
    {
        this.c = c;
    }
    
    public String getD()
    {
        return d;
    }
    
    public void setD(String d)
    {
        this.d = d;
    }
    
    public String getE()
    {
        return e;
    }
    
    public void setE(String e)
    {
        this.e = e;
    }
    
    public String getF()
    {
        return f;
    }
    
    public void setF(String f)
    {
        this.f = f;
    }
    
    public String getG()
    {
        return g;
    }
    
    public void setG(String g)
    {
        this.g = g;
    }

    @Override
    public String toString() {
        return "KLine{" +
                "a='" + a + '\'' +
                ", b='" + b + '\'' +
                ", c='" + c + '\'' +
                ", d='" + d + '\'' +
                ", e='" + e + '\'' +
                ", f='" + f + '\'' +
                ", g='" + g + '\'' +
                '}';
    }

    public KLine getObject(String priceTime, String openPrice, String closePrice, String lowestPrice, String hightestPrice, String dealbal, String dealAmtSum)
    {
        this.a = priceTime;
        this.b = openPrice;
        this.c = closePrice;
        this.d = lowestPrice;
        this.e = hightestPrice;
        this.f = dealbal;
        this.g = dealAmtSum;
        return this;

    }
    // public String getHighestPrice()
    // {
    // return highestPrice;
    // }
    //
    // public void setHighestPrice(String highestPrice)
    // {
    // this.highestPrice = highestPrice;
    // }
    //
    // public String getLowestPrice()
    // {
    // return lowestPrice;
    // }
    //
    // public void setLowestPrice(String lowestPrice)
    // {
    // this.lowestPrice = lowestPrice;
    // }
    //
    // public String getOpenPrice()
    // {
    // return openPrice;
    // }
    //
    // public void setOpenPrice(String openPrice)
    // {
    // this.openPrice = openPrice;
    // }
    //
    // public String getClosePrice()
    // {
    // return closePrice;
    // }
    //
    // public void setClosePrice(String closePrice)
    // {
    // this.closePrice = closePrice;
    // }
    //
    // public String getDealBal()
    // {
    // return dealBal;
    // }
    //
    // public void setDealBal(String dealBal)
    // {
    // this.dealBal = dealBal;
    // }
    //
    // public String getDealAmtSum()
    // {
    // return dealAmtSum;
    // }
    //
    // public void setDealAmtSum(String dealAmtSum)
    // {
    // this.dealAmtSum = dealAmtSum;
    // }
}

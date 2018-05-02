package com.blocain.bitms.quotation.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class DeepPrice implements Serializable
{
    private static final long   serialVersionUID = 1L;

    private              String desc;                 // 标题

    private              String entrustPrice;         // 委托价格

    private              String entrustAmt;           // 委托剩余数量

    private              String entrustAmtSum;        // 委托累计数量

    private              int    deepLevel;            // 行情深度

    private              int    entrustAccountType;   // 委托账户类型(0用户委托、1平台委托)

    private              String direct;               // 委托方向

    private              double entBalRatio;          //累计数量占比

    public String getEntrustPrice()
    {
        return entrustPrice;
    }

    public void setEntrustPrice(String entrustPrice)
    {
        this.entrustPrice = entrustPrice;
    }

    public String getEntrustAmt()
    {
        return entrustAmt;
    }

    public void setEntrustAmt(String entrustAmt)
    {
        this.entrustAmt = entrustAmt;
    }

    public String getEntrustAmtSum()
    {
        return entrustAmtSum;
    }

    public void setEntrustAmtSum(String entrustAmtSum)
    {
        this.entrustAmtSum = entrustAmtSum;
    }

    public int getDeepLevel()
    {
        return deepLevel;
    }

    public void setDeepLevel(int deepLevel)
    {
        this.deepLevel = deepLevel;
    }

    public String getDirect()
    {
        return direct;
    }

    public void setDirect(String direct)
    {
        this.direct = direct;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public int getEntrustAccountType()
    {
        return entrustAccountType;
    }

    public void setEntrustAccountType(int entrustAccountType)
    {
        this.entrustAccountType = entrustAccountType;
    }

    public double getEntBalRatio() { return entBalRatio; }

    public void setEntBalRatio(double entBalRatio) { this.entBalRatio = entBalRatio; }
}

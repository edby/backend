package com.blocain.bitms.quotation.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class QuotationServiceConfig implements Serializable
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1013982410917020423L;

    //服务名称
    public String     serverName;

    //业务表 委托表
    public String     tblEntrust;

    //业务表 成交表
    public String     tblRealDeal;

    //转换标志
    public String     bizConvert;

    //本币
    public String     bizBaseCur;

    //虚拟币
    public String     bizVirtualCur;

    //价格上限默认值
    public BigDecimal upRateDefault;

    //价格下限默认值
    public BigDecimal downRateDefault;

    public String getServerName()
    {
        return serverName;
    }

    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }

    public String getTblEntrust()
    {
        return tblEntrust;
    }

    public void setTblEntrust(String tblEntrust)
    {
        this.tblEntrust = tblEntrust;
    }

    public String getTblRealDeal()
    {
        return tblRealDeal;
    }

    public void setTblRealDeal(String tblRealDeal)
    {
        this.tblRealDeal = tblRealDeal;
    }

    public String getBizConvert()
    {
        return bizConvert;
    }

    public void setBizConvert(String bizConvert)
    {
        this.bizConvert = bizConvert;
    }

    public String getBizBaseCur()
    {
        return bizBaseCur;
    }

    public void setBizBaseCur(String bizBaseCur)
    {
        this.bizBaseCur = bizBaseCur;
    }

    public String getBizVirtualCur()
    {
        return bizVirtualCur;
    }

    public void setBizVirtualCur(String bizVirtualCur)
    {
        this.bizVirtualCur = bizVirtualCur;
    }

    public BigDecimal getUpRateDefault()
    {
        return upRateDefault;
    }

    public void setUpRateDefault(BigDecimal upRateDefault)
    {
        this.upRateDefault = upRateDefault;
    }

    public BigDecimal getDownRateDefault()
    {
        return downRateDefault;
    }

    public void setDownRateDefault(BigDecimal downRateDefault)
    {
        this.downRateDefault = downRateDefault;
    }
}

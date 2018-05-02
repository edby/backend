package com.blocain.bitms.apps.spot.model;

import com.blocain.bitms.apps.account.beans.AuthTokenRequest;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 委托下单model
 * Created by admin on 2018/3/20.
 */
public class EntrustModel extends AuthTokenRequest
{
    /** 委托数量 */
    @NotNull(message = "委托数量不能为空")
    private BigDecimal entrustAmt;
    
    /** 委托价格 */
    @NotNull(message = "委托价格不能为空")
    private BigDecimal entrustPrice;
    
    /** 委托类型 */
    @NotNull(message = "委托类型不能为空")
    private String     entrustType;
    
    /** 交易对 */
    @NotNull(message = "交易对不能为空")
    private String     pair;
    
    public BigDecimal getEntrustAmt()
    {
        return entrustAmt;
    }
    
    public void setEntrustAmt(BigDecimal entrustAmt)
    {
        this.entrustAmt = entrustAmt;
    }
    
    public BigDecimal getEntrustPrice()
    {
        return entrustPrice;
    }
    
    public void setEntrustPrice(BigDecimal entrustPrice)
    {
        this.entrustPrice = entrustPrice;
    }
    
    public String getEntrustType()
    {
        return entrustType;
    }
    
    public void setEntrustType(String entrustType)
    {
        this.entrustType = entrustType;
    }
    
    public String getPair()
    {
        return pair;
    }
    
    public void setPair(String pair)
    {
        this.pair = pair;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("EntrustModel{");
        sb.append("entrustAmt=").append(entrustAmt);
        sb.append(", entrustPrice=").append(entrustPrice);
        sb.append(", entrustType='").append(entrustType).append('\'');
        sb.append(", pair='").append(pair).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

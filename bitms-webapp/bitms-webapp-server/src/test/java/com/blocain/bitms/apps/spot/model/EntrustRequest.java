package com.blocain.bitms.apps.spot.model;

import com.blocain.bitms.apps.sdk.BitmsObject;
import com.blocain.bitms.apps.sdk.internal.mapping.ApiField;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 *  下单
 *
 * @author auto create
 * @since 1.0, 2017-12-06 21:42:16
 */
public class EntrustRequest extends BitmsObject
{
    private static final long serialVersionUID = 5768574576598366886L;
    
    @ApiField("auth_token")
    private String            authToken;
    
    /** 委托数量 */
    @NotNull(message = "委托数量不能为空")
    @ApiField("entrustAmt")
    private BigDecimal        entrustAmt;
    
    /** 委托价格 */
    @NotNull(message = "委托价格不能为空")
    @ApiField("entrustPrice")
    private BigDecimal        entrustPrice;
    
    /** 委托类型 */
    @NotNull(message = "委托类型不能为空")
    @ApiField("entrustType")
    private String            entrustType;
    
    /** 交易对 */
    @ApiField("pair")
    @NotNull(message = "交易对不能为空")
    private String            pair;
    
    /** 交易对 */
    @ApiField("entrustId")
    @NotNull(message = "委托ID不能为空")
    private Long              entrustId;
    
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
    
    public Long getEntrustId()
    {
        return entrustId;
    }
    
    public void setEntrustId(Long entrustId)
    {
        this.entrustId = entrustId;
    }
    
    public String getAuthToken()
    {
        return authToken;
    }
    
    public void setAuthToken(String authToken)
    {
        this.authToken = authToken;
    }
}

package com.blocain.bitms.apps.spot.model;

import com.blocain.bitms.apps.account.beans.AuthTokenRequest;
import com.blocain.bitms.apps.sdk.BitmsObject;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 委托撤单model
 * Created by admin on 2018/3/20.
 */
public class EntrustCancelModel extends AuthTokenRequest
{
    /** 委托ID */
    @NotNull(message = "委托ID不能为空")
    private Long   entrustId;
    
    /** 交易对 */
    @NotNull(message = "交易对不能为空")
    private String pair;
    
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
}

package com.blocain.bitms.apps.fund.model;

import javax.validation.constraints.NotNull;

import com.blocain.bitms.apps.sdk.BitmsObject;
import com.blocain.bitms.apps.sdk.internal.mapping.ApiField;

/**
 * <p>Author：ChenGang</p>
 * <p>Description:WithdrawCancelRequest </p>
 * <p>Date: Create in 18:19 2018/3/22</p>
 * <p>Modify By: ChenGang</p>
 *
 * @version 1.0
 */
public class WithdrawCancelRequest extends BitmsObject
{
    /**申请ID**/
    @ApiField("id")
    private Long   id;
    
    /** 交易对 */
    @ApiField("symbol")
    @NotNull(message = "交易对不能为空")
    private String symbol;
    
    @ApiField("auth_token")
    private String authToken;
    
    public String getSymbol()
    {
        return symbol;
    }
    
    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }
    
    public String getAuthToken()
    {
        return authToken;
    }
    
    public void setAuthToken(String authToken)
    {
        this.authToken = authToken;
    }
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
}

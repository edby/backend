package com.blocain.bitms.apps.fund.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.validation.constraints.NotNull;

/**
 * <p>Author：ChenGang</p>
 * <p>Description:WithdrawCancelModel </p>
 * <p>Date: Create in 13:09 2018/3/22</p>
 * <p>Modify By: ChenGang</p>
 *
 * @version 1.0
 */
public class WithdrawCancelModel
{
    @NotNull
    @JSONField(name = "auth_token")
    private String authToken;
    
//    @NotNull(message = "币种不能为空")
    private String symbol;

    @NotNull(message = "证券类型的id不能为空")
    private Long stockInfoId;
    
    @NotNull
    private Long   id;
    
    public String getAuthToken()
    {
        return authToken;
    }
    
    public void setAuthToken(String authToken)
    {
        this.authToken = authToken;
    }
    
    public String getSymbol()
    {
        return symbol;
    }
    
    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getStockInfoId() {
        return stockInfoId;
    }

    public void setStockInfoId(Long stockInfoId) {
        this.stockInfoId = stockInfoId;
    }
}

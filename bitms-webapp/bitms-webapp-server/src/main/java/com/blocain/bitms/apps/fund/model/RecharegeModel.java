package com.blocain.bitms.apps.fund.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.validation.constraints.NotNull;

/**
 * <p>Author：ChenGang</p>
 * <p>Description: RecharegeModel</p>
 * <p>Date: Create in 10:43 2018/3/22</p>
 * <p>Modify By: ChenGang</p>
 *
 * @version 1.0
 */
public class RecharegeModel {

    @NotNull
    @JSONField(name = "auth_token")
    private String            authToken;

    @NotNull(message = "币种不能为空")
    private String symbol;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}

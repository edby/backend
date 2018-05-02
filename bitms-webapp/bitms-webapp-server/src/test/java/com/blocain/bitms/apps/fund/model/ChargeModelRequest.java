package com.blocain.bitms.apps.fund.model;

import com.blocain.bitms.apps.sdk.BitmsObject;
import com.blocain.bitms.apps.sdk.internal.mapping.ApiField;

import javax.validation.constraints.NotNull;

/**
 * <p>Author：ChenGang</p>
 * <p>Description:ChargeModelRequest </p>
 * <p>Date: Create in 18:19 2018/3/22</p>
 * <p>Modify By: ChenGang</p>
 *
 * @version 1.0
 */
public class ChargeModelRequest extends BitmsObject {

    /**
     * 交易对
     */
    @ApiField("pair")
    @NotNull(message = "交易对不能为空")
    private String pair;

    @ApiField("symbol")
    private String symbol;

    @ApiField("auth_token")
    private String authToken;

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

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

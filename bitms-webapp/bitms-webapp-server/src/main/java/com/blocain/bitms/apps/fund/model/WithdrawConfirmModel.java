package com.blocain.bitms.apps.fund.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.validation.constraints.NotNull;

/**
 * <p>Author：ChenGang</p>
 * <p>Description:WithdrawConfirmModel </p>
 * <p>Date: Create in 11:41 2018/3/22</p>
 * <p>Modify By: ChenGang</p>
 *
 * @version 1.0
 */
public class WithdrawConfirmModel {

    @NotNull
    @JSONField(name = "auth_token")
    private String            authToken;


    @NotNull(message = "币种不能为空")
    private String symbol;


    @NotNull(message = "确认码")
    private String            confirmCode;

    private String id;

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

    public String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(String confirmCode) {
        this.confirmCode = confirmCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

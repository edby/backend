package com.blocain.bitms.apps.fund.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.blocain.bitms.apps.sdk.BitmsObject;
import com.blocain.bitms.apps.sdk.internal.mapping.ApiField;

import javax.validation.constraints.NotNull;

/**
 * <p>Author：ChenGang</p>
 * <p>Description:WithdrawConfirmRequest </p>
 * <p>Date: Create in 16:02 2018/3/26</p>
 * <p>Modify By: ChenGang</p>
 *
 * @version 1.0
 */
public class WithdrawConfirmRequest extends BitmsObject{

    @NotNull
    @JSONField(name = "auth_token")
    @ApiField("auth_token")
    private String            authToken;

    @NotNull(message = "币种不能为空")
    @ApiField("symbol")
    private String symbol;

    /**申请ID**/
    @NotNull(message = "申请ID")
    @ApiField("id")
    private String   id;

    /**申请ID**/
    @NotNull(message = "confirmCode")
    @ApiField("confirmCode")
    private String   confirmCode;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(String confirmCode) {
        this.confirmCode = confirmCode;
    }
}

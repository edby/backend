package com.blocain.bitms.apps.account.beans;

import com.alibaba.fastjson.annotation.JSONField;
import com.blocain.bitms.apps.sdk.BitmsObject;
import com.blocain.bitms.trade.account.entity.Account;

import javax.validation.constraints.NotNull;

/**
 * <p>Author：yukai </p>
 * <p>Description:修改自动借贷接受bean </p>
 * <p>Date: Create in 16:18 2018/4/3</p>
 * <p>Modify By: yukai</p>
 *
 * @version 1.0
 */
public class ChangeAccountBorrow {

    @NotNull
    @JSONField(name = "autoDebit")
    private Integer autoDebit;

    @NotNull
    @JSONField(name = "auth_token")
    protected String authToken;

    public ChangeAccountBorrow() {
    }

    public Integer getAutoDebit() {
        return autoDebit;
    }

    public void setAutoDebit(Integer autoDebit) {
        this.autoDebit = autoDebit;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

package com.blocain.bitms.apps.sdk.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.blocain.bitms.apps.sdk.BitmsObject;
import com.blocain.bitms.apps.sdk.internal.mapping.ApiField;

import javax.validation.constraints.NotNull;

/**
 * <p>Author：yukai </p>
 * <p>Description:Description </p>
 * <p>Date: Create in 16:32 2018/3/22</p>
 * <p>Modify By: yukai</p>
 *
 * @version 1.0
 */
public class AccountReqModel extends BitmsObject {

    private static final long serialVersionUID = 590395275723403470L;

    @NotNull
    @ApiField("username")
    private String username;

    @NotNull
    @ApiField("password")
    private String password;

    @ApiField("email")
    private String email;

    @ApiField("code")
    private String code;

    @JSONField(name = "auth_token")
    @ApiField("auth_token")
    private String auth_token = "pMGXqqNSWe+iOMN7GiJ4rGK6P7oEu2KQ";

    @JSONField(name="access_token")
    @ApiField("access_token")
    private String access_token = "i0M0P3fWKqJMRZz4n8mXueC3EbTl6ENB94M9QOctdlM=";

    public AccountReqModel() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}

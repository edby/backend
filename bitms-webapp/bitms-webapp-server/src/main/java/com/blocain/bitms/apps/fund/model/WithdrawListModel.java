package com.blocain.bitms.apps.fund.model;

import com.blocain.bitms.apps.sdk.internal.mapping.ApiField;

import javax.validation.constraints.NotNull;

/**
 * <p>Author：ChenGang</p>
 * <p>Description:WithdrawListModel</p>
 * <p>Date: Create in 9:31 2018/3/26</p>
 * <p>Modify By: ChenGang</p>
 *
 * @version 1.0
 */
public class WithdrawListModel {

    @NotNull(message = "币种不能为空")
    private String symbol;

    @ApiField("page")
    private Integer           page;

    @ApiField("rows")
    private Integer           rows;


    @ApiField("auth_token")
    private String            authToken;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

package com.blocain.bitms.apps.fund.model;

import com.blocain.bitms.apps.account.beans.AuthTokenRequest;
import com.blocain.bitms.tools.consts.BitmsConst;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * <p>Author：yukai </p>
 * <p>Description:Description </p>
 * <p>Date: Create in 19:50 2018/3/29</p>
 * <p>Modify By: yukai</p>
 *
 * @version 1.0
 */
public class ChargeListModel extends AuthTokenRequest {

    // 每页的记录数
    @ApiModelProperty(value = "分页大小")
    private Integer rows = BitmsConst.DEFAULT_PAGE_SIZE;

    // 当前是第几页
    @ApiModelProperty(value = "当前页数")
    private Integer page = BitmsConst.DEFAULT_CURRENT_PAGE;

    @NotNull(message = "币种不可为空")
    private String symbol;

    public ChargeListModel() {
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}

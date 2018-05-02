package com.blocain.bitms.apps.spot.model;

import com.blocain.bitms.apps.account.beans.AuthTokenRequest;
import com.blocain.bitms.tools.consts.BitmsConst;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 交易对model
 * Created by admin on 2018/3/20.
 */
public class EntrustPageModel extends AuthTokenRequest {
    /**
     * 交易对
     */
//    @NotNull(message = "交易对不能为空")
    private String pair;

    @NotNull(message = "币种不能为空")
    private String symbol;

    // 每页的记录数
    protected Integer rows = BitmsConst.DEFAULT_PAGE_SIZE;

    // 当前是第几页
    protected Integer page = BitmsConst.DEFAULT_CURRENT_PAGE;

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
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

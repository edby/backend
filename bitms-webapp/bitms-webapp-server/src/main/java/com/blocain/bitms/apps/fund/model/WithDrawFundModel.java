package com.blocain.bitms.apps.fund.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.blocain.bitms.apps.account.beans.AuthTokenRequest;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>Author：yukai </p>
 * <p>Description:Description </p>
 * <p>Date: Create in 19:49 2018/3/22</p>
 * <p>Modify By: yukai</p>
 *
 * @version 1.0
 */
public class WithDrawFundModel extends AuthTokenRequest {

    @NotNull(message = "币种不能为空")
    private String symbol;

    @NotNull(message = "地址不能为空")
    private String collectAddr;

    /**资金密码**/
    @NotNull(message = "资金密码")
    private String fundPwd;

    /**GOOGLE验证码**/
    private String ga;

    /**提币数量**/
    @NotNull(message = "提币数量")
    private BigDecimal amount;

    @Override
    public String getAuthToken() {
        return authToken;
    }

    @Override
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCollectAddr() {
        return collectAddr;
    }

    public void setCollectAddr(String collectAddr) {
        this.collectAddr = collectAddr;
    }

    public String getFundPwd() {
        return fundPwd;
    }

    public void setFundPwd(String fundPwd) {
        this.fundPwd = fundPwd;
    }

    public String getGa() {
        return ga;
    }

    public void setGa(String ga) {
        this.ga = ga;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

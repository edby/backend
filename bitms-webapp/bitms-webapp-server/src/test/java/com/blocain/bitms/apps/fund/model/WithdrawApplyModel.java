package com.blocain.bitms.apps.fund.model;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONField;
import com.blocain.bitms.apps.sdk.BitmsObject;
import com.blocain.bitms.apps.sdk.internal.mapping.ApiField;

import java.math.BigDecimal;

/**
 * 互转可用查询model
 * Created by admin on 2018/3/21.
 */
public class WithdrawApplyModel extends BitmsObject
{
    @NotNull
    @JSONField(name = "auth_token")
    @ApiField("auth_token")
    private String            authToken;

    @NotNull(message = "币种不能为空")
    @ApiField("symbol")
    private String symbol;

    @NotNull(message = "收款地址")
    @ApiField("collectAddr")
    private String collectAddr;

    @NotNull(message = "资金密码")
    @ApiField("fundPwd")
    private String fundPwd;

    @ApiField("ga")
    private String ga;

    @NotNull(message = "短信验证码")
    @ApiField("amount")
    private BigDecimal amount;

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

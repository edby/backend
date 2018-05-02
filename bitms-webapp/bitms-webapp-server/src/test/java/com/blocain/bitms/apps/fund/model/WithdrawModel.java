package com.blocain.bitms.apps.fund.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONField;
import com.blocain.bitms.apps.sdk.BitmsObject;
import com.blocain.bitms.apps.sdk.internal.mapping.ApiField;

/**
 * 互转可用查询model
 * Created by admin on 2018/3/21.
 */
public class WithdrawModel extends BitmsObject
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

    @NotNull(message = "Google验证码")
    @ApiField("ga")
    private String ga;

    @NotNull(message = "短信验证码")
    @ApiField("sms")
    private String sms;

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

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }


}

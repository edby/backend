package com.blocain.bitms.apps.fund.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.validation.constraints.NotNull;

/**
 * <p>Author：ChenGang</p>
 * <p>Description: WithdrawAddrModel</p>
 * <p>Date: Create in 11:22 2018/3/22</p>
 * <p>Modify By: ChenGang</p>
 *
 * @version 1.0
 */
public class WithdrawAddrModel {

    @NotNull
    @JSONField(name = "auth_token")
    private String            authToken;

    @NotNull(message = "币种不能为空")
    private String symbol;

    @NotNull(message = "收款地址")
    private String collectAddr;

    @NotNull(message = "资金密码")
    private String fundPwd;

    @NotNull(message = "Google验证码")
    private String ga;

    @NotNull(message = "短信验证码")
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

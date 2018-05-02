package com.blocain.bitms.apps.fund.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.blocain.bitms.trade.risk.model.EnableModel;
import com.blocain.bitms.trade.stockinfo.entity.StockRate;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>Author：yukai </p>
 * <p>Description:Description </p>
 * <p>Date: Create in 19:51 2018/3/27</p>
 * <p>Modify By: yukai</p>
 *
 * @version 1.0
 */
public class WithDrawCommonModel {

    //6.13  提现手续费
    private Map<String, Object> stockRate;

    private Map<String, Object> quota;

    //6.15  提现可用余额
    private Map<String, Object> enableModel;

    //费率或手续费
    private BigDecimal rate;
    //值类型(1默认比例2绝对值)
    @JSONField(name = "rateType")
    private String rateValueType;
    //未认证用户提现当日额度上限
    private String unauthUserWithdrawDayUpper;
    //已认证用户提现当日额度上限
    private String authedUserWithdrawDayUpper;
    //当前帐号当日累计已提现
    private String usedAmount;
    //当前账户当日剩余提现额度
    private String canUseAmount;
    //认证状态
    private String authState;
    //姓氏（已认证）
    private String surname;
    //名字（已认证）
    private String realname;
    //区域代码（已认证）
    private String regionId;
    //护照编号（已认证）
    private String passportId;
    //可用余额
    private BigDecimal enableAmount;
    //冻结余额
    private BigDecimal frozenAmt;

    public WithDrawCommonModel() {
    }

    public WithDrawCommonModel(Map<String, Object> stockRate, Map<String, Object> map, Map<String, Object> enableModel) {
        this.stockRate = stockRate;
        this.quota = map;
        this.enableModel = enableModel;
    }

    public WithDrawCommonModel(BigDecimal rate, String rateValueType, BigDecimal enableAmount, BigDecimal frozenAmt) {
        this.rate = rate;
        this.rateValueType = rateValueType;
        this.enableAmount = enableAmount;
        this.frozenAmt = frozenAmt;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getRateValueType() {
        return rateValueType;
    }

    public void setRateValueType(String rateValueType) {
        this.rateValueType = rateValueType;
    }

    public String getUnauthUserWithdrawDayUpper() {
        return unauthUserWithdrawDayUpper;
    }

    public void setUnauthUserWithdrawDayUpper(String unauthUserWithdrawDayUpper) {
        this.unauthUserWithdrawDayUpper = unauthUserWithdrawDayUpper;
    }

    public String getAuthedUserWithdrawDayUpper() {
        return authedUserWithdrawDayUpper;
    }

    public void setAuthedUserWithdrawDayUpper(String authedUserWithdrawDayUpper) {
        this.authedUserWithdrawDayUpper = authedUserWithdrawDayUpper;
    }

    public String getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(String usedAmount) {
        this.usedAmount = usedAmount;
    }

    public String getCanUseAmount() {
        return canUseAmount;
    }

    public void setCanUseAmount(String canUseAmount) {
        this.canUseAmount = canUseAmount;
    }

    public String getAuthState() {
        return authState;
    }

    public void setAuthState(String authState) {
        this.authState = authState;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public BigDecimal getEnableAmount() {
        return enableAmount;
    }

    public void setEnableAmount(BigDecimal enableAmount) {
        this.enableAmount = enableAmount;
    }

    public BigDecimal getFrozenAmt() {
        return frozenAmt;
    }

    public void setFrozenAmt(BigDecimal frozenAmt) {
        this.frozenAmt = frozenAmt;
    }


    public Map<String, Object> getQuota() {
        return quota;
    }

    public void setQuota(Map<String, Object> quota) {
        this.quota = quota;
    }

    public Map<String, Object> getStockRate() {
        return stockRate;
    }

    public void setStockRate(Map<String, Object> stockRate) {
        this.stockRate = stockRate;
    }

    public Map<String, Object> getEnableModel() {
        return enableModel;
    }

    public void setEnableModel(Map<String, Object> enableModel) {
        this.enableModel = enableModel;
    }
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.entity;

import com.blocain.bitms.orm.core.GenericEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 保证金监控表 实体对象
 * <p>File：MonitorMargin.java</p>
 * <p>Title: MonitorMargin</p>
 * <p>Description:MonitorMargin</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "保证金监控表")
public class MonitorMargin extends GenericEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 监控类型
     */
    @NotNull(message = "监控类型不可为空")
    @ApiModelProperty(value = "监控类型", required = true)
    private String monitorType;

    /**
     * 监控子类型描述
     */
    @NotNull(message = "监控子类型描述不可为空")
    @ApiModelProperty(value = "监控子类型描述", required = true)
    private String monitorSubType;

    /**
     * 平台账户ID
     */
    @NotNull(message = "平台账户ID不可为空")
    @ApiModelProperty(value = "平台账户ID", required = true)
    private Long accountId;

    /**
     * 业务品种证券ID
     */
    @NotNull(message = "业务品种证券ID不可为空")
    @ApiModelProperty(value = "业务品种证券ID", required = true)
    private Long stockinfoId;

    /**
     * 计价证券ID
     */
    @NotNull(message = "计价证券ID不可为空")
    @ApiModelProperty(value = "计价证券ID", required = true)
    private Long capitalStockinfoId;

    /**
     * 标的证券ID
     */
    @NotNull(message = "标的证券ID不可为空")
    @ApiModelProperty(value = "标的证券ID", required = true)
    private Long targetStockinfoId;

    /**
     * 合约账户计价证券余额
     */
    @NotNull(message = "合约账户计价证券余额不可为空")
    @ApiModelProperty(value = "合约账户计价证券余额", required = true)
    private java.math.BigDecimal capitalBal;

    /**
     * 合约账户标的证券余额
     */
    @NotNull(message = "合约账户标的证券余额不可为空")
    @ApiModelProperty(value = "合约账户标的证券余额", required = true)
    private java.math.BigDecimal targetBal;

    /**
     * 计价证券负债余额
     */
    @NotNull(message = "计价证券负债余额不可为空")
    @ApiModelProperty(value = "计价证券负债余额", required = true)
    private java.math.BigDecimal capitalDebtBal;

    /**
     * 标的证券负债余额
     */
    @NotNull(message = "标的证券负债余额不可为空")
    @ApiModelProperty(value = "标的证券负债余额", required = true)
    private java.math.BigDecimal targetDebtBal;

    /**
     * 标的证券折算计价证券系统内部价格
     */
    @NotNull(message = "标的证券折算计价证券系统内部价格不可为空")
    @ApiModelProperty(value = "标的证券折算计价证券系统内部价格", required = true)
    private java.math.BigDecimal platPrice;

    /**
     * 杠杆值
     */
    @NotNull(message = "杠杆值不可为空")
    @ApiModelProperty(value = "杠杆值", required = true)
    private java.math.BigDecimal lever;

    /**
     * 保证金预警比例(乘以100)
     */
    @NotNull(message = "保证金预警比例(乘以100)不可为空")
    @ApiModelProperty(value = "保证金预警比例(乘以100)", required = true)
    private java.math.BigDecimal warnRatio;

    /**
     * 保证金实时比例(乘以100)
     */
    @NotNull(message = "保证金实时比例(乘以100)不可为空")
    @ApiModelProperty(value = "保证金实时比例(乘以100)", required = true)
    private java.math.BigDecimal marginratio;

    /**
     * 监控结果描述
     */
    @ApiModelProperty(value = "监控结果描述")
    private String monitordesc;

    /**
     * 监控结果(-1 对账不平、1 对账已平)
     */
    @ApiModelProperty(value = "监控结果(-1 对账不平、1 对账已平)")
    private Boolean chkResult;

    /**
     * 监控时间
     */
    @NotNull(message = "监控时间不可为空")
    @ApiModelProperty(value = "监控时间", required = true)
    private java.sql.Timestamp chkDate;

    /**
     * 账户属性(0.空头；1.多头)
     */
    @NotNull(message = "账户属性(0.空头；1.多头)不可为空")
    @ApiModelProperty(value = "账户属性(0.空头；1.多头)", required = true)
    private Integer acctattr;

    /**
     * 强平价
     */
    @NotNull(message = "强平价不可为空")
    @ApiModelProperty(value = "强平价", required = true)
    private java.math.BigDecimal explosionPrice;

     /**
     * 风险率(乘以100)
     */
    @NotNull(message = "风险率(乘以100)不可为空")
    @ApiModelProperty(value = "风险率(乘以100)", required = true)
    private java.math.BigDecimal riskRate;

    /**
     * 临界爆仓比例(乘以100)
     */
    @NotNull(message = "临界爆仓比例(乘以100)不可为空")
    @ApiModelProperty(value = "临界爆仓比例(乘以100)", required = true)
    private java.math.BigDecimal criticalCPPercent;

    /**
     * 账户名
     */
    private String accountName;

    //多头账户状态
    private Integer bullStatus;
    //多头异常条数（ 0<=riskRate <=105为正常）
    private Integer bullAbNormalCount;
    //空头账户状态
    private Integer bearStatus;
    //空头异常条数（ 95<=riskRate 为正常）
    private Integer bearAbNormalCount;


    public Integer getBullStatus() {
        return bullStatus;
    }

    public void setBullStatus(Integer bullStatus) {
        this.bullStatus = bullStatus;
    }

    public Integer getBullAbNormalCount() {
        return bullAbNormalCount;
    }

    public void setBullAbNormalCount(Integer bullAbNormalCount) {
        this.bullAbNormalCount = bullAbNormalCount;
    }

    public Integer getBearStatus() {
        return bearStatus;
    }

    public void setBearStatus(Integer bearStatus) {
        this.bearStatus = bearStatus;
    }

    public Integer getBearAbNormalCount() {
        return bearAbNormalCount;
    }

    public void setBearAbNormalCount(Integer bearAbNormalCount) {
        this.bearAbNormalCount = bearAbNormalCount;
    }

    public String getMonitorType() {
        return this.monitorType;
    }

    public void setMonitorType(String monitorType) {
        this.monitorType = monitorType;
    }

    public String getMonitorSubType() {
        return this.monitorSubType;
    }

    public void setMonitorSubType(String monitorSubType) {
        this.monitorSubType = monitorSubType;
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getStockinfoId() {
        return stockinfoId;
    }

    public void setStockinfoId(Long stockinfoId) {
        this.stockinfoId = stockinfoId;
    }

    public Long getTargetStockinfoId() {
        return this.targetStockinfoId;
    }

    public void setTargetStockinfoId(Long targetStockinfoId) {
        this.targetStockinfoId = targetStockinfoId;
    }

    public java.math.BigDecimal getTargetBal() {
        return this.targetBal;
    }

    public void setTargetBal(java.math.BigDecimal targetBal) {
        this.targetBal = targetBal;
    }

    public java.math.BigDecimal getTargetDebtBal() {
        return this.targetDebtBal;
    }

    public void setTargetDebtBal(java.math.BigDecimal targetDebtBal) {
        this.targetDebtBal = targetDebtBal;
    }

    public java.math.BigDecimal getPlatPrice() {
        return this.platPrice;
    }

    public void setPlatPrice(java.math.BigDecimal platPrice) {
        this.platPrice = platPrice;
    }

    public java.math.BigDecimal getLever() {
        return this.lever;
    }

    public void setLever(java.math.BigDecimal lever) {
        this.lever = lever;
    }

    public java.math.BigDecimal getWarnRatio() {
        return this.warnRatio;
    }

    public void setWarnRatio(java.math.BigDecimal warnRatio) {
        this.warnRatio = warnRatio;
    }

    public java.math.BigDecimal getMarginratio() {
        return this.marginratio;
    }

    public Long getCapitalStockinfoId() {
        return capitalStockinfoId;
    }

    public void setCapitalStockinfoId(Long capitalStockinfoId) {
        this.capitalStockinfoId = capitalStockinfoId;
    }

    public BigDecimal getCapitalBal() {
        return capitalBal;
    }

    public void setCapitalBal(BigDecimal capitalBal) {
        this.capitalBal = capitalBal;
    }

    public BigDecimal getCapitalDebtBal() {
        return capitalDebtBal;
    }

    public void setCapitalDebtBal(BigDecimal capitalDebtBal) {
        this.capitalDebtBal = capitalDebtBal;
    }

    public void setMarginratio(java.math.BigDecimal marginratio) {
        this.marginratio = marginratio;
    }

    public String getMonitordesc() {
        return this.monitordesc;
    }

    public void setMonitordesc(String monitordesc) {
        this.monitordesc = monitordesc;
    }

    public Boolean getChkResult() {
        return this.chkResult;
    }

    public void setChkResult(Boolean chkResult) {
        this.chkResult = chkResult;
    }

    public java.sql.Timestamp getChkDate() {
        return this.chkDate;
    }

    public void setChkDate(java.sql.Timestamp chkDate) {
        this.chkDate = chkDate;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Integer getAcctattr() {
        return acctattr;
    }

    public void setAcctattr(Integer acctattr) {
        this.acctattr = acctattr;
    }

    public BigDecimal getExplosionPrice() {
        return explosionPrice;
    }

    public void setExplosionPrice(BigDecimal explosionPrice) {
        this.explosionPrice = explosionPrice;
    }

    public BigDecimal getRiskRate() {
        return riskRate;
    }

    public void setRiskRate(BigDecimal riskRate) {
        this.riskRate = riskRate;
    }

    public BigDecimal getCriticalCPPercent() {
        return criticalCPPercent;
    }

    public void setCriticalCPPercent(BigDecimal criticalCPPercent) {
        this.criticalCPPercent = criticalCPPercent;
    }
}

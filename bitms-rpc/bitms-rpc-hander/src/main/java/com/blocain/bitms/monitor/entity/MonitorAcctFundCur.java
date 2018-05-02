/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.entity;

import com.blocain.bitms.orm.core.GenericEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 账户资金流水监控表 实体对象
 * <p>File：MonitorAcctFundCur.java</p>
 * <p>Title: MonitorAcctFundCur</p>
 * <p>Description:MonitorAcctFundCur</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "账户资金流水监控表")
public class MonitorAcctFundCur extends GenericEntity {

    private static final long serialVersionUID = 1L;

    /**
     * monitorType
     */
    @NotNull(message = "monitorType不可为空")
    @ApiModelProperty(value = "monitorType", required = true)
    private String monitorType;
    /**
     * monitorSubType
     */
    @NotNull(message = "monitorSubType不可为空")
    @ApiModelProperty(value = "monitorSubType", required = true)
    private String monitorSubType;
    /**
     * 账户 id
     */
    @NotNull(message = "账户 id不可为空")
    @ApiModelProperty(value = "账户 id", required = true)
    private Long accountId;
    //账户资产类型(1.钱包资产，2.杠杆资产，3.理财资产)
    @NotNull(message = "账户资产类型不可为空")
    private Integer acctAssetType;
    /**
     * bizcategoryID
     */
    @NotNull(message = "bizcategoryID不可为空")
    @ApiModelProperty(value = "bizcategoryID", required = true)
    private Long bizCategoryId;
    /**
     * 证券ID 对应对应stockinfo TRADESTOCKINFOID 或 CAPITALSTOCKINFOID
     */
    @NotNull(message = "证券ID 对应对应stockinfo TRADESTOCKINFOID 或 CAPITALSTOCKINFOID 不可为空")
    @ApiModelProperty(value = "证券ID 对应对应stockinfo TRADESTOCKINFOID 或 CAPITALSTOCKINFOID ", required = true)
    private Long stockinfoId;
    /**
     * 资产余额
     */
    @NotNull(message = "资产余额不可为空")
    @ApiModelProperty(value = "资产余额", required = true)
    private java.math.BigDecimal assetBal;
    /**
     * 冻结资产余额
     */
    @NotNull(message = "冻结资产余额不可为空")
    @ApiModelProperty(value = "冻结资产余额", required = true)
    private java.math.BigDecimal assetfrozenbal;
    /**
     * 负债余额
     */
    @NotNull(message = "负债余额不可为空")
    @ApiModelProperty(value = "负债余额", required = true)
    private java.math.BigDecimal debetBal;
    /**
     * 资金流水资产发生总额
     */
    @NotNull(message = "资金流水资产发生总额不可为空")
    @ApiModelProperty(value = "资金流水资产发生总额", required = true)
    private java.math.BigDecimal curAssetBal;
    /**
     * 资金流水资产冻结余额
     */
    @NotNull(message = "资金流水资产冻结余额不可为空")
    @ApiModelProperty(value = "资金流水资产冻结余额", required = true)
    private java.math.BigDecimal curfrozenbal;
    /**
     * 资金流水负债发生总额
     */
    @NotNull(message = "资金流水负债发生总额不可为空")
    @ApiModelProperty(value = "资金流水负债发生总额", required = true)
    private java.math.BigDecimal curDebetBal;
    /**
     * 负债余额和负债流水余额的差额
     */
    @NotNull(message = "负债余额和负债流水余额的差额不可为空")
    @ApiModelProperty(value = "负债余额和负债流水余额的差额", required = true)
    private java.math.BigDecimal differenceDebetBal;
    /**
     * 账户余额和资金流水余额的差额
     */
    @NotNull(message = "账户余额和资金流水余额的差额不可为空")
    @ApiModelProperty(value = "账户余额和资金流水余额的差额", required = true)
    private java.math.BigDecimal differenceAssetBal;
    /**
     * 账户冻结余额和资金流水冻结余额的差额
     */
    @NotNull(message = "账户冻结余额和资金流水冻结余额的差额不可为空")
    @ApiModelProperty(value = "账户冻结余额和资金流水冻结余额的差额", required = true)
    private java.math.BigDecimal differencefrozenbal;
    /**
     * 监控结果描述
     */
    @ApiModelProperty(value = "监控结果描述")
    private String monitordesc;
    /**
     * 监控结果(-1 对账不平、1 对账已平)
     */
    @ApiModelProperty(value = "监控结果(-1 对账不平、1 对账已平)")
    private Integer chkResult;
    /**
     * 业务流水时间
     */
    @NotNull(message = "业务流水时间不可为空")
    @ApiModelProperty(value = "业务流水时间", required = true)
    private java.util.Date bizDate;
    /**
     * 监控时间
     */
    @NotNull(message = "监控时间不可为空")
    @ApiModelProperty(value = "监控时间", required = true)
    private java.util.Date chkDate;

    //监控总体状态（正常、异常）
    private Integer chkStatus;
    //异常记录条数
    private Integer abNormalCount;

    private String timeStart;

    private String timeEnd;

    private String accountName;

    private Long relatedStockinfoId;

    private String stockName;

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public Integer getChkStatus() {
        return chkStatus;
    }

    public void setChkStatus(Integer chkStatus) {
        this.chkStatus = chkStatus;
    }

    public Integer getAbNormalCount() {
        return abNormalCount;
    }

    public void setAbNormalCount(Integer abNormalCount) {
        this.abNormalCount = abNormalCount;
    }

    public Integer getAcctAssetType() {
        return acctAssetType;
    }

    public void setAcctAssetType(Integer acctAssetType) {
        this.acctAssetType = acctAssetType;
    }

    public Long getRelatedStockinfoId() {
        return relatedStockinfoId;
    }

    public void setRelatedStockinfoId(Long relatedStockinfoId) {
        this.relatedStockinfoId = relatedStockinfoId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
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

    public Long getBizCategoryId() {
        return this.bizCategoryId;
    }

    public void setBizCategoryId(Long bizCategoryId) {
        this.bizCategoryId = bizCategoryId;
    }

    public Long getStockinfoId() {
        return this.stockinfoId;
    }

    public void setStockinfoId(Long stockinfoId) {
        this.stockinfoId = stockinfoId;
    }

    public java.math.BigDecimal getAssetBal() {
        return this.assetBal;
    }

    public void setAssetBal(java.math.BigDecimal assetBal) {
        this.assetBal = assetBal;
    }

    public java.math.BigDecimal getAssetfrozenbal() {
        return this.assetfrozenbal;
    }

    public void setAssetfrozenbal(java.math.BigDecimal assetfrozenbal) {
        this.assetfrozenbal = assetfrozenbal;
    }

    public java.math.BigDecimal getDebetBal() {
        return this.debetBal;
    }

    public void setDebetBal(java.math.BigDecimal debetBal) {
        this.debetBal = debetBal;
    }

    public java.math.BigDecimal getCurAssetBal() {
        return this.curAssetBal;
    }

    public void setCurAssetBal(java.math.BigDecimal curAssetBal) {
        this.curAssetBal = curAssetBal;
    }

    public java.math.BigDecimal getCurfrozenbal() {
        return this.curfrozenbal;
    }

    public void setCurfrozenbal(java.math.BigDecimal curfrozenbal) {
        this.curfrozenbal = curfrozenbal;
    }

    public java.math.BigDecimal getCurDebetBal() {
        return this.curDebetBal;
    }

    public void setCurDebetBal(java.math.BigDecimal curDebetBal) {
        this.curDebetBal = curDebetBal;
    }

    public java.math.BigDecimal getDifferenceDebetBal() {
        return this.differenceDebetBal;
    }

    public void setDifferenceDebetBal(java.math.BigDecimal differenceDebetBal) {
        this.differenceDebetBal = differenceDebetBal;
    }

    public java.math.BigDecimal getDifferenceAssetBal() {
        return this.differenceAssetBal;
    }

    public void setDifferenceAssetBal(java.math.BigDecimal differenceAssetBal) {
        this.differenceAssetBal = differenceAssetBal;
    }

    public java.math.BigDecimal getDifferencefrozenbal() {
        return this.differencefrozenbal;
    }

    public void setDifferencefrozenbal(java.math.BigDecimal differencefrozenbal) {
        this.differencefrozenbal = differencefrozenbal;
    }

    public String getMonitordesc() {
        return this.monitordesc;
    }

    public void setMonitordesc(String monitordesc) {
        this.monitordesc = monitordesc;
    }

    public Integer getChkResult() {
        return chkResult;
    }

    public void setChkResult(Integer chkResult) {
        this.chkResult = chkResult;
    }

    public java.util.Date getBizDate() {
        return this.bizDate;
    }

    public void setBizDate(java.util.Date bizDate) {
        this.bizDate = bizDate;
    }

    public java.util.Date getChkDate() {
        return this.chkDate;
    }

    public void setChkDate(java.util.Date chkDate) {
        this.chkDate = chkDate;
    }

    @Override
    public String toString() {
        return "MonitorAcctFundCur{" +
                "monitorType='" + monitorType + '\'' +
                ", monitorSubType='" + monitorSubType + '\'' +
                ", accountId=" + accountId +
                ", bizCategoryId=" + bizCategoryId +
                ", stockinfoId=" + stockinfoId +
                ", assetBal=" + assetBal +
                ", assetfrozenbal=" + assetfrozenbal +
                ", debetBal=" + debetBal +
                ", curAssetBal=" + curAssetBal +
                ", curfrozenbal=" + curfrozenbal +
                ", curDebetBal=" + curDebetBal +
                ", differenceDebetBal=" + differenceDebetBal +
                ", differenceAssetBal=" + differenceAssetBal +
                ", differencefrozenbal=" + differencefrozenbal +
                ", monitordesc='" + monitordesc + '\'' +
                ", chkResult=" + chkResult +
                ", bizDate=" + bizDate +
                ", chkDate=" + chkDate +
                ", timeStart='" + timeStart + '\'' +
                ", timeEnd='" + timeEnd + '\'' +
                ", accountName='" + accountName + '\'' +
                ", relatedStockinfoId=" + relatedStockinfoId +
                '}';
    }
}


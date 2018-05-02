/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.entity;

import javax.validation.constraints.NotNull;

import com.blocain.bitms.orm.core.GenericEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * MonitorERC20Bal 实体对象
 * <p>File：MonitorERC20Bal.java</p>
 * <p>Title: MonitorERC20Bal</p>
 * <p>Description:MonitorERC20Bal</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "MonitorERC20Bal")
public class MonitorERC20Bal extends GenericEntity {
	
	private static final long serialVersionUID = 1L;

	/**stockinfoid*/
	@NotNull(message = "stockinfoid不可为空")
	@ApiModelProperty(value = "stockinfoid", required = true)
	private Long stockinfoId;
	/**monitorType*/
	@NotNull(message = "monitorType不可为空")
	@ApiModelProperty(value = "monitorType", required = true)
	private String monitorType;
	/**monitorSubType*/
	@NotNull(message = "monitorSubType不可为空")
	@ApiModelProperty(value = "monitorSubType", required = true)
	private String monitorSubType;
	/**externalHBal*/
	@NotNull(message = "externalHBal不可为空")
	@ApiModelProperty(value = "externalHBal", required = true)
	private java.math.BigDecimal externalHBal;
	/**externalCBal*/
	@NotNull(message = "externalCBal不可为空")
	@ApiModelProperty(value = "externalCBal", required = true)
	private java.math.BigDecimal externalCBal;
	/**externalEBal*/
	@NotNull(message = "externalEBal不可为空")
	@ApiModelProperty(value = "externalEBal", required = true)
	private java.math.BigDecimal externalEBal;
	/**internalbal*/
	@NotNull(message = "internalbal不可为空")
	@ApiModelProperty(value = "internalbal", required = true)
	private java.math.BigDecimal internalbal;
	/**differenceBal*/
	@NotNull(message = "differenceBal不可为空")
	@ApiModelProperty(value = "differenceBal", required = true)
	private java.math.BigDecimal differenceBal;
	/**externalWarnValue*/
	@NotNull(message = "externalWarnValue不可为空")
	@ApiModelProperty(value = "externalWarnValue", required = true)
	private java.math.BigDecimal externalWarnValue;
	/**internalWarnValue*/
	@NotNull(message = "internalWarnValue不可为空")
	@ApiModelProperty(value = "internalWarnValue", required = true)
	private java.math.BigDecimal internalWarnValue;
	/**externalForbidValue*/
	@NotNull(message = "externalForbidValue不可为空")
	@ApiModelProperty(value = "externalForbidValue", required = true)
	private java.math.BigDecimal externalForbidValue;
	/**internalForbidValue*/
	@NotNull(message = "internalForbidValue不可为空")
	@ApiModelProperty(value = "internalForbidValue", required = true)
	private java.math.BigDecimal internalForbidValue;
	/**monitordesc*/
	@ApiModelProperty(value = "monitordesc")
	private String monitordesc;
	/**chkResult*/
	@ApiModelProperty(value = "chkResult")
	private Integer chkResult;
	/**chkDate*/
	@NotNull(message = "chkDate不可为空")
	@ApiModelProperty(value = "chkDate", required = true)
	private java.util.Date chkDate;

	private String stockName;

	private String timeStart;

	private String timeEnd;

	//监控总体状态（正常、异常）
	private Integer chkStatus;
	//异常记录条数
	private Integer abNormalCount;

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

	public Long getStockinfoId() {
		return stockinfoId;
	}

	public void setStockinfoId(Long stockinfoId) {
		this.stockinfoId = stockinfoId;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public String getMonitorType()
	{
		return this.monitorType;
	}

	public void setMonitorType(String monitorType)
	{
		this.monitorType = monitorType;
	}

	public String getMonitorSubType()
	{
		return this.monitorSubType;
	}

	public void setMonitorSubType(String monitorSubType)
	{
		this.monitorSubType = monitorSubType;
	}

	public java.math.BigDecimal getExternalHBal()
	{
		return this.externalHBal;
	}

	public void setExternalHBal(java.math.BigDecimal externalHBal)
	{
		this.externalHBal = externalHBal;
	}

	public java.math.BigDecimal getExternalCBal()
	{
		return this.externalCBal;
	}

	public void setExternalCBal(java.math.BigDecimal externalCBal)
	{
		this.externalCBal = externalCBal;
	}

	public java.math.BigDecimal getExternalEBal()
	{
		return this.externalEBal;
	}

	public void setExternalEBal(java.math.BigDecimal externalEBal)
	{
		this.externalEBal = externalEBal;
	}

	public java.math.BigDecimal getInternalbal()
	{
		return this.internalbal;
	}

	public void setInternalbal(java.math.BigDecimal internalbal)
	{
		this.internalbal = internalbal;
	}

	public java.math.BigDecimal getDifferenceBal()
	{
		return this.differenceBal;
	}

	public void setDifferenceBal(java.math.BigDecimal differenceBal)
	{
		this.differenceBal = differenceBal;
	}

	public java.math.BigDecimal getExternalWarnValue()
	{
		return this.externalWarnValue;
	}

	public void setExternalWarnValue(java.math.BigDecimal externalWarnValue)
	{
		this.externalWarnValue = externalWarnValue;
	}

	public java.math.BigDecimal getInternalWarnValue()
	{
		return this.internalWarnValue;
	}

	public void setInternalWarnValue(java.math.BigDecimal internalWarnValue)
	{
		this.internalWarnValue = internalWarnValue;
	}

	public java.math.BigDecimal getExternalForbidValue()
	{
		return this.externalForbidValue;
	}

	public void setExternalForbidValue(java.math.BigDecimal externalForbidValue)
	{
		this.externalForbidValue = externalForbidValue;
	}

	public java.math.BigDecimal getInternalForbidValue()
	{
		return this.internalForbidValue;
	}

	public void setInternalForbidValue(java.math.BigDecimal internalForbidValue)
	{
		this.internalForbidValue = internalForbidValue;
	}

	public String getMonitordesc()
	{
		return this.monitordesc;
	}

	public void setMonitordesc(String monitordesc)
	{
		this.monitordesc = monitordesc;
	}

	public Integer getChkResult() {
		return chkResult;
	}

	public void setChkResult(Integer chkResult) {
		this.chkResult = chkResult;
	}

	public java.util.Date getChkDate()
	{
		return this.chkDate;
	}
	
	public void setChkDate(java.util.Date chkDate)
	{
		this.chkDate = chkDate;
	}
	
}


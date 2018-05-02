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
 * MonitorBlockNum 实体对象
 * <p>File：MonitorBlockNum.java</p>
 * <p>Title: MonitorBlockNum</p>
 * <p>Description:MonitorBlockNum</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "MonitorBlockNum")
public class MonitorBlockNum extends GenericEntity {
	
	private static final long serialVersionUID = 1L;

	/**区块来源（主键）*/
	@NotNull(message = "区块来源不可为空")
	@ApiModelProperty(value = "区块来源", required = true)
	private String blockResource;
	
	/**监控类型 (MONITORBLOCKNUM)*/
	@NotNull(message = "监控类型 (MONITORBLOCKNUM)不可为空")
	@ApiModelProperty(value = "监控类型 (MONITORBLOCKNUM)", required = true)
	private String monitorType;
	/**监控子类型描述 (ETH)*/
	@NotNull(message = "监控子类型描述 (ETH)不可为空")
	@ApiModelProperty(value = "监控子类型描述 (ETH)", required = true)
	private String monitorSubType;
	/**内部区块高度*/
	@ApiModelProperty(value = "内部区块高度")
	private Long inBlockNum;
	/**外部区块高度*/
	@ApiModelProperty(value = "外部区块高度")
	private Long outBlockNum;
	/**内外部区块高度差*/
	@ApiModelProperty(value = "内外部区块高度差")
	private Long differenceNum;
	/**外部预警阈值*/
	@NotNull(message = "外部预警阈值不可为空")
	@ApiModelProperty(value = "外部预警阈值", required = true)
	private java.math.BigDecimal externalWarnValue;
	/**内部预警阈值*/
	@NotNull(message = "内部预警阈值不可为空")
	@ApiModelProperty(value = "内部预警阈值", required = true)
	private java.math.BigDecimal internalWarnValue;
	/**外部禁止阈值*/
	@NotNull(message = "外部禁止阈值不可为空")
	@ApiModelProperty(value = "外部禁止阈值", required = true)
	private java.math.BigDecimal externalForbidValue;
	/**内部禁止阈值*/
	@NotNull(message = "内部禁止阈值不可为空")
	@ApiModelProperty(value = "内部禁止阈值", required = true)
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

	public String getBlockResource() {
		return blockResource;
	}

	public void setBlockResource(String blockResource) {
		this.blockResource = blockResource;
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

	public Long getInBlockNum()
	{
		return this.inBlockNum;
	}

	public void setInBlockNum(Long inBlockNum)
	{
		this.inBlockNum = inBlockNum;
	}

	public Long getOutBlockNum()
	{
		return this.outBlockNum;
	}

	public void setOutBlockNum(Long outBlockNum)
	{
		this.outBlockNum = outBlockNum;
	}

	public Long getDifferenceNum()
	{
		return this.differenceNum;
	}

	public void setDifferenceNum(Long differenceNum)
	{
		this.differenceNum = differenceNum;
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


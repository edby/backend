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
 * MonitorPlatBal 实体对象
 * <p>File：MonitorPlatBal.java</p>
 * <p>Title: MonitorPlatBal</p>
 * <p>Description:MonitorPlatBal</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "MonitorPlatBal")
public class MonitorPlatBal extends GenericEntity {
	
	private static final long                 serialVersionUID = 1L;

	/**证券id*/
	@NotNull(message = "证券id不可为空")
	@ApiModelProperty(value = "证券id", required = true)
	private              String               stockinfoId;
	
	/**monitorType*/
	@NotNull(message = "monitorType不可为空")
	@ApiModelProperty(value = "monitorType", required = true)
	private String monitorType;
	/**monitorSubType*/
	@NotNull(message = "monitorSubType不可为空")
	@ApiModelProperty(value = "monitorSubType", required = true)
	private String monitorSubType;
	/**externalRBal*/
	@NotNull(message = "externalRBal不可为空")
	@ApiModelProperty(value = "externalRBal", required = true)
	private java.math.BigDecimal externalRBal;
	/**externalWBal*/
	@NotNull(message = "externalWBal不可为空")
	@ApiModelProperty(value = "externalWBal", required = true)
	private java.math.BigDecimal externalWBal;
	/**externalEBal*/
	@NotNull(message = "externalEBal不可为空")
	@ApiModelProperty(value = "externalEBal", required = true)
	private java.math.BigDecimal externalEBal;
	/**internalBal*/
	@NotNull(message = "internalBal不可为空")
	@ApiModelProperty(value = "internalBal", required = true)
	private java.math.BigDecimal internalBal;
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
	private              java.math.BigDecimal internalForbidValue;
	/**monitorDesc*/
	@ApiModelProperty(value = "monitorDesc")
	private              String               monitorDesc;
	/**chkResult*/
	@ApiModelProperty(value = "chkResult")
	private              Integer              chkResult;
	/**chkDate*/
	@NotNull(message = "chkDate不可为空")
	@ApiModelProperty(value = "chkDate", required = true)
	private              java.util.Date       chkDate;

	//监控总体状态（正常、异常）
	private              Integer              chkStatus;

	private              BigDecimal           hotPayBal;

	private              BigDecimal           coldPayBal;

	private              BigDecimal           minParamVal;

	private              BigDecimal           maxParamVal;
	//异常记录条数
	private              Integer              abNormalCount;

	private              String               stockName;

	private              String               timeStart;

	private              String               timeEnd;

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

	public String getStockinfoId() {
		return stockinfoId;
	}

	public void setStockinfoId(String stockinfoId) {
		this.stockinfoId = stockinfoId;
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

	public java.math.BigDecimal getExternalRBal()
	{
		return this.externalRBal;
	}

	public void setExternalRBal(java.math.BigDecimal externalRBal)
	{
		this.externalRBal = externalRBal;
	}

	public java.math.BigDecimal getExternalWBal()
	{
		return this.externalWBal;
	}

	public void setExternalWBal(java.math.BigDecimal externalWBal)
	{
		this.externalWBal = externalWBal;
	}

	public java.math.BigDecimal getExternalEBal()
	{
		return this.externalEBal;
	}

	public void setExternalEBal(java.math.BigDecimal externalEBal)
	{
		this.externalEBal = externalEBal;
	}

	public java.math.BigDecimal getInternalBal()
	{
		return this.internalBal;
	}

	public void setInternalBal(java.math.BigDecimal internalBal)
	{
		this.internalBal = internalBal;
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

	public String getMonitorDesc()
	{
		return this.monitorDesc;
	}

	public void setMonitorDesc(String monitorDesc)
	{
		this.monitorDesc = monitorDesc;
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

	public BigDecimal getHotPayBal()
	{
		return hotPayBal;
	}

	public void setHotPayBal(BigDecimal hotPayBal)
	{
		this.hotPayBal = hotPayBal;
	}

	public BigDecimal getColdPayBal()
	{
		return coldPayBal;
	}

	public void setColdPayBal(BigDecimal coldPayBal)
	{
		this.coldPayBal = coldPayBal;
	}

	public BigDecimal getMinParamVal()
	{
		return minParamVal;
	}

	public void setMinParamVal(BigDecimal minParamVal)
	{
		this.minParamVal = minParamVal;
	}

	public BigDecimal getMaxParamVal()
	{
		return maxParamVal;
	}

	public void setMaxParamVal(BigDecimal maxParamVal)
	{
		this.maxParamVal = maxParamVal;
	}
}


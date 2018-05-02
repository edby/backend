/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.entity;

import javax.validation.constraints.NotNull;

import com.blocain.bitms.orm.core.GenericEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 监控阈值参数表 实体对象
 * <p>File：MonitorLimitParam.java</p>
 * <p>Title: MonitorLimitParam</p>
 * <p>Description:MonitorLimitParam</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "监控阈值参数表")
public class MonitorLimitParam extends GenericEntity {
	
	private static final long serialVersionUID = 1L;
	
	/**监控指标ID*/
	@NotNull(message = "监控指标ID不可为空")
	@ApiModelProperty(value = "监控指标ID", required = true)
	private Long relatedId;
	/**证券ID*/
	@NotNull(message = "证券ID不可为空")
	@ApiModelProperty(value = "证券ID", required = true)
	private Long stockinfoId;
	/**最大阈值*/
	@ApiModelProperty(value = "最大阈值")
	private java.math.BigDecimal maxValue;
	/**最小阈值*/
	@ApiModelProperty(value = "最小阈值")
	private java.math.BigDecimal minValue;

	/**比较方向*/
	@ApiModelProperty(value = "比较方向")
	private Integer compDirect;

	/**创建人ID*/
	@NotNull(message = "创建人ID不可为空")
	@ApiModelProperty(value = "创建人ID", required = true)
	private String createBy;
	/**参数备注*/
	@ApiModelProperty(value = "参数备注")
	private String paramDesc;
	/**创建时间*/
	@ApiModelProperty(value = "创建时间")
	private java.util.Date createDate;
	//指标名称
	private String idxName;
	//证券名称
	private String stockName;

	public String getIdxName() {
		return idxName;
	}

	public void setIdxName(String idxName) {
		this.idxName = idxName;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public Long getRelatedId()
	{
		return this.relatedId;
	}

	public void setRelatedId(Long relatedId)
	{
		this.relatedId = relatedId;
	}

	public Long getStockinfoId()
	{
		return this.stockinfoId;
	}

	public void setStockinfoId(Long stockinfoId)
	{
		this.stockinfoId = stockinfoId;
	}

	public BigDecimal getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(BigDecimal maxValue) {
		this.maxValue = maxValue;
	}

	public BigDecimal getMinValue() {
		return minValue;
	}

	public void setMinValue(BigDecimal minValue) {
		this.minValue = minValue;
	}

	public Integer getCompDirect() {
		return compDirect;
	}

	public void setCompDirect(Integer compDirect) {
		this.compDirect = compDirect;
	}

	public String getCreateBy()
	{
		return this.createBy;
	}

	public void setCreateBy(String createBy)
	{
		this.createBy = createBy;
	}

	public String getParamDesc()
	{
		return this.paramDesc;
	}

	public void setParamDesc(String paramDesc)
	{
		this.paramDesc = paramDesc;
	}
	
	public java.util.Date getCreateDate()
	{
		return this.createDate;
	}
	
	public void setCreateDate(java.util.Date createDate)
	{
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "MonitorLimitParam{" +
				"relatedId=" + relatedId +
				", stockinfoId=" + stockinfoId +
				", maxValue=" + maxValue +
				", minValue=" + minValue +
				", compDirect=" + compDirect +
				", createBy='" + createBy + '\'' +
				", paramDesc='" + paramDesc + '\'' +
				", createDate=" + createDate +
				", idxName='" + idxName + '\'' +
				", stockName='" + stockName + '\'' +
				", id=" + id +
				'}';
	}
}


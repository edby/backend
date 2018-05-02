/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.entity;

import com.blocain.bitms.orm.core.GenericEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 监控参数表 实体对象
 * <p>File：MonitorParam.java</p>
 * <p>Title: MonitorParam</p>
 * <p>Description:MonitorParam</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "监控参数表")
public class MonitorParam extends GenericEntity {
	
	private static final long serialVersionUID = 1L;
	
	/**paramType*/
	@NotNull(message = "paramType不可为空")
	@ApiModelProperty(value = "paramType", required = true)
	private String paramType;
	/**paramCode*/
	@NotNull(message = "paramCode不可为空")
	@ApiModelProperty(value = "paramCode", required = true)
	private String paramCode;
	/**paramName*/
	@NotNull(message = "paramName不可为空")
	@ApiModelProperty(value = "paramName", required = true)
	private String paramName;
	/**paramValue*/
	@NotNull(message = "paramValue不可为空")
	@ApiModelProperty(value = "paramValue", required = true)
	private String paramValue;
	/**createBy*/
	@NotNull(message = "createBy不可为空")
	@ApiModelProperty(value = "createBy", required = true)
	private String createBy;
	/**paramDesc*/
	@ApiModelProperty(value = "paramDesc")
	private String paramDesc;
	/**createDate*/
	@ApiModelProperty(value = "createDate")
	private Timestamp createDate;

	public String getParamType()
	{
		return this.paramType;
	}

	public void setParamType(String paramType)
	{
		this.paramType = paramType;
	}

	public String getParamCode()
	{
		return this.paramCode;
	}

	public void setParamCode(String paramCode)
	{
		this.paramCode = paramCode;
	}

	public String getParamName()
	{
		return this.paramName;
	}

	public void setParamName(String paramName)
	{
		this.paramName = paramName;
	}

	public String getParamValue()
	{
		return this.paramValue;
	}

	public void setParamValue(String paramValue)
	{
		this.paramValue = paramValue;
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

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	@Override public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MonitorParam that = (MonitorParam) o;
		return Objects.equals(paramType, that.paramType) && Objects.equals(paramCode, that.paramCode) && Objects.equals(paramName, that.paramName) && Objects
				.equals(paramValue, that.paramValue) && Objects.equals(createBy, that.createBy) && Objects.equals(paramDesc, that.paramDesc) && Objects
				.equals(createDate, that.createDate);
	}

	@Override public int hashCode()
	{
		return Objects.hash(paramType, paramCode, paramName, paramValue, createBy, paramDesc, createDate);
	}
}


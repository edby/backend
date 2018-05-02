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
 * 归档运行日志 实体对象
 * <p>File：ArchiveRunLogs.java</p>
 * <p>Title: ArchiveRunLogs</p>
 * <p>Description:ArchiveRunLogs</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "归档运行日志")
public class ArchiveRunLogs extends GenericEntity {
	
	private static final long serialVersionUID = 1L;
	
	/**归档存储过程*/
	@ApiModelProperty(value = "归档存储过程")
	private String procCode;
	/**归档日志描述*/
	@ApiModelProperty(value = "归档日志描述")
	private String logDesc;
	/**归档时间*/
	@NotNull(message = "归档时间不可为空")
	@ApiModelProperty(value = "归档时间", required = true)
	private java.util.Date recTime;

	private String timeStart;

	private String timeEnd;

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

	public String getProcCode()
	{
		return this.procCode;
	}

	public void setProcCode(String procCode)
	{
		this.procCode = procCode;
	}

	public String getLogDesc()
	{
		return this.logDesc;
	}

	public void setLogDesc(String logDesc)
	{
		this.logDesc = logDesc;
	}
	
	public java.util.Date getRecTime()
	{
		return this.recTime;
	}
	
	public void setRecTime(java.util.Date recTime)
	{
		this.recTime = recTime;
	}
	
}


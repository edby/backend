/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.entity;

import com.blocain.bitms.orm.core.GenericEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 监控指标表 实体对象
 * <p>File：MonitorIndex.java</p>
 * <p>Title: MonitorIndex</p>
 * <p>Description:MonitorIndex</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "监控指标表")
public class MonitorIndex extends GenericEntity {
	
	private static final long serialVersionUID = 1L;
	
	/**监控指标名称*/
	@ApiModelProperty(value = "监控指标名称")
	private String idxName;
	/**监控指标触发后的操作*/
	@ApiModelProperty(value = "监控指标触发后的操作")
	private String actionType;
	/**监控指标触发后操作的参数值*/
	@ApiModelProperty(value = "监控指标触发后操作的参数值")
	private String actionValue;
	/**创建人ID*/
	@ApiModelProperty(value = "创建人ID")
	private String createBy;
	/**参数备注*/
	@ApiModelProperty(value = "参数备注")
	private String paramDesc;
	/**创建时间*/
	@ApiModelProperty(value = "创建时间")
	private java.util.Date createDate;

	//指标级别
	private Integer idxLevel;

	//如果处理方式中包含消息提醒，需要设置该列表
	private List<MonitorParam> actionValueList;
	//键：证券id,值：阈值参数
	private HashMap<Long,MonitorLimitParam> limitParamMap;

	public Integer getIdxLevel() {
		return idxLevel;
	}

	public void setIdxLevel(Integer idxLevel) {
		this.idxLevel = idxLevel;
	}

	public HashMap<Long, MonitorLimitParam> getLimitParamMap() {
		return limitParamMap;
	}

	public void setLimitParamMap(HashMap<Long, MonitorLimitParam> limitParamMap) {
		this.limitParamMap = limitParamMap;
	}

	public List<MonitorParam> getActionValueList() {
		return actionValueList;
	}

	public void setActionValueList(List<MonitorParam> actionValueList) {
		this.actionValueList = actionValueList;
	}

	public String getIdxName()
	{
		return this.idxName;
	}

	public void setIdxName(String idxName)
	{
		this.idxName = idxName;
	}

	public String getActionType()
	{
		return this.actionType;
	}

	public void setActionType(String actionType)
	{
		this.actionType = actionType;
	}

	public String getActionValue()
	{
		return this.actionValue;
	}

	public void setActionValue(String actionValue)
	{
		this.actionValue = actionValue;
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
		return "MonitorIndex{" +
				"idxName='" + idxName + '\'' +
				", actionType='" + actionType + '\'' +
				", actionValue='" + actionValue + '\'' +
				", createBy='" + createBy + '\'' +
				", paramDesc='" + paramDesc + '\'' +
				", createDate=" + createDate +
				", actionValueList=" + actionValueList +
				", limitParamMap=" + limitParamMap +
				'}';
	}
}


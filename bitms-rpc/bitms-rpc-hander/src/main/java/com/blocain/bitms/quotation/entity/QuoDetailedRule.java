/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.quotation.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.blocain.bitms.orm.core.GenericEntity ;
import javax.validation.constraints.NotNull;
/**
 * 行情数据明细表 实体对象
 * <p>File：QuoDetailedRule.java</p>
 * <p>Title: QuoDetailedRule</p>
 * <p>Description:QuoDetailedRule</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "行情数据明细表")
public class QuoDetailedRule extends GenericEntity {
	
	private static final long serialVersionUID = 1L;
	
	/**主键ID*/
	@NotNull(message = "主键ID不可为空")
	@ApiModelProperty(value = "主键ID", required = true)
	private Long id;
	/**行情服务配置ID*/
	@ApiModelProperty(value = "行情服务配置ID")
	private Long relatedId;
	/**成交流水显示条数*/
	@NotNull(message = "成交流水显示条数不可为空")
	@ApiModelProperty(value = "成交流水显示条数", required = true)
	private Long realdealNum;
	/**委托盘口显示条数*/
	@ApiModelProperty(value = "委托盘口显示条数")
	private String entrustNum;
	/**委托深度级别*/
	@ApiModelProperty(value = "委托深度级别")
	private String entrustLevel;
	/**盘口买入排序*/
	@ApiModelProperty(value = "盘口买入排序")
	private String entrustBuySort;
	/**盘口卖出排序*/
	@ApiModelProperty(value = "盘口卖出排序")
	private String entrustSellSort;
	
	public Long getId()
	{
		return this.id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public Long getRelatedId()
	{
		return this.relatedId;
	}
	
	public void setRelatedId(Long relatedId)
	{
		this.relatedId = relatedId;
	}
	
	public Long getRealdealNum()
	{
		return this.realdealNum;
	}
	
	public void setRealdealNum(Long realdealNum)
	{
		this.realdealNum = realdealNum;
	}
	
	public String getEntrustNum()
	{
		return this.entrustNum;
	}
	
	public void setEntrustNum(String entrustNum)
	{
		this.entrustNum = entrustNum;
	}
	
	public String getEntrustLevel()
	{
		return this.entrustLevel;
	}
	
	public void setEntrustLevel(String entrustLevel)
	{
		this.entrustLevel = entrustLevel;
	}
	
	public String getEntrustBuySort()
	{
		return this.entrustBuySort;
	}
	
	public void setEntrustBuySort(String entrustBuySort)
	{
		this.entrustBuySort = entrustBuySort;
	}
	
	public String getEntrustSellSort()
	{
		return this.entrustSellSort;
	}
	
	public void setEntrustSellSort(String entrustSellSort)
	{
		this.entrustSellSort = entrustSellSort;
	}
	
}


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
 * 行情服务配置表 实体对象
 * <p>File：QuoServiceConfig.java</p>
 * <p>Title: QuoServiceConfig</p>
 * <p>Description:QuoServiceConfig</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "行情服务配置表")
public class QuoServiceConfig extends GenericEntity {
	
	private static final long serialVersionUID = 1L;
	
	/**主键ID*/
	@NotNull(message = "主键ID不可为空")
	@ApiModelProperty(value = "主键ID", required = true)
	private Long id;
	/**分组ID*/
	@ApiModelProperty(value = "分组ID")
	private Integer groupId;
	/**币对ID*/
	@NotNull(message = "币对ID不可为空")
	@ApiModelProperty(value = "币对ID", required = true)
	private Long stockinfoId;
	/**服务名称*/
	@ApiModelProperty(value = "服务名称")
	private String serName;
	/**服务前缀，用于区分不同服务推出的主题数据*/
	@ApiModelProperty(value = "服务前缀，用于区分不同服务推出的主题数据")
	private String serPrefix;
	/**websocket连接url*/
	@ApiModelProperty(value = "websocket连接url")
	private String websocketUrl;
	/**价格小数位数*/
	@ApiModelProperty(value = "价格小数位数")
	private Integer pricedigit;
	/**数量小数位数*/
	@ApiModelProperty(value = "数量小数位数")
	private Integer amtdigit;
	/**金额小数位数*/
	@ApiModelProperty(value = "金额小数位数")
	private Integer baldigit;
	/**启用标志*/
	@NotNull(message = "启用标志不可为空")
	@ApiModelProperty(value = "启用标志", required = true)
	private Boolean active;
	
	public Long getId()
	{
		return this.id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public Integer getGroupId()
	{
		return this.groupId;
	}
	
	public void setGroupId(Integer groupId)
	{
		this.groupId = groupId;
	}
	
	public Long getStockinfoId()
	{
		return this.stockinfoId;
	}
	
	public void setStockinfoId(Long stockinfoId)
	{
		this.stockinfoId = stockinfoId;
	}
	
	public String getSerName()
	{
		return this.serName;
	}
	
	public void setSerName(String serName)
	{
		this.serName = serName;
	}
	
	public String getSerPrefix()
	{
		return this.serPrefix;
	}
	
	public void setSerPrefix(String serPrefix)
	{
		this.serPrefix = serPrefix;
	}
	
	public String getWebsocketUrl()
	{
		return this.websocketUrl;
	}
	
	public void setWebsocketUrl(String websocketUrl)
	{
		this.websocketUrl = websocketUrl;
	}
	
	public Integer getPricedigit()
	{
		return this.pricedigit;
	}
	
	public void setPricedigit(Integer pricedigit)
	{
		this.pricedigit = pricedigit;
	}
	
	public Integer getAmtdigit()
	{
		return this.amtdigit;
	}
	
	public void setAmtdigit(Integer amtdigit)
	{
		this.amtdigit = amtdigit;
	}
	
	public Integer getBaldigit()
	{
		return this.baldigit;
	}
	
	public void setBaldigit(Integer baldigit)
	{
		this.baldigit = baldigit;
	}
	
	public Boolean getActive()
	{
		return this.active;
	}
	
	public void setActive(Boolean active)
	{
		this.active = active;
	}
	
}


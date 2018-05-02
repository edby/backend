/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.system.entity;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONField;
import com.blocain.bitms.orm.core.GenericEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 资源菜单信息表 实体对象
 * <p>File：Resources.java</p>
 * <p>Title: Resources</p>
 * <p>Description:Resources</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "资源菜单信息")
public class Resources extends GenericEntity
{
    private static final long serialVersionUID = 1L;
    
    /** 上级编号*/
    @ApiModelProperty(value = "上级ID")
    private Long              parentId;
    
    /**资源编码*/
    @NotNull(message = "资源编码不可为空")
    @ApiModelProperty(value = "资源编码")
    private String            resCode;
    
    /**资源名称*/
    @NotNull(message = "资源名称不可为空")
    @ApiModelProperty(value = "资源名称")
    private String            resName;
    
    /**资源描述*/
    @ApiModelProperty(value = "资源描述")
    private String            resDest;
    
    /**类型（菜单、权限）*/
    @ApiModelProperty(value = "类型（菜单、权限）")
    private Boolean           type;
    
    /**图标*/
    @JSONField(name = "iconCls")
    @ApiModelProperty(value = "图标")
    private String            icon;
    
    /**排序号*/
    @ApiModelProperty(value = "排序号")
    private Integer           sortNum;
    
    /**资源地址*/
    @NotNull(message = "资源地址不可为空")
    @ApiModelProperty(value = "资源地址")
    private String            resUrl;
    
    /**创建人*/
    @ApiModelProperty(value = "创建人ID")
    private Long              createBy;
    
    /**创建人*/
    @ApiModelProperty(value = "创建人")
    private String            createByName;
    
    /**创建时间*/
    @ApiModelProperty(value = "创建时间")
    private Long              createDate;
    
    /**修改人*/
    @ApiModelProperty(value = "修改人")
    private Long              updateBy;
    
    /**修改时间*/
    @ApiModelProperty(value = "修改时间")
    private Long              updateDate;
    
    public Long getParentId()
    {
        return this.parentId;
    }
    
    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }
    
    public String getResCode()
    {
        return this.resCode;
    }
    
    public void setResCode(String resCode)
    {
        this.resCode = resCode;
    }
    
    public String getResName()
    {
        return this.resName;
    }
    
    public void setResName(String resName)
    {
        this.resName = resName;
    }
    
    public String getResDest()
    {
        return this.resDest;
    }
    
    public void setResDest(String resDest)
    {
        this.resDest = resDest;
    }
    
    public Boolean getType()
    {
        return this.type;
    }
    
    public void setType(Boolean type)
    {
        this.type = type;
    }
    
    public String getIcon()
    {
        return this.icon;
    }
    
    public void setIcon(String icon)
    {
        this.icon = icon;
    }
    
    public Integer getSortNum()
    {
        return this.sortNum;
    }
    
    public void setSortNum(Integer sortNum)
    {
        this.sortNum = sortNum;
    }
    
    public String getResUrl()
    {
        return this.resUrl;
    }
    
    public void setResUrl(String resUrl)
    {
        this.resUrl = resUrl;
    }
    
    public Long getCreateBy()
    {
        return this.createBy;
    }
    
    public String getCreateByName()
    {
        return createByName;
    }
    
    public void setCreateByName(String createByName)
    {
        this.createByName = createByName;
    }
    
    public void setCreateBy(Long createBy)
    {
        this.createBy = createBy;
    }
    
    public Long getCreateDate()
    {
        return this.createDate;
    }
    
    public void setCreateDate(Long createDate)
    {
        this.createDate = createDate;
    }
    
    public Long getUpdateBy()
    {
        return this.updateBy;
    }
    
    public void setUpdateBy(Long updateBy)
    {
        this.updateBy = updateBy;
    }
    
    public Long getUpdateDate()
    {
        return this.updateDate;
    }
    
    public void setUpdateDate(Long updateDate)
    {
        this.updateDate = updateDate;
    }
}

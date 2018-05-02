/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.common.entity;

import com.blocain.bitms.orm.core.GenericEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

/**
 * 数据字典 实体对象
 * <p>File：Dictionary.java</p>
 * <p>Title: Dictionary</p>
 * <p>Description:Dictionary</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "数据字典")
public class Dictionary extends GenericEntity
{
    private static final long  serialVersionUID = 1L;
    
    /**上级编码*/
    @ApiModelProperty(value = "上级编码")
    private Long               parentId;
    
    /**编码*/
    @NotNull(message = "编码不可为空")
    @ApiModelProperty(value = "编码")
    private String             code;
    
    /**名称*/
    @NotNull(message = "名称不可为空")
    @ApiModelProperty(value = "名称")
    private String             name;
    
    /**语言*/
    @ApiModelProperty(value = "语言")
    private String             lang;
    
    /**描述*/
    @ApiModelProperty(value = "描述")
    private String             dest;
    
    /**排序号*/
    @ApiModelProperty(value = "排序号")
    private Long               sortNum;
    
    /**启用标识*/
    @NotNull(message = "启用标识不可为空")
    @ApiModelProperty(value = "启用标识")
    private Boolean            active;
    
    /**创建人*/
    @ApiModelProperty(value = "创建人编号")
    private Long               createBy;
    
    /**创建人*/
    @ApiModelProperty(value = "创建人名称")
    private String             createByName;
    
    /**创建时间*/
    @ApiModelProperty(value = "创建时间")
    private java.sql.Timestamp createDate;
    
    @ApiModelProperty(value = "子项")
    private List<Dictionary>   children;
    
    public Long getParentId()
    {
        return parentId;
    }
    
    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }
    
    public String getCode()
    {
        return code;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getLang()
    {
        return lang;
    }
    
    public void setLang(String lang)
    {
        this.lang = lang;
    }
    
    public String getDest()
    {
        return dest;
    }
    
    public void setDest(String dest)
    {
        this.dest = dest;
    }
    
    public Long getSortNum()
    {
        return sortNum;
    }
    
    public void setSortNum(Long sortNum)
    {
        this.sortNum = sortNum;
    }
    
    public Boolean getActive()
    {
        return active;
    }
    
    public void setActive(Boolean active)
    {
        this.active = active;
    }
    
    public Long getCreateBy()
    {
        return createBy;
    }
    
    public void setCreateBy(Long createBy)
    {
        this.createBy = createBy;
    }
    
    public String getCreateByName()
    {
        return createByName;
    }
    
    public void setCreateByName(String createByName)
    {
        this.createByName = createByName;
    }
    
    public Timestamp getCreateDate()
    {
        return createDate;
    }
    
    public void setCreateDate(Timestamp createDate)
    {
        this.createDate = createDate;
    }
    
    public List<Dictionary> getChildren()
    {
        return children;
    }
    
    public void setChildren(List<Dictionary> children)
    {
        this.children = children;
    }
}

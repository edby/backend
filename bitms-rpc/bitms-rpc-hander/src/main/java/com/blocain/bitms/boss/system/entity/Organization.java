/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.system.entity;

import javax.validation.constraints.NotNull;

import com.blocain.bitms.orm.core.GenericEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 机构信息表 实体对象
 * <p>File：Organization.java</p>
 * <p>Title: Organization</p>
 * <p>Description:Organization</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "机构信息")
public class Organization extends GenericEntity
{
    private static final long serialVersionUID = 1L;
    
    /**上级编号*/
    @ApiModelProperty(value = "上级ID")
    private Long              parentId;
    
    /**机构编码*/
    @NotNull(message = "机构编码不可为空")
    @ApiModelProperty(value = "机构编码")
    private String            orgCode;
    
    /**机构名称*/
    @NotNull(message = "机构名称不可为空")
    @ApiModelProperty(value = "机构名称")
    private String            orgName;
    
    /**机构描述*/
    @ApiModelProperty(value = "机构描述")
    private String            orgDest;
    
    /**排序号*/
    @ApiModelProperty(value = "排序号")
    private Long              sortNum;
    
    /**创建人*/
    @ApiModelProperty(value = "创建人ID")
    private Long              createBy;
    
    /**创建人*/
    @ApiModelProperty(value = "创建人姓名")
    private String            createByName;
    
    /**创建时间*/
    @ApiModelProperty(value = "创建时间")
    private Long              createDate;
    
    /**修改人*/
    @ApiModelProperty(value = "修改人ID")
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
    
    public String getOrgCode()
    {
        return this.orgCode;
    }
    
    public void setOrgCode(String orgCode)
    {
        this.orgCode = orgCode;
    }
    
    public String getOrgName()
    {
        return this.orgName;
    }
    
    public void setOrgName(String orgName)
    {
        this.orgName = orgName;
    }
    
    public String getOrgDest()
    {
        return this.orgDest;
    }
    
    public void setOrgDest(String orgDest)
    {
        this.orgDest = orgDest;
    }
    
    public Long getSortNum()
    {
        return sortNum;
    }
    
    public void setSortNum(Long sortNum)
    {
        this.sortNum = sortNum;
    }
    
    public Long getCreateBy()
    {
        return this.createBy;
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
    
    public String getCreateByName()
    {
        return createByName;
    }
    
    public void setCreateByName(String createByName)
    {
        this.createByName = createByName;
    }
}

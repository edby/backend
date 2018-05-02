/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.common.entity;

import com.blocain.bitms.orm.core.GenericEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 区域代码 实体对象
 * <p>File：Region.java</p>
 * <p>Title: Region</p>
 * <p>Description:Region</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "区域代码")
public class Region extends GenericEntity
{
    private static final long serialVersionUID = 1L;
    
    /**国际简码*/
    @ApiModelProperty(value = "国际简码")
    private String            sCode;
    
    /**国际代码*/
    @ApiModelProperty(value = "国际代码")
    private String            lCode;
    
    /**英文名称*/
    @ApiModelProperty(value = "英文名称")
    private String            enName;
    
    /**中文名称*/
    @ApiModelProperty(value = "中文名称")
    private String            cnName;
    
    /**区域(亚洲、非洲)*/
    @ApiModelProperty(value = "区域(亚洲、非洲)")
    private String            area;
    
    /**排序号*/
    @ApiModelProperty(value = "排序号")
    private Long              sortNum;
    
    public String getSCode()
    {
        return this.sCode;
    }
    
    public void setSCode(String sCode)
    {
        this.sCode = sCode;
    }
    
    public String getLCode()
    {
        return this.lCode;
    }
    
    public void setLCode(String lCode)
    {
        this.lCode = lCode;
    }
    
    public String getEnName()
    {
        return this.enName;
    }
    
    public void setEnName(String enName)
    {
        this.enName = enName;
    }
    
    public String getCnName()
    {
        return this.cnName;
    }
    
    public void setCnName(String cnName)
    {
        this.cnName = cnName;
    }
    
    public String getArea()
    {
        return this.area;
    }
    
    public void setArea(String area)
    {
        this.area = area;
    }
    
    public Long getSortNum()
    {
        return this.sortNum;
    }
    
    public void setSortNum(Long sortNum)
    {
        this.sortNum = sortNum;
    }
}

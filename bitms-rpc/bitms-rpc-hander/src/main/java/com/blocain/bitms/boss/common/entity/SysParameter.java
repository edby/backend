/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.blocain.bitms.orm.core.GenericEntity;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * 系统参数表 实体对象
 * <p>File：SysParameter.java</p>
 * <p>Title: SysParameter</p>
 * <p>Description:SysParameter</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "系统参数表")
public class SysParameter extends GenericEntity
{
    private static final long  serialVersionUID = 1L;
    
    /**系统名称*/
    @NotNull(message = "系统名称不可为空")
    @ApiModelProperty(value = "系统名称", required = true)
    private String             systemName;
    
    /**参数名称*/
    @NotNull(message = "参数名称不可为空")
    @ApiModelProperty(value = "参数名称", required = true)
    private String             parameterName;
    
    /**参数描述*/
    @NotNull(message = "参数描述不可为空")
    @ApiModelProperty(value = "参数描述", required = true)
    private String             describe;
    
    /**参数大类*/
    @NotNull(message = "参数大类不可为空")
    @ApiModelProperty(value = "参数大类", required = true)
    private String             division;
    
    /**参数类型(文本、单选、多选)*/
    @NotNull(message = "参数类型(文本、单选、多选)不可为空")
    @ApiModelProperty(value = "参数类型(文本、单选、多选)", required = true)
    private String             type;
    
    /**参数值值域*/
    @ApiModelProperty(value = "参数值值域")
    private String             valueBound;
    
    /** 参数值*/
    @NotNull(message = " 参数值不可为空")
    @ApiModelProperty(value = " 参数值", required = true)
    private String             value;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String             remark;
    
    /**创建人*/
    @ApiModelProperty(value = "创建人", required = true)
    private Long               createBy;
    
    /**创建时间*/
    @ApiModelProperty(value = "创建时间", required = true)
    private java.sql.Timestamp createDate;
    
    /**修改人*/
    @ApiModelProperty(value = "修改人")
    private Long               updateBy;
    
    /**修改时间*/
    @ApiModelProperty(value = "修改时间")
    private java.sql.Timestamp updateDate;
    
    /**创建人*/
    private String             createByName;
    
    /**修改人*/
    private String             updateByName;
    
    public String getSystemName()
    {
        return this.systemName;
    }
    
    public void setSystemName(String systemName)
    {
        this.systemName = systemName;
    }
    
    public String getParameterName()
    {
        return this.parameterName;
    }
    
    public void setParameterName(String parameterName)
    {
        this.parameterName = parameterName;
    }
    
    public String getDescribe()
    {
        return this.describe;
    }
    
    public void setDescribe(String describe)
    {
        this.describe = describe;
    }
    
    public String getDivision()
    {
        return this.division;
    }
    
    public void setDivision(String division)
    {
        this.division = division;
    }
    
    public String getType()
    {
        return this.type;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public String getValueBound()
    {
        return this.valueBound;
    }
    
    public void setValueBound(String valueBound)
    {
        this.valueBound = valueBound;
    }
    
    public String getValue()
    {
        return this.value;
    }
    
    public void setValue(String value)
    {
        this.value = value;
    }
    
    public String getRemark()
    {
        return this.remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public Long getCreateBy()
    {
        return createBy;
    }
    
    public void setCreateBy(Long createBy)
    {
        this.createBy = createBy;
    }
    
    public Long getUpdateBy()
    {
        return updateBy;
    }
    
    public void setUpdateBy(Long updateBy)
    {
        this.updateBy = updateBy;
    }
    
    public String getCreateByName()
    {
        return createByName;
    }
    
    public void setCreateByName(String createByName)
    {
        this.createByName = createByName;
    }
    
    public String getUpdateByName()
    {
        return updateByName;
    }
    
    public void setUpdateByName(String updateByName)
    {
        this.updateByName = updateByName;
    }
    
    public Timestamp getCreateDate()
    {
        return createDate;
    }
    
    public void setCreateDate(Timestamp createDate)
    {
        this.createDate = createDate;
    }
    
    public Timestamp getUpdateDate()
    {
        return updateDate;
    }
    
    public void setUpdateDate(Timestamp updateDate)
    {
        this.updateDate = updateDate;
    }
}

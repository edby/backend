/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.entity;

import javax.validation.constraints.NotNull;
import com.blocain.bitms.orm.core.GenericEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 消息公告表 实体对象
 * <p>File：Notice.java</p>
 * <p>Title: Notice</p>
 * <p>Description:Notice</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "消息公告表")
public class Notice extends GenericEntity
{
    private static final long serialVersionUID = 1L;
    
    /**语言类型(zh_CN中文简体、zh_HK中文繁体、en_US英文)*/
    @NotNull(message = "语言类型不可为空")
    @ApiModelProperty(value = "语言类型(zh_CN中文简体、zh_HK中文繁体、en_US英文)")
    private String            langType;
    
    /**标题*/
    @NotNull(message = "标题不可为空")
    @ApiModelProperty(value = "标题")
    private String            title;
    
    /**内容*/
    @NotNull(message = "内容不可为空")
    @ApiModelProperty(value = "内容")
    private String            content;
    
    /**状态(0:未发布、1:已发布)*/
    @NotNull(message = "状态不可为空")
    @ApiModelProperty(value = "状态(0:未发布、1:已发布)")
    private Boolean           status;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String            remark;
    
    /**创建人*/
    @ApiModelProperty(value = "创建ID")
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
    
    /**发布时间*/
    @NotNull(message = "发布时间戳不可为空")
    @ApiModelProperty(value = "发布时间")
    private Long              publicDate;
    
    /**读取状态（0:未读，1:已读）*/
    @ApiModelProperty(hidden = true)
    private Boolean           readFlag         = false;
    
    public String getLangType()
    {
        return langType;
    }
    
    public void setLangType(String langType)
    {
        this.langType = langType;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public Boolean getStatus()
    {
        return status;
    }
    
    public void setStatus(Boolean status)
    {
        this.status = status;
    }
    
    public String getRemark()
    {
        return remark;
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
    
    public String getCreateByName()
    {
        return createByName;
    }
    
    public void setCreateByName(String createByName)
    {
        this.createByName = createByName;
    }
    
    public Long getCreateDate()
    {
        return createDate;
    }
    
    public void setCreateDate(Long createDate)
    {
        this.createDate = createDate;
    }
    
    public Long getUpdateBy()
    {
        return updateBy;
    }
    
    public void setUpdateBy(Long updateBy)
    {
        this.updateBy = updateBy;
    }
    
    public Long getUpdateDate()
    {
        return updateDate;
    }
    
    public void setUpdateDate(Long updateDate)
    {
        this.updateDate = updateDate;
    }
    
    public Long getPublicDate()
    {
        return publicDate;
    }
    
    public void setPublicDate(Long publicDate)
    {
        this.publicDate = publicDate;
    }
    
    public Boolean getReadFlag()
    {
        return readFlag;
    }
    
    public void setReadFlag(Boolean readFlag)
    {
        this.readFlag = readFlag;
    }
}

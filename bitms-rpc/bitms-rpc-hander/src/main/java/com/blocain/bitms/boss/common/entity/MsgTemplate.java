/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.common.entity;

import javax.validation.constraints.NotNull;

import com.blocain.bitms.orm.core.GenericEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 消息模版 实体对象
 * <p>File：MsgTemplate.java</p>
 * <p>Title: MsgTemplate</p>
 * <p>Description:MsgTemplate</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "消息模版")
public class MsgTemplate extends GenericEntity
{
    private static final long serialVersionUID = 1L;
    
    /**模版KEY*/
    @NotNull(message = "模版KEY不可为空")
    @ApiModelProperty(value = "模版KEY", required = true)
    private String            key;
    
    /**语言编码（en_US,zh_CN,zh_HK)*/
    @NotNull(message = "语言编码")
    @ApiModelProperty(value = "语言编码（en_US,zh_CN,zh_HK)", required = true)
    private String            lang;
    
    /**模版类型(email:邮件、sms:短信)*/
    @NotNull(message = "模版类型(email:邮件、sms:短信)不可为空")
    @ApiModelProperty(value = "模版类型(email:邮件、sms:短信)", required = true)
    private String            type;
    
    /**标题*/
    @NotNull(message = "消息标题")
    @ApiModelProperty(value = "消息标题", required = true)
    private String            title;
    
    /**模版内容*/
    @NotNull(message = "模版内容不可为空")
    @ApiModelProperty(value = "模版内容", required = true)
    private String            content;
    
    /**描述*/
    @ApiModelProperty(value = "描述")
    private String            dest;
    
    /**创建人*/
    @ApiModelProperty(value = "创建人")
    private Long              createBy;
    
    /**创建人*/
    private String            createName;
    
    /**创建时间*/
    @ApiModelProperty(value = "创建时间")
    private Long              createDate;
    
    public String getKey()
    {
        return this.key;
    }
    
    public void setKey(String key)
    {
        this.key = key;
    }
    
    public String getLang()
    {
        return this.lang;
    }
    
    public void setLang(String lang)
    {
        this.lang = lang;
    }
    
    public String getType()
    {
        return this.type;
    }
    
    public void setType(String type)
    {
        this.type = type;
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
        return this.content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public String getDest()
    {
        return this.dest;
    }
    
    public void setDest(String dest)
    {
        this.dest = dest;
    }
    
    public Long getCreateBy()
    {
        return this.createBy;
    }
    
    public void setCreateBy(Long createBy)
    {
        this.createBy = createBy;
    }
    
    public String getCreateName()
    {
        return createName;
    }
    
    public void setCreateName(String createName)
    {
        this.createName = createName;
    }
    
    public Long getCreateDate()
    {
        return this.createDate;
    }
    
    public void setCreateDate(Long createDate)
    {
        this.createDate = createDate;
    }
}

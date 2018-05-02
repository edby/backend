/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.entity;

import com.blocain.bitms.orm.core.GenericEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 消息表 实体对象
 * <p>File：Message.java</p>
 * <p>Title: Message</p>
 * <p>Description:Message</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "消息表")
public class Message extends GenericEntity
{
    private static final long serialVersionUID = 1L;
    
    /**语言类型(zh_CN中文简体、zh_HK中文繁体、en_US英文)*/
    @NotNull(message = "语言类型(zh_CN中文简体、zh_HK中文繁体、en_US英文)不可为空")
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
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String            remark;
    
    /**创建人*/
    @NotNull(message = "创建人不可为空")
    @ApiModelProperty(value = "创建人")
    private Long              createBy;
    
    /**创建时间*/
    @NotNull(message = "创建时间不可为空")
    @ApiModelProperty(value = "创建时间")
    private Long              createDate;
    
    /**修改人*/
    @ApiModelProperty(value = "修改人")
    private Long              updateBy;
    
    /**修改时间*/
    @ApiModelProperty(value = "修改时间")
    private Long              updateDate;
    
    /**消息接收人*/
    @ApiModelProperty(value = "帐户ID")
    private Long              accountId;
    
    /**消息接收人账户*/
    private String            accountName;
    
    /**用户界面传值 查询开始时间 */
    private String            timeStart;
    
    /**用户界面传值 查询结束时间 */
    private String            timeEnd;
    
    public String getLangType()
    {
        return this.langType;
    }
    
    public void setLangType(String langType)
    {
        this.langType = langType;
    }
    
    public String getTitle()
    {
        return this.title;
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
    
    public Long getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public String getAccountName()
    {
        return accountName;
    }
    
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    
    public String getTimeStart()
    {
        return timeStart;
    }
    
    public void setTimeStart(String timeStart)
    {
        this.timeStart = timeStart;
    }
    
    public String getTimeEnd()
    {
        return timeEnd;
    }
    
    public void setTimeEnd(String timeEnd)
    {
        this.timeEnd = timeEnd;
    }
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.common.entity;

import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;

import com.blocain.bitms.orm.core.GenericEntity;

/**
 * 短信记录表 实体对象
 * <p>File：MsgRecord.java</p>
 * <p>Title: MsgRecord</p>
 * <p>Description:MsgRecord</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Document(collection = "MsgRecord")
public class MsgRecord extends GenericEntity
{
    private static final long serialVersionUID = 1L;
    
    /**手机号*/
    @NotNull(message = "消息类型(email:邮件、sms:短信)")
    private String            type;
    
    /**手机号*/
    @NotNull(message = "发送对象")
    private String            object;
    
    /**内容*/
    @NotNull(message = "内容不可为空")
    private String            content;
    
    /**创建时间*/
    @NotNull(message = "创建时间不可为空")
    private Long              createDate;
    
    /**发送状态（0：成功，1：失败）*/
    @NotNull(message = "发送状态（0：成功，1：失败）不可为空")
    private Boolean           status;
    
    /**用户界面传值 查询开始时间 */
    private Long              timeStart;
    
    /**用户界面传值 查询结束时间 */
    private Long              timeEnd;
    
    public MsgRecord()
    {
    }
    
    public MsgRecord(String type, String object, String content)
    {
        this.type = type;
        this.object = object;
        this.content = content;
    }
    
    public MsgRecord(String type, String object, String content, Boolean status)
    {
        this.type = type;
        this.object = object;
        this.content = content;
        this.status = status;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public String getObject()
    {
        return object;
    }
    
    public void setObject(String object)
    {
        this.object = object;
    }
    
    public String getContent()
    {
        return this.content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public Long getCreateDate()
    {
        return this.createDate;
    }
    
    public void setCreateDate(Long createDate)
    {
        this.createDate = createDate;
    }
    
    public Boolean getStatus()
    {
        return this.status;
    }
    
    public void setStatus(Boolean status)
    {
        this.status = status;
    }
    
    public Long getTimeStart()
    {
        return timeStart;
    }
    
    public void setTimeStart(Long timeStart)
    {
        this.timeStart = timeStart;
    }
    
    public Long getTimeEnd()
    {
        return timeEnd;
    }
    
    public void setTimeEnd(Long timeEnd)
    {
        this.timeEnd = timeEnd;
    }
}

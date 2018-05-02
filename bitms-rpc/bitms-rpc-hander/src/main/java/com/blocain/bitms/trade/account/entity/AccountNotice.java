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
 * 用户消息公告关联表 实体对象
 * <p>File：AccountNotice.java</p>
 * <p>Title: AccountNotice</p>
 * <p>Description:AccountNotice</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "用户消息公告关联表")
public class AccountNotice extends GenericEntity
{
    private static final long serialVersionUID = 1L;
    
    /**通知编号*/
    @NotNull(message = "通知编号不可为空")
    @ApiModelProperty(value = "通知编号", required = true)
    private Long              noticeId;
    
    /**帐户编号*/
    @NotNull(message = "用户编号不可为空")
    @ApiModelProperty(value = "帐户编号", required = true)
    private Long              accountId;
    
    /**读取状态（0:未读，1:已读）*/
    @NotNull(message = "读取状态（0:未读，1:已读）不可为空")
    @ApiModelProperty(value = "读取状态（0:未读，1:已读）", required = true)
    private Boolean           readFlag;
    
    public Long getNoticeId()
    {
        return this.noticeId;
    }
    
    public void setNoticeId(Long noticeId)
    {
        this.noticeId = noticeId;
    }
    
    public Long getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public Boolean getReadFlag()
    {
        return this.readFlag;
    }
    
    public void setReadFlag(Boolean readFlag)
    {
        this.readFlag = readFlag;
    }
}

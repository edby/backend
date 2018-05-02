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
 * 用户消息关联表 实体对象
 * <p>File：AccountMessage.java</p>
 * <p>Title: AccountMessage</p>
 * <p>Description:AccountMessage</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "用户消息关联表")
public class AccountMessage extends GenericEntity
{
    private static final long serialVersionUID = 1L;
    
    /**消息编号*/
    @NotNull(message = "消息编号不可为空")
    @ApiModelProperty(value = "消息编号", required = true)
    private Long              messageId;
    
    /**帐户编号*/
    @NotNull(message = "帐户编号不可为空")
    @ApiModelProperty(value = "帐户编号", required = true)
    private Long              accountId;
    
    /**读取状态（0:未读，1:已读）*/
    @NotNull(message = "读取状态（0:未读，1:已读）不可为空")
    @ApiModelProperty(value = "读取状态（0:未读，1:已读）", required = true)
    private Boolean           readFlag;
    
    public Long getMessageId()
    {
        return this.messageId;
    }
    
    public void setMessageId(Long messageId)
    {
        this.messageId = messageId;
    }
    
    public Long getAccountId()
    {
        return this.accountId;
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

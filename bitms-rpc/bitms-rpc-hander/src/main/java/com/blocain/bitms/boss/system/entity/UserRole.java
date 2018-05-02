/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.system.entity;

import javax.validation.constraints.NotNull;

import com.blocain.bitms.orm.core.GenericEntity;

/**
 * 用户角色权限表 实体对象
 * <p>File：UserRole.java</p>
 * <p>Title: UserRole</p>
 * <p>Description:UserRole</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class UserRole extends GenericEntity
{
    private static final long serialVersionUID = 1L;
    
    /**角色ID*/
    @NotNull(message = "角色ID不可为空")
    private Long              roleId;
    
    /**用户ID*/
    @NotNull(message = "用户ID不可为空")
    private Long              userId;
    
    public UserRole()
    {
    }
    
    public UserRole(Long roleId, Long userId)
    {
        this.roleId = roleId;
        this.userId = userId;
    }
    
    public Long getRoleId()
    {
        return this.roleId;
    }
    
    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }
    
    public Long getUserId()
    {
        return this.userId;
    }
    
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }
}

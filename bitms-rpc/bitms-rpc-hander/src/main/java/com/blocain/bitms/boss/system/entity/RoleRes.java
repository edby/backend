/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.system.entity;

import javax.validation.constraints.NotNull;

import com.blocain.bitms.orm.core.GenericEntity;

/**
 * 角色权限信息表 实体对象
 * <p>File：RoleRes.java</p>
 * <p>Title: RoleRes</p>
 * <p>Description:RoleRes</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class RoleRes extends GenericEntity
{
    private static final long serialVersionUID = 1L;
    
    /**角色ID*/
    @NotNull(message = "角色ID不可为空")
    private Long              roleId;
    
    /**资源ID*/
    @NotNull(message = "资源ID不可为空")
    private Long              resId;

    public RoleRes(Long roleId, Long resId)
    {
        this.roleId = roleId;
        this.resId = resId;
    }
    
    public Long getRoleId()
    {
        return this.roleId;
    }
    
    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }
    
    public Long getResId()
    {
        return this.resId;
    }
    
    public void setResId(Long resId)
    {
        this.resId = resId;
    }
}

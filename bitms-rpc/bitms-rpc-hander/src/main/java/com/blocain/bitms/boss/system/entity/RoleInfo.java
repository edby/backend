/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.system.entity;

import com.blocain.bitms.orm.core.GenericEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色信息表 实体对象
 * <p>File：RoleInfo.java</p>
 * <p>Title: RoleInfo</p>
 * <p>Description:RoleInfo</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class RoleInfo extends GenericEntity
{
    private static final long serialVersionUID = 1L;
    
    /**角色编码*/
    @NotNull(message = "角色编码不可为空")
    private String            roleCode;
    
    /**角色名称*/
    @NotNull(message = "角色名称不可为空")
    private String            roleName;
    
    /**角色描述*/
    private String            roleDest;
    
    /**创建人*/
    private Long              createBy;
    
    /**创改人*/
    private String            createByName;
    
    /**创建时间*/
    private Long              createDate;
    
    /**修改人*/
    private Long              updateBy;
    
    /**修改时间*/
    private Long              updateDate;
    
    /**资源信息*/
    List<Resources>           resources;
    
    /**是否需要绑定GA  1需要 0不需要*/
    Integer                   needGa;
    
    public String getRoleCode()
    {
        return this.roleCode;
    }
    
    public void setRoleCode(String roleCode)
    {
        this.roleCode = roleCode;
    }
    
    public String getRoleName()
    {
        return this.roleName;
    }
    
    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }
    
    public String getRoleDest()
    {
        return this.roleDest;
    }
    
    public void setRoleDest(String roleDest)
    {
        this.roleDest = roleDest;
    }
    
    public Long getCreateBy()
    {
        return this.createBy;
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
    
    public List<Resources> getResources()
    {
        return resources;
    }
    
    public void setResources(List<Resources> resources)
    {
        this.resources = resources;
    }
    
    public Integer getNeedGa()
    {
        return needGa;
    }
    
    public void setNeedGa(Integer needGa)
    {
        this.needGa = needGa;
    }
}

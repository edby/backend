/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.system.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.boss.system.entity.RoleRes;

/**
 * 角色权限信息表 持久层接口
 * <p>File：RoleResDao.java </p>
 * <p>Title: RoleResDao </p>
 * <p>Description:RoleResDao </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface RoleResMapper extends GenericMapper<RoleRes>
{
    /**
     * 根据角色编号删除权限
     * @param roleId
     * @return
     */
    int removeByRoleId(Long roleId);
}

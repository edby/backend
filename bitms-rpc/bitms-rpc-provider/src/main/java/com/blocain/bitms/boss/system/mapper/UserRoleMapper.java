/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.system.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.boss.system.entity.UserRole;

/**
 * 用户角色权限表 持久层接口
 * <p>File：UserRoleDao.java </p>
 * <p>Title: UserRoleDao </p>
 * <p>Description:UserRoleDao </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface UserRoleMapper extends GenericMapper<UserRole>
{
    /**
     * 根据用户ID删除
     * @param userId
     */
    void removeByUser(Long userId);
}

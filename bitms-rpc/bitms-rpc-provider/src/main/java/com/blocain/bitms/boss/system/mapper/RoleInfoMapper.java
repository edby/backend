/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.system.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.boss.system.entity.RoleInfo;

import java.util.List;

/**
 * 角色信息表 持久层接口
 * <p>File：RoleInfoDao.java </p>
 * <p>Title: RoleInfoDao </p>
 * <p>Description:RoleInfoDao </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface RoleInfoMapper extends GenericMapper<RoleInfo>
{
    /**
     * 根据用户ID取角色
     * @param userId
     * @return
     */
    List<RoleInfo> findByUserId(Long userId);
}

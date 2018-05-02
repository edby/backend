/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.system.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.boss.system.entity.Resources;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资源菜单信息表 持久层接口
 * <p>File：ResourcesDao.java </p>
 * <p>Title: ResourcesDao </p>
 * <p>Description:ResourcesDao </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface ResourcesMapper extends GenericMapper<Resources>
{
    /**
     * 根据ID取资源菜单
     * @param roleId
     * @return {@link Resources}
     */
    List<Resources> findByRoleId(Long roleId);
    
    /**
     * 根据上线ID取资源
     * @param parentId
     * @return
     */
    List<Resources> findByParentId(@Param("parentId") Long parentId);
}

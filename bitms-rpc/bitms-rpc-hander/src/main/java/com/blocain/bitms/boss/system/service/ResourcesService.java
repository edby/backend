/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.system.service;

import java.util.List;

import com.blocain.bitms.boss.system.entity.Resources;
import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.TreeModel;
import com.blocain.bitms.tools.exception.BusinessException;

/**
 * 资源菜单信息表 服务接口
 * <p>File：ResourcesService.java </p>
 * <p>Title: ResourcesService </p>
 * <p>Description:ResourcesService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface ResourcesService extends GenericService<Resources>
{
    /**
     * 根据ID取资源菜单
     * @param roleId
     * @return {@link Resources}
     */
    List<Resources> findByRoleId(Long roleId);
    
    /**
     * 取资源数据并返回树形对象
     * @param id
     * @return {@link List< TreeModel >}
     * @throws BusinessException
     */
    List<TreeModel> findByResources(Long id) throws BusinessException;
}

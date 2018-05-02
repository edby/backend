/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.system.service;

import java.util.List;

import com.blocain.bitms.boss.system.entity.RoleInfo;
import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.TreeModel;
import com.blocain.bitms.tools.exception.BusinessException;

/**
 * 角色信息表 服务接口
 * <p>File：RoleInfoService.java </p>
 * <p>Title: RoleInfoService </p>
 * <p>Description:RoleInfoService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface RoleInfoService extends GenericService<RoleInfo>
{
    /**
     * 根据用户ID取角色
     * @param userId
     * @return
     */
    List<RoleInfo> findByUserId(Long userId);
    
    /**
     * 保存角色授权
     * @param id
     * @param resourceIds
     * @throws BusinessException
     */
    void saveGrant(Long id, String resourceIds) throws BusinessException;
    
    /**
     * 返回以TREEMODEL对象的所有数据
     * @param role
     * @return {@link List<TreeModel>}
     * @throws BusinessException
     */
    List<TreeModel> findByRole(RoleInfo role) throws BusinessException;

    /**
     * 批量插入测试
     * @param list
     * @param flag
     * @return
     * @throws BusinessException
     */
    int insertBatch(List<RoleInfo> list, boolean flag) throws BusinessException;
}

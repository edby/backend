/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocain.bitms.boss.system.entity.RoleInfo;
import com.blocain.bitms.boss.system.entity.RoleRes;
import com.blocain.bitms.boss.system.mapper.RoleInfoMapper;
import com.blocain.bitms.boss.system.mapper.RoleResMapper;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.bean.TreeModel;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.ListUtils;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.google.common.collect.Lists;

/**
 * 角色信息表 服务实现类
 * <p>File：RoleInfo.java </p>
 * <p>Title: RoleInfo </p>
 * <p>Description:RoleInfo </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class RoleInfoServiceImpl extends GenericServiceImpl<RoleInfo> implements RoleInfoService
{
    private RoleInfoMapper roleInfoMapper;
    
    @Autowired
    private RoleResMapper  roleResMapper;
    
    @Autowired
    public RoleInfoServiceImpl(RoleInfoMapper roleInfoMapper)
    {
        super(roleInfoMapper);
        this.roleInfoMapper = roleInfoMapper;
    }
    
    @Override
    public List<RoleInfo> findByUserId(Long userId)
    {
        return roleInfoMapper.findByUserId(userId);
    }
    
    @Override
    public void saveGrant(Long id, String resourceIds) throws BusinessException
    {
        if (null == id) { throw new BusinessException("角色编码不可为空"); }
        if (StringUtils.isBlank(resourceIds)) { throw new BusinessException("角色授权资源不可为空"); }
        roleResMapper.removeByRoleId(id);// 先删除原有的授权数据
        String[] resIds = resourceIds.split(",");
        List<RoleRes> resList = Lists.newArrayList();
        RoleRes res;
        for (String resId : resIds)
        {
            res = new RoleRes(id, Long.parseLong(resId));
            res.setId(SerialnoUtils.buildPrimaryKey());
            resList.add(res);
        }
        roleResMapper.insertBatch(resList);
    }
    
    @Override
    public List<TreeModel> findByRole(RoleInfo role) throws BusinessException
    {
        List<RoleInfo> data = roleInfoMapper.findList(role);
        if (ListUtils.isNull(data)) return null;
        List<TreeModel> models = Lists.newArrayList();
        for (RoleInfo roleInfo : data)
        {
            models.add(conventObject(roleInfo));
        }
        return models;
    }
    
    /**
     * 转换对象
     * @param roleInfo
     * @return
     */
    private TreeModel conventObject(RoleInfo roleInfo)
    {
        TreeModel model = new TreeModel();
        model.setId(roleInfo.getId());
        model.setText(roleInfo.getRoleName());
        return model;
    }
    
    @Override
    public int insertBatch(List<RoleInfo> list, boolean flag) throws BusinessException
    {
        int count = roleInfoMapper.insertBatch(list);
        if (flag) { throw new BusinessException("数据插入失败！"); }
        return count;
    }
}

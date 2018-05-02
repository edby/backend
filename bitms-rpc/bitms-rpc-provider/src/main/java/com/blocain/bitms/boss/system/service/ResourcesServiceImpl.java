/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.system.service;

import java.util.List;

import com.blocain.bitms.tools.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocain.bitms.boss.system.entity.Resources;
import com.blocain.bitms.boss.system.mapper.ResourcesMapper;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.bean.TreeModel;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.ListUtils;
import com.google.common.collect.Lists;

/**
 * 资源菜单信息表 服务实现类
 * <p>File：Resources.java </p>
 * <p>Title: Resources </p>
 * <p>Description:Resources </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class ResourcesServiceImpl extends GenericServiceImpl<Resources> implements ResourcesService
{
    private ResourcesMapper resourcesMapper;
    
    @Autowired
    public ResourcesServiceImpl(ResourcesMapper resourcesMapper)
    {
        super(resourcesMapper);
        this.resourcesMapper = resourcesMapper;
    }
    
    @Override
    public List<Resources> findByRoleId(Long roleId)
    {
        return resourcesMapper.findByRoleId(roleId);
    }
    
    @Override
    public List<TreeModel> findByResources(Long id) throws BusinessException
    {
        List<Resources> data = resourcesMapper.findByParentId(id);
        if (ListUtils.isNull(data)) return null;
        List<TreeModel> models = Lists.newArrayList();
        for (Resources res : data)
        {
            models.add(conventResources(res));
        }
        return models;
    }
    
    /**
     * 转换对象
     * @param resource
     * @return
     */
    TreeModel conventResources(Resources resource)
    {
        TreeModel model = new TreeModel();
        model.setId(resource.getId());
        model.setPid(resource.getParentId());
        if (null != resource.getParentId())
        {
            model.setState("closed");
        }
        model.setIconCls(resource.getIcon());
        model.setText(resource.getResName());
        return model;
    }
}

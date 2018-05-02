/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.system.controller;

import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.bean.TreeModel;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.utils.CalendarUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blocain.bitms.boss.system.entity.Resources;
import com.blocain.bitms.boss.system.service.ResourcesService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 资源菜单信息表 控制器
 * <p>File：ResourcesController.java </p>
 * <p>Title: ResourcesController </p>
 * <p>Description:ResourcesController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.SYSTEM)
public class ResourcesController extends GenericController
{
    @Autowired(required = false)
    private ResourcesService resourcesService;
    
    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/resource")
    @RequiresPermissions("system:setting:resource:index")
    public String list() throws BusinessException
    {
        return "boss/system/resource/treelist";
    }
    
    /**
     * 编辑页面导航
     * @param id
     * @return {@link String}
     * @throws BusinessException
     */
    @RequestMapping(value = "/resource/modify")
    @RequiresPermissions("system:setting:resource:operator")
    public ModelAndView modify(Long id, Long parentId) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("boss/system/resource/modify");
        Resources resource = new Resources();
        if (null != parentId) resource.setParentId(parentId);
        if (null != id) resource = resourcesService.selectByPrimaryKey(id);
        mav.addObject("resource", resource);
        return mav;
    }
    
    /**
     * 返回以TREEMODEL对象的所有数据
     * @return {@link List}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/resource/tree")
    public List<TreeModel> tree(Long id) throws BusinessException
    {
        return resourcesService.findByResources(id);
    }
    
    /**
     * 操作数据
     * @param info
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequiresPermissions("system:setting:resource:operator")
    @RequestMapping(value = "/resource/save", method = RequestMethod.POST)
    public JsonMessage save(Resources info) throws BusinessException
    {
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        long currentDate = CalendarUtils.getCurrentLong();
        if (null == info.getId())
        {
            info.setCreateBy(principal.getId());
            info.setCreateDate(currentDate);
        }
        info.setUpdateBy(principal.getId());
        info.setUpdateDate(currentDate);
        resourcesService.save(info);
        return json;
    }
    
    /**ø
     * 查询数据
     * @param entity
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequiresPermissions("system:setting:resource:data")
    @RequestMapping(value = "/resource/data", method = RequestMethod.POST)
    public List<Resources> data(Resources entity) throws BusinessException
    {
        return resourcesService.findList(entity);
    }
    
    /**
     * 根据指定ID删除
     * @param ids
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequiresPermissions("system:setting:resource:operator")
    @RequestMapping(value = "/resource/del", method = RequestMethod.POST)
    public JsonMessage del(String ids) throws BusinessException
    {
        resourcesService.deleteBatch(ids.split(","));
        return getJsonMessage(CommonEnums.SUCCESS);
    }
}

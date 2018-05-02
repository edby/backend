/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.system.controller;

import com.blocain.bitms.boss.common.entity.SysParameter;
import com.blocain.bitms.boss.system.entity.Resources;
import com.blocain.bitms.boss.system.service.ResourcesService;
import com.blocain.bitms.tools.bean.TreeModel;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.utils.StringUtils;
import com.google.common.collect.Lists;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.blocain.bitms.boss.system.entity.RoleInfo;
import com.blocain.bitms.boss.system.service.RoleInfoService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.CalendarUtils;

import java.util.List;

/**
 * 角色信息表 控制器
 * <p>File：RoleInfoController.java </p>
 * <p>Title: RoleInfoController </p>
 * <p>Description:RoleInfoController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.SYSTEM)
public class RoleInfoController extends GenericController
{
    @Autowired(required = false)
    private RoleInfoService  roleInfoService;
    
    @Autowired(required = false)
    private ResourcesService resourcesService;
    
    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/role")
    @RequiresPermissions("system:setting:role:index")
    public String list() throws BusinessException
    {
        return "boss/system/role/list";
    }
    
    /**
     * 授权页面导航
     * @param id
     * @return {@link ModelAndView}
     * @throws BusinessException
     */
    @RequestMapping(value = "/role/grant")
    @RequiresPermissions("system:setting:role:operator")
    public ModelAndView grant(String id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("boss/system/role/roleGrant");
        mav.addObject("id", id);
        return mav;
    }
    
    /**
     * 添加或修改角色
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/role/modify")
    @RequiresPermissions("system:setting:role:operator")
    public ModelAndView modify(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("boss/system/role/modify");
        RoleInfo role = new RoleInfo();
        if (null != id)
        {
            role = roleInfoService.selectByPrimaryKey(id);
        }
        mav.addObject("role", role);
        return mav;
    }
    
    /**
     * 操作角色信息表
     * @param info
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequiresPermissions("system:setting:role:operator")
    @RequestMapping(value = "/role/save", method = RequestMethod.POST)
    public JsonMessage save(RoleInfo info) throws BusinessException
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
        roleInfoService.save(info);
        return json;
    }
    
    /**
     * 保存角色授权信息
     * @param id
     * @param resourceIds
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequiresPermissions("system:setting:role:operator")
    @RequestMapping(value = "/role/saveGrant", method = RequestMethod.POST)
    public JsonMessage saveGrant(Long id, String resourceIds) throws BusinessException
    {
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        roleInfoService.saveGrant(id, resourceIds);
        return json;
    }
    
    /**
     * 查询角色信息表
     * @param entity
     * @param pagin
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @ResponseBody
    @RequiresPermissions("system:setting:role:data")
    @RequestMapping(value = "/role/data", method = RequestMethod.POST)
    public JsonMessage data(RoleInfo entity, Pagination pagin) throws BusinessException
    {
        PaginateResult<RoleInfo> result = roleInfoService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 根据指定ID删除
     * @param ids
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequiresPermissions("system:setting:role:operator")
    @RequestMapping(value = "/role/del", method = RequestMethod.POST)
    public JsonMessage del(String ids) throws BusinessException
    {
        roleInfoService.removeBatch(ids.split(","));
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 角色授权信息
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/role/findByRoleId")
    @RequiresPermissions("system:setting:role:data")
    public JsonMessage findByRoleId(Long roleId) throws BusinessException
    {
        List<Resources> data = resourcesService.findByRoleId(roleId);
        return getJsonMessage(CommonEnums.SUCCESS, data);
    }
    
    /**
     * 返回以TREEMODEL对象的所有数据
     * @return {@link List}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/role/tree")
    public List<TreeModel> tree(RoleInfo role) throws BusinessException
    {
        if (null == role) role = new RoleInfo();
        return roleInfoService.findByRole(role);
    }
}

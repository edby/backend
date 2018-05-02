/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.system.controller;

import com.blocain.bitms.boss.system.entity.Organization;
import com.blocain.bitms.boss.system.service.OrganizationService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.TreeModel;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.CalendarUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 机构信息表 控制器
 * <p>File：OrganizationController.java </p>
 * <p>Title: OrganizationController </p>
 * <p>Description:OrganizationController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.SYSTEM)
public class OrganizationController extends GenericController
{
    @Autowired(required = false)
    private OrganizationService organizationService;
    
    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/organiz")
    @RequiresPermissions("system:setting:organiz:index")
    public String list() throws BusinessException
    {
        return "boss/system/organiz/treelist";
    }
    
    /**
     * 编辑页面导航
     * @param id
     * @return {@link String}
     * @throws BusinessException
     */
    @RequestMapping(value = "/organiz/modify")
    @RequiresPermissions("system:setting:organiz:operator")
    public ModelAndView modify(Long id, Long parentId) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("boss/system/organiz/modify");
        Organization organiz = new Organization();
        if (null != parentId) organiz.setParentId(parentId);
        if (null != id) organiz = organizationService.selectByPrimaryKey(id);
        mav.addObject("organiz", organiz);
        return mav;
    }
    
    /**
     * 返回以TREEMODEL对象的所有数据
     * @return {@link List}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/organiz/tree")
    public List<TreeModel> tree(Organization organiz) throws BusinessException
    {
        if (null == organiz) organiz = new Organization();
        return organizationService.findByOrganization(organiz);
    }
    
    /**
     * 操作数据
     * @param info
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequiresPermissions("system:setting:organiz:operator")
    @RequestMapping(value = "/organiz/save", method = RequestMethod.POST)
    public JsonMessage save(Organization info) throws BusinessException
    {
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        long currentDate = CalendarUtils.getCurrentLong();
        if (null == info.getId())
        {
            info.setCreateDate(currentDate);
            info.setCreateBy(principal.getId());
        }
        info.setUpdateBy(principal.getId());
        info.setUpdateDate(currentDate);
        organizationService.save(info);
        return json;
    }
    
    /**
     * 查询数据
     * @param entity
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequiresPermissions("system:setting:organiz:data")
    @RequestMapping(value = "/organiz/data", method = RequestMethod.POST)
    public List<Organization> data(Organization entity) throws BusinessException
    {
        return organizationService.findList(entity);
    }
    
    /**
     * 根据指定ID删除
     * @param ids
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequiresPermissions("system:setting:organiz:operator")
    @RequestMapping(value = "/organiz/del", method = RequestMethod.POST)
    public JsonMessage del(String ids) throws BusinessException
    {
        organizationService.deleteBatch(ids.split(","));
        return getJsonMessage(CommonEnums.SUCCESS);
    }
}

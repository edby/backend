/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.common.controller;

import com.blocain.bitms.tools.consts.BitmsConst;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.blocain.bitms.boss.common.entity.Region;
import com.blocain.bitms.boss.common.service.RegionService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.StringUtils;

/**
 * 区域代码 控制器
 * <p>File：RegionController.java </p>
 * <p>Title: RegionController </p>
 * <p>Description:RegionController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.COMMON)
public class RegionController extends GenericController
{
    @Autowired(required = false)
    private RegionService regionService;
    
    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/region")
    @RequiresPermissions("system:setting:region:index")
    public String list() throws BusinessException
    {
        return "boss/common/region/list";
    }
    
    /**
     * 编辑页面导航
     * @param id 
     * @return {@link String}
     * @throws BusinessException
     */
    @RequestMapping(value = "/region/modify")
    @RequiresPermissions("system:setting:region:operator")
    public ModelAndView modify(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("boss/common/region/modify");
        Region region = new Region();
        if (null != id)
        {
            region = regionService.selectByPrimaryKey(id);
        }
        mav.addObject("region", region);
        return mav;
    }
    
    /**
     * 保存 区域代码
     * @param info
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequiresPermissions("system:setting:region:operator")
    @RequestMapping(value = "/region/save", method = RequestMethod.POST)
    public JsonMessage save(Region info) throws BusinessException
    {
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        regionService.save(info);
        return json;
    }
    
    /**
     * 查询区域代码
     * <p>
     *     @RequestBody 此注解加入后接收的参数将是JSON字符串，加入此注解是的目的是为了统一数据交互方式，实现真正的前后端分离。
     *     若相应模块中需要以FORM表单方式提交，请将此注册取消掉;
     * </p>
     * @param entity
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/region/data")
    @RequiresPermissions("system:setting:region:data")
    public JsonMessage data(Region entity, Pagination pagin) throws BusinessException
    {
        PaginateResult<Region> result = regionService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 根据指定ID删除
     * @param ids
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/region/del", method = RequestMethod.POST)
    @RequiresPermissions("system:setting:region:operator")
    public JsonMessage del(String ids) throws BusinessException
    {
        regionService.removeBatch(ids.split(","));
        return getJsonMessage(CommonEnums.SUCCESS);
    }
}

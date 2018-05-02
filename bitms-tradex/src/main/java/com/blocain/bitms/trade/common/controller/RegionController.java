/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.blocain.bitms.boss.common.entity.Region;
import com.blocain.bitms.boss.common.service.RegionService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
@Api(description = "区域代码")
@RequestMapping(BitmsConst.COMMON)
public class RegionController extends GenericController
{
    @Autowired(required = false)
    private RegionService regionService;
    
    /**
     * 区域代码中心页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/region", method = RequestMethod.GET)
    @ApiOperation(value = "区域代码中心页面导航", httpMethod = "GET")
    public ModelAndView index() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("account/region");
        return mav;
    }
    
    /**
     * 查询区域代码
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/region/data")
    @ApiOperation(value = "查询区域代码", httpMethod = "POST")
    public JsonMessage data(@ModelAttribute Region entity, @ModelAttribute Pagination pagin) throws BusinessException
    {
        PaginateResult<Region> data = regionService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, data);
    }
    
    /**
     * 查询区域代码
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/region/findAll")
    @ApiOperation(value = "查询区域代码", httpMethod = "POST")
    public JsonMessage findAll() throws BusinessException
    {
        return getJsonMessage(CommonEnums.SUCCESS, regionService.selectAll());
    }
}

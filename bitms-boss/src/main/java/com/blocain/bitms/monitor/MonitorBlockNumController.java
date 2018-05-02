/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blocain.bitms.monitor.entity.MonitorBlockNum;
import com.blocain.bitms.monitor.service.MonitorBlockNumService;
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
 * MonitorBlockNum 控制器
 * <p>File：MonitorBlockNumController.java </p>
 * <p>Title: MonitorBlockNumController </p>
 * <p>Description:MonitorBlockNumController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author fzk
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.MONITOR)
@Api(description = "MonitorBlockNum")
public class MonitorBlockNumController extends GenericController
{
    @Autowired(required = false)
    private MonitorBlockNumService monitorBlockNumService;
    
    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/blockNum")
    @RequiresPermissions("monitor:setting:monitorblocknum:index")
    public String list() throws BusinessException
    {
        return "monitor/blockerc20/blockNum";
    }
    
    // /**
    // * 操作MonitorBlockNum
    // * @param info
    // * @return {@link JsonMessage}
    // * @throws BusinessException
    // */
    // @ResponseBody
    // @RequestMapping(value = "/save", method = RequestMethod.POST)
    // @ApiOperation(value = "保存MonitorBlockNum", httpMethod = "POST")
    // public JsonMessage save(@ModelAttribute MonitorBlockNum info) throws BusinessException
    // {
    // JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
    // if (beanValidator(json, info))
    // {
    // monitorBlockNumService.save(info);
    // }
    // return json;
    // }
    /**
     * 查询MonitorBlockNum
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/blockNum/data", method = RequestMethod.POST)
    @RequiresPermissions("monitor:setting:monitorblocknum:data")
    @ApiOperation(value = "查询MonitorBlockNum", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage data(@ModelAttribute MonitorBlockNum entity, @ModelAttribute Pagination pagin) throws BusinessException
    {
        PaginateResult<MonitorBlockNum> result = monitorBlockNumService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    // /**
    // * 根据指定ID删除
    // * @param ids
    // * @return {@link JsonMessage}
    // * @throws BusinessException
    // */
    // @ResponseBody
    // @RequestMapping(value = "/del", method = RequestMethod.POST)
    // @ApiOperation(value = "根据指定ID删除", httpMethod = "POST")
    // @ApiImplicitParam(name = "ids", value = "以','分割的编号组",paramType = "form")
    // public JsonMessage del(String ids) throws BusinessException
    // {
    // monitorBlockNumService.deleteBatch(ids.split(","));
    // return getJsonMessage(CommonEnums.SUCCESS);
    // }
}

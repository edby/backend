/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor;

import com.blocain.bitms.tools.consts.BitmsConst;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blocain.bitms.monitor.entity.MonitorERC20Bal;
import com.blocain.bitms.monitor.service.MonitorERC20BalService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;


/**
 * MonitorERC20Bal 控制器
 * <p>File：MonitorERC20BalController.java </p>
 * <p>Title: MonitorERC20BalController </p>
 * <p>Description:MonitorERC20BalController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.MONITOR)
@Api(description = "MonitorERC20Bal")
public class MonitorERC20BalController extends GenericController
{
    @Autowired(required = false)
    private MonitorERC20BalService monitorERC20BalService;


    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/erc20Bal")
    @RequiresPermissions("monitor:setting:monitorerc20bal:index")
    public String list() throws BusinessException
    {
        return "monitor/blockerc20/erc20BalList";
    }



//    /**
//     * 操作MonitorERC20Bal
//     * @param info
//     * @return {@link JsonMessage}
//     * @throws BusinessException
//     */
//    @ResponseBody
//    @RequestMapping(value = "/save", method = RequestMethod.POST)
//    @ApiOperation(value = "保存MonitorERC20Bal", httpMethod = "POST")
//    public JsonMessage save(@ModelAttribute MonitorERC20Bal info) throws BusinessException
//    {
//        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
//        if (beanValidator(json, info))
//        {
//            monitorERC20BalService.save(info);
//        }
//        return json;
//    }

    /**
     * 查询MonitorERC20Bal
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/erc20Bal/data", method = RequestMethod.POST)
    @RequiresPermissions("monitor:setting:monitorerc20bal:data")
    @ApiOperation(value = "查询MonitorERC20Bal", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage data(@ModelAttribute MonitorERC20Bal entity,@ModelAttribute Pagination pagin) throws BusinessException
    {
        PaginateResult<MonitorERC20Bal> result = monitorERC20BalService.findJoinList(pagin,entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

//    /**
//     * 根据指定ID删除
//     * @param ids
//     * @return {@link JsonMessage}
//     * @throws BusinessException
//     */
//    @ResponseBody
//    @RequestMapping(value = "/del", method = RequestMethod.POST)
//    @ApiOperation(value = "根据指定ID删除", httpMethod = "POST")
//    @ApiImplicitParam(name = "ids", value = "以','分割的编号组",paramType = "form")
//    public JsonMessage del(String ids) throws BusinessException
//    {
//        monitorERC20BalService.deleteBatch(ids.split(","));
//        return getJsonMessage(CommonEnums.SUCCESS);
//    }
}

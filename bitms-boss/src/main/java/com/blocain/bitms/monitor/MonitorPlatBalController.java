/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor;

import com.blocain.bitms.monitor.entity.MonitorPlatBal;
import com.blocain.bitms.monitor.service.MonitorPlatBalService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


/**
 * MonitorPlatBal 控制器
 * <p>File：MonitorPlatBalController.java </p>
 * <p>Title: MonitorPlatBalController </p>
 * <p>Description:MonitorPlatBalController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.MONITOR)
@Api(description = "MonitorPlatBal")
public class MonitorPlatBalController extends GenericController
{
    @Autowired(required = false)
    private MonitorPlatBalService monitorPlatBalService;

    /**
     * 列表页面导航-平台数字资产总额监控
     *
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/platbal")
    @RequiresPermissions("monitor:setting:platbal:index")
    public ModelAndView list(String chkDate, String monitorType, String monitorSubType, String chkResult) throws BusinessException {

        ModelAndView mav = new ModelAndView("monitor/monitor/monitorplatbal_list");
        mav.addObject("chkDate", chkDate);
        mav.addObject("monitorType", monitorType);
        mav.addObject("monitorSubType", monitorSubType);
        mav.addObject("chkResult", chkResult);
        return mav;
    }

    /**
     * 查询MonitorPlatBal
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/platbal/data", method = RequestMethod.POST)
    @RequiresPermissions("monitor:setting:platbal:data")
    @ApiOperation(value = "查询MonitorPlatBal", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage data(@ModelAttribute MonitorPlatBal entity,@ModelAttribute Pagination pagin) throws BusinessException
    {
        PaginateResult<MonitorPlatBal> result = monitorPlatBalService.findMonitorPlatBalList(pagin,entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
}

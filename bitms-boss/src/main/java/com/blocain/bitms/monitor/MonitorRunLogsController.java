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

import com.blocain.bitms.monitor.entity.MonitorRunLogs;
import com.blocain.bitms.monitor.service.MonitorRunLogsService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.servlet.ModelAndView;


/**
 * 监控服务运行日志 控制器
 * <p>File：MonitorRunLogsController.java </p>
 * <p>Title: MonitorRunLogsController </p>
 * <p>Description:MonitorRunLogsController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author fzk
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.MONITOR)
@Api(description = "MonitorRunLogs")
public class MonitorRunLogsController extends GenericController
{
    @Autowired(required = false)
    private MonitorRunLogsService monitorRunLogsService;


    /**
     * 列表页面导航-监控服务运行日志
     *
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/runlogs")
    @RequiresPermissions("monitor:setting:runlogs:index")
    public ModelAndView list() throws BusinessException {

        ModelAndView mav = new ModelAndView("monitor/monitor/monitorrunlogs_list");
        return mav;
    }
    /**
     * 查询监控服务运行日志
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/runlogs/data", method = RequestMethod.POST)
    @RequiresPermissions("monitor:setting:runlogs:data")
    @ApiOperation(value = "查询监控服务运行日志", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage data(@ModelAttribute MonitorRunLogs entity,@ModelAttribute Pagination pagin) throws BusinessException
    {
        PaginateResult<MonitorRunLogs> result = monitorRunLogsService.findMonitorRunLogsList(pagin,entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

}

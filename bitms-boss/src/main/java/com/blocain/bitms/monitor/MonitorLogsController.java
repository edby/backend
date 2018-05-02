/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;

import com.blocain.bitms.monitor.entity.MonitorLogs;
import com.blocain.bitms.monitor.service.MonitorLogsService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.DateConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.CalendarUtils;

/**
 * 监控日志表 控制器
 * <p>File：MonitorLogsController.java </p>
 * <p>Title: MonitorLogsController </p>
 * <p>Description:MonitorLogsController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.MONITOR)
@Api(description = "监控日志表")
public class MonitorLogsController extends GenericController
{
    @Autowired(required = false)
    private MonitorLogsService monitorLogsService;
    
    /**
     * 页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/log")
    @RequiresPermissions("monitor:setting:monitorlog:index")
    public String list() throws BusinessException
    {
        return "monitor/log/list";
    }
    
    /**
     * 监控日志记录
     * @param entity
     * @param pagin
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/log/data", method = RequestMethod.POST)
    @RequiresPermissions("monitor:setting:monitorlog:data")
    public JsonMessage data(MonitorLogs entity, String timeStart, String timeEnd, Pagination pagin) throws BusinessException
    {
        if (StringUtils.isNotBlank(timeStart))
        {
            entity.setTimeStart(CalendarUtils.getLongFromTime(timeStart, DateConst.DATE_FORMAT_YMDHMS));
        }
        if (StringUtils.isNotBlank(timeEnd))
        {
            entity.setTimeEnd(CalendarUtils.getLongFromTime(timeEnd, DateConst.DATE_FORMAT_YMDHMS));
        }
        PaginateResult<MonitorLogs> result = monitorLogsService.findMonitorLogsList(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
}

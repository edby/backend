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
import org.springframework.web.servlet.ModelAndView;

import com.blocain.bitms.monitor.entity.ArchiveRunLogs;
import com.blocain.bitms.monitor.service.ArchiveRunLogsService;
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
 * 归档运行日志 控制器
 * <p>File：ArchiveRunLogsController.java </p>
 * <p>Title: ArchiveRunLogsController </p>
 * <p>Description:ArchiveRunLogsController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.MONITOR)
@Api(description = "归档运行日志")
public class ArchiveRunLogsController extends GenericController
{
    @Autowired(required = false)
    private ArchiveRunLogsService archiveRunLogsService;
    
    /**
     * 列表页面导航-账户资金流水监控
     *
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/archiveRunLogs")
    @RequiresPermissions("monitor:setting:archiverunlogs:index")
    public ModelAndView list() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("monitor/archive/archiveRunLogs");
        return mav;
    }
    
    /**
     * 查询归档运行日志
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequiresPermissions("monitor:setting:archiverunlogs:data")
    @RequestMapping(value = "/archiveRunLogs/data", method = RequestMethod.POST)
    @ApiOperation(value = "查询归档运行日志", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage data(@ModelAttribute ArchiveRunLogs entity, @ModelAttribute Pagination pagin) throws BusinessException
    {
        PaginateResult<ArchiveRunLogs> result = archiveRunLogsService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
}

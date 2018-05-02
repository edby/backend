/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor;

import com.blocain.bitms.monitor.entity.MonitorConfig;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.consts.BitmsConst;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blocain.bitms.monitor.entity.MonitorLimitParam;
import com.blocain.bitms.monitor.service.MonitorLimitParamService;
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

import java.util.Date;


/**
 * MonitorLimitParam 控制器
 * <p>File：MonitorLimitParamController.java </p>
 * <p>Title: MonitorLimitParamController </p>
 * <p>Description:MonitorLimitParamController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.MONITOR)
@Api(description = "MonitorLimitParam")
public class MonitorLimitParamController extends GenericController
{
    @Autowired(required = false)
    private MonitorLimitParamService monitorLimitParamService;

    /**
     * 操作MonitorLimitParam
     * @param info
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/index/limitParam/save", method = RequestMethod.POST)
    @RequiresPermissions("monitor:setting:monitorindex:operator")
    @ApiOperation(value = "保存MonitorLimitParam", httpMethod = "POST")
    public JsonMessage save(@ModelAttribute MonitorLimitParam info) throws BusinessException
    {
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        info.setCreateBy(String.valueOf(principal.getId()));
        info.setCreateDate(new Date(System.currentTimeMillis()));
        if (beanValidator(json, info))
        {
            monitorLimitParamService.save(info);
        }
        return json;
    }

    /**
     * 查询MonitorLimitParam
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/index/limitParam/data", method = RequestMethod.POST)
    @RequiresPermissions("monitor:setting:monitorindex:data")
    @ApiOperation(value = "查询MonitorLimitParam", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage data(@ModelAttribute MonitorLimitParam entity,@ModelAttribute Pagination pagin) throws BusinessException
    {
        PaginateResult<MonitorLimitParam> result = monitorLimitParamService.findJoinList(pagin,entity);

        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    /**
     * 根据指定ID删除
     * @param ids
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/index/limitParam/del", method = RequestMethod.POST)
    @ApiOperation(value = "根据指定ID删除", httpMethod = "POST")
    @RequiresPermissions("monitor:setting:monitorindex:operator")
    @ApiImplicitParam(name = "ids", value = "以','分割的编号组",paramType = "form")
    public JsonMessage del(String ids) throws BusinessException
    {
        monitorLimitParamService.deleteBatch(ids.split(","));
        return getJsonMessage(CommonEnums.SUCCESS);
    }


    /**
     * 添加或修改监控配置
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/index/limitParam/modify")
    @RequiresPermissions("monitor:setting:monitorindex:operator")
    public ModelAndView modify(Long id,String idxName) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("monitor/config/index/modify_param");
        MonitorLimitParam param = new MonitorLimitParam();
        if (id != null)
        {
            param = monitorLimitParamService.selectByPrimaryKey(id);
            param.setIdxName(idxName);
        }
        mav.addObject("monitorLimitParam", param);
        return mav;
    }

}

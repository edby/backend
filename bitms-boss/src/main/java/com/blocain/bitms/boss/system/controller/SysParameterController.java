/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.system.controller;

import com.blocain.bitms.tools.consts.BitmsConst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;

import com.blocain.bitms.boss.common.entity.SysParameter;
import com.blocain.bitms.boss.common.service.SysParameterService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;

/**
 * 系统参数表 控制器
 * <p>File：SysParameterController.java </p>
 * <p>Title: SysParameterController </p>
 * <p>Description:SysParameterController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.SYSTEM)
@Api(description = "系统参数表")
public class SysParameterController extends GenericController
{
    @Autowired(required = false)
    private SysParameterService sysParameterService;
    
    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/params")
    @RequiresPermissions("system:setting:sysparameter:index")
    public String list() throws BusinessException
    {
        return "boss/system/params/list";
    }
    
    @RequestMapping(value = "/params/modify")
    @RequiresPermissions("system:setting:sysparameter:operator")
    public ModelAndView modify(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("boss/system/params/modify");
        SysParameter sysParameter = new SysParameter();
        if (null != id)
        {
            sysParameter = sysParameterService.selectByPrimaryKey(id);
        }
        mav.addObject("sysParameter", sysParameter);
        return mav;
    }
    
    /**
     * 操作系统参数表
     * <p>
     *     @RequestBody 此注解加入后接收的参数将是JSON字符串，若相应模块中需要以FORM表单方式提交，
     *     请将此注册取消掉;加入此注解是的目的是为了统一数据交互方式，实现真正的前后端分离。
     * </p>
     * @param info
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/params/save", method = RequestMethod.POST)
    @ApiOperation(value = "保存系统参数表", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermissions("system:setting:sysparameter:operator")
    public JsonMessage save(@ModelAttribute SysParameter info) throws BusinessException
    {
        String value = info.getValue();
        if (value == null) value = "";
        value = value.replaceAll(",", ";");
        SysParameter sysParameter = new SysParameter();
        sysParameter.setId(info.getId());
        sysParameter.setValue(value);
        sysParameter.setRemark(info.getRemark());
        sysParameterService.updateByPrimaryKeySelective(sysParameter);
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 查询系统参数表
     * <p>
     *     @RequestBody 此注解加入后接收的参数将是JSON字符串，加入此注解是的目的是为了统一数据交互方式，实现真正的前后端分离。
     *     若相应模块中需要以FORM表单方式提交，请将此注册取消掉;
     * </p>
     * @param entity
     * @param pagin
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/params/data", method = RequestMethod.POST)
    @ApiOperation(value = "查询系统参数表", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermissions("system:setting:sysparameter:data")
    public JsonMessage data(@ModelAttribute SysParameter entity, @ModelAttribute Pagination pagin) throws BusinessException
    {
        PaginateResult<SysParameter> result = sysParameterService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
}

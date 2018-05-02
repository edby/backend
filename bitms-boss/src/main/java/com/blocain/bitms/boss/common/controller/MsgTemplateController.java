/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.common.controller;

import com.blocain.bitms.tools.consts.BitmsConst;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.blocain.bitms.boss.common.entity.MsgTemplate;
import com.blocain.bitms.boss.common.service.MsgTemplateService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.CalendarUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * 消息模版 控制器
 * <p>File：MsgTemplateController.java </p>
 * <p>Title: MsgTemplateController </p>
 * <p>Description:MsgTemplateController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@Api(description = "消息模版")
@RequestMapping(BitmsConst.COMMON)
public class MsgTemplateController extends GenericController
{
    @Autowired(required = false)
    private MsgTemplateService msgTemplateService;
    
    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/msgTemplate")
    @ApiOperation(value = "列表页面导航", httpMethod = "GET")
    @RequiresPermissions("system:setting:msgtemplate:index")
    public String index() throws BusinessException
    {
        return "boss/common/template/list";
    }
    
    /**
     * 添加或修改 消息模版
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/msgTemplate/modify")
    @RequiresPermissions("system:setting:msgtemplate::operator")
    public ModelAndView modify(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("boss/common/template/modify");
        MsgTemplate template = new MsgTemplate();
        if (null != id)
        {
            template = msgTemplateService.selectByPrimaryKey(id);
        }
        mav.addObject("template", template);
        return mav;
    }
    
    /**
     * 操作消息模版
     * <p>
     *     @RequestBody 此注解加入后接收的参数将是JSON字符串，若相应模块中需要以FORM表单方式提交，
     *     请将此注册取消掉;加入此注解是的目的是为了统一数据交互方式，实现真正的前后端分离。
     * </p>
     * @param info
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/msgTemplate/save", method = RequestMethod.POST)
    @ApiOperation(value = "保存消息模版", httpMethod = "POST")
    @RequiresPermissions("system:setting:msgtemplate:operator")
    public JsonMessage save(MsgTemplate info) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == info.getId())
        {
            info.setCreateBy(principal.getId());
            info.setCreateDate(CalendarUtils.getCurrentLong());
        }
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        if (beanValidator(json, info))
        {
            msgTemplateService.save(info);
        }
        return json;
    }
    
    /**
     * 查询消息模版
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/msgTemplate/data", method = RequestMethod.POST)
    @ApiOperation(value = "查询消息模版", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermissions("system:setting:msgtemplate:data")
    public JsonMessage data(@ModelAttribute MsgTemplate entity, @ModelAttribute Pagination pagin) throws BusinessException
    {
        PaginateResult<MsgTemplate> result = msgTemplateService.search(pagin, entity);
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
    @RequestMapping(value = "/msgTemplate/del", method = RequestMethod.POST)
    @ApiOperation(value = "根据指定ID删除", httpMethod = "POST")
    @ApiImplicitParam(name = "ids", value = "以','分割的编号组", paramType = "form")
    @RequiresPermissions("system:setting:msgtemplate:operator")
    public JsonMessage del(String ids) throws BusinessException
    {
        msgTemplateService.deleteBatch(ids.split(","));
        return getJsonMessage(CommonEnums.SUCCESS);
    }
}

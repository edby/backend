/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.common.controller;

import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.consts.BitmsConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.account.entity.Message;
import com.blocain.bitms.trade.account.service.MessageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.servlet.ModelAndView;

/**
 * 消息表 控制器
 * <p>File：MessageController.java </p>
 * <p>Title: MessageController </p>
 * <p>Description:MessageController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@Api(description = "消息表")
@RequestMapping(BitmsConst.COMMON)
public class MessageController extends GenericController
{
    @Autowired(required = false)
    private MessageService messageService;

    /**
     * 我的消息页面导航
     * @return {@link String}
     * @throws BusinessException
     */
    @RequestMapping("/message")
    @ApiOperation(value = "我的消息页面导航", httpMethod = "GET")
    public ModelAndView message() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("account/message");
        return mav;
    }

    /**
     * 查询用户自己的消息列表
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/message/data", method = RequestMethod.POST)
    @ApiOperation(value = "查询消息表", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage data(@ModelAttribute Message entity, @ModelAttribute Pagination pagin) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        entity.setAccountId(principal.getId());// 将当前操作人员的ID加入到参数对象中
        PaginateResult<Message> result = messageService.searchByAccount(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 删除用户自己的消息
     * @param ids
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/message/del", method = RequestMethod.POST)
    @ApiOperation(value = "根据指定ID删除", httpMethod = "POST")
    @ApiImplicitParam(name = "ids", value = "以','分割的编号组", paramType = "form")
    public JsonMessage del(String ids) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        messageService.deleteAccountMessageById(principal.getId(), ids.split(","));
        return getJsonMessage(CommonEnums.SUCCESS);
    }
}

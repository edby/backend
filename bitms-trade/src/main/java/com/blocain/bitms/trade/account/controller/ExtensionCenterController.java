/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.controller;

import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.account.entity.AccountInvitation;
import com.blocain.bitms.trade.account.service.AccountInvitationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *  用户推广中心  控制器
 * <p>File：ExtensionCenterController.java</p>
 * <p>Title: ExtensionCenterController</p>
 * <p>Description:ExtensionCenterController</p>
 * <p>Copyright: Copyright (c) 2017年7月14日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.ACCOUNT)
@Api(description = "用户推广中心")
public class ExtensionCenterController extends GenericController
{
    @Autowired(required = false)
    private AccountInvitationService accountInvitationService;
    
    /**
     * 用户推广中心页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/extensionCenter", method = RequestMethod.GET)
    @ApiOperation(value = "用户推广中心页面导航", httpMethod = "GET")
    public ModelAndView index() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("account/extensionCenter");
        mav.addObject("account", OnLineUserUtils.getPrincipal());
        return mav;
    }
    
    /**
     * 用户推广中心数据查询
     * @param entity
     * @param pagin
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/extensionCenter/data", method = RequestMethod.POST)
    @ApiOperation(value = "用户推广中心数据查询", httpMethod = "POST")
    public JsonMessage data(@ModelAttribute AccountInvitation entity, @ModelAttribute Pagination pagin) throws BusinessException
    {
        // 限定邀请人为自已的UNID
        entity.setInvitCode(OnLineUserUtils.getUnid());
        PaginateResult<AccountInvitation> data = accountInvitationService.search(pagin, entity);
        return this.getJsonMessage(CommonEnums.SUCCESS, data);
    }
}

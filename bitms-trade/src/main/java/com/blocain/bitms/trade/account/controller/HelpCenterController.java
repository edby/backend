/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.exception.BusinessException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *  用户帮助中心  控制器
 * <p>File：HelpCenterController.java</p>
 * <p>Title: HelpCenterController</p>
 * <p>Description:HelpCenterController</p>
 * <p>Copyright: Copyright (c) 2017年7月14日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping("/account/helpCenter")
@Api(description = "用户帮助中心")
public class HelpCenterController extends GenericController
{
    
    /**
     * 用户帮助中心页面导航
     * @return
     * @throws BusinessException
     */
	@RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "用户帮助中心页面导航", httpMethod = "GET")
    public ModelAndView helpCenter() throws BusinessException
    {
    	 ModelAndView mav = new ModelAndView("account/helpCenter");
         return mav;
    }
    
}

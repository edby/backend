/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.common.controller;

import javax.servlet.http.HttpServletRequest;

import com.blocain.bitms.tools.consts.BitmsConst;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.blocain.bitms.tools.utils.LanguageUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.entity.Notice;
import com.blocain.bitms.trade.account.service.NoticeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.servlet.ModelAndView;


/**
 * 消息公告表 控制器
 * <p>File：NoticeController.java </p>
 * <p>Title: NoticeController </p>
 * <p>Description:NoticeController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.COMMON)
@Api(description = "消息公告")
public class NoticeController extends GenericController
{
    @Autowired(required = false)
    private NoticeService noticeService;
    
    /**
     * 公告页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping("/notice")
    @ApiOperation(value = "公告页面导航", httpMethod = "GET")
    public String notice() throws BusinessException
    {
        return "account/notice";
    }
    
    /**
     * 查询消息公告
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/notice/data", method = RequestMethod.POST)
    @ApiOperation(value = "查询消息公告", httpMethod = "POST")
    public JsonMessage data(HttpServletRequest request, @ModelAttribute Notice entity, @ModelAttribute Pagination pagin) throws BusinessException
    {
        entity.setStatus(true);// 只取已发布的公告
        if (StringUtils.isBlank(entity.getLangType()))
        {// 设置语言类型
            entity.setLangType(LanguageUtils.getLang(request));
        }
        PaginateResult<?> result = noticeService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    /**
     * 根据指定ID获取一条公告
     * @param id
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/notice/getNotice", method = RequestMethod.GET)
    @ApiOperation(value = "根据指定ID获取公告", httpMethod = "GET")
    public ModelAndView getNotice(Long id) throws BusinessException
    {
        Notice result = noticeService.selectByPrimaryKey(id);
        ModelAndView mav = new ModelAndView("/account/noticeInfo");
        mav.addObject("notice", result);
        return mav;
    }
}

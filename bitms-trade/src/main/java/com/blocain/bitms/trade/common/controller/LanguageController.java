package com.blocain.bitms.trade.common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.service.AccountService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * 国际化语言切换 介绍
 * <p>File：LanguageController.java </p>
 * <p>Title: LanguageController </p>
 * <p>Description:LanguageController </p>
 * <p>Copyright: Copyright (c) 2017/7/20 </p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.COMMON)
@Api(description = "国际化语言切换")
public class LanguageController extends GenericController
{
    // 帐户服务
    @Autowired(required = false)
    private AccountService accountService;
    
    /**
     * 切换语言
     * @param request
     * @param response
     * @param locale
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @ApiOperation(value = "切换语言", httpMethod = "GET")
    @RequestMapping(value = "/changeLang", method = RequestMethod.GET)
    @ApiImplicitParam(name = "locale", value = "语言编码（en_US,zh_CN,zh_HK)", required = true, paramType = "form")
    public JsonMessage changeLanguage(HttpServletRequest request, HttpServletResponse response, String locale) throws BusinessException
    {
        if (locale != null)
        {
            if (localeResolver == null) { throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?"); }
            localeResolver.setLocale(request, response, StringUtils.parseLocaleString(locale));
            UserPrincipal principal = OnLineUserUtils.getPrincipal();
            if (null != principal)
            {// 已登陆的情况下把选择的语言编码存入数据中
                Account account = accountService.selectByPrimaryKey(principal.getId());
                account.setLang(locale);
                accountService.updateByPrimaryKey(account);
            }
        }
        return this.getJsonMessage(CommonEnums.SUCCESS);
    }
}

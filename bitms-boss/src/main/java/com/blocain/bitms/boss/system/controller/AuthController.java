package com.blocain.bitms.boss.system.controller;

import javax.servlet.http.HttpServletRequest;

import com.blocain.bitms.boss.common.consts.MessageConst;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.RedisSessionManager;
import com.blocain.bitms.security.exception.AccountPolicyException;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.consts.DateConst;
import com.blocain.bitms.tools.utils.*;
import com.blocain.bitms.trade.account.model.PolicyModel;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.shiro.model.AccountToken;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.AliyunModel;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import org.springframework.web.servlet.ModelAndView;

/**
 * LoginController Introduce
 * <p>File：LoginController.java </p>
 * <p>Title: LoginController </p>
 * <p>Description:LoginController </p>
 * <p>Copyright: Copyright (c) 17/6/21</p>
 * <p>Company: blocain.com</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Controller
public class AuthController extends GenericController
{
    /**
     * 登陆页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping("/login")
    public String login() throws BusinessException
    {
        if (SecurityUtils.getSubject().isAuthenticated())
        {// 如果登陆过就直接进入后台
            return "redirect:/dispatch";
        }
        return "login";
    }
    
    /**
     * 用户登陆认证
     * @param request
     * @param token
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping("/login/submit")
    public JsonMessage submit(HttpServletRequest request, @ModelAttribute AccountToken token, @ModelAttribute AliyunModel model)
    {
        if (SecurityUtils.getSubject().isAuthenticated())
        {// 如果登陆过就直接进入后台
            return this.getJsonMessage(CommonEnums.SUCCESS);
        }
        if (!AliyunUtils.validParams(model))
        {// 验证不通过时
            return this.getJsonMessage(CommonEnums.ERROR_LOGIN_CAPTCHA, Boolean.TRUE);
        }
        token.setHost(IPUtil.getOriginalIpAddr(request));
        Subject subject = SecurityUtils.getSubject();
        try
        {
            subject.login(token);
        }
        catch (IncorrectCredentialsException ice)
        {
            return this.getJsonMessage(CommonEnums.ERROR_LOGIN_PASSWORD);
        }
        catch (UnknownAccountException uae)
        {
            return this.getJsonMessage(CommonEnums.ERROR_LOGIN_ACCOUNT);
        }
        catch (ExcessiveAttemptsException eae)
        {
            return this.getJsonMessage(CommonEnums.ERROR_LOGIN_TIMEOUT);
        }
        catch (AccountPolicyException gae)
        {
            StringBuffer key = new StringBuffer(CacheConst.LOGIN_PERFIX).append(subject.getSession().getId());
            RedisUtils.putObject(key.toString(), token, CacheConst.DEFAULT_CACHE_TIME);
            return this.getJsonMessage(CommonEnums.NEED_POLICY_CHECK);
        }
        return this.getJsonMessage(CommonEnums.SUCCESS);
    }

    /**
     * 登陆页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping("/login/check")
    @ApiOperation(value = "登陆导航", httpMethod = "GET")
    public ModelAndView check() throws BusinessException
    {
        Subject subject = SecurityUtils.getSubject();
        StringBuffer key = new StringBuffer(CacheConst.LOGIN_PERFIX).append(subject.getSession().getId());
        AccountToken token = (AccountToken) RedisUtils.getObject(key.toString());
        if (null == token) return new ModelAndView("redirect:/login");
        ModelAndView mav = new ModelAndView("check");
        mav.addObject("token", token);
        return mav;
    }

    /**
     * 用户登陆认证
     * @param request
     * @param gaCode
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping("/login/check/submit")
    @ApiOperation(value = "登陆", httpMethod = "POST")
    public JsonMessage checkSubmit(HttpServletRequest request, String gaCode) throws BusinessException
    {
        Subject subject = SecurityUtils.getSubject();
        StringBuffer key = new StringBuffer(CacheConst.LOGIN_PERFIX).append(subject.getSession().getId());
        AccountToken token = (AccountToken) RedisUtils.getObject(key.toString());
        if (null == token) return this.getJsonMessage(CommonEnums.ERROR_SESSION_TIME_OUT);
        if (null == gaCode) return this.getJsonMessage(CommonEnums.NEED_POLICY_CHECK);
        token.setGaCode(gaCode);
        token.setHost(IPUtil.getOriginalIpAddr(request));
        try
        {
            subject.login(token);
        }
        catch (AccountPolicyException gae)
        {
            return this.getJsonMessage(CommonEnums.ERROR_AUTHER_FAILED);
        }
        RedisUtils.del(key.toString());
        return this.getJsonMessage(CommonEnums.SUCCESS);
    }

    /**
     * 退出认证
     * @return {@link String}
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/logout")
    public JsonMessage logout(HttpServletRequest request) throws Exception
    {
        Subject subject = SecurityUtils.getSubject();
        if (null != subject)
        {
            subject.logout();
        }
        return this.getJsonMessage(CommonEnums.SUCCESS);
    }
}

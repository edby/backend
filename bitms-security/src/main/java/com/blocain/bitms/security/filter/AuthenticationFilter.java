package com.blocain.bitms.security.filter;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.consts.CharsetConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.utils.StringUtils;

/**
 * <p>File：AuthenticationFilter.java </p>
 * <p>Title: 表单验证（包含验证码）过滤类 </p>
 * <p>Description: AuthenticationFilter </p>
 * <p>Copyright: Copyright (c) 2014 08/08/2015 09:52</p>
 * <p>Company: BloCain</p>
 *
 * @author playguy
 * @version 1.0
 */
public class AuthenticationFilter extends FormAuthenticationFilter
{
    private String captchaParam = "validateCode";
    
    private String messageParam = "message";
    
    public AuthenticationFilter()
    {
        super();
    }
    
    public String getCaptchaParam()
    {
        return captchaParam;
    }
    
    protected String getCaptcha(ServletRequest request)
    {
        return WebUtils.getCleanParam(request, getCaptchaParam());
    }
    
    public String getMessageParam()
    {
        return messageParam;
    }
    
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception
    {
        if (isLoginRequest(request, response))
        { // 登陆状态下
            if (isLoginSubmission(request, response))
            {
                return executeLogin(request, response);
            }
            else
            {
                return true;
            }
        }
        else
        { // 未登陆状态
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            if ("XMLHttpRequest".equalsIgnoreCase(httpRequest.getHeader("X-Requested-With")))
            {// ajax请求
                httpResponse.setHeader("Content-type", "application/json;charset=UTF-8");
                httpResponse.setCharacterEncoding(CharsetConst.CHARSET_UT);
                JsonMessage message = getJsonMessage(CommonEnums.ERROR_LOGIN_TIMEOUT);
                httpResponse.setHeader("sessionstatus", "timeout");
                PrintWriter outPrintWriter = httpResponse.getWriter();
                outPrintWriter.println(JSON.toJSON(message));
                outPrintWriter.flush();
                outPrintWriter.close();
                return false;
            }
            else
            {// http 请求
                saveRequestAndRedirectToLogin(request, response);
                return false;
            }
        }
    }
    
    /**
     * 登录失败调用事件
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response)
    {
        String className = e.getClass().getName(), message = "";
        if (IncorrectCredentialsException.class.getName().equals(className) || UnknownAccountException.class.getName().equals(className))
        {
            message = "用户或密码错误, 请重试.";
        }
        else if (e.getMessage() != null && StringUtils.startsWith(e.getMessage(), "msg:"))
        {
            message = StringUtils.replace(e.getMessage(), "msg:", "");
        }
        else
        {
            message = "系统出现点问题，请稍后再试！";
        }
        request.setAttribute(getFailureKeyAttribute(), className);
        request.setAttribute(getMessageParam(), message);
        return true;
    }
    
    /**
     * 封装JSON对象
     * @param describable
     * @return
     */
    JsonMessage getJsonMessage(CommonEnums describable)
    {
        JsonMessage jsonMessage = new JsonMessage();
        jsonMessage.setCode(describable.getCode());
        jsonMessage.setMessage(describable.getMessage());
        return jsonMessage;
    }
}
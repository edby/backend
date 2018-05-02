package com.blocain.bitms.apps.basic.interceptor;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.blocain.bitms.apps.basic.consts.AppsContsts;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.blocain.bitms.apps.sdk.internal.util.Signature;
import com.google.common.collect.Maps;

/**
 * APP签名拦截器 Introduce
 * <p>Title: SignHandlerInterceptor</p>
 * <p>File：SignHandlerInterceptor.java</p>
 * <p>Description: SignHandlerInterceptor</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Deprecated
public class SignHandlerInterceptor extends HandlerInterceptorAdapter
{
    /**
     * 进入业务之前需要先签名
     * @param request
     * @param response
     * @param handler
     * @return {@link Boolean}
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        Map<String, String> params = Maps.newHashMap();
        Enumeration<String> parameterNames = request.getParameterNames();
        String parameterName;
        while (parameterNames.hasMoreElements())
        {
            parameterName = parameterNames.nextElement();
            params.put(parameterName, request.getParameter(parameterName));
        }
        return Signature.checkSign(params, AppsContsts.PUBLIC_KEY);
    }
}

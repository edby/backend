/*
 * @(#)AdminInterceptor.java 2017年7月21日 上午11:27:15
 * Copyright 2017 施建波, Inc. All rights reserved. BloCain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.bitpay.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * <p>File：AdminInterceptor.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月21日 上午11:27:15</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
public class AdminInterceptor extends HandlerInterceptorAdapter
{
    public static final Logger logger = LoggerFactory.getLogger(AdminInterceptor.class);
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException
    {
        Object obj = request.getSession().getAttribute(ApplicationConst.SESSION_MEMBER_KEY);
        if(null == obj) {
            response.sendRedirect("/common/login");
            return false;
        }
        return true;
    }
}

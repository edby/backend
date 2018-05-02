/*
 * @(#)DecorateHeaderDao 2014/3/22 10:52
 * Copyright 2014 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.security.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.security.RedisSessionManager;
import com.blocain.bitms.security.utils.IdGen;
import com.blocain.bitms.tools.consts.BitmsConst;

/**
 * <p>File：CSRFDataValueProcessor</p>
 * <p>Title: </p>
 * <p>Description: csrf token 生成工具 </p>
 * <p>Copyright: Copyright (c) 2014 2014/3/22 10:52</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public final class CSRFTokenManager
{
    private static final Logger LOGGER          = Logger.getLogger(CSRFTokenManager.class);
    
    public static final String  CSRF_TOKEN_NAME = "csrf";
    
    public static final String  CSRF_FORM_ID    = "formId";
    
    private RedisSessionManager redisSessionManager;
    
    public void setRedisSessionManager(RedisSessionManager redisSessionManager)
    {
        this.redisSessionManager = redisSessionManager;
    }
    
    public String getTokenForRequest(HttpServletRequest request, String formId)
    {
        String token;
        synchronized (request)
        {
            token = redisSessionManager.getString(request, RedisSessionManager.SessionKey.CSRF_KEY, formId);
            if (StringUtils.isNotBlank(token))
            {// 更新会话时间
                redisSessionManager.expire(request, RedisSessionManager.SessionKey.CSRF_KEY, formId);
            }
            else
            {
                StringBuffer buffer = new StringBuffer(formId);
                buffer.append(BitmsConst.SEPARATOR).append(IdGen.uuid());
                token = EncryptUtils.desEncrypt(buffer.toString());
                redisSessionManager.put(request, RedisSessionManager.SessionKey.CSRF_KEY, formId, token, RedisSessionManager.defaultExpireSeconds);
            }
        }
        return token;
    }
    
    /**
     * 移出CSRF TOKEN_NAME
     * @param request
     */
    public void removeTokenForRequest(HttpServletRequest request, String key)
    {
        redisSessionManager.remove(request, RedisSessionManager.SessionKey.CSRF_KEY, key);
    }
    
    /**
     * 取CSRF口令，优先从hearder中取，然后再从请求参数中取
     * 
     * @param request
     * @return
     */
    public static String getTokenFromRequest(HttpServletRequest request)
    {
        String token = request.getParameter(CSRF_TOKEN_NAME);
        if (StringUtils.isBlank(token)) token = request.getHeader(CSRF_TOKEN_NAME);
        return token;
    }
    
    /**
     * 取CSRF口令，优先从hearder中取，然后再从请求参数中取
     *
     * @param request
     * @return
     */
    public static void removeTokenFromRequest(HttpServletRequest request)
    {
        request.removeAttribute(CSRF_TOKEN_NAME);
    }
    
    /**
     * 取表单标识
     *
     * @param request
     * @return
     */
    public static String getFormIDFromRequest(HttpServletRequest request)
    {
        String token = request.getParameter(CSRF_FORM_ID);
        if (StringUtils.isBlank(token)) token = request.getHeader(CSRF_FORM_ID);
        return token;
    }
}

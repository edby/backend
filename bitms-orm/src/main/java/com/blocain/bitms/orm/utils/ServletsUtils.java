package com.blocain.bitms.orm.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Servlets 辅助工具
 * <p>File：Servlets.java </p>
 * <p>Title: Servlets </p>
 * <p>Description:Servlets </p>
 * <p>Copyright: Copyright (c) May 26, 2017 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class ServletsUtils
{
    /**
     * 获取当前请求对象
     * @return
     */
    public static HttpServletRequest getRequest()
    {
        try
        {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    /**
     * 获取当前请求对象
     * @return
     */
    public static HttpServletResponse getResponse()
    {
        try
        {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        }
        catch (Exception e)
        {
            return null;
        }
    }
}

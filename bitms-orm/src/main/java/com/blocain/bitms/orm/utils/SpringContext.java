/*
 * @(#)SpringUtils.java 2015-4-16 下午3:25:32
 * Copyright 2015 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.orm.utils;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.blocain.bitms.tools.utils.LoggerUtils;

/**
 * <p>File：SpringUtils.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2015 2015-4-16 下午3:25:32</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class SpringContext implements ApplicationContextAware, DisposableBean
{
    private static final Logger logger = LoggerFactory.getLogger(SpringContext.class);
    
    private SpringContext()
    {
    }
    
    private static ApplicationContext applicationContext = null;
    
    /**
     * 取得存储在静态变量中的ApplicationContext.
     */
    public static ApplicationContext getApplicationContext()
    {
        assertContextInjected();
        return applicationContext;
    }
    
    @Override
    public void destroy() throws Exception
    {
        SpringContext.clearHolder();
    }
    
    /**
     * 根据类型名称取得bean
     * @param name 类型名称
     * @return T basic
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name)
    {
        assertContextInjected();
        return (T) applicationContext.getBean(name);
    }
    
    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     * @param requiredType 类型
     * @return T basic
     */
    public static <T> T getBean(Class<T> requiredType)
    {
        assertContextInjected();
        return applicationContext.getBean(requiredType);
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
    {
        logger.debug("注入ApplicationContext到SpringContextHolder:{}", applicationContext);
        if (SpringContext.applicationContext != null)
        {
            LoggerUtils.logWarn(logger, "SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:" + SpringContext.applicationContext);
        }
        SpringContext.applicationContext = applicationContext; // NOSONAR
    }
    
    /**
     * 清除SpringContextHolder中的ApplicationContext为Null.
     */
    public static void clearHolder()
    {
        LoggerUtils.logDebug(logger, "清除SpringContextHolder中的ApplicationContext:" + applicationContext);
        applicationContext = null;
    }
    
    /**
     * 检查ApplicationContext不为空.
     */
    private static void assertContextInjected()
    {
        Validate.validState(applicationContext != null, "applicaitonContext属性未注入, 请在applicationContext.xml中定义SpringContextHolder.");
    }
}

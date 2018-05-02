/*
 * @(#)AbstractQuartzBean.java 11:11:35 AM
 * 
 * Copyright 2010 Lzj.Liu, Inc. All rights reserved. Fortune
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.task.basic;

import java.io.FileNotFoundException;
import java.io.Serializable;

import javax.servlet.ServletContext;

import com.blocain.bitms.tools.utils.LoggerUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.WebUtils;

/**
 * <p>Title: AbstractQuartzBean.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2016-4-12 14:51</p>
 * <p>Company: Co.,Ltd</p>
 * @author Playguys
 * @version 1.0
 */
public abstract class AbstractQuartzBean extends QuartzJobBean implements Serializable
{
    private static final long  serialVersionUID        = -141700316829390355L;
    
    public static final Logger logger                  = LoggerFactory.getLogger(AbstractQuartzBean.class);
    
    /**
     * Quartz取当前路径时的常量
     */
    public static final String APPLICATION_CONTEXT_KEY = "applicationContext";
    
    /**
     * 获取ServletContext
     * @param jobexecutioncontext JobExecutionContext
     * @return ServletContext ServletContext
     * @throws SchedulerException SchedulerException
     */
    protected ServletContext getServletContext(JobExecutionContext jobexecutioncontext) throws SchedulerException
    {
        ApplicationContext appCtx = (ApplicationContext) jobexecutioncontext.getScheduler().getContext().get(APPLICATION_CONTEXT_KEY);
        WebApplicationContext webCtx = null;
        ServletContext srvCtx = null;
        if (appCtx instanceof WebApplicationContext)
        {
            webCtx = (WebApplicationContext) appCtx;
            srvCtx = webCtx.getServletContext();
        }
        if (appCtx == null)
        {
            LoggerUtils.logError(logger, "获取ApplicationContext异常,applicationContext 变量未注入");
            throw new JobExecutionException("No application context available in scheduler context for key \"" + APPLICATION_CONTEXT_KEY + "\"");
        }
        return srvCtx;
    }
    
    /**
     * 取得ApplicationContext对象，须在spring配置中注入
     * @param jobexecutioncontext JobExecutionContext
     * @return ApplicationContext ApplicationContext
     * @throws SchedulerException SchedulerException
     */
    protected ApplicationContext getApplicationContext(JobExecutionContext jobexecutioncontext) throws SchedulerException
    {
        ApplicationContext appCtx = (ApplicationContext) jobexecutioncontext.getScheduler().getContext().get(APPLICATION_CONTEXT_KEY);
        return appCtx;
    }
    
    /**
     * 在调度中取得指定的应用程序目录
     * @param jobExecutionContext JobExecutionContext
     * @param path 指定的应用程序目录
     * @return String 指定的应用程序目录
     */
    protected String getRootPath(JobExecutionContext jobExecutionContext, String path)
    {
        String rootPath = null;
        ServletContext servletContext = null;
        try
        {
            servletContext = this.getServletContext(jobExecutionContext);
        }
        catch (SchedulerException e)
        {
            LoggerUtils.logError(logger, "getRootPath 方法异常:", e);
        }
        if (null != servletContext) try
        {
            rootPath = WebUtils.getRealPath(servletContext, path);
        }
        catch (FileNotFoundException e)
        {
            LoggerUtils.logError(logger, "getRootPath 方法FileNotFoundException异常:", e);
        }
        return rootPath;
    }

    @Override
    protected abstract void executeInternal(JobExecutionContext arg0) throws JobExecutionException;
}

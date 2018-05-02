package com.blocain.bitms.task.demo;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blocain.bitms.tools.utils.LoggerUtils;

/**
 * TestTask Introduce
 * <p>File：TestTask.java</p>
 * <p>Title: TestTask</p>
 * <p>Description: TestTask</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class TestTask
{
    public static final Logger logger = LoggerFactory.getLogger(TestTask.class);
    
    // private AccountService accountService = SpringContext.getBean(AccountService.class);
    public void task() throws JobExecutionException
    {
        LoggerUtils.logInfo(logger, "开始测试任务");
        try
        {
            // accountService.modifyAccountByTask();
            LoggerUtils.logInfo(logger, "开始执行任务");
            Thread.sleep(5000);// 每次休眠5秒
        }
        catch (Exception e)
        {
            LoggerUtils.logError(logger, "测试任务失败：{}", e.getLocalizedMessage());
        }
        LoggerUtils.logInfo(logger, "结束测试任务");
    }
}

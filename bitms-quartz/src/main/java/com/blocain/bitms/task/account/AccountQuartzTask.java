package com.blocain.bitms.task.account;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

import com.blocain.bitms.task.basic.AbstractQuartzBean;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.trade.account.service.AccountService;

/**
 * 账户解冻任务 Introduce
 * <p>File：AccountQuartzTask.java</p>
 * <p>Title: AccountQuartzTask</p>
 * <p>Description: AccountQuartzTask</p>
 * <p>Copyright: Copyright (c) 2017/7/7</p>
 * <p>Company: BloCain</p>
 *
 * @author sunbiao
 * @version 1.0
 */
public class AccountQuartzTask extends AbstractQuartzBean
{ 
	private static final long serialVersionUID = -2684286662908854480L;
	private AccountService accountService;
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException
    {
        LoggerUtils.logInfo(logger, "开始帐户解冻轮询任务");
        try
        {
            initialize(getApplicationContext(context));
            accountService.modifyAccountByTask();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LoggerUtils.logError(logger, "帐户解冻任务失败：{}", e.getLocalizedMessage());
        }
        LoggerUtils.logInfo(logger, "结束帐户解冻轮询任务");
    }
    
    /**
     * 初始化服务
     * @param applicationContext
     */
    protected void initialize(ApplicationContext applicationContext)
    {
        if (null == accountService) accountService = applicationContext.getBean(AccountService.class);
    }
}

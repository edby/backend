package com.blocain.bitms.task.trade;

import com.blocain.bitms.task.basic.AbstractQuartzBean;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.trade.fund.service.AccountDebitAssetService;
import com.blocain.bitms.trade.fund.service.AccountWealthAssetService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

/**
 * 理财资产计息轮询 Introduce
 * <p>File：WealthAssetInterestQuartzTask.java</p>
 * <p>Title: WealthAssetInterestQuartzTask</p>
 * <p>Description: WealthAssetInterestQuartzTask</p>
 * <p>Copyright: Copyright (c) 2017/7/7</p>
 * <p>Company: BloCain</p>
 *
 * @author sunbiao
 * @version 1.0
 */
public class WealthAssetInterestQuartzTask extends AbstractQuartzBean
{
	
	private static final long serialVersionUID = 6818068059579857089L;

	private AccountWealthAssetService accountWealthAssetService;
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException
    {
        LoggerUtils.logInfo(logger, "开始理财资产计息轮询任务");
        try
        {
            // initialize(getApplicationContext(context));
            // 自动理财计息
            // accountWealthAssetService.autoAccountWealthAssetInterest();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LoggerUtils.logError(logger, "理财资产计息轮询任务失败：{}", e.getLocalizedMessage());
        }
        LoggerUtils.logInfo(logger, "结束理财资产计息轮询任务");
    }
    
    /**
     * 初始化服务
     * @param applicationContext
     */
    protected void initialize(ApplicationContext applicationContext)
    {
        if (null == accountWealthAssetService) accountWealthAssetService = applicationContext.getBean(AccountWealthAssetService.class);
    }
}

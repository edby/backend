package com.blocain.bitms.task.trade;

import com.blocain.bitms.task.basic.AbstractQuartzBean;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.trade.fund.service.AccountDebitAssetService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

/**
 * 借贷资产计息轮询 Introduce
 * <p>File：DebitAssetInterestQuartzTask.java</p>
 * <p>Title: DebitAssetInterestQuartzTask</p>
 * <p>Description: DebitAssetInterestQuartzTask</p>
 * <p>Copyright: Copyright (c) 2017/7/7</p>
 * <p>Company: BloCain</p>
 *
 * @author sunbiao
 * @version 1.0
 */
public class DebitAssetInterestQuartzTask extends AbstractQuartzBean
{
	
	private static final long serialVersionUID = 6818068059579857089L;

	private AccountDebitAssetService accountDebitAssetService;
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException
    {
        LoggerUtils.logInfo(logger, "开始借贷资产计息轮询任务");
        try
        {
            // initialize(getApplicationContext(context));
            // 自动借贷计息
            // accountDebitAssetService.autoAccountDebitAssetInterest();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LoggerUtils.logError(logger, "借贷资产计息轮询任务失败：{}", e.getLocalizedMessage());
        }
        LoggerUtils.logInfo(logger, "结束借贷资产计息轮询任务");
    }
    
    /**
     * 初始化服务
     * @param applicationContext
     */
    protected void initialize(ApplicationContext applicationContext)
    {
        if (null == accountDebitAssetService) accountDebitAssetService = applicationContext.getBean(AccountDebitAssetService.class);
    }
}

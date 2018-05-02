package com.blocain.bitms.task.fund;

import com.blocain.bitms.task.basic.AbstractQuartzBean;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.trade.fund.service.AccountCandyRecordService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

/**
 * 定时轮询 糖果奖励（USD交易奖励BIEX）
 * <p>File：AccountCandyQuartzTask.java</p>
 * <p>Title: AccountCandyQuartzTask</p>
 * <p>Description: AccountCandyQuartzTask</p>
 * <p>Copyright: Copyright (c) 2018-03-15</p>
 * <p>Company: BloCain</p>
 *
 * @author zhangchunxi
 * @version 1.0
 */
public class AccountCandyQuartzTask extends AbstractQuartzBean
{
	private static final long serialVersionUID = 9031747787993971408L;

	private AccountCandyRecordService accountCandyRecordService;
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException
    {
        LoggerUtils.logInfo(logger, "开始定时轮询糖果奖励的交易状态");
        try
        {
            // initialize(getApplicationContext(context));
            // 糖果奖励
            // accountCandyRecordService.autoTradeAward();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LoggerUtils.logError(logger, "定时轮询糖果奖励的交易状态失败：{}", e.getLocalizedMessage());
        }
        LoggerUtils.logInfo(logger, "结束定时轮询糖果奖励的交易状态");
    }
    
    /**
     * 初始化服务
     * @param applicationContext
     */
    protected void initialize(ApplicationContext applicationContext)
    {
        if (null == accountCandyRecordService) accountCandyRecordService = applicationContext.getBean(AccountCandyRecordService.class);
    }
}

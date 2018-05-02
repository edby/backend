package com.blocain.bitms.task.fund;

import com.blocain.bitms.trade.fund.service.SheetBalanceService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import com.blocain.bitms.task.basic.AbstractQuartzBean;
import com.blocain.bitms.tools.utils.LoggerUtils;

/**
 * 营收统计(资产负债统计)轮询 Introduce
 * <p>File：DebitAssetInterestQuartzTask.java</p>
 * <p>Title: DebitAssetInterestQuartzTask</p>
 * <p>Description: DebitAssetInterestQuartzTask</p>
 * <p>Copyright: Copyright (c) 2017/7/7</p>
 * <p>Company: BloCain</p>
 *
 * @author zhangchunxi
 * @version 1.0
 */
public class SheetBalanceQuartzTask extends AbstractQuartzBean
{
	
	private static final long serialVersionUID = 6818068059579857089L;

	private SheetBalanceService sheetBalanceService;
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException
    {
        LoggerUtils.logInfo(logger, "开始营收统计(资产负债统计)轮询任务");
        try
        {
            initialize(getApplicationContext(context));
            // 自动营收统计
            sheetBalanceService.insertPlatCalUserAssetDebitForDay();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LoggerUtils.logError(logger, "营收统计(资产负债统计)任务失败：{}", e.getLocalizedMessage());
        }
        LoggerUtils.logInfo(logger, "结束营收统计(资产负债统计)轮询任务");
    }
    
    /**
     * 初始化服务
     * @param applicationContext
     */
    protected void initialize(ApplicationContext applicationContext)
    {
        if (null == sheetBalanceService) sheetBalanceService = applicationContext.getBean(SheetBalanceService.class);
    }
}

package com.blocain.bitms.task.archive;

import com.blocain.bitms.archive.service.ArchiveService;
import com.blocain.bitms.task.basic.AbstractQuartzBean;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.trade.fund.service.AccountDebitAssetService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

/**
 * 资金流水归档轮询 Introduce
 * <p>File：FundCurrentArchiveQuartzTask.java</p>
 * <p>Title: FundCurrentArchiveQuartzTask</p>
 * <p>Description: FundCurrentArchiveQuartzTask</p>
 * <p>Copyright: Copyright (c) 2017/7/7</p>
 * <p>Company: BloCain</p>
 *
 * @author Jiangsc
 * @version 1.0
 */
public class FundCurrentArchiveQuartzTask extends AbstractQuartzBean
{
	
	private static final long           serialVersionUID = 0;

    private              ArchiveService archiveService;
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException
    {
        LoggerUtils.logInfo(logger, "开始资金流水归档轮询任务");
        try
        {
            initialize(getApplicationContext(context));
            archiveService.dealFundcurrentArchive();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LoggerUtils.logError(logger, "资金流水归档轮询任务失败：{}", e.getLocalizedMessage());
        }
        LoggerUtils.logInfo(logger, "结束资金流水归档轮询任务");
    }
    
    /**
     * 初始化服务
     * @param applicationContext
     */
    protected void initialize(ApplicationContext applicationContext)
    {
        if (null == archiveService) archiveService = applicationContext.getBean(ArchiveService.class);
    }
}

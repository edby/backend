package com.blocain.bitms.task.archive;

import com.blocain.bitms.archive.service.ArchiveService;
import com.blocain.bitms.task.basic.AbstractQuartzBean;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.trade.fund.service.AccountDebitAssetService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

/**
 * 外部指数行情归档轮询 Introduce
 * <p>File：QuotationArchiveQuartzTask.java</p>
 * <p>Title: QuotationArchiveQuartzTask</p>
 * <p>Description: QuotationArchiveQuartzTask</p>
 * <p>Copyright: Copyright (c) 2017/7/7</p>
 * <p>Company: BloCain</p>
 *
 * @author Jiangsc
 * @version 1.0
 */
public class QuotationArchiveQuartzTask extends AbstractQuartzBean
{
	
	private static final long           serialVersionUID = 0;

	private              ArchiveService archiveService;
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException
    {
        LoggerUtils.logInfo(logger, "开始外部指数行情归档轮询任务");
        try
        {
            initialize(getApplicationContext(context));
            archiveService.dealQuotationArchive();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LoggerUtils.logError(logger, "外部指数行情归档轮询任务失败：{}", e.getLocalizedMessage());
        }
        LoggerUtils.logInfo(logger, "结束外部指数行情归档轮询任务");
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

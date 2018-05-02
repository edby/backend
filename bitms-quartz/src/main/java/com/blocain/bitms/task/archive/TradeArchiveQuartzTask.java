package com.blocain.bitms.task.archive;

import com.blocain.bitms.archive.service.ArchiveService;
import com.blocain.bitms.task.basic.AbstractQuartzBean;
import com.blocain.bitms.tools.utils.LoggerUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

/**
 * 交易相关流水归档轮询 Introduce
 * <p>File：TradeArchiveQuartzTask.java</p>
 * <p>Title: TradeArchiveQuartzTask</p>
 * <p>Description: TradeArchiveQuartzTask</p>
 * <p>Copyright: Copyright (c) 2017/7/7</p>
 * <p>Company: BloCain</p>
 *
 * @author Jiangsc
 * @version 1.0
 */
public class TradeArchiveQuartzTask extends AbstractQuartzBean
{
    private static final long           serialVersionUID = 0;

    private              ArchiveService archiveService;

    @Override protected void executeInternal(JobExecutionContext context) throws JobExecutionException
    {
        LoggerUtils.logInfo(logger, "开始交易相关流水归档轮询任务");
        try
        {
            initialize(getApplicationContext(context));
            archiveService.dealTradeArchive();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LoggerUtils.logError(logger, "交易相关流水归档轮询任务失败：{}", e.getLocalizedMessage());
        }
        LoggerUtils.logInfo(logger, "结束交易相关流水归档轮询任务");
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

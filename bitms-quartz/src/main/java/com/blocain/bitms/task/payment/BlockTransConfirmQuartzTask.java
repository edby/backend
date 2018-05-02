package com.blocain.bitms.task.payment;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

import com.blocain.bitms.task.basic.AbstractQuartzBean;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.trade.fund.service.BlockTransConfirmService;

/**
 * 区块交易确认轮询 Introduce
 * <p>File：DebitAssetInterestQuartzTask.java</p>
 * <p>Title: BlockTransConfirmQuartzTask</p>
 * <p>Description: BlockTransConfirmQuartzTask</p>
 * <p>Copyright: Copyright (c) 2017/7/7</p>
 * <p>Company: BloCain</p>
 *
 * @author sunbiao
 * @version 1.0
 */
public class BlockTransConfirmQuartzTask extends AbstractQuartzBean
{
	
	private static final long serialVersionUID = 6838068059579857089L;

	private BlockTransConfirmService blockTransConfirmService;
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException
    {
        LoggerUtils.logInfo(logger, "开始区块交易确认轮询任务");
        try
        {
            initialize(getApplicationContext(context));
            // 根据bitgo、btc、blockmeta三方接口查询区块交易确认结果
            blockTransConfirmService.transExternalQuery();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LoggerUtils.logError(logger, "区块交易确认轮询任务失败：{}", e.getLocalizedMessage());
        }
        LoggerUtils.logInfo(logger, "结束区块交易确认轮询任务");
    }
    
    /**
     * 初始化服务
     * @param applicationContext
     */
    protected void initialize(ApplicationContext applicationContext)
    {
        if (null == blockTransConfirmService) blockTransConfirmService = applicationContext.getBean(BlockTransConfirmService.class);
    }
}

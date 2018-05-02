package com.blocain.bitms.task.fund;

import com.blocain.bitms.task.basic.AbstractQuartzBean;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.trade.fund.entity.AccountFundTransfer;
import com.blocain.bitms.trade.fund.service.AccountFundTransferService;
import com.blocain.bitms.trade.fund.service.FundService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

/**
 * 定时轮询划拨状态
 * <p>File：FundTransferQuartzTask.java</p>
 * <p>Title: FundTransferQuartzTask</p>
 * <p>Description: FundTransferQuartzTask</p>
 * <p>Copyright: Copyright (c) 2017/7/7</p>
 * <p>Company: BloCain</p>
 *
 * @author zhangchunxi
 * @version 1.0
 */
public class FundTransferQuartzTask extends AbstractQuartzBean
{
	private static final long serialVersionUID = 9031747787993971408L;

	private AccountFundTransferService accountFundTransferService;
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException
    {
        LoggerUtils.logInfo(logger, "开始划拨状态轮询任务");
        try
        {
            initialize(getApplicationContext(context));
            accountFundTransferService.autoGetSinglePendingApprovals();
            accountFundTransferService.autoGetSingleTransaction();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LoggerUtils.logError(logger, "划拨状态轮询任务失败：{}", e.getLocalizedMessage());
        }
        LoggerUtils.logInfo(logger, "结束划拨状态轮询任务");
    }
    
    /**
     * 初始化服务
     * @param applicationContext
     */
    protected void initialize(ApplicationContext applicationContext)
    {
        if (null == accountFundTransferService) accountFundTransferService = applicationContext.getBean(AccountFundTransferService.class);
    }
}

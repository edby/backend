package com.blocain.bitms.task.fund;

import com.blocain.bitms.task.basic.AbstractQuartzBean;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.trade.fund.service.AccountDebitAssetPremiumService;
import com.blocain.bitms.trade.fund.service.AccountFundTransferService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

/**
 *  杠杆现货类快照定时任务
 * <p>File：LeveragedSpotSnapQuartzTask.java</p>
 * <p>Title: LeveragedSpotSnapQuartzTask</p>
 * <p>Description: LeveragedSpotSnapQuartzTask</p>
 * <p>Copyright: Copyright (c) 2017/7/7</p>
 * <p>Company: BloCain</p>
 *
 * @author zhangchunxi
 * @version 1.0
 */
public class LeveragedSpotSnapQuartzTask extends AbstractQuartzBean
{
    private static final long               serialVersionUID = 9031747787993971408L;
    
    private AccountDebitAssetPremiumService accountDebitAssetPremiumService;
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException
    {
        LoggerUtils.logInfo(logger, "开始杠杆现货类快照定时任务");
        try
        {
            initialize(getApplicationContext(context));
            accountDebitAssetPremiumService.doMarketSnap();
            accountDebitAssetPremiumService.doLeveragedSpotAssetAndDebitSnap();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LoggerUtils.logError(logger, "杠杆现货类快照定时任务失败：{}", e.getLocalizedMessage());
        }
        LoggerUtils.logInfo(logger, "结束杠杆现货类快照定时任务");
    }
    
    /**
     * 初始化服务
     * @param applicationContext
     */
    protected void initialize(ApplicationContext applicationContext)
    {
        if (null == accountDebitAssetPremiumService) accountDebitAssetPremiumService = applicationContext.getBean(AccountDebitAssetPremiumService.class);
    }
}

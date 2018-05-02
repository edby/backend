package com.blocain.bitms.task.fund;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

import com.blocain.bitms.task.basic.AbstractQuartzBean;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.trade.fund.service.FundService;

/**
 * 定时解冻BMS资产 Introduce
 * <p>File：FundUnfrozenBmsAssetQuartzTask.java</p>
 * <p>Title: FundUnfrozenBmsAssetQuartzTask</p>
 * <p>Description: FundUnfrozenBmsAssetQuartzTask</p>
 * <p>Copyright: Copyright (c) 2017/7/7</p>
 * <p>Company: BloCain</p>
 *
 * @author sunbiao
 * @version 1.0
 */
public class FundUnfrozenBmsAssetQuartzTask extends AbstractQuartzBean
{
	private static final long serialVersionUID = 9031747787993971408L;

	private FundService fundService;
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException
    {
        LoggerUtils.logInfo(logger, "开始解冻BMS资产轮询任务");
        try
        {
            // initialize(getApplicationContext(context));
            // 定时解冻BMS资产
            // fundService.fundUnfrozenBmsAsset();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LoggerUtils.logError(logger, "解冻BMS资产轮询任务失败：{}", e.getLocalizedMessage());
        }
        LoggerUtils.logInfo(logger, "结束解冻BMS资产轮询任务");
    }
    
    /**
     * 初始化服务
     * @param applicationContext
     */
    protected void initialize(ApplicationContext applicationContext)
    {
        if (null == fundService) fundService = applicationContext.getBean(FundService.class);
    }
}

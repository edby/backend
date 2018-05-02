package com.blocain.bitms.task.stockinfo;

import com.blocain.bitms.task.basic.AbstractQuartzBean;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.trade.fund.service.AccountDebitAssetService;
import com.blocain.bitms.trade.stockinfo.service.StockRateService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

/**
 * 提现费率计算
 * <p>File：StockRateQuartzTask.java</p>
 * <p>Title: StockRateQuartzTask</p>
 * <p>Description: StockRateQuartzTask</p>
 * <p>Copyright: Copyright (c) 2017/7/7</p>
 * <p>Company: BloCain</p>
 *
 * @author sunbiao
 * @version 1.0
 */
public class StockRateQuartzTask extends AbstractQuartzBean
{
	
	private static final long serialVersionUID = 6818068059579857089L;

	private StockRateService stockRateService;
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException
    {
        LoggerUtils.logInfo(logger, "开始提现手续费费率轮询任务");
        try
        {
            stockRateService.fiexWithdrawFeeRateFromQuotation();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LoggerUtils.logError(logger, "提现手续费费率轮询任务失败：{}", e.getLocalizedMessage());
        }
        LoggerUtils.logInfo(logger, "结束提现手续费费率轮询任务");
    }
    
    /**
     * 初始化服务
     * @param applicationContext
     */
    protected void initialize(ApplicationContext applicationContext)
    {
        if (null == stockRateService) stockRateService = applicationContext.getBean(StockRateService.class);
    }
}

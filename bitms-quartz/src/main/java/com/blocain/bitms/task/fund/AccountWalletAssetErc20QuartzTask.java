package com.blocain.bitms.task.fund;

import com.blocain.bitms.trade.fund.service.AccountWalletAssetService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import com.blocain.bitms.task.basic.AbstractQuartzBean;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.trade.fund.service.AccountCandyRecordService;

/**
 * 定时轮询 检查token平台内部累计已充值是否已大于发行总量
 * <p>File：AccountWalletAssetErc20QuartzTask.java</p>
 * <p>Title: AccountWalletAssetErc20QuartzTask</p>
 * <p>Description: AccountWalletAssetErc20QuartzTask</p>
 * <p>Copyright: Copyright (c) 2018-03-15</p>
 * <p>Company: BloCain</p>
 *
 * @author zhangchunxi
 * @version 1.0
 */
public class AccountWalletAssetErc20QuartzTask extends AbstractQuartzBean
{
	private static final long serialVersionUID = 9031747787993971408L;

	private AccountWalletAssetService accountWalletAssetService;
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException
    {
        LoggerUtils.logInfo(logger, "开始定时轮询检查token平台内部累计已充值是否已大于发行总量");
        try
        {
            accountWalletAssetService.autoCheckPlatSumCoin();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LoggerUtils.logError(logger, "定时轮询检查token平台内部累计已充值是否已大于发行总量失败：{}", e.getLocalizedMessage());
        }
        LoggerUtils.logInfo(logger, "结束定时轮询检查token平台内部累计已充值是否已大于发行总量");
    }
    
    /**
     * 初始化服务
     * @param applicationContext
     */
    protected void initialize(ApplicationContext applicationContext)
    {
        if (null == accountWalletAssetService) accountWalletAssetService = applicationContext.getBean(AccountWalletAssetService.class);
    }
}

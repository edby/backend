package com.blocain.bitms.task.fund;

import com.blocain.bitms.task.basic.AbstractQuartzBean;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.trade.fund.service.AccountWithdrawRecordERC20Service;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

/**
 * 定时轮询 ERC20token的交易状态
 * <p>File：Erc20TokenWithdrawQuartzTask.java</p>
 * <p>Title: Erc20TokenWithdrawQuartzTask</p>
 * <p>Description: Erc20TokenWithdrawQuartzTask</p>
 * <p>Copyright: Copyright (c) 2018-03-02</p>
 * <p>Company: BloCain</p>
 *
 * @author zhangchunxi
 * @version 1.0
 */
public class Erc20TokenWithdrawQuartzTask extends AbstractQuartzBean
{
	private static final long serialVersionUID = 9031747787993971408L;

	private AccountWithdrawRecordERC20Service accountWithdrawRecordERC20Service;
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException
    {
        LoggerUtils.logInfo(logger, "开始定时轮询ERC20token的交易状态");
        try
        {
            initialize(getApplicationContext(context));
            accountWithdrawRecordERC20Service.autoTransactionStatus();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LoggerUtils.logError(logger, "定时轮询ERC20token的交易状态失败：{}", e.getLocalizedMessage());
        }
        LoggerUtils.logInfo(logger, "结束定时轮询ERC20token的交易状态");
    }
    
    /**
     * 初始化服务
     * @param applicationContext
     */
    protected void initialize(ApplicationContext applicationContext)
    {
        if (null == accountWithdrawRecordERC20Service) accountWithdrawRecordERC20Service = applicationContext.getBean(AccountWithdrawRecordERC20Service.class);
    }
}

package com.blocain.bitms.task.payment;

import com.blocain.bitms.task.basic.AbstractQuartzBean;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.trade.fund.service.BlockTransConfirmService;
import com.blocain.bitms.trade.fund.service.SystemWalletAddrService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

/**
 * 钱包地址总接收轮询 Introduce
 * <p>File：WalletAddrReceivedQuartzTask.java</p>
 * <p>Title: WalletAddrReceivedQuartzTask</p>
 * <p>Description: WalletAddrReceivedQuartzTask</p>
 * <p>Copyright: Copyright (c) 2017/7/7</p>
 * <p>Company: BloCain</p>
 *
 * @author sunbiao
 * @version 1.0
 */
public class WalletAddrReceivedQuartzTask extends AbstractQuartzBean
{
	
	private static final long serialVersionUID = 6838068059579857189L;

	private SystemWalletAddrService systemWalletAddrService;
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException
    {
        LoggerUtils.logInfo(logger, "开始钱包地址总接收轮询任务");
        try
        {
            initialize(getApplicationContext(context));
            // 根据btc三方接口查询地址上总接收数量
            systemWalletAddrService.addressExternalQuery();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LoggerUtils.logError(logger, "钱包地址总接收轮询任务失败：{}", e.getLocalizedMessage());
        }
        LoggerUtils.logInfo(logger, "结束钱包地址总接收轮询任务");
    }
    
    /**
     * 初始化服务
     * @param applicationContext
     */
    protected void initialize(ApplicationContext applicationContext)
    {
        if (null == systemWalletAddrService) systemWalletAddrService = applicationContext.getBean(SystemWalletAddrService.class);
    }
}

package com.blocain.bitms.task.stockinfo;

import com.blocain.bitms.task.basic.AbstractQuartzBean;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.trade.stockinfo.service.Erc20TokenService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

/**
 * 定时轮询 ERC20token的激活状态-过期关闭激活状态
 * <p>File：Erc20TokenQuartzTask.java</p>
 * <p>Title: Erc20TokenQuartzTask</p>
 * <p>Description: Erc20TokenQuartzTask</p>
 * <p>Copyright: Copyright (c) 2018-03-02</p>
 * <p>Company: BloCain</p>
 *
 * @author zhangchunxi
 * @version 1.0
 */
public class Erc20TokenQuartzTask extends AbstractQuartzBean
{
    private static final long serialVersionUID = 9031747787993971408L;
    
    private Erc20TokenService erc20TokenService;
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException
    {
        LoggerUtils.logInfo(logger, "开始定时轮询ERC20token激活状态");
        try
        {
            initialize(getApplicationContext(context));
            erc20TokenService.autoCloseActiveToken();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LoggerUtils.logError(logger, "定时轮询ERC20token的激活状态失败：{}", e.getLocalizedMessage());
        }
        LoggerUtils.logInfo(logger, "结束定时轮询ERC20token的激活状态");
    }
    
    /**
     * 初始化服务
     * @param applicationContext
     */
    protected void initialize(ApplicationContext applicationContext)
    {
        if (null == erc20TokenService) erc20TokenService = applicationContext.getBean(Erc20TokenService.class);
    }
}

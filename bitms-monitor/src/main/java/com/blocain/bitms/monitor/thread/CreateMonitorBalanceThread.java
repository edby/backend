package com.blocain.bitms.monitor.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blocain.bitms.monitor.service.MonitorEngineService;
import com.blocain.bitms.tools.utils.LoggerUtils;

/**
 * 生成资金流水余额服务线程
 * CreateMonitorBalanceThread Introduce
 * <p>File：CreateMonitorBalanceThread.java</p>
 * <p>Title: CreateMonitorBalanceThread</p>
 * <p>Description: CreateMonitorBalanceThread</p>
 * <p>Copyright: Copyright (c) 2017/9/26</p>
 * <p>Company: BloCain</p>
 *
 * @author Jiangsc
 * @version 1.0
 */
@Component
public class CreateMonitorBalanceThread implements Runnable
{
    private static Logger        logger    = LoggerFactory.getLogger(CreateMonitorBalanceThread.class);
    
    private boolean              isRunning = true;
    
    @Autowired
    private MonitorEngineService monitorEngineService;
    
    @Override
    public void run()
    {
        while (isRunning)
        {
            try
            {
                LoggerUtils.logDebug(logger, "开始生成资金流水余额服务 ===============");
                //monitorEngineService.generateMonitorBalance();
            }
            catch (Exception e)
            {
                LoggerUtils.logError(logger, "执行生成资金流水余额失败：{}", e.getLocalizedMessage());
            }
            finally
            {
                try
                {
                    Thread.sleep(60000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            LoggerUtils.logDebug(logger, "结束生成资金流水余额服务 ===============");
        }
    }
    
    public void setRunning(boolean running)
    {
        isRunning = running;
    }
}

package com.blocain.bitms.monitor.thread;

import com.blocain.bitms.monitor.consts.MonitorConst;
import com.blocain.bitms.monitor.entity.MonitorConfig;
import com.blocain.bitms.monitor.service.MonitorEngineService;
import com.blocain.bitms.tools.utils.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 热钱包余额不足监控服务线程
 * MonitorErc20HotWalletThread Introduce
 * <p>File：MonitorErc20HotWalletThread.java</p>
 * <p>Title: MonitorErc20HotWalletThread</p>
 * <p>Description: MonitorErc20HotWalletThread</p>
 * <p>Copyright: Copyright (c) 2017/9/26</p>
 * <p>Company: BloCain</p>
 *
 * @author Jiangsc
 * @version 1.0
 */
@Component
public class MonitorErc20HotWalletThread implements Runnable
{
    private static Logger        logger    = LoggerFactory.getLogger(MonitorErc20HotWalletThread.class);
    
    private boolean              isRunning = true;

    @Autowired
    private MonitorEngineService monitorEngineService;
    
    @Override
    public void run()
    {
        MonitorConfig config = null;
        Long pollingTime = 5000L;
        while (isRunning)
        {
            config = (MonitorConfig)MonitorConst.CACHE_MAP.get(MonitorConst.MONITOR_PARAMTYPE_ERC20HOTWALLET);
            //未启用的监控，不往下执行
            if( null == config || !config.getActive()){
                try
                {
                    Thread.sleep(pollingTime);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                continue;
            }

            try{
                pollingTime = config.getPollingTime();
                LoggerUtils.logDebug(logger, "开始热钱包余额不足提醒服务 ===============");
                monitorEngineService.dealBlockNumberMonitor(config);
                LoggerUtils.logDebug(logger, "结束热钱包余额不足提醒服务 ===============");
            }
            catch (Exception e)
            {
                LoggerUtils.logError(logger, "执行热钱包余额不足提醒失败：{}", e.getMessage());
            }
            finally
            {
                try
                {

                    Thread.sleep(pollingTime);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void setRunning(boolean running)
    {
        isRunning = running;
    }
}

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
 * B现金资产内外总额监控服务线程
 * MonitorCashCoinThread Introduce
 * <p>File：MonitorCashCoinThread.java</p>
 * <p>Title: MonitorCashCoinThread</p>
 * <p>Description: MonitorCashCoinThread</p>
 * <p>Copyright: Copyright (c) 2017/9/26</p>
 * <p>Company: BloCain</p>
 *
 * @author Jiangsc
 * @version 1.0
 */
@Component
public class MonitorCashCoinThread implements Runnable
{
    private static Logger        logger    = LoggerFactory.getLogger(MonitorCashCoinThread.class);
    
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
            config = (MonitorConfig)MonitorConst.CACHE_MAP.get(MonitorConst.MONITOR_PARAMTYPE_CASHCOIN);
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
                LoggerUtils.logDebug(logger, "开始现金资产内外总额监控服务 ===============");
                monitorEngineService.dealCachCoinMonitor(config);
                LoggerUtils.logDebug(logger, "结束现金资产内外总额监控服务 ===============");
            }
            catch (Exception e)
            {
                LoggerUtils.logError(logger, "执行现金资产内外总额监控失败：{}", e.getMessage());
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

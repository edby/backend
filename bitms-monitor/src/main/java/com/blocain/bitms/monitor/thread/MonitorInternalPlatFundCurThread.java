package com.blocain.bitms.monitor.thread;

import com.blocain.bitms.monitor.consts.MonitorConst;
import com.blocain.bitms.monitor.entity.MonitorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blocain.bitms.monitor.service.MonitorEngineService;
import com.blocain.bitms.tools.utils.LoggerUtils;

/**
 *  撮合总账资金监控服务线程
 * MonitorMatchFundCurThread Introduce
 * <p>File：MonitorInternalPlatFundCurThread.java</p>
 * <p>Title: MonitorMatchFundCurThread</p>
 * <p>Description: MonitorMatchFundCurThread</p>
 * <p>Copyright: Copyright (c) 2017/9/26</p>
 * <p>Company: BloCain</p>
 *
 * @author Jiangsc
 * @version 1.0
 */
@Component
public class MonitorInternalPlatFundCurThread implements Runnable
{
    private static Logger        logger    = LoggerFactory.getLogger(MonitorInternalPlatFundCurThread.class);
    
    private boolean              isRunning = true;

    @Autowired
    private MonitorEngineService monitorEngineService;
    
    @Override
    public void run()
    {
        MonitorConfig config = null;
        Long pollingTime = 3000L;
        while (isRunning)
        {
            config = (MonitorConfig)MonitorConst.CACHE_MAP.get(MonitorConst.MONITOR_PARAMTYPE_INTERNALPLATFUNDCUR);
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

            try
            {
                pollingTime = config.getPollingTime();
                LoggerUtils.logDebug(logger, "开始撮合总账资金监控服务 ===============");
                monitorEngineService.dealInternalPlatFundCurMonitor(config);
                LoggerUtils.logDebug(logger, "结束撮合总账资金监控服务 ===============");
            }
            catch (Exception e)
            {
                LoggerUtils.logError(logger, "执行撮合总账资金监控失败：{}", e.getMessage());
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

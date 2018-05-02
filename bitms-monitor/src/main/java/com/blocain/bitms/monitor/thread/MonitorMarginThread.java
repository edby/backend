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
 * 杠杆保证金监控服务线程
 * MonitorMarginThread Introduce
 * <p>File：MonitorMarginThread.java</p>
 * <p>Title: MonitorMarginThread</p>
 * <p>Description: MonitorMarginThread</p>
 * <p>Copyright: Copyright (c) 2017/9/26</p>
 * <p>Company: BloCain</p>
 *
 * @author Jiangsc
 * @version 1.0
 */
@Component
public class MonitorMarginThread implements Runnable
{
    private static Logger        logger    = LoggerFactory.getLogger(MonitorMarginThread.class);

    private boolean              isRunning = true;

    @Autowired
    private MonitorEngineService monitorEngineService;

    @Override
    public void run()
    {
        MonitorConfig config = null;
        Long pollingTime = 1000L;
        while (isRunning)
        {

            config = (MonitorConfig)MonitorConst.CACHE_MAP.get(MonitorConst.MONITOR_PARAMTYPE_MARGIN);
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

            try {
                pollingTime = config.getPollingTime();
                LoggerUtils.logDebug(logger, "开始杠杆保证金监控服务 ===============");
                monitorEngineService.dealMarginMonitor(config);
                LoggerUtils.logDebug(logger, "结束杠杆保证金监控服务 ===============");
            }
            catch (Exception e)
            {
                LoggerUtils.logError(logger, "执行杠杆保证金监控失败：{}", e.getLocalizedMessage());
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

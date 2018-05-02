package com.blocain.bitms.monitor.thread;

import com.blocain.bitms.monitor.consts.MonitorConst;
import com.blocain.bitms.monitor.entity.MonitorConfig;
import com.blocain.bitms.monitor.service.MonitorConfigService;
import com.blocain.bitms.tools.utils.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 监控服务配置扫描服务线程
 * MonitorConfigScanThread Introduce
 * <p>File：MonitorConfigScanThread.java</p>
 * <p>Title: MonitorConfigScanThread</p>
 * <p>Description: MonitorConfigScanThread</p>
 * <p>Copyright: Copyright (c) 2017/9/26</p>
 * <p>Company: BloCain</p>
 *
 * @author Jiangsc
 * @version 1.0
 */
@Component
public class MonitorConfigScanThread implements Runnable
{
    private static Logger        logger    = LoggerFactory.getLogger(MonitorConfigScanThread.class);

    private boolean              isRunning = true;

    @Autowired
    private MonitorConfigService monitorConfigService;

    @Override
    public void run()
    {
        while (isRunning)
        {
            try
            {
                LoggerUtils.logDebug(logger, "开始监控服务配置扫描服务 ===============");
                List<MonitorConfig> configLists = monitorConfigService.buildConfigList();
                reloadToCash(configLists);
                LoggerUtils.logDebug(logger, "结束监控服务配置扫描服务 ===============");
            }
            catch (Exception e)
            {
                LoggerUtils.logError(logger, "执行监控服务配置扫描失败：{}", e.getLocalizedMessage());
            }
            finally
            {
                try
                {
                    Thread.sleep(10000);
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


    private void reloadToCash(List<MonitorConfig> configLists)
    {

        if (CollectionUtils.isEmpty(configLists))
            MonitorConst.CACHE_MAP.clear();

        MonitorConfig config;
        MonitorConfig cashConfig;
        for(int i = 0 ; i<configLists.size(); i++)
        {
            config = configLists.get(i);
            cashConfig = (MonitorConfig)MonitorConst.CACHE_MAP.get(config.getMonitorCode());

            if (null == cashConfig || !config.equals(cashConfig))
                MonitorConst.CACHE_MAP.put(config.getMonitorCode(),config);
        }
    }

}

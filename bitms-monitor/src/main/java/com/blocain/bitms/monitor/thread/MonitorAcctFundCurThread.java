package com.blocain.bitms.monitor.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blocain.bitms.monitor.consts.MonitorConst;
import com.blocain.bitms.monitor.entity.MonitorConfig;
import com.blocain.bitms.monitor.service.MonitorEngineService;
import com.blocain.bitms.tools.utils.LoggerUtils;

/**
 *  账户级别资金监控服务线程
 * MonitorAcctFundCurThread Introduce
 * <p>File：MonitorAcctFundCurThread.java</p>
 * <p>Title: MonitorAcctFundCurThread</p>
 * <p>Description: MonitorAcctFundCurThread</p>
 * <p>Copyright: Copyright (c) 2017/9/26</p>
 * <p>Company: BloCain</p>
 *
 * @author Jiangsc
 * @version 1.0
 */
@Component
public class MonitorAcctFundCurThread implements Runnable
{
    private static Logger        logger    = LoggerFactory.getLogger(MonitorAcctFundCurThread.class);
    
    private boolean              isRunning = true;
    
    @Autowired
    private MonitorEngineService monitorEngineService;
    
    @Override
    public void run()
    {
        Long pollingTime = 7000L;
        MonitorConfig config = null;
        while (isRunning)
        {
            config = (MonitorConfig) MonitorConst.CACHE_MAP.get(MonitorConst.MONITOR_PARAMTYPE_ACCTFUNDCUR);
            // 未启用的监控，不往下执行
            if (null == config || !config.getActive())
            {
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
                LoggerUtils.logDebug(logger, "开始账户级别资金监控服务 ===============");
                monitorEngineService.dealAccountFundCur(config);
                LoggerUtils.logDebug(logger, "结束账户级别资金监控服务 ===============");
            }
            catch (Exception e)
            {
                LoggerUtils.logError(logger, "执行账户级别资金监控失败：{}", e.getLocalizedMessage());
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

package com.blocain.bitms.quotation.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blocain.bitms.quotation.config.InQuotationConfig;
import com.blocain.bitms.quotation.service.AllRtQuotationEngineService;
import com.blocain.bitms.tools.utils.LoggerUtils;

/**
 * 全行情推送服务线程
 */
@Component
public class AllRtQuotationThread implements Runnable
{
    private static Logger               logger    = LoggerFactory.getLogger(AllRtQuotationThread.class);
    
    private boolean                     isRunning = true;
    
    @Autowired
    private AllRtQuotationEngineService allRtQuotationEngineService;
    
    @Override
    public void run()
    {
        while (isRunning)
        {
            try
            {
                LoggerUtils.logDebug(logger, "开始[最新全行情推送]线程服务 ===============");
                allRtQuotationEngineService.pushAllRtQuotationData();
                Thread.sleep(InQuotationConfig.THREAD_SLEEP_ALLRTQUOTATION);
            }
            catch (Exception e)
            {
                LoggerUtils.logError(logger, "获取[最新全行情推送]失败：{}", e.getLocalizedMessage());
            }
            LoggerUtils.logDebug(logger, "结束[最新全行情推送]线程服务 ===============");
        }
    }
    
    public void setRunning(boolean running)
    {
        this.isRunning = running;
    }
    
    public boolean isRunning()
    {
        return isRunning;
    }
}
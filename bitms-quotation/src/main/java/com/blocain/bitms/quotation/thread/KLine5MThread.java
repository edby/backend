package com.blocain.bitms.quotation.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blocain.bitms.quotation.config.InQuotationConfig;
import com.blocain.bitms.quotation.service.KLineEngineService;
import com.blocain.bitms.tools.utils.LoggerUtils;

/**
 * K线图线程
 * KLineThread Introduce
 * <p>File：KLine_1MinThread.java</p>
 * <p>Title: KLineThread</p>
 * <p>Description: KLineThread</p>
 * <p>Copyright: Copyright (c) 2017/9/19</p>
 * <p>Company: BloCain</p>
 *
 * @author Jiangsc
 * @version 1.0
 */
@Component
public class KLine5MThread implements Runnable
{
    private static Logger      logger    = LoggerFactory.getLogger(KLine5MThread.class);
    
    private boolean            isRunning = true;
    
    @Autowired
    private KLineEngineService kLineEngineService;
    
    @Override
    public void run()
    {
        Long pollingTime = 6550L;
        while (isRunning)
        {
            try
            {
                pollingTime = InQuotationConfig.THREAD_SLEEP_KLINE_5M;
                LoggerUtils.logDebug(logger, "启动[" + InQuotationConfig.SERVER_NAME + " K线_5分钟线]线程服务 ===============");
                kLineEngineService.pushKLineData(InQuotationConfig.TOPIC_KLINE_5M);
                LoggerUtils.logDebug(logger, "结束[" + InQuotationConfig.SERVER_NAME + " K线_5分钟线]线程服务 ===============");
            }
            catch (Exception e)
            {
                LoggerUtils.logError(logger, "获取[" + InQuotationConfig.SERVER_NAME + " K线_5分钟线]数据失败：{}", e.getLocalizedMessage());
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
    
    public boolean isRunning()
    {
        return isRunning;
    }
    
    public void setRunning(boolean running)
    {
        this.isRunning = running;
    }
}

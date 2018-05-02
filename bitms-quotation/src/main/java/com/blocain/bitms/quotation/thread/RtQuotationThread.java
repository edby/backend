package com.blocain.bitms.quotation.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blocain.bitms.quotation.config.InQuotationConfig;
import com.blocain.bitms.quotation.service.RtQuotationEngineService;
import com.blocain.bitms.tools.utils.LoggerUtils;

/**
 * 最新撮合行情服务线程
 * MatchPriceThread Introduce
 * <p>File：RtQuotationThread.java</p>
 * <p>Title: MatchPriceThread</p>
 * <p>Description: MatchPriceThread</p>
 * <p>Copyright: Copyright (c) 2017/9/19</p>
 * <p>Company: BloCain</p>
 *
 * @author Jiangsc
 * @version 1.0
 */
@Component
public class RtQuotationThread implements Runnable
{
    private static Logger            logger    = LoggerFactory.getLogger(RtQuotationThread.class);
    
    private boolean                  isRunning = true;
    
    @Autowired
    private RtQuotationEngineService rtQuotationEngineService;
    
    @Override
    public void run()
    {
        Long pollingTime = 3540L;
        while (isRunning)
        {
            try
            {
                pollingTime = InQuotationConfig.THREAD_SLEEP_RTQUOTATION;
                LoggerUtils.logDebug(logger, "开始[最新" + InQuotationConfig.SERVER_NAME + "撮合行情]线程服务 ===============");
                rtQuotationEngineService.pushRtQuotationInfoData(InQuotationConfig.TOPIC_RTQUOTATION_PRICE);
                LoggerUtils.logDebug(logger, "结束[最新" + InQuotationConfig.SERVER_NAME + "撮合行情]线程服务 ===============");
            }
            catch (Exception e)
            {
                LoggerUtils.logError(logger, "获取[最新" + InQuotationConfig.SERVER_NAME + "撮合行情]失败：{}", e.getLocalizedMessage());
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

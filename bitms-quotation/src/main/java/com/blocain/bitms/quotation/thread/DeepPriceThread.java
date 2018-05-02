package com.blocain.bitms.quotation.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blocain.bitms.quotation.config.InQuotationConfig;
import com.blocain.bitms.quotation.service.DeepPriceEngineService;
import com.blocain.bitms.tools.utils.LoggerUtils;

/**
 * 委托行情服务线程
 * DeepPriceThread Introduce
 * <p>File：DeepPriceThread.java</p>
 * <p>Title: DeepPriceThread</p>
 * <p>Description: DeepPriceThread</p>
 * <p>Copyright: Copyright (c) 2017/9/19</p>
 * <p>Company: BloCain</p>
 *
 * @author Jiangsc
 * @version 1.0
 */
@Component
public class DeepPriceThread implements Runnable
{
    private static Logger          logger    = LoggerFactory.getLogger(DeepPriceThread.class);
    
    private static String          msgInfo   = new StringBuilder("【").append(InQuotationConfig.SERVER_NAME).append("委托行情】").toString();
    
    private boolean                isRunning = true;
    
    @Autowired
    private DeepPriceEngineService deepPriceEngineService;
    
    @Override
    public void run()
    {
        Long pollingTime = 1730L;
        while (isRunning)
        {
            try
            {
                pollingTime = InQuotationConfig.THREAD_SLEEP_DEEPPRICE;
                LoggerUtils.logDebug(logger, "启动" + msgInfo + "服务 ===============");
                deepPriceEngineService.pushDeepPriceData(InQuotationConfig.TOPIC_ENTRUST_DEEPPRICE);
                LoggerUtils.logDebug(logger, "结束" + msgInfo + "线程服务 ===============");
            }
            catch (Exception e)
            {
                LoggerUtils.logError(logger, "获取" + msgInfo + "失败：{}", e.getLocalizedMessage());
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

package com.blocain.bitms.quotation.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blocain.bitms.quotation.config.InQuotationConfig;
import com.blocain.bitms.quotation.service.TransactionEngineService;
import com.blocain.bitms.tools.utils.LoggerUtils;

/**
 * 交易流水服务线程
 * TransactionThread Introduce
 * <p>File：TransactionThread.java</p>
 * <p>Title: TransactionThread</p>
 * <p>Description: TransactionThread</p>
 * <p>Copyright: Copyright (c) 2017/9/19</p>
 * <p>Company: BloCain</p>
 *
 * @author Jiangsc
 * @version 1.0
 */
@Component
public class TransactionThread implements Runnable
{
    private static Logger            logger    = LoggerFactory.getLogger(TransactionThread.class);
    
    private boolean                  isRunning = true;
    
    @Autowired
    private TransactionEngineService transactionEngineService;
    
    @Override
    public void run()
    {
        Long pollingTime = 5000L;
        while (isRunning)
        {
            try
            {
                LoggerUtils.logDebug(logger, "开始[" + InQuotationConfig.SERVER_NAME + "交易流水]线程服务 ===============");
                transactionEngineService.pushTransactionData(InQuotationConfig.TOPIC_REALDEAL_TRANSACTION, InQuotationConfig.REALDEAL_NUM);
                Thread.sleep(InQuotationConfig.THREAD_SLEEP_REALDEAL);
            }
            catch (Exception e)
            {
                LoggerUtils.logError(logger, "获取[" + InQuotationConfig.SERVER_NAME + "交易流水]失败：{}", e.getLocalizedMessage());
            }
            LoggerUtils.logDebug(logger, "结束[" + InQuotationConfig.SERVER_NAME + "交易流水]线程服务 ===============");
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

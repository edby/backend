package com.blocain.bitms.platscan.thread;

import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.trade.fund.service.FundScanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 资产变动扫描服务线程
 * FundScanThread Introduce
 * <p>File：FundScanThread.java</p>
 * <p>Title: FundScanThread</p>
 * <p>Description: FundScanThread</p>
 * <p>Copyright: Copyright (c) 2017/9/19</p>
 * <p>Company: BloCain</p>
 *
 * @author sunbiao
 * @version 1.0
 */
@Component
public class FundScanThread implements Runnable
{
    private static Logger   logger    = LoggerFactory.getLogger(FundScanThread.class);
    
    @Autowired
    private FundScanService fundScanService;

    private boolean         isRunning = true;

    @Override
    public void run()
    {
        while (isRunning)
        {
            LoggerUtils.logInfo(logger, "开始资产变动扫描线程服务 ===============");
            try
            {
                // 定时资产流水变动扫描
                fundScanService.fundChangeScan();
                Thread.sleep(500);
            }
            catch (Exception e)
            {
                LoggerUtils.logError(logger, "资产变动扫描失败：{}", e.getLocalizedMessage());
            }
            LoggerUtils.logInfo(logger, "结束资产变动扫描线程服务 ===============");
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

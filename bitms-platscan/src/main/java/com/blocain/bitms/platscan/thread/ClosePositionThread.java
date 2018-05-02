package com.blocain.bitms.platscan.thread;

import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.trade.trade.service.ClosePositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 自动强制平仓服务线程
 * ClosePositionThread Introduce
 * <p>File：ClosePositionThread.java</p>
 * <p>Title: ClosePositionThread</p>
 * <p>Description: ClosePositionThread</p>
 * <p>Copyright: Copyright (c) 2017/9/19</p>
 * <p>Company: BloCain</p>
 *
 * @author sunbiao
 * @version 1.0
 */
@Component
public class ClosePositionThread implements Runnable
{
    private static Logger   logger    = LoggerFactory.getLogger(ClosePositionThread.class);
    
    @Autowired
    private ClosePositionService closePositionService;

    private boolean             isRunning = true;

    @Override
    public void run()
    {
        while (isRunning)
        {
            LoggerUtils.logInfo(logger, "开始定时自动强制平仓轮询任务");
            try
            {
                // 自动强制平仓
                closePositionService.autoClosePosition();
                Thread.sleep(5000);
            }
            catch (Exception e)
            {
                LoggerUtils.logError(logger, "定时自动强制平仓轮询任务失败：{}", e.getLocalizedMessage());
            }
            LoggerUtils.logInfo(logger, "结束定时自动强制平仓轮询任务");
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

package com.blocain.bitms.wallet.thread;

import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.wallet.ERC20BlockService;
import com.blocain.bitms.wallet.config.WalletConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ERC20余额归集线程
 */
@Component
public class CollectERC20BalanceThread implements Runnable
{
    private static Logger                 logger    = LoggerFactory.getLogger(CollectERC20BalanceThread.class);

    @Autowired
    private ERC20BlockService             eRC20BlockService;

    private boolean                       isRunning = true;
    
    @Override
    public void run()
    {
        while (isRunning)
        {
            try
            {
                LoggerUtils.logDebug(logger, "启动ERC20余额归集线程服务 ===============");
                eRC20BlockService.collectERC20Balance();
                LoggerUtils.logDebug(logger, "结束ERC20余额归集线程服务 ===============");
            }
            catch (Exception e)
            {
                LoggerUtils.logError(logger, "ERC20余额归集线程服务失败：{}", e.getLocalizedMessage());
            }
            finally
            {
                try
                {
                    Thread.sleep(WalletConfig.THREAD_COLLECT_ERC20_BALANCE_SLEEP);
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

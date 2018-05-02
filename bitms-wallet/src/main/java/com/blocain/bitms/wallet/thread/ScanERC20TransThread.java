package com.blocain.bitms.wallet.thread;

import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.wallet.ERC20BlockService;
import com.blocain.bitms.wallet.config.WalletConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ERC20交易扫描线程
 */
@Component
public class ScanERC20TransThread implements Runnable
{
    private static Logger                 logger    = LoggerFactory.getLogger(ScanERC20TransThread.class);

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
                LoggerUtils.logDebug(logger, "启动ERC20交易扫描线程服务 ===============");
                eRC20BlockService.scanERC20Trans();
                LoggerUtils.logDebug(logger, "结束ERC20交易扫描线程服务 ===============");
            }
            catch (Exception e)
            {
                LoggerUtils.logError(logger, "ERC20交易扫描线程服务失败：{}", e.getLocalizedMessage());
            }
            finally
            {
                try
                {
                    Thread.sleep(WalletConfig.THREAD_SCAN_ERC20_TRANS_SLEEP);
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

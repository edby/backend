package com.blocain.bitms.wallet.thread;

import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.wallet.ERC20BlockService;
import com.blocain.bitms.wallet.config.WalletConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ERC20区块同步线程 Introduce
 * <p>Title: SyncERC20BlockThread</p>
 * <p>File：SyncERC20BlockThread.java</p>
 * <p>Description: SyncERC20BlockThread</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Component
public class SyncERC20BlockThread implements Runnable
{
    private static Logger           logger    = LoggerFactory.getLogger(SyncERC20BlockThread.class);

    @Autowired
    private ERC20BlockService       eRC20BlockService;

    private boolean                 isRunning = true;

    @Override
    public void run()
    {
        while (isRunning)
        {
            try
            {
                LoggerUtils.logDebug(logger, "启动ERC20区块同步线程服务 ===============");
                eRC20BlockService.doSyncBlockNumber();
                LoggerUtils.logDebug(logger, "结束ERC20区块同步线程服务 ===============");
            }
            catch (Exception e)
            {
                LoggerUtils.logError(logger, "ERC20区块同步线程服务失败：{}", e.getLocalizedMessage());
            }
            finally
            {
                try
                {
                    Thread.sleep(WalletConfig.THREAD_SYNC_ERC20_BLOCK_SLEEP);
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

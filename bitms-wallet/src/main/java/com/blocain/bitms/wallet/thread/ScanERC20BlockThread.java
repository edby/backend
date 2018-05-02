package com.blocain.bitms.wallet.thread;

import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.trade.block.entity.BlockInfoERC20;
import com.blocain.bitms.wallet.ERC20BlockService;
import com.blocain.bitms.wallet.config.WalletConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ERC20区块扫描线程
 */
@Component
public class ScanERC20BlockThread implements Runnable
{
    private static Logger     logger    = LoggerFactory.getLogger(ScanERC20BlockThread.class);
    
    @Autowired
    private ERC20BlockService eRC20BlockService;

    private boolean           isRunning = true;
    
    @Override
    public void run()
    {
        while (isRunning)
        {
            try
            {
                LoggerUtils.logDebug(logger, "启动ERC20区块扫描线程服务 ===============");
                // 取最后一次待扫描的区块高度
                BlockInfoERC20 LastUnScanTransBlockInfoErc20 = eRC20BlockService.getLastUnScanTransBlockInfoErc20();
                LoggerUtils.logDebug(logger, "LastUnScanTransBlockInfoErc20:" + LastUnScanTransBlockInfoErc20);
                if (null != LastUnScanTransBlockInfoErc20)
                {// 表示有区块未扫描
                    eRC20BlockService.doScanERC20Block(LastUnScanTransBlockInfoErc20);
                }
                LoggerUtils.logDebug(logger, "结束ERC20区块扫描线程服务 ===============");
            }
            catch (Exception e)
            {
                LoggerUtils.logError(logger, "ERC20区块扫描线程服务失败：{}", e.getLocalizedMessage());
            }
            finally
            {
                try
                {
                    Thread.sleep(WalletConfig.THREAD_SACN_ERC20_BLOCK_SLEEP);
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

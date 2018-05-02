package com.blocain.bitms.wallet.thread;

import com.blocain.bitms.payment.eth.EthereumUtils;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.trade.stockinfo.entity.Erc20Token;
import com.blocain.bitms.wallet.ERC20BlockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigInteger;
import java.util.List;

/**
 * ERC20区块Erc20Token信息扫描线程
 */
@Component
public class ScanERC20BlockErc20TokenThread implements Runnable
{
    private static Logger     logger    = LoggerFactory.getLogger(ScanERC20BlockErc20TokenThread.class);
    
    @Autowired
    private ERC20BlockService eRC20BlockService;

    private boolean           isRunning = true;
    
    @Override
    public void run()
    {
        // BigInteger iStartNum = new BigInteger("0");
         BigInteger iStartNum = new BigInteger("5500000");
        // 3105009
        // BigInteger iStartNum = new BigInteger("3105009");
//        List<Erc20Token> listErc20Token = eRC20BlockService.findListMinHeightErc20Token();
//        if(null != listErc20Token && listErc20Token.size() > 0){
//            LoggerUtils.logInfo(logger, "Erc20Token高度开始BlockHeight:" + listErc20Token.get(0).getBlockHeight());
//            iStartNum = BigInteger.valueOf(listErc20Token.get(0).getBlockHeight()).add(BigInteger.ONE);
//            LoggerUtils.logInfo(logger, "最原始高度开始iStartNum:" + iStartNum);
//        }
        iStartNum = iStartNum.add(BigInteger.ONE);
        while (isRunning)
        {
            try
            {
                LoggerUtils.logDebug(logger, "启动ERC20区块Erc20Token信息扫描线程服务 ===============");
                iStartNum = iStartNum.subtract(BigInteger.ONE);
                LoggerUtils.logInfo(logger, "iStartNum:" + iStartNum);
                EthBlock.Block ethBlock = EthereumUtils.ethGetBlockByNumber(iStartNum, false);
                LoggerUtils.logDebug(logger, "ethBlock:" + ethBlock.toString());
                if (null != ethBlock)
                {
                    eRC20BlockService.doScanERC20BlockErc20Token(ethBlock);
                }
                LoggerUtils.logDebug(logger, "结束ERC20区块Erc20Token信息扫描线程服务 ===============");
            }
            catch (Exception e)
            {
                LoggerUtils.logError(logger, "ERC20区块Erc20Token信息扫描线程服务失败：{}", e.getLocalizedMessage());
            }
            finally
            {
//                try
//                {
//                    Thread.sleep(500);
//                }
//                catch (InterruptedException e)
//                {
//                    e.printStackTrace();
//                }
            }
        }
    }
    
    public void setRunning(boolean running)
    {
        isRunning = running;
    }
}

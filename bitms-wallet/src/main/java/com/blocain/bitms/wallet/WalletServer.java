package com.blocain.bitms.wallet;

import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.wallet.thread.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import sun.misc.Signal;
import sun.misc.SignalHandler;

public class WalletServer
{
    private static final Logger logger = LoggerFactory.getLogger(WalletServer.class);
    
    public static void main(String[] args) throws Exception
    {
        String[] config = new String[]{"spring.xml", "spring-jdbc.xml", "spring-jedis.xml", "spring-mongodb.xml"};
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(config);

        LoggerUtils.logInfo(logger, "WalletServer starting ...");
        SyncERC20BlockThread syncERC20Block = context.getBean(SyncERC20BlockThread.class);
        ScanERC20BlockThread scanERC20Block = context.getBean(ScanERC20BlockThread.class);
        ScanERC20TransThread scanERC20Trans = context.getBean(ScanERC20TransThread.class);
        CollectERC20BalanceThread collectERC20Balance = context.getBean(CollectERC20BalanceThread.class);
        ScanERC20BlockErc20TokenThread scanERC20BlockErc20Token = context.getBean(ScanERC20BlockErc20TokenThread.class);
        ScanEthAddrsThread scanEthAddrs = context.getBean(ScanEthAddrsThread.class);

        Thread syncERC20BlockNumberThread = new Thread(syncERC20Block);
        Thread scanERC20BlockThread = new Thread(scanERC20Block);
        Thread scanERC20TransThread = new Thread(scanERC20Trans);
        Thread collectERC20BalanceThread = new Thread(collectERC20Balance);
        Thread scanERC20BlockErc20TokenThread = new Thread(scanERC20BlockErc20Token);
        Thread scanEthAddrsThread = new Thread(scanEthAddrs);

        if (!syncERC20BlockNumberThread.isAlive())
        {
            syncERC20BlockNumberThread.start();
            Thread.sleep(1000);
            LoggerUtils.logInfo(logger, "ERC20区块编号同步服务启动完成!");
        }
        if (!scanERC20BlockThread.isAlive())
        {
            scanERC20BlockThread.start();
            Thread.sleep(500);
            LoggerUtils.logInfo(logger, "ERC20区块扫描服务启动完成!");
        }
        if (!scanERC20TransThread.isAlive())
        {
            scanERC20TransThread.start();
            Thread.sleep(1500);
            LoggerUtils.logInfo(logger, " ERC20交易扫描服务启动完成!");
        }
        if (!collectERC20BalanceThread.isAlive())
        {
            collectERC20BalanceThread.start();
            Thread.sleep(1500);
            LoggerUtils.logInfo(logger, " ERC20余额归集服务启动完成!");
        }
//        if (!scanERC20BlockErc20TokenThread.isAlive())
//        {
//            scanERC20BlockErc20TokenThread.start();
//            Thread.sleep(1500);
//            LoggerUtils.logInfo(logger, " ERC20区块Erc20Token信息扫描服务启动完成!");
//        }
//        if (!scanEthAddrsThread.isAlive())
//        {
//            scanEthAddrsThread.start();
//            Thread.sleep(1500);
//            LoggerUtils.logInfo(logger, " ETH有效地址库信息扫描服务启动完成!");
//        }
        LoggerUtils.logInfo(logger, "WalletServer start successed!");

        SignalHandler handler = sig -> {
            LoggerUtils.logError(logger, "recieved kill {} Singnal,prepare exit..", sig.getName());
            syncERC20Block.setRunning(false);
            scanERC20Block.setRunning(false);
            scanERC20Trans.setRunning(false);
            collectERC20Balance.setRunning(false);
            scanERC20BlockErc20Token.setRunning(false);
            scanEthAddrs.setRunning(false);
        };
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("windows"))
        {
            Signal.handle(new Signal("INT"), handler);
        }
        else
        {
            Signal.handle(new Signal("TERM"), handler);
        }
    }
}

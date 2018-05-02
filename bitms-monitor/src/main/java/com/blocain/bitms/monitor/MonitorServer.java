package com.blocain.bitms.monitor;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.blocain.bitms.monitor.thread.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.blocain.bitms.monitor.config.MonitorConfigLoader;
import com.blocain.bitms.tools.utils.LoggerUtils;

import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * 监控服务启动器
 * <p>File：MonitorServer.java</p>
 * <p>Title: MonitorServer</p>
 * <p>Description: MonitorServer</p>
 * <p>Copyright: Copyright (c) 2017/9/22</p>
 * <p>Company: BloCain</p>
 *
 * @author Jiangsc
 * @version 1.0
 */
public class MonitorServer
{
    private static final Logger logger = LoggerFactory.getLogger(MonitorServer.class);
    
    public static void main(String[] args) throws IOException
    {
        String[] config = new String[]{
                "classpath:spring.xml",
                "classpath:spring-jedis.xml",
                "classpath:spring-jdbc.xml",
                "classpath:spring-mongodb.xml"
        };
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(config);
        // 动态加载监控配置文件
        MonitorConfigLoader.loadProp();
        SimpleDateFormat startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LoggerUtils.logInfo(logger, "==========================================");
        LoggerUtils.logInfo(logger, "开始启动监控服务线程--- " + startDate.format(new Date()));

        LoggerUtils.logInfo(logger, "启动监控服务配置扫描服务线程!");
        MonitorConfigScanThread monitorConfigScanServiceThread = context.getBean(MonitorConfigScanThread.class);
        Thread monitorConfigScanThread = new Thread(monitorConfigScanServiceThread);
        if (!monitorConfigScanThread.isAlive()) monitorConfigScanThread.start();

        LoggerUtils.logInfo(logger, "启动杠杆保证金监控服务线程!");
        MonitorMarginThread monitorMarginServiceThread = context.getBean(MonitorMarginThread.class);
        Thread monitorMarginThread = new Thread(monitorMarginServiceThread);
        if (!monitorMarginThread.isAlive()) monitorMarginThread.start();

        LoggerUtils.logInfo(logger, "启动资金总账监控服务线程!");
        MonitorInternalPlatFundCurThread monitorInternalPlatFundCurServiceThread = context.getBean(MonitorInternalPlatFundCurThread.class);
        Thread monitorInternalPlatFundCurThread = new Thread(monitorInternalPlatFundCurServiceThread);
        if (!monitorInternalPlatFundCurThread.isAlive()) monitorInternalPlatFundCurThread.start();

        LoggerUtils.logInfo(logger, "启动账户资金流水监控服务线程!");
        MonitorAcctFundCurThread monitorAcctFundCurServiceThread = context.getBean(MonitorAcctFundCurThread.class);
        Thread monitorAcctFundCurThread = new Thread(monitorAcctFundCurServiceThread);
        if (!monitorAcctFundCurThread.isAlive()) monitorAcctFundCurThread.start();

        LoggerUtils.logInfo(logger, "启动数字资产内外部总账监控服务线程!");
        MonitorDigitalCoinThread monitorDigitalCoinServiceThread = context.getBean(MonitorDigitalCoinThread.class);
        Thread monitorDigitalCoinThread = new Thread(monitorDigitalCoinServiceThread);
        if (!monitorDigitalCoinThread.isAlive()) monitorDigitalCoinThread.start();

        LoggerUtils.logInfo(logger, "启动现金资产内外部总账监控服务线程!");
        MonitorCashCoinThread monitorCashCoinServiceThread = context.getBean(MonitorCashCoinThread.class);
        Thread monitorCashCoinThread = new Thread(monitorCashCoinServiceThread);
        if (!monitorCashCoinThread.isAlive()) monitorCashCoinThread.start();

        LoggerUtils.logInfo(logger, "启动现金资产内外部总账监控服务线程!");
        MonitorBlockNumThread monitorBlockNumServiceThread = context.getBean(MonitorBlockNumThread.class);
        Thread monitorBlockNumThread = new Thread(monitorBlockNumServiceThread);
        if (!monitorBlockNumThread.isAlive()) monitorBlockNumThread.start();

        LoggerUtils.logInfo(logger, "启动现金资产内外部总账监控服务线程!");
        MonitorErc20BalThread monitorErc20BalServiceThread = context.getBean(MonitorErc20BalThread.class);
        Thread monitorErc20BalThread = new Thread(monitorErc20BalServiceThread);
        if (!monitorErc20BalThread.isAlive()) monitorErc20BalThread.start();

        LoggerUtils.logInfo(logger, "启动冷钱包余额不足提醒服务线程!");
        MonitorErc20ColdWalletThread monitorErc20ColdWalletServiceThread = context.getBean(MonitorErc20ColdWalletThread.class);
        Thread monitorErc20ColdWalletThread = new Thread(monitorErc20ColdWalletServiceThread);
        if (!monitorErc20ColdWalletThread.isAlive()) monitorErc20ColdWalletThread.start();

        LoggerUtils.logInfo(logger, "启动热钱包余额不足提醒服务线程!");
        MonitorErc20HotWalletThread monitorErc20HotWalletServiceThread = context.getBean(MonitorErc20HotWalletThread.class);
        Thread monitorErc20HotWalletThread = new Thread(monitorErc20HotWalletServiceThread);
        if (!monitorErc20HotWalletThread.isAlive()) monitorErc20HotWalletThread.start();

        LoggerUtils.logInfo(logger, "启动归集费用余额不足提醒服务线程!");
        MonitorErc20CollectFeeThread monitorErc20CollectFeeServiceThread = context.getBean(MonitorErc20CollectFeeThread.class);
        Thread monitorErc20CollectFeeThread = new Thread(monitorErc20CollectFeeServiceThread);
        if (!monitorErc20CollectFeeThread.isAlive()) monitorErc20CollectFeeThread.start();

        SimpleDateFormat endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LoggerUtils.logInfo(logger, "监控服务线程启动成功--- " + endDate.format(new Date()));
        LoggerUtils.logInfo(logger, "==========================================");
        SignalHandler handler = sig -> {
            LoggerUtils.logError(logger, "recieved kill {} Singnal,prepare exit..", sig.getName());
            monitorConfigScanServiceThread.setRunning(false);
            monitorAcctFundCurServiceThread.setRunning(false);
            monitorMarginServiceThread.setRunning(false);
            monitorInternalPlatFundCurServiceThread.setRunning(false);
            monitorDigitalCoinServiceThread.setRunning(false);
            monitorCashCoinServiceThread.setRunning(false);
            monitorBlockNumServiceThread.setRunning(false);
            monitorErc20BalServiceThread.setRunning(false);
            monitorErc20ColdWalletServiceThread.setRunning(false);
            monitorErc20HotWalletServiceThread.setRunning(false);
            monitorErc20CollectFeeServiceThread.setRunning(false);
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

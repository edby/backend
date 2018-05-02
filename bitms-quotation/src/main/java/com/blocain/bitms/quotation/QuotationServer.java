package com.blocain.bitms.quotation;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.blocain.bitms.quotation.config.InQuotationConfig;
import com.blocain.bitms.quotation.config.QuotationConfigLoader;
import com.blocain.bitms.quotation.thread.*;
import com.blocain.bitms.tools.utils.LoggerUtils;

import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * 行情服务启动器
 * <p>File：QuotationServer.java</p>
 * <p>Title: QuotationServer</p>
 * <p>Description: QuotationServer</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class QuotationServer
{
    private static final Logger logger = LoggerFactory.getLogger(QuotationServer.class);
    
    public static void main(String[] args) throws IOException
    {
        if (args.length == 0 || args.length > 1)
        {
            logger.error("FATAL ERROR: no parameter or more than one parameter confile give!");
            logger.error("java QuotationServer configFile");
            logger.error("example:java QuotationServer btc2usdx.properties");
            return;
        }
        else
        {
            // 加载配置文件
            QuotationConfigLoader.loadProp(args[0]);
            // 动态替换查询的SQL语句
            QuotationConfigLoader.preProcessSQLByConfig();
        }
        logger.info("starting with quotationServer config file: {}", args[0]);
        try
        {
            start();
        }
        catch (Exception e)
        {
            logger.error("FATAL ERROR:read quotationServer config file fail:{}", e.getMessage(), e);
            return;
        }
    }
    
    public static void start() throws Exception
    {
        String[] config = new String[]{"spring.xml", "spring-jdbc.xml", "spring-jedis.xml", "spring-mongodb.xml", "spring-mq.xml"};
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(config);
        LoggerUtils.logInfo(logger, "QuotationServer start success!");
        // 最新撮合行情数据推送线程
        RtQuotationThread rtQuotation_ServiceThread = context.getBean(RtQuotationThread.class);
        // 成交流水数据推送线程
        TransactionThread transaction_ServiceThread = context.getBean(TransactionThread.class);
        // 深度行情推送线程
        DeepPriceThread deepPrice_ServiceThread = context.getBean(DeepPriceThread.class);
        // K线数据生成线程
        KLineCreateThread kLine_create_ServiceThread = context.getBean(KLineCreateThread.class);
        // 1分钟线K线数据推送线程
        KLine1MThread kLine_1Min_ServiceThread = context.getBean(KLine1MThread.class);
        // 5分钟线K线数据推送线程
        KLine5MThread kLine_5Min_ServiceThread = context.getBean(KLine5MThread.class);
        // 15分钟线K线数据推送线程
        KLine15MThread kLine_15Min_ServiceThread = context.getBean(KLine15MThread.class);
        // 30分钟线K线数据推送线程
        KLine30MThread kLine_30Min_ServiceThread = context.getBean(KLine30MThread.class);
        // 60分钟线K线数据推送线程
        KLine1HThread kLine_60Min_ServiceThread = context.getBean(KLine1HThread.class);
        // 日线K线数据推送线程
        KLine1DThread kLine_1Day_ServiceThread = context.getBean(KLine1DThread.class);
        // 全行情推送线程
        AllRtQuotationThread allRtQuotationServiceThread = context.getBean(AllRtQuotationThread.class);
        LoggerUtils.logInfo(logger, "启动深度行情数据服务线程!");
        Thread deepPrice_Thread = new Thread(deepPrice_ServiceThread);
        if (!deepPrice_Thread.isAlive())
        {
            deepPrice_Thread.start();
            Thread.sleep(500);
        }
        LoggerUtils.logInfo(logger, "启动最新撮合行情服务线程!");
        Thread rtQuotation_Thread = new Thread(rtQuotation_ServiceThread);
        if (!rtQuotation_Thread.isAlive())
        {
            rtQuotation_Thread.start();
            Thread.sleep(500);
        }
        LoggerUtils.logInfo(logger, "启动成交流水服务线程!");
        Thread transactionThread = new Thread(transaction_ServiceThread);
        if (!transactionThread.isAlive())
        {
            transactionThread.start();
            Thread.sleep(500);
        }
        LoggerUtils.logInfo(logger, "启动K线数据生成服务线程!");
        Thread kLine_create_Thread = new Thread(kLine_create_ServiceThread);
        if (!kLine_create_Thread.isAlive())
        {
            kLine_create_Thread.start();
            Thread.sleep(500);
        }
        LoggerUtils.logInfo(logger, "启动1分钟K线服务线程!");
        Thread kLine_1Min_Thread = new Thread(kLine_1Min_ServiceThread);
        if (!kLine_1Min_Thread.isAlive())
        {
            kLine_1Min_Thread.start();
            Thread.sleep(500);
        }
        LoggerUtils.logInfo(logger, "启动5分钟K线服务线程!");
        Thread kLine_5Min_Thread = new Thread(kLine_5Min_ServiceThread);
        if (!kLine_5Min_Thread.isAlive())
        {
            kLine_5Min_Thread.start();
            Thread.sleep(500);
        }
        LoggerUtils.logInfo(logger, "启动15分钟K线服务线程!");
        Thread kLine_15Min_Thread = new Thread(kLine_15Min_ServiceThread);
        if (!kLine_15Min_Thread.isAlive())
        {
            kLine_15Min_Thread.start();
            Thread.sleep(500);
        }
        LoggerUtils.logInfo(logger, "启动30分钟K线服务线程!");
        Thread kLine_30Min_Thread = new Thread(kLine_30Min_ServiceThread);
        if (!kLine_30Min_Thread.isAlive())
        {
            kLine_30Min_Thread.start();
            Thread.sleep(500);
        }
        LoggerUtils.logInfo(logger, "启动1小时K线服务线程!");
        Thread kLine_60Min_Thread = new Thread(kLine_60Min_ServiceThread);
        if (!kLine_60Min_Thread.isAlive())
        {
            kLine_60Min_Thread.start();
            Thread.sleep(500);
        }
        LoggerUtils.logInfo(logger, "启动日K线服务线程!");
        Thread kLine_1Day_Thread = new Thread(kLine_1Day_ServiceThread);
        if (!kLine_1Day_Thread.isAlive())
        {
            kLine_1Day_Thread.start();
            Thread.sleep(500);
        }
        LoggerUtils.logInfo(logger, "启动全行情推送服务线程!");
        Thread allRtQuotationThread = new Thread(allRtQuotationServiceThread);
        if ((!allRtQuotationThread.isAlive()) && "open".equals(InQuotationConfig.QUOTATION_SWITCH))
        {
            allRtQuotationThread.start();
            Thread.sleep(500);
        }
        // 使用sun提供的私有api处理kill信号停止所有线程
        SignalHandler handler = sig -> {
            LoggerUtils.logError(logger, "recieved kill {} Singnal,prepare exit..", sig.getName());
            transaction_ServiceThread.setRunning(false);
            deepPrice_ServiceThread.setRunning(false);
            kLine_create_ServiceThread.setRunning(false);
            kLine_1Min_ServiceThread.setRunning(false);
            kLine_5Min_ServiceThread.setRunning(false);
            kLine_15Min_ServiceThread.setRunning(false);
            kLine_30Min_ServiceThread.setRunning(false);
            kLine_60Min_ServiceThread.setRunning(false);
            kLine_1Day_ServiceThread.setRunning(false);
            rtQuotation_ServiceThread.setRunning(false);
            allRtQuotationServiceThread.setRunning(false);
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

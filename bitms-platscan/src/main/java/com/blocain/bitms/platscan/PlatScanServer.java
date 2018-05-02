package com.blocain.bitms.platscan;

import java.io.IOException;

import com.blocain.bitms.platscan.config.PlatScanConfigLoader;
import com.blocain.bitms.platscan.thread.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import sun.misc.Signal;
import sun.misc.SignalHandler;

import com.blocain.bitms.tools.utils.LoggerUtils;

/**
 * 平台扫描服务启动器
 * <p>File：PlatScanServer.java</p>
 * <p>Title: PlatScanServer</p>
 * <p>Description: PlatScanServer</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class PlatScanServer
{
    private static final Logger logger = LoggerFactory.getLogger(PlatScanServer.class);

    public static void main(String[] args) throws IOException
    {
        String[] config = new String[]{"spring.xml", "spring-jdbc.xml", "spring-jedis.xml", "spring-mongodb.xml"};
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(config);

        //加载平台扫描配置文件
        PlatScanConfigLoader.loadProp();

        FundScanThread fundScanServiceThread = context.getBean(FundScanThread.class);
        Thread fundScanThread = new Thread(fundScanServiceThread);

        DebitAssetThread debitAssetServiceThread = context.getBean(DebitAssetThread.class);
        Thread debitAssetThread = new Thread(debitAssetServiceThread);

        ClosePositionThread closePositionServiceThread = context.getBean(ClosePositionThread.class);
        Thread closePositionThread = new Thread(closePositionServiceThread);

        QuotationThread quotationServiceThread = context.getBean(QuotationThread.class);
        Thread quotationThread = new Thread(quotationServiceThread);

        if (!fundScanThread.isAlive()) fundScanThread.start();
        if (!debitAssetThread.isAlive()) debitAssetThread.start();
        if (!closePositionThread.isAlive()) closePositionThread.start();
        if (!quotationThread.isAlive()) quotationThread.start();

        SignalHandler handler = sig -> {
            LoggerUtils.logError(logger, "recieved kill {} Singnal,prepare exit..", sig.getName());

            fundScanServiceThread.setRunning(false);
            debitAssetServiceThread.setRunning(false);
            closePositionServiceThread.setRunning(false);
            quotationServiceThread.setRunning(false);
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

package com.blocain.bitms.monitor.config;

import com.blocain.bitms.tools.utils.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 撮合引擎配置文件装载工具
 *
 * @author NoOne
 */
public class MonitorConfigLoader
{
    public static final Logger logger     = LoggerFactory.getLogger(MonitorConfigLoader.class);

    public static final String CONFIGPATH = "monitor.properties";

    public static void loadProp() throws IOException
    {
        PropertiesUtils properties = new PropertiesUtils(CONFIGPATH);

        //业务品种列表
        MonitorConfig.BIZ_CATEGORYS = properties.getProperty("biz.categorys");

        //监控保证金休眠
        MonitorConfig.SLEEP_MONITOR_MARGIN = Long.valueOf(properties.getProperty("sleep.monitor.margin"));

        // 监控撮合总账休眠时间
        MonitorConfig.SLEEP_MONITOR_MATCHFUNDCUR = Long.valueOf(properties.getProperty("sleep.monitor.matchFundCur"));

        // 监控撮合总账休眠时间
        MonitorConfig.SLEEP_MONITOR_ACCTFUNDCUR = Long.valueOf(properties.getProperty("sleep.monitor.accountFundCur"));
    }
}

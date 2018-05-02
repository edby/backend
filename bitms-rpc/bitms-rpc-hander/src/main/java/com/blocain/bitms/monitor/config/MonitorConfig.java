package com.blocain.bitms.monitor.config;

public abstract class MonitorConfig
{
    //业务品种列表
    public static String BIZ_CATEGORYS;

    // 监控保证金休眠时间
    public static Long   SLEEP_MONITOR_MARGIN;

    // 监控撮合总账休眠时间
    public static Long   SLEEP_MONITOR_MATCHFUNDCUR;

    // 账户级别资金流水监控休眠时间
    public static Long   SLEEP_MONITOR_ACCTFUNDCUR;

}

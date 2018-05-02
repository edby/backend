package com.blocain.bitms.archive.config;

/**
 * 归档配置表
 */
public class ArchiveConfig
{
    //K线数据归档提前量
    public static String KLINE_ARCHIVE_PREACTTINE       = "3";

    //外部指数行情提前量
    public static String QUOTATION_ARCHIVE_PREACTTINE   = "2";

    //交易流水提前量
    public static String TRADE_ARCHIVE_PREACTTINE       = "30";

    //资金流水提前量
    public static String FUNDCURRENT_ARCHIVE_PREACTTINE = "30";

    //交易流水保留条数
    public static String TRADE_ARCHIVE_ROWS             = "20";

    //提前量单位：小时
    public static String ARCHIVE_PREACTUNIT_HOUR        = "hour";

    //提前量单位：分钟
    public static String ARCHIVE_PREACTUNIT_MIN         = "minute";

    //提前量单位：月
    public static String ARCHIVE_PREACTUNIT_MONTH       = "month";
}

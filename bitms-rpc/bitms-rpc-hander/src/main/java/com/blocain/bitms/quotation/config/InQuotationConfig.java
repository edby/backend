package com.blocain.bitms.quotation.config;

import java.math.BigDecimal;

public class InQuotationConfig
{
    // 服务名称
    public static String     SERVER_NAME;
    
    // 业务表 委托表
    public static String     TBL_ENTRUST;
    
    // 业务表 成交表
    public static String     TBL_REALDEAL;
    
    // 业务表 成交历史表
    public static String     TBL_REALDEALHIS;
    
    public static String     TBL_KLINE;
    
    // 行情渠道
    public static String     QUOTATION_CHANNEL;
    
    // 转换标志
    public static String     BIZ_CONVERT;
    
    // 业务标的
    public static String     BIZ_TARGET;
    
    // 业务品种
    public static String     BIZ_CATEGORY;
    
    // 价格下限默认值
    public static BigDecimal DOWNRATEDEFAULT;
    
    // 价格上限默认值
    public static BigDecimal UPRATEDEFAULT;

    // WS通知主题
    public static String     PUSH_TOPIC;
    
    // 消息主题
    // 1分钟 K线
    public static String     TOPIC_KLINE_1M;
    
    // 5分钟 K线
    public static String     TOPIC_KLINE_5M;
    
    // 15分钟 K线
    public static String     TOPIC_KLINE_15M;
    
    // 30分钟 K线
    public static String     TOPIC_KLINE_30M;
    
    // 时钟 K线
    public static String     TOPIC_KLINE_HOUR;
    
    // 日线 K线
    public static String     TOPIC_KLINE_DAY;
    
    // 周线 K线
    public static String     TOPIC_KLINE_WEEK;
    
    // 月线 K线
    public static String     TOPIC_KLINE_MONTH;
    
    // 委托盘口深度行情
    public static String     TOPIC_ENTRUST_DEEPPRICE;
    
    // 撮合成交流水
    public static String     TOPIC_REALDEAL_TRANSACTION;
    
    // 最新撮合成交行情
    public static String     TOPIC_RTQUOTATION_PRICE;
    
    // K线 1分钟线 取6小时的交易数据
    public static Integer    KLINE1M_RANGE;
    
    // K线 5分钟线 取6小时的交易数据
    public static Integer    KLINE5M_RANGE;
    
    // K线 15分钟线 取20小时的交易数据
    public static Integer    KLINE15M_RANGE;
    
    // K线 30分钟线 取42小时的交易数据
    public static Integer    KLINE30M_RANGE;
    
    // K线 小时线 取60小时的交易数据
    public static Integer    KLINE1H_RANGE;
    
    // K线 日线 取60小时的交易数据
    public static Integer    KLINE1D_RANGE;
    
    // 交易流水每次推送数量
    public static Integer    REALDEAL_NUM;
    
    // 买卖盘口委托数量
    public static String     ENTRUST_NUM;
    
    // 盘口价格位数
    public static Integer    QUOTATION_PRICE_DIGIT;
    
    // 盘口挂单数量保留小数位数
    public static Integer    QUOTATION_AMT_DIGIT;
    
    // 盘口累计保留小数位数
    public static Integer    QUOTATION_ACCUMULATEBAL_DIGIT;
    
    // 价格格式
    public static String     QUOTATION_PRICE_FORMAT;
    
    // 数量格式
    public static String     QUOTATION_AMT_FORMAT;

    // 盘口买价排序规则
    public static String     ENTRUST_BUY_SORT;

    //盘口卖价排序规则
    public static String     ENTRUST_SELL_SORT;
    // 盘口挂单数量保留小数位数
    
    // 买卖盘口深度
    public static Integer    ENTRUST_DEEPLEVEL;
    
    // K线休眠时间 1分钟
    public static Long       THREAD_SLEEP_KLINE_1M;
    
    public static Long       THREAD_SLEEP_KLINE_5M;
    
    public static Long       THREAD_SLEEP_KLINE_15M;
    
    public static Long       THREAD_SLEEP_KLINE_30M;
    
    public static Long       THREAD_SLEEP_KLINE_1H;
    
    public static Long       THREAD_SLEEP_KLINE_1D;
    
    // K线数据生成休眠时间 1分钟
    public static Long       THREAD_SLEEP_KLINE_CREATE;
    
    // 深度行情休眠时间 1秒
    public static Long       THREAD_SLEEP_DEEPPRICE;
    
    // 交易流水休眠时间 1秒
    public static Long       THREAD_SLEEP_REALDEAL;
    
    // 最新撮合行情休眠时间 1秒
    public static Long       THREAD_SLEEP_RTQUOTATION;
    
    // 最新全行情推送休眠时间
    public static Integer    THREAD_SLEEP_ALLRTQUOTATION;
    
    // 实时行情推送列表
    public static String     QUOTATION_STOCKS;
    
    // 全行情推送开关
    public static String     QUOTATION_SWITCH;
    
    // 全行情主题
    public static String     TOPIC_ALLQUOTATION;
}

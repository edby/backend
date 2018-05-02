package com.blocain.bitms.quotation.consts;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.blocain.bitms.quotation.entity.KLine;
import com.blocain.bitms.quotation.entity.KLineEntity;

/**
 * InQuotationConsts Introduce
 * <p>File：InQuotationConsts.java</p>
 * <p>Title: InQuotationConsts</p>
 * <p>Description: InQuotationConsts</p>
 * <p>Copyright: Copyright (c) 2017/9/20</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class InQuotationConsts
{
    // 重置K线数据
    public static final String                           RELOAD_KLINEDATA           = "doReload";
    
    // K 线实时数据
    public static final String                           KLINE_RTKLINEENTITY        = "KLINE|RTKLINEENTITY|1Min";
    
    public static final String                           KLINE_RTKLINEENTITY_5M     = "KLINE|RTKLINEENTITY|5Min";
    
    public static final String                           KLINE_RTKLINEENTITY_15M    = "KLINE|RTKLINEENTITY|15Min";
    
    public static final String                           KLINE_RTKLINEENTITY_30M    = "KLINE|RTKLINEENTITY|30Min";
    
    public static final String                           KLINE_RTKLINEENTITY_1H     = "KLINE|RTKLINEENTITY|1Hour";
    
    public static final String                           KLINE_RTKLINEENTITY_1D     = "KLINE|RTKLINEENTITY|1Day";
    
    // 实际真实1分钟统计数据
    public static final String                           KLINE_KLINEENTITY_LAST_1M  = "KLINE|LASTKLINEENTITY";
    
    // 消息类型
    // 消息类型_K 线
    public static final String                           MESSAGE_TYPE_KLINE         = "K_LINE";
    
    // 消息类型_深度行情
    public static final String                           MESSAGE_TYPE_DEEPPRICE     = "DEEP_PRICE";
    
    // 消息类型_成交流水
    public static final String                           MESSAGE_TYPE_REALDEAL      = "REAL_DEAL";
    
    // 消息类型_最新撮合行情
    public static final String                           MESSAGE_TYPE_RTQUOTATION   = "RTQUOTATION";
    
    // K线时间类型 查询起始时间
    public static final String                           KLINE_TIMETYPE_INSERT      = "insert";
    
    // K线时间类型 推送结束时间
    public static final String                           KLINE_TIMETYPE_PUSH        = "push";
    
    // K线时间类型 查询起始时间
    public static final String                           KLINE_TIMETYPE_LIST        = "list";
    
    // K线时间类型 推送结束时间
    public static final String                           KLINE_TIMETYPE_LAST        = "last";
    
    // 最新撮合行情 涨跌幅方向
    public static final String                           RTQUOTATION_RANGE_UP       = "UP";
    
    // 最新撮合行情 涨跌幅方向
    public static final String                           RTQUOTATION_RANGE_DOWN     = "DOWN";
    
    public static final String                           RTQUOTATION_RANGE_ZERO     = "ZERO";
    
    // K线时间类型 显示时间
    public static final String                           KLINE_TIMETYPE_DISPLAY     = "dispalytime";
    
    // K线时间类型 查询起始时间
    public static final String                           KLINE_TIMETYPE_QUERY_START = "queryStart";
    
    // K线时间类型 查询结束时间
    public static final String                           KLINE_TIMETYPE_QUERY_END   = "queryEnd";
    
    // K线时间类型 推送起始时间
    public static final String                           KLINE_TIMETYPE_PUSH_START  = "pushStart";
    
    // K线时间类型 推送结束时间
    public static final String                           KLINE_TIMETYPE_PUSH_END    = "pushEnd";
    
    // K线推送时间计算方式 全局添加
    public static final String                           KLINE_TIME_CALTYPE_ADD     = "add";
    
    // K线推送时间计算方式 追加
    public static final String                           KLINE_TIME_CALTYPE_APPEND  = "append";
    
    public static ConcurrentHashMap<String, Object>      CACHE_MAP                  = new ConcurrentHashMap<String, Object>();
    
    public static ConcurrentHashMap<String, KLineEntity> CAMHE_RTKLINE_MAP          = new ConcurrentHashMap<String, KLineEntity>();
    
    public static ConcurrentHashMap<String, List<KLine>> CAMHE_KLINELIST_MAP        = new ConcurrentHashMap<String, List<KLine>>();
    
    public static ConcurrentHashMap<String, Long>        PUSHTIME_MAP               = new ConcurrentHashMap<String, Long>();
    
    public static ConcurrentHashMap<String, String>      CONTENT_MAP                = new ConcurrentHashMap<String, String>();
    
    // 缓存上次推送时间
    public static final String                           PUSH_TIME_LAST             = "quotation.pushtime.last.";
}

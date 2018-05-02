package com.blocain.bitms.trade.quotation.consts;

import java.util.Set;

import org.springframework.web.socket.WebSocketSession;

import com.google.common.collect.Sets;

/**
 * WebScoket连接会话
 * <p>File：SessionConst.java</p>
 * <p>Title: SessionConst</p>
 * <p>Description: SessionConst</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class SessionConst
{
    // 日线会话连接
    private Set<WebSocketSession> kLineDaySessions           = Sets.newHashSet();
    
    // 周线会话连接
    private Set<WebSocketSession> kLineWeekSessions          = Sets.newHashSet();
    
    // 月线会话连接
    private Set<WebSocketSession> kLineMonthSessions         = Sets.newHashSet();
    
    // 60分线会话连接
    private Set<WebSocketSession> kLineHourSessions          = Sets.newHashSet();
    
    // 30分线会话连接
    private Set<WebSocketSession> kLine30MinSessions         = Sets.newHashSet();
    
    // 15分线会话连接
    private Set<WebSocketSession> kLine15MinSessions         = Sets.newHashSet();
    
    // 5分线会话连接
    private Set<WebSocketSession> kLine5MinSessions          = Sets.newHashSet();
    
    // 1分线会话连接
    private Set<WebSocketSession> kLine1MinSessions          = Sets.newHashSet();
    
    // 委托深度行情会话连接
    private Set<WebSocketSession> deepPriceSessions          = Sets.newHashSet();
    
    // 成交流水会话连接
    private Set<WebSocketSession> realDealSessions           = Sets.newHashSet();
    
    // 行情会话连接
    private Set<WebSocketSession> rtQuotationSessions        = Sets.newHashSet();
    
    // 全行情会话连接
    private Set<WebSocketSession> allRtQuotationSessions     = Sets.newHashSet();
    
    // 日线会话连接
    private Set<WebSocketSession> kLineDaySessions_tmp       = Sets.newHashSet();
    
    // 周线会话连接
    private Set<WebSocketSession> kLineWeekSessions_tmp      = Sets.newHashSet();
    
    // 月线会话连接
    private Set<WebSocketSession> kLineMonthSessions_tmp     = Sets.newHashSet();
    
    // 60分线会话连接
    private Set<WebSocketSession> kLineHourSessions_tmp      = Sets.newHashSet();
    
    // 30分线会话连接
    private Set<WebSocketSession> kLine30MinSessions_tmp     = Sets.newHashSet();
    
    // 15分线会话连接
    private Set<WebSocketSession> kLine15MinSessions_tmp     = Sets.newHashSet();
    
    // 5分线会话连接
    private Set<WebSocketSession> kLine5MinSessions_tmp      = Sets.newHashSet();
    
    // 1分线会话连接
    private Set<WebSocketSession> kLine1MinSessions_tmp      = Sets.newHashSet();
    
    // 委托深度行情会话连接
    private Set<WebSocketSession> deepPriceSessions_tmp      = Sets.newHashSet();
    
    // 成交流水会话连接
    private Set<WebSocketSession> realDealSessions_tmp       = Sets.newHashSet();
    
    // 行情会话连接
    private Set<WebSocketSession> rtQuotationSessions_tmp    = Sets.newHashSet();
    
    // 全行情会话连接
    private Set<WebSocketSession> allRtQuotationSessions_tmp = Sets.newHashSet();
    
    private String                KLINE_1MIN_CONTENT         = "";
    
    private String                KLINE_5MIN_CONTENT         = "";
    
    private String                KLINE_15MIN_CONTENT        = "";
    
    private String                KLINE_30MIN_CONTENT        = "";
    
    private String                KLINE_DAY_CONTENT          = "";
    
    private String                KLINE_HOUR_CONTENT         = "";
    
    private String                KLINE_WEEK_CONTENT         = "";
    
    private String                KLINE_MONTH_CONTENT        = "";
    
    private String                REAL_DEAL_CONTENT          = "";
    
    private String                DEEP_PRICE_CONTENT         = "";
    
    private String                RT_QUOTATION_CONTENT       = "";
    
    private String                ALL_QUOTATION_CONTENT      = "";
    
    public Set<WebSocketSession> getkLineDaySessions_tmp()
    {
        return kLineDaySessions_tmp;
    }
    
    public Set<WebSocketSession> getkLineWeekSessions_tmp()
    {
        return kLineWeekSessions_tmp;
    }
    
    public Set<WebSocketSession> getkLineMonthSessions_tmp()
    {
        return kLineMonthSessions_tmp;
    }
    
    public Set<WebSocketSession> getkLineHourSessions_tmp()
    {
        return kLineHourSessions_tmp;
    }
    
    public Set<WebSocketSession> getkLine30MinSessions_tmp()
    {
        return kLine30MinSessions_tmp;
    }
    
    public Set<WebSocketSession> getkLine15MinSessions_tmp()
    {
        return kLine15MinSessions_tmp;
    }
    
    public Set<WebSocketSession> getkLine5MinSessions_tmp()
    {
        return kLine5MinSessions_tmp;
    }
    
    public Set<WebSocketSession> getkLine1MinSessions_tmp()
    {
        return kLine1MinSessions_tmp;
    }
    
    public Set<WebSocketSession> getDeepPriceSessions_tmp()
    {
        return deepPriceSessions_tmp;
    }
    
    public Set<WebSocketSession> getRealDealSessions_tmp()
    {
        return realDealSessions_tmp;
    }
    
    public Set<WebSocketSession> getRtQuotationSessions_tmp()
    {
        return rtQuotationSessions_tmp;
    }
    
    public Set<WebSocketSession> getAllRtQuotationSessions_tmp()
    {
        return allRtQuotationSessions_tmp;
    }
    
    public Set<WebSocketSession> getkLineDaySessions()
    {
        return kLineDaySessions;
    }
    
    public Set<WebSocketSession> getkLineWeekSessions()
    {
        return kLineWeekSessions;
    }
    
    public Set<WebSocketSession> getkLineMonthSessions()
    {
        return kLineMonthSessions;
    }
    
    public Set<WebSocketSession> getkLineHourSessions()
    {
        return kLineHourSessions;
    }
    
    public Set<WebSocketSession> getkLine30MinSessions()
    {
        return kLine30MinSessions;
    }
    
    public Set<WebSocketSession> getkLine15MinSessions()
    {
        return kLine15MinSessions;
    }
    
    public Set<WebSocketSession> getkLine5MinSessions()
    {
        return kLine5MinSessions;
    }
    
    public Set<WebSocketSession> getkLine1MinSessions()
    {
        return kLine1MinSessions;
    }
    
    public Set<WebSocketSession> getDeepPriceSessions()
    {
        return deepPriceSessions;
    }
    
    public Set<WebSocketSession> getRealDealSessions()
    {
        return realDealSessions;
    }
    
    public Set<WebSocketSession> getRtQuotationSessions()
    {
        return rtQuotationSessions;
    }
    
    public Set<WebSocketSession> getAllRtQuotationSessions()
    {
        return allRtQuotationSessions;
    }
    
    public String getKline1minContent()
    {
        return KLINE_1MIN_CONTENT;
    }
    
    public void setKline1minContent(String kline1minContent)
    {
        KLINE_1MIN_CONTENT = kline1minContent;
    }
    
    public String getKline5minContent()
    {
        return KLINE_5MIN_CONTENT;
    }
    
    public void setKline5minContent(String kline5minContent)
    {
        KLINE_5MIN_CONTENT = kline5minContent;
    }
    
    public String getKline15minContent()
    {
        return KLINE_15MIN_CONTENT;
    }
    
    public void setKline15minContent(String kline15minContent)
    {
        KLINE_15MIN_CONTENT = kline15minContent;
    }
    
    public String getKline30minContent()
    {
        return KLINE_30MIN_CONTENT;
    }
    
    public void setKline30minContent(String kline30minContent)
    {
        KLINE_30MIN_CONTENT = kline30minContent;
    }
    
    public String getKlineDayContent()
    {
        return KLINE_DAY_CONTENT;
    }
    
    public void setKlineDayContent(String klineDayContent)
    {
        KLINE_DAY_CONTENT = klineDayContent;
    }
    
    public String getKlineHourContent()
    {
        return KLINE_HOUR_CONTENT;
    }
    
    public void setKlineHourContent(String klineHourContent)
    {
        KLINE_HOUR_CONTENT = klineHourContent;
    }
    
    public String getKlineWeekContent()
    {
        return KLINE_WEEK_CONTENT;
    }
    
    public void setKlineWeekContent(String klineWeekContent)
    {
        KLINE_WEEK_CONTENT = klineWeekContent;
    }
    
    public String getKlineMonthContent()
    {
        return KLINE_MONTH_CONTENT;
    }
    
    public void setKlineMonthContent(String klineMonthContent)
    {
        KLINE_MONTH_CONTENT = klineMonthContent;
    }
    
    public String getRealDealContent()
    {
        return REAL_DEAL_CONTENT;
    }
    
    public void setRealDealContent(String realDealContent)
    {
        REAL_DEAL_CONTENT = realDealContent;
    }
    
    public String getRtQuotationContent()
    {
        return RT_QUOTATION_CONTENT;
    }
    
    public String getDeepPriceContent()
    {
        return DEEP_PRICE_CONTENT;
    }
    
    public void setDeepPriceContent(String DEEP_PRICE_CONTENT)
    {
        this.DEEP_PRICE_CONTENT = DEEP_PRICE_CONTENT;
    }
    
    public void setRtQuotationContent(String rtQuotationContent)
    {
        RT_QUOTATION_CONTENT = rtQuotationContent;
    }
    
    public String getAllQuotationContent()
    {
        return ALL_QUOTATION_CONTENT;
    }
    
    public void setAllQuotationContent(String allQuotationContent)
    {
        ALL_QUOTATION_CONTENT = allQuotationContent;
    }
}

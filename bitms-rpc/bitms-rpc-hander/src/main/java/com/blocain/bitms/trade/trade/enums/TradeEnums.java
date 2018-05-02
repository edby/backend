/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.trade.enums;

import com.blocain.bitms.tools.bean.BusinessEnums;

/**
 * <p>File：TradeEnums.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月10日 上午10:44:38</p>
 * <p>Company: BloCain</p>
 * @author sunbiao
 * @version 1.0
 */
public enum TradeEnums implements BusinessEnums
{
    TRADE_TYPE_PUSHTRADE("pushTarde", "trade.type.pushTarde"), // Push交易
    TRADE_TYPE_FAIRTRADE("fairTrade", "trade.type.fairTrade"), // 集市交易
    TRADE_TYPE_QUOTATIONTRADE("quotationTrade", "trade.type.quotationTrade"), // 报价交易
    TRADE_TYPE_MATCHTRADE("matchTrade", "trade.type.matchTrade"), // 撮合交易
    TRADE_TYPE_CLOSEPOSITIONTRADE("closePositionTrade", "trade.type.closePositionTrade"), // 爆仓强平交易
    TRADE_TYPE_SETTLEMENTTRADE("settlementTrade", "trade.type.settlementTrade"), // 交割交易

    ENTRUST_DEAL_DIRECT_SPOT_BUY("spotBuy", "entrust.deal.direct.spotBuy"), // 现货买入
    ENTRUST_DEAL_DIRECT_SPOT_SELL("spotSell", "entrust.deal.direct.spotSell"), // 现货卖出
    ENTRUST_DEAL_DIRECT_FUTURES_LONGOPEN("futuresLongOpen", "entrust.deal.direct.futuresLongOpen"), // 期货多头开仓

    ENTRUST_STATUS_PENDING("pending", "entrust.status.pending"), // 待成交
    ENTRUST_STATUS_WITHDRAWED("withdrawed", "entrust.status.withdrawed"), // 已撤单
    ENTRUST_STATUS_PARTIALACCEPTED("partiaAccepted", "entrust.status.partiaAccepted"), // 已部分接受
    ENTRUST_STATUS_ALLACCEPTED("allAccepted", "entrust.status.allAccepted"), // 已全部接受
    ENTRUST_STATUS_REFUSED("refused", "entrust.status.refused"), // 已拒绝
    ENTRUST_STATUS_EXPIRED("expired", "entrust.status.expired"), // 已过期

    DEAL_STATUS_INIT("init", "deal.status.init"), // 未进入撮合
    DEAL_STATUS_NODEAL("noDeal", "deal.status.noDeal"), // 未成交
    DEAL_STATUS_PARTIALDEAL("partialDeal", "deal.status.partialDeal"), // 部分成交
    DEAL_STATUS_ALLDEAL("allDeal", "deal.status.allDeal"), // 全部成交
    DEAL_STATUS_WITHDRAW("withDrawed", "deal.status.withDrawed"), // 已撤单
    DEAL_STATUS_ABNORMAL("abnormal", "deal.status.abnormal"), // 异常委托

    DEAL_STATUS_EFFECTIVE("effective","deal.status.effective"),//有效
    DEAL_STATUS_INVALID("invalid","deal.status.invalid"),//无效

    ENTRUST_SUCCESS("entrustSuccess", "entrustSuccess"), // 操作成功
    ENTRUST_FAIL("entrustFail", "entrustFail"), // 操作失败
    ENTRUST_WITHDRAW_SUCCESS("entrustWithdrawSuccess", "entrustWithdrawSuccess"), // 操作成功
    ENTRUST_WITHDRAW_FAIL("entrustWithdrawFail", "entrustWithdrawFail"), // 操作失败
    ENTRUST_REJECT_SUCCESS("entrustRejectSuccess", "entrustRejectSuccess"), // 操作成功
    ENTRUST_REJECT_FAIL("entrustRejectFail", "entrustRejectFail"), // 操作失败
    ENTRUST_REALDEAL_SUCCESS("entrustRealdealSuccess", "entrustRealdealSuccess"), // 操作成功
    ENTRUST_REALDEAL_FAIL("entrustRealdealFail", "entrustRealdealFail"), // 操作失败
    SYS_PARAMETER_SYSTEM_NAME_TRADE("TRADE", "TRADE"), // 系统名称=交易系统

    TRADE_SYS_PARAMETER_PUSHTRADE_SELL_BMS_ENTRUST("PushTradeSellBMSEntrust", "PushTradeSellBMSEntrust"), //push交易卖出BMS委托收取BTC手续费比例
    TRADE_SYS_PARAMETER_PUSHTRADE_BUY_BMS_NOENTRUST_DEAL("PushTradeBuyBMSNoEntrustDeal", "PushTradeBuyBMSNoEntrustDeal"), //push交易买入BMS无委托成交收取BMS手续费比例
    TRADE_SYS_PARAMETER_FAIRTRADE_SELL_BMS_ENTRUST("FairTradeSellBMSEntrust", "FairTradeSellBMSEntrust"), //集市交易卖出BMS委托收取BTC手续费比例
    TRADE_SYS_PARAMETER_FAIRTRADE_BUY_BMS_NOENTRUST_DEAL("FairTradeBuyBMSNoEntrustDeal", "FairTradeBuyBMSNoEntrustDeal"), //集市交易买入BMS无委托成交收取BMS手续费比例
    TRADE_SYS_PARAMETER_FAIRTRADE_BUY_BMS_ENTRUST("FairTradeBuyBMSEntrust", "FairTradeBuyBMSEntrust"), //集市交易买入BMS委托收取BMS手续费比例
    TRADE_SYS_PARAMETER_FAIRTRADE_SELL_BMS_NOENTRUST_DEAL("FairTradeSellBMSNoEntrustDeal", "FairTradeSellBMSNoEntrustDeal"), //集市交易卖出BMS无委托成交收取BTC手续费比例

    PUSH_ENTRUST_DAY_QUOTA_UPPER("PushEntrustDayQuotaUpper", "PushEntrustDayQuotaUpper"), //当日PUSH委托次数上限
    PUSH_ENTRUST_MAX_NUM("PushEntrustMaxNum", "PushEntrustMaxNum"), //PUSH委托交易单账户未成交委托总BMS数量上限
    FAIR_ENTRUST_MAX_NUM("FairEntrustMaxNum", "FairEntrustMaxNum"), //集市委托交易单账户未成交委托总BMS数量上限
    MATCH_ENTRUST_MAX_NUM("MatchEntrustMaxNum", "MatchEntrustMaxNum"), //撮合交易单账户未成交委托总USDX数量上限
    PUSH_ENTRUST_MAX_PRICE("PushEntrustMaxPrice", "PushEntrustMaxPrice"), //PUSH委托交易最大价格
    PUSH_ENTRUST_MIN_PRICE("PushEntrustMinPrice", "PushEntrustMinPrice"), //PUSH委托交易最小价格
    FAIR_ENTRUST_MAX_PRICE("FairEntrustMaxPrice", "FairEntrustMaxPrice"), //集市委托交易最大价格
    FAIR_ENTRUST_MIN_PRICE("FairEntrustMinPrice", "FairEntrustMinPrice"), //集市委托交易最小价格

    FUND_CONVERSION_MAX_CNT_BY_ONCE("FundConversionMaxCntByOnce", "FundConversionMaxCntByOnce"), // 合约账户和钱包账户互转单笔最大值

    // 撮合交易-委托类型
    ENTRUST_X_ENTRUST_TYPE_LIMITPRICE("limitPrice", "limitPrice"), //限价
    ENTRUST_X_ENTRUST_TYPE_MARKETPRICE("marketPrice", "marketPrice"), //市价
    ENTRUST_X_ENTRUST_TYPE_LIMITANDMARKETPRICE("limitAndMarket", "limitAndMarket"), //限价和市价
    TRADE_SYS_PARAMETER_MATCHTRADE_BUY_BTC_ENTRUST("MatchTradeBuyBTCEntrust", "MatchTradeBuyBTCEntrust"), //委托交易买入BTC无委托收取BTC手续费比例
    TRADE_SYS_PARAMETER_MATCHTRADE_SELL_BTC_ENTRUST("MatchTradeSellBTCEntrust", "MatchTradeSellBTCEntrust"), //委托交易卖出BTC无委托成交收取USDX手续费比例

    // 委托来源(web,app,api,agent)
    ENTRUST_X_ENTRUST_SOURCE_WEB("web", "web"), //web
    ENTRUST_X_ENTRUST_SOURCE_APP("app", "app"), //app
    ENTRUST_X_ENTRUST_SOURCE_API("api", "api"), //api
    ENTRUST_X_ENTRUST_SOURCE_AGENT("agent", "agent"), //agent

    //symbol交易对
    SYMBOL_BTC2USD("111111111101","155555555502"),
    SYMBOL_BEX2BTC("122222222201","144444444402"),
    SYMBOL_BTC2EUR("111111111101","133333333302"),
    ;
    public String code;
    
    public String message;
    
    private TradeEnums(String code, String message)
    {
        this.code = code;
        this.message = message;
    }
    
    @Override
    public String getCode()
    {
        return code;
    }
    
    @Override
    public String getMessage()
    {
        return message;
    }
    
    /**
     * 根据状态码获取状态码描述
     * @param code 状态码
     * @return String 状态码描述
     */
    public static String getMessage(Integer code)
    {
        String result = null;
        for (TradeEnums c : TradeEnums.values())
        {
            if (c.code.equals(code))
            {
                result = c.message;
                break;
            }
        }
        return result;
    }
}

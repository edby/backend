/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.stockinfo.entity;

import com.blocain.bitms.orm.core.GenericEntity;
import com.blocain.bitms.tools.annotation.ExcelField;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 证券信息表 实体对象
 * <p>File：StockInfo.java</p>
 * <p>Title: StockInfo</p>
 * <p>Description:StockInfo</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class StockInfo extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**证券代码*/
    @NotNull(message = "证券代码不可为空")
    @ApiModelProperty(value = "证券代码", required = true)
    private java.lang.String     stockCode;
    
    /**证券名称*/
    @NotNull(message = "证券名称不可为空")
    @ApiModelProperty(value = "证券名称", required = true)
    private java.lang.String     stockName;
    
    /**证券类型(spotExchange现货汇兑 indexFuture指数期货)*/
    @NotNull(message = "证券类型(spotExchange现货汇兑 indexFuture指数期货)不可为空")
    @ApiModelProperty(value = "证券类型(spotExchange现货汇兑 indexFuture指数期货)", required = true)
    private java.lang.String     stockType;
    
    /**能否充值(yes可以 no不能)*/
    @NotNull(message = "能否充值(yes可以 no不能)不可为空")
    @ApiModelProperty(value = "能否充值(yes可以 no不能)", required = true)
    private java.lang.String     canRecharge;
    
    /**能否提现(yes可以 no不能)*/
    @NotNull(message = "能否提现(yes可以 no不能)不可为空")
    @ApiModelProperty(value = "能否提现(yes可以 no不能)", required = true)
    private java.lang.String     canWithdraw;
    
    /**能否交易(yes可以 no不能)*/
    @NotNull(message = "能否交易(yes可以 no不能)不可为空")
    @ApiModelProperty(value = "能否交易(yes可以 no不能)", required = true)
    private java.lang.String     canTrade;
    
    /**能否可借(yes可以 no不能)*/
    @NotNull(message = "能否可借(yes可以 no不能)不可为空")
    @ApiModelProperty(value = "能否可借(yes可以 no不能)", required = true)
    private java.lang.String     canBorrow;
    
    /**能否互转(yes可以 no不能)*/
    @NotNull(message = "能否互转(yes可以 no不能)不可为空")
    @ApiModelProperty(value = "能否互转(yes可以 no不能)", required = true)
    private java.lang.String     canConversion;
    
    /**是否启用(yes可以 no不能)*/
    @NotNull(message = "是否启用(yes可以 no不能)不可为空")
    @ApiModelProperty(value = "是否启用(yes可以 no不能)", required = true)
    private java.lang.String     isActive;
    
    /**能否可理财(yes可以 no不能)*/
    @NotNull(message = "能否可理财(yes可以 no不能)不可为空")
    @ApiModelProperty(value = "能否可理财(yes可以 no不能)", required = true)
    private java.lang.String     canWealth;
    
    /**是否是交易对(yes是 no不是)*/
    @NotNull(message = "是否是交易对(yes是 no不是)不可为空")
    @ApiModelProperty(value = "是否是交易对(yes是 no不是)", required = true)
    private java.lang.String     isExchange;
    
    /**交易标的证券ID*/
    @NotNull(message = "交易标的证券ID不可为空")
    @ApiModelProperty(value = "交易标的证券ID", required = true)
    private java.lang.Long       tradeStockinfoId;
    
    /**资金计价单位证券ID*/
    @NotNull(message = "资金计价单位证券ID不可为空")
    @ApiModelProperty(value = "资金计价单位证券ID", required = true)
    private java.lang.Long       capitalStockinfoId;
    
    /**清算标的证券ID*/
    @NotNull(message = "清算标的证券ID不可为空")
    @ApiModelProperty(value = "清算标的证券ID", required = true)
    private java.lang.Long       clearStockinfoId;
    
    /**外部行情标的证券ID*/
    @NotNull(message = "外部行情标的证券ID不可为空")
    @ApiModelProperty(value = "外部行情标的证券ID", required = true)
    private java.lang.Long       quotationStockinfoId;
    
    /**交易标的证券数量单位*/
    @NotNull(message = "交易标的证券数量单位不可为空")
    @ApiModelProperty(value = "交易标的证券数量单位", required = true)
    private java.lang.String     tradeAmtUnit;
    
    /**交易标的证券数量单位符号*/
    @NotNull(message = "交易标的证券数量单位符号不可为空")
    @ApiModelProperty(value = "交易标的证券数量单位符号", required = true)
    private java.lang.String     tradeAmtSymbol;
    
    /**资金计价单位证券数量单位*/
    @NotNull(message = "资金计价单位证券数量单位不可为空")
    @ApiModelProperty(value = "资金计价单位证券数量单位", required = true)
    private java.lang.String     capitalAmtUnit;
    
    /**资金计价单位证券数量单位符号*/
    @NotNull(message = "资金计价单位证券数量单位符号不可为空")
    @ApiModelProperty(value = "资金计价单位证券数量单位符号", required = true)
    private java.lang.String     capitalAmtSymbol;
    
    /**资产数量单位*/
    @NotNull(message = "资产数量单位不可为空")
    @ApiModelProperty(value = "资产数量单位", required = true)
    private java.lang.String     assetAmtUnit;
    
    /**资产数量单位符号*/
    @NotNull(message = "资产数量单位符号不可为空")
    @ApiModelProperty(value = "资产数量单位符号", required = true)
    private java.lang.String     assetAmtSymbol;
    
    /**清算数量单位*/
    @NotNull(message = "清算数量单位不可为空")
    @ApiModelProperty(value = "清算数量单位", required = true)
    private java.lang.String     clearAmtUnit;
    
    /**清算数量单位符号*/
    @NotNull(message = "清算数量单位符号不可为空")
    @ApiModelProperty(value = "清算数量单位符号", required = true)
    private java.lang.String     clearAmtSymbol;
    
    /**做多最大杠杆*/
    @NotNull(message = "做多最大杠杆不可为空")
    @ApiModelProperty(value = "做多最大杠杆", required = true)
    private java.math.BigDecimal maxLongLever;
    
    /**做空最大杠杆*/
    @NotNull(message = "做空最大杠杆不可为空")
    @ApiModelProperty(value = "做空最大杠杆", required = true)
    private java.math.BigDecimal maxShortLever;
    
    /**做多最大杠杆开关*/
    @NotNull(message = "做多最大杠杆开关不可为空")
    @ApiModelProperty(value = "做多最大杠杆开关", required = true)
    private String               maxLongLeverSwitch;
    
    /**做空最大杠杆开关*/
    @NotNull(message = "做空最大杠杆开关不可为空")
    @ApiModelProperty(value = "做空最大杠杆开关", required = true)
    private String               maxShortLeverSwitch;
    
    /**单笔委托买入数量上限*/
    @NotNull(message = "单笔委托买入数量上限不可为空")
    @ApiModelProperty(value = "单笔委托买入数量上限", required = true)
    private java.math.BigDecimal maxSingleBuyEntrustAmt;
    
    /**单笔委托卖出数量上限*/
    @NotNull(message = "单笔委托卖出数量上限不可为空")
    @ApiModelProperty(value = "单笔委托卖出数量上限", required = true)
    private java.math.BigDecimal maxSingleSellEntrustAmt;
    
    /**交割结算周期*/
    @NotNull(message = "交割结算周期不可为空")
    @ApiModelProperty(value = "交割结算周期", required = true)
    private java.lang.String     settlementCycle;
    
    /**交割结算价格*/
    @NotNull(message = "交割结算价格不可为空")
    @ApiModelProperty(value = "交割结算价格", required = true)
    private java.math.BigDecimal settlementPrice;
    
    /**交割结算步骤*/
    @NotNull(message = "交割结算步骤不可为空")
    @ApiModelProperty(value = "交割结算步骤", required = true)
    private Integer              settlementStep;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private java.lang.String     remark;
    
    /**创建人*/
    @NotNull(message = "创建人不可为空")
    @ApiModelProperty(value = "创建人", required = true)
    private java.lang.Long       createBy;
    
    /**创建时间*/
    @NotNull(message = "创建时间不可为空")
    @ApiModelProperty(value = "创建时间", required = true)
    private java.sql.Timestamp   createDate;
    
    /**修改人*/
    @ApiModelProperty(value = "修改人")
    private java.lang.Long       updateBy;
    
    /**修改时间*/
    @NotNull(message = "修改时间不可为空")
    @ApiModelProperty(value = "修改时间", required = true)
    private java.sql.Timestamp   updateDate;
    
    private String               stockTypeName;
    
    /**创建人*/
    @ExcelField(title = "创建人")
    private String               createByName;
    
    /**修改人*/
    @ExcelField(title = "修改人")
    private String               updateByName;
    
    /**多单爆仓价提前比例*/
    @NotNull(message = "多单爆仓价提前比例不可为空")
    @ApiModelProperty(value = "多单爆仓价提前比例", required = true)
    private java.math.BigDecimal closePositionLongPrePercent;
    
    /**空单爆仓价提前比例*/
    @NotNull(message = "空单爆仓价提前比例不可为空")
    @ApiModelProperty(value = "空单爆仓价提前比例", required = true)
    private java.math.BigDecimal closePositionShortPrePercent;
    
    /**买入大限价上浮%*/
    @NotNull(message = "买入大限价上浮%不可为空")
    @ApiModelProperty(value = "买入大限价上浮%", required = true)
    private java.math.BigDecimal buyMaxLimitPriceUpPercent;
    
    /**买入大限价下浮%*/
    @NotNull(message = "买入大限价下浮%不可为空")
    @ApiModelProperty(value = "买入大限价下浮%", required = true)
    private java.math.BigDecimal buyMaxLimitPriceDownPercent;
    
    /**买入小限价上浮%*/
    @NotNull(message = "买入小限价上浮不可为空")
    @ApiModelProperty(value = "买入小限价上浮%", required = true)
    private java.math.BigDecimal buyMinLimitPriceUpPercent;
    
    /**买入小限价下浮%*/
    @NotNull(message = "买入小限价下浮不可为空")
    @ApiModelProperty(value = "买入小限价下浮%", required = true)
    private java.math.BigDecimal buyMinLimitPriceDownPercent;
    
    /**卖出大限价上浮%*/
    @NotNull(message = "卖出大限价上浮不可为空")
    @ApiModelProperty(value = "卖出大限价上浮%", required = true)
    private java.math.BigDecimal sellMaxLimitPriceUpPercent;
    
    /**卖出大限价下浮%*/
    @NotNull(message = "卖出大限价下浮不可为空")
    @ApiModelProperty(value = "卖出大限价下浮%", required = true)
    private java.math.BigDecimal sellMaxLimitPriceDownPercent;
    
    /**卖出小限价上浮%*/
    @NotNull(message = "卖出小限价上浮不可为空")
    @ApiModelProperty(value = "卖出小限价上浮%", required = true)
    private java.math.BigDecimal sellMinLimitPriceUpPercent;
    
    /**卖出小限价下浮%*/
    @NotNull(message = "卖出小限价下浮不可为空")
    @ApiModelProperty(value = "卖出小限价下浮%", required = true)
    private java.math.BigDecimal sellMinLimitPriceDownPercent;
    
    /**买入价格精度*/
    @NotNull(message = "买入价格精度不可为空")
    @ApiModelProperty(value = "买入价格精度", required = true)
    private Integer              buyPricePrecision;
    
    /**买入数量精度*/
    @NotNull(message = "买入数量精度不可为空")
    @ApiModelProperty(value = "买入数量精度", required = true)
    private Integer              buyAmountPrecision;
    
    /**买入最小数量*/
    @NotNull(message = "买入最小数量不可为空")
    @ApiModelProperty(value = "买入最小数量", required = true)
    private java.math.BigDecimal buyMinAmount;
    
    /**卖出价格精度*/
    @NotNull(message = "卖出价格精度不可为空")
    @ApiModelProperty(value = "卖出价格精度", required = true)
    private Integer              sellPricePrecision;
    
    /**卖出数量精度*/
    @NotNull(message = "卖出数量精度不可为空")
    @ApiModelProperty(value = "卖出数量精度", required = true)
    private Integer              sellAmountPrecision;
    
    /**卖出最小数量*/
    @NotNull(message = "卖出最小数量不可为空")
    @ApiModelProperty(value = "卖出最小数量", required = true)
    private java.math.BigDecimal sellMinAmount;
    
    /**资产配置表*/
    @NotNull(message = "资产配置表不可为空")
    @ApiModelProperty(value = "资产配置表", required = true)
    private java.lang.String     tableAsset;
    
    /**借贷资产配置表*/
    @NotNull(message = "借贷资产配置表不可为空")
    @ApiModelProperty(value = "借贷资产配置表", required = true)
    private java.lang.String     tableDebitAsset;
    
    /**借贷资产明细配置表*/
    @NotNull(message = "借贷资产明细配置表不可为空")
    @ApiModelProperty(value = "借贷资产明细配置表", required = true)
    private java.lang.String     tableDebitAssetDetail;
    
    /**委托配置表*/
    @NotNull(message = "委托配置表不可为空")
    @ApiModelProperty(value = "委托配置表", required = true)
    private java.lang.String     tableEntrust;
    
    /**委托历史配置表*/
    @NotNull(message = "委托历史配置表不可为空")
    @ApiModelProperty(value = "委托历史配置表", required = true)
    private java.lang.String     tableEntrustHis;
    
    /**成交配置表*/
    @NotNull(message = "成交配置表不可为空")
    @ApiModelProperty(value = "成交配置表", required = true)
    private java.lang.String     tableRealDeal;
    
    /**成交历史配置表*/
    @NotNull(message = "成交历史配置表不可为空")
    @ApiModelProperty(value = "成交历史配置表", required = true)
    private java.lang.String     tableRealDealHis;
    
    /**资产流水配置表*/
    @NotNull(message = "资产流水配置表不可为空")
    @ApiModelProperty(value = "资产流水配置表", required = true)
    private java.lang.String     tableFundCurrent;
    
    /**资产流水历史配置表*/
    @NotNull(message = "资产流水历史配置表不可为空")
    @ApiModelProperty(value = "资产流水历史配置表", required = true)
    private java.lang.String     tableFundCurrentHis;
    
    /**行情K线配置表*/
    @NotNull(message = "行情K线配置表不可为空")
    @ApiModelProperty(value = "tableQuotationKline", required = true)
    private java.lang.String     tableQuotationKline;
    
    /**webSocketUrl*/
    @NotNull(message = "webSocketUrl不可为空")
    @ApiModelProperty(value = "webSocketUrl", required = true)
    private java.lang.String     webSocketUrl;
    
    /**topicKline1m*/
    @NotNull(message = "topicKline1m不可为空")
    @ApiModelProperty(value = "topicKline1m", required = true)
    private java.lang.String     topicKline1m;
    
    /**topicKline5m*/
    @NotNull(message = "topicKline5m不可为空")
    @ApiModelProperty(value = "topicKline5m", required = true)
    private java.lang.String     topicKline5m;
    
    /**topicKline15m*/
    @NotNull(message = "topicKline15m不可为空")
    @ApiModelProperty(value = "topicKline15m", required = true)
    private java.lang.String     topicKline15m;
    
    /**topicKline30m*/
    @NotNull(message = "topicKline30m不可为空")
    @ApiModelProperty(value = "topicKline30m", required = true)
    private java.lang.String     topicKline30m;
    
    /**topicKlineHour*/
    @NotNull(message = "topicKlineHour不可为空")
    @ApiModelProperty(value = "topicKlineHour", required = true)
    private java.lang.String     topicKlineHour;
    
    /**topicKlineDay*/
    @NotNull(message = "topicKlineDay不可为空")
    @ApiModelProperty(value = "topicKlineDay", required = true)
    private java.lang.String     topicKlineDay;
    
    /**topicKlineWeek*/
    @NotNull(message = "topicKlineWeek不可为空")
    @ApiModelProperty(value = "topicKlineWeek", required = true)
    private java.lang.String     topicKlineWeek;
    
    /**topicKlineMonth*/
    @NotNull(message = "topicKlineMonth不可为空")
    @ApiModelProperty(value = "topicKlineMonth", required = true)
    private java.lang.String     topicKlineMonth;
    
    /**topicEntrustDeepprice*/
    @NotNull(message = "topicEntrustDeepprice不可为空")
    @ApiModelProperty(value = "topicEntrustDeepprice", required = true)
    private java.lang.String     topicEntrustDeepprice;
    
    /**topicRealdealTransaction*/
    @NotNull(message = "topicRealdealTransaction不可为空")
    @ApiModelProperty(value = "topicRealdealTransaction", required = true)
    private java.lang.String     topicRealdealTransaction;
    
    /**topicRtquotationPrice*/
    @NotNull(message = "topicRtquotationPrice不可为空")
    @ApiModelProperty(value = "topicRtquotationPrice", required = true)
    private java.lang.String     topicRtquotationPrice;
    
    /**开放委托类型范围(limitPrice、marketPrice、limitAndMarket)*/
    @NotNull(message = "开放委托类型范围不可为空")
    @ApiModelProperty(value = "开放委托类型范围(limitPrice、marketPrice、limitAndMarket)", required = true)
    private java.lang.String     openEntrustType;
    
    /**数字货币借款日利率*/
    @NotNull(message = "数字货币借款日利率不可为空")
    @ApiModelProperty(value = "数字货币借款日利率", required = true)
    private java.math.BigDecimal digitBorrowDayRate;
    
    /**法定货币借款日利率*/
    @NotNull(message = "法定货币借款日利率不可为空")
    @ApiModelProperty(value = "法定货币借款日利率", required = true)
    private java.math.BigDecimal legalBorrowDayRate;
    
    /**借款总额度(交易标的)*/
    @NotNull(message = "借款总额度(交易标的)不可为空")
    @ApiModelProperty(value = "借款总额度(交易标的)", required = true)
    private java.math.BigDecimal tradeDebitTotal;
    
    /**计价标的借款总额度*/
    @NotNull(message = "计价标的借款总额度不可为空")
    @ApiModelProperty(value = "计价标的借款总额度", required = true)
    private java.math.BigDecimal capitalDebitTotal;
    
    /**理财日收益率*/
    @NotNull(message = "理财日收益率不可为空")
    @ApiModelProperty(value = "理财日收益率", required = true)
    private java.math.BigDecimal wealthDayRate;
    
    /**理财流水配置表*/
    @NotNull(message = "理财流水配置表不可为空")
    @ApiModelProperty(value = "理财流水配置表", required = true)
    private java.lang.String     tableWealthCurrent;
    
    /**做多溢价不高于百分比*/
    @NotNull(message = "做多溢价不高于百分比不可为空")
    @ApiModelProperty(value = "做多溢价不高于百分比", required = true)
    private java.math.BigDecimal maxLongFuse;
    
    /**做空负溢价不高于百分比*/
    @NotNull(message = "做空负溢价不高于百分比不可为空")
    @ApiModelProperty(value = "做空负溢价不高于百分比", required = true)
    private java.math.BigDecimal maxShortFuse;
    
    /**盘口累计量比例条分母*/
    @NotNull(message = "盘口累计量比例条分母不可为空")
    @ApiModelProperty(value = "盘口累计量比例条分母", required = true)
    private java.math.BigDecimal entrustAccumDenom;
    
    /**token合约地址*/
    @NotNull(message = "token合约地址")
    @ApiModelProperty(value = "token合约地址")
    private java.lang.String     tokenContactAddr;
    
    /**token小数精度*/
    @NotNull(message = "token小数精度不可为空")
    @ApiModelProperty(value = "token小数精度", required = true)
    private java.math.BigDecimal tokenDecimals;
    
    /**未认证用户提现当日额度上限*/
    @NotNull(message = "未认证用户提现当日额度上限不可为空")
    @ApiModelProperty(value = "未认证用户提现当日额度上限", required = true)
    private java.math.BigDecimal unauthUserWithdrawDayUpper;
    
    /**已认证用户提现当日额度上限*/
    @NotNull(message = "已认证用户提现当日额度上限不可为空")
    @ApiModelProperty(value = "已认证用户提现当日额度上限", required = true)
    private java.math.BigDecimal authedUserWithdrawDayUpper;
    
    /**小额提现热签临界值数量(小于等于就热签,大于就冷签)*/
    @NotNull(message = "小额提现热签临界值数量(小于等于就热签,大于就冷签)不可为空")
    @ApiModelProperty(value = "小额提现热签临界值数量(小于等于就热签,大于就冷签)", required = true)
    private java.math.BigDecimal smallWithdrawHotSignValue;
    
    /**小额充值手续费数量(小额充值收取SDF费用,但是充值金额要超过SDF的起点,否则不进行充值入账处理)*/
    @NotNull(message = "小额充值手续费数量(小额充值收取SDF费用,但是充值金额要超过SDF的起点,否则不进行充值入账处理)不可为空")
    @ApiModelProperty(value = "小额充值手续费数量(小额充值收取SDF费用,但是充值金额要超过SDF的起点,否则不进行充值入账处理)", required = true)
    private java.math.BigDecimal smallDepositFeeValue;
    
    /**小额充值标准临界值数量(小于等于就收取SDF，大于就不收取SDF)*/
    @NotNull(message = "小额充值标准临界值数量(小于等于就收取SDF，大于就不收取SDF)不可为空")
    @ApiModelProperty(value = "小额充值标准临界值数量(小于等于就收取SDF，大于就不收取SDF)", required = true)
    private java.math.BigDecimal smallDepositStandardValue;
    
    public String getStockCode()
    {
        return stockCode;
    }
    
    public void setStockCode(String stockCode)
    {
        this.stockCode = stockCode;
    }
    
    public String getStockName()
    {
        return stockName;
    }
    
    public void setStockName(String stockName)
    {
        this.stockName = stockName;
    }
    
    public String getStockType()
    {
        return stockType;
    }
    
    public void setStockType(String stockType)
    {
        this.stockType = stockType;
    }
    
    public String getCanRecharge()
    {
        return canRecharge;
    }
    
    public void setCanRecharge(String canRecharge)
    {
        this.canRecharge = canRecharge;
    }
    
    public String getCanWithdraw()
    {
        return canWithdraw;
    }
    
    public void setCanWithdraw(String canWithdraw)
    {
        this.canWithdraw = canWithdraw;
    }
    
    public String getCanTrade()
    {
        return canTrade;
    }
    
    public void setCanTrade(String canTrade)
    {
        this.canTrade = canTrade;
    }
    
    public String getCanBorrow()
    {
        return canBorrow;
    }
    
    public void setCanBorrow(String canBorrow)
    {
        this.canBorrow = canBorrow;
    }
    
    public String getCanConversion()
    {
        return canConversion;
    }
    
    public void setCanConversion(String canConversion)
    {
        this.canConversion = canConversion;
    }
    
    public String getCanWealth()
    {
        return canWealth;
    }
    
    public void setCanWealth(String canWealth)
    {
        this.canWealth = canWealth;
    }
    
    public String getIsExchange()
    {
        return isExchange;
    }
    
    public void setIsExchange(String isExchange)
    {
        this.isExchange = isExchange;
    }
    
    public String getTradeAmtUnit()
    {
        return tradeAmtUnit;
    }
    
    public void setTradeAmtUnit(String tradeAmtUnit)
    {
        this.tradeAmtUnit = tradeAmtUnit;
    }
    
    public String getTradeAmtSymbol()
    {
        return tradeAmtSymbol;
    }
    
    public void setTradeAmtSymbol(String tradeAmtSymbol)
    {
        this.tradeAmtSymbol = tradeAmtSymbol;
    }
    
    public String getCapitalAmtUnit()
    {
        return capitalAmtUnit;
    }
    
    public void setCapitalAmtUnit(String capitalAmtUnit)
    {
        this.capitalAmtUnit = capitalAmtUnit;
    }
    
    public String getCapitalAmtSymbol()
    {
        return capitalAmtSymbol;
    }
    
    public void setCapitalAmtSymbol(String capitalAmtSymbol)
    {
        this.capitalAmtSymbol = capitalAmtSymbol;
    }
    
    public String getAssetAmtUnit()
    {
        return assetAmtUnit;
    }
    
    public void setAssetAmtUnit(String assetAmtUnit)
    {
        this.assetAmtUnit = assetAmtUnit;
    }
    
    public String getAssetAmtSymbol()
    {
        return assetAmtSymbol;
    }
    
    public void setAssetAmtSymbol(String assetAmtSymbol)
    {
        this.assetAmtSymbol = assetAmtSymbol;
    }
    
    public String getClearAmtUnit()
    {
        return clearAmtUnit;
    }
    
    public void setClearAmtUnit(String clearAmtUnit)
    {
        this.clearAmtUnit = clearAmtUnit;
    }
    
    public String getClearAmtSymbol()
    {
        return clearAmtSymbol;
    }
    
    public void setClearAmtSymbol(String clearAmtSymbol)
    {
        this.clearAmtSymbol = clearAmtSymbol;
    }
    
    public BigDecimal getMaxLongLever()
    {
        return maxLongLever;
    }
    
    public void setMaxLongLever(BigDecimal maxLongLever)
    {
        this.maxLongLever = maxLongLever;
    }
    
    public BigDecimal getMaxShortLever()
    {
        return maxShortLever;
    }
    
    public void setMaxShortLever(BigDecimal maxShortLever)
    {
        this.maxShortLever = maxShortLever;
    }
    
    public BigDecimal getMaxSingleBuyEntrustAmt()
    {
        return maxSingleBuyEntrustAmt;
    }
    
    public void setMaxSingleBuyEntrustAmt(BigDecimal maxSingleBuyEntrustAmt)
    {
        this.maxSingleBuyEntrustAmt = maxSingleBuyEntrustAmt;
    }
    
    public BigDecimal getMaxSingleSellEntrustAmt()
    {
        return maxSingleSellEntrustAmt;
    }
    
    public void setMaxSingleSellEntrustAmt(BigDecimal maxSingleSellEntrustAmt)
    {
        this.maxSingleSellEntrustAmt = maxSingleSellEntrustAmt;
    }
    
    public String getSettlementCycle()
    {
        return settlementCycle;
    }
    
    public void setSettlementCycle(String settlementCycle)
    {
        this.settlementCycle = settlementCycle;
    }
    
    public BigDecimal getSettlementPrice()
    {
        return settlementPrice;
    }
    
    public void setSettlementPrice(BigDecimal settlementPrice)
    {
        this.settlementPrice = settlementPrice;
    }
    
    public Integer getSettlementStep()
    {
        return settlementStep;
    }
    
    public void setSettlementStep(Integer settlementStep)
    {
        this.settlementStep = settlementStep;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public Long getCreateBy()
    {
        return createBy;
    }
    
    public void setCreateBy(Long createBy)
    {
        this.createBy = createBy;
    }
    
    public Timestamp getCreateDate()
    {
        return createDate;
    }
    
    public void setCreateDate(Timestamp createDate)
    {
        this.createDate = createDate;
    }
    
    public Long getUpdateBy()
    {
        return updateBy;
    }
    
    public void setUpdateBy(Long updateBy)
    {
        this.updateBy = updateBy;
    }
    
    public Timestamp getUpdateDate()
    {
        return updateDate;
    }
    
    public void setUpdateDate(Timestamp updateDate)
    {
        this.updateDate = updateDate;
    }
    
    public String getStockTypeName()
    {
        return stockTypeName;
    }
    
    public void setStockTypeName(String stockTypeName)
    {
        this.stockTypeName = stockTypeName;
    }
    
    public String getCreateByName()
    {
        return createByName;
    }
    
    public void setCreateByName(String createByName)
    {
        this.createByName = createByName;
    }
    
    public String getUpdateByName()
    {
        return updateByName;
    }
    
    public void setUpdateByName(String updateByName)
    {
        this.updateByName = updateByName;
    }
    
    public BigDecimal getClosePositionLongPrePercent()
    {
        return closePositionLongPrePercent;
    }
    
    public void setClosePositionLongPrePercent(BigDecimal closePositionLongPrePercent)
    {
        this.closePositionLongPrePercent = closePositionLongPrePercent;
    }
    
    public BigDecimal getClosePositionShortPrePercent()
    {
        return closePositionShortPrePercent;
    }
    
    public void setClosePositionShortPrePercent(BigDecimal closePositionShortPrePercent)
    {
        this.closePositionShortPrePercent = closePositionShortPrePercent;
    }
    
    public BigDecimal getBuyMaxLimitPriceUpPercent()
    {
        return buyMaxLimitPriceUpPercent;
    }
    
    public void setBuyMaxLimitPriceUpPercent(BigDecimal buyMaxLimitPriceUpPercent)
    {
        this.buyMaxLimitPriceUpPercent = buyMaxLimitPriceUpPercent;
    }
    
    public BigDecimal getBuyMaxLimitPriceDownPercent()
    {
        return buyMaxLimitPriceDownPercent;
    }
    
    public void setBuyMaxLimitPriceDownPercent(BigDecimal buyMaxLimitPriceDownPercent)
    {
        this.buyMaxLimitPriceDownPercent = buyMaxLimitPriceDownPercent;
    }
    
    public BigDecimal getBuyMinLimitPriceUpPercent()
    {
        return buyMinLimitPriceUpPercent;
    }
    
    public void setBuyMinLimitPriceUpPercent(BigDecimal buyMinLimitPriceUpPercent)
    {
        this.buyMinLimitPriceUpPercent = buyMinLimitPriceUpPercent;
    }
    
    public BigDecimal getBuyMinLimitPriceDownPercent()
    {
        return buyMinLimitPriceDownPercent;
    }
    
    public void setBuyMinLimitPriceDownPercent(BigDecimal buyMinLimitPriceDownPercent)
    {
        this.buyMinLimitPriceDownPercent = buyMinLimitPriceDownPercent;
    }
    
    public BigDecimal getSellMaxLimitPriceUpPercent()
    {
        return sellMaxLimitPriceUpPercent;
    }
    
    public void setSellMaxLimitPriceUpPercent(BigDecimal sellMaxLimitPriceUpPercent)
    {
        this.sellMaxLimitPriceUpPercent = sellMaxLimitPriceUpPercent;
    }
    
    public BigDecimal getSellMaxLimitPriceDownPercent()
    {
        return sellMaxLimitPriceDownPercent;
    }
    
    public void setSellMaxLimitPriceDownPercent(BigDecimal sellMaxLimitPriceDownPercent)
    {
        this.sellMaxLimitPriceDownPercent = sellMaxLimitPriceDownPercent;
    }
    
    public BigDecimal getSellMinLimitPriceUpPercent()
    {
        return sellMinLimitPriceUpPercent;
    }
    
    public void setSellMinLimitPriceUpPercent(BigDecimal sellMinLimitPriceUpPercent)
    {
        this.sellMinLimitPriceUpPercent = sellMinLimitPriceUpPercent;
    }
    
    public BigDecimal getSellMinLimitPriceDownPercent()
    {
        return sellMinLimitPriceDownPercent;
    }
    
    public void setSellMinLimitPriceDownPercent(BigDecimal sellMinLimitPriceDownPercent)
    {
        this.sellMinLimitPriceDownPercent = sellMinLimitPriceDownPercent;
    }
    
    public Integer getBuyPricePrecision()
    {
        return buyPricePrecision;
    }
    
    public void setBuyPricePrecision(Integer buyPricePrecision)
    {
        this.buyPricePrecision = buyPricePrecision;
    }
    
    public Integer getBuyAmountPrecision()
    {
        return buyAmountPrecision;
    }
    
    public void setBuyAmountPrecision(Integer buyAmountPrecision)
    {
        this.buyAmountPrecision = buyAmountPrecision;
    }
    
    public BigDecimal getBuyMinAmount()
    {
        return buyMinAmount;
    }
    
    public void setBuyMinAmount(BigDecimal buyMinAmount)
    {
        this.buyMinAmount = buyMinAmount;
    }
    
    public Integer getSellPricePrecision()
    {
        return sellPricePrecision;
    }
    
    public void setSellPricePrecision(Integer sellPricePrecision)
    {
        this.sellPricePrecision = sellPricePrecision;
    }
    
    public Integer getSellAmountPrecision()
    {
        return sellAmountPrecision;
    }
    
    public void setSellAmountPrecision(Integer sellAmountPrecision)
    {
        this.sellAmountPrecision = sellAmountPrecision;
    }
    
    public BigDecimal getSellMinAmount()
    {
        return sellMinAmount;
    }
    
    public void setSellMinAmount(BigDecimal sellMinAmount)
    {
        this.sellMinAmount = sellMinAmount;
    }
    
    public String getTableAsset()
    {
        return tableAsset;
    }
    
    public void setTableAsset(String tableAsset)
    {
        this.tableAsset = tableAsset;
    }
    
    public String getTableDebitAsset()
    {
        return tableDebitAsset;
    }
    
    public void setTableDebitAsset(String tableDebitAsset)
    {
        this.tableDebitAsset = tableDebitAsset;
    }
    
    public String getTableDebitAssetDetail()
    {
        return tableDebitAssetDetail;
    }
    
    public void setTableDebitAssetDetail(String tableDebitAssetDetail)
    {
        this.tableDebitAssetDetail = tableDebitAssetDetail;
    }
    
    public String getTableEntrust()
    {
        return tableEntrust;
    }
    
    public void setTableEntrust(String tableEntrust)
    {
        this.tableEntrust = tableEntrust;
    }
    
    public String getTableRealDeal()
    {
        return tableRealDeal;
    }
    
    public void setTableRealDeal(String tableRealDeal)
    {
        this.tableRealDeal = tableRealDeal;
    }
    
    public String getTableFundCurrent()
    {
        return tableFundCurrent;
    }
    
    public void setTableFundCurrent(String tableFundCurrent)
    {
        this.tableFundCurrent = tableFundCurrent;
    }
    
    public String getTableEntrustHis()
    {
        return tableEntrustHis;
    }
    
    public void setTableEntrustHis(String tableEntrustHis)
    {
        this.tableEntrustHis = tableEntrustHis;
    }
    
    public String getTableRealDealHis()
    {
        return tableRealDealHis;
    }
    
    public void setTableRealDealHis(String tableRealDealHis)
    {
        this.tableRealDealHis = tableRealDealHis;
    }
    
    public String getTableFundCurrentHis()
    {
        return tableFundCurrentHis;
    }
    
    public void setTableFundCurrentHis(String tableFundCurrentHis)
    {
        this.tableFundCurrentHis = tableFundCurrentHis;
    }
    
    public String getTableQuotationKline()
    {
        return tableQuotationKline;
    }
    
    public void setTableQuotationKline(String tableQuotationKline)
    {
        this.tableQuotationKline = tableQuotationKline;
    }
    
    public Long getTradeStockinfoId()
    {
        return tradeStockinfoId;
    }
    
    public void setTradeStockinfoId(Long tradeStockinfoId)
    {
        this.tradeStockinfoId = tradeStockinfoId;
    }
    
    public Long getCapitalStockinfoId()
    {
        return capitalStockinfoId;
    }
    
    public void setCapitalStockinfoId(Long capitalStockinfoId)
    {
        this.capitalStockinfoId = capitalStockinfoId;
    }
    
    public Long getClearStockinfoId()
    {
        return clearStockinfoId;
    }
    
    public void setClearStockinfoId(Long clearStockinfoId)
    {
        this.clearStockinfoId = clearStockinfoId;
    }
    
    public Long getQuotationStockinfoId()
    {
        return quotationStockinfoId;
    }
    
    public void setQuotationStockinfoId(Long quotationStockinfoId)
    {
        this.quotationStockinfoId = quotationStockinfoId;
    }
    
    public String getWebSocketUrl()
    {
        return webSocketUrl;
    }
    
    public void setWebSocketUrl(String webSocketUrl)
    {
        this.webSocketUrl = webSocketUrl;
    }
    
    public String getTopicKline1m()
    {
        return topicKline1m;
    }
    
    public void setTopicKline1m(String topicKline1m)
    {
        this.topicKline1m = topicKline1m;
    }
    
    public String getTopicKline5m()
    {
        return topicKline5m;
    }
    
    public void setTopicKline5m(String topicKline5m)
    {
        this.topicKline5m = topicKline5m;
    }
    
    public String getTopicKline15m()
    {
        return topicKline15m;
    }
    
    public void setTopicKline15m(String topicKline15m)
    {
        this.topicKline15m = topicKline15m;
    }
    
    public String getTopicKline30m()
    {
        return topicKline30m;
    }
    
    public void setTopicKline30m(String topicKline30m)
    {
        this.topicKline30m = topicKline30m;
    }
    
    public String getTopicKlineHour()
    {
        return topicKlineHour;
    }
    
    public void setTopicKlineHour(String topicKlineHour)
    {
        this.topicKlineHour = topicKlineHour;
    }
    
    public String getTopicKlineDay()
    {
        return topicKlineDay;
    }
    
    public void setTopicKlineDay(String topicKlineDay)
    {
        this.topicKlineDay = topicKlineDay;
    }
    
    public String getTopicKlineWeek()
    {
        return topicKlineWeek;
    }
    
    public void setTopicKlineWeek(String topicKlineWeek)
    {
        this.topicKlineWeek = topicKlineWeek;
    }
    
    public String getTopicKlineMonth()
    {
        return topicKlineMonth;
    }
    
    public void setTopicKlineMonth(String topicKlineMonth)
    {
        this.topicKlineMonth = topicKlineMonth;
    }
    
    public String getTopicEntrustDeepprice()
    {
        return topicEntrustDeepprice;
    }
    
    public void setTopicEntrustDeepprice(String topicEntrustDeepprice)
    {
        this.topicEntrustDeepprice = topicEntrustDeepprice;
    }
    
    public String getTopicRealdealTransaction()
    {
        return topicRealdealTransaction;
    }
    
    public void setTopicRealdealTransaction(String topicRealdealTransaction)
    {
        this.topicRealdealTransaction = topicRealdealTransaction;
    }
    
    public String getTopicRtquotationPrice()
    {
        return topicRtquotationPrice;
    }
    
    public void setTopicRtquotationPrice(String topicRtquotationPrice)
    {
        this.topicRtquotationPrice = topicRtquotationPrice;
    }
    
    public String getOpenEntrustType()
    {
        return openEntrustType;
    }
    
    public void setOpenEntrustType(String openEntrustType)
    {
        this.openEntrustType = openEntrustType;
    }
    
    public BigDecimal getDigitBorrowDayRate()
    {
        return digitBorrowDayRate;
    }
    
    public void setDigitBorrowDayRate(BigDecimal digitBorrowDayRate)
    {
        this.digitBorrowDayRate = digitBorrowDayRate;
    }
    
    public BigDecimal getLegalBorrowDayRate()
    {
        return legalBorrowDayRate;
    }
    
    public void setLegalBorrowDayRate(BigDecimal legalBorrowDayRate)
    {
        this.legalBorrowDayRate = legalBorrowDayRate;
    }
    
    public BigDecimal getTradeDebitTotal()
    {
        return tradeDebitTotal;
    }
    
    public void setTradeDebitTotal(BigDecimal tradeDebitTotal)
    {
        this.tradeDebitTotal = tradeDebitTotal;
    }
    
    public BigDecimal getCapitalDebitTotal()
    {
        return capitalDebitTotal;
    }
    
    public void setCapitalDebitTotal(BigDecimal capitalDebitTotal)
    {
        this.capitalDebitTotal = capitalDebitTotal;
    }
    
    public String getMaxLongLeverSwitch()
    {
        return maxLongLeverSwitch;
    }
    
    public void setMaxLongLeverSwitch(String maxLongLeverSwitch)
    {
        this.maxLongLeverSwitch = maxLongLeverSwitch;
    }
    
    public String getMaxShortLeverSwitch()
    {
        return maxShortLeverSwitch;
    }
    
    public void setMaxShortLeverSwitch(String maxShortLeverSwitch)
    {
        this.maxShortLeverSwitch = maxShortLeverSwitch;
    }
    
    public BigDecimal getWealthDayRate()
    {
        return wealthDayRate;
    }
    
    public void setWealthDayRate(BigDecimal wealthDayRate)
    {
        this.wealthDayRate = wealthDayRate;
    }
    
    public String getTableWealthCurrent()
    {
        return tableWealthCurrent;
    }
    
    public void setTableWealthCurrent(String tableWealthCurrent)
    {
        this.tableWealthCurrent = tableWealthCurrent;
    }
    
    public BigDecimal getMaxLongFuse()
    {
        return maxLongFuse;
    }
    
    public void setMaxLongFuse(BigDecimal maxLongFuse)
    {
        this.maxLongFuse = maxLongFuse;
    }
    
    public BigDecimal getMaxShortFuse()
    {
        return maxShortFuse;
    }
    
    public void setMaxShortFuse(BigDecimal maxShortFuse)
    {
        this.maxShortFuse = maxShortFuse;
    }
    
    public BigDecimal getEntrustAccumDenom()
    {
        return entrustAccumDenom;
    }
    
    public void setEntrustAccumDenom(BigDecimal entrustAccumDenom)
    {
        this.entrustAccumDenom = entrustAccumDenom;
    }
    
    public String getTokenContactAddr()
    {
        return tokenContactAddr;
    }
    
    public void setTokenContactAddr(String tokenContactAddr)
    {
        this.tokenContactAddr = tokenContactAddr;
    }
    
    public BigDecimal getTokenDecimals()
    {
        return tokenDecimals;
    }
    
    public void setTokenDecimals(BigDecimal tokenDecimals)
    {
        this.tokenDecimals = tokenDecimals;
    }
    
    public BigDecimal getUnauthUserWithdrawDayUpper()
    {
        return unauthUserWithdrawDayUpper;
    }
    
    public void setUnauthUserWithdrawDayUpper(BigDecimal unauthUserWithdrawDayUpper)
    {
        this.unauthUserWithdrawDayUpper = unauthUserWithdrawDayUpper;
    }
    
    public BigDecimal getAuthedUserWithdrawDayUpper()
    {
        return authedUserWithdrawDayUpper;
    }
    
    public void setAuthedUserWithdrawDayUpper(BigDecimal authedUserWithdrawDayUpper)
    {
        this.authedUserWithdrawDayUpper = authedUserWithdrawDayUpper;
    }
    
    public BigDecimal getSmallDepositFeeValue()
    {
        return smallDepositFeeValue;
    }
    
    public void setSmallDepositFeeValue(BigDecimal smallDepositFeeValue)
    {
        this.smallDepositFeeValue = smallDepositFeeValue;
    }
    
    public BigDecimal getSmallWithdrawHotSignValue()
    {
        return smallWithdrawHotSignValue;
    }
    
    public void setSmallWithdrawHotSignValue(BigDecimal smallWithdrawHotSignValue)
    {
        this.smallWithdrawHotSignValue = smallWithdrawHotSignValue;
    }
    
    public BigDecimal getSmallDepositStandardValue()
    {
        return smallDepositStandardValue;
    }
    
    public void setSmallDepositStandardValue(BigDecimal smallDepositStandardValue)
    {
        this.smallDepositStandardValue = smallDepositStandardValue;
    }
    
    public String getIsActive()
    {
        return isActive;
    }
    
    public void setIsActive(String isActive)
    {
        this.isActive = isActive;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("StockInfo{");
        sb.append("stockCode='").append(stockCode).append('\'');
        sb.append(", stockName='").append(stockName).append('\'');
        sb.append(", stockType='").append(stockType).append('\'');
        sb.append(", canRecharge='").append(canRecharge).append('\'');
        sb.append(", canWithdraw='").append(canWithdraw).append('\'');
        sb.append(", canTrade='").append(canTrade).append('\'');
        sb.append(", canBorrow='").append(canBorrow).append('\'');
        sb.append(", canConversion='").append(canConversion).append('\'');
        sb.append(", isActive='").append(isActive).append('\'');
        sb.append(", canWealth='").append(canWealth).append('\'');
        sb.append(", isExchange='").append(isExchange).append('\'');
        sb.append(", tradeStockinfoId=").append(tradeStockinfoId);
        sb.append(", capitalStockinfoId=").append(capitalStockinfoId);
        sb.append(", clearStockinfoId=").append(clearStockinfoId);
        sb.append(", quotationStockinfoId=").append(quotationStockinfoId);
        sb.append(", tradeAmtUnit='").append(tradeAmtUnit).append('\'');
        sb.append(", tradeAmtSymbol='").append(tradeAmtSymbol).append('\'');
        sb.append(", capitalAmtUnit='").append(capitalAmtUnit).append('\'');
        sb.append(", capitalAmtSymbol='").append(capitalAmtSymbol).append('\'');
        sb.append(", assetAmtUnit='").append(assetAmtUnit).append('\'');
        sb.append(", assetAmtSymbol='").append(assetAmtSymbol).append('\'');
        sb.append(", clearAmtUnit='").append(clearAmtUnit).append('\'');
        sb.append(", clearAmtSymbol='").append(clearAmtSymbol).append('\'');
        sb.append(", maxLongLever=").append(maxLongLever);
        sb.append(", maxShortLever=").append(maxShortLever);
        sb.append(", maxLongLeverSwitch='").append(maxLongLeverSwitch).append('\'');
        sb.append(", maxShortLeverSwitch='").append(maxShortLeverSwitch).append('\'');
        sb.append(", maxSingleBuyEntrustAmt=").append(maxSingleBuyEntrustAmt);
        sb.append(", maxSingleSellEntrustAmt=").append(maxSingleSellEntrustAmt);
        sb.append(", settlementCycle='").append(settlementCycle).append('\'');
        sb.append(", settlementPrice=").append(settlementPrice);
        sb.append(", settlementStep=").append(settlementStep);
        sb.append(", remark='").append(remark).append('\'');
        sb.append(", createBy=").append(createBy);
        sb.append(", createDate=").append(createDate);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", updateDate=").append(updateDate);
        sb.append(", stockTypeName='").append(stockTypeName).append('\'');
        sb.append(", createByName='").append(createByName).append('\'');
        sb.append(", updateByName='").append(updateByName).append('\'');
        sb.append(", closePositionLongPrePercent=").append(closePositionLongPrePercent);
        sb.append(", closePositionShortPrePercent=").append(closePositionShortPrePercent);
        sb.append(", buyMaxLimitPriceUpPercent=").append(buyMaxLimitPriceUpPercent);
        sb.append(", buyMaxLimitPriceDownPercent=").append(buyMaxLimitPriceDownPercent);
        sb.append(", buyMinLimitPriceUpPercent=").append(buyMinLimitPriceUpPercent);
        sb.append(", buyMinLimitPriceDownPercent=").append(buyMinLimitPriceDownPercent);
        sb.append(", sellMaxLimitPriceUpPercent=").append(sellMaxLimitPriceUpPercent);
        sb.append(", sellMaxLimitPriceDownPercent=").append(sellMaxLimitPriceDownPercent);
        sb.append(", sellMinLimitPriceUpPercent=").append(sellMinLimitPriceUpPercent);
        sb.append(", sellMinLimitPriceDownPercent=").append(sellMinLimitPriceDownPercent);
        sb.append(", buyPricePrecision=").append(buyPricePrecision);
        sb.append(", buyAmountPrecision=").append(buyAmountPrecision);
        sb.append(", buyMinAmount=").append(buyMinAmount);
        sb.append(", sellPricePrecision=").append(sellPricePrecision);
        sb.append(", sellAmountPrecision=").append(sellAmountPrecision);
        sb.append(", sellMinAmount=").append(sellMinAmount);
        sb.append(", tableAsset='").append(tableAsset).append('\'');
        sb.append(", tableDebitAsset='").append(tableDebitAsset).append('\'');
        sb.append(", tableDebitAssetDetail='").append(tableDebitAssetDetail).append('\'');
        sb.append(", tableEntrust='").append(tableEntrust).append('\'');
        sb.append(", tableEntrustHis='").append(tableEntrustHis).append('\'');
        sb.append(", tableRealDeal='").append(tableRealDeal).append('\'');
        sb.append(", tableRealDealHis='").append(tableRealDealHis).append('\'');
        sb.append(", tableFundCurrent='").append(tableFundCurrent).append('\'');
        sb.append(", tableFundCurrentHis='").append(tableFundCurrentHis).append('\'');
        sb.append(", tableQuotationKline='").append(tableQuotationKline).append('\'');
        sb.append(", webSocketUrl='").append(webSocketUrl).append('\'');
        sb.append(", topicKline1m='").append(topicKline1m).append('\'');
        sb.append(", topicKline5m='").append(topicKline5m).append('\'');
        sb.append(", topicKline15m='").append(topicKline15m).append('\'');
        sb.append(", topicKline30m='").append(topicKline30m).append('\'');
        sb.append(", topicKlineHour='").append(topicKlineHour).append('\'');
        sb.append(", topicKlineDay='").append(topicKlineDay).append('\'');
        sb.append(", topicKlineWeek='").append(topicKlineWeek).append('\'');
        sb.append(", topicKlineMonth='").append(topicKlineMonth).append('\'');
        sb.append(", topicEntrustDeepprice='").append(topicEntrustDeepprice).append('\'');
        sb.append(", topicRealdealTransaction='").append(topicRealdealTransaction).append('\'');
        sb.append(", topicRtquotationPrice='").append(topicRtquotationPrice).append('\'');
        sb.append(", openEntrustType='").append(openEntrustType).append('\'');
        sb.append(", digitBorrowDayRate=").append(digitBorrowDayRate);
        sb.append(", legalBorrowDayRate=").append(legalBorrowDayRate);
        sb.append(", tradeDebitTotal=").append(tradeDebitTotal);
        sb.append(", capitalDebitTotal=").append(capitalDebitTotal);
        sb.append(", wealthDayRate=").append(wealthDayRate);
        sb.append(", tableWealthCurrent='").append(tableWealthCurrent).append('\'');
        sb.append(", maxLongFuse=").append(maxLongFuse);
        sb.append(", maxShortFuse=").append(maxShortFuse);
        sb.append(", entrustAccumDenom=").append(entrustAccumDenom);
        sb.append(", tokenContactAddr='").append(tokenContactAddr).append('\'');
        sb.append(", tokenDecimals=").append(tokenDecimals);
        sb.append(", unauthUserWithdrawDayUpper=").append(unauthUserWithdrawDayUpper);
        sb.append(", authedUserWithdrawDayUpper=").append(authedUserWithdrawDayUpper);
        sb.append(", smallWithdrawHotSignValue=").append(smallWithdrawHotSignValue);
        sb.append(", smallDepositFeeValue=").append(smallDepositFeeValue);
        sb.append(", smallDepositStandardValue=").append(smallDepositStandardValue);
        sb.append('}');
        return sb.toString();
    }
}

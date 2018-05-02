/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.trade.entity;

import com.blocain.bitms.orm.core.GenericEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 成交表X 实体对象
 * <p>File：RealDealVCoinMoney.java</p>
 * <p>Title: RealDealVCoinMoney</p>
 * <p>Description:RealDealVCoinMoney</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "成交表X")
public class RealDealVCoinMoney extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**买单账户ID*/
    @NotNull(message = "买单账户ID不可为空")
    @ApiModelProperty(value = "买单账户ID", required = true)
    private Long                 buyAccountId;
    
    /**买单账户*/
    private String               buyAccountName;
    
    /**卖单账户ID*/
    @NotNull(message = "卖单账户ID不可为空")
    @ApiModelProperty(value = "卖单账户ID", required = true)
    private Long                 sellAccountId;
    
    /**卖单账户*/
    private String               sellAccountName;
    
    /**买单委托ID*/
    @NotNull(message = "买单委托ID不可为空")
    @ApiModelProperty(value = "买单委托ID", required = true)
    private Long                 buyEntrustId;
    
    /**卖单委托ID*/
    @NotNull(message = "卖单委托ID不可为空")
    @ApiModelProperty(value = "卖单委托ID", required = true)
    private Long                 sellEntrustId;
    
    /**交易类型(撮合交易matchTrade)*/
    @NotNull(message = "交易类型(撮合交易matchTrade)不可为空")
    @ApiModelProperty(value = "交易类型(撮合交易matchTrade)", required = true)
    private String               tradeType;
    
    /**业务类别(撮合交易现货买入委托成交matchTradeSpotBuyEntrustDeal、撮合交易现货卖出委托成交matchTradeSpotSellEntrustDeal)*/
    @NotNull(message = "业务类别(撮合交易现货买入委托成交matchTradeSpotBuyEntrustDeal、撮合交易现货卖出委托成交matchTradeSpotSellEntrustDeal)不可为空")
    @ApiModelProperty(value = "业务类别(撮合交易现货买入委托成交matchTradeSpotBuyEntrustDeal、撮合交易现货卖出委托成交matchTradeSpotSellEntrustDeal)", required = true)
    private String               businessFlag;
    
    /**成交方向(现货买入spotBuy、现货卖出spotSell)*/
    @NotNull(message = "成交方向(现货买入spotBuy、现货卖出spotSell)不可为空")
    @ApiModelProperty(value = "成交方向(现货买入spotBuy、现货卖出spotSell)", required = true)
    private String               dealDirect;
    
    /**成交证券内码(177777777706)*/
    @NotNull(message = "成交证券内码(177777777706)不可为空")
    @ApiModelProperty(value = "成交证券内码(177777777706)", required = true)
    private Long                 dealStockinfoId;
    
    /**成交证券代码*/
    private String               dealStockinfoCode;
    
    /**成交数量*/
    @NotNull(message = "成交数量不可为空")
    @ApiModelProperty(value = "成交数量", required = true)
    private java.math.BigDecimal dealAmt;
    
    /**成交价格*/
    @NotNull(message = "成交价格不可为空")
    @ApiModelProperty(value = "成交价格", required = true)
    private java.math.BigDecimal dealPrice;
    
    /**成交金额*/
    @NotNull(message = "成交金额不可为空")
    @ApiModelProperty(value = "成交金额", required = true)
    private java.math.BigDecimal dealBalance;
    
    /**成交时间*/
    @NotNull(message = "成交时间不可为空")
    @ApiModelProperty(value = "成交时间", required = true)
    private java.sql.Timestamp       dealTime;
    
    /**买单手续费对应证券内码(111111111101)*/
    @NotNull(message = "买单手续费对应证券内码(111111111101)不可为空")
    @ApiModelProperty(value = "买单手续费对应证券内码(111111111101)", required = true)
    private Long                 buyFeeType;
    
    /**卖单手续费对应证券内码(111111111101)*/
    @NotNull(message = "卖单手续费对应证券内码(111111111101)不可为空")
    @ApiModelProperty(value = "卖单手续费对应证券内码(111111111101)", required = true)
    private Long                 sellFeeType;
    
    /**买单手续费*/
    @NotNull(message = "买单手续费不可为空")
    @ApiModelProperty(value = "买单手续费", required = true)
    private java.math.BigDecimal buyFee;
    
    /**卖单手续费*/
    @NotNull(message = "卖单手续费不可为空")
    @ApiModelProperty(value = "卖单手续费", required = true)
    private java.math.BigDecimal sellFee;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String               remark;
    
    // 用于按委托ID查看成交信息
    /**委托方向(现货买入spotBuy、现货卖出spotSell)*/
    private String               entrustDirect;
    
    /**用户界面传值 查询开始时间 */
    private String               timeStart;
    
    /**用户界面传值 查询结束时间 */
    private String               timeEnd;
    
    /**成交交易对类型*/
    private String               tableName;

    /**手续费证券*/
    private String               stockCode;

    /**委托表*/
    private String               enturstTableName;

    /** 委托表ID */
    private Long                 entrustId;

    /**
     * 委托方成交手续费
     */
    private BigDecimal  dealFee;

    public Long getBuyAccountId()
    {
        return this.buyAccountId;
    }
    
    public void setBuyAccountId(Long buyAccountId)
    {
        this.buyAccountId = buyAccountId;
    }
    
    public Long getSellAccountId()
    {
        return this.sellAccountId;
    }
    
    public void setSellAccountId(Long sellAccountId)
    {
        this.sellAccountId = sellAccountId;
    }
    
    public Long getBuyEntrustId()
    {
        return this.buyEntrustId;
    }
    
    public void setBuyEntrustId(Long buyEntrustId)
    {
        this.buyEntrustId = buyEntrustId;
    }
    
    public Long getSellEntrustId()
    {
        return this.sellEntrustId;
    }
    
    public void setSellEntrustId(Long sellEntrustId)
    {
        this.sellEntrustId = sellEntrustId;
    }
    
    public String getTradeType()
    {
        return this.tradeType;
    }
    
    public void setTradeType(String tradeType)
    {
        this.tradeType = tradeType;
    }
    
    public String getBusinessFlag()
    {
        return this.businessFlag;
    }
    
    public void setBusinessFlag(String businessFlag)
    {
        this.businessFlag = businessFlag;
    }
    
    public String getDealDirect()
    {
        return this.dealDirect;
    }
    
    public void setDealDirect(String dealDirect)
    {
        this.dealDirect = dealDirect;
    }
    
    public Long getDealStockinfoId()
    {
        return this.dealStockinfoId;
    }
    
    public void setDealStockinfoId(Long dealStockinfoId)
    {
        this.dealStockinfoId = dealStockinfoId;
    }
    
    public java.math.BigDecimal getDealAmt()
    {
        return this.dealAmt;
    }
    
    public void setDealAmt(java.math.BigDecimal dealAmt)
    {
        this.dealAmt = dealAmt;
    }
    
    public java.math.BigDecimal getDealPrice()
    {
        return this.dealPrice;
    }
    
    public void setDealPrice(java.math.BigDecimal dealPrice)
    {
        this.dealPrice = dealPrice;
    }
    
    public java.sql.Timestamp getDealTime()
    {
        return this.dealTime;
    }
    
    public void setDealTime(java.sql.Timestamp dealTime)
    {
        this.dealTime = dealTime;
    }
    
    public Long getBuyFeeType()
    {
        return this.buyFeeType;
    }
    
    public void setBuyFeeType(Long buyFeeType)
    {
        this.buyFeeType = buyFeeType;
    }
    
    public Long getSellFeeType()
    {
        return this.sellFeeType;
    }
    
    public void setSellFeeType(Long sellFeeType)
    {
        this.sellFeeType = sellFeeType;
    }
    
    public java.math.BigDecimal getBuyFee()
    {
        return this.buyFee;
    }
    
    public void setBuyFee(java.math.BigDecimal buyFee)
    {
        this.buyFee = buyFee;
    }
    
    public java.math.BigDecimal getSellFee()
    {
        return this.sellFee;
    }
    
    public void setSellFee(java.math.BigDecimal sellFee)
    {
        this.sellFee = sellFee;
    }
    
    public String getRemark()
    {
        return this.remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public String getEntrustDirect()
    {
        return entrustDirect;
    }
    
    public void setEntrustDirect(String entrustDirect)
    {
        this.entrustDirect = entrustDirect;
    }
    
    public BigDecimal getDealBalance()
    {
        return dealBalance;
    }
    
    public void setDealBalance(BigDecimal dealBalance)
    {
        this.dealBalance = dealBalance;
    }
    
    public String getBuyAccountName()
    {
        return buyAccountName;
    }
    
    public void setBuyAccountName(String buyAccountName)
    {
        this.buyAccountName = buyAccountName;
    }
    
    public String getSellAccountName()
    {
        return sellAccountName;
    }
    
    public void setSellAccountName(String sellAccountName)
    {
        this.sellAccountName = sellAccountName;
    }
    
    public String getDealStockinfoCode()
    {
        return dealStockinfoCode;
    }
    
    public void setDealStockinfoCode(String dealStockinfoCode)
    {
        this.dealStockinfoCode = dealStockinfoCode;
    }
    
    public String getTimeStart()
    {
        return timeStart;
    }
    
    public void setTimeStart(String timeStart)
    {
        this.timeStart = timeStart;
    }
    
    public String getTimeEnd()
    {
        return timeEnd;
    }
    
    public void setTimeEnd(String timeEnd)
    {
        this.timeEnd = timeEnd;
    }
    
    public String getTableName()
    {
        return tableName;
    }
    
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getStockCode()
    {
        return stockCode;
    }

    public void setStockCode(String stockCode)
    {
        this.stockCode = stockCode;
    }

    public String getEnturstTableName()
    {
        return enturstTableName;
    }

    public void setEnturstTableName(String enturstTableName)
    {
        this.enturstTableName = enturstTableName;
    }

    public Long getEntrustId()
    {
        return entrustId;
    }

    public void setEntrustId(Long entrustId)
    {
        this.entrustId = entrustId;
    }

    public BigDecimal getDealFee()
    {
        return dealFee;
    }

    public void setDealFee(BigDecimal dealFee)
    {
        this.dealFee = dealFee;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("RealDealVCoinMoney{");
        sb.append("id=").append(id);
        sb.append(", buyAccountId=").append(buyAccountId);
        sb.append(", buyAccountName='").append(buyAccountName).append('\'');
        sb.append(", sellAccountId=").append(sellAccountId);
        sb.append(", sellAccountName='").append(sellAccountName).append('\'');
        sb.append(", buyEntrustId=").append(buyEntrustId);
        sb.append(", sellEntrustId=").append(sellEntrustId);
        sb.append(", tradeType='").append(tradeType).append('\'');
        sb.append(", businessFlag='").append(businessFlag).append('\'');
        sb.append(", dealDirect='").append(dealDirect).append('\'');
        sb.append(", dealStockinfoId=").append(dealStockinfoId);
        sb.append(", dealStockinfoCode='").append(dealStockinfoCode).append('\'');
        sb.append(", dealAmt=").append(dealAmt);
        sb.append(", dealPrice=").append(dealPrice);
        sb.append(", dealBalance=").append(dealBalance);
        sb.append(", dealTime=").append(dealTime);
        sb.append(", buyFeeType=").append(buyFeeType);
        sb.append(", sellFeeType=").append(sellFeeType);
        sb.append(", buyFee=").append(buyFee);
        sb.append(", sellFee=").append(sellFee);
        sb.append(", remark='").append(remark).append('\'');
        sb.append(", entrustDirect='").append(entrustDirect).append('\'');
        sb.append(", timeStart='").append(timeStart).append('\'');
        sb.append(", timeEnd='").append(timeEnd).append('\'');
        sb.append(", tableName='").append(tableName).append('\'');
        sb.append(", stockCode='").append(stockCode).append('\'');
        sb.append(", enturstTableName='").append(enturstTableName).append('\'');
        sb.append(", entrustId=").append(entrustId);
        sb.append(", dealFee=").append(dealFee);
        sb.append('}');
        return sb.toString();
    }
}

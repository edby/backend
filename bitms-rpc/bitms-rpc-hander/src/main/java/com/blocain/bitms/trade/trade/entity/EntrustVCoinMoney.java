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
 * 委托表X 实体对象
 * <p>File：EntrustVCoinMoney.java</p>
 * <p>Title: EntrustVCoinMoney</p>
 * <p>Description:EntrustVCoinMoney</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "委托表X")
public class EntrustVCoinMoney extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**账户ID*/
    @NotNull(message = "账户ID不可为空")
    @ApiModelProperty(value = "账户ID", required = true)
    private Long                 accountId;
    
    /**账户名*/
    private String               accountName;
    
    /**委托时间*/
    @NotNull(message = "委托时间不可为空")
    @ApiModelProperty(value = "委托时间", required = true)
    private java.sql.Timestamp   entrustTime;
    
    /**委托来源(web,app,api,agent)*/
    @NotNull(message = "委托来源(web,app,api,agent)不可为空")
    @ApiModelProperty(value = "委托来源(web,app,api,agent)", required = true)
    private String               entrustSource;
    
    /**业务类别(撮合交易现货买入委托matchTradeSpotBuyEntrust、撮合交易现货卖出委托matchTradeSpotSellEntrust)*/
    @NotNull(message = "业务类别(撮合交易现货买入委托matchTradeSpotBuyEntrust、撮合交易现货卖出委托matchTradeSpotSellEntrust)不可为空")
    @ApiModelProperty(value = "业务类别(撮合交易现货买入委托matchTradeSpotBuyEntrust、撮合交易现货卖出委托matchTradeSpotSellEntrust)", required = true)
    private String               businessFlag;
    
    /**交易类型(撮合交易matchTrade)*/
    @NotNull(message = "交易类型(撮合交易matchTrade)不可为空")
    @ApiModelProperty(value = "交易类型(撮合交易matchTrade)", required = true)
    private String               tradeType;
    
    /**委托类型(限价limitPrice、市价marketPrice)*/
    @NotNull(message = "委托类型(限价limitPrice、市价marketPrice)不可为空")
    @ApiModelProperty(value = "委托类型(限价limitPrice、市价marketPrice)", required = true)
    private String               entrustType;
    
    /**委托方向(现货买入spotBuy、现货卖出spotSell)*/
    @NotNull(message = "委托方向(现货买入spotBuy、现货卖出spotSell)不可为空")
    @ApiModelProperty(value = "委托方向(现货买入spotBuy、现货卖出spotSell)", required = true)
    private String               entrustDirect;
    
    /**委托证券内码(177777777706)*/
    @NotNull(message = "委托证券内码(177777777706)不可为空")
    @ApiModelProperty(value = "委托证券内码(177777777706)", required = true)
    private Long                 entrustStockinfoId;
    
    /**委托关联证券内码(177777777706)*/
    @NotNull(message = "委托关联证券内码(177777777706)不可为空")
    @ApiModelProperty(value = "委托关联证券内码(177777777706)", required = true)
    private Long                 entrustRelatedStockinfoId;
    
    /**委托数量*/
    @NotNull(message = "委托数量不可为空")
    @ApiModelProperty(value = "委托数量", required = true)
    private java.math.BigDecimal entrustAmt;
    
    /**委托价格*/
    @NotNull(message = "委托价格不可为空")
    @ApiModelProperty(value = "委托价格", required = true)
    private java.math.BigDecimal entrustPrice;
    
    /**委托备注*/
    @ApiModelProperty(value = "委托备注")
    private String               entrustRemark;
    
    /**委托账户类型(0用户委托、1平台委托)*/
    @NotNull(message = "委托账户类型(0用户委托、1平台委托)不可为空")
    @ApiModelProperty(value = "委托账户类型(0用户委托、1平台委托)", required = true)
    private Boolean              entrustAccountType;
    
    /**手续费率*/
    @NotNull(message = "手续费率不可为空")
    @ApiModelProperty(value = "手续费率", required = true)
    private java.math.BigDecimal feeRate;
    
    /**手续费对应证券内码(111111111101)*/
    @NotNull(message = "手续费对应证券内码(111111111101)不可为空")
    @ApiModelProperty(value = "手续费对应证券内码(111111111101)", required = true)
    private Long                 feeStockinfoId;
    
    /**成交数量*/
    @NotNull(message = "成交数量不可为空")
    @ApiModelProperty(value = "成交数量", required = true)
    private java.math.BigDecimal dealAmt;
    
    /**成交金额(金额)*/
    @NotNull(message = "成交金额(金额)不可为空")
    @ApiModelProperty(value = "成交金额(金额)", required = true)
    private java.math.BigDecimal dealBalance;
    
    /**成交手续费*/
    @NotNull(message = "成交手续费不可为空")
    @ApiModelProperty(value = "成交手续费", required = true)
    private java.math.BigDecimal dealFee;
    
    /**状态(初始化(未进撮合,不会在盘口上体现出来,避免出现交叉单)init、未成交noDeal、部分成交partialDeal、全部成交allDeal、已撤单withdrawed、异常委托abnormal)*/
    @NotNull(message = "状态(初始化(未进撮合,不会在盘口上体现出来,避免出现交叉单)init、未成交noDeal、部分成交partialDeal、全部成交allDeal、已撤单withdrawed、异常委托abnormal)不可为空")
    @ApiModelProperty(value = "状态(初始化(未进撮合,不会在盘口上体现出来,避免出现交叉单)init、未成交noDeal、部分成交partialDeal、全部成交allDeal、已撤单withdrawed、异常委托abnormal)", required = true)
    private String               status;
    
    /**最后更新时间*/
    @NotNull(message = "最后更新时间不可为空")
    @ApiModelProperty(value = "最后更新时间", required = true)
    private java.sql.Timestamp   updateTime;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String               remark;
    
    /**委托交易对类型*/
    private String               tableName;
    
    /**证券代码 */
    private String               stockCode;

    /**证券代码 */
    private String               stockName;
    
    /**用户界面传值 查询开始时间 */
    private String               timeStart;
    
    /**用户界面传值 查询结束时间 */
    private String               timeEnd;
    
    public Long getAccountId()
    {
        return this.accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public java.util.Date getEntrustTime()
    {
        return this.entrustTime;
    }
    
    public void setEntrustTime(java.sql.Timestamp entrustTime)
    {
        this.entrustTime = entrustTime;
    }
    
    public String getEntrustSource()
    {
        return this.entrustSource;
    }
    
    public void setEntrustSource(String entrustSource)
    {
        this.entrustSource = entrustSource;
    }
    
    public String getBusinessFlag()
    {
        return this.businessFlag;
    }
    
    public void setBusinessFlag(String businessFlag)
    {
        this.businessFlag = businessFlag;
    }
    
    public String getTradeType()
    {
        return this.tradeType;
    }
    
    public void setTradeType(String tradeType)
    {
        this.tradeType = tradeType;
    }
    
    public String getEntrustType()
    {
        return this.entrustType;
    }
    
    public void setEntrustType(String entrustType)
    {
        this.entrustType = entrustType;
    }
    
    public String getEntrustDirect()
    {
        return this.entrustDirect;
    }
    
    public void setEntrustDirect(String entrustDirect)
    {
        this.entrustDirect = entrustDirect;
    }
    
    public Long getEntrustStockinfoId()
    {
        return this.entrustStockinfoId;
    }
    
    public void setEntrustStockinfoId(Long entrustStockinfoId)
    {
        this.entrustStockinfoId = entrustStockinfoId;
    }
    
    public java.math.BigDecimal getEntrustAmt()
    {
        return this.entrustAmt;
    }
    
    public void setEntrustAmt(java.math.BigDecimal entrustAmt)
    {
        this.entrustAmt = entrustAmt;
    }
    
    public java.math.BigDecimal getEntrustPrice()
    {
        return this.entrustPrice;
    }
    
    public void setEntrustPrice(java.math.BigDecimal entrustPrice)
    {
        this.entrustPrice = entrustPrice;
    }
    
    public String getEntrustRemark()
    {
        return this.entrustRemark;
    }
    
    public void setEntrustRemark(String entrustRemark)
    {
        this.entrustRemark = entrustRemark;
    }
    
    public Boolean getEntrustAccountType()
    {
        return this.entrustAccountType;
    }
    
    public void setEntrustAccountType(Boolean entrustAccountType)
    {
        this.entrustAccountType = entrustAccountType;
    }
    
    public java.math.BigDecimal getFeeRate()
    {
        return this.feeRate;
    }
    
    public void setFeeRate(java.math.BigDecimal feeRate)
    {
        this.feeRate = feeRate;
    }
    
    public Long getFeeStockinfoId()
    {
        return this.feeStockinfoId;
    }
    
    public void setFeeStockinfoId(Long feeStockinfoId)
    {
        this.feeStockinfoId = feeStockinfoId;
    }
    
    public java.math.BigDecimal getDealAmt()
    {
        return this.dealAmt;
    }
    
    public void setDealAmt(java.math.BigDecimal dealAmt)
    {
        this.dealAmt = dealAmt;
    }
    
    public BigDecimal getDealBalance()
    {
        return dealBalance;
    }
    
    public void setDealBalance(BigDecimal dealBalance)
    {
        this.dealBalance = dealBalance;
    }
    
    public java.math.BigDecimal getDealFee()
    {
        return this.dealFee;
    }
    
    public void setDealFee(java.math.BigDecimal dealFee)
    {
        this.dealFee = dealFee;
    }
    
    public String getStatus()
    {
        return this.status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public java.sql.Timestamp getUpdateTime()
    {
        return this.updateTime;
    }
    
    public void setUpdateTime(java.sql.Timestamp updateTime)
    {
        this.updateTime = updateTime;
    }
    
    public String getRemark()
    {
        return this.remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public String getAccountName()
    {
        return accountName;
    }
    
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
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
    
    public String getStockCode()
    {
        return stockCode;
    }
    
    public void setStockCode(String stockCode)
    {
        this.stockCode = stockCode;
    }
    
    public String getTableName()
    {
        return tableName;
    }
    
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }
    
    public Long getEntrustRelatedStockinfoId()
    {
        return entrustRelatedStockinfoId;
    }
    
    public void setEntrustRelatedStockinfoId(Long entrustRelatedStockinfoId)
    {
        this.entrustRelatedStockinfoId = entrustRelatedStockinfoId;
    }

    public String getStockName()
    {
        return stockName;
    }

    public void setStockName(String stockName)
    {
        this.stockName = stockName;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("EntrustVCoinMoney{");
        sb.append("id=").append(id);
        sb.append(", accountId=").append(accountId);
        sb.append(", accountName='").append(accountName).append('\'');
        sb.append(", entrustTime=").append(entrustTime);
        sb.append(", entrustSource='").append(entrustSource).append('\'');
        sb.append(", businessFlag='").append(businessFlag).append('\'');
        sb.append(", tradeType='").append(tradeType).append('\'');
        sb.append(", entrustType='").append(entrustType).append('\'');
        sb.append(", entrustDirect='").append(entrustDirect).append('\'');
        sb.append(", entrustStockinfoId=").append(entrustStockinfoId);
        sb.append(", entrustRelatedStockinfoId=").append(entrustRelatedStockinfoId);
        sb.append(", entrustAmt=").append(entrustAmt);
        sb.append(", entrustPrice=").append(entrustPrice);
        sb.append(", entrustRemark='").append(entrustRemark).append('\'');
        sb.append(", entrustAccountType=").append(entrustAccountType);
        sb.append(", feeRate=").append(feeRate);
        sb.append(", feeStockinfoId=").append(feeStockinfoId);
        sb.append(", dealAmt=").append(dealAmt);
        sb.append(", dealBalance=").append(dealBalance);
        sb.append(", dealFee=").append(dealFee);
        sb.append(", status='").append(status).append('\'');
        sb.append(", updateTime=").append(updateTime);
        sb.append(", remark='").append(remark).append('\'');
        sb.append(", tableName='").append(tableName).append('\'');
        sb.append(", stockCode='").append(stockCode).append('\'');
        sb.append(", timeStart='").append(timeStart).append('\'');
        sb.append(", timeEnd='").append(timeEnd).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

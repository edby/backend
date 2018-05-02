/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.trade.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>File：EntrustModel.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月22日 上午10:35:44</p>
 * <p>Company: BloCain</p>
 * @author sunbiao
 * @version 1.0
 */
public class EntrustModel implements Serializable
{
    //
    private static final long serialVersionUID   = 6541545936709826221L;
    
    // 帐户ID
    public Long               accountId;
    
    // 交易类型(汇兑Push交易、集市交易、报价交易、撮合交易)
    public String             tradeType;
    
    // 交易对手ID
    public Long               rivalId;
    
    // 委托方向(现货买入、现货卖出、期货多头开仓、多头平仓、空头开仓、空头平仓)
    public String             entrustDirect;
    
    // 证券信息ID
    public Long               stockinfoId;
    
    // 委托数量
    @Min(0)
    @Max(999999)
    public BigDecimal         entrustAmt;
    
    // 委托价格
    @Min(0)
    @Max(999999)
    public BigDecimal         entrustPrice;
    
    // 委托结束日期
    public Long               entrustEndDate;
    
    // 委托备注
    public String             entrustRemark;
    
    // 证券信息ID扩展
    public Long               stockinfoIdEx;
    
    // 委托数量扩展
    @Min(0)
    @Max(999999)
    public BigDecimal         entrustAmtEx;
    
    // 手续费扩展
    @Min(0)
    @Max(999)
    public BigDecimal         fee;
    
    // 手续费费率
    @Min(0)
    @Max(1)
    public BigDecimal         feeRate            = BigDecimal.ZERO;
    
    /**委托类型(限价limitPrice、市价marketPrice)*/
    private String            entrustType;
    
    // 委托记录ID
    public Long               entrustId;
    
    /**委托账户类型(0用户委托、1平台委托)*/
    private Boolean           entrustAccountType = false;               // 默认用户下单
    
    /**委托交易对类型*/
    private String            tableName;
    
    public Long getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public String getTradeType()
    {
        return tradeType;
    }
    
    public void setTradeType(String tradeType)
    {
        this.tradeType = tradeType;
    }
    
    public Long getRivalId()
    {
        return rivalId;
    }
    
    public void setRivalId(Long rivalId)
    {
        this.rivalId = rivalId;
    }
    
    public String getEntrustDirect()
    {
        return entrustDirect;
    }
    
    public void setEntrustDirect(String entrustDirect)
    {
        this.entrustDirect = entrustDirect;
    }
    
    public Long getStockinfoId()
    {
        return stockinfoId;
    }
    
    public void setStockinfoId(Long stockinfoId)
    {
        this.stockinfoId = stockinfoId;
    }
    
    public BigDecimal getEntrustAmt()
    {
        return entrustAmt;
    }
    
    public void setEntrustAmt(BigDecimal entrustAmt)
    {
        this.entrustAmt = entrustAmt;
    }
    
    public BigDecimal getEntrustPrice()
    {
        return entrustPrice;
    }
    
    public void setEntrustPrice(BigDecimal entrustPrice)
    {
        this.entrustPrice = entrustPrice;
    }
    
    public Long getEntrustEndDate()
    {
        return entrustEndDate;
    }
    
    public void setEntrustEndDate(Long entrustEndDate)
    {
        this.entrustEndDate = entrustEndDate;
    }
    
    public String getEntrustRemark()
    {
        return entrustRemark;
    }
    
    public void setEntrustRemark(String entrustRemark)
    {
        this.entrustRemark = entrustRemark;
    }
    
    public Long getStockinfoIdEx()
    {
        return stockinfoIdEx;
    }
    
    public void setStockinfoIdEx(Long stockinfoIdEx)
    {
        this.stockinfoIdEx = stockinfoIdEx;
    }
    
    public BigDecimal getEntrustAmtEx()
    {
        return entrustAmtEx;
    }
    
    public void setEntrustAmtEx(BigDecimal entrustAmtEx)
    {
        this.entrustAmtEx = entrustAmtEx;
    }
    
    public BigDecimal getFee()
    {
        return fee;
    }
    
    public void setFee(BigDecimal fee)
    {
        this.fee = fee;
    }
    
    public Long getEntrustId()
    {
        return entrustId;
    }
    
    public void setEntrustId(Long entrustId)
    {
        this.entrustId = entrustId;
    }
    
    public Boolean getEntrustAccountType()
    {
        return entrustAccountType;
    }
    
    public void setEntrustAccountType(Boolean entrustAccountType)
    {
        this.entrustAccountType = entrustAccountType;
    }
    
    public String getEntrustType()
    {
        return entrustType;
    }
    
    public void setEntrustType(String entrustType)
    {
        this.entrustType = entrustType;
    }
    
    public BigDecimal getFeeRate()
    {
        return feeRate;
    }
    
    public void setFeeRate(BigDecimal feeRate)
    {
        this.feeRate = feeRate;
    }
    
    public String getTableName()
    {
        return tableName;
    }
    
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("EntrustModel{");
        sb.append("accountId=").append(accountId);
        sb.append(", tradeType='").append(tradeType).append('\'');
        sb.append(", rivalId=").append(rivalId);
        sb.append(", entrustDirect='").append(entrustDirect).append('\'');
        sb.append(", stockinfoId=").append(stockinfoId);
        sb.append(", entrustAmt=").append(entrustAmt);
        sb.append(", entrustPrice=").append(entrustPrice);
        sb.append(", entrustEndDate=").append(entrustEndDate);
        sb.append(", entrustRemark='").append(entrustRemark).append('\'');
        sb.append(", stockinfoIdEx=").append(stockinfoIdEx);
        sb.append(", entrustAmtEx=").append(entrustAmtEx);
        sb.append(", fee=").append(fee);
        sb.append(", feeRate=").append(feeRate);
        sb.append(", entrustType='").append(entrustType).append('\'');
        sb.append(", entrustId=").append(entrustId);
        sb.append(", entrustAccountType=").append(entrustAccountType);
        sb.append(", tableName='").append(tableName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

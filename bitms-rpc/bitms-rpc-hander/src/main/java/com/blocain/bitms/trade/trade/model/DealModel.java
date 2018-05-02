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
 * <p>File：DealModel.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月22日 上午10:35:44</p>
 * <p>Company: BloCain</p>
 * @author sunbiao
 * @version 1.0
 */
public class DealModel implements Serializable
{
    //
    private static final long serialVersionUID = 6541545936709826221L;
    
    // 帐户ID
    public Long               accountId;
    
    // 交易类型(汇兑Push交易、集市交易、报价交易、撮合交易)
    public String             tradeType;
    
    // 交易方向(现货买入、现货卖出、期货多头开仓、多头平仓、空头开仓、空头平仓)
    public String             dealDirect;
    
    // 证券信息ID
    public Long               stockinfoId;
    
    // 交易数量
    @Min(0)
    @Max(999999)
    public BigDecimal         dealAmt;
    
    // 交易价格
    @Min(0)
    @Max(999999)
    public BigDecimal         dealPrice;
    
    // 手续费
    @Min(0)
    @Max(999)
    public BigDecimal         fee;
    
    // 交易备注
    public String             dealRemark;
    
    // 证券信息ID扩展
    public Long               stockinfoIdEx;
    
    // 交易数量扩展
    @Min(0)
    @Max(999999)
    public BigDecimal         dealAmtEx;
    
    // 手续费扩展
    @Min(0)
    @Max(999)
    public BigDecimal         feeEx;
    
    // 委托记录ID
    public Long               entrustId;
    
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
    
    public String getDealDirect()
    {
        return dealDirect;
    }
    
    public void setDealDirect(String dealDirect)
    {
        this.dealDirect = dealDirect;
    }
    
    public Long getStockinfoId()
    {
        return stockinfoId;
    }
    
    public void setStockinfoId(Long stockinfoId)
    {
        this.stockinfoId = stockinfoId;
    }
    
    public BigDecimal getDealAmt()
    {
        return dealAmt;
    }
    
    public void setDealAmt(BigDecimal dealAmt)
    {
        this.dealAmt = dealAmt;
    }
    
    public BigDecimal getDealPrice()
    {
        return dealPrice;
    }
    
    public void setDealPrice(BigDecimal dealPrice)
    {
        this.dealPrice = dealPrice;
    }
    
    public BigDecimal getFee()
    {
        return fee;
    }
    
    public void setFee(BigDecimal fee)
    {
        this.fee = fee;
    }
    
    public String getDealRemark()
    {
        return dealRemark;
    }
    
    public void setDealRemark(String dealRemark)
    {
        this.dealRemark = dealRemark;
    }
    
    public Long getStockinfoIdEx()
    {
        return stockinfoIdEx;
    }
    
    public void setStockinfoIdEx(Long stockinfoIdEx)
    {
        this.stockinfoIdEx = stockinfoIdEx;
    }
    
    public BigDecimal getDealAmtEx()
    {
        return dealAmtEx;
    }
    
    public void setDealAmtEx(BigDecimal dealAmtEx)
    {
        this.dealAmtEx = dealAmtEx;
    }
    
    public BigDecimal getFeeEx()
    {
        return feeEx;
    }
    
    public void setFeeEx(BigDecimal feeEx)
    {
        this.feeEx = feeEx;
    }
    
    public Long getEntrustId()
    {
        return entrustId;
    }
    
    public void setEntrustId(Long entrustId)
    {
        this.entrustId = entrustId;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("DealModel{");
        sb.append("accountId=").append(accountId);
        sb.append(", tradeType='").append(tradeType).append('\'');
        sb.append(", dealDirect='").append(dealDirect).append('\'');
        sb.append(", stockinfoId=").append(stockinfoId);
        sb.append(", dealAmt=").append(dealAmt);
        sb.append(", dealPrice=").append(dealPrice);
        sb.append(", fee=").append(fee);
        sb.append(", dealRemark='").append(dealRemark).append('\'');
        sb.append(", stockinfoIdEx=").append(stockinfoIdEx);
        sb.append(", dealAmtEx=").append(dealAmtEx);
        sb.append(", feeEx=").append(feeEx);
        sb.append(", entrustId=").append(entrustId);
        sb.append('}');
        return sb.toString();
    }
}

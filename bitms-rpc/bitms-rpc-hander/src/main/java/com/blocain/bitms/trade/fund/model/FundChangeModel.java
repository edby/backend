package com.blocain.bitms.trade.fund.model;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>File：FundChangeModel.java</p>
 * <p>Title:FundChangeModel </p>
 * <p>Description:资金变动流水</p>
 * <p>Copyright: Copyright (c) 2017年7月22日 上午10:35:44</p>
 * <p>Company: BloCain</p>
 * @author Jiangsc
 * @version 1.0
 */
public class FundChangeModel implements Serializable
{
    private static final long serialVersionUID = 6541545936709826221L;
    
    public Long               accountId;
    
    public BigDecimal         btcNetValue;                            // 账户净值
    
    public BigDecimal         profitAndLoss;                          // 盈亏
    
    public BigDecimal         riskRate;                               // 风险率
    
    public BigDecimal         explosionPrice;                         // 强平价
    
    public String             settlementTimeNo;                       // 清算时间
    
    public String             direction;                              // 持仓方向
    
    public BigDecimal         usdxPosition;                           // USDX仓位数量
    
    public BigDecimal         usdxPositionValue;                      // USDX仓位价值
    
    public BigDecimal         usdxPositionAvg;                        // USDX持仓均价
    
    public BigDecimal         usdxAmtBalance;                         // USDX余额(可用)
    
    public BigDecimal         usdxFrozen;                             // USDX冻结
    
    public BigDecimal         btcAmtBalance;                          // BTC余额(可用)
    
    public BigDecimal         btcFrozen;                              // BTC冻结
    
    public BigDecimal         btcBuyMaxCnt;                           // 最大可买
    
    public BigDecimal         btcMaxBorrow;                           // BTC最大可借
    
    public BigDecimal         btcSellMaxCnt;                          // 最大可卖
    
    public BigDecimal         btcBuyMaxCntBalance;                    // 最大可买
    
    public BigDecimal         buyBtcFeeRate;                          // 费率
    
    public BigDecimal         sellBtcFeeRate;                         // 费率
    
    public BigDecimal         usdxLever;                              // 法定货币最大可贷款杠杆倍数
    
    public BigDecimal         btcLever;                               // 数字货币最大可贷款杠杆倍数
    
    public BigDecimal         usdxBorrow;                             // 法定货币借款
    
    public BigDecimal         usdxAmount;                             // 法定货币账户资产
    
    public BigDecimal         btcBorrow;                              // 数字货币借款
    
    public BigDecimal         btcAmount;                              // 数字货币账户资产
    
    public BigDecimal         btcBeginning;                           // 期初
    
    public BigDecimal         btcSumIn;                               // 总流入
    
    public BigDecimal         btcSumOut;                              // 总流出
    
    /**交易标的证券数量单位*/
    private java.lang.String  tradeAmtUnit;
    
    /**资金计价单位证券数量单位*/
    private java.lang.String  capitalAmtUnit;
    
    public Long getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public BigDecimal getBtcNetValue()
    {
        return btcNetValue;
    }
    
    public void setBtcNetValue(BigDecimal btcNetValue)
    {
        this.btcNetValue = btcNetValue;
    }
    
    public BigDecimal getProfitAndLoss()
    {
        return profitAndLoss;
    }
    
    public void setProfitAndLoss(BigDecimal profitAndLoss)
    {
        this.profitAndLoss = profitAndLoss;
    }
    
    public BigDecimal getRiskRate()
    {
        return riskRate;
    }
    
    public void setRiskRate(BigDecimal riskRate)
    {
        this.riskRate = riskRate;
    }
    
    public BigDecimal getExplosionPrice()
    {
        return explosionPrice;
    }
    
    public void setExplosionPrice(BigDecimal explosionPrice)
    {
        this.explosionPrice = explosionPrice;
    }
    
    public String getSettlementTimeNo()
    {
        return settlementTimeNo;
    }
    
    public void setSettlementTimeNo(String settlementTimeNo)
    {
        this.settlementTimeNo = settlementTimeNo;
    }
    
    public String getDirection()
    {
        return direction;
    }
    
    public void setDirection(String direction)
    {
        this.direction = direction;
    }
    
    public BigDecimal getUsdxPosition()
    {
        return usdxPosition;
    }
    
    public void setUsdxPosition(BigDecimal usdxPosition)
    {
        this.usdxPosition = usdxPosition;
    }
    
    public BigDecimal getUsdxPositionValue()
    {
        return usdxPositionValue;
    }
    
    public void setUsdxPositionValue(BigDecimal usdxPositionValue)
    {
        this.usdxPositionValue = usdxPositionValue;
    }
    
    public BigDecimal getUsdxPositionAvg()
    {
        return usdxPositionAvg;
    }
    
    public void setUsdxPositionAvg(BigDecimal usdxPositionAvg)
    {
        this.usdxPositionAvg = usdxPositionAvg;
    }
    
    public BigDecimal getUsdxAmtBalance()
    {
        return usdxAmtBalance;
    }
    
    public void setUsdxAmtBalance(BigDecimal usdxAmtBalance)
    {
        this.usdxAmtBalance = usdxAmtBalance;
    }
    
    public BigDecimal getUsdxFrozen()
    {
        return usdxFrozen;
    }
    
    public void setUsdxFrozen(BigDecimal usdxFrozen)
    {
        this.usdxFrozen = usdxFrozen;
    }
    
    public BigDecimal getBtcAmtBalance()
    {
        return btcAmtBalance;
    }
    
    public void setBtcAmtBalance(BigDecimal btcAmtBalance)
    {
        this.btcAmtBalance = btcAmtBalance;
    }
    
    public BigDecimal getBtcFrozen()
    {
        return btcFrozen;
    }
    
    public void setBtcFrozen(BigDecimal btcFrozen)
    {
        this.btcFrozen = btcFrozen;
    }
    
    public BigDecimal getBtcBuyMaxCnt()
    {
        return btcBuyMaxCnt;
    }
    
    public void setBtcBuyMaxCnt(BigDecimal btcBuyMaxCnt)
    {
        this.btcBuyMaxCnt = btcBuyMaxCnt;
    }
    
    public BigDecimal getBtcSellMaxCnt()
    {
        return btcSellMaxCnt;
    }
    
    public void setBtcSellMaxCnt(BigDecimal btcSellMaxCnt)
    {
        this.btcSellMaxCnt = btcSellMaxCnt;
    }
    
    public BigDecimal getBtcBuyMaxCntBalance()
    {
        return btcBuyMaxCntBalance;
    }
    
    public void setBtcBuyMaxCntBalance(BigDecimal btcBuyMaxCntBalance)
    {
        this.btcBuyMaxCntBalance = btcBuyMaxCntBalance;
    }
    
    public BigDecimal getBuyBtcFeeRate()
    {
        return buyBtcFeeRate;
    }
    
    public void setBuyBtcFeeRate(BigDecimal buyBtcFeeRate)
    {
        this.buyBtcFeeRate = buyBtcFeeRate;
    }
    
    public BigDecimal getSellBtcFeeRate()
    {
        return sellBtcFeeRate;
    }
    
    public void setSellBtcFeeRate(BigDecimal sellBtcFeeRate)
    {
        this.sellBtcFeeRate = sellBtcFeeRate;
    }
    
    public BigDecimal getUsdxLever()
    {
        return usdxLever;
    }
    
    public void setUsdxLever(BigDecimal usdxLever)
    {
        this.usdxLever = usdxLever;
    }
    
    public BigDecimal getBtcLever()
    {
        return btcLever;
    }
    
    public void setBtcLever(BigDecimal btcLever)
    {
        this.btcLever = btcLever;
    }
    
    public BigDecimal getUsdxBorrow()
    {
        return usdxBorrow;
    }
    
    public void setUsdxBorrow(BigDecimal usdxBorrow)
    {
        this.usdxBorrow = usdxBorrow;
    }
    
    public BigDecimal getUsdxAmount()
    {
        return usdxAmount;
    }
    
    public void setUsdxAmount(BigDecimal usdxAmount)
    {
        this.usdxAmount = usdxAmount;
    }
    
    public BigDecimal getBtcBorrow()
    {
        return btcBorrow;
    }
    
    public void setBtcBorrow(BigDecimal btcBorrow)
    {
        this.btcBorrow = btcBorrow;
    }
    
    public BigDecimal getBtcAmount()
    {
        return btcAmount;
    }
    
    public void setBtcAmount(BigDecimal btcAmount)
    {
        this.btcAmount = btcAmount;
    }
    
    public BigDecimal getBtcBeginning()
    {
        return btcBeginning;
    }
    
    public void setBtcBeginning(BigDecimal btcBeginning)
    {
        this.btcBeginning = btcBeginning;
    }
    
    public BigDecimal getBtcSumIn()
    {
        return btcSumIn;
    }
    
    public void setBtcSumIn(BigDecimal btcSumIn)
    {
        this.btcSumIn = btcSumIn;
    }
    
    public BigDecimal getBtcSumOut()
    {
        return btcSumOut;
    }
    
    public void setBtcSumOut(BigDecimal btcSumOut)
    {
        this.btcSumOut = btcSumOut;
    }
    
    public BigDecimal getBtcMaxBorrow()
    {
        return btcMaxBorrow;
    }
    
    public void setBtcMaxBorrow(BigDecimal btcMaxBorrow)
    {
        this.btcMaxBorrow = btcMaxBorrow;
    }
    
    public String getTradeAmtUnit()
    {
        return tradeAmtUnit;
    }
    
    public void setTradeAmtUnit(String tradeAmtUnit)
    {
        this.tradeAmtUnit = tradeAmtUnit;
    }
    
    public String getCapitalAmtUnit()
    {
        return capitalAmtUnit;
    }
    
    public void setCapitalAmtUnit(String capitalAmtUnit)
    {
        this.capitalAmtUnit = capitalAmtUnit;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("FundChangeModel{");
        sb.append("accountId=").append(accountId);
        sb.append(", btcNetValue=").append(btcNetValue);
        sb.append(", profitAndLoss=").append(profitAndLoss);
        sb.append(", riskRate=").append(riskRate);
        sb.append(", explosionPrice=").append(explosionPrice);
        sb.append(", settlementTimeNo='").append(settlementTimeNo).append('\'');
        sb.append(", direction='").append(direction).append('\'');
        sb.append(", usdxPosition=").append(usdxPosition);
        sb.append(", usdxPositionValue=").append(usdxPositionValue);
        sb.append(", usdxPositionAvg=").append(usdxPositionAvg);
        sb.append(", usdxAmtBalance=").append(usdxAmtBalance);
        sb.append(", usdxFrozen=").append(usdxFrozen);
        sb.append(", btcAmtBalance=").append(btcAmtBalance);
        sb.append(", btcFrozen=").append(btcFrozen);
        sb.append(", btcBuyMaxCnt=").append(btcBuyMaxCnt);
        sb.append(", btcMaxBorrow=").append(btcMaxBorrow);
        sb.append(", btcSellMaxCnt=").append(btcSellMaxCnt);
        sb.append(", btcBuyMaxCntBalance=").append(btcBuyMaxCntBalance);
        sb.append(", buyBtcFeeRate=").append(buyBtcFeeRate);
        sb.append(", sellBtcFeeRate=").append(sellBtcFeeRate);
        sb.append(", usdxLever=").append(usdxLever);
        sb.append(", btcLever=").append(btcLever);
        sb.append(", usdxBorrow=").append(usdxBorrow);
        sb.append(", usdxAmount=").append(usdxAmount);
        sb.append(", btcBorrow=").append(btcBorrow);
        sb.append(", btcAmount=").append(btcAmount);
        sb.append(", btcBeginning=").append(btcBeginning);
        sb.append(", btcSumIn=").append(btcSumIn);
        sb.append(", btcSumOut=").append(btcSumOut);
        sb.append('}');
        return sb.toString();
    }
}

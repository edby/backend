/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.blocain.bitms.orm.core.GenericEntity;
import javax.validation.constraints.NotNull;

/**
 * 账户理财资产表 实体对象
 * <p>File：AccountWealthAsset.java</p>
 * <p>Title: AccountWealthAsset</p>
 * <p>Description:AccountWealthAsset</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "账户理财资产表")
public class AccountWealthAsset extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**理财投资账户ID*/
    @NotNull(message = "理财投资账户ID不可为空")
    @ApiModelProperty(value = "理财投资账户ID", required = true)
    private Long                 wealthAccountId;
    
    /**理财发行账户ID*/
    @NotNull(message = "理财发行账户ID不可为空")
    @ApiModelProperty(value = "理财发行账户ID", required = true)
    private Long                 issuerAccountId;
    
    /**借贷证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "借贷证券信息id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "借贷证券信息id 对应Stockinfo表中的ID字段", required = true)
    private Long                 stockinfoId;
    
    /**借贷证券信息关联id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "借贷证券信息关联id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "借贷证券信息关联id 对应Stockinfo表中的ID字段", required = true)
    private Long                 relatedStockinfoId;
    
    /**理财财富数量或金额*/
    @NotNull(message = "理财财富数量或金额不可为空")
    @ApiModelProperty(value = "理财财富数量或金额", required = true)
    private java.math.BigDecimal wealthAmt;
    
    /**累计利息*/
    @NotNull(message = "累计利息不可为空")
    @ApiModelProperty(value = "累计利息", required = true)
    private java.math.BigDecimal accumulateInterest;
    
    /**最后计息日*/
    @NotNull(message = "最后计息日不可为空")
    @ApiModelProperty(value = "最后计息日", required = true)
    private Long                 lastInterestDay;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String               remark;
    
    /**修改时间*/
    @NotNull(message = "修改时间不可为空")
    @ApiModelProperty(value = "修改时间", required = true)
    private java.util.Date       updateDate;
    
    private String               stockCode;

    private String               stockName;
    
    private String               relatedStockName;
    
    private String               accountName;
    
    /**能否充值(yes可以 no不能)*/
    private String               canRecharge;
    
    /**能否提现(yes可以 no不能)*/
    private String               canWithdraw;
    
    /**能否交易(yes可以 no不能)*/
    private String               canTrade;
    
    /**能否可借(yes可以 no不能)*/
    private String               canBorrow;
    
    /**是否是交易对(yes是 no不是)*/
    private String               isExchange;
    
    /**能否互转(yes可以 no不能)*/
    private String               canConversion;
    
    /** /**能否理财(yes可以 no不能)*/
    private String               canWealth;
    
    public Long getWealthAccountId()
    {
        return this.wealthAccountId;
    }
    
    public void setWealthAccountId(Long wealthAccountId)
    {
        this.wealthAccountId = wealthAccountId;
    }
    
    public Long getIssuerAccountId()
    {
        return this.issuerAccountId;
    }
    
    public void setIssuerAccountId(Long issuerAccountId)
    {
        this.issuerAccountId = issuerAccountId;
    }
    
    public Long getStockinfoId()
    {
        return this.stockinfoId;
    }
    
    public void setStockinfoId(Long stockinfoId)
    {
        this.stockinfoId = stockinfoId;
    }
    
    public Long getRelatedStockinfoId()
    {
        return this.relatedStockinfoId;
    }
    
    public void setRelatedStockinfoId(Long relatedStockinfoId)
    {
        this.relatedStockinfoId = relatedStockinfoId;
    }
    
    public java.math.BigDecimal getWealthAmt()
    {
        return this.wealthAmt;
    }
    
    public void setWealthAmt(java.math.BigDecimal wealthAmt)
    {
        this.wealthAmt = wealthAmt;
    }
    
    public java.math.BigDecimal getAccumulateInterest()
    {
        return this.accumulateInterest;
    }
    
    public void setAccumulateInterest(java.math.BigDecimal accumulateInterest)
    {
        this.accumulateInterest = accumulateInterest;
    }
    
    public Long getLastInterestDay()
    {
        return this.lastInterestDay;
    }
    
    public void setLastInterestDay(Long lastInterestDay)
    {
        this.lastInterestDay = lastInterestDay;
    }
    
    public String getRemark()
    {
        return this.remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public java.util.Date getUpdateDate()
    {
        return this.updateDate;
    }
    
    public void setUpdateDate(java.util.Date updateDate)
    {
        this.updateDate = updateDate;
    }
    
    public String getStockCode()
    {
        return stockCode;
    }
    
    public void setStockCode(String stockCode)
    {
        this.stockCode = stockCode;
    }
    
    public String getAccountName()
    {
        return accountName;
    }
    
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
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
    
    public String getIsExchange()
    {
        return isExchange;
    }
    
    public void setIsExchange(String isExchange)
    {
        this.isExchange = isExchange;
    }
    
    public String getCanConversion()
    {
        return canConversion;
    }
    
    public void setCanConversion(String canConversion)
    {
        this.canConversion = canConversion;
    }
    
    public String getRelatedStockName()
    {
        return relatedStockName;
    }
    
    public void setRelatedStockName(String relatedStockName)
    {
        this.relatedStockName = relatedStockName;
    }
    
    public String getCanWealth()
    {
        return canWealth;
    }
    
    public void setCanWealth(String canWealth)
    {
        this.canWealth = canWealth;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("AccountWealthAsset{");
        sb.append("id=").append(id);
        sb.append(", wealthAccountId=").append(wealthAccountId);
        sb.append(", issuerAccountId=").append(issuerAccountId);
        sb.append(", stockinfoId=").append(stockinfoId);
        sb.append(", relatedStockinfoId=").append(relatedStockinfoId);
        sb.append(", wealthAmt=").append(wealthAmt);
        sb.append(", accumulateInterest=").append(accumulateInterest);
        sb.append(", lastInterestDay=").append(lastInterestDay);
        sb.append(", remark='").append(remark).append('\'');
        sb.append(", updateDate=").append(updateDate);
        sb.append(", stockCode='").append(stockCode).append('\'');
        sb.append(", relatedStockName='").append(relatedStockName).append('\'');
        sb.append(", accountName='").append(accountName).append('\'');
        sb.append(", canRecharge='").append(canRecharge).append('\'');
        sb.append(", canWithdraw='").append(canWithdraw).append('\'');
        sb.append(", canTrade='").append(canTrade).append('\'');
        sb.append(", canBorrow='").append(canBorrow).append('\'');
        sb.append(", isExchange='").append(isExchange).append('\'');
        sb.append(", canConversion='").append(canConversion).append('\'');
        sb.append(", canWealth='").append(canWealth).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

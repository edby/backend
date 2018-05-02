/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.model;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>File：DebitAssetModel.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月11日 上午10:35:44</p>
 * <p>Company: BloCain</p>
 * @author sunbiao
 * @version 1.0
 */
public class DebitAssetModel implements Serializable
{
    //
    private static final long    serialVersionUID = 6541545936709826208L;
    
    /**贷款ID*/
    private Long                 debitRecordId;
    
    /**借款人账户ID*/
    private Long                 borrowerAccountId;
    
    /**贷款人账户ID*/
    private Long                 lenderAccountId;
    
    /**借贷证券信息id 对应Stockinfo表中的ID字段 借款时用做：与关联证券相对应的数字货币*/
    private Long                 stockinfoId;
    
    /**关联证券信息id 对应Stockinfo表中的ID字段*/
    private Long                 RelatedStockinfoId;
    
    /**借贷数量或金额*/
    private java.math.BigDecimal debitAmt;
    
    /**累计借贷数量或金额*/
    private java.math.BigDecimal debitAmtSum;
    
    /**所借数字货币的类型*/
    private String               stockinfoType;
    
    /**做多最大杠杆*/
    @NotNull(message = "做多最大杠杆不可为空")
    @ApiModelProperty(value = "做多最大杠杆", required = true)
    private BigDecimal           maxLongLever;
    
    /**做空最大杠杆*/
    @NotNull(message = "做空最大杠杆不可为空")
    @ApiModelProperty(value = "做空最大杠杆", required = true)
    private BigDecimal           maxShortLever;

    public Long getBorrowerAccountId()
    {
        return borrowerAccountId;
    }

    public void setBorrowerAccountId(Long borrowerAccountId)
    {
        this.borrowerAccountId = borrowerAccountId;
    }

    public Long getLenderAccountId()
    {
        return lenderAccountId;
    }

    public void setLenderAccountId(Long lenderAccountId)
    {
        this.lenderAccountId = lenderAccountId;
    }

    public Long getStockinfoId()
    {
        return stockinfoId;
    }

    public void setStockinfoId(Long stockinfoId)
    {
        this.stockinfoId = stockinfoId;
    }

    public BigDecimal getDebitAmt()
    {
        return debitAmt;
    }

    public void setDebitAmt(BigDecimal debitAmt)
    {
        this.debitAmt = debitAmt;
    }

    public BigDecimal getDebitAmtSum()
    {
        return debitAmtSum;
    }

    public void setDebitAmtSum(BigDecimal debitAmtSum)
    {
        this.debitAmtSum = debitAmtSum;
    }

    public Long getDebitRecordId()
    {
        return debitRecordId;
    }

    public void setDebitRecordId(Long debitRecordId)
    {
        this.debitRecordId = debitRecordId;
    }

    public Long getRelatedStockinfoId()
    {
        return RelatedStockinfoId;
    }

    public void setRelatedStockinfoId(Long relatedStockinfoId)
    {
        RelatedStockinfoId = relatedStockinfoId;
    }

    public String getStockinfoType()
    {
        return stockinfoType;
    }

    public void setStockinfoType(String stockinfoType)
    {
        this.stockinfoType = stockinfoType;
    }

    public BigDecimal getMaxShortLever()
    {
        return maxShortLever;
    }

    public void setMaxShortLever(BigDecimal maxShortLever)
    {
        this.maxShortLever = maxShortLever;
    }

    public BigDecimal getMaxLongLever()
    {
        return maxLongLever;
    }

    public void setMaxLongLever(BigDecimal maxLongLever)
    {
        this.maxLongLever = maxLongLever;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("DebitAssetModel{");
        sb.append("debitRecordId=").append(debitRecordId);
        sb.append(", borrowerAccountId=").append(borrowerAccountId);
        sb.append(", lenderAccountId=").append(lenderAccountId);
        sb.append(", stockinfoId=").append(stockinfoId);
        sb.append(", RelatedStockinfoId=").append(RelatedStockinfoId);
        sb.append(", debitAmt=").append(debitAmt);
        sb.append(", debitAmtSum=").append(debitAmtSum);
        sb.append(", stockinfoType='").append(stockinfoType).append('\'');
        sb.append(", maxLongLever=").append(maxLongLever);
        sb.append(", maxShortLever=").append(maxShortLever);
        sb.append('}');
        return sb.toString();
    }
}

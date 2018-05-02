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
 * 账户理财资产明细表 实体对象
 * <p>File：AccountWealthAssetDetail.java</p>
 * <p>Title: AccountWealthAssetDetail</p>
 * <p>Description:AccountWealthAssetDetail</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "账户理财资产明细表")
public class AccountWealthAssetDetail extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**借款人账户ID*/
    @NotNull(message = "借款人账户ID不可为空")
    @ApiModelProperty(value = "借款人账户ID", required = true)
    private Long                 wealthAccountId;
    
    /**贷款人账户ID*/
    @NotNull(message = "贷款人账户ID不可为空")
    @ApiModelProperty(value = "贷款人账户ID", required = true)
    private Long                 issuerAccountId;
    
    /**借贷证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "借贷证券信息id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "借贷证券信息id 对应Stockinfo表中的ID字段", required = true)
    private Long                 stockinfoId;
    
    /**借贷证券信息关联id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "借贷证券信息关联id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "借贷证券信息关联id 对应Stockinfo表中的ID字段", required = true)
    private Long                 relatedStockinfoId;
    
    /**借贷数量或金额(总负债)*/
    @NotNull(message = "借贷数量或金额(总负债)不可为空")
    @ApiModelProperty(value = "借贷数量或金额(总负债)", required = true)
    private java.math.BigDecimal wealthAmt;
    
    /**借贷数量或金额(有效负债)*/
    @NotNull(message = "借贷数量或金额(有效负债)不可为空")
    @ApiModelProperty(value = "借贷数量或金额(有效负债)", required = true)
    private java.math.BigDecimal effectiveWealthAmt;
    
    /**借贷日利率*/
    @NotNull(message = "借贷日利率不可为空")
    @ApiModelProperty(value = "借贷日利率", required = true)
    private java.math.BigDecimal wealthDayRate;
    
    /**当日利息*/
    @NotNull(message = "当日利息不可为空")
    @ApiModelProperty(value = "当日利息", required = true)
    private java.math.BigDecimal dayInterest;
    
    /**累计利息*/
    @NotNull(message = "累计利息不可为空")
    @ApiModelProperty(value = "累计利息", required = true)
    private java.math.BigDecimal accumulateInterest;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String               remark;
    
    /**修改时间*/
    @NotNull(message = "修改时间不可为空")
    @ApiModelProperty(value = "修改时间", required = true)
    private java.util.Date       updateDate;

    /**用户界面传值 查询开始时间 */
    private String               timeStart;

    /**用户界面传值 查询结束时间 */
    private String               timeEnd;

    /**证券代码 */
    private String               stockCode;

    /**交易对或专区 */
    private String               tradingZone;
    
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
    
    public java.math.BigDecimal getEffectiveWealthAmt()
    {
        return this.effectiveWealthAmt;
    }
    
    public void setEffectiveWealthAmt(java.math.BigDecimal effectiveWealthAmt)
    {
        this.effectiveWealthAmt = effectiveWealthAmt;
    }
    
    public java.math.BigDecimal getWealthDayRate()
    {
        return this.wealthDayRate;
    }
    
    public void setWealthDayRate(java.math.BigDecimal wealthDayRate)
    {
        this.wealthDayRate = wealthDayRate;
    }
    
    public java.math.BigDecimal getDayInterest()
    {
        return this.dayInterest;
    }
    
    public void setDayInterest(java.math.BigDecimal dayInterest)
    {
        this.dayInterest = dayInterest;
    }
    
    public java.math.BigDecimal getAccumulateInterest()
    {
        return this.accumulateInterest;
    }
    
    public void setAccumulateInterest(java.math.BigDecimal accumulateInterest)
    {
        this.accumulateInterest = accumulateInterest;
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

    public String getTradingZone()
    {
        return tradingZone;
    }

    public void setTradingZone(String tradingZone)
    {
        this.tradingZone = tradingZone;
    }
}

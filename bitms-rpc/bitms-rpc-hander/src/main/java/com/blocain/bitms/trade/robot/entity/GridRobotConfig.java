/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.robot.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.blocain.bitms.orm.core.GenericEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * GridRobotConfig 实体对象
 * <p>File：GridRobotConfig.java</p>
 * <p>Title: GridRobotConfig</p>
 * <p>Description:GridRobotConfig</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "GridRobotConfig")
public class GridRobotConfig extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**configName*/
    @NotNull(message = "configName不可为空")
    @ApiModelProperty(value = "configName", required = true)
    private String               configName;
    
    /**accountId*/
    @NotNull(message = "accountId不可为空")
    @ApiModelProperty(value = "accountId", required = true)
    private Long                 accountId;
    
    /**maxOrderSize*/
    @Max(25)
    @Min(0)
    @Digits(integer = 3, fraction = 0)
    @NotNull(message = "maxOrderSize不可为空")
    @ApiModelProperty(value = "maxOrderSize", required = true)
    private Integer              maxOrderSize;
    
    /**priceGrade*/
    @Max(10000)
    @Min(0)
    @Digits(integer = 5, fraction = 0)
    @NotNull(message = "priceGrade不可为空")
    @ApiModelProperty(value = "priceGrade", required = true)
    private java.math.BigDecimal priceGrade;
    
    /**beginPriceGrade*/
    @Max(10000)
    @Min(0)
    @Digits(integer = 5, fraction = 0)
    @NotNull(message = "beginPriceGrade不可为空")
    @ApiModelProperty(value = "beginPriceGrade", required = true)
    private java.math.BigDecimal beginPriceGrade;
    
    /**amtType*/
    @NotNull(message = "amtType不可为空")
    @ApiModelProperty(value = "amtType", required = true)
    private Integer              amtType;
    
    /**minAmt*/
    @Max(99)
    @Min(0)
    @Digits(integer = 2, fraction = 2)
    @NotNull(message = "minAmt不可为空")
    @ApiModelProperty(value = "minAmt", required = true)
    private java.math.BigDecimal minAmt;
    
    /**maxAmt*/
    @Max(99)
    @Min(0)
    @Digits(integer = 2, fraction = 2)
    @NotNull(message = "maxAmt不可为空")
    @ApiModelProperty(value = "maxAmt", required = true)
    private java.math.BigDecimal maxAmt;
    
    /**avgUpdateLimit*/
    @Max(10000)
    @Min(0)
    @Digits(integer = 5, fraction = 0)
    @NotNull(message = "avgUpdateLimit不可为空")
    @ApiModelProperty(value = "avgUpdateLimit", required = true)
    private java.math.BigDecimal avgUpdateLimit;
    
    /**maxOpenInterest*/
    @NotNull(message = "maxOpenInterest不可为空")
    @ApiModelProperty(value = "maxOpenInterest", required = true)
    private java.math.BigDecimal maxOpenInterest;
    
    /**minOpenInterest*/
    @NotNull(message = "minOpenInterest不可为空")
    @ApiModelProperty(value = "minOpenInterest", required = true)
    private java.math.BigDecimal minOpenInterest;
    
    /**rebuyOpenInterest*/
    @NotNull(message = "rebuyOpenInterest不可为空")
    @ApiModelProperty(value = "rebuyOpenInterest", required = true)
    private java.math.BigDecimal reBuyOpenInterest;
    
    /**reSellOpenInterest*/
    @NotNull(message = "reSellOpenInterest不可为空")
    @ApiModelProperty(value = "reSellOpenInterest", required = true)
    private java.math.BigDecimal reSellOpenInterest;
    
    /**active*/
    @NotNull(message = "active不可为空")
    @ApiModelProperty(value = "active", required = true)
    private Integer              active;
    
    /**updateDate*/
    @ApiModelProperty(value = "updateDate")
    private Date                 updateDate;
    
    /**cancelLimit*/
    @Max(10000)
    @Min(0)
    @Digits(integer = 5, fraction = 0)
    @NotNull(message = "cancelLimit不可为空")
    @ApiModelProperty(value = "cancelLimit", required = true)
    private java.math.BigDecimal cancelLimit;
    
    private String               pairName;

    private Integer robotType;
    
    private BigDecimal           amt;
    
    // 机器人循环执行时间间隔
    private Integer              dt;
    
    // 盘口动态效果控制级别：最大100，最快，最小0，无动态效果
    private Integer              dynLevel;
    
    // 基准价缓存
    public static BigDecimal     basePrice;

    public Integer getRobotType() {
        return robotType;
    }

    public void setRobotType(Integer robotType) {
        this.robotType = robotType;
    }

    public BigDecimal getAmt()
    {
        return amt;
    }
    
    public void setAmt(BigDecimal amt)
    {
        this.amt = amt;
    }
    
    public String getPairName()
    {
        return pairName;
    }
    
    public void setPairName(String pairName)
    {
        this.pairName = pairName;
    }
    
    public String getConfigName()
    {
        return configName;
    }
    
    public void setConfigName(String configName)
    {
        this.configName = configName;
    }
    
    public Long getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public Integer getMaxOrderSize()
    {
        return maxOrderSize;
    }
    
    public void setMaxOrderSize(Integer maxOrderSize)
    {
        this.maxOrderSize = maxOrderSize;
    }
    
    public BigDecimal getPriceGrade()
    {
        return priceGrade;
    }
    
    public void setPriceGrade(BigDecimal priceGrade)
    {
        this.priceGrade = priceGrade;
    }
    
    public BigDecimal getBeginPriceGrade()
    {
        return beginPriceGrade;
    }
    
    public void setBeginPriceGrade(BigDecimal beginPriceGrade)
    {
        this.beginPriceGrade = beginPriceGrade;
    }
    
    public Integer getAmtType()
    {
        return amtType;
    }
    
    public void setAmtType(Integer amtType)
    {
        this.amtType = amtType;
    }
    
    public BigDecimal getMinAmt()
    {
        return minAmt;
    }
    
    public void setMinAmt(BigDecimal minAmt)
    {
        this.minAmt = minAmt;
    }
    
    public BigDecimal getMaxAmt()
    {
        return maxAmt;
    }
    
    public void setMaxAmt(BigDecimal maxAmt)
    {
        this.maxAmt = maxAmt;
    }
    
    public BigDecimal getAvgUpdateLimit()
    {
        return avgUpdateLimit;
    }
    
    public void setAvgUpdateLimit(BigDecimal avgUpdateLimit)
    {
        this.avgUpdateLimit = avgUpdateLimit;
    }
    
    public BigDecimal getMaxOpenInterest()
    {
        return maxOpenInterest;
    }
    
    public void setMaxOpenInterest(BigDecimal maxOpenInterest)
    {
        this.maxOpenInterest = maxOpenInterest;
    }
    
    public BigDecimal getMinOpenInterest()
    {
        return minOpenInterest;
    }
    
    public void setMinOpenInterest(BigDecimal minOpenInterest)
    {
        this.minOpenInterest = minOpenInterest;
    }
    
    public BigDecimal getReBuyOpenInterest()
    {
        return reBuyOpenInterest;
    }
    
    public void setReBuyOpenInterest(BigDecimal reBuyOpenInterest)
    {
        this.reBuyOpenInterest = reBuyOpenInterest;
    }
    
    public BigDecimal getReSellOpenInterest()
    {
        return reSellOpenInterest;
    }
    
    public void setReSellOpenInterest(BigDecimal reSellOpenInterest)
    {
        this.reSellOpenInterest = reSellOpenInterest;
    }
    
    public Integer getActive()
    {
        return active;
    }
    
    public void setActive(Integer active)
    {
        this.active = active;
    }
    
    public Date getUpdateDate()
    {
        return updateDate;
    }
    
    public void setUpdateDate(Date updateDate)
    {
        this.updateDate = updateDate;
    }
    
    public BigDecimal getCancelLimit()
    {
        return cancelLimit;
    }
    
    public void setCancelLimit(BigDecimal cancelLimit)
    {
        this.cancelLimit = cancelLimit;
    }
    
    public Integer getDt()
    {
        return dt;
    }
    
    public void setDt(Integer dt)
    {
        this.dt = dt;
    }
    
    public Integer getDynLevel()
    {
        return dynLevel;
    }
    
    public void setDynLevel(Integer dynLevel)
    {
        this.dynLevel = dynLevel;
    }
    
    @Override
    public String toString()
    {
        return "GridRobotConfig{" + "configName='" + configName + '\'' + ", accountId=" + accountId + ", maxOrderSize=" + maxOrderSize + ", priceGrade=" + priceGrade
                + ", beginPriceGrade=" + beginPriceGrade + ", amtType=" + amtType + ", minAmt=" + minAmt + ", maxAmt=" + maxAmt + ", avgUpdateLimit=" + avgUpdateLimit
                + ", maxOpenInterest=" + maxOpenInterest + ", minOpenInterest=" + minOpenInterest + ", reBuyOpenInterest=" + reBuyOpenInterest + ", reSellOpenInterest="
                + reSellOpenInterest + ", active=" + active + ", updateDate=" + updateDate + ", cancelLimit=" + cancelLimit + ", pairName='" + pairName + '\'' + ", amt="
                + amt + ", dt=" + dt + ", dynLevel=" + dynLevel + '}';
    }
}

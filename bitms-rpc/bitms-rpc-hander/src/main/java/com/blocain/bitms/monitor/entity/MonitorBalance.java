/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.entity;

import com.blocain.bitms.orm.core.GenericEntity;
import io.swagger.annotations.ApiModel;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 账户余额表 实体对象
 * <p>File：MonitorBalance.java</p>
 * <p>Title: MonitorBalance</p>
 * <p>Description:MonitorBalance</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "账户余额表")
public class MonitorBalance extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    private Long                 accountId;
    
    private Long                 stockinfoId;
    
    private Long                 bizCategoryId;
    
    // 账户资产类别
    private Integer              acctAssetType;
    
    private java.math.BigDecimal beginBal;
    
    private java.math.BigDecimal beginFrozenBal;
    
    private java.math.BigDecimal beginFeeBal;
    
    private java.math.BigDecimal endBal;
    
    private java.math.BigDecimal endFrozenBal;
    
    private java.math.BigDecimal endFeeBal;
    
    private java.sql.Timestamp   createDate;
    
    private java.sql.Timestamp   businessDate;
    
    public Long getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public Long getStockinfoId()
    {
        return stockinfoId;
    }
    
    public void setStockinfoId(Long stockinfoId)
    {
        this.stockinfoId = stockinfoId;
    }
    
    public BigDecimal getBeginBal()
    {
        return beginBal;
    }
    
    public void setBeginBal(BigDecimal beginBal)
    {
        this.beginBal = beginBal;
    }
    
    public BigDecimal getBeginFrozenBal()
    {
        return beginFrozenBal;
    }
    
    public void setBeginFrozenBal(BigDecimal beginFrozenBal)
    {
        this.beginFrozenBal = beginFrozenBal;
    }
    
    public BigDecimal getBeginFeeBal()
    {
        return beginFeeBal;
    }
    
    public void setBeginFeeBal(BigDecimal beginFeeBal)
    {
        this.beginFeeBal = beginFeeBal;
    }
    
    public BigDecimal getEndBal()
    {
        return endBal;
    }
    
    public void setEndBal(BigDecimal endBal)
    {
        this.endBal = endBal;
    }
    
    public BigDecimal getEndFrozenBal()
    {
        return endFrozenBal;
    }
    
    public void setEndFrozenBal(BigDecimal endFrozenBal)
    {
        this.endFrozenBal = endFrozenBal;
    }
    
    public BigDecimal getEndFeeBal()
    {
        return endFeeBal;
    }
    
    public void setEndFeeBal(BigDecimal endFeeBal)
    {
        this.endFeeBal = endFeeBal;
    }
    
    public Timestamp getCreateDate()
    {
        return createDate;
    }
    
    public void setCreateDate(Timestamp createDate)
    {
        this.createDate = createDate;
    }
    
    public Timestamp getBusinessDate()
    {
        return businessDate;
    }
    
    public void setBusinessDate(Timestamp businessDate)
    {
        this.businessDate = businessDate;
    }
    
    public Integer getAcctAssetType()
    {
        return acctAssetType;
    }
    
    public void setAcctAssetType(Integer acctAssetType)
    {
        this.acctAssetType = acctAssetType;
    }
    
    public Long getBizCategoryId()
    {
        return bizCategoryId;
    }
    
    public void setBizCategoryId(Long bizCategoryId)
    {
        this.bizCategoryId = bizCategoryId;
    }
}

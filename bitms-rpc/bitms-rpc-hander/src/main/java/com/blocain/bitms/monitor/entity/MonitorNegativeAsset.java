/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.entity;

import io.swagger.annotations.ApiModel;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.blocain.bitms.orm.core.GenericEntity;

/**
 * 账户负资产监控表 实体对象
 * <p>File：MonitorNegativeAsset.java</p>
 * <p>Title: MonitorNegativeAsset</p>
 * <p>Description:MonitorNegativeAsset</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "账户负资产监控表")
public class MonitorNegativeAsset extends GenericEntity
{
    private static final long serialVersionUID = 1L;
    
    private String            monitorType;
    
    private String            monitorSubType;
    
    private BigDecimal        amount;
    
    private Long              accountId;
    
    private Long              stockinfoId;
    
    private BigDecimal        frozenAmt;
    
    private Integer           chkResult;
    
    private Timestamp         chkDate;
    
    private String            monitorDesc;
    
    private Long              unid;
    
    private String            accountName;
    
    private String            stockName;
    
    private Integer           status;
    
    private String            timeStart;
    
    private String            timeEnd;
    
    public String getMonitorType()
    {
        return this.monitorType;
    }
    
    public void setMonitorType(String monitorType)
    {
        this.monitorType = monitorType;
    }
    
    public String getMonitorSubType()
    {
        return this.monitorSubType;
    }
    
    public void setMonitorSubType(String monitorSubType)
    {
        this.monitorSubType = monitorSubType;
    }
    
    public BigDecimal getAmount()
    {
        return this.amount;
    }
    
    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }
    
    public BigDecimal getFrozenAmt()
    {
        return this.frozenAmt;
    }
    
    public void setFrozenAmt(BigDecimal frozenAmt)
    {
        this.frozenAmt = frozenAmt;
    }
    
    public Integer getChkResult()
    {
        return this.chkResult;
    }
    
    public void setChkResult(Integer chkResult)
    {
        this.chkResult = chkResult;
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
    
    public Timestamp getChkDate()
    {
        return chkDate;
    }
    
    public void setChkDate(Timestamp chkDate)
    {
        this.chkDate = chkDate;
    }
    
    public Long getUnid()
    {
        return unid;
    }
    
    public void setUnid(Long unid)
    {
        this.unid = unid;
    }
    
    public String getAccountName()
    {
        return accountName;
    }
    
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    
    public Integer getStatus()
    {
        return status;
    }
    
    public void setStatus(Integer status)
    {
        this.status = status;
    }
    
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
    
    public String getMonitorDesc()
    {
        return monitorDesc;
    }
    
    public void setMonitorDesc(String monitorDesc)
    {
        this.monitorDesc = monitorDesc;
    }

    public String getStockName()
    {
        return stockName;
    }

    public void setStockName(String stockName)
    {
        this.stockName = stockName;
    }
}

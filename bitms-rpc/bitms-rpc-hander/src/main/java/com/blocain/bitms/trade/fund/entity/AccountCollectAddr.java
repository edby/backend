/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.entity;

import com.blocain.bitms.orm.core.SignableEntity;
import com.blocain.bitms.tools.consts.CharsetConst;

import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;

/**
 * 账户收款地址表 实体对象
 * <p>File：AccountCollectAddr.java</p>
 * <p>Title: AccountCollectAddr</p>
 * <p>Description:AccountCollectAddr</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class AccountCollectAddr extends SignableEntity
{
    private static final long  serialVersionUID = 1L;
    
    /**账户ID*/
    @NotNull(message = "账户ID不可为空")
    private Long               accountId;
    
    /**证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "证券信息id 对应Stockinfo表中的ID字段不可为空")
    private Long               stockinfoId;
    
    /**收款地址*/
    @NotNull(message = "收款地址不可为空")
    private String             collectAddr;
    
    /**认证状态(unauth未认证、auth已认证)*/
    @NotNull(message = "认证状态不可为空")
    private String             certStatus;
    
    /**备注*/
    private String             remark;
    
    /**创建人*/
    @NotNull(message = "创建人不可为空")
    private Long               createBy;
    
    /**创建时间*/
    @NotNull(message = "创建时间不可为空")
    private java.sql.Timestamp createDate;
    
    /**激活状态*/
    @NotNull(message = "激活状态不可为空")
    private String             isActivate;
    
    /**审核状态*/
    @NotNull(message = "审核状态不可为空")
    private String             status;
    
    /**证券代码*/
    private String             stockCode;
    
    private String             accountName;
    
    @Override
    protected byte[] acquiresSignValue() throws UnsupportedEncodingException
    {
        StringBuffer signValue = new StringBuffer(String.valueOf(this.id)).append(this.accountId);
        signValue.append(this.stockinfoId).append(this.collectAddr);
        signValue.append(this.certStatus);
        return signValue.toString().getBytes(CharsetConst.CHARSET_UT);
    }
    
    public Long getAccountId()
    {
        return this.accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public Long getStockinfoId()
    {
        return this.stockinfoId;
    }
    
    public void setStockinfoId(Long stockinfoId)
    {
        this.stockinfoId = stockinfoId;
    }
    
    public String getCollectAddr()
    {
        return this.collectAddr;
    }
    
    public void setCollectAddr(String collectAddr)
    {
        this.collectAddr = collectAddr;
    }
    
    public String getCertStatus()
    {
        return this.certStatus;
    }
    
    public void setCertStatus(String certStatus)
    {
        this.certStatus = certStatus;
    }
    
    public String getRemark()
    {
        return this.remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public Long getCreateBy()
    {
        return this.createBy;
    }
    
    public void setCreateBy(Long createBy)
    {
        this.createBy = createBy;
    }
    
    public Timestamp getCreateDate()
    {
        return createDate;
    }
    
    public void setCreateDate(Timestamp createDate)
    {
        this.createDate = createDate;
    }
    
    public String getStockCode()
    {
        return stockCode;
    }
    
    public void setStockCode(String stockCode)
    {
        this.stockCode = stockCode;
    }
    
    public String getIsActivate()
    {
        return isActivate;
    }
    
    public void setIsActivate(String isActivate)
    {
        this.isActivate = isActivate;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public String getAccountName()
    {
        return accountName;
    }
    
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
}

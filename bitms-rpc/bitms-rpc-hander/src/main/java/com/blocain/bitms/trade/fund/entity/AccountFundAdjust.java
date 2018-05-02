/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.entity;

import com.blocain.bitms.orm.core.SignableEntity;
import com.blocain.bitms.tools.consts.CharsetConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 账户资金调整记录表 实体对象
 * <p>File：AccountFundAdjust.java</p>
 * <p>Title: AccountFundAdjust</p>
 * <p>Description:AccountFundAdjust</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "账户资金调整记录表")
public class AccountFundAdjust extends SignableEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**账户ID*/
    @NotNull(message = "账户ID不可为空")
    @ApiModelProperty(value = "账户ID", required = true)
    private Long                 accountId;
    
    /**证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "证券信息id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "证券信息id 对应Stockinfo表中的ID字段", required = true)
    private Long                 stockinfoId;
    
    /**业务类型*/
    @NotNull(message = "业务类型不可为空")
    @ApiModelProperty(value = "业务类型", required = true)
    private String               businessFlag;
    
    /**调整类型(调增、调减)*/
    @NotNull(message = "调整类型(调增、调减)不可为空")
    @ApiModelProperty(value = "调整类型(调增、调减)", required = true)
    private java.lang.String     adjustType;
    
    /**调整数量*/
    @NotNull(message = "调整数量不可为空")
    @ApiModelProperty(value = "调整数量", required = true)
    private java.math.BigDecimal adjustAmt;
    
    /**是否需要锁定(yes、no)*/
    @NotNull(message = "是否需要锁定(yes、no)不可为空")
    @ApiModelProperty(value = "是否需要锁定(yes、no)", required = true)
    private java.lang.String     needLock;
    
    /**锁定结束日期*/
    @ApiModelProperty(value = "锁定结束日期")
    private java.sql.Timestamp   lockEndDay;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private java.lang.String     remark;
    
    /**创建人*/
    @ApiModelProperty(value = "创建人")
    private Long                 createBy;
    
    /**创建人*/
    private String               createByName;
    
    /**创建时间*/
    @ApiModelProperty(value = "创建时间")
    private java.sql.Timestamp   createDate;
    
    /**修改人*/
    @ApiModelProperty(value = "修改人")
    private Long                 updateBy;
    
    /**修改人*/
    private String               updateByName;
    
    /**修改时间*/
    @ApiModelProperty(value = "修改时间")
    private java.sql.Timestamp   updateDate;
    
    /**锁定状态（yes锁定 no解锁）*/
    @NotNull(message = "锁定状态（yes锁定 no解锁）不可为空")
    private String               lockStatus       = "yes";
    
    /**证券代码*/
    private String               stockCode;
    
    /**帐号*/
    private String               accountName;
    
    @Override
    protected byte[] acquiresSignValue() throws UnsupportedEncodingException
    {
        StringBuffer signValue = new StringBuffer(String.valueOf(this.id)).append(this.accountId);
        signValue.append(this.stockinfoId).append(this.adjustType);
        signValue.append(this.businessFlag);
        return signValue.toString().getBytes(CharsetConst.CHARSET_UT);
    }
    
    public java.lang.Long getAccountId()
    {
        return this.accountId;
    }
    
    public void setAccountId(java.lang.Long accountId)
    {
        this.accountId = accountId;
    }
    
    public java.lang.Long getStockinfoId()
    {
        return this.stockinfoId;
    }
    
    public void setStockinfoId(java.lang.Long stockinfoId)
    {
        this.stockinfoId = stockinfoId;
    }
    
    public java.lang.String getAdjustType()
    {
        return this.adjustType;
    }
    
    public void setAdjustType(java.lang.String adjustType)
    {
        this.adjustType = adjustType;
    }
    
    public java.math.BigDecimal getAdjustAmt()
    {
        return this.adjustAmt;
    }
    
    public void setAdjustAmt(java.math.BigDecimal adjustAmt)
    {
        this.adjustAmt = adjustAmt;
    }
    
    public java.lang.String getNeedLock()
    {
        return this.needLock;
    }
    
    public void setNeedLock(java.lang.String needLock)
    {
        this.needLock = needLock;
    }
    
    public java.lang.String getRemark()
    {
        return this.remark;
    }
    
    public void setRemark(java.lang.String remark)
    {
        this.remark = remark;
    }
    
    public java.lang.Long getCreateBy()
    {
        return this.createBy;
    }
    
    public void setCreateBy(java.lang.Long createBy)
    {
        this.createBy = createBy;
    }
    
    public java.lang.Long getUpdateBy()
    {
        return this.updateBy;
    }
    
    public void setUpdateBy(java.lang.Long updateBy)
    {
        this.updateBy = updateBy;
    }
    
    public Timestamp getLockEndDay()
    {
        return lockEndDay;
    }
    
    public void setLockEndDay(Timestamp lockEndDay)
    {
        this.lockEndDay = lockEndDay;
    }
    
    public Timestamp getCreateDate()
    {
        return createDate;
    }
    
    public void setCreateDate(Timestamp createDate)
    {
        this.createDate = createDate;
    }
    
    public Timestamp getUpdateDate()
    {
        return updateDate;
    }
    
    public void setUpdateDate(Timestamp updateDate)
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
    
    public String getBusinessFlag()
    {
        return businessFlag;
    }
    
    public void setBusinessFlag(String businessFlag)
    {
        this.businessFlag = businessFlag;
    }
    
    public String getLockStatus()
    {
        return lockStatus;
    }
    
    public void setLockStatus(String lockStatus)
    {
        this.lockStatus = lockStatus;
    }
    
    public String getCreateByName()
    {
        return createByName;
    }
    
    public void setCreateByName(String createByName)
    {
        this.createByName = createByName;
    }
    
    public String getUpdateByName()
    {
        return updateByName;
    }
    
    public void setUpdateByName(String updateByName)
    {
        this.updateByName = updateByName;
    }

    @Override public String toString()
    {
        final StringBuilder sb = new StringBuilder("AccountFundAdjust{");
        sb.append("accountId=").append(accountId);
        sb.append(", stockinfoId=").append(stockinfoId);
        sb.append(", businessFlag='").append(businessFlag).append('\'');
        sb.append(", adjustType='").append(adjustType).append('\'');
        sb.append(", adjustAmt=").append(adjustAmt);
        sb.append(", needLock='").append(needLock).append('\'');
        sb.append(", lockEndDay=").append(lockEndDay);
        sb.append(", remark='").append(remark).append('\'');
        sb.append(", createBy=").append(createBy);
        sb.append(", createByName='").append(createByName).append('\'');
        sb.append(", createDate=").append(createDate);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", updateByName='").append(updateByName).append('\'');
        sb.append(", updateDate=").append(updateDate);
        sb.append(", lockStatus='").append(lockStatus).append('\'');
        sb.append(", stockCode='").append(stockCode).append('\'');
        sb.append(", accountName='").append(accountName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

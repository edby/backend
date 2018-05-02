/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.entity;

import com.blocain.bitms.orm.core.GenericEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 账户邀请记录表 实体对象
 * <p>File：AccountInvitation.java</p>
 * <p>Title: AccountInvitation</p>
 * <p>Description:AccountInvitation</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "账户邀请记录表")
public class AccountInvitation extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**帐户ID*/
    @NotNull(message = "帐户ID不可为空")
    @ApiModelProperty(value = "帐户ID")
    private Long                 accountId;
    
    /**帐户名称*/
    @NotNull(message = "帐户名称不可为空")
    @ApiModelProperty(value = "帐户名称")
    private String               accountName;
    
    /**邀请账户*/
    private String               invitationAccountName;
    
    /**邀请码(UNID)*/
    @NotNull(message = "邀请码(UNID)不可为空")
    @ApiModelProperty(value = "邀请码(UNID)")
    private Long                 invitCode;
    
    /**创建时间*/
    @NotNull(message = "创建时间不可为空")
    @ApiModelProperty(value = "创建时间")
    private Long                 createDate;
    
    /**BMS数量*/
    @NotNull(message = "BMS数量不可为空")
    @ApiModelProperty(value = "BMS数量")
    private java.math.BigDecimal bmsNum;
    
    /**是否已发放BMS*/
    @NotNull(message = "是否已发放BMS不可为空")
    @ApiModelProperty(value = "是否已发放BMS")
    private String               isGrant;
    
    /**BMS到帐状态*/
    @NotNull(message = "BMS到帐状态不可为空")
    @ApiModelProperty(value = "BMS到帐状态")
    private String               grantFlag;
    
    /**锁定结束日期*/
    private Long                 lockEndDay;
    
    /**锁定状态（yes锁定 no解锁）*/
    @NotNull(message = "锁定状态（yes锁定 no解锁）不可为空")
    private String               lockStatus       = "yes";
    
    public Long getAccountId()
    {
        return this.accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public String getAccountName()
    {
        return this.accountName;
    }
    
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    
    public Long getInvitCode()
    {
        return this.invitCode;
    }
    
    public void setInvitCode(Long invitCode)
    {
        this.invitCode = invitCode;
    }
    
    public Long getCreateDate()
    {
        return this.createDate;
    }
    
    public void setCreateDate(Long createDate)
    {
        this.createDate = createDate;
    }
    
    public java.math.BigDecimal getBmsNum()
    {
        return bmsNum;
    }
    
    public void setBmsNum(java.math.BigDecimal bmsNum)
    {
        this.bmsNum = bmsNum;
    }
    
    public String getIsGrant()
    {
        return this.isGrant;
    }
    
    public void setIsGrant(String isGrant)
    {
        this.isGrant = isGrant;
    }
    
    public String getGrantFlag()
    {
        return this.grantFlag;
    }
    
    public void setGrantFlag(String grantFlag)
    {
        this.grantFlag = grantFlag;
    }
    
    public String getInvitationAccountName()
    {
        return invitationAccountName;
    }
    
    public void setInvitationAccountName(String invitationAccountName)
    {
        this.invitationAccountName = invitationAccountName;
    }
    
    public Long getLockEndDay()
    {
        return lockEndDay;
    }
    
    public void setLockEndDay(Long lockEndDay)
    {
        this.lockEndDay = lockEndDay;
    }
    
    public String getLockStatus()
    {
        return lockStatus;
    }
    
    public void setLockStatus(String lockStatus)
    {
        this.lockStatus = lockStatus;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("AccountInvitation{");
        sb.append("id=").append(id);
        sb.append(", accountId=").append(accountId);
        sb.append(", accountName='").append(accountName).append('\'');
        sb.append(", invitationAccountName='").append(invitationAccountName).append('\'');
        sb.append(", invitCode=").append(invitCode);
        sb.append(", createDate=").append(createDate);
        sb.append(", bmsNum=").append(bmsNum);
        sb.append(", isGrant='").append(isGrant).append('\'');
        sb.append(", grantFlag='").append(grantFlag).append('\'');
        sb.append(", lockEndDay=").append(lockEndDay);
        sb.append(", lockStatus='").append(lockStatus).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

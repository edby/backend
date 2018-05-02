/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.stockinfo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.blocain.bitms.orm.core.GenericEntity;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ERC20 TOKEN 实体对象
 * <p>File：Erc20Token.java</p>
 * <p>Title: Erc20Token</p>
 * <p>Description:Erc20Token</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "ERC20 TOKEN")
public class Erc20Token extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**符号*/
    @NotNull(message = "符号不可为空")
    @ApiModelProperty(value = "符号", required = true)
    private String               symbol;
    
    /**全称*/
    @NotNull(message = "全称不可为空")
    @ApiModelProperty(value = "全称", required = true)
    private String               symbolName;
    
    /**平台交易对*/
    @NotNull(message = "平台交易对不可为空")
    @ApiModelProperty(value = "平台交易对", required = true)
    private String               pair;
    
    /**合约地址*/
    @NotNull(message = "合约地址不可为空")
    @ApiModelProperty(value = "合约地址", required = true)
    private String               contractAddr;
    
    /**发行量*/
    @NotNull(message = "发行量不可为空")
    @ApiModelProperty(value = "发行量", required = true)
    private java.math.BigDecimal totalSupply;
    
    /**精度*/
    @NotNull(message = "精度不可为空")
    @ApiModelProperty(value = "精度", required = true)
    private Long                 tokenDecimals;
    
    /**图标*/
    @ApiModelProperty(value = "图标")
    private String               icon;
    
    /**创建时间*/
    @NotNull(message = "创建时间不可为空")
    @ApiModelProperty(value = "创建时间", required = true)
    private java.util.Date       createDate;
    
    /**激活状态  no未激活 yes已激活*/
    @NotNull(message = "激活状态不可为空")
    @ApiModelProperty(value = "激活状态  no未激活 yes已激活", required = true)
    private String               isActive;
    
    /**有效期结束时间*/
    @NotNull(message = "有效期结束时间不可为空")
    @ApiModelProperty(value = "有效期结束时间", required = true)
    private java.util.Date       activeEndDate;
    
    /**邀请人ID*/
    @ApiModelProperty(value = "邀请人ID", required = true)
    private Long                 inviteAccountId;
    
    /**邀请人奖励状态  0未奖励 1已奖励 -1不需奖励*/
    @NotNull(message = "邀请人奖励状态 (0未奖励 1已奖励 -1不需奖励)不可为空")
    @ApiModelProperty(value = "邀请人奖励状态 (0未奖励 1已奖励 -1不需奖励)", required = true)
    private Integer              needAward;
    
    /**token奖励状态  0未奖励 1已奖励 -1不再奖励*/
    @NotNull(message = "token奖励状态 (0未奖励 1已奖励 -1不再奖励)不可为空")
    @ApiModelProperty(value = "token奖励状态 (0未奖励 1已奖励 -1不再奖励)", required = true)
    private Integer              awardStatus;
    
    /** 首次激活token的人ID */
    private Long                 activeAccountId;
    
    /** 受邀者 */
    private String               inviter;
    
    /** 块高度 */
    private Long                 blockHeight;
    
    /** 奖励数量*/
    @ApiModelProperty(value = "奖励数量")
    private java.math.BigDecimal awardAmount;
    
    public String getSymbol()
    {
        return this.symbol;
    }
    
    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }
    
    public String getPair()
    {
        return this.pair;
    }
    
    public void setPair(String pair)
    {
        this.pair = pair;
    }
    
    public String getContractAddr()
    {
        return this.contractAddr;
    }
    
    public void setContractAddr(String contractAddr)
    {
        this.contractAddr = contractAddr;
    }
    
    public java.math.BigDecimal getTotalSupply()
    {
        return this.totalSupply;
    }
    
    public void setTotalSupply(java.math.BigDecimal totalSupply)
    {
        this.totalSupply = totalSupply;
    }
    
    public Long getTokenDecimals()
    {
        return this.tokenDecimals;
    }
    
    public void setTokenDecimals(Long tokenDecimals)
    {
        this.tokenDecimals = tokenDecimals;
    }
    
    public String getIcon()
    {
        return this.icon;
    }
    
    public void setIcon(String icon)
    {
        this.icon = icon;
    }
    
    public java.util.Date getCreateDate()
    {
        return this.createDate;
    }
    
    public void setCreateDate(java.util.Date createDate)
    {
        this.createDate = createDate;
    }
    
    public String getIsActive()
    {
        return isActive;
    }
    
    public void setIsActive(String isActive)
    {
        this.isActive = isActive;
    }
    
    public Date getActiveEndDate()
    {
        return activeEndDate;
    }
    
    public void setActiveEndDate(Date activeEndDate)
    {
        this.activeEndDate = activeEndDate;
    }
    
    public String getSymbolName()
    {
        return symbolName;
    }
    
    public void setSymbolName(String symbolName)
    {
        this.symbolName = symbolName;
    }
    
    public Long getInviteAccountId()
    {
        return inviteAccountId;
    }
    
    public void setInviteAccountId(Long inviteAccountId)
    {
        this.inviteAccountId = inviteAccountId;
    }
    
    public Integer getNeedAward()
    {
        return needAward;
    }
    
    public void setNeedAward(Integer needAward)
    {
        this.needAward = needAward;
    }
    
    public Integer getAwardStatus()
    {
        return awardStatus;
    }
    
    public void setAwardStatus(Integer awardStatus)
    {
        this.awardStatus = awardStatus;
    }
    
    public Long getActiveAccountId()
    {
        return activeAccountId;
    }
    
    public void setActiveAccountId(Long activeAccountId)
    {
        this.activeAccountId = activeAccountId;
    }
    
    public String getInviter()
    {
        return inviter;
    }
    
    public void setInviter(String inviter)
    {
        this.inviter = inviter;
    }
    
    public BigDecimal getAwardAmount()
    {
        return awardAmount;
    }
    
    public void setAwardAmount(BigDecimal awardAmount)
    {
        this.awardAmount = awardAmount;
    }
    
    public Long getBlockHeight()
    {
        return blockHeight;
    }
    
    public void setBlockHeight(Long blockHeight)
    {
        this.blockHeight = blockHeight;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("Erc20Token{");
        sb.append("symbol='").append(symbol).append('\'');
        sb.append(", symbolName='").append(symbolName).append('\'');
        sb.append(", pair='").append(pair).append('\'');
        sb.append(", contractAddr='").append(contractAddr).append('\'');
        sb.append(", totalSupply=").append(totalSupply);
        sb.append(", tokenDecimals=").append(tokenDecimals);
        sb.append(", icon='").append(icon).append('\'');
        sb.append(", createDate=").append(createDate);
        sb.append(", isActive='").append(isActive).append('\'');
        sb.append(", activeEndDate=").append(activeEndDate);
        sb.append(", inviteAccountId=").append(inviteAccountId);
        sb.append(", needAward=").append(needAward);
        sb.append(", awardStatus=").append(awardStatus);
        sb.append(", activeAccountId=").append(activeAccountId);
        sb.append(", inviter='").append(inviter).append('\'');
        sb.append(", blockHeight=").append(blockHeight);
        sb.append(", awardAmount=").append(awardAmount);
        sb.append('}');
        return sb.toString();
    }
}

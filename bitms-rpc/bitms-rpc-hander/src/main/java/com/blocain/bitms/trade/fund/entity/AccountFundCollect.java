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
 * 账户资金归集表 实体对象
 * <p>File：AccountFundCollect.java</p>
 * <p>Title: AccountFundCollect</p>
 * <p>Description:AccountFundCollect</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "账户资金归集表")
public class AccountFundCollect extends SignableEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**Boss系统用户ID*/
    @NotNull(message = "Boss系统用户ID不可为空")
    @ApiModelProperty(value = "Boss系统用户ID", required = true)
    private Long                 userinfoId;
    
    /**证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "证券信息id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "证券信息id 对应Stockinfo表中的ID字段", required = true)
    private Long                 stockinfoId;
    
    /**源钱包ID*/
    @NotNull(message = "源钱包ID不可为空")
    @ApiModelProperty(value = "源钱包ID", required = true)
    private String                 srcWalletId;
    
    /**源钱包地址*/
    @NotNull(message = "源钱包地址不可为空")
    @ApiModelProperty(value = "源钱包地址", required = true)
    private String               srcWalletAddr;
    
    /**源账户ID*/
    @NotNull(message = "源账户ID不可为空")
    @ApiModelProperty(value = "源账户ID", required = true)
    private Long                 accountId;
    
    /**目标钱包ID*/
    @NotNull(message = "目标钱包ID不可为空")
    @ApiModelProperty(value = "目标钱包ID", required = true)
    private Long                 targetWalletId;
    
    /**目标钱包地址*/
    @NotNull(message = "目标钱包地址不可为空")
    @ApiModelProperty(value = "目标钱包地址", required = true)
    private String               targetWalletAddr;
    
    /**归集时间*/
    @NotNull(message = "归集时间不可为空")
    @ApiModelProperty(value = "归集时间", required = true)
    private java.sql.Timestamp   collectTime;
    
    /**归集数量*/
    @NotNull(message = "归集数量不可为空")
    @ApiModelProperty(value = "归集数量", required = true)
    private java.math.BigDecimal collectAmt;
    
    /**划拨费用*/
    @NotNull(message = "划拨费用不可为空")
    @ApiModelProperty(value = "划拨费用", required = true)
    private java.math.BigDecimal collectFee;
    
    /**归集状态(noCollect无需归集unCollect未归集、collected已归集)*/
    @NotNull(message = "归集状态不可为空")
    @ApiModelProperty(value = "归集状态")
    private String               collectStatus;
    
    /**区块交易ID*/
    @ApiModelProperty(value = "区块交易ID")
    private String               transId;
    
    /**区块确认状态（unconfirm未确认、confirm已确认）*/
    @ApiModelProperty(value = "区块确认状态（unconfirm未确认、confirm已确认）", required = true)
    private String               confirmStatus;
    
    /**原资金流水数据ID*/
    @NotNull(message = "原资金流水数据ID不可为空")
    private String               originalCurrentId;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String               remark;
    
    @Override
    protected byte[] acquiresSignValue() throws UnsupportedEncodingException
    {
        StringBuffer signValue = new StringBuffer(String.valueOf(this.id)).append(this.accountId);
        signValue.append(this.stockinfoId).append(this.srcWalletId);
        signValue.append(this.targetWalletId).append(this.targetWalletAddr);
        return signValue.toString().getBytes(CharsetConst.CHARSET_UT);
    }
    
    public Long getUserinfoId()
    {
        return this.userinfoId;
    }
    
    public void setUserinfoId(Long userinfoId)
    {
        this.userinfoId = userinfoId;
    }
    
    public Long getStockinfoId()
    {
        return this.stockinfoId;
    }
    
    public void setStockinfoId(Long stockinfoId)
    {
        this.stockinfoId = stockinfoId;
    }
    
    public String getSrcWalletId()
    {
        return this.srcWalletId;
    }
    
    public void setSrcWalletId(String srcWalletId)
    {
        this.srcWalletId = srcWalletId;
    }
    
    public String getSrcWalletAddr()
    {
        return this.srcWalletAddr;
    }
    
    public void setSrcWalletAddr(String srcWalletAddr)
    {
        this.srcWalletAddr = srcWalletAddr;
    }
    
    public Long getAccountId()
    {
        return this.accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public Long getTargetWalletId()
    {
        return this.targetWalletId;
    }
    
    public void setTargetWalletId(Long targetWalletId)
    {
        this.targetWalletId = targetWalletId;
    }
    
    public String getTargetWalletAddr()
    {
        return this.targetWalletAddr;
    }
    
    public void setTargetWalletAddr(String targetWalletAddr)
    {
        this.targetWalletAddr = targetWalletAddr;
    }
    
    public void setCollectTime(Timestamp collectTime)
    {
        this.collectTime = collectTime;
    }
    
    public java.math.BigDecimal getCollectAmt()
    {
        return this.collectAmt;
    }
    
    public void setCollectAmt(java.math.BigDecimal collectAmt)
    {
        this.collectAmt = collectAmt;
    }
    
    public java.math.BigDecimal getCollectFee()
    {
        return this.collectFee;
    }
    
    public void setCollectFee(java.math.BigDecimal collectFee)
    {
        this.collectFee = collectFee;
    }
    
    public String getCollectStatus()
    {
        return this.collectStatus;
    }
    
    public void setCollectStatus(String collectStatus)
    {
        this.collectStatus = collectStatus;
    }
    
    public String getTransId()
    {
        return this.transId;
    }
    
    public void setTransId(String transId)
    {
        this.transId = transId;
    }
    
    public String getConfirmStatus()
    {
        return this.confirmStatus;
    }
    
    public void setConfirmStatus(String confirmStatus)
    {
        this.confirmStatus = confirmStatus;
    }
    
    public String getRemark()
    {
        return this.remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public String getOriginalCurrentId()
    {
        return originalCurrentId;
    }
    
    public void setOriginalCurrentId(String originalCurrentId)
    {
        this.originalCurrentId = originalCurrentId;
    }
}

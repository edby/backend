/*
 * @(#)TransferRecord.java 2017年7月20日 上午11:27:04
 * Copyright 2017 施建波, Inc. All rights reserved. BloCain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.bitpay.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;

/**
 * <p>File：TransferRecord.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月20日 上午11:27:04</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
public class TransferRecord extends BaseEntity
{
    @TableField(exist = false)
    private static final long serialVersionUID = -5826588305487006766L;
    
    @TableId(type = IdType.INPUT)
    private Long id;
    /**钱包ID*/
    private String walletId;
    /**钱包名称*/
    private String walletName;
    /**转帐金额*/
    private BigDecimal amount;
    /**手续费*/
    private BigDecimal fee;
    /**转帐地址*/
    private String address;
    /**交易ID*/
    private String transId;
    /**创建时间*/
    private Long createDate;
    
    //支付密码
    @TableField(exist = false)
    private String payPass;
    
    public Long getId()
    {
        return id;
    }
    public void setId(Long id)
    {
        this.id = id;
    }
    public String getWalletId()
    {
        return walletId;
    }
    public void setWalletId(String walletId)
    {
        this.walletId = walletId;
    }
    public String getWalletName()
    {
        return walletName;
    }
    public void setWalletName(String walletName)
    {
        this.walletName = walletName;
    }
    public BigDecimal getAmount()
    {
        return amount;
    }
    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }
    public BigDecimal getFee()
    {
        return fee;
    }
    public void setFee(BigDecimal fee)
    {
        this.fee = fee;
    }
    public String getAddress()
    {
        return address;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }
    public String getTransId()
    {
        return transId;
    }
    public void setTransId(String transId)
    {
        this.transId = transId;
    }
    public Long getCreateDate()
    {
        return createDate;
    }
    public void setCreateDate(Long createDate)
    {
        this.createDate = createDate;
    }
    public String getPayPass()
    {
        return payPass;
    }
    public void setPayPass(String payPass)
    {
        this.payPass = payPass;
    }
}

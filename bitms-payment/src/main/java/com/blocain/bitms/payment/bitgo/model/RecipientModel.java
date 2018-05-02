/*
 * @(#)RecipientModel.java 2017年7月7日 下午1:31:18
 * Copyright 2017 施建波, Inc. All rights reserved. BloCain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.payment.bitgo.model;

import java.io.Serializable;

/**
 * <p>File：RecipientModel.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月7日 下午1:31:18</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
public class RecipientModel implements Serializable
{
    //
    private static final long serialVersionUID = -3818446015209313198L;
    
    // 目标地址
    private String            address;
    
    // 发送金额
    private Long              amount;
    
    public String getAddress()
    {
        return address;
    }
    
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    public Long getAmount()
    {
        return amount;
    }
    
    public void setAmount(Long amount)
    {
        this.amount = amount;
    }
}

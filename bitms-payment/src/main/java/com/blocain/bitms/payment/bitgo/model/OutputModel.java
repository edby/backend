/*
 * @(#)OutputModel.java 2017年7月7日 下午3:13:32
 * Copyright 2017 施建波, Inc. All rights reserved. BloCain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.payment.bitgo.model;

import java.io.Serializable;

/**
 * <p>File：OutputModel.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月7日 下午3:13:32</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
public class OutputModel implements Serializable
{
    //
    private static final long serialVersionUID = 3508519722767211193L;
    
    // 目标帐号也就是目标地址
    private String            account;

    // 目标帐号也就是目标地址
    private String            address;
    
    // 发送金额
    private Long              value;
    
    public String getAccount()
    {
        return account;
    }
    
    public void setAccount(String account)
    {
        this.account = account;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getValue()
    {
        return value;
    }
    
    public void setValue(Long value)
    {
        this.value = value;
    }

    @Override
    public String toString() {
        return "OutputModel{" +
                "account='" + account + '\'' +
                ", address='" + address + '\'' +
                ", value=" + value +
                '}';
    }
}

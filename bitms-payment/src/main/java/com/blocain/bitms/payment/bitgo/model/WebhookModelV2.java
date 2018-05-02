/*
 * @(#)WebhookModel.java 2017年7月20日 上午8:37:07
 * Copyright 2017 施建波, Inc. All rights reserved. BloCain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.payment.bitgo.model;

import java.io.Serializable;

/**
 * <p>File：WebhookModelV2.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月20日 上午8:37:07</p>
 * <p>Company: BloCain</p>
 * @author sunbiao
 * @version 1.0
 */
public class WebhookModelV2 implements Serializable
{
    //
    private static final long serialVersionUID = 5399437624762179717L;
    
    //Webhook 的类型: 'transfer' (交易), 'pendingapproval'(待批准), 'address_confirmation', 'block' (区块), 和 'wallet_confirmation'
    private String type;
    
    //与 webhook 事件关联的钱包 ID
    private String walletId;
    
    //交易 ID
    private String hash;

    private String pendingApprovalId;

    private String state;
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public String getWalletId()
    {
        return walletId;
    }
    
    public void setWalletId(String walletId)
    {
        this.walletId = walletId;
    }
    
    public String getHash()
    {
        return hash;
    }
    
    public void setHash(String hash)
    {
        this.hash = hash;
    }

    public String getPendingApprovalId() {
        return pendingApprovalId;
    }

    public void setPendingApprovalId(String pendingApprovalId) {
        this.pendingApprovalId = pendingApprovalId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "WebhookModelV2{" +
                "type='" + type + '\'' +
                ", walletId='" + walletId + '\'' +
                ", hash='" + hash + '\'' +
                ", pendingApprovalId='" + pendingApprovalId + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

}

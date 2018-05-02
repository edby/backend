/*
 * @(#)WebhookModel.java 2017年7月20日 上午8:37:07
 * Copyright 2017 施建波, Inc. All rights reserved. BloCain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.payment.bitgo.model;

import java.io.Serializable;

/**
 * <p>File：WebhookModel.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月20日 上午8:37:07</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
public class WebhookModel implements Serializable
{
    //
    private static final long serialVersionUID = 6399437624762179717L;
    
    //Webhook 的类型: 'transaction' (交易), 'transactionExpire' (交易过期), 'transactionRemoved' (交易已移除), 'block' (区块), 和 'pendingapproval' (待批准)
    private String type;
    
    //与 webhook 事件关联的钱包 ID
    private String walletId;
    
    //交易 ID
    private String hash;
    
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
    
	@Override
	public String toString() {
		return "WebhookModel [type=" + type + ", walletId=" + walletId + ", hash=" + hash + "]";
	}
    
}

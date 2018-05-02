/*
 * @(#)PayModel.java 2017年7月7日 上午10:03:44
 * Copyright 2017 施建波, Inc. All rights reserved. BloCain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.payment.bitgo.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**
 * <p>File：PayModel.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月7日 上午10:03:44</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
public class BitPayModel implements Serializable
{
    //
    private static final long serialVersionUID = -1829342644797370113L;
    
    // 证券id
    private String            stockinfoId;
    
    // 钱包ID
    private String            id;
    
    // 钱包名称
    private String            label;
    
    // 钱包地址
    private String            address;
    
    // 交易ID
    private String            hash;
    
    // 已确认区块节点数量
    private Integer           confirmations;
    
    // 目标帐号集合
    private List<OutputModel> outputs;
    
    // 地址交易数量量
    private Integer           txCount;
    
    // 手续费
    private Long              fee;
    
    // 新加
    // 解密后的私钥
    private String            decrypted;
    
    // 加密后的私钥
    private String            encrypted;
    
    // 余额
    private String            balance;
    
    // 已确认余额
    private String            confirmedBalance;
    
    // 钱包类型：1、收款 2、付款
    private Integer           walletType;
    
    // 公钥
    private String            xpub;
    
    // 交易参数ID
    private String            keychainId;
    
    // 请求成功与否
    private String            error;
    
    // pendingApproval
    private String            pendingApproval;
    
    // 按每千字节计费，目标为设定数量区块的交易确认。默认：2， 最小：2，最大：20.
    private java.lang.Integer feeTxConfirmTarget;

    // 币种
    private String            coin;

    private String            received;

    private String            unconfirmed_received;
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getLabel()
    {
        return label;
    }
    
    public void setLabel(String label)
    {
        this.label = label;
    }
    
    public String getAddress()
    {
        return address;
    }
    
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    public String getHash()
    {
        return hash;
    }
    
    public void setHash(String hash)
    {
        this.hash = hash;
    }
    
    public Integer getConfirmations()
    {
        return confirmations;
    }
    
    public void setConfirmations(Integer confirmations)
    {
        this.confirmations = confirmations;
    }
    
    public List<OutputModel> getOutputs()
    {
        return outputs;
    }
    
    public void setOutputs(List<OutputModel> outputs)
    {
        this.outputs = outputs;
    }
    
    public Integer getTxCount()
    {
        return txCount;
    }
    
    @JSONField(name = "tx_count")
    public void setTxCount(Integer txCount)
    {
        this.txCount = txCount;
    }
    
    public Long getFee()
    {
        return fee;
    }
    
    public void setFee(Long fee)
    {
        this.fee = fee;
    }
    
    public String getDecrypted()
    {
        return decrypted;
    }
    
    public void setDecrypted(String decrypted)
    {
        this.decrypted = decrypted;
    }
    
    public String getEncrypted()
    {
        return encrypted;
    }
    
    public void setEncrypted(String encrypted)
    {
        this.encrypted = encrypted;
    }
    
    public String getBalance()
    {
        return balance;
    }
    
    public void setBalance(String balance)
    {
        this.balance = balance;
    }
    
    public String getConfirmedBalance()
    {
        return confirmedBalance;
    }
    
    public void setConfirmedBalance(String confirmedBalance)
    {
        this.confirmedBalance = confirmedBalance;
    }
    
    public Integer getWalletType()
    {
        return walletType;
    }
    
    public void setWalletType(Integer walletType)
    {
        this.walletType = walletType;
    }
    
    public String getXpub()
    {
        return xpub;
    }
    
    public void setXpub(String xpub)
    {
        this.xpub = xpub;
    }
    
    public String getKeychainId()
    {
        return keychainId;
    }
    
    public void setKeychainId(String keychainId)
    {
        this.keychainId = keychainId;
    }
    
    public String getStockinfoId()
    {
        return stockinfoId;
    }
    
    public void setStockinfoId(String stockinfoId)
    {
        this.stockinfoId = stockinfoId;
    }
    
    public Integer getFeeTxConfirmTarget()
    {
        return feeTxConfirmTarget;
    }
    
    public void setFeeTxConfirmTarget(Integer feeTxConfirmTarget)
    {
        this.feeTxConfirmTarget = feeTxConfirmTarget;
    }
    
    public String getError()
    {
        return error;
    }
    
    public void setError(String error)
    {
        this.error = error;
    }
    
    public String getPendingApproval()
    {
        return pendingApproval;
    }
    
    public void setPendingApproval(String pendingApproval)
    {
        this.pendingApproval = pendingApproval;
    }
    
    public String getCoin()
    {
        return coin;
    }
    
    public void setCoin(String coin)
    {
        this.coin = coin;
    }

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }

    public String getUnconfirmed_received() {
        return unconfirmed_received;
    }

    public void setUnconfirmed_received(String unconfirmed_received) {
        this.unconfirmed_received = unconfirmed_received;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("BitPayModel{");
        sb.append("stockinfoId='").append(stockinfoId).append('\'');
        sb.append(", id='").append(id).append('\'');
        sb.append(", label='").append(label).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", hash='").append(hash).append('\'');
        sb.append(", confirmations=").append(confirmations);
        sb.append(", outputs=").append(outputs);
        sb.append(", txCount=").append(txCount);
        sb.append(", fee=").append(fee);
        sb.append(", decrypted='").append(decrypted).append('\'');
        sb.append(", encrypted='").append(encrypted).append('\'');
        sb.append(", balance='").append(balance).append('\'');
        sb.append(", confirmedBalance='").append(confirmedBalance).append('\'');
        sb.append(", walletType=").append(walletType);
        sb.append(", xpub='").append(xpub).append('\'');
        sb.append(", keychainId='").append(keychainId).append('\'');
        sb.append(", error='").append(error).append('\'');
        sb.append(", pendingApproval='").append(pendingApproval).append('\'');
        sb.append(", feeTxConfirmTarget=").append(feeTxConfirmTarget);
        sb.append(", coin=").append(coin);
        sb.append(", received=").append(received);
        sb.append(", unconfirmed_received=").append(unconfirmed_received);
        sb.append('}');
        return sb.toString();
    }
}

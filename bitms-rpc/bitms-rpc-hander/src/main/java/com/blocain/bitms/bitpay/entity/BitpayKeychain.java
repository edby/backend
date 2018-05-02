/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.bitpay.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.blocain.bitms.orm.core.GenericEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;

/**
 * 钱包参数 实体对象
 * <p>File：BitpayKeychain.java</p>
 * <p>Title: BitpayKeychain</p>
 * <p>Description:BitpayKeychain</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "钱包参数")
public class BitpayKeychain extends GenericEntity
{
    private static final long  serialVersionUID = 1L;
    
    /**钱包ID*/
    @ApiModelProperty(value = "钱包ID")
    private java.lang.String   walletId;
    
    /**钱包名称*/
    @ApiModelProperty(value = "钱包名称")
    private java.lang.String   walletName;
    
    /**TOKEN*/
    @ApiModelProperty(value = "TOKEN")
    private java.lang.String   token;
    
    /**加密后的私钥*/
    @ApiModelProperty(value = "加密后的私钥")
    private java.lang.String   xprv;
    
    /**公钥*/
    @ApiModelProperty(value = "公钥")
    private java.lang.String   xpub;
    
    /**系统密码*/
    @JSONField(serialize = false)
    @ApiModelProperty(value = "系统密码")
    private java.lang.String   systemPass;
    
    /**钱包密码加密后的密文*/
    @ApiModelProperty(value = "钱包密码加密后的密文")
    private java.lang.String   ciphertext;
    
    /**证券信息ID*/
    @ApiModelProperty(value = "证券信息ID")
    private java.lang.Long     stockinfoId;
    
    /**按每千字节计费，目标为设定数量区块的交易确认。默认：2， 最小：2，最大：20.*/
    @ApiModelProperty(value = "按每千字节计费，目标为设定数量区块的交易确认。默认：2， 最小：2，最大：20.")
    private java.lang.Integer  feeTxConfirmTarget;
    
    /**钱包类型：1、收款 2、付款*/
    @ApiModelProperty(value = "钱包类型：1、收款 2、付款")
    private java.lang.Integer  type;
    
    /**创建时间*/
    @ApiModelProperty(value = "创建时间")
    private java.sql.Timestamp createDate;
    
    /** 币种(接口请求时用) */
    private String             coin;
    
    public java.lang.String getWalletId()
    {
        return this.walletId;
    }
    
    public void setWalletId(java.lang.String walletId)
    {
        this.walletId = walletId;
    }
    
    public java.lang.String getWalletName()
    {
        return this.walletName;
    }
    
    public void setWalletName(java.lang.String walletName)
    {
        this.walletName = walletName;
    }
    
    public java.lang.String getToken()
    {
        return this.token;
    }
    
    public void setToken(java.lang.String token)
    {
        this.token = token;
    }
    
    public java.lang.String getXprv()
    {
        return this.xprv;
    }
    
    public void setXprv(java.lang.String xprv)
    {
        this.xprv = xprv;
    }
    
    public java.lang.String getXpub()
    {
        return this.xpub;
    }
    
    public void setXpub(java.lang.String xpub)
    {
        this.xpub = xpub;
    }
    
    public java.lang.String getSystemPass()
    {
        return this.systemPass;
    }
    
    public void setSystemPass(java.lang.String systemPass)
    {
        this.systemPass = systemPass;
    }
    
    public java.lang.String getCiphertext()
    {
        return this.ciphertext;
    }
    
    public void setCiphertext(java.lang.String ciphertext)
    {
        this.ciphertext = ciphertext;
    }
    
    public java.lang.Long getStockinfoId()
    {
        return this.stockinfoId;
    }
    
    public void setStockinfoId(java.lang.Long stockinfoId)
    {
        this.stockinfoId = stockinfoId;
    }
    
    public java.lang.Integer getFeeTxConfirmTarget()
    {
        return this.feeTxConfirmTarget;
    }
    
    public void setFeeTxConfirmTarget(java.lang.Integer feeTxConfirmTarget)
    {
        this.feeTxConfirmTarget = feeTxConfirmTarget;
    }
    
    public java.lang.Integer getType()
    {
        return this.type;
    }
    
    public void setType(java.lang.Integer type)
    {
        this.type = type;
    }
    
    public Timestamp getCreateDate()
    {
        return createDate;
    }
    
    public void setCreateDate(Timestamp createDate)
    {
        this.createDate = createDate;
    }
    
    public String getCoin()
    {
        return coin;
    }
    
    public void setCoin(String coin)
    {
        this.coin = coin;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("BitpayKeychain{");
        sb.append("id=").append(id);
        sb.append(", walletId='").append(walletId).append('\'');
        sb.append(", walletName='").append(walletName).append('\'');
        sb.append(", token='").append(token).append('\'');
        sb.append(", xprv='").append(xprv).append('\'');
        sb.append(", xpub='").append(xpub).append('\'');
        sb.append(", systemPass='").append(systemPass).append('\'');
        sb.append(", ciphertext='").append(ciphertext).append('\'');
        sb.append(", stockinfoId=").append(stockinfoId);
        sb.append(", feeTxConfirmTarget=").append(feeTxConfirmTarget);
        sb.append(", type=").append(type);
        sb.append(", createDate=").append(createDate);
        sb.append(", coin='").append(coin).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

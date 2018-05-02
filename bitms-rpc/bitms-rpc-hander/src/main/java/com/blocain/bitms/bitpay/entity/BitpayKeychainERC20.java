/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.bitpay.entity;

import javax.validation.constraints.NotNull;

import com.blocain.bitms.orm.core.GenericEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * BitpayKeychainERC20 实体对象
 * <p>File：BitpayKeychainERC20.java</p>
 * <p>Title: BitpayKeychainERC20</p>
 * <p>Description:BitpayKeychainERC20</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "BitpayKeychainERC20")
public class BitpayKeychainERC20 extends GenericEntity
{
    private static final long serialVersionUID = 1L;
    
    /**stockinfoId*/
    @NotNull(message = "stockinfoId不可为空")
    @ApiModelProperty(value = "stockinfoId")
    private String            stockinfoId;
    
    /**walletId*/
    @NotNull(message = "walletId不可为空")
    @ApiModelProperty(value = "walletId")
    private String            walletId;
    
    /**walletName*/
    @NotNull(message = "walletName不可为空")
    @ApiModelProperty(value = "walletName")
    private String            walletName;
    
    /**walletPwd*/
    @ApiModelProperty(value = "walletPwd")
    private String            walletPwd;
    
    /**walletType*/
    @NotNull(message = "walletType不可为空")
    @ApiModelProperty(value = "walletType")
    private Integer           walletType;
    
    /**coin*/
    @NotNull(message = "coin不可为空")
    @ApiModelProperty(value = "coin")
    private String            coin;
    
    /**createDate*/
    @NotNull(message = "createDate不可为空")
    @ApiModelProperty(value = "createDate", required = true)
    private java.util.Date    createDate;
    
    // 证券名称
    private String            stockName;

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockinfoId()
    {
        return this.stockinfoId;
    }
    
    public void setStockinfoId(String stockinfoId)
    {
        this.stockinfoId = stockinfoId;
    }
    
    public String getWalletId()
    {
        return this.walletId;
    }
    
    public void setWalletId(String walletId)
    {
        this.walletId = walletId;
    }
    
    public String getWalletName()
    {
        return this.walletName;
    }
    
    public void setWalletName(String walletName)
    {
        this.walletName = walletName;
    }
    
    public String getWalletPwd()
    {
        return this.walletPwd;
    }
    
    public void setWalletPwd(String walletPwd)
    {
        this.walletPwd = walletPwd;
    }
    
    public Integer getWalletType()
    {
        return this.walletType;
    }
    
    public void setWalletType(Integer walletType)
    {
        this.walletType = walletType;
    }
    
    public String getCoin()
    {
        return this.coin;
    }
    
    public void setCoin(String coin)
    {
        this.coin = coin;
    }
    
    public java.util.Date getCreateDate()
    {
        return this.createDate;
    }
    
    public void setCreateDate(java.util.Date createDate)
    {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "BitpayKeychainERC20{" +
                "stockinfoId='" + stockinfoId + '\'' +
                ", walletId='" + walletId + '\'' +
                ", walletName='" + walletName + '\'' +
                ", walletPwd='" + walletPwd + '\'' +
                ", walletType=" + walletType +
                ", coin='" + coin + '\'' +
                ", createDate=" + createDate +
                ", stockName='" + stockName + '\'' +
                ", id=" + id +
                '}';
    }
}

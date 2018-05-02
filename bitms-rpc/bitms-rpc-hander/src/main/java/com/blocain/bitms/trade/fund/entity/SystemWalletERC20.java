/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.entity;

import com.blocain.bitms.orm.core.SignableEntity;
import com.blocain.bitms.tools.consts.CharsetConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.blocain.bitms.orm.core.GenericEntity;
import org.web3j.crypto.Sign;

import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;

/**
 * 系统ERC20钱包表 实体对象
 * <p>File：SystemWalletERC20.java</p>
 * <p>Title: SystemWalletERC20</p>
 * <p>Description:SystemWalletERC20</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "系统ERC20钱包表")
public class SystemWalletERC20 extends SignableEntity
{
    private static final long serialVersionUID = 1L;
    
    /**证券信息id 对应Stockinfo表中的ID字段(erc20虚拟化)*/
    @NotNull(message = "证券信息id 对应Stockinfo表中的ID字段(erc20虚拟化)不可为空")
    @ApiModelProperty(value = "证券信息id 对应Stockinfo表中的ID字段(erc20虚拟化)", required = true)
    private Long              stockinfoId;
    
    /**walletId*/
    @NotNull(message = "walletId不可为空")
    @ApiModelProperty(value = "walletId", required = true)
    private String            walletId;
    
    /**钱包名字*/
    @NotNull(message = "钱包名字不可为空")
    @ApiModelProperty(value = "钱包名字", required = true)
    private String            walletName;
    
    /**钱包用途类型（chargeAccount充币转账、collectAccount充币归集、transferAccount提币划拨）*/
    @NotNull(message = "钱包用途类型（chargeAccount充币转账、collectAccount充币归集、transferAccount提币划拨）不可为空")
    @ApiModelProperty(value = "钱包用途类型（chargeAccount充币转账、collectAccount充币归集、transferAccount提币划拨）", required = true)
    private String            walletUsageType;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String            remark;
    
    /**创建人*/
    @NotNull(message = "创建人不可为空")
    @ApiModelProperty(value = "创建人", required = true)
    private Long              createBy;
    
    /**创建时间*/
    @NotNull(message = "创建时间不可为空")
    @ApiModelProperty(value = "创建时间", required = true)
    private java.util.Date    createDate;
    
    // 证券代码
    private String            stockCode;
    
    // 证券名称
    private String            stockName;
    
    public String getStockCode()
    {
        return stockCode;
    }
    
    public void setStockCode(String stockCode)
    {
        this.stockCode = stockCode;
    }
    
    public String getStockName()
    {
        return stockName;
    }
    
    public void setStockName(String stockName)
    {
        this.stockName = stockName;
    }
    
    public Long getStockinfoId()
    {
        return this.stockinfoId;
    }
    
    public void setStockinfoId(Long stockinfoId)
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
    
    public String getWalletUsageType()
    {
        return this.walletUsageType;
    }
    
    public void setWalletUsageType(String walletUsageType)
    {
        this.walletUsageType = walletUsageType;
    }
    
    public String getRemark()
    {
        return this.remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public Long getCreateBy()
    {
        return this.createBy;
    }
    
    public void setCreateBy(Long createBy)
    {
        this.createBy = createBy;
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
    protected byte[] acquiresSignValue() throws UnsupportedEncodingException
    {
        StringBuffer signValue = new StringBuffer(String.valueOf(this.id)).append(this.stockinfoId);
        signValue.append(this.walletId).append(this.walletName);
        signValue.append(this.walletUsageType);
        return signValue.toString().getBytes(CharsetConst.CHARSET_UT);
    }

    @Override
    public String toString() {
        return "SystemWalletERC20{" +
                "stockinfoId=" + stockinfoId +
                ", walletId='" + walletId + '\'' +
                ", walletName='" + walletName + '\'' +
                ", walletUsageType='" + walletUsageType + '\'' +
                ", remark='" + remark + '\'' +
                ", createBy=" + createBy +
                ", createDate=" + createDate +
                ", stockCode='" + stockCode + '\'' +
                ", stockName='" + stockName + '\'' +
                ", sign='" + sign + '\'' +
                ", randomKey='" + randomKey + '\'' +
                ", id=" + id +
                '}';
    }
}

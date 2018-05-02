/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.blocain.bitms.orm.core.GenericEntity;
import javax.validation.constraints.NotNull;

/**
 * EthAddrs 实体对象
 * <p>File：EthAddrs.java</p>
 * <p>Title: EthAddrs</p>
 * <p>Description:EthAddrs</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "EthAddrs")
public class EthAddrs extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**区块对应高度*/
    @NotNull(message = "区块对应高度不可为空")
    @ApiModelProperty(value = "区块对应高度", required = true)
    private Long                 blockHeight;
    
    /**地址*/
    @NotNull(message = "地址不可为空")
    @ApiModelProperty(value = "地址", required = true)
    private String               addr;
    
    /**eth余额*/
    @NotNull(message = "eth余额不可为空")
    @ApiModelProperty(value = "eth余额", required = true)
    private java.math.BigDecimal ethBalance;

    /**是否已经领取*/
    @NotNull(message = "是否已经领取不可为空")
    @ApiModelProperty(value = "是否已经领取", required = true)
    private String isCollect;

    /**创建时间*/
    @NotNull(message = "创建时间不可为空")
    @ApiModelProperty(value = "创建时间", required = true)
    private java.util.Date       createDate;
    
    public Long getBlockHeight()
    {
        return this.blockHeight;
    }
    
    public void setBlockHeight(Long blockHeight)
    {
        this.blockHeight = blockHeight;
    }
    
    public String getAddr()
    {
        return this.addr;
    }
    
    public void setAddr(String addr)
    {
        this.addr = addr;
    }
    
    public java.math.BigDecimal getEthBalance()
    {
        return this.ethBalance;
    }
    
    public void setEthBalance(java.math.BigDecimal ethBalance)
    {
        this.ethBalance = ethBalance;
    }

    public String getIsCollect()
    {
        return isCollect;
    }

    public void setIsCollect(String isCollect)
    {
        this.isCollect = isCollect;
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
        return "EthAddrs{" +
                "blockHeight=" + blockHeight +
                ", addr='" + addr + '\'' +
                ", ethBalance=" + ethBalance +
                ", isCollect='" + isCollect + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}

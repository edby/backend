/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.trade.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>File：FeeModel.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2018-03-14 17:26:56/p>
 * <p>Company: BloCain</p>
 * @author zhangchunxi
 * @version 1.0
 */
public class FeeModel implements Serializable
{
    //
    private static final long serialVersionUID = 6541545936709826221L;
    
    // 帐户ID
    private Long              accountId;
    
    // 标的手续费
    private BigDecimal        tradeFee;
    
    // 计价手续费
    private BigDecimal        captalFee;
    
    public Long getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public BigDecimal getTradeFee()
    {
        return tradeFee;
    }
    
    public void setTradeFee(BigDecimal tradeFee)
    {
        this.tradeFee = tradeFee;
    }
    
    public BigDecimal getCaptalFee()
    {
        return captalFee;
    }
    
    public void setCaptalFee(BigDecimal captalFee)
    {
        this.captalFee = captalFee;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("FeeModel{");
        sb.append("accountId=").append(accountId);
        sb.append(", tradeFee=").append(tradeFee);
        sb.append(", captalFee=").append(captalFee);
        sb.append('}');
        return sb.toString();
    }
}

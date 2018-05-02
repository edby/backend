/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>File：ShareLossModel.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月11日 上午10:35:44</p>
 * <p>Company: BloCain</p>
 * @author sunbiao
 * @version 1.0
 */
public class ShareLossModel implements Serializable
{
    //
    private static final long serialVersionUID = 6541545936709826208L;
    
    /**超级用户委托中的BTC*/
    private BigDecimal        inEntrustBtc;
    
    /**超级用户USDX借款*/
    private BigDecimal        superDebit;
    
    /**行情或结算价*/
    private BigDecimal        hangqing;
    
    /**盈利 false=亏损*/
    private Boolean           result;
    
    /**需分摊BTC*/
    private BigDecimal        needBtc;
    
    /**全市场BTC持仓*/
    private BigDecimal        allBtc;
    
    /**全市场-总流入BTC数量*/
    private BigDecimal        allIn;
    
    /**全市场-总流出BTC数量*/
    private BigDecimal        allOut;
    
    /**全市场盈利*/
    private BigDecimal        superYingLi;
    
    /**用户ID*/
    private Long              accountId;
    
    /**用户名*/
    private String            accountName;
    
    /**用户BTC持仓*/
    private BigDecimal        personBtc;
    
    /**用户-总流入BTC数量*/
    private BigDecimal        personIn;
    
    /**用户-总流出BTC数量*/
    private BigDecimal        personOut;
    
    /**用户盈利*/
    private BigDecimal        personYingLi;
    
    /**用户分摊*/
    private BigDecimal        shareLossAmt;
    
    /**准备金*/
    private BigDecimal        reserveFund;
    
    public BigDecimal getInEntrustBtc()
    {
        return inEntrustBtc;
    }
    
    public void setInEntrustBtc(BigDecimal inEntrustBtc)
    {
        this.inEntrustBtc = inEntrustBtc;
    }
    
    public BigDecimal getSuperDebit()
    {
        return superDebit;
    }
    
    public void setSuperDebit(BigDecimal superDebit)
    {
        this.superDebit = superDebit;
    }
    
    public BigDecimal getHangqing()
    {
        return hangqing;
    }
    
    public void setHangqing(BigDecimal hangqing)
    {
        this.hangqing = hangqing;
    }
    
    public Boolean getResult()
    {
        return result;
    }
    
    public void setResult(Boolean result)
    {
        this.result = result;
    }
    
    public BigDecimal getNeedBtc()
    {
        return needBtc;
    }
    
    public void setNeedBtc(BigDecimal needBtc)
    {
        this.needBtc = needBtc;
    }
    
    public BigDecimal getAllBtc()
    {
        return allBtc;
    }
    
    public void setAllBtc(BigDecimal allBtc)
    {
        this.allBtc = allBtc;
    }
    
    public BigDecimal getAllIn()
    {
        return allIn;
    }
    
    public void setAllIn(BigDecimal allIn)
    {
        this.allIn = allIn;
    }
    
    public BigDecimal getAllOut()
    {
        return allOut;
    }
    
    public void setAllOut(BigDecimal allOut)
    {
        this.allOut = allOut;
    }
    
    public BigDecimal getSuperYingLi()
    {
        return superYingLi;
    }
    
    public void setSuperYingLi(BigDecimal superYingLi)
    {
        this.superYingLi = superYingLi;
    }
    
    public Long getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public String getAccountName()
    {
        return accountName;
    }
    
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    
    public BigDecimal getPersonBtc()
    {
        return personBtc;
    }
    
    public void setPersonBtc(BigDecimal personBtc)
    {
        this.personBtc = personBtc;
    }
    
    public BigDecimal getPersonIn()
    {
        return personIn;
    }
    
    public void setPersonIn(BigDecimal personIn)
    {
        this.personIn = personIn;
    }
    
    public BigDecimal getPersonOut()
    {
        return personOut;
    }
    
    public void setPersonOut(BigDecimal personOut)
    {
        this.personOut = personOut;
    }
    
    public BigDecimal getPersonYingLi()
    {
        return personYingLi;
    }
    
    public void setPersonYingLi(BigDecimal personYingLi)
    {
        this.personYingLi = personYingLi;
    }
    
    public BigDecimal getShareLossAmt()
    {
        return shareLossAmt;
    }
    
    public void setShareLossAmt(BigDecimal shareLossAmt)
    {
        this.shareLossAmt = shareLossAmt;
    }
    
    public BigDecimal getReserveFund()
    {
        return reserveFund;
    }
    
    public void setReserveFund(BigDecimal reserveFund)
    {
        this.reserveFund = reserveFund;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("ShareLossModel{");
        sb.append("inEntrustBtc=").append(inEntrustBtc);
        sb.append(", superDebit=").append(superDebit);
        sb.append(", hangqing=").append(hangqing);
        sb.append(", result=").append(result);
        sb.append(", needBtc=").append(needBtc);
        sb.append(", allBtc=").append(allBtc);
        sb.append(", allIn=").append(allIn);
        sb.append(", allOut=").append(allOut);
        sb.append(", superYingLi=").append(superYingLi);
        sb.append(", accountId=").append(accountId);
        sb.append(", accountName='").append(accountName).append('\'');
        sb.append(", personBtc=").append(personBtc);
        sb.append(", personIn=").append(personIn);
        sb.append(", personOut=").append(personOut);
        sb.append(", personYingLi=").append(personYingLi);
        sb.append(", shareLossAmt=").append(shareLossAmt);
        sb.append(", reserveFund=").append(reserveFund);
        sb.append('}');
        return sb.toString();
    }
}

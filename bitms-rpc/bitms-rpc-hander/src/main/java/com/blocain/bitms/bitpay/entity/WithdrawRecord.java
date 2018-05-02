/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.bitpay.entity;

import com.blocain.bitms.orm.core.GenericEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户提现记录 实体对象
 * <p>File：WithdrawRecord.java</p>
 * <p>Title: WithdrawRecord</p>
 * <p>Description:WithdrawRecord</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "账户提现记录")
public class WithdrawRecord extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**账户ID*/
    @ApiModelProperty(value = "账户ID")
    private java.lang.Long       accountId;
    
    /**提现地址*/
    @ApiModelProperty(value = "提现地址")
    private java.lang.String     raiseAddr;
    
    /**提现金额*/
    @ApiModelProperty(value = "提现金额")
    private java.math.BigDecimal occurAmt;
    
    /**手续费*/
    @ApiModelProperty(value = "手续费")
    private java.math.BigDecimal netFee;
    
    /**证券信息ID*/
    @ApiModelProperty(value = "证券信息ID")
    private java.lang.Long       stockinfoId;
    
    /**交易ID*/
    @ApiModelProperty(value = "交易ID")
    private java.lang.String       transId;
    
    /**状态：0、未提现 1、已提现 2、提现失败*/
    @ApiModelProperty(value = "状态：0、未提现 1、已提现 2、提现失败")
    private java.lang.Integer    state;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private java.lang.String     remark;
    
    /**创建时间*/
    @ApiModelProperty(value = "创建时间")
    private java.lang.Long       createDate;
    
    // ID集合
    private List<Long>           idList;
    
    public Long getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public String getRaiseAddr()
    {
        return raiseAddr;
    }
    
    public void setRaiseAddr(String raiseAddr)
    {
        this.raiseAddr = raiseAddr;
    }
    
    public BigDecimal getOccurAmt()
    {
        return occurAmt;
    }
    
    public void setOccurAmt(BigDecimal occurAmt)
    {
        this.occurAmt = occurAmt;
    }
    
    public BigDecimal getNetFee()
    {
        return netFee;
    }
    
    public void setNetFee(BigDecimal netFee)
    {
        this.netFee = netFee;
    }
    
    public Long getStockinfoId()
    {
        return stockinfoId;
    }
    
    public void setStockinfoId(Long stockinfoId)
    {
        this.stockinfoId = stockinfoId;
    }
    
    public String getTransId()
    {
        return transId;
    }
    
    public void setTransId(String transId)
    {
        this.transId = transId;
    }
    
    public Integer getState()
    {
        return state;
    }
    
    public void setState(Integer state)
    {
        this.state = state;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public Long getCreateDate()
    {
        return createDate;
    }
    
    public void setCreateDate(Long createDate)
    {
        this.createDate = createDate;
    }
    
    public List<Long> getIdList()
    {
        return idList;
    }
    
    public void setIdList(List<Long> idList)
    {
        this.idList = idList;
    }

    @Override public String toString()
    {
        final StringBuilder sb = new StringBuilder("WithdrawRecord{");
        sb.append("accountId=").append(accountId);
        sb.append(", raiseAddr='").append(raiseAddr).append('\'');
        sb.append(", occurAmt=").append(occurAmt);
        sb.append(", netFee=").append(netFee);
        sb.append(", stockinfoId=").append(stockinfoId);
        sb.append(", transId='").append(transId).append('\'');
        sb.append(", state=").append(state);
        sb.append(", remark='").append(remark).append('\'');
        sb.append(", createDate=").append(createDate);
        sb.append(", idList=").append(idList);
        sb.append('}');
        return sb.toString();
    }
}

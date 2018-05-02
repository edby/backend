/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.entity;

import com.blocain.bitms.orm.core.SignableEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.blocain.bitms.orm.core.GenericEntity;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;

/**
 * 账户收款银行表 实体对象
 * <p>File：AccountCollectBank.java</p>
 * <p>Title: AccountCollectBank</p>
 * <p>Description:AccountCollectBank</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "账户收款银行表")
public class AccountCollectBank extends SignableEntity
{
    private static final long serialVersionUID = 1L;
    
    /**账户ID*/
    @NotNull(message = "账户ID不可为空")
    @ApiModelProperty(value = "账户ID", required = true)
    private Long              accountId;
    
    /**证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "证券信息id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "证券信息id 对应Stockinfo表中的ID字段", required = true)
    private Long              stockinfoId;
    
    /**收款银行名称*/
    @NotNull(message = "收款银行名称不可为空")
    @ApiModelProperty(value = "收款银行名称", required = true)
    private String            bankName;
    
    /**收款卡号*/
    @NotNull(message = "收款卡号不可为空")
    @ApiModelProperty(value = "收款卡号", required = true)
    private String            cardNo;
    
    /**swift/Bic*/
    @NotNull(message = "swift/Bic不可为空")
    @ApiModelProperty(value = "swift/Bic", required = true)
    private String            swiftBic;
    
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
    
    // 用户名称
    private String            accountName;
    
    // 证券代码
    private String            stockName;
    
    public Long getAccountId()
    {
        return this.accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public Long getStockinfoId()
    {
        return this.stockinfoId;
    }
    
    public void setStockinfoId(Long stockinfoId)
    {
        this.stockinfoId = stockinfoId;
    }
    
    public String getBankName()
    {
        return this.bankName;
    }
    
    public void setBankName(String bankName)
    {
        this.bankName = bankName;
    }
    
    public String getCardNo()
    {
        return this.cardNo;
    }
    
    public void setCardNo(String cardNo)
    {
        this.cardNo = cardNo;
    }
    
    public String getSwiftBic()
    {
        return this.swiftBic;
    }
    
    public void setSwiftBic(String swiftBic)
    {
        this.swiftBic = swiftBic;
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
    
    public String getAccountName()
    {
        return accountName;
    }
    
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    
    public String getStockName()
    {
        return stockName;
    }
    
    public void setStockName(String stockName)
    {
        this.stockName = stockName;
    }
    
    @Override
    protected byte[] acquiresSignValue() throws UnsupportedEncodingException
    {
        return new byte[0];
    }
}

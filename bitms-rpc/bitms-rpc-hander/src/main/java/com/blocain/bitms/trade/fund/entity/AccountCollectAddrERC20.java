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
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;

/**
 * 账户收款地址表 实体对象
 * <p>File：AccountCollectAddrERC20.java</p>
 * <p>Title: AccountCollectAddrERC20</p>
 * <p>Description:AccountCollectAddrERC20</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "账户收款地址表")
public class AccountCollectAddrERC20 extends SignableEntity
{
    private static final long serialVersionUID = 1L;
    
    /**账户ID*/
    @NotNull(message = "账户ID不可为空")
    @ApiModelProperty(value = "账户ID", required = true)
    private Long              accountId;
    
    /**证券信息id 对应Stockinfo表中的ID字段(erc20虚拟化)*/
    @NotNull(message = "证券信息id 对应Stockinfo表中的ID字段(erc20虚拟化)不可为空")
    @ApiModelProperty(value = "证券信息id 对应Stockinfo表中的ID字段(erc20虚拟化)", required = true)
    private Long              stockinfoId;
    
    /**收款地址*/
    @NotNull(message = "收款地址不可为空")
    @ApiModelProperty(value = "收款地址", required = true)
    private String            collectAddr;
    
    /**认证状态(unauth未认证、auth已认证)*/
    @NotNull(message = "认证状态(unauth未认证、auth已认证)不可为空")
    @ApiModelProperty(value = "认证状态(unauth未认证、auth已认证)", required = true)
    private String            certStatus;
    
    /**激活状态(yes激活，no未激活)*/
    @NotNull(message = "激活状态(yes激活，no未激活)不可为空")
    @ApiModelProperty(value = "激活状态(yes激活，no未激活)", required = true)
    private String            isActivate;
    
    /**审核状态(yes已人工审核，no待人工审核)*/
    @NotNull(message = "审核状态(yes已人工审核，no待人工审核)不可为空")
    @ApiModelProperty(value = "审核状态(yes已人工审核，no待人工审核)", required = true)
    private String            status;
    
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
    
    public String getCollectAddr()
    {
        return this.collectAddr;
    }
    
    public void setCollectAddr(String collectAddr)
    {
        this.collectAddr = collectAddr;
    }
    
    public String getCertStatus()
    {
        return this.certStatus;
    }
    
    public void setCertStatus(String certStatus)
    {
        this.certStatus = certStatus;
    }
    
    public String getIsActivate()
    {
        return this.isActivate;
    }
    
    public void setIsActivate(String isActivate)
    {
        this.isActivate = isActivate;
    }
    
    public String getStatus()
    {
        return this.status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
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
        StringBuffer signValue = new StringBuffer(String.valueOf(this.id)).append(this.accountId);
        signValue.append(this.stockinfoId).append(this.collectAddr);
        signValue.append(this.certStatus);
        return signValue.toString().getBytes(CharsetConst.CHARSET_UT);
    }
}

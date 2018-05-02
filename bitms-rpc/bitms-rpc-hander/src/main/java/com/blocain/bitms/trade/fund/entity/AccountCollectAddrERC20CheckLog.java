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
 * ERC20账户收款地址审核日志表 实体对象
 * <p>File：AccountCollectAddrERC20CheckLog.java</p>
 * <p>Title: AccountCollectAddrERC20CheckLog</p>
 * <p>Description:AccountCollectAddrERC20CheckLog</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "ERC20账户收款地址审核日志表")
public class AccountCollectAddrERC20CheckLog extends GenericEntity
{
    private static final long serialVersionUID = 1L;
    
    /**账户收款地址ID*/
    @NotNull(message = "账户收款地址ID不可为空")
    @ApiModelProperty(value = "账户收款地址ID", required = true)
    private Long              collectAddrId;
    
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
    
    public Long getCollectAddrId()
    {
        return this.collectAddrId;
    }
    
    public void setCollectAddrId(Long collectAddrId)
    {
        this.collectAddrId = collectAddrId;
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
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.settlement.entity;

import com.blocain.bitms.orm.core.GenericEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 清算交割流程表 实体对象
 * <p>File：SettlementProcess.java</p>
 * <p>Title: SettlementProcess</p>
 * <p>Description:SettlementProcess</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "清算交割流程表")
public class SettlementProcess extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**父流程*/
    @NotNull(message = "父流程不可为空")
    @ApiModelProperty(value = "父流程", required = true)
    private Long                 parentProcessId;
    
    /**流程名*/
    @NotNull(message = "流程名不可为空")
    @ApiModelProperty(value = "流程名", required = true)
    private String               processName;
    
    /**状态(0表示未执行,1表示执行成功,-1表示执行失败)*/
    @NotNull(message = "状态(0表示未执行,1表示执行成功,-1表示执行失败)不可为空")
    @ApiModelProperty(value = "状态(0表示未执行,1表示执行成功,-1表示执行失败)", required = true)
    private java.math.BigDecimal status;
    
    /**最后更新日期*/
    @NotNull(message = "最后更新日期不可为空")
    @ApiModelProperty(value = "最后更新日期", required = true)
    private java.util.Date       updateDate;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String               remark;
    
    public Long getParentProcessId()
    {
        return this.parentProcessId;
    }
    
    public void setParentProcessId(Long parentProcessId)
    {
        this.parentProcessId = parentProcessId;
    }
    
    public String getProcessName()
    {
        return this.processName;
    }
    
    public void setProcessName(String processName)
    {
        this.processName = processName;
    }
    
    public java.math.BigDecimal getStatus()
    {
        return this.status;
    }
    
    public void setStatus(java.math.BigDecimal status)
    {
        this.status = status;
    }
    
    public java.util.Date getUpdateDate()
    {
        return this.updateDate;
    }
    
    public void setUpdateDate(java.util.Date updateDate)
    {
        this.updateDate = updateDate;
    }
    
    public String getRemark()
    {
        return this.remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
}

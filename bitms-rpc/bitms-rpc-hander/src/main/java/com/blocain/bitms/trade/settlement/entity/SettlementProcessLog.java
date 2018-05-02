/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.settlement.entity;

import com.blocain.bitms.orm.core.GenericEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 清算交割流程记录表 实体对象
 * <p>File：SettlementProcessLog.java</p>
 * <p>Title: SettlementProcessLog</p>
 * <p>Description:SettlementProcessLog</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "清算交割流程记录表")
public class SettlementProcessLog extends GenericEntity
{
    private static final long  serialVersionUID = 1L;
    
    /**父流程*/
    @NotNull(message = "父流程不可为空")
    @ApiModelProperty(value = "父流程", required = true)
    private Long               processId;
    
    /**流程名*/
    @NotNull(message = "流程名不可为空")
    @ApiModelProperty(value = "流程名", required = true)
    private String             processName;
    
    /**状态(0表示未执行,1表示执行成功,-1表示执行失败)*/
    @NotNull(message = "状态(0表示未执行,1表示执行成功,-1表示执行失败)不可为空")
    @ApiModelProperty(value = "状态(0表示未执行,1表示执行成功,-1表示执行失败)", required = true)
    private BigDecimal         status;
    
    /**创建人*/
    @NotNull(message = "创建人不可为空")
    @ApiModelProperty(value = "创建人", required = true)
    private Long               createBy;
    
    /**最后更新日期*/
    @NotNull(message = "最后更新日期不可为空")
    @ApiModelProperty(value = "最后更新日期", required = true)
    private java.sql.Timestamp createDate;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String             remark;
    
    public Long getProcessId()
    {
        return this.processId;
    }
    
    public void setProcessId(Long processId)
    {
        this.processId = processId;
    }
    
    public String getProcessName()
    {
        return this.processName;
    }
    
    public void setProcessName(String processName)
    {
        this.processName = processName;
    }
    
    public BigDecimal getStatus()
    {
        return this.status;
    }
    
    public void setStatus(BigDecimal status)
    {
        this.status = status;
    }
    
    public Long getCreateBy()
    {
        return this.createBy;
    }
    
    public void setCreateBy(Long createBy)
    {
        this.createBy = createBy;
    }
    
    public java.sql.Timestamp getCreateDate()
    {
        return this.createDate;
    }
    
    public void setCreateDate(java.sql.Timestamp createDate)
    {
        this.createDate = createDate;
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

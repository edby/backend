/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.entity;

import javax.validation.constraints.NotNull;

import com.blocain.bitms.orm.core.GenericEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 监控服务运行日志 实体对象
 * <p>File：MonitorRunLogs.java</p>
 * <p>Title: MonitorRunLogs</p>
 * <p>Description:MonitorRunLogs</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author fzk
 * @version 1.0
 */
@ApiModel(description = "MonitorRunLogs")
public class MonitorRunLogs extends GenericEntity
{
    private static final long serialVersionUID = 1L;
    
    /**monitorCode*/
    @NotNull(message = "monitorCode不可为空")
    @ApiModelProperty(value = "monitorCode", required = true)
    private String            monitorCode;
    
    /**日期*/
    @NotNull(message = "日期不可为空")
    @ApiModelProperty(value = "createDate", required = true)
    private String            createDate;
    
    /**logDesc*/
    @ApiModelProperty(value = "logDesc")
    private String            logDesc;
    
    private String            monitorName;
    
    private String            timeStart;
    
    private String            timeEnd;
    
    public String getCreateDate()
    {
        return createDate;
    }
    
    public void setCreateDate(String createDate)
    {
        this.createDate = createDate;
    }
    
    public String getMonitorName()
    {
        return monitorName;
    }
    
    public void setMonitorName(String monitorName)
    {
        this.monitorName = monitorName;
    }
    
    public String getTimeStart()
    {
        return timeStart;
    }
    
    public void setTimeStart(String timeStart)
    {
        this.timeStart = timeStart;
    }
    
    public String getTimeEnd()
    {
        return timeEnd;
    }
    
    public void setTimeEnd(String timeEnd)
    {
        this.timeEnd = timeEnd;
    }
    
    public String getMonitorCode()
    {
        return this.monitorCode;
    }
    
    public void setMonitorCode(String monitorCode)
    {
        this.monitorCode = monitorCode;
    }
    
    public String getLogDesc()
    {
        return this.logDesc;
    }
    
    public void setLogDesc(String logDesc)
    {
        this.logDesc = logDesc;
    }
    
    @Override
    public String toString()
    {
        return "MonitorRunLogs{" + "monitorCode='" + monitorCode + '\'' + ", createDate='" + createDate + '\'' + ", logDesc='" + logDesc + '\'' + ", monitorName='"
                + monitorName + '\'' + ", timeStart='" + timeStart + '\'' + ", timeEnd='" + timeEnd + '\'' + '}';
    }
}

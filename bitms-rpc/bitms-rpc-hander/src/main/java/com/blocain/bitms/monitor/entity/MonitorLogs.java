/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.entity;

import com.blocain.bitms.tools.utils.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;
import java.util.Random;
import javax.validation.constraints.NotNull;

import com.blocain.bitms.orm.core.GenericEntity;

/**
 * 监控日志表 实体对象
 * <p>File：MonitorLogs.java</p>
 * <p>Title: MonitorLogs</p>
 * <p>Description:MonitorLogs</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "监控日志表")
public class MonitorLogs extends GenericEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "监控类型(资金监控: FUNDCURRENT；ICO监控: ICO；负资产监控：NEGATIVEASSET；余额监控：ACCTBAL)")
    @ApiModelProperty(value = "监控类型(资金监控: FUNDCURRENT；ICO监控: ICO；负资产监控：NEGATIVEASSET；余额监控：ACCTBAL)", required = true)
    private String monitorType;

    @NotNull(message = "监控子类型（平台：PLAT；账户：ACCT）")
    @ApiModelProperty(value = "监控子类型（平台：PLAT；账户：ACCT）", required = true)
    private String monitorSubType;

    @NotNull(message = "监控类型描述")
    @ApiModelProperty(value = "监控类型描述", required = true)
    private String monitorName;

    @NotNull(message = "监控错误日志描述")
    @ApiModelProperty(value = "监控错误日志描述", required = true)
    private String monitorLogDesc;

    @NotNull(message = "业务品种ID")
    @ApiModelProperty(value = "业务品种ID", required = true)
    private Long bizCategoryId;

    @NotNull(message = "监控结果(-1 异常、1 正常)")
    @ApiModelProperty(value = "监控结果(-1 异常、1 正常)", required = true)
    private Integer monitorResult;

    @NotNull(message = "监控日期")
    @ApiModelProperty(value = "监控日期", required = true)
    private Timestamp monitorDate;

    @NotNull(message = "监控结果流水表")
    @ApiModelProperty(value = "监控结果流水表", required = true)
    private String targetTable;

    //关联属性，与stockinfo表中stockname对应
    private String stockName;

    //关联属性，与总账表ID对应
    private Long relatedId;

    /**
     * 用户界面传值 查询开始时间
     */
    private Long timeStart;

    /**
     * 用户界面传值 查询结束时间
     */
    private Long timeEnd;

    public Long getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Long relatedId) {
        this.relatedId = relatedId;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getMonitorLogDesc() {
        return monitorLogDesc;
    }

    public void setMonitorLogDesc(String monitorLogDesc) {
        this.monitorLogDesc = monitorLogDesc;
    }

    public Long getBizCategoryId() {
        return bizCategoryId;
    }

    public void setBizCategoryId(Long bizCategoryId) {
        this.bizCategoryId = bizCategoryId;
    }

    public String getMonitorName() {
        return this.monitorName;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }

    public Integer getMonitorResult() {
        return this.monitorResult;
    }

    public void setMonitorResult(Integer monitorResult) {
        this.monitorResult = monitorResult;
    }

    public String getTargetTable() {
        return this.targetTable;
    }

    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    public Long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Long timeStart) {
        this.timeStart = timeStart;
    }

    public Long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Long timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getMonitorType() {
        return monitorType;
    }

    public void setMonitorType(String monitorType) {
        this.monitorType = monitorType;
    }

    public String getMonitorSubType() {
        return monitorSubType;
    }

    public void setMonitorSubType(String monitorSubType) {
        this.monitorSubType = monitorSubType;
    }

    public Timestamp getMonitorDate() {
        return monitorDate;
    }

    public void setMonitorDate(Timestamp monitorDate) {
        this.monitorDate = monitorDate;
    }

    public void setAttr(Long stockid,String monitorType,String monitorSubType,String monitorName,Integer monitorResult,String monitorLogDesc)
    {
        Random rand = new Random();
        super.id = 990000000000000000L+System.currentTimeMillis()*1000+rand.nextInt(10);
        this.bizCategoryId = stockid;
        this.monitorType = monitorType;
        this.monitorSubType = monitorSubType;
        this.monitorName = monitorName;
        this.monitorResult = monitorResult;
        this.monitorLogDesc = monitorLogDesc;
    }
}

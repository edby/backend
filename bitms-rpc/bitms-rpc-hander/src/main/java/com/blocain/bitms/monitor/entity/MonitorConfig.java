/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.entity;

import com.blocain.bitms.orm.core.GenericEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * MonitorConfig 实体对象
 * <p>File：MonitorConfig.java</p>
 * <p>Title: MonitorConfig</p>
 * <p>Description:MonitorConfig</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "MonitorConfig")
public class MonitorConfig extends GenericEntity {

    private static final long serialVersionUID = 1L;

    /**
     * monitorCode
     */
    @NotNull(message = "monitorCode不可为空")
    @ApiModelProperty(value = "monitorCode", required = true)
    private String monitorCode;
    /**
     * monitorName
     */
    @NotNull(message = "monitorName不可为空")
    @ApiModelProperty(value = "monitorName", required = true)
    private String monitorName;
    /**
     * pollingTime
     */
    @NotNull(message = "pollingTime不可为空")
    @ApiModelProperty(value = "pollingTime", required = true)
    private Long pollingTime;
    /**
     * monitorCategorys
     */
    @ApiModelProperty(value = "monitorCategorys")
    private String monitorCategorys;
    private String monitorCategorys_str;
    /**
     * active
     */
    @NotNull(message = "active不可为空")
    @ApiModelProperty(value = "active", required = true)
    private Boolean active;
    /**
     * createBy
     */
    @NotNull(message = "createBy不可为空")
    @ApiModelProperty(value = "createBy", required = true)
    private String createBy;
    /**
     * monitorDesc
     */
    @ApiModelProperty(value = "monitorDesc")
    private String monitorDesc;
    /**
     * createDate
     */
    @ApiModelProperty(value = "createDate")
    private Timestamp createDate;
    //关联表数据集合，通过forbidActionValue与MonitorParam表id关联

    // 指标
    private Long idxid1;

    private Long idxid2;

    private Long idxid3;

    private Long idxid4;

    private String idxName1;
    private String idxName2;
    private String idxName3;
    private String idxName4;

    private HashMap<Long, MonitorIndex> idxids;
    private List<MonitorParam> warnParamList;

    private List<MonitorParam> forbidParamList;

    public String getIdxName1() {
        return idxName1;
    }

    public void setIdxName1(String idxName1) {
        this.idxName1 = idxName1;
    }

    public String getIdxName2() {
        return idxName2;
    }

    public void setIdxName2(String idxName2) {
        this.idxName2 = idxName2;
    }

    public String getIdxName3() {
        return idxName3;
    }

    public void setIdxName3(String idxName3) {
        this.idxName3 = idxName3;
    }

    public String getIdxName4() {
        return idxName4;
    }

    public void setIdxName4(String idxName4) {
        this.idxName4 = idxName4;
    }

    public Long getIdxid1() {
        return idxid1;
    }

    public void setIdxid1(Long idxid1) {
        this.idxid1 = idxid1;
    }

    public Long getIdxid2() {
        return idxid2;
    }

    public void setIdxid2(Long idxid2) {
        this.idxid2 = idxid2;
    }

    public Long getIdxid3() {
        return idxid3;
    }

    public void setIdxid3(Long idxid3) {
        this.idxid3 = idxid3;
    }

    public Long getIdxid4() {
        return idxid4;
    }

    public void setIdxid4(Long idxid4) {
        this.idxid4 = idxid4;
    }

    public HashMap<Long, MonitorIndex> getIdxids() {
        return idxids;
    }

    public void setIdxids(HashMap<Long, MonitorIndex> idxids) {
        this.idxids = idxids;
    }

    public List<MonitorParam> getWarnParamList() {
        return warnParamList;
    }

    public void setWarnParamList(List<MonitorParam> warnParamList) {
        this.warnParamList = warnParamList;
    }

    public List<MonitorParam> getForbidParamList() {
        return forbidParamList;
    }

    public void setForbidParamList(List<MonitorParam> forbidParamList) {
        this.forbidParamList = forbidParamList;
    }

    public String getMonitorCode() {
        return this.monitorCode;
    }

    public void setMonitorCode(String monitorCode) {
        this.monitorCode = monitorCode;
    }

    public String getMonitorName() {
        return this.monitorName;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }

    public Long getPollingTime() {
        return this.pollingTime;
    }

    public void setPollingTime(Long pollingTime) {
        this.pollingTime = pollingTime;
    }

    public String getMonitorCategorys() {
        return this.monitorCategorys;
    }

    public void setMonitorCategorys(String monitorCategorys) {
        this.monitorCategorys = monitorCategorys;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getMonitorDesc() {
        return this.monitorDesc;
    }

    public void setMonitorDesc(String monitorDesc) {
        this.monitorDesc = monitorDesc;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getMonitorCategorys_str() {
        return monitorCategorys_str;
    }

    public void setMonitorCategorys_str(String monitorCategorys_str) {
        this.monitorCategorys_str = monitorCategorys_str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonitorConfig that = (MonitorConfig) o;
        return Objects.equals(monitorCode, that.monitorCode)
                && Objects.equals(monitorName, that.monitorName)
                && Objects.equals(pollingTime, that.pollingTime)
                && Objects
                .equals(monitorCategorys, that.monitorCategorys)
                && Objects.equals(idxid1, that.idxid1)
                && Objects.equals(idxid2, that.idxid2)
                && Objects.equals(idxid3, that.idxid3)
                && Objects
                .equals(idxid4, that.idxid4)
                && Objects.equals(idxName1, that.idxName1)
                && Objects
                .equals(idxName2, that.idxName2)
                && Objects.equals(idxName3, that.idxName3)
                && Objects
                .equals(idxName4, that.idxName4)
                && Objects.equals(active, that.active)
                && Objects.equals(createBy, that.createBy)
                && Objects
                .equals(monitorDesc, that.monitorDesc)
                && Objects.equals(createDate, that.createDate)
                && Objects.equals(idxids, that.idxids);
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(monitorCode, monitorName, pollingTime, monitorCategorys, idxid1, idxid2, idxid3, idxid4, idxName1,
                        idxName2, idxName3, idxName4, active, createBy, monitorDesc, createDate, idxids);
    }

    @Override
    public String toString() {
        return "MonitorConfig{" +
                "monitorCode='" + monitorCode + '\'' +
                ", monitorName='" + monitorName + '\'' +
                ", pollingTime=" + pollingTime +
                ", monitorCategorys='" + monitorCategorys + '\'' +
                ", active=" + active +
                ", createBy='" + createBy + '\'' +
                ", monitorDesc='" + monitorDesc + '\'' +
                ", createDate=" + createDate +
                ", idxid1=" + idxid1 +
                ", idxid2=" + idxid2 +
                ", idxid3=" + idxid3 +
                ", idxid4=" + idxid4 +
                ", idxName1='" + idxName1 + '\'' +
                ", idxName2='" + idxName2 + '\'' +
                ", idxName3='" + idxName3 + '\'' +
                ", idxName4='" + idxName4 + '\'' +
                ", idxids=" + idxids +
                ", warnParamList=" + warnParamList +
                ", forbidParamList=" + forbidParamList +
                ", id=" + id +
                '}';
    }
}


package com.blocain.bitms.monitor.entity;

import com.blocain.bitms.orm.core.GenericEntity;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class MonitorDetail extends GenericEntity {

    private static final long serialVersionUID = -8088342944272676952L;

    // 证券ID
    @NotNull(message = "证券ID不可为空")
    private Long stockinfoId;
    // 业务品种ID
    @NotNull(message = "业务品种ID不可为空")
    private Long bizCategoryId;
    //关联id,与stockinfo表id对应
    @NotNull(message = "关联id不可为空")
    private Long relatedStockinfoId;
    //账户资产类型(1.钱包资产，2.杠杆资产，3.理财资产)
    @NotNull(message = "账户资产类型不可为空")
    private Integer acctAssetType;
    //账户ID
    @NotNull(message = "账户ID不可为空")
    private Long accountId;
    //原业务流水ID
    @NotNull(message = "原业务流水ID不可为空")
    private Long originalBusinessId;
    //关联业务流水ID
    @NotNull(message = "关联业务流水ID不可为空")
    private Long relatedBusinessId;
    //业务类别，同资金流水表
    @NotNull(message = "业务类别不可为空")
    private String businessFlag;
    //业务时间
    @NotNull(message = "业务时间不可为空")
    private Timestamp businessDate;
    //数据来源
    @NotNull(message = "数据来源不可为空")
    private Integer dataSource;
    //监控结果描述
    private String monitorDesc;
    //监控时间
    private Timestamp monitorDate;

    private String monitorDateStr;

    public Integer getAcctAssetType() {
        return acctAssetType;
    }

    public void setAcctAssetType(Integer acctAssetType) {
        this.acctAssetType = acctAssetType;
    }

    public Integer getDataSource() {
        return dataSource;
    }

    public void setDataSource(Integer dataSource) {
        this.dataSource = dataSource;
    }

    public String getMonitorDateStr() {
        return monitorDateStr;
    }

    public void setMonitorDateStr(String monitorDateStr) {
        this.monitorDateStr = monitorDateStr;
    }

    public Long getRelatedStockinfoId() {
        return relatedStockinfoId;
    }

    public void setRelatedStockinfoId(Long relatedStockinfoId) {
        this.relatedStockinfoId = relatedStockinfoId;
    }

    public Long getStockinfoId() {
        return stockinfoId;
    }

    public void setStockinfoId(Long stockinfoId) {
        this.stockinfoId = stockinfoId;
    }

    public Long getBizCategoryId() {
        return bizCategoryId;
    }

    public void setBizCategoryId(Long bizCategoryId) {
        this.bizCategoryId = bizCategoryId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getOriginalBusinessId() {
        return originalBusinessId;
    }

    public void setOriginalBusinessId(Long originalBusinessId) {
        this.originalBusinessId = originalBusinessId;
    }

    public Long getRelatedBusinessId() {
        return relatedBusinessId;
    }

    public void setRelatedBusinessId(Long relatedBusinessId) {
        this.relatedBusinessId = relatedBusinessId;
    }

    public String getBusinessFlag() {
        return businessFlag;
    }

    public void setBusinessFlag(String businessFlag) {
        this.businessFlag = businessFlag;
    }

    public Timestamp getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(Timestamp businessDate) {
        this.businessDate = businessDate;
    }

    public String getMonitorDesc() {
        return monitorDesc;
    }

    public void setMonitorDesc(String monitorDesc) {
        this.monitorDesc = monitorDesc;
    }

    public Timestamp getMonitorDate() {
        return monitorDate;
    }

    public void setMonitorDate(Timestamp monitorDate) {
        this.monitorDate = monitorDate;
    }

    @Override
    public String toString() {
        return "MonitorDetail{" +
                "stockinfoId=" + stockinfoId +
                ", bizCategoryId=" + bizCategoryId +
                ", relatedStockinfoId=" + relatedStockinfoId +
                ", accountId=" + accountId +
                ", originalBusinessId=" + originalBusinessId +
                ", relatedBusinessId=" + relatedBusinessId +
                ", businessFlag='" + businessFlag + '\'' +
                ", businessDate=" + businessDate +
                ", monitorDesc='" + monitorDesc + '\'' +
                ", monitorDate=" + monitorDate +
                '}';
    }
}

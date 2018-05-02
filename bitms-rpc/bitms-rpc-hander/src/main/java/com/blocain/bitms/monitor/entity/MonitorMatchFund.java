package com.blocain.bitms.monitor.entity;

import com.blocain.bitms.orm.core.GenericEntity;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import io.swagger.annotations.ApiModel;

import java.math.BigDecimal;
import java.sql.Timestamp;

@ApiModel(description = "撮合交易总账监控表")
public class MonitorMatchFund extends GenericEntity {
    private static final long serialVersionUID = 1L;

    // 监控类型
    private String monitorType;

    // 监控子类型
    private String monitorSubType;

    //证券类型
    private String stockType;

    // 证券ID
    private Long stockinfoId;

    // 业务品种ID
    private Long bizCategoryId;

    // 资产余额
    private BigDecimal assetBal;

    // 冻结资产余额
    private BigDecimal assetFrozenBal;

    // 负债余额
    private BigDecimal debetBal;

    // 资金流水总额
    private BigDecimal curAssetBal;

    // 资金流水冻结总额
    private BigDecimal curFrozenBal;

    // 资金流水负债总额
    private BigDecimal curDebetBal;

    // 数字货币流入总额
    private BigDecimal inBal;

    // 数字货币流出总额
    private BigDecimal outBal;

    // 当前负债普通用户基于资金流水的负债余额
    private BigDecimal borrowDebetBal;

    // 费用总额
    private BigDecimal feeBal;

    // 强平负债转入的余额
    private BigDecimal closePositionDebetBal;

    // 强平资产转入的余额
    private BigDecimal closePositionAssetBal;

    // 残渣退款余额
    private BigDecimal closePositionReturnBal;

    private Integer chkResult;

    private Timestamp chkDate;

    private String monitorDesc;

    private String timeStart;

    private String timeEnd;

    private String stockName;
    //对应stockinfo表id
    private Long relatedStockinfoId;
    //资产类型
    private Integer acctAssetType;

    //监控总体状态（正常、异常）
    private Integer chkStatus;
    //异常记录条数
    private Integer abNormalCount;







    @Override
    public String toString() {
        return "MonitorMatchFund{" +
                "monitorType='" + monitorType + '\'' +
                ", monitorSubType='" + monitorSubType + '\'' +
                ", stockType='" + stockType + '\'' +
                ", stockinfoId=" + stockinfoId +
                ", bizCategoryId=" + bizCategoryId +
                ", assetBal=" + assetBal +
                ", assetFrozenBal=" + assetFrozenBal +
                ", debetBal=" + debetBal +
                ", curAssetBal=" + curAssetBal +
                ", curFrozenBal=" + curFrozenBal +
                ", curDebetBal=" + curDebetBal +
                ", inBal=" + inBal +
                ", outBal=" + outBal +
                ", borrowDebetBal=" + borrowDebetBal +
                ", feeBal=" + feeBal +
                ", closePositionDebetBal=" + closePositionDebetBal +
                ", closePositionAssetBal=" + closePositionAssetBal +
                ", closePositionReturnBal=" + closePositionReturnBal +
                ", chkResult=" + chkResult +
                ", chkDate=" + chkDate +
                ", monitorDesc='" + monitorDesc + '\'' +
                ", timeStart='" + timeStart + '\'' +
                ", timeEnd='" + timeEnd + '\'' +
                ", stockName='" + stockName + '\'' +
                ", relatedStockinfoId=" + relatedStockinfoId +
                '}';
    }

    public Integer getChkStatus() {
        return chkStatus;
    }

    public void setChkStatus(Integer chkStatus) {
        this.chkStatus = chkStatus;
    }

    public Integer getAbNormalCount() {
        return abNormalCount;
    }

    public void setAbNormalCount(Integer abNormalCount) {
        this.abNormalCount = abNormalCount;
    }

    public Integer getAcctAssetType() {
        return acctAssetType;
    }

    public void setAcctAssetType(Integer acctAssetType) {
        this.acctAssetType = acctAssetType;
    }

    public Long getRelatedStockinfoId() {
        return relatedStockinfoId;
    }

    public void setRelatedStockinfoId(Long relatedStockinfoId) {
        this.relatedStockinfoId = relatedStockinfoId;
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

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
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

    public BigDecimal getAssetBal() {
        return assetBal;
    }

    public void setAssetBal(BigDecimal assetBal) {
        this.assetBal = assetBal;
    }

    public BigDecimal getAssetFrozenBal() {
        return assetFrozenBal;
    }

    public void setAssetFrozenBal(BigDecimal assetFrozenBal) {
        this.assetFrozenBal = assetFrozenBal;
    }

    public BigDecimal getDebetBal() {
        return debetBal;
    }

    public void setDebetBal(BigDecimal debetBal) {
        this.debetBal = debetBal;
    }

    public BigDecimal getCurAssetBal() {
        return curAssetBal;
    }

    public void setCurAssetBal(BigDecimal curAssetBal) {
        this.curAssetBal = curAssetBal;
    }

    public BigDecimal getCurFrozenBal() {
        return curFrozenBal;
    }

    public void setCurFrozenBal(BigDecimal curFrozenBal) {
        this.curFrozenBal = curFrozenBal;
    }

    public BigDecimal getCurDebetBal() {
        return curDebetBal;
    }

    public void setCurDebetBal(BigDecimal curDebetBal) {
        this.curDebetBal = curDebetBal;
    }

    public BigDecimal getInBal() {
        return inBal;
    }

    public void setInBal(BigDecimal inBal) {
        this.inBal = inBal;
    }

    public BigDecimal getOutBal() {
        return outBal;
    }

    public void setOutBal(BigDecimal outBal) {
        this.outBal = outBal;
    }

    public BigDecimal getBorrowDebetBal() {
        return borrowDebetBal;
    }

    public void setBorrowDebetBal(BigDecimal borrowDebetBal) {
        this.borrowDebetBal = borrowDebetBal;
    }

    public BigDecimal getFeeBal() {
        return feeBal;
    }

    public void setFeeBal(BigDecimal feeBal) {
        this.feeBal = feeBal;
    }

    public BigDecimal getClosePositionDebetBal() {
        return closePositionDebetBal;
    }

    public void setClosePositionDebetBal(BigDecimal closePositionDebetBal) {
        this.closePositionDebetBal = closePositionDebetBal;
    }

    public BigDecimal getClosePositionAssetBal() {
        return closePositionAssetBal;
    }

    public void setClosePositionAssetBal(BigDecimal closePositionAssetBal) {
        this.closePositionAssetBal = closePositionAssetBal;
    }

    public BigDecimal getClosePositionReturnBal() {
        return closePositionReturnBal;
    }

    public void setClosePositionReturnBal(BigDecimal closePositionReturnBal) {
        this.closePositionReturnBal = closePositionReturnBal;
    }

    public Integer getChkResult() {
        return chkResult;
    }

    public void setChkResult(Integer chkResult) {
        this.chkResult = chkResult;
    }

    public Timestamp getChkDate() {
        return chkDate;
    }

    public void setChkDate(Timestamp chkDate) {
        this.chkDate = chkDate;
    }

    public String getMonitorDesc() {
        return monitorDesc;
    }

    public void setMonitorDesc(String monitorDesc) {
        this.monitorDesc = monitorDesc;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }
}

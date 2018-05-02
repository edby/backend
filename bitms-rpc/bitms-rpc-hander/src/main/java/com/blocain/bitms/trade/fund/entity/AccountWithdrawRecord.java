/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.entity;

import com.blocain.bitms.orm.core.GenericEntity;
import com.blocain.bitms.tools.annotation.ExcelField;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 账户提现记录表 实体对象
 * <p>File：AccountWithdrawRecord.java</p>
 * <p>Title: AccountWithdrawRecord</p>
 * <p>Description:AccountWithdrawRecord</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class AccountWithdrawRecord extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**时间*/
    @ExcelField(title = "时间")
    private String               currentDateStr;       // 用于导出 顺序第一个
    
    /**账户ID*/
    @NotNull(message = "账户ID不可为空")
    private Long                 accountId;
    
    /**账户类型(钱包账户、合约账户)*/
    @NotNull(message = "账户类型不可为空")
    @ExcelField(title = "账户类型")
    private String               accountAssetType;
    
    /**账户资产ID*/
    @NotNull(message = "账户资产ID不可为空")
    private Long                 accountAssetId;
    
    /**流水时间戳*/
    @NotNull(message = "流水时间戳不可为空")
    private Timestamp            currentDate;
    
    /**业务类别(钱包账户充值、钱包账户提现、钱包账户转合约账户、合约账户转钱包账户)*/
    @NotNull(message = "业务类别不可为空")
    @ExcelField(title = "业务类型")
    private String               businessFlag;
    
    /**证券id*/
    @NotNull(message = "证券id不可为空")
    private Long                 stockinfoId;
    
    /**证券代码*/
    @ExcelField(title = "资产分类")
    private String               stockCode;
    
    /**合约数量只限合约相关的*/
    @NotNull(message = "合约数量只限合约相关的不可为空")
    private BigDecimal           contractAmt;
    
    /**资产发生方向(增加、减少)*/
    @NotNull(message = "资产发生方向不可为空")
    @ExcelField(title = "资产发生方向")
    private String               occurDirect;
    
    /**原资产数量余额*/
    @NotNull(message = "原资产数量余额不可为空")
    @ExcelField(title = "原资产数量余额")
    private BigDecimal           orgAmt;
    
    /**资产发生数量*/
    @NotNull(message = "资产发生数量不可为空")
    @ExcelField(title = "发生数量")
    private BigDecimal           occurAmt;
    
    /**最新资产数量余额*/
    @NotNull(message = "最新资产数量余额不可为空")
    @ExcelField(title = "最新余额")
    private BigDecimal           lastAmt;
    
    /**原冻结数量余额*/
    @NotNull(message = "原冻结数量余额不可为空")
    private BigDecimal           forzenOrgAmt;
    
    /**冻结解冻发生数量*/
    @NotNull(message = "冻结解冻发生数量不可为空")
    private BigDecimal           occurForzenAmt;
    
    /**最新冻结数量余额*/
    @NotNull(message = "最新冻结数量余额不可为空")
    private BigDecimal           forzenLastAmt;
    
    /**备注*/
    private String               remark;
    
    /**证券名称*/
    private String               stockName;
    
    /**关联证券名称*/
    private String               relatedStockName;
    
    /**证券类型*/
    private String               stockType;
    
    /**充币目标地址*/
    private String               chargeAddr;
    
    /**提币目标地址*/
    private String               withdrawAddr;
    
    /**费用*/
    @NotNull(message = "费用不可为空")
    private BigDecimal           fee;
    
    /**充值提现网络转账费用*/
    private BigDecimal           netFee;
    
    /**状态(有效、无效)*/
    @NotNull(message = "状态(有效、无效)不可为空")
    private String               status;
    
    /**审批状态(无需审批、待审核、审核绝对、待复核、复核通过、复核拒绝)*/
    @NotNull(message = "审批状态(无需审批、待审核、审核绝对、待复核、复核通过、复核拒绝)不可为空")
    private String               approveStatus;
    
    /**划拨状态(noTransfer无需划拨unTransfer未划拨、Transfer已划拨)*/
    @NotNull(message = "划拨状态(noTransfer无需划拨unTransfer未划拨、Transfer已划拨)")
    private String               transferStatus;
    
    /**区块确认状态（unconfirm未确认、confirm已确认）*/
    @ApiModelProperty(value = "区块确认状态（unconfirm未确认、confirm已确认）", required = true)
    private String               confirmStatus;
    
    /**原始业务ID*/
    @NotNull(message = "原始业务ID")
    private Long                 originalBusinessId;
    
    /**关联证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "关联证券信息id 对应Stockinfo表中的ID字段")
    private Long                 relatedStockinfoId;
    
    /**创建人*/
    private Long                 createBy;
    
    /**创建时间*/
    @NotNull(message = "创建时间不可为空")
    private Timestamp            createDate;
    
    /**审核人*/
    private Long                 auditBy;
    
    /**审核时间*/
    private Timestamp            auditDate;
    
    /**复核人*/
    private Long                 checkBy;
    
    /**复核时间*/
    private Timestamp            checkDate;
    
    /**帐号*/
    private String               accountName;
    
    /**实际划拨费用*/
    private java.math.BigDecimal realTransferFee;
    
    /**
     * 用户编号
     */
    private Long                 unid;
    
    /**交易ID*/
    private String               transId;
    
    /**归集状态(noCollect无需归集unCollect未归集、collected已归集)*/
    @NotNull(message = "归集状态不可为空")
    private String               collectStatus;
    
    /**用户界面传值 查询开始时间 */
    private String               timeStart;
    
    /**用户界面传值 查询结束时间 */
    private String               timeEnd;
    
    /**钱包余额别名*/
    private BigDecimal           walletLastAmt;
    
    /**表名*/
    private String               tableName;
    
    // pendingApproval
    private String               pendingApproval;
    
    public Long getAccountId()
    {
        return this.accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public String getAccountAssetType()
    {
        return accountAssetType;
    }
    
    public void setAccountAssetType(String accountAssetType)
    {
        this.accountAssetType = accountAssetType;
    }
    
    public Long getAccountAssetId()
    {
        return accountAssetId;
    }
    
    public void setAccountAssetId(Long accountAssetId)
    {
        this.accountAssetId = accountAssetId;
    }
    
    public Long getStockinfoId()
    {
        return stockinfoId;
    }
    
    public void setStockinfoId(Long stockinfoId)
    {
        this.stockinfoId = stockinfoId;
    }
    
    public Long getRelatedStockinfoId()
    {
        return relatedStockinfoId;
    }
    
    public void setRelatedStockinfoId(Long relatedStockinfoId)
    {
        this.relatedStockinfoId = relatedStockinfoId;
    }
    
    public String getStockCode()
    {
        return stockCode;
    }
    
    public void setStockCode(String stockCode)
    {
        this.stockCode = stockCode;
    }
    
    public String getStockName()
    {
        return stockName;
    }
    
    public void setStockName(String stockName)
    {
        this.stockName = stockName;
    }
    
    public String getStockType()
    {
        return stockType;
    }
    
    public void setStockType(String stockType)
    {
        this.stockType = stockType;
    }
    
    public String getBusinessFlag()
    {
        return this.businessFlag;
    }
    
    public void setBusinessFlag(String businessFlag)
    {
        this.businessFlag = businessFlag;
    }
    
    public BigDecimal getContractAmt()
    {
        return this.contractAmt;
    }
    
    public void setContractAmt(BigDecimal contractAmt)
    {
        this.contractAmt = contractAmt;
    }
    
    public BigDecimal getOrgAmt()
    {
        return this.orgAmt;
    }
    
    public void setOrgAmt(BigDecimal orgAmt)
    {
        this.orgAmt = orgAmt;
    }
    
    public String getOccurDirect()
    {
        return this.occurDirect;
    }
    
    public void setOccurDirect(String occurDirect)
    {
        this.occurDirect = occurDirect;
    }
    
    public BigDecimal getOccurAmt()
    {
        return this.occurAmt;
    }
    
    public void setOccurAmt(BigDecimal occurAmt)
    {
        this.occurAmt = occurAmt;
    }
    
    public BigDecimal getLastAmt()
    {
        return this.lastAmt;
    }
    
    public void setLastAmt(BigDecimal lastAmt)
    {
        this.lastAmt = lastAmt;
    }
    
    public BigDecimal getForzenOrgAmt()
    {
        return forzenOrgAmt;
    }
    
    public void setForzenOrgAmt(BigDecimal forzenOrgAmt)
    {
        this.forzenOrgAmt = forzenOrgAmt;
    }
    
    public BigDecimal getForzenLastAmt()
    {
        return forzenLastAmt;
    }
    
    public void setForzenLastAmt(BigDecimal forzenLastAmt)
    {
        this.forzenLastAmt = forzenLastAmt;
    }
    
    public String getChargeAddr()
    {
        return this.chargeAddr;
    }
    
    public void setChargeAddr(String chargeAddr)
    {
        this.chargeAddr = chargeAddr;
    }
    
    public String getWithdrawAddr()
    {
        return this.withdrawAddr;
    }
    
    public void setWithdrawAddr(String withdrawAddr)
    {
        this.withdrawAddr = withdrawAddr;
    }
    
    public BigDecimal getFee()
    {
        return this.fee;
    }
    
    public void setFee(BigDecimal fee)
    {
        this.fee = fee;
    }
    
    public BigDecimal getNetFee()
    {
        return netFee;
    }
    
    public void setNetFee(BigDecimal netFee)
    {
        this.netFee = netFee;
    }
    
    public String getStatus()
    {
        return this.status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public String getApproveStatus()
    {
        return this.approveStatus;
    }
    
    public void setApproveStatus(String approveStatus)
    {
        this.approveStatus = approveStatus;
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
    
    public String getAccountName()
    {
        return accountName;
    }
    
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    
    public String getTransferStatus()
    {
        return transferStatus;
    }
    
    public void setTransferStatus(String transferStatus)
    {
        this.transferStatus = transferStatus;
    }
    
    public Long getOriginalBusinessId()
    {
        return originalBusinessId;
    }
    
    public void setOriginalBusinessId(Long originalBusinessId)
    {
        this.originalBusinessId = originalBusinessId;
    }
    
    public String getTransId()
    {
        return transId;
    }
    
    public void setTransId(String transId)
    {
        this.transId = transId;
    }
    
    public String getCollectStatus()
    {
        return collectStatus;
    }
    
    public void setCollectStatus(String collectStatus)
    {
        this.collectStatus = collectStatus;
    }
    
    public BigDecimal getWalletLastAmt()
    {
        return walletLastAmt;
    }
    
    public void setWalletLastAmt(BigDecimal walletLastAmt)
    {
        this.walletLastAmt = walletLastAmt;
    }
    
    public Long getAuditBy()
    {
        return auditBy;
    }
    
    public void setAuditBy(Long auditBy)
    {
        this.auditBy = auditBy;
    }
    
    public Long getCheckBy()
    {
        return checkBy;
    }
    
    public void setCheckBy(Long checkBy)
    {
        this.checkBy = checkBy;
    }
    
    public Timestamp getCurrentDate()
    {
        return currentDate;
    }
    
    public void setCurrentDate(Timestamp currentDate)
    {
        this.currentDate = currentDate;
    }
    
    public Timestamp getCreateDate()
    {
        return createDate;
    }
    
    public void setCreateDate(Timestamp createDate)
    {
        this.createDate = createDate;
    }
    
    public Timestamp getAuditDate()
    {
        return auditDate;
    }
    
    public void setAuditDate(Timestamp auditDate)
    {
        this.auditDate = auditDate;
    }
    
    public Timestamp getCheckDate()
    {
        return checkDate;
    }
    
    public void setCheckDate(Timestamp checkDate)
    {
        this.checkDate = checkDate;
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
    
    public BigDecimal getOccurForzenAmt()
    {
        return occurForzenAmt;
    }
    
    public void setOccurForzenAmt(BigDecimal occurForzenAmt)
    {
        this.occurForzenAmt = occurForzenAmt;
    }
    
    public String getCurrentDateStr()
    {
        return currentDateStr;
    }
    
    public void setCurrentDateStr(String currentDateStr)
    {
        this.currentDateStr = currentDateStr;
    }
    
    public String getRelatedStockName()
    {
        return relatedStockName;
    }
    
    public void setRelatedStockName(String relatedStockName)
    {
        this.relatedStockName = relatedStockName;
    }
    
    public String getTableName()
    {
        return tableName;
    }
    
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }
    
    public String getPendingApproval()
    {
        return pendingApproval;
    }
    
    public void setPendingApproval(String pendingApproval)
    {
        this.pendingApproval = pendingApproval;
    }
    
    public Long getUnid()
    {
        return unid;
    }
    
    public void setUnid(Long unid)
    {
        this.unid = unid;
    }
    
    public BigDecimal getRealTransferFee()
    {
        return realTransferFee;
    }
    
    public void setRealTransferFee(BigDecimal realTransferFee)
    {
        this.realTransferFee = realTransferFee;
    }
    
    public String getConfirmStatus()
    {
        return confirmStatus;
    }
    
    public void setConfirmStatus(String confirmStatus)
    {
        this.confirmStatus = confirmStatus;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("AccountWithdrawRecord{");
        sb.append("id=").append(id);
        sb.append(", currentDateStr='").append(currentDateStr).append('\'');
        sb.append(", accountId=").append(accountId);
        sb.append(", accountAssetType='").append(accountAssetType).append('\'');
        sb.append(", accountAssetId=").append(accountAssetId);
        sb.append(", currentDate=").append(currentDate);
        sb.append(", businessFlag='").append(businessFlag).append('\'');
        sb.append(", stockinfoId=").append(stockinfoId);
        sb.append(", stockCode='").append(stockCode).append('\'');
        sb.append(", contractAmt=").append(contractAmt);
        sb.append(", occurDirect='").append(occurDirect).append('\'');
        sb.append(", orgAmt=").append(orgAmt);
        sb.append(", occurAmt=").append(occurAmt);
        sb.append(", lastAmt=").append(lastAmt);
        sb.append(", forzenOrgAmt=").append(forzenOrgAmt);
        sb.append(", occurForzenAmt=").append(occurForzenAmt);
        sb.append(", forzenLastAmt=").append(forzenLastAmt);
        sb.append(", remark='").append(remark).append('\'');
        sb.append(", stockName='").append(stockName).append('\'');
        sb.append(", relatedStockName='").append(relatedStockName).append('\'');
        sb.append(", stockType='").append(stockType).append('\'');
        sb.append(", chargeAddr='").append(chargeAddr).append('\'');
        sb.append(", withdrawAddr='").append(withdrawAddr).append('\'');
        sb.append(", fee=").append(fee);
        sb.append(", netFee=").append(netFee);
        sb.append(", status='").append(status).append('\'');
        sb.append(", approveStatus='").append(approveStatus).append('\'');
        sb.append(", transferStatus='").append(transferStatus).append('\'');
        sb.append(", originalBusinessId=").append(originalBusinessId);
        sb.append(", relatedStockinfoId=").append(relatedStockinfoId);
        sb.append(", createBy=").append(createBy);
        sb.append(", createDate=").append(createDate);
        sb.append(", auditBy=").append(auditBy);
        sb.append(", auditDate=").append(auditDate);
        sb.append(", checkBy=").append(checkBy);
        sb.append(", checkDate=").append(checkDate);
        sb.append(", accountName='").append(accountName).append('\'');
        sb.append(", unid=").append(unid);
        sb.append(", transId='").append(transId).append('\'');
        sb.append(", collectStatus='").append(collectStatus).append('\'');
        sb.append(", timeStart='").append(timeStart).append('\'');
        sb.append(", timeEnd='").append(timeEnd).append('\'');
        sb.append(", walletLastAmt=").append(walletLastAmt);
        sb.append(", tableName='").append(tableName).append('\'');
        sb.append(", pendingApproval='").append(pendingApproval).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

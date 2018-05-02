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
 * ERC20账户提现记录表 实体对象
 * <p>File：AccountWithdrawRecordERC20.java</p>
 * <p>Title: AccountWithdrawRecordERC20</p>
 * <p>Description:AccountWithdrawRecordERC20</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "ERC20账户提现记录表")
public class AccountWithdrawRecordERC20 extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    /**账户ID*/
    @NotNull(message = "账户ID不可为空")
    @ApiModelProperty(value = "账户ID", required = true)
    private Long                 accountId;
    
    /**账户资产类型(钱包账户资产、合约账户资产)*/
    @NotNull(message = "账户资产类型(钱包账户资产、合约账户资产)不可为空")
    @ApiModelProperty(value = "账户资产类型(钱包账户资产、合约账户资产)", required = true)
    private String               accountAssetType;
    
    /**账户资产ID(钱包账户资产或合约账户资产对应的ID)*/
    @NotNull(message = "账户资产ID(钱包账户资产或合约账户资产对应的ID)不可为空")
    @ApiModelProperty(value = "账户资产ID(钱包账户资产或合约账户资产对应的ID)", required = true)
    private Long                 accountAssetId;
    
    /**流水时间戳*/
    @NotNull(message = "流水时间戳不可为空")
    @ApiModelProperty(value = "流水时间戳", required = true)
    private java.util.Date       currentDate;
    
    /**业务类别(钱包账户充值、钱包账户提现、钱包账户转合约账户、合约账户转钱包账户等)*/
    @NotNull(message = "业务类别(钱包账户充值、钱包账户提现、钱包账户转合约账户、合约账户转钱包账户等)不可为空")
    @ApiModelProperty(value = "业务类别(钱包账户充值、钱包账户提现、钱包账户转合约账户、合约账户转钱包账户等)", required = true)
    private String               businessFlag;
    
    /**证券信息id 对应Stockinfo表中的ID字段(具体token,包括eth)*/
    @NotNull(message = "证券信息id 对应Stockinfo表中的ID字段(具体token,包括eth)不可为空")
    @ApiModelProperty(value = "证券信息id 对应Stockinfo表中的ID字段(具体token,包括eth)", required = true)
    private Long                 stockinfoId;
    
    /**关联证券信息id 对应Stockinfo表中的ID字段(具体token,包括eth)*/
    @NotNull(message = "关联证券信息id 对应Stockinfo表中的ID字段(具体token,包括eth)不可为空")
    @ApiModelProperty(value = "关联证券信息id 对应Stockinfo表中的ID字段(具体token,包括eth)", required = true)
    private Long                 relatedStockinfoId;
    
    /**合约数量只限合约相关的,默认填0*/
    @NotNull(message = "合约数量只限合约相关的,默认填0不可为空")
    @ApiModelProperty(value = "合约数量只限合约相关的,默认填0", required = true)
    private java.math.BigDecimal contractAmt;
    
    /**资产发生方向(增加increase、减少decrease、冻结frozen、解冻unfrozen、解冻并减少frozenDecrease)*/
    @NotNull(message = "资产发生方向(增加increase、减少decrease、冻结frozen、解冻unfrozen、解冻并减少frozenDecrease)不可为空")
    @ApiModelProperty(value = "资产发生方向(增加increase、减少decrease、冻结frozen、解冻unfrozen、解冻并减少frozenDecrease)", required = true)
    private String               occurDirect;
    
    /**原资产当前数量余额*/
    @NotNull(message = "原资产当前数量余额不可为空")
    @ApiModelProperty(value = "原资产当前数量余额", required = true)
    private java.math.BigDecimal orgAmt;
    
    /**资产增加减少发生数量*/
    @NotNull(message = "资产增加减少发生数量不可为空")
    @ApiModelProperty(value = "资产增加减少发生数量", required = true)
    private java.math.BigDecimal occurAmt;
    
    /**最新资产当前数量余额*/
    @NotNull(message = "最新资产当前数量余额不可为空")
    @ApiModelProperty(value = "最新资产当前数量余额", required = true)
    private java.math.BigDecimal lastAmt;
    
    /**原冻结资产数量余额*/
    @NotNull(message = "原冻结资产数量余额不可为空")
    @ApiModelProperty(value = "原冻结资产数量余额", required = true)
    private java.math.BigDecimal forzenOrgAmt;
    
    /**冻结解冻发生数量*/
    @NotNull(message = "冻结解冻发生数量不可为空")
    @ApiModelProperty(value = "冻结解冻发生数量", required = true)
    private java.math.BigDecimal occurForzenAmt;
    
    /**最新冻结资产数量余额*/
    @NotNull(message = "最新冻结资产数量余额不可为空")
    @ApiModelProperty(value = "最新冻结资产数量余额", required = true)
    private java.math.BigDecimal forzenLastAmt;
    
    /**区块交易ID*/
    @ApiModelProperty(value = "区块交易ID")
    private String               transId;
    
    /**充币目标地址*/
    @ApiModelProperty(value = "充币目标地址")
    private String               chargeAddr;
    
    /**提币目标地址*/
    @ApiModelProperty(value = "提币目标地址")
    private String               withdrawAddr;
    
    /**费用*/
    @NotNull(message = "费用不可为空")
    @ApiModelProperty(value = "费用", required = true)
    private java.math.BigDecimal fee;
    
    /**区块转账费用*/
    @ApiModelProperty(value = "区块转账费用")
    private java.math.BigDecimal netFee;
    
    /**状态(有效effective、无效invalid)*/
    @NotNull(message = "状态(有效effective、无效invalid)不可为空")
    @ApiModelProperty(value = "状态(有效effective、无效invalid)", required = true)
    private String               status;
    
    /**审批状态(无需审批noApprove、待审核auditPending、审核拒绝auditReject、待复核checkPending、复核通过checkThrough、复核拒绝checkReject)*/
    @NotNull(message = "审批状态(无需审批noApprove、待审核auditPending、审核拒绝auditReject、待复核checkPending、复核通过checkThrough、复核拒绝checkReject)不可为空")
    @ApiModelProperty(value = "审批状态(无需审批noApprove、待审核auditPending、审核拒绝auditReject、待复核checkPending、复核通过checkThrough、复核拒绝checkReject)", required = true)
    private String               approveStatus;
    
    /**归集状态(noCollect无需归集、unCollect未归集、collected已归集)*/
    @NotNull(message = "归集状态(noCollect无需归集、unCollect未归集、collected已归集)不可为空")
    @ApiModelProperty(value = "归集状态(noCollect无需归集、unCollect未归集、collected已归集)", required = true)
    private String               collectStatus;
    
    /**划拨状态(noTransfer无需划拨、unTransfer未划拨、transferPending待划拨、Transfer已划拨)*/
    @NotNull(message = "划拨状态(noTransfer无需划拨、unTransfer未划拨、transferPending待划拨、Transfer已划拨)不可为空")
    @ApiModelProperty(value = "划拨状态(noTransfer无需划拨、unTransfer未划拨、transferPending待划拨、Transfer已划拨)", required = true)
    private String               transferStatus;

    /**区块确认状态(noConfirm无需确认、unconfirm未确认、confirm已确认)*/
    @NotNull(message = "区块确认状态(noConfirm无需确认、unconfirm未确认、confirm已确认)不可为空")
    @ApiModelProperty(value = "区块确认状态(noConfirm无需确认、unconfirm未确认、confirm已确认)", required = true)
    private String               confirmStatus;
    
    /**原始业务ID*/
    @NotNull(message = "原始业务ID不可为空")
    @ApiModelProperty(value = "原始业务ID", required = true)
    private Long                 originalBusinessId;
    
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String               remark;
    
    /**创建人*/
    @ApiModelProperty(value = "创建人")
    private Long                 createBy;
    
    /**创建时间*/
    @NotNull(message = "创建时间不可为空")
    @ApiModelProperty(value = "创建时间", required = true)
    private java.util.Date       createDate;
    
    /**审核人*/
    @ApiModelProperty(value = "审核人")
    private Long                 auditBy;
    
    /**审核时间*/
    @ApiModelProperty(value = "审核时间")
    private java.util.Date       auditDate;
    
    /**复核人*/
    @ApiModelProperty(value = "复核人")
    private Long                 checkBy;
    
    /**复核时间*/
    @ApiModelProperty(value = "复核时间")
    private java.util.Date       checkDate;
    
    /**帐号*/
    private String               accountName;
    
    /**证券代码*/
    private String               stockCode;
    
    private String               timeStart;
    
    private String               timeEnd;
    
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
        return this.accountAssetType;
    }
    
    public void setAccountAssetType(String accountAssetType)
    {
        this.accountAssetType = accountAssetType;
    }
    
    public Long getAccountAssetId()
    {
        return this.accountAssetId;
    }
    
    public void setAccountAssetId(Long accountAssetId)
    {
        this.accountAssetId = accountAssetId;
    }
    
    public java.util.Date getCurrentDate()
    {
        return this.currentDate;
    }
    
    public void setCurrentDate(java.util.Date currentDate)
    {
        this.currentDate = currentDate;
    }
    
    public String getBusinessFlag()
    {
        return this.businessFlag;
    }
    
    public void setBusinessFlag(String businessFlag)
    {
        this.businessFlag = businessFlag;
    }
    
    public Long getStockinfoId()
    {
        return this.stockinfoId;
    }
    
    public void setStockinfoId(Long stockinfoId)
    {
        this.stockinfoId = stockinfoId;
    }
    
    public Long getRelatedStockinfoId()
    {
        return this.relatedStockinfoId;
    }
    
    public void setRelatedStockinfoId(Long relatedStockinfoId)
    {
        this.relatedStockinfoId = relatedStockinfoId;
    }
    
    public java.math.BigDecimal getContractAmt()
    {
        return this.contractAmt;
    }
    
    public void setContractAmt(java.math.BigDecimal contractAmt)
    {
        this.contractAmt = contractAmt;
    }
    
    public String getOccurDirect()
    {
        return this.occurDirect;
    }
    
    public void setOccurDirect(String occurDirect)
    {
        this.occurDirect = occurDirect;
    }
    
    public java.math.BigDecimal getOrgAmt()
    {
        return this.orgAmt;
    }
    
    public void setOrgAmt(java.math.BigDecimal orgAmt)
    {
        this.orgAmt = orgAmt;
    }
    
    public java.math.BigDecimal getOccurAmt()
    {
        return this.occurAmt;
    }
    
    public void setOccurAmt(java.math.BigDecimal occurAmt)
    {
        this.occurAmt = occurAmt;
    }
    
    public java.math.BigDecimal getLastAmt()
    {
        return this.lastAmt;
    }
    
    public void setLastAmt(java.math.BigDecimal lastAmt)
    {
        this.lastAmt = lastAmt;
    }
    
    public java.math.BigDecimal getForzenOrgAmt()
    {
        return this.forzenOrgAmt;
    }
    
    public void setForzenOrgAmt(java.math.BigDecimal forzenOrgAmt)
    {
        this.forzenOrgAmt = forzenOrgAmt;
    }
    
    public java.math.BigDecimal getOccurForzenAmt()
    {
        return this.occurForzenAmt;
    }
    
    public void setOccurForzenAmt(java.math.BigDecimal occurForzenAmt)
    {
        this.occurForzenAmt = occurForzenAmt;
    }
    
    public java.math.BigDecimal getForzenLastAmt()
    {
        return this.forzenLastAmt;
    }
    
    public void setForzenLastAmt(java.math.BigDecimal forzenLastAmt)
    {
        this.forzenLastAmt = forzenLastAmt;
    }
    
    public String getTransId()
    {
        return this.transId;
    }
    
    public void setTransId(String transId)
    {
        this.transId = transId;
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
    
    public java.math.BigDecimal getFee()
    {
        return this.fee;
    }
    
    public void setFee(java.math.BigDecimal fee)
    {
        this.fee = fee;
    }
    
    public java.math.BigDecimal getNetFee()
    {
        return this.netFee;
    }
    
    public void setNetFee(java.math.BigDecimal netFee)
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
    
    public String getCollectStatus()
    {
        return this.collectStatus;
    }
    
    public void setCollectStatus(String collectStatus)
    {
        this.collectStatus = collectStatus;
    }
    
    public String getTransferStatus()
    {
        return this.transferStatus;
    }
    
    public void setTransferStatus(String transferStatus)
    {
        this.transferStatus = transferStatus;
    }

    public String getConfirmStatus()
    {
        return confirmStatus;
    }

    public void setConfirmStatus(String confirmStatus)
    {
        this.confirmStatus = confirmStatus;
    }
    
    public Long getOriginalBusinessId()
    {
        return this.originalBusinessId;
    }
    
    public void setOriginalBusinessId(Long originalBusinessId)
    {
        this.originalBusinessId = originalBusinessId;
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
    
    public Long getAuditBy()
    {
        return this.auditBy;
    }
    
    public void setAuditBy(Long auditBy)
    {
        this.auditBy = auditBy;
    }
    
    public java.util.Date getAuditDate()
    {
        return this.auditDate;
    }
    
    public void setAuditDate(java.util.Date auditDate)
    {
        this.auditDate = auditDate;
    }
    
    public Long getCheckBy()
    {
        return this.checkBy;
    }
    
    public void setCheckBy(Long checkBy)
    {
        this.checkBy = checkBy;
    }
    
    public java.util.Date getCheckDate()
    {
        return this.checkDate;
    }
    
    public void setCheckDate(java.util.Date checkDate)
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
    
    public String getAccountName()
    {
        return accountName;
    }
    
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    
    public String getStockCode()
    {
        return stockCode;
    }
    
    public void setStockCode(String stockCode)
    {
        this.stockCode = stockCode;
    }

}

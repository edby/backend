/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.entity;

import com.blocain.bitms.tools.annotation.ExcelField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.blocain.bitms.orm.core.GenericEntity;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 账户Cash提现记录表 实体对象
 * <p>File：AccountCashWithdraw.java</p>
 * <p>Title: AccountCashWithdraw</p>
 * <p>Description:AccountCashWithdraw</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "账户Cash提现记录表")
public class AccountCashWithdraw extends GenericEntity
{
    private static final long    serialVersionUID = 1L;
    
    @ExcelField(title = "---ID---", align = 3)
    private String               idStr;
    
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
    
    // 用户名称
    @ExcelField(title = "Account", align = 3)
    private String               accountName;
    
    @ExcelField(title = "Coin", align = 3)
    private String               stockCode;
    
    @ExcelField(title = "--Time--", align = 3)
    private String               dateStr;
    
    /**业务类别*/
    @NotNull(message = "业务类别不可为空")
    @ApiModelProperty(value = "业务类别", required = true)
    private String               businessFlag;
    
    /**证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "证券信息id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "证券信息id 对应Stockinfo表中的ID字段", required = true)
    private Long                 stockinfoId;
    
    /**关联证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "关联证券信息id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "关联证券信息id 对应Stockinfo表中的ID字段", required = true)
    private Long                 relatedStockinfoId;
    
    /**资产发生方向(增加increase、减少decrease、冻结frozen、解冻unfrozen、解冻并减少frozenDecrease)*/
    @NotNull(message = "资产发生方向(增加increase、减少decrease、冻结frozen、解冻unfrozen、解冻并减少frozenDecrease)不可为空")
    @ApiModelProperty(value = "资产发生方向(增加increase、减少decrease、冻结frozen、解冻unfrozen、解冻并减少frozenDecrease)", required = true)
    private String               occurDirect;
    
    /**资产增加减少发生数量*/
    @NotNull(message = "资产增加减少发生数量不可为空")
    @ApiModelProperty(value = "资产增加减少发生数量", required = true)
    @ExcelField(title = "Amount", align = 3)
    private java.math.BigDecimal occurAmt;

    /**手续费*/
    @NotNull(message = "手续费不可为空")
    @ExcelField(title = "Fee", align = 3)
    @ApiModelProperty(value = "手续费", required = true)
    private java.math.BigDecimal fee;

    /**提币目标银行名称*/
    @ApiModelProperty(value = "提币目标银行名称")
    private String               withdrawBankName;
    
    /**提币目标卡号*/
    @ExcelField(title = "-------------------IBAN---------------", align = 3)
    @ApiModelProperty(value = "提币目标卡号")
    private String               withdrawCardNo;
    
    /**swift/Bic*/
    @ExcelField(title = "SWIFT/BTC", align = 3)
    @NotNull(message = "swift/Bic不可为空")
    @ApiModelProperty(value = "swift/Bic", required = true)
    private String               swiftBic;
    
    /**交易唯一ID*/
    @ExcelField(title = "-----------TR.ID-----------", align = 3)
    @ApiModelProperty(value = "交易唯一ID")
    private String               transId;
    
    /**真实手续费*/
    @ApiModelProperty(value = "真实手续费", required = true)
    private java.math.BigDecimal realFee;
    
    /**审批状态(无需审批noApprove、待审核auditPending、审核拒绝auditReject、待复核checkPending、复核通过checkThrough、复核拒绝checkReject)*/
    @NotNull(message = "审批状态(无需审批noApprove、待审核auditPending、审核拒绝auditReject、待复核checkPending、复核通过checkThrough、复核拒绝checkReject)不可为空")
    @ApiModelProperty(value = "审批状态(无需审批noApprove、待审核auditPending、审核拒绝auditReject、待复核checkPending、复核通过checkThrough、复核拒绝checkReject)", required = true)
    private String               approveStatus;
    
    /**划拨状态(noTransfer无需划拨、unTransfer未划拨、transferPending待划拨、Transfer已划拨)*/
    @NotNull(message = "划拨状态(noTransfer无需划拨、unTransfer未划拨、transferPending待划拨、Transfer已划拨)不可为空")
    @ApiModelProperty(value = "划拨状态(noTransfer无需划拨、unTransfer未划拨、transferPending待划拨、Transfer已划拨)", required = true)
    private String               transferStatus;
    
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
    
    private String               timeStart;
    
    private String               timeEnd;
    
    public String getDateStr()
    {
        return dateStr;
    }
    
    public void setDateStr(String dateStr)
    {
        this.dateStr = dateStr;
    }
    
    public String getIdStr()
    {
        return idStr;
    }
    
    public void setIdStr(String idStr)
    {
        this.idStr = idStr;
    }
    
    public Long getAccountId()
    {
        return accountId;
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
    
    public Date getCurrentDate()
    {
        return currentDate;
    }
    
    public void setCurrentDate(Date currentDate)
    {
        this.currentDate = currentDate;
    }
    
    public String getBusinessFlag()
    {
        return businessFlag;
    }
    
    public void setBusinessFlag(String businessFlag)
    {
        this.businessFlag = businessFlag;
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
    
    public String getOccurDirect()
    {
        return occurDirect;
    }
    
    public void setOccurDirect(String occurDirect)
    {
        this.occurDirect = occurDirect;
    }
    
    public BigDecimal getOccurAmt()
    {
        return occurAmt;
    }
    
    public void setOccurAmt(BigDecimal occurAmt)
    {
        this.occurAmt = occurAmt;
    }
    
    public String getTransId()
    {
        return transId;
    }
    
    public void setTransId(String transId)
    {
        this.transId = transId;
    }
    
    public String getWithdrawBankName()
    {
        return withdrawBankName;
    }
    
    public void setWithdrawBankName(String withdrawBankName)
    {
        this.withdrawBankName = withdrawBankName;
    }
    
    public String getWithdrawCardNo()
    {
        return withdrawCardNo;
    }
    
    public void setWithdrawCardNo(String withdrawCardNo)
    {
        this.withdrawCardNo = withdrawCardNo;
    }
    
    public BigDecimal getFee()
    {
        return fee;
    }
    
    public void setFee(BigDecimal fee)
    {
        this.fee = fee;
    }
    
    public BigDecimal getRealFee()
    {
        return realFee;
    }
    
    public void setRealFee(BigDecimal realFee)
    {
        this.realFee = realFee;
    }
    
    public String getApproveStatus()
    {
        return approveStatus;
    }
    
    public void setApproveStatus(String approveStatus)
    {
        this.approveStatus = approveStatus;
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
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public Long getCreateBy()
    {
        return createBy;
    }
    
    public void setCreateBy(Long createBy)
    {
        this.createBy = createBy;
    }
    
    public Date getCreateDate()
    {
        return createDate;
    }
    
    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate;
    }
    
    public Long getAuditBy()
    {
        return auditBy;
    }
    
    public void setAuditBy(Long auditBy)
    {
        this.auditBy = auditBy;
    }
    
    public Date getAuditDate()
    {
        return auditDate;
    }
    
    public void setAuditDate(Date auditDate)
    {
        this.auditDate = auditDate;
    }
    
    public Long getCheckBy()
    {
        return checkBy;
    }
    
    public void setCheckBy(Long checkBy)
    {
        this.checkBy = checkBy;
    }
    
    public Date getCheckDate()
    {
        return checkDate;
    }
    
    public void setCheckDate(Date checkDate)
    {
        this.checkDate = checkDate;
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
    
    public String getSwiftBic()
    {
        return swiftBic;
    }
    
    public void setSwiftBic(String swiftBic)
    {
        this.swiftBic = swiftBic;
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
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("AccountCashWithdraw{");
        sb.append("id=").append(id);
        sb.append(", accountId=").append(accountId);
        sb.append(", accountAssetType='").append(accountAssetType).append('\'');
        sb.append(", accountAssetId=").append(accountAssetId);
        sb.append(", currentDate=").append(currentDate);
        sb.append(", businessFlag='").append(businessFlag).append('\'');
        sb.append(", stockinfoId=").append(stockinfoId);
        sb.append(", relatedStockinfoId=").append(relatedStockinfoId);
        sb.append(", occurDirect='").append(occurDirect).append('\'');
        sb.append(", occurAmt=").append(occurAmt);
        sb.append(", transId='").append(transId).append('\'');
        sb.append(", withdrawBankName='").append(withdrawBankName).append('\'');
        sb.append(", withdrawCardNo='").append(withdrawCardNo).append('\'');
        sb.append(", fee=").append(fee);
        sb.append(", realFee=").append(realFee);
        sb.append(", approveStatus='").append(approveStatus).append('\'');
        sb.append(", transferStatus='").append(transferStatus).append('\'');
        sb.append(", originalBusinessId=").append(originalBusinessId);
        sb.append(", remark='").append(remark).append('\'');
        sb.append(", createBy=").append(createBy);
        sb.append(", createDate=").append(createDate);
        sb.append(", auditBy=").append(auditBy);
        sb.append(", auditDate=").append(auditDate);
        sb.append(", checkBy=").append(checkBy);
        sb.append(", checkDate=").append(checkDate);
        sb.append(", accountName='").append(accountName).append('\'');
        sb.append(", stockCode='").append(stockCode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

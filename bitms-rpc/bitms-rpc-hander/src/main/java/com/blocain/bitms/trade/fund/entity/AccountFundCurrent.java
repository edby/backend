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
 * 账户流水表 实体对象
 * <p>File：AccountCurrent.java</p>
 * <p>Title: AccountCurrent</p>
 * <p>Description:AccountCurrent</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class AccountFundCurrent extends GenericEntity
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
    private java.sql.Timestamp   currentDate;
    
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
    private java.math.BigDecimal contractAmt;
    
    /**资产发生方向(增加、减少)*/
    @NotNull(message = "资产发生方向不可为空")
    @ExcelField(title = "资产发生方向")
    private String               occurDirect;
    
    /**原资产数量余额*/
    @NotNull(message = "原资产数量余额不可为空")
    @ExcelField(title = "原资产数量余额")
    private java.math.BigDecimal orgAmt;
    
    /**资产发生数量*/
    @NotNull(message = "资产发生数量不可为空")
    @ExcelField(title = "发生数量")
    private java.math.BigDecimal occurAmt;
    
    /**最新资产数量余额*/
    @NotNull(message = "最新资产数量余额不可为空")
    @ExcelField(title = "最新余额")
    private java.math.BigDecimal lastAmt;
    
    /**原冻结数量余额*/
    @NotNull(message = "原冻结数量余额不可为空")
    private java.math.BigDecimal forzenOrgAmt;
    
    /**冻结解冻发生数量*/
    @NotNull(message = "冻结解冻发生数量不可为空")
    private java.math.BigDecimal occurForzenAmt;
    
    /**最新冻结数量余额*/
    @NotNull(message = "最新冻结数量余额不可为空")
    private java.math.BigDecimal forzenLastAmt;
    
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
    private java.math.BigDecimal fee;
    
    /**充值提现网络转账费用*/
    private java.math.BigDecimal netFee;
    
    /**状态(有效、无效)*/
    @NotNull(message = "状态(有效、无效)不可为空")
    private String               status;
    
    /**原始业务ID*/
    @NotNull(message = "原始业务ID")
    private Long                 originalBusinessId;
    
    /**关联证券信息id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "关联证券信息id 对应Stockinfo表中的ID字段")
    private Long                 relatedStockinfoId;
    
    /**帐号*/
    private String               accountName;
    
    /**
     * 用户编号
     */
    private Long                 unid;
    
    /**交易ID*/
    private String               transId;
    
    /**用户界面传值 查询开始时间 */
    private String               timeStart;
    
    /**用户界面传值 查询结束时间 */
    private String               timeEnd;
    
    /**钱包余额别名*/
    private java.math.BigDecimal walletLastAmt;
    
    /**表名*/
    private String               tableName;
    
    // pendingApproval
    private String               pendingApproval;

    private String approveStatus;

    private String transferStatus;
    
    public String getCurrentDateStr()
    {
        return currentDateStr;
    }
    
    public void setCurrentDateStr(String currentDateStr)
    {
        this.currentDateStr = currentDateStr;
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
    
    public Timestamp getCurrentDate()
    {
        return currentDate;
    }
    
    public void setCurrentDate(Timestamp currentDate)
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
    
    public String getStockCode()
    {
        return stockCode;
    }
    
    public void setStockCode(String stockCode)
    {
        this.stockCode = stockCode;
    }
    
    public BigDecimal getContractAmt()
    {
        return contractAmt;
    }
    
    public void setContractAmt(BigDecimal contractAmt)
    {
        this.contractAmt = contractAmt;
    }
    
    public String getOccurDirect()
    {
        return occurDirect;
    }
    
    public void setOccurDirect(String occurDirect)
    {
        this.occurDirect = occurDirect;
    }
    
    public BigDecimal getOrgAmt()
    {
        return orgAmt;
    }
    
    public void setOrgAmt(BigDecimal orgAmt)
    {
        this.orgAmt = orgAmt;
    }
    
    public BigDecimal getOccurAmt()
    {
        return occurAmt;
    }
    
    public void setOccurAmt(BigDecimal occurAmt)
    {
        this.occurAmt = occurAmt;
    }
    
    public BigDecimal getLastAmt()
    {
        return lastAmt;
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
    
    public BigDecimal getOccurForzenAmt()
    {
        return occurForzenAmt;
    }
    
    public void setOccurForzenAmt(BigDecimal occurForzenAmt)
    {
        this.occurForzenAmt = occurForzenAmt;
    }
    
    public BigDecimal getForzenLastAmt()
    {
        return forzenLastAmt;
    }
    
    public void setForzenLastAmt(BigDecimal forzenLastAmt)
    {
        this.forzenLastAmt = forzenLastAmt;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public String getStockName()
    {
        return stockName;
    }
    
    public void setStockName(String stockName)
    {
        this.stockName = stockName;
    }
    
    public String getRelatedStockName()
    {
        return relatedStockName;
    }
    
    public void setRelatedStockName(String relatedStockName)
    {
        this.relatedStockName = relatedStockName;
    }
    
    public String getStockType()
    {
        return stockType;
    }
    
    public void setStockType(String stockType)
    {
        this.stockType = stockType;
    }
    
    public String getChargeAddr()
    {
        return chargeAddr;
    }
    
    public void setChargeAddr(String chargeAddr)
    {
        this.chargeAddr = chargeAddr;
    }
    
    public String getWithdrawAddr()
    {
        return withdrawAddr;
    }
    
    public void setWithdrawAddr(String withdrawAddr)
    {
        this.withdrawAddr = withdrawAddr;
    }
    
    public BigDecimal getFee()
    {
        return fee;
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
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public Long getOriginalBusinessId()
    {
        return originalBusinessId;
    }
    
    public void setOriginalBusinessId(Long originalBusinessId)
    {
        this.originalBusinessId = originalBusinessId;
    }
    
    public Long getRelatedStockinfoId()
    {
        return relatedStockinfoId;
    }
    
    public void setRelatedStockinfoId(Long relatedStockinfoId)
    {
        this.relatedStockinfoId = relatedStockinfoId;
    }
    
    public String getAccountName()
    {
        return accountName;
    }
    
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    
    public Long getUnid()
    {
        return unid;
    }
    
    public void setUnid(Long unid)
    {
        this.unid = unid;
    }
    
    public String getTransId()
    {
        return transId;
    }
    
    public void setTransId(String transId)
    {
        this.transId = transId;
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
    
    public BigDecimal getWalletLastAmt()
    {
        return walletLastAmt;
    }
    
    public void setWalletLastAmt(BigDecimal walletLastAmt)
    {
        this.walletLastAmt = walletLastAmt;
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

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("AccountFundCurrent{");
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
        sb.append(", originalBusinessId=").append(originalBusinessId);
        sb.append(", relatedStockinfoId=").append(relatedStockinfoId);
        sb.append(", accountName='").append(accountName).append('\'');
        sb.append(", unid=").append(unid);
        sb.append(", transId='").append(transId).append('\'');
        sb.append(", timeStart='").append(timeStart).append('\'');
        sb.append(", timeEnd='").append(timeEnd).append('\'');
        sb.append(", walletLastAmt=").append(walletLastAmt);
        sb.append(", tableName='").append(tableName).append('\'');
        sb.append(", pendingApproval='").append(pendingApproval).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

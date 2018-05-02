/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.model;

import com.blocain.bitms.orm.core.GenericEntity;

import java.math.BigDecimal;

/**
 * 用户账户资产实体
 * <p>File：StockInfoModel.java</p>
 * <p>Title: StockInfoModel</p>
 * <p>Description:StockInfoModel</p>
 * <p>Copyright: Copyright (c) 2017年7月19日</p>
 * <p>Company: BloCain</p>
 *
 * @version 1.0
 */
public class AccountAssetModel extends GenericEntity {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 数字货币id
     */
    private Long digitalId;

    /**
     * 数字货币证券ID
     */
    private Long digitalStockInfoId;

    /**
     * 数字货币证券代码
     */
    private String digitalStockCode;

    /**
     * 数字货币余额
     */
    private BigDecimal digitalAmount;

    /**
     * 数字货币冻结余额
     */
    private BigDecimal digitalFrozenamt;

    /**
     * 法定货币id
     */
    private Long legalId;

    /**
     * 数字货币证券ID
     */
    private Long legalStockInfoId;

    /**
     * 法定货币证券代码
     */
    private String legalStockCode;

    /**
     * 法定货币余额
     */
    private BigDecimal legalAmount;

    /**
     * 法定货币冻结余额
     */
    private BigDecimal legalFrozenamt;

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 单表证券ID
     */
    private Long stockInfoId;

    private Long relatedStockinfoId;

    /**
     * 单表余额
     */
    private BigDecimal amount;

    /**
     * 单表冻结余额
     */
    private BigDecimal frozenamt;

    /**
     * 单表借款
     */
    private BigDecimal debitAmt;

    /**
     * 单表累计利息
     */
    private BigDecimal accumulateInterest;

    private String tableAsset;

    private String tableDebitAsset;

    private String stockType;

    private String stockName;

    private String canWealth;

    private String canConversion;

    public Long getDigitalId() {
        return digitalId;
    }

    public void setDigitalId(Long digitalId) {
        this.digitalId = digitalId;
    }

    public Long getDigitalStockInfoId() {
        return digitalStockInfoId;
    }

    public void setDigitalStockInfoId(Long digitalStockInfoId) {
        this.digitalStockInfoId = digitalStockInfoId;
    }

    public BigDecimal getDigitalAmount() {
        return digitalAmount;
    }

    public void setDigitalAmount(BigDecimal digitalAmount) {
        this.digitalAmount = digitalAmount;
    }

    public BigDecimal getDigitalFrozenamt() {
        return digitalFrozenamt;
    }

    public void setDigitalFrozenamt(BigDecimal digitalFrozenamt) {
        this.digitalFrozenamt = digitalFrozenamt;
    }

    public Long getLegalId() {
        return legalId;
    }

    public void setLegalId(Long legalId) {
        this.legalId = legalId;
    }

    public Long getLegalStockInfoId() {
        return legalStockInfoId;
    }

    public void setLegalStockInfoId(Long legalStockInfoId) {
        this.legalStockInfoId = legalStockInfoId;
    }

    public BigDecimal getLegalAmount() {
        return legalAmount;
    }

    public void setLegalAmount(BigDecimal legalAmount) {
        this.legalAmount = legalAmount;
    }

    public BigDecimal getLegalFrozenamt() {
        return legalFrozenamt;
    }

    public void setLegalFrozenamt(BigDecimal legalFrozenamt) {
        this.legalFrozenamt = legalFrozenamt;
    }

    public String getDigitalStockCode() {
        return digitalStockCode;
    }

    public void setDigitalStockCode(String digitalStockCode) {
        this.digitalStockCode = digitalStockCode;
    }

    public String getLegalStockCode() {
        return legalStockCode;
    }

    public void setLegalStockCode(String legalStockCode) {
        this.legalStockCode = legalStockCode;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getStockInfoId() {
        return stockInfoId;
    }

    public void setStockInfoId(Long stockInfoId) {
        this.stockInfoId = stockInfoId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFrozenamt() {
        return frozenamt;
    }

    public void setFrozenamt(BigDecimal frozenamt) {
        this.frozenamt = frozenamt;
    }

    public BigDecimal getDebitAmt() {
        return debitAmt;
    }

    public void setDebitAmt(BigDecimal debitAmt) {
        this.debitAmt = debitAmt;
    }

    public BigDecimal getAccumulateInterest() {
        return accumulateInterest;
    }

    public void setAccumulateInterest(BigDecimal accumulateInterest) {
        this.accumulateInterest = accumulateInterest;
    }

    public Long getRelatedStockinfoId() {
        return relatedStockinfoId;
    }

    public void setRelatedStockinfoId(Long relatedStockinfoId) {
        this.relatedStockinfoId = relatedStockinfoId;
    }

    public String getTableAsset() {
        return tableAsset;
    }

    public void setTableAsset(String tableAsset) {
        this.tableAsset = tableAsset;
    }

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    public String getTableDebitAsset() {
        return tableDebitAsset;
    }

    public void setTableDebitAsset(String tableDebitAsset) {
        this.tableDebitAsset = tableDebitAsset;
    }

    public String getCanWealth() {
        return canWealth;
    }

    public void setCanWealth(String canWealth) {
        this.canWealth = canWealth;
    }

    public String getCanConversion() {
        return canConversion;
    }

    public void setCanConversion(String canConversion) {
        this.canConversion = canConversion;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AccountAssetModel{");
        sb.append("digitalId=").append(digitalId);
        sb.append(", digitalStockInfoId=").append(digitalStockInfoId);
        sb.append(", digitalStockCode='").append(digitalStockCode).append('\'');
        sb.append(", digitalAmount=").append(digitalAmount);
        sb.append(", digitalFrozenamt=").append(digitalFrozenamt);
        sb.append(", legalId=").append(legalId);
        sb.append(", legalStockInfoId=").append(legalStockInfoId);
        sb.append(", legalStockCode='").append(legalStockCode).append('\'');
        sb.append(", legalAmount=").append(legalAmount);
        sb.append(", legalFrozenamt=").append(legalFrozenamt);
        sb.append(", accountId=").append(accountId);
        sb.append(", stockInfoId=").append(stockInfoId);
        sb.append(", relatedStockinfoId=").append(relatedStockinfoId);
        sb.append(", amount=").append(amount);
        sb.append(", frozenamt=").append(frozenamt);
        sb.append(", debitAmt=").append(debitAmt);
        sb.append(", accumulateInterest=").append(accumulateInterest);
        sb.append(", tableAsset='").append(tableAsset).append('\'');
        sb.append(", tableDebitAsset='").append(tableDebitAsset).append('\'');
        sb.append(", stockType='").append(stockType).append('\'');
        sb.append(", canWealth='").append(canWealth).append('\'');
        sb.append(", canConversion='").append(canConversion).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

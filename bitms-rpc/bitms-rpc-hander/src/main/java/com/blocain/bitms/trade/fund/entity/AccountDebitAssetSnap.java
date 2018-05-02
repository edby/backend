/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.blocain.bitms.orm.core.GenericEntity ;
import javax.validation.constraints.NotNull;
/**
 * 账户借贷资产快照表 实体对象
 * <p>File：AccountDebitAssetSnap.java</p>
 * <p>Title: AccountDebitAssetSnap</p>
 * <p>Description:AccountDebitAssetSnap</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "账户借贷资产快照表")
public class AccountDebitAssetSnap extends GenericEntity {
	
	private static final long serialVersionUID = 1L;
	
	/**借款人账户ID*/
	@NotNull(message = "借款人账户ID不可为空")
	@ApiModelProperty(value = "借款人账户ID", required = true)
	private Long borrowerAccountId;
	/**贷款人账户ID*/
	@NotNull(message = "贷款人账户ID不可为空")
	@ApiModelProperty(value = "贷款人账户ID", required = true)
	private Long lenderAccountId;
	/**借贷证券信息id 对应Stockinfo表中的ID字段*/
	@NotNull(message = "借贷证券信息id 对应Stockinfo表中的ID字段不可为空")
	@ApiModelProperty(value = "借贷证券信息id 对应Stockinfo表中的ID字段", required = true)
	private Long stockinfoId;
	/**借贷证券信息关联id 对应Stockinfo表中的ID字段*/
	@NotNull(message = "借贷证券信息关联id 对应Stockinfo表中的ID字段不可为空")
	@ApiModelProperty(value = "借贷证券信息关联id 对应Stockinfo表中的ID字段", required = true)
	private Long relatedStockinfoId;
	/**借贷数量或金额*/
	@NotNull(message = "借贷数量或金额不可为空")
	@ApiModelProperty(value = "借贷数量或金额", required = true)
	private java.math.BigDecimal debitAmt;
	/**累计利息*/
	@NotNull(message = "累计利息不可为空")
	@ApiModelProperty(value = "累计利息", required = true)
	private java.math.BigDecimal accumulateInterest;
	/**最后计息日*/
	@NotNull(message = "最后计息日不可为空")
	@ApiModelProperty(value = "最后计息日", required = true)
	private Integer lastInterestDay;
	/**最新平台行情价格*/
	@ApiModelProperty(value = "最新平台行情价格")
	private java.math.BigDecimal lastPrice;
	/**备注*/
	@ApiModelProperty(value = "备注")
	private String remark;
	/**修改时间*/
	@NotNull(message = "修改时间不可为空")
	@ApiModelProperty(value = "修改时间", required = true)
	private java.util.Date updateDate;
	
	public Long getBorrowerAccountId()
	{
		return this.borrowerAccountId;
	}
	
	public void setBorrowerAccountId(Long borrowerAccountId)
	{
		this.borrowerAccountId = borrowerAccountId;
	}
	
	public Long getLenderAccountId()
	{
		return this.lenderAccountId;
	}
	
	public void setLenderAccountId(Long lenderAccountId)
	{
		this.lenderAccountId = lenderAccountId;
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
	
	public java.math.BigDecimal getDebitAmt()
	{
		return this.debitAmt;
	}
	
	public void setDebitAmt(java.math.BigDecimal debitAmt)
	{
		this.debitAmt = debitAmt;
	}
	
	public java.math.BigDecimal getAccumulateInterest()
	{
		return this.accumulateInterest;
	}
	
	public void setAccumulateInterest(java.math.BigDecimal accumulateInterest)
	{
		this.accumulateInterest = accumulateInterest;
	}
	
	public Integer getLastInterestDay()
	{
		return this.lastInterestDay;
	}
	
	public void setLastInterestDay(Integer lastInterestDay)
	{
		this.lastInterestDay = lastInterestDay;
	}
	
	public java.math.BigDecimal getLastPrice()
	{
		return this.lastPrice;
	}
	
	public void setLastPrice(java.math.BigDecimal lastPrice)
	{
		this.lastPrice = lastPrice;
	}
	
	public String getRemark()
	{
		return this.remark;
	}
	
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	
	public java.util.Date getUpdateDate()
	{
		return this.updateDate;
	}
	
	public void setUpdateDate(java.util.Date updateDate)
	{
		this.updateDate = updateDate;
	}
	
}


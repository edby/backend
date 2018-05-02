package com.blocain.bitms.bitpay.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.blocain.bitms.tools.annotation.ExcelField;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>File：WithdrawRecord.java</p>
 * <p>Title: </p>
 * <p>Description:bitpay</p>
 * <p>Copyright: Copyright (c) 2017-07-18 11:05</p>
 * <p>Company: jmwenhua.cn</p>
 * @author 施建波
 * @version 1.0
 */
public class WithdrawRecord extends BaseEntity { 
	
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
	
	@TableId(type = IdType.INPUT)
	@ExcelField(title = "提现记录ID")
	private Long id;
	/**帐户ID*/
	private String accountId;
	/**提现地址*/
	private String withdrawAddr;
	/**提现金额*/
	private BigDecimal occurAmt;
	/**手续费*/
	private BigDecimal netFee;
	/**证券信息ID*/
	private String stockinfoId;
	/**交易ID*/
	@ExcelField(title = "交易ID")
	private String transId;
	/**状态：0、未提现 1、已提现 2、提现失败*/
	private Integer state;
	/**备注*/
	private String remark;
	/**提现时间*/
	private Long createDate;
	
	
	
	// ID集合
	@TableField(exist = false)
	private List<String> idList;
	
	/**
	 *方法: 取得String
	 *@return: String  提现记录ID
	 */
	public Long getId(){
		return this.id;
	}

	/**
	 *方法: 设置String
	 *@param: String  提现记录ID
	 */
	public void setId(Long id){
		this.id = id;
	}
	
	/**
	 *方法: 取得String
	 *@return: String  帐户ID
	 */
	public String getAccountId(){
		return this.accountId;
	}

	/**
	 *方法: 设置String
	 *@param: String  帐户ID
	 */
	public void setAccountId(String accountId){
		this.accountId = accountId;
	}
	
	/**
	 *方法: 取得String
	 *@return: String  提现地址
	 */
	public String getRaiseAddr(){
		return this.withdrawAddr;
	}

	/**
	 *方法: 设置String
	 *@param: String  提现地址
	 */
	public void setRaiseAddr(String withdrawAddr){
		this.withdrawAddr = withdrawAddr;
	}
	
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  提现金额
	 */
	public BigDecimal getOccurAmt(){
		return this.occurAmt;
	}

	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  提现金额
	 */
	public void setOccurAmt(BigDecimal occurAmt){
		this.occurAmt = occurAmt;
	}
	
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  手续费
	 */
	public BigDecimal getNetFee(){
		return this.netFee;
	}

	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  手续费
	 */
	public void setNetFee(BigDecimal netFee){
		this.netFee = netFee;
	}
	
	/**
	 *方法: 取得String
	 *@return: String  证券信息ID
	 */
	public String getStockinfoId(){
		return this.stockinfoId;
	}

	/**
	 *方法: 设置String
	 *@param: String  证券信息ID
	 */
	public void setStockinfoId(String stockinfoId){
		this.stockinfoId = stockinfoId;
	}
	
	public String getTransId()
    {
        return transId;
    }

    public void setTransId(String transId)
    {
        this.transId = transId;
    }

    /**
	 *方法: 取得Integer
	 *@return: Integer  状态：0、未提现 1、已提现 2、提现失败
	 */
	public Integer getState(){
		return this.state;
	}

	/**
	 *方法: 设置Integer
	 *@param: Integer  状态：0、未提现 1、已提现 2、提现失败
	 */
	public void setState(Integer state){
		this.state = state;
	}
	
	/**
	 *方法: 取得String
	 *@return: String  备注
	 */
	public String getRemark(){
		return this.remark;
	}

	/**
	 *方法: 设置String
	 *@param: String  备注
	 */
	public void setRemark(String remark){
		this.remark = remark;
	}
	
	/**
	 *方法: 取得Date
	 *@return: Date  提现时间
	 */
	public Long getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置Date
	 *@param: Date  提现时间
	 */
	public void setCreateDate(Long createDate){
		this.createDate = createDate;
	}

    public List<String> getIdList()
    {
        return idList;
    }

    public void setIdList(List<String> idList)
    {
        this.idList = idList;
    }
	
}

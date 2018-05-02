/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.block.entity;

import javax.validation.constraints.NotNull;

import com.blocain.bitms.orm.core.GenericEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * BlockInfoERC20 实体对象
 * <p>File：BlockInfoERC20.java</p>
 * <p>Title: BlockInfoERC20</p>
 * <p>Description:BlockInfoERC20</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "BlockInfoERC20")
public class BlockInfoERC20 extends GenericEntity {

	private static final long serialVersionUID = 1L;

	/**hash*/
	@NotNull(message = "hash不可为空")
	@ApiModelProperty(value = "hash", required = true)
	private String hash;

	/**parentHash*/
	@NotNull(message = "parentHash不可为空")
	@ApiModelProperty(value = "parentHash", required = true)
	private String parentHash;

	/**height*/
	@NotNull(message = "height不可为空")
	@ApiModelProperty(value = "height", required = true)
	private Long height;

	/**blockTimeStamp*/
	@NotNull(message = "blockTimeStamp不可为空")
	@ApiModelProperty(value = "blockTimeStamp", required = true)
	private java.util.Date blockTimeStamp;

	/**transScanStatus*/
	@NotNull(message = "transScanStatus不可为空")
	@ApiModelProperty(value = "transScanStatus", required = true)
	private String transScanStatus;

	/**erc20TokenScanNumber*/
	@NotNull(message = "erc20TokenScanNumber不可为空")
	@ApiModelProperty(value = "erc20TokenScanNumber", required = true)
	private Long erc20TokenScanNumber;

	/**remark*/
	@ApiModelProperty(value = "remark")
	private String remark;

	public String getHash()
	{
		return this.hash;
	}

	public void setHash(String hash)
	{
		this.hash = hash;
	}

	public String getParentHash()
	{
		return this.parentHash;
	}

	public void setParentHash(String parentHash)
	{
		this.parentHash = parentHash;
	}

	public Long getHeight()
	{
		return this.height;
	}

	public void setHeight(Long height)
	{
		this.height = height;
	}

	public java.util.Date getBlockTimeStamp()
	{
		return this.blockTimeStamp;
	}

	public void setBlockTimeStamp(java.util.Date blockTimeStamp)
	{
		this.blockTimeStamp = blockTimeStamp;
	}

	public String getTransScanStatus()
	{
		return this.transScanStatus;
	}

	public void setTransScanStatus(String transScanStatus)
	{
		this.transScanStatus = transScanStatus;
	}

	public Long getErc20TokenScanNumber() {
		return erc20TokenScanNumber;
	}

	public void setErc20TokenScanNumber(Long erc20TokenScanNumber) {
		this.erc20TokenScanNumber = erc20TokenScanNumber;
	}

	public String getRemark()
	{
		return this.remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "BlockInfoERC20{" +
				"hash='" + hash + '\'' +
				", parentHash='" + parentHash + '\'' +
				", height=" + height +
				", blockTimeStamp=" + blockTimeStamp +
				", transScanStatus='" + transScanStatus + '\'' +
				", erc20TokenScanNumber=" + erc20TokenScanNumber +
				", remark='" + remark + '\'' +
				'}';
	}
}


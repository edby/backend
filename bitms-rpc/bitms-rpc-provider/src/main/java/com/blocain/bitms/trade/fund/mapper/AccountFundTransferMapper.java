/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.core.GenericMapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.trade.fund.entity.AccountFundTransfer;

/**
 * 账户资金归集表 持久层接口
 * <p>File：AccountFundTransferDao.java </p>
 * <p>Title: AccountFundTransferDao </p>
 * <p>Description:AccountFundTransferDao </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface AccountFundTransferMapper extends GenericMapper<AccountFundTransfer>
{
	 List<AccountFundTransfer> findTransferAddrList(@Param("map") Map<String, Object> addrMap);
	 
	 AccountFundTransfer findTransferAddr(AccountFundTransfer accountFundTransfer);

	List<AccountFundTransfer> findByIds(@Param("ids") Long[] ids);

	List<AccountFundTransfer> findNeedUpdatePendingApprovals();

	List<AccountFundTransfer> findNeedUpdateTransaction();

	AccountFundTransfer selectByOriginalCurrentId(@Param("id") Long id);
}

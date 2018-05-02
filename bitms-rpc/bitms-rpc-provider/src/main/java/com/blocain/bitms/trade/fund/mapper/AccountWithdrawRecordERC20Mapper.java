/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao ;
import com.blocain.bitms.trade.fund.entity.AccountWithdrawRecordERC20;

import java.math.BigDecimal;
import java.util.List;

/**
 * ERC20账户提现记录表 持久层接口
 * <p>File：AccountWithdrawRecordERC20Mapper.java </p>
 * <p>Title: AccountWithdrawRecordERC20Mapper </p>
 * <p>Description:AccountWithdrawRecordERC20Mapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface AccountWithdrawRecordERC20Mapper extends GenericMapper<AccountWithdrawRecordERC20>
{
    AccountWithdrawRecordERC20 selectByIdForUpdate(Long id);

    List<AccountWithdrawRecordERC20> getListRecordUnCoinfirm();

    BigDecimal findSumAmtByAccount(AccountWithdrawRecordERC20 accountWithdrawRecordERC20);

    /**
     * 查询用户已用提现次数（所有 token）
     * @param accountWithdrawRecordERC20
     * @return
     */
    Integer findCuntByAccount(AccountWithdrawRecordERC20 accountWithdrawRecordERC20);
}

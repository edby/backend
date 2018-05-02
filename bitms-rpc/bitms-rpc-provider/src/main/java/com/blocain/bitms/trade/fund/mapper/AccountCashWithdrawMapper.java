/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao ;
import com.blocain.bitms.trade.fund.entity.AccountCashWithdraw;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户Cash提现记录表 持久层接口
 * <p>File：AccountCashWithdrawMapper.java </p>
 * <p>Title: AccountCashWithdrawMapper </p>
 * <p>Description:AccountCashWithdrawMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface AccountCashWithdrawMapper extends GenericMapper<AccountCashWithdraw>
{
    AccountCashWithdraw selectByIdForUpdate(Long id);

    List<AccountCashWithdraw> listForExcel();

    BigDecimal findSumAmtByAccount(AccountCashWithdraw accountCashWithdraw);
}

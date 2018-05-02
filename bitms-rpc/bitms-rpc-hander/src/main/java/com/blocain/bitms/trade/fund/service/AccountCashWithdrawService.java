/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.trade.fund.entity.AccountCashWithdraw;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户Cash提现记录表 服务接口
 * <p>File：AccountCashWithdrawService.java </p>
 * <p>Title: AccountCashWithdrawService </p>
 * <p>Description:AccountCashWithdrawService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface AccountCashWithdrawService extends GenericService<AccountCashWithdraw>{

    AccountCashWithdraw selectByIdForUpdate(Long id);

    List<AccountCashWithdraw> listForExcel();

    BigDecimal findSumAmtByAccount(AccountCashWithdraw accountCashWithdraw);
}

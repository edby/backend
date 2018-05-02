/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;


import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.fund.entity.AccountCashWithdraw;
import com.blocain.bitms.trade.fund.mapper.AccountCashWithdrawMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户Cash提现记录表 服务实现类
 * <p>File：AccountCashWithdrawServiceImpl.java </p>
 * <p>Title: AccountCashWithdrawServiceImpl </p>
 * <p>Description:AccountCashWithdrawServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountCashWithdrawServiceImpl extends GenericServiceImpl<AccountCashWithdraw> implements AccountCashWithdrawService
{

    protected AccountCashWithdrawMapper accountCashWithdrawMapper;

    @Autowired
    public AccountCashWithdrawServiceImpl(AccountCashWithdrawMapper accountCashWithdrawMapper)
    {
        super(accountCashWithdrawMapper);
        this.accountCashWithdrawMapper = accountCashWithdrawMapper;
    }

    public AccountCashWithdraw selectByIdForUpdate(Long id)
    {
        return accountCashWithdrawMapper.selectByIdForUpdate(id);
    }

    @Override
    public List<AccountCashWithdraw> listForExcel()
    {
        return accountCashWithdrawMapper.listForExcel();
    }

    @Override
    public BigDecimal findSumAmtByAccount(AccountCashWithdraw accountCashWithdraw)
    {
        return accountCashWithdrawMapper.findSumAmtByAccount(accountCashWithdraw);
    }

}

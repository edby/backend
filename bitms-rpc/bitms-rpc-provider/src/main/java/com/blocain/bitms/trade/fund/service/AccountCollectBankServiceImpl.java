/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;


import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.fund.entity.AccountCollectBank;
import com.blocain.bitms.trade.fund.mapper.AccountCollectBankMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 账户收款银行表 服务实现类
 * <p>File：AccountCollectBankServiceImpl.java </p>
 * <p>Title: AccountCollectBankServiceImpl </p>
 * <p>Description:AccountCollectBankServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountCollectBankServiceImpl extends GenericServiceImpl<AccountCollectBank> implements AccountCollectBankService
{

    protected AccountCollectBankMapper accountCollectBankMapper;

    @Autowired
    public AccountCollectBankServiceImpl(AccountCollectBankMapper accountCollectBankMapper)
    {
        super(accountCollectBankMapper);
        this.accountCollectBankMapper = accountCollectBankMapper;
    }
}

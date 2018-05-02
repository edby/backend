package com.blocain.bitms.ignite.service.impl;

import com.blocain.bitms.ignite.service.AccountFundCurrentIgnite;
import com.blocain.bitms.orm.core.GenericIgnigeImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.blocain.bitms.ignite.repository.AccountFundCurrentRepository;
import com.blocain.bitms.trade.fund.entity.AccountFundCurrent;

/**
 * AccountFundCurrentIgniteImpl
 * <p>File：AccountFundCurrentIgniteImpl.java </p>
 * <p>Title: AccountFundCurrentIgniteImpl </p>
 * <p>Description:AccountFundCurrentIgniteImpl </p>
 * <p>Copyright: Copyright (c) 2018/4/2</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
// @Service
public class AccountFundCurrentIgniteImpl extends GenericIgnigeImpl<AccountFundCurrent> implements AccountFundCurrentIgnite
{
    protected AccountFundCurrentRepository accountFundCurrentRepository;
    
    @Autowired
    public AccountFundCurrentIgniteImpl(AccountFundCurrentRepository cache)
    {
        super(cache);
        this.accountFundCurrentRepository = cache;
    }
}

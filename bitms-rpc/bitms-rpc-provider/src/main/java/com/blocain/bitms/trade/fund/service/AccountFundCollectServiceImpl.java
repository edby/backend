/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.fund.entity.AccountFundCollect;
import com.blocain.bitms.trade.fund.mapper.AccountFundCollectMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 账户资金归集表 服务实现类
 * <p>File：AccountFundCollect.java </p>
 * <p>Title: AccountFundCollect </p>
 * <p>Description:AccountFundCollect </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountFundCollectServiceImpl extends GenericServiceImpl<AccountFundCollect> implements AccountFundCollectService
{
    AccountFundCollectMapper accountFundCollectMapper;
    
    @Autowired
    public AccountFundCollectServiceImpl(AccountFundCollectMapper accountFundCollectMapper)
    {
        super(accountFundCollectMapper);
        this.accountFundCollectMapper = accountFundCollectMapper;
    }
}

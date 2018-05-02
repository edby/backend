/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.fund.entity.AccountFundAdjust;
import com.blocain.bitms.trade.fund.mapper.AccountFundAdjustMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 账户资金调整记录表 服务实现类
 * <p>File：AccountFundAdjustServiceImpl.java </p>
 * <p>Title: AccountFundAdjustServiceImpl </p>
 * <p>Description:AccountFundAdjustServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountFundAdjustServiceImpl extends GenericServiceImpl<AccountFundAdjust> implements AccountFundAdjustService
{
    private AccountFundAdjustMapper accountFundAdjustMapper;
    
    @Autowired
    public AccountFundAdjustServiceImpl(AccountFundAdjustMapper accountFundAdjustMapper)
    {
        super(accountFundAdjustMapper);
        this.accountFundAdjustMapper = accountFundAdjustMapper;
    }
    
    @Override
    public List<AccountFundAdjust> findLockedList(AccountFundAdjust entity)
    {
        return accountFundAdjustMapper.findLockedList(entity);
    }
}

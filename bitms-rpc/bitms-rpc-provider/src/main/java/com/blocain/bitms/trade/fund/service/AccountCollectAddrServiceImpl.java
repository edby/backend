/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.blocain.bitms.trade.fund.entity.AccountCollectAddr;
import com.blocain.bitms.trade.fund.mapper.AccountCollectAddrMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 账户收款地址表 服务实现类
 * <p>File：AccountCollectAddr.java </p>
 * <p>Title: AccountCollectAddr </p>
 * <p>Description:AccountCollectAddr </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountCollectAddrServiceImpl extends GenericServiceImpl<AccountCollectAddr> implements AccountCollectAddrService
{
    private AccountCollectAddrMapper accountCollectAddrMapper;
    
    @Autowired
    public AccountCollectAddrServiceImpl(AccountCollectAddrMapper accountCollectAddrMapper)
    {
        super(accountCollectAddrMapper);
        this.accountCollectAddrMapper = accountCollectAddrMapper;
    }
    
    @Override
    public AccountCollectAddr findAccountCollectAddr(AccountCollectAddr accountCollectAddr)
    {
        return accountCollectAddrMapper.findAccountCollectAddr(accountCollectAddr);
    }
    
    @Override
    public Long insertAccountCollectAddr(AccountCollectAddr accountCollectAddr)
    {
        Long id = SerialnoUtils.buildPrimaryKey();
        accountCollectAddr.setId(id);
        accountCollectAddrMapper.insert(accountCollectAddr);
        return id;
    }
}

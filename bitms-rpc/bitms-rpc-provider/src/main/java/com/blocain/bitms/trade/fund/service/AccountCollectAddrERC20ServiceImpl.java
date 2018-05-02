/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;


import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.fund.entity.AccountCollectAddrERC20;
import com.blocain.bitms.trade.fund.mapper.AccountCollectAddrERC20Mapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 账户收款地址表 服务实现类
 * <p>File：AccountCollectAddrERC20ServiceImpl.java </p>
 * <p>Title: AccountCollectAddrERC20ServiceImpl </p>
 * <p>Description:AccountCollectAddrERC20ServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountCollectAddrERC20ServiceImpl extends GenericServiceImpl<AccountCollectAddrERC20> implements AccountCollectAddrERC20Service
{

    protected AccountCollectAddrERC20Mapper accountCollectAddrERC20Mapper;

    @Autowired
    public AccountCollectAddrERC20ServiceImpl(AccountCollectAddrERC20Mapper accountCollectAddrERC20Mapper)
    {
        super(accountCollectAddrERC20Mapper);
        this.accountCollectAddrERC20Mapper = accountCollectAddrERC20Mapper;
    }
}

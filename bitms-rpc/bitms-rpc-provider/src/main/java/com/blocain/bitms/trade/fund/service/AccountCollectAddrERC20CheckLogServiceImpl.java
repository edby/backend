/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;


import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.fund.entity.AccountCollectAddrERC20CheckLog;
import com.blocain.bitms.trade.fund.mapper.AccountCollectAddrERC20CheckLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ERC20账户收款地址审核日志表 服务实现类
 * <p>File：AccountCollectAddrERC20CheckLogServiceImpl.java </p>
 * <p>Title: AccountCollectAddrERC20CheckLogServiceImpl </p>
 * <p>Description:AccountCollectAddrERC20CheckLogServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountCollectAddrERC20CheckLogServiceImpl extends GenericServiceImpl<AccountCollectAddrERC20CheckLog> implements AccountCollectAddrERC20CheckLogService
{

    protected AccountCollectAddrERC20CheckLogMapper accountCollectAddrERC20CheckLogMapper;

    @Autowired
    public AccountCollectAddrERC20CheckLogServiceImpl(AccountCollectAddrERC20CheckLogMapper accountCollectAddrERC20CheckLogMapper)
    {
        super(accountCollectAddrERC20CheckLogMapper);
        this.accountCollectAddrERC20CheckLogMapper = accountCollectAddrERC20CheckLogMapper;
    }
}

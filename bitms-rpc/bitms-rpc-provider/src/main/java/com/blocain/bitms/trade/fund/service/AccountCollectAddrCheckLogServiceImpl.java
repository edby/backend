/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;


import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.fund.entity.AccountCollectAddrCheckLog;
import com.blocain.bitms.trade.fund.mapper.AccountCollectAddrCheckLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 账户收款地址审核日志表 服务实现类
 * <p>File：AccountCollectAddrCheckLogServiceImpl.java </p>
 * <p>Title: AccountCollectAddrCheckLogServiceImpl </p>
 * <p>Description:AccountCollectAddrCheckLogServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountCollectAddrCheckLogServiceImpl extends GenericServiceImpl<AccountCollectAddrCheckLog> implements AccountCollectAddrCheckLogService
{

    protected AccountCollectAddrCheckLogMapper accountCollectAddrCheckLogMapper;

    @Autowired
    public AccountCollectAddrCheckLogServiceImpl(AccountCollectAddrCheckLogMapper accountCollectAddrCheckLogMapper)
    {
        super(accountCollectAddrCheckLogMapper);
        this.accountCollectAddrCheckLogMapper = accountCollectAddrCheckLogMapper;
    }
}

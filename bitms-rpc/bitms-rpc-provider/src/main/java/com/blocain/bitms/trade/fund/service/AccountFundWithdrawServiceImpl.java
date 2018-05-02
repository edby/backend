/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.tools.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.blocain.bitms.trade.fund.entity.AccountFundWithdraw;
import com.blocain.bitms.trade.fund.mapper.AccountFundWithdrawMapper;

/**
 * 账户资金提现记录表 服务实现类
 * <p>File：AccountFundWithdrawServiceImpl.java </p>
 * <p>Title: AccountFundWithdrawServiceImpl </p>
 * <p>Description:AccountFundWithdrawServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountFundWithdrawServiceImpl extends GenericServiceImpl<AccountFundWithdraw> implements AccountFundWithdrawService
{
    protected AccountFundWithdrawMapper accountFundWithdrawMapper;
    
    @Autowired
    public AccountFundWithdrawServiceImpl(AccountFundWithdrawMapper accountFundWithdrawMapper)
    {
        super(accountFundWithdrawMapper);
        this.accountFundWithdrawMapper = accountFundWithdrawMapper;
    }
    
    @Override
    public AccountFundWithdraw insertAccountFundWithdraw(AccountFundWithdraw entity)
    {
        Long id = SerialnoUtils.buildPrimaryKey();
        entity.setId(id);
        entity.setConfirmCode(RandomUtils.getFixLenthString(6));// 获取6位随机码
        accountFundWithdrawMapper.insert(entity);
        entity.setId(id);
        return entity;
    }
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.account.entity.AccountInvitation;
import com.blocain.bitms.trade.account.mapper.AccountInvitationMapper;

/**
 * 账户邀请记录表 服务实现类
 * <p>File：AccountInvitation.java </p>
 * <p>Title: AccountInvitation </p>
 * <p>Description:AccountInvitation </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountInvitationServiceImpl extends GenericServiceImpl<AccountInvitation> implements AccountInvitationService
{
    private AccountInvitationMapper accountInvitationMapper;
    
    @Autowired
    public AccountInvitationServiceImpl(AccountInvitationMapper accountInvitationMapper)
    {
        super(accountInvitationMapper);
        this.accountInvitationMapper = accountInvitationMapper;
    }

    @Override
    public List<AccountInvitation> findLockedList(AccountInvitation entity)
    {
        return accountInvitationMapper.findLockedList(entity);
    }
}

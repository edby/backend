/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.service;

import java.util.List;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.trade.account.entity.AccountInvitation;

/**
 * 账户邀请记录表 服务接口
 * <p>File：AccountInvitationService.java </p>
 * <p>Title: AccountInvitationService </p>
 * <p>Description:AccountInvitationService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface AccountInvitationService extends GenericService<AccountInvitation>
{
    /**
     * 新用户注册奖励（冻结的奖励）
     * @param entity
     * @return
     */
    List<AccountInvitation> findLockedList(AccountInvitation entity);
}

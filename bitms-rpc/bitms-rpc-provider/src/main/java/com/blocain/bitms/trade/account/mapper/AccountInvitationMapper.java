/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.mapper;

import java.util.List;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.trade.account.entity.AccountInvitation;

/**
 * 账户邀请记录表 持久层接口
 * <p>File：AccountInvitationDao.java </p>
 * <p>Title: AccountInvitationDao </p>
 * <p>Description:AccountInvitationDao </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface AccountInvitationMapper extends GenericMapper<AccountInvitation>
{
    /**
     * 新用户注册奖励（冻结的奖励）
     * @param entity
     * @return
     */
    List<AccountInvitation> findLockedList(AccountInvitation entity);
}

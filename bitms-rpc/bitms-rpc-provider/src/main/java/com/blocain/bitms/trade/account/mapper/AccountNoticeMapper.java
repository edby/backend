/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.mapper;

import org.apache.ibatis.annotations.Param;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.trade.account.entity.AccountNotice;

/**
 * 用户消息公告关联表 持久层接口
 * <p>File：AccountNoticeDao.java </p>
 * <p>Title: AccountNoticeDao </p>
 * <p>Description:AccountNoticeDao </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface AccountNoticeMapper extends GenericMapper<AccountNotice>
{
    /**
     * 根据帐号ID和公告ID取数据
     * @param accountId
     * @param noticeId
     * @return
     */
    AccountNotice findByAccountNotice(@Param("accountId") Long accountId, @Param("noticeId") Long noticeId);
}

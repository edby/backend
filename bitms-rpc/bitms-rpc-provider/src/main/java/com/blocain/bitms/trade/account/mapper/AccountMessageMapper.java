/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.mapper;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.trade.account.entity.AccountMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户消息关联表 持久层接口
 * <p>File：AccountMessageMapper.java </p>
 * <p>Title: AccountMessageMapper </p>
 * <p>Description:AccountMessageMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface AccountMessageMapper extends GenericMapper<AccountMessage>
{
    /**
     * 根据消息ID和帐户ID删除
     * @param accountId
     * @param messageIds
     */
    void deleteAccountMessageById(@Param("accountId") Long accountId, @Param("messageIds") List<Long> messageIds);
}

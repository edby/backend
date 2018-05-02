/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.core.GenericMapper;

import java.util.List;

import com.blocain.bitms.orm.annotation.MyBatisDao ;
import com.blocain.bitms.trade.fund.entity.AccountFundAdjust;

/**
 * 账户资金调整记录表 持久层接口
 * <p>File：AccountFundAdjustMapper.java </p>
 * <p>Title: AccountFundAdjustMapper </p>
 * <p>Description:AccountFundAdjustMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface AccountFundAdjustMapper extends GenericMapper<AccountFundAdjust>
{
    /**
     * 查询锁定日期到未解冻的资金调整记录
     * @param entity
     * @return
     */
    List<AccountFundAdjust> findLockedList(AccountFundAdjust entity);

}

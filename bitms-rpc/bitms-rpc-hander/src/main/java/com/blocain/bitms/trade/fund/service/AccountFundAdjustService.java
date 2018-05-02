/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import java.util.List;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.trade.fund.entity.AccountFundAdjust;
/**
 * 账户资金调整记录表 服务接口
 * <p>File：AccountFundAdjustService.java </p>
 * <p>Title: AccountFundAdjustService </p>
 * <p>Description:AccountFundAdjustService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface AccountFundAdjustService extends GenericService<AccountFundAdjust>{
    /**
     * 查询锁定日期到未解冻的资金调整记录
     * @param entity
     * @return
     */
    List<AccountFundAdjust> findLockedList(AccountFundAdjust entity);
    
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.trade.fund.entity.AccountFundWithdraw;

/**
 * 账户资金提现记录表 服务接口
 * <p>File：AccountFundWithdrawService.java </p>
 * <p>Title: AccountFundWithdrawService </p>
 * <p>Description:AccountFundWithdrawService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface AccountFundWithdrawService extends GenericService<AccountFundWithdraw>{
    /**
     * 插入数据并返回对象
     * @param entity
     * @return
     */
    AccountFundWithdraw insertAccountFundWithdraw(AccountFundWithdraw entity);

}

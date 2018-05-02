/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.trade.fund.entity.AccountWithdrawRecordERC20;
/**
 * ERC20账户提现记录表 服务接口
 * <p>File：AccountWithdrawRecordERC20Service.java </p>
 * <p>Title: AccountWithdrawRecordERC20Service </p>
 * <p>Description:AccountWithdrawRecordERC20Service </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface AccountWithdrawRecordERC20Service extends GenericService<AccountWithdrawRecordERC20>{

    AccountWithdrawRecordERC20 selectByIdForUpdate(Long id);

    /**
     * 轮询ERC20 TOKEN 的交易状态
     */
    void autoTransactionStatus();

    /**
     * 轮询ERC20 TOKEN 的交易状态 单个
     */
    void doTransactionStatus(Long id);

    /**
     * 查询用户已用提现额度
     * @param accountWithdrawRecordERC20
     * @return
     */
    java.math.BigDecimal findSumAmtByAccount(AccountWithdrawRecordERC20 accountWithdrawRecordERC20);

    /**
     * 查询用户已用提现次数（所有 token）
     * @param accountWithdrawRecordERC20
     * @return
     */
    Integer findCuntByAccount(AccountWithdrawRecordERC20 accountWithdrawRecordERC20);
}

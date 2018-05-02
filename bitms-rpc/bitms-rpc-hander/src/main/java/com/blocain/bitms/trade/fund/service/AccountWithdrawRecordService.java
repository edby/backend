/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.annotation.SlaveDataSource;
import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.entity.AccountWithdrawRecord;

import java.sql.Timestamp;
import java.util.List;

/**
 * 账户提现记录表 服务接口
 * <p>File：AccountWithdrawRecordService.java </p>
 * <p>Title: AccountWithdrawRecordService </p>
 * <p>Description:AccountWithdrawRecordService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface AccountWithdrawRecordService extends GenericService<AccountWithdrawRecord>
{

    AccountWithdrawRecord selectByPrimaryKey(String tableName, Long id) throws BusinessException;
    /**
     * 获取账户资金流水实体
     * @param accountWithdrawRecord    查询实体
     * @return
     * @author 施建波  2017年7月11日 下午2:33:17
     */
    AccountWithdrawRecord findAccountFundCurrent(AccountWithdrawRecord accountWithdrawRecord);

    /**
     * 获取资金流水列表
     * @param accountWithdrawRecord    查询实体
     * @return
     * @author 施建波  2017年7月13日 上午9:38:40
     */
    PaginateResult<AccountWithdrawRecord> accountFundCurrentSearch(Pagination pagin, AccountWithdrawRecord accountWithdrawRecord);

    /**
     * 获取转帐资金流水
     * @param pagin
     * @param accountWithdrawRecord
     * @return
     * @author 施建波  2017年7月13日 下午3:37:01
     */
    PaginateResult<AccountWithdrawRecord> accountFundCurrentChargeSearch(Pagination pagin, AccountWithdrawRecord accountWithdrawRecord, String... tableNames);

    /**
     * 查询用户资金流水
     * @param pagin
     * @param accountWithdrawRecord
     * @param businessFlags
     * @return
     */
    @SlaveDataSource()
    PaginateResult<AccountWithdrawRecord> findListByAccount(Pagination pagin, AccountWithdrawRecord accountWithdrawRecord, String... businessFlags);

    /**
     * 查询用户已用提现额度
     * @param accountWithdrawRecord
     * @return
     */
    java.math.BigDecimal findSumAmtByAccount(AccountWithdrawRecord accountWithdrawRecord);

    /**
     * 查询用户已充值额度
     * @param accountWithdrawRecord
     * @return
     */
    java.math.BigDecimal findSumChargeAmtByAccount(AccountWithdrawRecord accountWithdrawRecord);

    /**
     * 查询所有账户本周总流入或流出BTC数量（不包括超级账户）
     * @param accountWithdrawRecord （如果accountId 不为空 则查询个人）
     * @return
     */
    java.math.BigDecimal findCurrentWeekSumAmtByAccount(AccountWithdrawRecord accountWithdrawRecord);

    /**
     * 获取交割平仓中的已还借款
     * @param accountWithdrawRecord    查询实体
     * @return
     * @author zcx 2017-10-25 16:04:15
     */
    PaginateResult<AccountWithdrawRecord> findDebitRepaySettlemenet(Pagination pagin, AccountWithdrawRecord accountWithdrawRecord, String... businessFlags);

    /**
     * 获取最近一笔流水
      * @return
     */
    AccountWithdrawRecord findTheLatestFundCurrent();

    /**
     * 获取最近一段时间内的资金变动账户
     * @param currentdate 查询时间
     * @return
     */
    List<Long> getChangeAcctListByTimestamp(Timestamp currentdate, String tableName);

}

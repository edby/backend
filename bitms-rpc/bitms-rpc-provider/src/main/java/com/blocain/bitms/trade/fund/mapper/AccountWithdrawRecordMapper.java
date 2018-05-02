/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.trade.fund.entity.AccountWithdrawRecord;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

/**
 * 账户提现记录表 持久层接口
 * <p>File：AccountWithdrawRecordDao.java </p>
 * <p>Title: AccountWithdrawRecordDao </p>
 * <p>Description:AccountWithdrawRecordDao </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface AccountWithdrawRecordMapper extends GenericMapper<AccountWithdrawRecord>
{
    AccountWithdrawRecord findFundCurrent(AccountWithdrawRecord accountWithdrawRecord);

    List<AccountWithdrawRecord> findFundCurrentList(AccountWithdrawRecord accountWithdrawRecord);

    List<AccountWithdrawRecord> findFundCurrentChargeList(@Param("page") Pagination pagin, @Param("accountWithdrawRecord") AccountWithdrawRecord accountWithdrawRecord, @Param("tableNames") String... tableNames);

    Long findFundCurrentChargeListCount(@Param("accountWithdrawRecord") AccountWithdrawRecord accountWithdrawRecord, @Param("tableNames") String... tableNames);

    AccountWithdrawRecord selectByPrimaryKey(@Param("tableName") String tableName, @Param("id") Long id);

    /**
     * 统计资金流水记录数
     * @param accountWithdrawRecord
     * @param businessFlags
     * @return
     */
    Long countByAccount(@Param("accountWithdrawRecord") AccountWithdrawRecord accountWithdrawRecord, @Param("businessFlags") String[] businessFlags);

    /**
    * 查询用户资金流水
    * @param pagin
    * @param accountWithdrawRecord
    * @param businessFlags
    * @return
    */
    List<AccountWithdrawRecord> findListByAccount(@Param("accountWithdrawRecord") AccountWithdrawRecord accountWithdrawRecord, @Param("businessFlags") String[] businessFlags,
                                               @Param("page") Pagination pagin);

    /**
     * 查询用户已用提现额度
     * @param accountWithdrawRecord
     * @return
     */
    java.math.BigDecimal findSumAmtByAccount(AccountWithdrawRecord accountWithdrawRecord);

    /**
     * 查询用户已用充值额度
     * @param accountWithdrawRecord
     * @return
     */
    java.math.BigDecimal findSumChargeAmtByAccount(AccountWithdrawRecord accountWithdrawRecord);

    /**
     * 查询所有账户本周总流入BTC数量（不包括超级账户）
     * @param accountWithdrawRecord （如果accountId 不为空 则查询个人）
     * @return
     */
    java.math.BigDecimal findCurrentWeekSumAmtByAccount(AccountWithdrawRecord accountWithdrawRecord);

    /**
     * 获取交割平仓中的已还借款
     * @param accountWithdrawRecord
     * @param businessFlags
     * @return
     */
    List<AccountWithdrawRecord> findDebitRepaySettlemenet(@Param("accountWithdrawRecord") AccountWithdrawRecord accountWithdrawRecord, @Param("businessFlags") String[] businessFlags);

    /**
     * 获取最近一笔资金流水
     * @return
     */
    AccountWithdrawRecord findTheLatestFundCurrent();

    /**
     * 获取最近一段时间内的资金变动账户
     * @param currentdate  查询的起始时间
     * @return
     */
    List<Long> getChangeAcctListByTimestamp(@Param("currentdate") Timestamp currentdate, @Param("tableName") String tableName);
}

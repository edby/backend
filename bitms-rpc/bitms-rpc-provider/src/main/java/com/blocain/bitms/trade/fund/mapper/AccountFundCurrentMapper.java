/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.trade.fund.entity.AccountFundCurrent;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

/**
 * 账户流水表 持久层接口
 * <p>File：AccountCurrentDao.java </p>
 * <p>Title: AccountCurrentDao </p>
 * <p>Description:AccountCurrentDao </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface AccountFundCurrentMapper extends GenericMapper<AccountFundCurrent>
{
    AccountFundCurrent findFundCurrent(AccountFundCurrent accountFundCurrent);
    
    List<AccountFundCurrent> findFundCurrentList(AccountFundCurrent accountFundCurrent);
    
    List<AccountFundCurrent> findFundCurrentChargeList(@Param("page") Pagination pagin,@Param("accountFundCurrent")AccountFundCurrent accountFundCurrent,@Param("tableNames") String ... tableNames);

    Long findFundCurrentChargeListCount(@Param("accountFundCurrent")AccountFundCurrent accountFundCurrent,@Param("tableNames") String ... tableNames);

    AccountFundCurrent selectByPrimaryKey(@Param("tableName") String tableName, @Param("id") Long id);
    
    /**
     * 统计资金流水记录数
     * @param accountFundCurrent
     * @param businessFlags
     * @return
     */
    Long countByAccount(@Param("accountFundCurrent") AccountFundCurrent accountFundCurrent, @Param("businessFlags") String[] businessFlags);
    
    /**
    * 查询用户资金流水
    * @param pagin
    * @param accountFundCurrent
    * @param businessFlags
    * @return
    */
    List<AccountFundCurrent> findListByAccount(@Param("accountFundCurrent") AccountFundCurrent accountFundCurrent, @Param("businessFlags") String[] businessFlags,
            @Param("page") Pagination pagin);
    
    /**
     * 查询用户已用提现额度
     * @param accountFundCurrent
     * @return
     */
    java.math.BigDecimal findSumAmtByAccount(AccountFundCurrent accountFundCurrent);
    
    /**
     * 查询用户已用充值额度
     * @param accountFundCurrent
     * @return
     */
    java.math.BigDecimal findSumChargeAmtByAccount(AccountFundCurrent accountFundCurrent);
    
    /**
     * 查询所有账户本周总流入BTC数量（不包括超级账户）
     * @param accountFundCurrent （如果accountId 不为空 则查询个人）
     * @return
     */
    java.math.BigDecimal findCurrentWeekSumAmtByAccount(AccountFundCurrent accountFundCurrent);
    
    /**
     * 获取交割平仓中的已还借款
     * @param accountFundCurrent
     * @param businessFlags
     * @return
     */
    List<AccountFundCurrent> findDebitRepaySettlemenet(@Param("accountFundCurrent") AccountFundCurrent accountFundCurrent, @Param("businessFlags") String[] businessFlags);
    
    /**
     * 获取最近一笔资金流水
     * @return
     */
    AccountFundCurrent findTheLatestFundCurrent();
    
    /**
     * 获取最近一段时间内的资金变动账户
     * @param currentdate  查询的起始时间
     * @return
     */
    List<Long> getChangeAcctListByTimestamp(@Param("currentdate")Timestamp currentdate,@Param("tableName")String tableName);
}

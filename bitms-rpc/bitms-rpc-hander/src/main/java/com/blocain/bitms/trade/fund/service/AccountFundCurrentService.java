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
import com.blocain.bitms.trade.fund.entity.AccountFundCurrent;

import java.sql.Timestamp;
import java.util.List;

/**
 * 账户资金流水表 服务接口
 * <p>File：AccountCurrentService.java </p>
 * <p>Title: AccountCurrentService </p>
 * <p>Description:AccountCurrentService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface AccountFundCurrentService extends GenericService<AccountFundCurrent>
{

    AccountFundCurrent selectByPrimaryKey(String tableName,Long id) throws BusinessException;
    /**
     * 获取账户资金流水实体
     * @param accountFundCurrent    查询实体
     * @return
     * @author 施建波  2017年7月11日 下午2:33:17
     */
    AccountFundCurrent findAccountFundCurrent(AccountFundCurrent accountFundCurrent);
    
    /**
     * 获取资金流水列表
     * @param accountFundCurrent    查询实体
     * @return
     * @author 施建波  2017年7月13日 上午9:38:40
     */
    PaginateResult<AccountFundCurrent> accountFundCurrentSearch(Pagination pagin, AccountFundCurrent accountFundCurrent);
    
    /**
     * 获取转帐资金流水
     * @param pagin
     * @param accountFundCurrent
     * @return
     * @author 施建波  2017年7月13日 下午3:37:01
     */
    PaginateResult<AccountFundCurrent> accountFundCurrentChargeSearch(Pagination pagin, AccountFundCurrent accountFundCurrent,String ... tableNames);
   
    /**
     * 查询用户资金流水
     * @param pagin
     * @param accountFundCurrent
     * @param businessFlags
     * @return
     */
    @SlaveDataSource()
    PaginateResult<AccountFundCurrent> findListByAccount(Pagination pagin,AccountFundCurrent accountFundCurrent,String ... businessFlags);
    
    /**
     * 查询用户已用提现额度
     * @param accountFundCurrent
     * @return
     */
    java.math.BigDecimal findSumAmtByAccount(AccountFundCurrent accountFundCurrent);

    /**
     * 查询用户已充值额度
     * @param accountFundCurrent
     * @return
     */
    java.math.BigDecimal findSumChargeAmtByAccount(AccountFundCurrent accountFundCurrent);

    /**
     * 查询所有账户本周总流入或流出BTC数量（不包括超级账户）
     * @param accountFundCurrent （如果accountId 不为空 则查询个人）
     * @return
     */
    java.math.BigDecimal findCurrentWeekSumAmtByAccount(AccountFundCurrent accountFundCurrent);

    /**
     * 获取交割平仓中的已还借款
     * @param accountFundCurrent    查询实体
     * @return
     * @author zcx 2017-10-25 16:04:15
     */
    PaginateResult<AccountFundCurrent> findDebitRepaySettlemenet(Pagination pagin, AccountFundCurrent accountFundCurrent,String ... businessFlags);

    /**
     * 获取最近一笔流水
      * @return
     */
    AccountFundCurrent findTheLatestFundCurrent();

    /**
     * 获取最近一段时间内的资金变动账户
     * @param currentdate 查询时间
     * @return
     */
    List<Long> getChangeAcctListByTimestamp(Timestamp currentdate,String tableName);

}

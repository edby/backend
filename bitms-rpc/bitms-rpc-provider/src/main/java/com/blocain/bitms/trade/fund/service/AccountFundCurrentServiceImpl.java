/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.entity.AccountFundCurrent;
import com.blocain.bitms.trade.fund.mapper.AccountFundCurrentMapper;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 账户资金流水表 服务实现类
 * <p>File：AccountCurrent.java </p>
 * <p>Title: AccountCurrent </p>
 * <p>Description:AccountCurrent </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountFundCurrentServiceImpl extends GenericServiceImpl<AccountFundCurrent> implements AccountFundCurrentService
{
    private AccountFundCurrentMapper accountCurrentMapper;
    
    @Autowired
    public AccountFundCurrentServiceImpl(AccountFundCurrentMapper accountCurrentMapper)
    {
        super(accountCurrentMapper);
        this.accountCurrentMapper = accountCurrentMapper;
    }
    
    @Override
    public AccountFundCurrent selectByPrimaryKey(String tableName, Long id) throws BusinessException
    {
        return accountCurrentMapper.selectByPrimaryKey(tableName, id);
    }
    
    /*
     * (non-Javadoc)
     * @see com.blocain.bitms.trade.fund.service.AccountFundCurrentService#findAccountFundCurrent(com.blocain.bitms.trade.fund.entity.AccountFundCurrent)
     */
    @Override
    public AccountFundCurrent findAccountFundCurrent(AccountFundCurrent accountFundCurrent)
    {
        return accountCurrentMapper.findFundCurrent(accountFundCurrent);
    }
    
    /*
     * (non-Javadoc)
     * @see com.blocain.bitms.trade.fund.service.AccountFundCurrentService#findFundCurrentSearch(com.blocain.bitms.tools.bean.Pagination,
     * com.blocain.bitms.trade.fund.entity.AccountFundCurrent)
     */
    @Override
    public PaginateResult<AccountFundCurrent> accountFundCurrentSearch(Pagination pagin, AccountFundCurrent accountFundCurrent)
    {
        accountFundCurrent.setPagin(pagin);
        List<AccountFundCurrent> fundCurrentList = accountCurrentMapper.findFundCurrentList(accountFundCurrent);
        return new PaginateResult<>(pagin, fundCurrentList);
    }
    
    /*
     * (non-Javadoc)
     * @see com.blocain.bitms.trade.fund.service.AccountFundCurrentService#fundCurrentChargeSearch(com.blocain.bitms.tools.bean.Pagination,
     * com.blocain.bitms.trade.fund.entity.AccountFundCurrent)
     */
    @Override
    public PaginateResult<AccountFundCurrent> accountFundCurrentChargeSearch(Pagination pagin, AccountFundCurrent accountFundCurrent,String ... tableNames)
    {
        accountFundCurrent.setPagin(pagin);
        pagin.setTotalRows(accountCurrentMapper.findFundCurrentChargeListCount(accountFundCurrent,tableNames));
        List<AccountFundCurrent> fundCurrentList = accountCurrentMapper.findFundCurrentChargeList(pagin,accountFundCurrent,tableNames);
        return new PaginateResult<>(pagin, fundCurrentList);
    }
    
    @Override
    public BigDecimal findSumAmtByAccount(AccountFundCurrent accountFundCurrent)
    {
        return accountCurrentMapper.findSumAmtByAccount(accountFundCurrent);
    }
    
    @Override
    public BigDecimal findSumChargeAmtByAccount(AccountFundCurrent accountFundCurrent)
    {
        return accountCurrentMapper.findSumChargeAmtByAccount(accountFundCurrent);
    }
    
    @Override
    public PaginateResult<AccountFundCurrent> findListByAccount(Pagination pagin, AccountFundCurrent accountFundCurrent, String ... businessFlags)
    {
        List<String> filter = Lists.newArrayList(businessFlags);
        pagin.setTotalRows(accountCurrentMapper.countByAccount(accountFundCurrent, filter.toArray(new String[]{})));
        List<AccountFundCurrent> fundCurrentList = accountCurrentMapper.findListByAccount(accountFundCurrent, filter.toArray(new String[]{}), pagin);
        return new PaginateResult<>(pagin, fundCurrentList);
    }
    
    @Override
    public BigDecimal findCurrentWeekSumAmtByAccount(AccountFundCurrent accountFundCurrent)
    {
        return accountCurrentMapper.findCurrentWeekSumAmtByAccount(accountFundCurrent);
    }
    
    @Override
    public PaginateResult<AccountFundCurrent> findDebitRepaySettlemenet(Pagination pagin, AccountFundCurrent entity, String ... businessFlags)
    {
        List<String> filter = Lists.newArrayList(businessFlags);
        if (null == pagin) pagin = new Pagination();
        entity.setPagin(pagin);
        List<AccountFundCurrent> data = accountCurrentMapper.findDebitRepaySettlemenet(entity, filter.toArray(new String[]{}));
        return new PaginateResult<>(pagin, data);
    }
    
    @Override
    public AccountFundCurrent findTheLatestFundCurrent()
    {
        return accountCurrentMapper.findTheLatestFundCurrent();
    }
    
    @Override
    public List<Long> getChangeAcctListByTimestamp(Timestamp currentdate,String tableName)
    {
        List<Long> acctList = new ArrayList<Long>();
        acctList = accountCurrentMapper.getChangeAcctListByTimestamp(currentdate,tableName);
        return acctList;
    }
}

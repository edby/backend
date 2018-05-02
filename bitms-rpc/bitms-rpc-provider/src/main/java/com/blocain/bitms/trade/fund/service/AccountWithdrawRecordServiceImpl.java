/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.entity.AccountWithdrawRecord;
import com.blocain.bitms.trade.fund.mapper.AccountWithdrawRecordMapper;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 账户提现记录表 服务实现类
 * <p>File：AccountWithdrawRecord.java </p>
 * <p>Title: AccountWithdrawRecord </p>
 * <p>Description:AccountWithdrawRecord </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountWithdrawRecordServiceImpl extends GenericServiceImpl<AccountWithdrawRecord> implements AccountWithdrawRecordService
{
    private AccountWithdrawRecordMapper accountWithdrawRecordMapper;

    @Autowired
    public AccountWithdrawRecordServiceImpl(AccountWithdrawRecordMapper accountWithdrawRecordMapper)
    {
        super(accountWithdrawRecordMapper);
        this.accountWithdrawRecordMapper = accountWithdrawRecordMapper;
    }
    
    @Override
    public AccountWithdrawRecord selectByPrimaryKey(String tableName, Long id) throws BusinessException
    {
        return accountWithdrawRecordMapper.selectByPrimaryKey(tableName, id);
    }

    @Override
    public AccountWithdrawRecord findAccountFundCurrent(AccountWithdrawRecord accountWithdrawRecord)
    {
        return accountWithdrawRecordMapper.findFundCurrent(accountWithdrawRecord);
    }

    @Override
    public PaginateResult<AccountWithdrawRecord> accountFundCurrentSearch(Pagination pagin, AccountWithdrawRecord accountWithdrawRecord)
    {
        accountWithdrawRecord.setPagin(pagin);
        List<AccountWithdrawRecord> fundCurrentList = accountWithdrawRecordMapper.findFundCurrentList(accountWithdrawRecord);
        return new PaginateResult<>(pagin, fundCurrentList);
    }

    @Override
    public PaginateResult<AccountWithdrawRecord> accountFundCurrentChargeSearch(Pagination pagin, AccountWithdrawRecord accountWithdrawRecord,String ... tableNames)
    {
        accountWithdrawRecord.setPagin(pagin);
        pagin.setTotalRows(accountWithdrawRecordMapper.findFundCurrentChargeListCount(accountWithdrawRecord,tableNames));
        List<AccountWithdrawRecord> fundCurrentList = accountWithdrawRecordMapper.findFundCurrentChargeList(pagin,accountWithdrawRecord,tableNames);
        return new PaginateResult<>(pagin, fundCurrentList);
    }
    
    @Override
    public BigDecimal findSumAmtByAccount(AccountWithdrawRecord accountWithdrawRecord)
    {
        return accountWithdrawRecordMapper.findSumAmtByAccount(accountWithdrawRecord);
    }
    
    @Override
    public BigDecimal findSumChargeAmtByAccount(AccountWithdrawRecord accountWithdrawRecord)
    {
        return accountWithdrawRecordMapper.findSumChargeAmtByAccount(accountWithdrawRecord);
    }
    
    @Override
    public PaginateResult<AccountWithdrawRecord> findListByAccount(Pagination pagin, AccountWithdrawRecord accountWithdrawRecord, String ... businessFlags)
    {
        List<String> filter = Lists.newArrayList(businessFlags);
        pagin.setTotalRows(accountWithdrawRecordMapper.countByAccount(accountWithdrawRecord, filter.toArray(new String[]{})));
        List<AccountWithdrawRecord> fundCurrentList = accountWithdrawRecordMapper.findListByAccount(accountWithdrawRecord, filter.toArray(new String[]{}), pagin);
        return new PaginateResult<>(pagin, fundCurrentList);
    }
    
    @Override
    public BigDecimal findCurrentWeekSumAmtByAccount(AccountWithdrawRecord accountWithdrawRecord)
    {
        return accountWithdrawRecordMapper.findCurrentWeekSumAmtByAccount(accountWithdrawRecord);
    }
    
    @Override
    public PaginateResult<AccountWithdrawRecord> findDebitRepaySettlemenet(Pagination pagin, AccountWithdrawRecord accountWithdrawRecord, String ... businessFlags)
    {
        List<String> filter = Lists.newArrayList(businessFlags);
        if (null == pagin) pagin = new Pagination();
        accountWithdrawRecord.setPagin(pagin);
        List<AccountWithdrawRecord> data = accountWithdrawRecordMapper.findDebitRepaySettlemenet(accountWithdrawRecord, filter.toArray(new String[]{}));
        return new PaginateResult<>(pagin, data);
    }
    
    @Override
    public AccountWithdrawRecord findTheLatestFundCurrent()
    {
        return accountWithdrawRecordMapper.findTheLatestFundCurrent();
    }
    
    @Override
    public List<Long> getChangeAcctListByTimestamp(Timestamp currentdate,String tableName)
    {
        List<Long> acctList = new ArrayList<Long>();
        acctList = accountWithdrawRecordMapper.getChangeAcctListByTimestamp(currentdate,tableName);
        return acctList;
    }
}

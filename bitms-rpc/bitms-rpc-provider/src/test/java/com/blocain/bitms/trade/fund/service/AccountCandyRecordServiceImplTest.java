package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountCandyRecord;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by admin on 2018/3/13.
 */
public class AccountCandyRecordServiceImplTest extends AbstractBaseTest
{
    @Autowired
    AccountCandyRecordService accountCandyRecordService;

    @Autowired AccountService accountService;
    
    @Test
    public void findLastRecord() throws Exception
    {
        accountCandyRecordService.findLastRecord(1L);
    }
    
    @Test
    public void list()
    {
        AccountCandyRecord entity = new AccountCandyRecord();
        accountCandyRecordService.findList(entity);
    }
    
    @Test
    public void doTradeAward() throws Exception
    {
        accountCandyRecordService.doTradeAward(67057441564528640L, FundConsts.WALLET_BIEX_TYPE, BigDecimal.valueOf(10000));
    }
    
    @Test
    public void autoTradeAward() throws Exception
    {
        accountCandyRecordService.autoTradeAward();
    }

    @Test
    public void accountAddAward()
    {
        List<Account> list = accountService.selectAll();
        for(Account account:list)
        {
            if(account.getId().longValue()>200000000000L)
            {
                try
                {
                    accountCandyRecordService.doTradeAward(account.getId(), FundConsts.WALLET_BIEX_TYPE, BigDecimal.valueOf(10000));
                }catch(Exception e)
                {
                    System.out.println(e.getLocalizedMessage());
                }
            }
        }

    }
}
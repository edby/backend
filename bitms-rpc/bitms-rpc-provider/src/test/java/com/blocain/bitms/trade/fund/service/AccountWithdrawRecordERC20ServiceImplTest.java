package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.tools.consts.DateConst;
import com.blocain.bitms.tools.utils.CalendarUtils;
import com.blocain.bitms.trade.fund.entity.AccountWithdrawRecordERC20;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by admin on 2018/3/3.
 */
public class AccountWithdrawRecordERC20ServiceImplTest extends AbstractBaseTest
{

    @Autowired
    AccountWithdrawRecordERC20Service accountWithdrawRecordERC20Service;
    @Test
    public void autoTransactionStatus() throws Exception
    {
        accountWithdrawRecordERC20Service.autoTransactionStatus();
    }

    @Test
    public void count()
    {
        AccountWithdrawRecordERC20 accountWithdrawRecord = new AccountWithdrawRecordERC20();
        accountWithdrawRecord.setAccountId(80123498525102080L);
        String dateStr = CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMD);
        accountWithdrawRecord.setTimeStart(dateStr + " 00:00:00");
        accountWithdrawRecord.setTimeEnd(dateStr + " 23:59:59");
        int cnt = accountWithdrawRecordERC20Service.findCuntByAccount(accountWithdrawRecord);
        System.out.println(cnt);
    }
}
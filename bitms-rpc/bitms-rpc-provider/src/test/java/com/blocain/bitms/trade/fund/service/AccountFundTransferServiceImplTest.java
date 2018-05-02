package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.trade.fund.entity.AccountFundTransfer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by admin on 2017/12/27.
 */
public class AccountFundTransferServiceImplTest extends AbstractBaseTest
{

    @Test
    public void doGetSingleTransaction() throws Exception
    {
        accountFundTransferService.autoGetSingleTransaction();
    }

    @Test
    public void doGetSinglePendingApprovals() throws Exception
    {
        accountFundTransferService.autoGetSinglePendingApprovals();
    }


    @Test
    public void doSingleCashWthdrawal() throws Exception
    {
        accountFundTransferService.doSingleCashWthdrawal(49392442276843520L,"","");
    }

    @Autowired(required = false)
    AccountFundTransferService accountFundTransferService;
    @Test
    public void findByIds() throws Exception
    {
        accountFundTransferService.findByIds("39619637729562624,39949758021767168");
    }
}
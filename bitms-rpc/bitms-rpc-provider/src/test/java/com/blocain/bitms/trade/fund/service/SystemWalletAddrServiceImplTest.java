package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by admin on 2018/1/22.
 */
public class SystemWalletAddrServiceImplTest extends AbstractBaseTest
{
    @Autowired
    SystemWalletAddrService systemWalletAddrService;

    @Test
    public void addressExternalQuery()
    {
        systemWalletAddrService.addressExternalQuery();
    }
}
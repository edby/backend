package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by admin on 2018/1/2.
 */
public class AccountWealthAssetServiceImplTest extends AbstractBaseTest
{
    @Autowired
    AccountWealthAssetService accountWealthAssetService;

    @Test
    public void autoAccountWealthAssetInterest() throws Exception
    {
        accountWealthAssetService.autoAccountWealthAssetInterest();
    }
}
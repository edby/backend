package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.bitpay.service.BitpayKeychainService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by admin on 2017/10/31.
 */
public class BitpayKeychainServiceImplTest extends AbstractBaseTest
{
    @Autowired
    BitpayKeychainService bitpayKeychainService;

    @Test
    public void test() throws Exception
    {
        bitpayKeychainService.getBtcCZWalletConfirmedBalance();
        bitpayKeychainService.getBtcTXWalletAllBalance();
    }

}
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by admin on 2018/4/2.
 */
public class MarketSnapServiceImplTest extends AbstractBaseTest
{

    @Autowired
    MarketSnapService marketSnapService;

    @Test
    public void selectLastOne() throws Exception
    {
        marketSnapService.selectLastOne(FundConsts.WALLET_BTC2USD_TYPE);
    }
}
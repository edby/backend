package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by admin on 2017/10/26.
 */
public class FundScanServiceImplTest extends AbstractBaseTest
{
    @Test
    public void setAccountAssetAttr() throws Exception
    {
        fundScanService.setAccountAssetAttr(300000067890L, FundConsts.WALLET_BTC_TYPE,
                FundConsts.WALLET_BTC2USDX_TYPE);

        fundScanService.setAccountAssetAttr(300000067890L, FundConsts.WALLET_BTC_TYPE,
                FundConsts.WALLET_BTC2USD_TYPE);
    }

    public static final Logger  logger = LoggerFactory.getLogger(FundScanServiceImplTest.class);

    @Autowired
    FundScanService fundScanService;

    @Test
    public void fundChangeScan() throws Exception
    {
        while(true)
        {
            fundScanService.fundChangeScan();
            Thread.sleep(1000);
        }
    }
}
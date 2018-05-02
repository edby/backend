package com.blocain.bitms.quotation.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by admin on 2017/9/20.
 */
public class RtQuotationServiceImplTest extends AbstractBaseTest
{
    @Autowired(required = false)
    RtQuotationInfoService rtQuotationInfoService;
    
    @Test
    public void queryRtQuotationInfo() throws Exception
    {
        rtQuotationInfoService.queryRtQuotationInfo(FundConsts.WALLET_BTC_TYPE, FundConsts.WALLET_BTC2USDX_TYPE);
    }
}
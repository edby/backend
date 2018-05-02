package com.blocain.bitms.quotation.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by admin on 2017/9/20.
 */
public class RtQuotationInfoServiceImplTest extends AbstractBaseTest
{
    @Autowired(required = false)
    RtQuotationInfoService rtQuotationInfoService;

    public static final Logger logger           = LoggerFactory.getLogger(RtQuotationInfoServiceImplTest.class);


    public static final String QUOTATIONCONFIG  = new StringBuffer(CacheConst.REDIS_QUOTATION_PREFIX).append(BitmsConst.SEPARATOR)
            .append(BitmsConst.OP_QUOTATION_CONFIG).append(BitmsConst.SEPARATOR).toString();
    public static final String QUOTATIONSERVERS = new StringBuffer(CacheConst.REDIS_QUOTATION_PREFIX).append(BitmsConst.SEPARATOR)
            .append(BitmsConst.OP_QUOTATION_SERVERS).append(BitmsConst.SEPARATOR).toString();


    @Test
    public void queryRtQuotationInfoTest()
    {
        rtQuotationInfoService.queryRtQuotationInfo(FundConsts.WALLET_BTC_TYPE,FundConsts.WALLET_BTC2USDX_TYPE);
    }

    public static void main(String[] args) throws IOException
    {
        //quotation|config|177777777702
        Long a = 177777777702L;
        String sa = "177777777702";

        Boolean b = a.toString().equalsIgnoreCase(sa);
        System.out.println(b);


    }
}
package com.blocain.bitms.quotation.service;

import com.blocain.bitms.quotation.entity.Quotation;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.trade.fund.consts.FundConsts;

/**
 * Created by admin on 2017/9/20.
 */
public class QuotationServiceImplTest extends AbstractBaseTest
{
    @Autowired(required = false)
    QuotationService quotationService;
    
    @Test
    public void findQuotationByLastTime() throws Exception
    {
        Quotation quotationParam = new Quotation();
        quotationParam.setStockId(FundConsts.WALLET_BTC_TYPE);
        quotationParam.setBitstampId(14517915491504128l);
        quotationParam.setBitfienexId(14516000644927488l);
        quotationService.findQuotationByLastTime(quotationParam);
    }
}
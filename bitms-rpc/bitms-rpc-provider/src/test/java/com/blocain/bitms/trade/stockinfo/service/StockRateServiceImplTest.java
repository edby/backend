package com.blocain.bitms.trade.stockinfo.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.stockinfo.entity.StockRate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by admin on 2017/11/2.
 */
public class StockRateServiceImplTest extends AbstractBaseTest
{
    @Autowired
    StockRateService stockRateService;

    @Test
    public void testSelect()
    {
        StockRate stockRate = new StockRate();
        stockRate.setStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
        stockRate.setRateType(FundConsts.MATCHTRADE_SELL_FEE_RATE);//卖出费率
        List<StockRate> list = stockRateService.findList(stockRate);
        if(list.size()>0)
        {
            stockRate = list.get(0);
            System.out.println("费率:"+stockRate.getRate());
        }
        else
        {
            System.out.println("费率有问题");

        }
    }

    @Test
    public void fiexWithdrawFeeRateFromQuotation()
    {
        stockRateService.fiexWithdrawFeeRateFromQuotation();
    }
}
package com.blocain.bitms.trade.trade.service;

import com.blocain.bitms.trade.trade.enums.TradeEnums;

/**
 * Created by admin on 2017/11/3.
 */
public class CommonTest
{
    public static  void  main(String a[])
    {
        System.out.println(TradeEnums.valueOf("TRADE_TYPE_PUSHTRADE").getMessage());
    }
}

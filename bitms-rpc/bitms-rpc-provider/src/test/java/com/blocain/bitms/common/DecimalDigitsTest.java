package com.blocain.bitms.common;

import java.math.BigDecimal;

/**
 * Created by admin on 2017/10/16.
 */
public class DecimalDigitsTest
{
    public static  void  main(String a[])
    {

        Integer a1 = Integer.valueOf(0);
        Integer a2 = Integer.valueOf(0);
        System.out.println(a1==a2);

        BigDecimal key=null ;
        if(key==null)
        {
            System.out.println("参数错误：空值");
        }
        else
        {
            if(key.setScale(2,BigDecimal.ROUND_DOWN).doubleValue()<key.doubleValue())
            {
                System.out.println("位数超过2位");
            }else{
                System.out.println("位数没超过2位");
            }

        }


    }


}

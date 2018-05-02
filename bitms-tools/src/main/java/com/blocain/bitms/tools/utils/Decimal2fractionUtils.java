package com.blocain.bitms.tools.utils;


public class Decimal2fractionUtils {
    /**
     * 求两个数的最大公约数
     * @param a
     * @param b
     * @return
     */
    private static int getGongYueShu(int a, int b) {
        int t = 0;
        if(a < b){
        t = a;
        a = b;
        b = t;
        }
        int c = a % b;
        if(c == 0){
            return b;
        }else{
            return getGongYueShu(b, c);
        }
    }
    
    /**
     * 根据小数获得 分子和分母   如 0.01
     * @param xiaoshu
     * @return
     */
    public static int[] getFraction(String xiaoshu) {
        String[] array = new String[2];
        array = xiaoshu.split("\\.");
        int a = Integer.parseInt(array[0]);//获取整数部分
        int b = Integer.parseInt(array[1]);//获取小数部分
        int length = array[1].length();
        int FenZi = (int) (a * Math.pow(10, length) + b);
        int FenMu = (int) Math.pow(10, length);
        int MaxYueShu = getGongYueShu(FenZi, FenMu);
        int[] result = new int[2];
        result[0]=FenZi / MaxYueShu;
        result[1]=FenMu / MaxYueShu;
        return result;
    }


}

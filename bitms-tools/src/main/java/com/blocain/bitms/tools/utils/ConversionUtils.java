package com.blocain.bitms.tools.utils;

import java.math.BigInteger;

/**
 * 进制转换工具类
 */
public class ConversionUtils {

    /**
     * 将十进制转二进制
     *
     * @param number
     * @return
     */
    public static String toBinaryString(Long number) {
        return number.toBinaryString(number);
    }

    /**
     * 将十进制转八进制
     *
     * @param number
     * @return
     */
    public static String toOctalString(Long number) {
        return number.toOctalString(number);
    }

    /**
     * 将十进制转十六进制
     *
     * @param number
     * @return
     */
    public static String toHexString(Long number) {
        return number.toHexString(number);
    }

    /**
     * 将十进制转十六进制
     *
     * @param number
     * @return
     */
    public static String toHexString(BigInteger number) {
        return number.toString(16);
    }

    /**
     * 将二进制转十进制
     *
     * @param string
     * @return
     */
    public static Long fromBinaryString(String string) {
        return Long.parseLong(string, 2);
    }

    /**
     * 将8进制转十进制
     *
     * @param string
     * @return
     */
    public static Long fromOctalString(String string) {
        return Long.parseLong(string, 8);
    }

    /**
     * 将十六进制转十进制
     *
     * @param string
     * @return
     */
    public static Long fromHexString(String string) {
        return Long.parseLong(string, 16);
    }

    /**
     * 字符串转化成为16进制字符串
     *
     * @param s
     * @return
     */
    public static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    /**
     * 16进制转换成为string类型字符串
     *
     * @param s
     * @return
     */
    public static String hexStrToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    public static void main(String[] args) {
        System.out.println(hexStrToString("6162736466616466617366617366617366"));
    }
}

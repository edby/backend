/*
 * @(#)NumericUtils.java 2014-4-15 下午4:28:44
 * Copyright 2014 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.utils;

import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

/**
 * <p>File：NumericUtils.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2014 2014-4-15 下午4:28:44</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class NumericUtils {
    private NumericUtils() {
        super();
    }

    // 默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;

    /**
     * 默认货币类型小数保留位数
     */
    public static final int CURRENCY_SCALE_NUM = 2;

    public static final Logger logger = LoggerFactory.getLogger(NumericUtils.class);


    /**
     * 将34.22%格式的字符串转为0.3422
     *
     * @param string 百分比格式的字符串
     * @param len    精确位数
     * @return Double 转化后的double
     */
    public static Double stringToDouble(String string, int len) {
        DecimalFormat df = new DecimalFormat("###.####");
        string = StringUtils.trimToEmpty(string);
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumIntegerDigits(3);// 小数点前面最多显示几位的
        nf.setMaximumFractionDigits(len);
        Double d = 0D;
        try {
            d = (Double) nf.parse(string);
            d = Double.parseDouble(df.format(d));
        } catch (ParseException e) {
        } catch (ClassCastException e) {
        }
        // System.out.println(string +"================="+d);
        return d;
    }

    /**
     * 将Double转百分比格式的字符串输出
     *
     * @param d   Double
     * @param len 精确位数
     * @return String 百分比格式的字符串
     */
    public static String doubleToString(Double d, int len) {
        if (null == d) d = 0D;
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumIntegerDigits(3);// 小数点前面最多显示几位的
        nf.setMaximumFractionDigits(len);// 小数点后面最多显示几位
        nf.setMinimumFractionDigits(len);
        return nf.format(d);
    }

    /**
     * Double对象类型格式化处理
     *
     * @param d Double
     * @return double　格式化后的double
     */
    public static double formatDouble(Double d) {
        if (null == d) return 0;
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(CURRENCY_SCALE_NUM);
        String result = nf.format(d);
        return Double.parseDouble(result);
    }

    /**
     * Double对象类型格式化处理
     *
     * @param d   Double
     * @param len 小数点后精确的位数
     * @return double　格式化后的double
     */
    public static double formatDouble(Double d, int len) {
        if (null == d) return 0;
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(len);
        String result = nf.format(d);
        return Double.parseDouble(result);
    }

    /**
     * Double对象类型格式化处理(向零方向舍入)
     *
     * @param d   需要格式化的Double对象
     * @param len 小数点后精确的位数
     * @return
     * @author 潘健雷
     */
    public static double formatDoubleDown(String d, int len) {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(len, BigDecimal.ROUND_DOWN);
        return bd.doubleValue();
    }

    /**
     * 默认的小数格式化方法(保留两位小数，向零方向舍入)
     *
     * @param d 需要格式化的Double对象
     * @return
     * @author 潘健雷
     */
    public static double formatDoubleDefault(Double d) {
        return formatDoubleDefault(String.valueOf(d));
    }

    /**
     * 默认的小数格式化方法(保留两位小数，向零方向舍入)
     *
     * @param d 需要格式化的Double对象
     * @return
     * @author 潘健雷
     */
    public static double formatDoubleDefault(String d) {
        return formatDoubleDown(d, CURRENCY_SCALE_NUM);
    }

    /**
     * BigDecimal对象类型格式化处理(向零方向舍入)
     *
     * @param bd  需要格式化的BigDecimal对象
     * @param len 小数点后精确的位数
     * @return
     * @author 潘健雷
     */
    public static BigDecimal formatDown(BigDecimal bd, int len) {
        bd = bd.setScale(len, BigDecimal.ROUND_DOWN);
        return bd;
    }

    /**
     * 小数格式化方法(保留两位小数，向零方向舍入)
     *
     * @param bd 需要格式化的BigDecimal对象
     * @return
     * @author 潘健雷
     */
    public static BigDecimal formatDefault(BigDecimal bd) {
        return formatDown(bd, CURRENCY_SCALE_NUM);
    }

    /**
     * 转换成BigDecimal
     *
     * @param obj
     * @return
     * @author 潘健雷
     */
    public static BigDecimal newBigDecimal(Object obj) {
        return new BigDecimal(String.valueOf(obj == null ? 0 : obj));
    }

    /**
     * Double转字符串
     *
     * @param d        Double
     * @param defValue d为null时的默认值
     * @return String String
     */
    public static String doubleToString(Double d, String defValue) {
        String result = defValue;
        if (null != d) result = Double.toString(d);
        return result;
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的加法运算。 适合大金额的运算
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static BigDecimal add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2);
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。   此个可以进行大额的金额运算
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static BigDecimal sub(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2);
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static BigDecimal round(BigDecimal v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(v.toString());
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 默认的金额计算，小数点后四舍五入保留留两位
     *
     * @param bigDecimal 需要保留两位小数的的货币金额
     * @return 保留两位的金额数字
     * @author Playguy
     */
    public static BigDecimal roundCurrency(BigDecimal bigDecimal) {
        Preconditions.checkNotNull(bigDecimal);
        return bigDecimal.setScale(NumericUtils.CURRENCY_SCALE_NUM, RoundingMode.HALF_UP);
    }

    /**
     * 整数null转换为零
     *
     * @param integer Integer
     * @return int 转换后的整数
     */
    public static int nullToZero(Integer integer) {
        return nullToDefault(integer, 0);
    }

    /**
     * 整数null转换为指定的整数值
     *
     * @param integer  Integer
     * @param idefault 指定的整数值，为null时返回该值
     * @return int 转换后的整数值
     */
    public static int nullToDefault(Integer integer, int idefault) {
        int iResult = idefault;
        if (null != integer) iResult = integer.intValue();
        return iResult;
    }

    /**
     * 加法运算(值可为空)
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     * @author Playguy
     */
    public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
        BigDecimal b1 = (null == v1 ? BigDecimal.ZERO : v1);
        BigDecimal b2 = (null == v2 ? BigDecimal.ZERO : v2);
        return b1.add(b2);
    }

    /**
     * 加法运算(值可为空)
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     * @author Playguy
     */
    public static Double add(Double v1, Double v2) {
        Double d1 = (null == v1 ? 0 : v1);
        Double d2 = (null == v2 ? 0 : v2);
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.add(b2).doubleValue();
        // return b1.doubleValue() + b2.doubleValue();
    }

    /**
     * 加法运算(值可为空)
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     * @author Playguy
     */
    public static Integer add(Integer v1, Integer v2) {
        Integer b1 = (null == v1 ? 0 : v1);
        Integer b2 = (null == v2 ? 0 : v2);
        return b1.intValue() + b2.intValue();
    }

    /**
     * 加法运算(值可为空)
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     * @author Playguy
     */
    public static BigDecimal sub(BigDecimal v1, BigDecimal v2) {
        BigDecimal b1 = (null == v1 ? BigDecimal.ZERO : v1);
        BigDecimal b2 = (null == v2 ? BigDecimal.ZERO : v2);
        return b1.subtract(b2);
    }

    /**
     * 加法运算(值可为空)
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     * @author Playguy
     */
    public static Double sub(Double v1, Double v2) {
        Double d1 = (null == v1 ? 0 : v1);
        Double d2 = (null == v2 ? 0 : v2);
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 加法运算(值可为空)
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     * @author Playguy
     */
    public static Integer sub(Integer v1, Integer v2) {
        Integer b1 = (null == v1 ? 0 : v1);
        Integer b2 = (null == v2 ? 0 : v2);
        return b1.intValue() - b2.intValue();
    }

    /**
     * 取中位数
     *
     * @param decimals
     * @return
     */
    public static BigDecimal getMedian(BigDecimal... decimals) {
        return getMedian(Lists.newArrayList(decimals));
    }

    /**
     * 取中位数
     *
     * @param arr
     * @return
     */
    public static BigDecimal getMedian(List<BigDecimal> arr) {
        Collections.sort(arr);
        BigDecimal num;
        if (arr.size() % 2 == 0) {
            if (arr.size() == 2) {// 若是只有两个数时直接相加除2
                num = (arr.get(0).add(arr.get(1))).divide(BigDecimal.valueOf(2));
            } else {
                num = (arr.get(arr.size() / 2).add(arr.get(arr.size() / 2 + 1))).divide(BigDecimal.valueOf(2));
            }
        } else {
            num = arr.get(arr.size() / 2);
        }
        return num;
    }

    /**
     * 取中位数
     *
     * @param decimals
     * @return
     */
    public static Double getMedian(Double... decimals) {
        List<Double> arr = Lists.newArrayList(decimals);
        Collections.sort(arr);
        Double num;
        if (arr.size() % 2 == 0) {
            if (arr.size() == 2) {// 若是只有两个数时直接相加除2
                num = (arr.get(0) + arr.get(1)) / 2;
            } else {
                num = (arr.get(arr.size() / 2) + arr.get(arr.size() / 2 + 1)) / 2;
            }
        } else {
            num = arr.get(arr.size() / 2);
        }
        return num;
    }

    /**
     * 判断对象是否偏移超出中位数的10%，如果超出则按10%计算
     * <p>
     * 例：
     * a)中位数是100，对象是120，返回值是：100+100*0.1 = 110;
     * b)中位数是100，对象是 70，返回值是：100-100*0.1 = 90;
     * </p>
     *
     * @param median
     * @param object
     * @return
     */
    public static Double getValue(Double median, Double object) {
        double value = object;
        if (object > median) {
            if (object >= median * 0.1 + median) {
                value = median * 0.1 + median;
            }
        } else if (object < median) {
            if (object <= median - median * 0.1) {
                value = median - median * 0.1;
            }
        }
        return value;
    }

    /**
     * 校验BigDecimal小数位数
     *
     * @param key       参数名
     * @param keyValue  参数值
     * @param digitsNum 小数位数
     * @throws BusinessException
     */
    public static void checkDecimalDigits(String key, BigDecimal keyValue, int digitsNum) throws BusinessException {
        if (keyValue == null) {
            logger.debug(key + "参数错误：空值");
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
        } else {
            if (keyValue.setScale(digitsNum, BigDecimal.ROUND_DOWN).doubleValue() < keyValue.doubleValue()) {
                logger.debug(key + "位数超过" + digitsNum + "位");
                throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
            } else {
                logger.debug(key + "位数没超过" + digitsNum + "位");
            }
        }
    }
}

/*
 * @(#)ValidateUtils.java 2014-2-26 上午11:12:35
 * Copyright 2014 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.utils;

import com.blocain.bitms.tools.consts.DateConst;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.commons.validator.routines.CalendarValidator;
import org.apache.oro.text.perl.Perl5Util;

import java.math.BigDecimal;
import java.util.Locale;

/**
 * <p>File：ValidateUtils.java</p>
 * <p>Title: 服务器端验证通用处理类</p>
 * <p>Description:支持中文，全角字符长度计数</p>
 * <p>Copyright: Copyright (c) 2014 2014-2-26 上午11:12:35</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class ValidateUtils
{
    // 密码验证正则表达式
    public static final String REGULAR_PASSWORD   = "^(?=.*[0-9])(?=.*[A-Z])([^ ]{8,})$";
    
    // url地址验证正则表达式
    public static final String REGULAR_URL        = "^(http|https|ftp)://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?$";
    
    // IP地址验证正则表达式
    public static final String REGULAR_IP_ADDRESS = "^(((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?))?$";
    
    // 身份证验证正则表达式
    public static final String REGULAR_ID_CARD    = "^([1-9][0-9]{14}|[1-9][0-9]{17}|[1-9][0-9]{16}[x,X])?$";
    
    // 邮政编码验证正则表达式
    public static final String REGULAR_ZIP_CODE   = "^([1-9][0-9]{5})?$";
    
    // 电话验证正则表达式
    public static final String REGULAR_PHONE      = "^([0-9]{3,4}-[0-9]{7,8}(-[0-9]{2,6})?)?$";
    
    // 邮箱验证正则表达式
    public static final String REGULAR_EMIAL      = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$";
    
    // 手机验证正则表达式
    public static final String REGULAR_MOBILE     = "^(1[3-8]+\\d{9})?$";
    
    // 金额验证正则表达式
    public static final String REGULAR_MONEY      = "^((([1-9]{1}\\d{0,9})|([0]{1}))(\\.(\\d){1,2})?)?$";
    
    // email 地址最大长度
    public static final int    MAX_EMAIL_LENGTH   = 320;
    
    // 手机号码长度
    public static final int    MOBILE_LENGTH      = 11;
    
    // 密码最小长度
    public static final int    MIN_PWD_LENGTH     = 6;
    
    // 密码最大长度
    private static final int   MAX_PWD_LENGTH     = 20;
    
    // 重量验证正则表达式
    public static final String PRODUCT_WIGHT      = "^((([1-9]{1}\\d{0,9})|([0]{1}))(\\.(\\d){1,3})?)?$";
    
    /**
     * 私有构造器，防止类的实例化
     */
    private ValidateUtils()
    {
        super();
    }
    
    /**
     * 判断一个字符串是否为Null或""或"  "
     * @param checkString 要检查的字符串
     * @return boolean 是否为null或""或"  "
     */
    public static boolean isNull(String checkString)
    {
        return StringUtils.isBlank(checkString);
    }
    
    /**
     * 检查一个字符串的内容长度（一个中文等于一个字符）
     * @param checkString 要检查的字符串
     * @return int 字符串内容长度
     */
    public static int length(String checkString)
    {
        int length = 0;
        if (!isNull(checkString)) length = checkString.length();
        return length;
    }
    
    /**
     * 获取一个字符串的长度（一个中文等于两个字符）
     * @param checkString 指定的字符串
     * @return int 字符串长度
     */
    public static int size(String checkString)
    {
        int valueLength = 0;
        if (StringUtils.isNotBlank(checkString))
        {
            String chinese = "[\u0391-\uFFE5]";
            /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
            for (int i = 0; i < checkString.length(); i++)
            {
                /* 获取一个字符 */
                String temp = checkString.substring(i, i + 1);
                /* 判断是否为中文字符 */
                if (temp.matches(chinese))
                {
                    /* 中文字符长度为2 */
                    valueLength += 2;
                }
                else
                {
                    /* 其他字符长度为1 */
                    valueLength += 1;
                }
            }
        }
        return valueLength;
    }
    
    /**
     * 检查一个字符串的内容是否包含中文字
     * @param checkString 要检查的字符串
     * @return boolean 是否包含中文字
     */
    public static boolean isChinese(String checkString)
    {
        if (isNull(checkString)) return false;
        for (int i = 0; i < checkString.length(); i++)
        {
            if ((int) checkString.charAt(i) > 256) return true;
        }
        return false;
    }
    
    /**
     * 检查一个字符串的内容是否包含系统不允许的特殊字符<主要是防止javascript处理出错>
     * @param checkString 要检查的字符串
     * @return boolean 是否包含系统不允许的特殊字符
     */
    public static boolean hasBadChar(String checkString)
    {
        String szExp = "'\\/?\"<>|";
        if (isNull(checkString) || isNull(szExp)) { return false; }
        for (int counter = 0; counter < szExp.length(); counter++)
        {
            char curr_char = szExp.charAt(counter);
            if (checkString.indexOf(curr_char) >= 0) { return true; }
        }
        return false;
    }
    
    /**
     * 检查一个字符串是否为指定的帐号格式
     * @param checkString 要检查的字符串
     * @param isRequired 是否必须输入
     * @param min 最小长度
     * @param max 最大长度
     * @return boolean 是否为指定的帐号格式
     */
    public static boolean isAccountFormat(String checkString, boolean isRequired, int min, int max)
    {
        boolean bool = true;
        boolean boolNull = isNull(checkString);
        if (isRequired) bool = !boolNull;
        if (!boolNull)
        {
            String regex = "^([0-9A-Za-z_.@-]{" + min + "," + max + "})?$";// 该表达式允许字符串为空
            bool = matchRegexp(checkString, regex);
        }
        return bool;
    }
    
    /**
     * 检查一个字符串是否为6-20位字符长度的帐号格式
     * @param checkString 要检查的字符串
     * @param isRequired 是否必须
     * @return boolean 是否为6-20位字符长度的帐号格式
     */
    public static boolean isAccountFormat(String checkString, boolean isRequired)
    {
        return isAccountFormat(checkString, isRequired, MIN_PWD_LENGTH, MAX_PWD_LENGTH);
    }
    
    /**
     * 检查一个字符串是否为正确的邮件格式(支持一个邮件地址或多个以;号隔开的邮件地址验证)
     * @param checkString 要查检的字符串
     * @param isRequired 是否必须
     * @param maxLength 最大长度
     * @return boolean 是否为正确的邮件格式
     */
    public static boolean isMailFormat(String checkString, boolean isRequired, int maxLength)
    {
        boolean flag = validate(checkString, isRequired, maxLength, REGULAR_EMIAL);
        if (flag)
        {// 防止线数字的邮件地址处理
            if (isNumber(checkString.substring(0, checkString.indexOf("@")))) { return false; }
        }
        return flag;
    }
    
    /**
     * 判断一个字符串是否匹配一个正则表达式
     * @param value 要判断的字符串
     * @param regexp 要匹配的正则表达式
     * @return boolean 是否匹配
     */
    public static boolean matchRegexp(String value, String regexp)
    {
        if (regexp == null || regexp.length() <= 0) { return false; }
        Perl5Util matcher = new Perl5Util();
        return matcher.match("/" + regexp + "/", value);
    }
    
    /**
     * 检查一个字符串是否为正确的电话格式(支持一个电话号或多个以;号隔开的电话号验证)
     * @param checkString 要检查的字符串
     * @param isRequired 是否必须
     * @param maxLength 最大长度
     * @return boolean 是否为正确的电话格式
     */
    public static boolean isTelFormat(String checkString, boolean isRequired, int maxLength)
    {
        return validate(checkString, isRequired, maxLength, REGULAR_PHONE);
    }
    
    /**
     * 判断一个字符串是否正确的日期格式
     * @param value 要检查的字符串
     * @param locale java.util.Locale
     * @return boolean 是否正确的日期格式
     */
    public static boolean isDate(String value, Locale locale)
    {
        return CalendarValidator.getInstance().isValid(value, locale);
    }
    
    /**
     * 判断一个字符串是否为正确的日期及格式
     * @param value 要检查的字符串
     * @param datePattern 日期格式
     * @return boolean 是否为正确的日期及格式
     */
    public static boolean isDate(String value, String datePattern)
    {
        return CalendarValidator.getInstance().isValid(value, datePattern);
    }
    
    public static boolean isTimeFormat(String checkString, String timeFormat, boolean isRequired)
    {
        return false;
    }
    
    /**
     * 检查一个字符串是否为正确有效的日期格式字符串
     * @param checkString 要检查的字符串
     * @param dateFormat 指定的日期格式，如：yyyy-MM-dd 或 yyyy-MM
     * @param isRequired 是否必须
     * @return boolean 是否为正确有效的日期格式字符串
     */
    public static boolean isDateFormat(String checkString, String dateFormat, boolean isRequired)
    {
        boolean boolNull = isNull(checkString);
        if (isRequired)
        {
            if (boolNull) return false;
        }
        if (!boolNull)
        {
            if (!isDate(checkString, dateFormat)) { return false; }
            if (dateFormat.equalsIgnoreCase(DateConst.DATE_FORMAT_YM)) { return matchRegexp(checkString, "^([0-9]{4}-[0-9]{1,2})?$"); }
            if (dateFormat.equalsIgnoreCase(DateConst.DATE_FORMAT_YMD)) { return matchRegexp(checkString, "^([0-9]{4}-[0-9]{1,2}-[0-9]{1,2})?$"); }
        }
        return true;
    }
    
    /**
     * 判断一个字符串是否为Byte类型的数据
     * @param value 要检查的字符串
     * @return boolean 是否为Byte类型的数据
     */
    public static boolean isByte(String value)
    {
        return (GenericTypeValidator.formatByte(value) != null);
    }
    
    /**
     * 判断一个字符串是否为Short类型的数据
     * @param value 要检查的字符串
     * @return boolean 是否为Short类型的数据
     */
    public static boolean isShort(String value)
    {
        return (GenericTypeValidator.formatShort(value) != null);
    }
    
    /**
     * 判断一个字符串是否为Int类型的数据
     * @param value 要检查的字符串
     * @return boolean 是否为Int类型的数据
     */
    public static boolean isInt(String value)
    {
        return (GenericTypeValidator.formatInt(value) != null);
    }
    
    /**
     * 判断一个字符串是否为Long类型的数据
     * @param value 要检查的字符串
     * @return boolean 是否为Long类型的数据
     */
    public static boolean isLong(String value)
    {
        return (GenericTypeValidator.formatLong(value) != null);
    }
    
    /**
     * 判断一个字符串是否为Float类型的数据
     * @param value 要检查的字符串
     * @return boolean 是否为Float类型的数据
     */
    public static boolean isFloat(String value)
    {
        return (GenericTypeValidator.formatFloat(value) != null);
    }
    
    /**
     * 判断一个字符串是否为Double类型的数据
     * @param value 要检查的字符串
     * @return boolean 是否为Double类型的数据
     */
    public static boolean isDouble(String value)
    {
        return (GenericTypeValidator.formatDouble(value) != null);
    }
    
    /**
     * 验证整数范围
     * @param value 要验证的整数
     * @param min 最小值
     * @param max 最大值
     * @return boolean 是否在指定的整数范围之内
     */
    public static boolean isInRange(int value, int min, int max)
    {
        return ((value >= min) && (value <= max));
    }
    
    /**
     * 验证整数范围
     * @param checkInt  要验证的整数
     * @param min       最小值
     * @param max       最大值
     * @param isRequiredd 是否必须
     * @return boolean 是否在指定的整数范围之内
     */
    public static boolean isIntRange(int checkInt, int min, int max, boolean isRequiredd)
    {
        if (!isRequiredd)
        {
            if (checkInt == 0) return true;
        }
        return isInRange(checkInt, min, max);
    }
    
    /**
     * 验证邮政编码 6位(支持验证多个的功能)
     * @param checkString 要检查的字符串
     * @param isRequired 是否必填
     * @param maxLength 最大长度
     * @return boolean 是否正确的邮编格式
     */
    public static boolean isZipCode(String checkString, boolean isRequired, int maxLength)
    {
        return validate(checkString, isRequired, maxLength, REGULAR_ZIP_CODE);
    }
    
    /**
     * 验证身份证号码 15位或18位(支持验证多个的功能)
     * @param checkString 要检查的字符串
     * @param isRequired 是否必填
     * @param maxLength 最大长度
     * @return boolean 是否正确的身份证号码格式
     */
    public static boolean isIDCard(String checkString, boolean isRequired, int maxLength)
    {
        return validate(checkString, isRequired, maxLength, REGULAR_ID_CARD);
    }
    
    /**
     * 手机号码格式验证(支持验证多个的功能)
     * 
     * @param checkString 要检查的字符串
     * @param isRequired 是否必填
     * @param maxLength 最大长度
     * @return boolean 是否正确的手机号码格式
     */
    public static boolean isMobileFormat(String checkString, boolean isRequired, int maxLength)
    {
        // 正则表达式参考 @link http://my.oschina.net/william1/blog/4752
        return validate(checkString, isRequired, maxLength, REGULAR_MOBILE);
    }
    
    /**
     * 检查一个字符串是否为正确的IP地址格式(支持验证多个的功能)
     * @param checkString 要检查的字符串
     * @param isRequired 是否必填
     * @param maxLength 最大长度
     * @return boolean 是否正确的IP地址格式
     */
    public static boolean isIpAddress(String checkString, boolean isRequired, int maxLength)
    {
        return validate(checkString, isRequired, maxLength, REGULAR_IP_ADDRESS);
    }
    
    /**
     * 检查一个字符串是否为正确的URL地址格式(支持验证多个的功能)
     * @param checkString 要检查的字符串
     * @param isRequired 是否必填
     * @param maxLength 最大长度
     * @return boolean 是否正确的URL地址格式
     */
    public static boolean isUrl(String checkString, boolean isRequired, int maxLength)
    {
        return validate(checkString, isRequired, maxLength, REGULAR_URL);
    }
    
    /**
     * 抽象验证，正则匹配
     * @param checkString 要检查的字符串
     * @param isRequired 是否必填
     * @param maxLength 最大长度
     * @param regex 正则表达式
     * @param splitChar 支持多个字符串验证，该参数表示字符串之间的分隔符
     * @return boolean 是否通过验证
     */
    public static boolean validate(String checkString, boolean isRequired, int maxLength, String regex, String splitChar)
    {
        checkString = StringUtils.trimToEmpty(LanguageUtils.quanToBan(checkString));
        boolean bool = isRange(checkString, isRequired, maxLength);
        boolean boolNull = isNull(checkString);
        if (checkString.indexOf(splitChar) == -1)
        {
            if (bool && !boolNull) bool = matchRegexp(checkString, regex);
            return bool;
        }
        else
        {
            if (bool && !boolNull)
            {
                String[] strs = checkString.split(splitChar);
                for (int i = 0; i < strs.length; i++)
                {
                    String str = strs[i];
                    if (!matchRegexp(str, regex))
                    {
                        bool = false;
                        break;
                    }
                }
            }
            return bool;
        }
    }
    
    /**
     * 抽象验证，正则匹配
     * @param checkString 要检查的字符串
     * @param isRequired 是否必填
     * @param maxLength 最大长度
     * @param regex 正则表达式
     * @return boolean 是否通过验证
     */
    public static boolean validate(String checkString, boolean isRequired, int maxLength, String regex)
    {
        checkString = StringUtils.trimToEmpty(LanguageUtils.quanToBan(checkString));
        boolean bool = isRange(checkString, isRequired, maxLength);
        boolean boolNull = isNull(checkString);
        if (checkString.indexOf(";") == -1)
        {
            if (bool && !boolNull) bool = matchRegexp(checkString, regex);
            return bool;
        }
        else
        {
            if (bool && !boolNull)
            {
                String[] strs = checkString.split(";");
                for (int i = 0; i < strs.length; i++)
                {
                    String str = strs[i];
                    if (!matchRegexp(str, regex))
                    {
                        bool = false;
                        break;
                    }
                }
            }
            return bool;
        }
    }
    
    /**
     * 普通字符串验证，检查字符串内容长度是否在指定范围之内
     * @param checkString 要检查的字符串
     * @param min 最小长度
     * @param max 最大长度
     * @param isRequired 是否必须
     * @return boolean 是否通过验证
     */
    public static boolean isRange(String checkString, int min, int max, boolean isRequired)
    {
        boolean bool = true;
        boolean boolNull = isNull(checkString);
        if (isRequired) bool = !boolNull;
        if (!boolNull)
        {
            int iLen = length(checkString);
            bool = (iLen >= min) && (iLen <= max);
        }
        return bool;
    }
    
    /**
     * 用于密码校验
     * @param checkString
     * @param min
     * @param max
     * @param isRequired
     * @return
     */
    public static boolean isRegex(String checkString, int min, int max, boolean isRequired)
    {
        boolean bool = true;
        boolean boolNull = isNull(checkString);
        if (isRequired) bool = !boolNull;
        if (!boolNull)
        {
            int iLen = length(checkString);
            bool = (iLen >= min) && (iLen <= max) && matchRegexp(checkString, REGULAR_PASSWORD);
        }
        return bool;
    }
    
    /**
     * 普通字符串验证，检查字符串内容长度是否在指定范围之内
     * @param checkString 要检查的字符串
     * @param max 最大长度
     * @param isRequired 是否必须
     * @return boolean 是否通过验证
     */
    public static boolean isRange(String checkString, boolean isRequired, int max)
    {
        boolean bool = true;
        boolean boolNull = isNull(checkString);
        if (isRequired) bool = !boolNull;
        if (!boolNull) bool = length(checkString) <= max;
        return bool;
    }
    
    /**
     * 普通字符串验证，检查字符串内容长度是否在指定范围之内(中文两个字节计算)
     * @param checkString   要检查的字符串
     * @param isRequired    是否必须
     * @param max           最大长度
     * @return
     * @author 施建波
     */
    public static boolean isChinaRange(String checkString, boolean isRequired, int max)
    {
        boolean bool = true;
        boolean boolNull = isNull(checkString);
        if (isRequired) bool = !boolNull;
        if (!boolNull) bool = size(checkString) <= max;
        return bool;
    }
    
    /**
     * 验证是否为金额，如果是是否在指定的范围内
     * @param price     金额
     * @param min       最小值　
     * @param max       最大值
     * @author 施建波
     */
    public static Boolean isMoney(BigDecimal price, BigDecimal min, BigDecimal max)
    {
        boolean bool = true;
        bool = (null != price);
        if (bool)
        {
            bool = matchRegexp(price.toString(), REGULAR_MONEY);
        }
        if (bool && null != min)
        {
            bool = (price.compareTo(min) > 0);
        }
        if (bool && null != max)
        {
            bool = (price.compareTo(max) < 0);
        }
        return bool;
    }
    
    /**
     * 验证是否为金额，如果是是否在指定的范围内
     * @param price     金额
     * @param min       最小值　
     * @param max       最大值
     */
    public static Boolean isPrice(BigDecimal price, BigDecimal min, BigDecimal max)
    {
        boolean bool = true;
        bool = (null != price);
        if (bool)
        {
            bool = matchRegexp(price.toString(), REGULAR_MONEY);
        }
        if (bool && null != min)
        {
            bool = (price.compareTo(min) >= 0);
        }
        if (bool && null != max)
        {
            bool = (price.compareTo(max) < 0);
        }
        return bool;
    }
    
    public static Boolean isWight(Double wight, Double min, Double max)
    {
        boolean bool = true;
        bool = (null != wight);
        if (bool)
        {
            bool = matchRegexp(wight.toString(), PRODUCT_WIGHT);
        }
        if (bool && null != min)
        {
            bool = (wight.doubleValue() > min.doubleValue());
        }
        if (bool && null != max)
        {
            bool = (wight.doubleValue() < max.doubleValue());
        }
        return bool;
    }
    
    /**普通字符串验证，检查字符串内容是否为全数字
     * @param checkString
     * @return
     */
    public static boolean isNumber(String checkString)
    {
        return StringUtils.isNumeric(checkString);
    }
    
    /**
     * 加密手机号或者用户名
     * @return
     */
    public static String getSecrectStr(String str)
    {
        String secrectStr = "";
        if (StringUtils.isNotBlank(str))
        {
            if (isMobileFormat(str, false, 11))
            {
                secrectStr = str.substring(0, 3) + "****" + str.substring(7, str.length());
            }
            else
            {
                secrectStr = str.substring(0, 1) + "**";
            }
        }
        return secrectStr;
    }
}
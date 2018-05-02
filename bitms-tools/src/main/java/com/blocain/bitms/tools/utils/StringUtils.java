/*
 * @(#)Dialect.java 2015-4-17 下午3:29:26
 * Copyright 2015 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CharsetConst;
import com.google.common.base.Preconditions;

/**
 * <p>File：StringUtils.java</p>
 * <p>Title: StringUtils</p>
 * <p>Description:StringUtils</p>
 * <p>Copyright: Copyright (c) 2015/04/18 11:29</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils
{
    private static final String DFT_ELLIPSIS = "...";
    
    /**
     * 模糊手机号，取后几位
     * @param mobile
     * @return
     */
    public static String vagueMobile(String mobile)
    {
        if (StringUtils.isBlank(mobile)) return null;
        return "**" + mobile.substring(mobile.length() - 4, mobile.length());
    }
    
    /**
     * 转换字节
     * @param bytes
     * @param charsetName
     * @return
     */
    public static String newString(byte[] bytes, String charsetName)
    {
        if (bytes == null)
        {
            return null;
        }
        else
        {
            try
            {
                return new String(bytes, charsetName);
            }
            catch (UnsupportedEncodingException ex)
            {
                throw new RuntimeException(ex);
            }
        }
    }
    
    /**
     * 转换为字节数组
     *
     * @param str
     * @return
     */
    public static byte[] getBytes(String str)
    {
        if (str != null)
        {
            try
            {
                return str.getBytes(CharsetConst.CHARSET_UT);
            }
            catch (UnsupportedEncodingException e)
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 隐藏电话号码
     * @param number
     * @return
     */
    public static final String hideTelNumber(String number)
    {
        if (org.apache.commons.lang3.StringUtils.isEmpty(number)) { return ""; }
        if (number.length() < 5) { return "****" + number.substring(number.length() - 2); }
        return number.substring(0, number.length() - 6) + "****" + number.substring(number.length() - 2);
    }
    
    /**
     * 隐藏文本
     * @param alpha
     * @return
     */
    public static final String hideAlphaNumber(String alpha)
    {
        if (org.apache.commons.lang3.StringUtils.isEmpty(alpha)) { return ""; }
        if (alpha.length() < 6) { return alpha + "*******"; }
        return alpha.substring(0, alpha.length() - 6) + "******";
    }
    
    /**
     * URL 编码
     * @param str
     * @return
     * @author Playguy
     */
    public static String urlEncoding(String str)
    {
        if (org.apache.commons.lang3.StringUtils.isBlank(str)) { return ""; }
        try
        {
            return URLEncoder.encode(str, CharsetConst.CHARSET_UT);
        }
        catch (UnsupportedEncodingException e)
        {
            return "";
        }
    }
    
    /**
     * 去除字符串中的html元素
     * @param str
     * @return
     * @author Playguy
     */
    public static String cleanHtmlElems(String str)
    {
        return Jsoup.clean(str, Whitelist.none());
    }
    
    /**
     * 比较字符是否在字符串数组中，如果存在则返回数姐中的位置
     * @param strAry    字符串数组
     * @param value     需要验证的数组
     * @return
     * @author 施建波
     */
    public static final Integer strArraySearch(String[] strAry, String value)
    {
        if (!ArrayUtils.isEmpty(strAry))
        {
            for (int i = 0; i < strAry.length; i++)
            {
                if (strAry[i].equals(value)) { return i; }
            }
        }
        return -1;
    }
    
    /**
     * 转换为字节数组
     *
     * @param bytes
     * @return {@link String}
     */
    public static String toString(byte[] bytes)
    {
        try
        {
            return new String(bytes, CharsetConst.CHARSET_UT);
        }
        catch (UnsupportedEncodingException e)
        {
            return EMPTY;
        }
    }
    
    /**
     * 超长字符串截断
     *
     * @param origText 原始字符串
     * @param length   限定长度
     * @return
     */
    public static final String trimLongText(String origText, int length)
    {
        return trimLongText(origText, length, DFT_ELLIPSIS);
    }
    
    /**
     * 超长字符串截断
     *
     * @param orgiText    原始字符串
     * @param length      限定长度
     * @param ellipsisStr 省略字符 不能为null
     * @return
     */
    public static final String trimLongText(String orgiText, int length, String ellipsisStr)
    {
        Preconditions.checkNotNull(ellipsisStr);
        String reValue = null;
        if (orgiText != null)
        {
            int len = countChineseStringLength(orgiText);
            if (len / 2 > length)
            {
                reValue = orgiText.substring(0, length) + ellipsisStr;
            }
            else
            {
                reValue = orgiText;
            }
        }
        return reValue;
    }
    
    /**
     * 统计字符串长度，一个中文算两个字符长度，英文算一个，中文标点算两个
     *
     * @param string
     * @return
     */
    public static int countChineseStringLength(String string)
    {
        int count = 0;
        char[] chars = string.toCharArray();
        for (char c : chars)
        {
            if (isChineseChar(c))
            {
                count += 2;
            }
            else
            {
                count += 1;
            }
        }
        return count;
    }
    
    /**
     * 检测是否是中文字符，字符编码必须是UTF-8
     *
     * @param c
     * @return
     */
    public static boolean isChineseChar(char c)
    {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) { return true; }
        return false;
    }
    
    /**
     * 替换前num位的字符
     *
     * @param sourceStr  原字符串
     * @param num        前num位
     * @param replaceStr 替换的符号
     * @return
     * @author Playguy
     */
    public static String changeString(String sourceStr, int num, String replaceStr)
    {
        if (null == sourceStr || "".equals(sourceStr)) { return ""; }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < sourceStr.length(); i++)
        {
            if (i < num)
            {
                sb.append(sourceStr.charAt(i));
            }
            else
            {
                sb.append("*");
            }
        }
        return sb.toString();
    }
    
    /**
     * 是否包含字符串
     *
     * @param str  验证字符串
     * @param strs 字符串组
     * @return {@link Boolean}
     */
    public static Boolean inString(String str, String ... strs)
    {
        if (str != null)
        {
            for (String s : strs)
            {
                if (str.equals(trim(s))) { return Boolean.TRUE; }
            }
        }
        return Boolean.FALSE;
    }
    
    /**
     * 替换掉HTML标签方法
     *
     * @param html
     * @return {@link String}
     */
    public static String replaceHtml(String html)
    {
        if (isBlank(html)) { return ""; }
        String regEx = "<.+?>";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(html);
        String s = m.replaceAll("");
        return s;
    }
    
    public static String xssEncode(String s)
    {
        if (s == null || "".equals(s)) { return s; }
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            switch (c)
            {
                case '>':
                    sb.append('＞');// 全角大于号
                    break;
                case '<':
                    sb.append('＜');// 全角小于号
                    break;
                case '\'':
                    sb.append('‘');// 全角单引号
                    break;
                case '\"':
                    sb.append('“');// 全角双引号
                    break;
                case '&':
                    sb.append('＆');// 全角
                    break;
                case '\\':
                    sb.append('＼');// 全角斜线
                    break;
                case '#':
                    sb.append('＃');// 全角井号
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }
    
    /**
     * 替换为手机识别的HTML，去掉样式及属性，保留回车。
     *
     * @param html
     * @return {@link String}
     */
    public static String replaceMobileHtml(String html)
    {
        if (html == null) { return ""; }
        return html.replaceAll("<([a-z]+?)\\s+?.*?>", "<$1>");
    }
    
    /**
     * 替换为手机识别的HTML，去掉样式及属性，保留回车。
     *
     * @param txt
     * @return {@link String}
     */
    public static String toHtml(String txt)
    {
        if (txt == null) { return ""; }
        return replace(replace(EncodeUtils.escapeHtml(txt), "\n", "<br/>"), "\t", "&nbsp; &nbsp; ");
    }
    
    /**
     * 缩略字符串（不区分中英文字符）
     *
     * @param str    目标字符串
     * @param length 截取长度
     * @return {@link String}
     */
    public static String abbr(String str, int length)
    {
        if (str == null) { return ""; }
        try
        {
            StringBuilder sb = new StringBuilder();
            int currentLength = 0;
            for (char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray())
            {
                currentLength += String.valueOf(c).getBytes("GBK").length;
                if (currentLength <= length - 3)
                {
                    sb.append(c);
                }
                else
                {
                    sb.append("...");
                    break;
                }
            }
            return sb.toString();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return "";
    }
    
    /**
     * 转换为Double类型
     */
    public static Double toDouble(Object val)
    {
        if (val == null) { return 0D; }
        try
        {
            return Double.valueOf(trim(val.toString()));
        }
        catch (Exception e)
        {
            return 0D;
        }
    }
    
    /**
     * 转换为Float类型
     */
    public static Float toFloat(Object val)
    {
        return toDouble(val).floatValue();
    }
    
    /**
     * 转换为Long类型
     */
    public static Long toLong(Object val)
    {
        return toDouble(val).longValue();
    }
    
    /**
     * 转换为Integer类型
     */
    public static Integer toInteger(Object val)
    {
        return toLong(val).intValue();
    }
    
    /**
     * 获得用户远程地址
     */
    public static String getRemoteAddr(HttpServletRequest request)
    {
        String remoteAddr = request.getHeader("X-Real-IP");
        if (isNotBlank(remoteAddr))
        {
            remoteAddr = request.getHeader("X-Forwarded-For");
        }
        else if (isNotBlank(remoteAddr))
        {
            remoteAddr = request.getHeader("Proxy-Client-IP");
        }
        else if (isNotBlank(remoteAddr))
        {
            remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
    }
    
    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase("hello_world") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCamelCase(String s)
    {
        if (s == null) { return null; }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            if (c == BitmsConst.SEPARATOR)
            {
                upperCase = true;
            }
            else if (upperCase)
            {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            }
            else
            {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase("hello_world") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCapitalizeCamelCase(String s)
    {
        if (s == null) { return null; }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
    
    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase("hello_world") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toUnderScoreCase(String s)
    {
        if (s == null) { return null; }
        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            boolean nextUpperCase = true;
            if (i < (s.length() - 1))
            {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }
            if ((i > 0) && Character.isUpperCase(c))
            {
                if (!upperCase || !nextUpperCase)
                {
                    sb.append(BitmsConst.SEPARATOR);
                }
                upperCase = true;
            }
            else
            {
                upperCase = false;
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }
    
    /**
     * 如果不为空，则设置值
     *
     * @param target
     * @param source
     */
    public static void setValueIfNotBlank(String target, String source)
    {
        if (isNotBlank(source))
        {
            target = source;
        }
    }
    
    /**
     * 如果为空，返回0
     *
     * @param target
     */
    public static String getStringNum(String target)
    {
        if (target != null && !target.equals("null"))
        {
            return target;
        }
        else
        {
            return "0";
        }
    }
    
    /**
     * 转换为JS获取对象值，生成三目运算返回结果
     *
     * @param objectString 对象串
     *                     例如：row.user.id
     *                     返回：!row?'':!row.user?'':!row.user.id?'':row.user.id
     */
    public static String jsGetVal(String objectString)
    {
        StringBuilder result = new StringBuilder();
        StringBuilder val = new StringBuilder();
        String[] vals = split(objectString, ".");
        for (int i = 0; i < vals.length; i++)
        {
            val.append("." + vals[i]);
            result.append("!" + (val.substring(1)) + "?'':");
        }
        result.append(val.substring(1));
        return result.toString();
    }
    
    public static String getListString(List<String> stringList)
    {
        int length = stringList.size();
        StringBuffer sb = new StringBuffer();
        if (length > 0)
        {
            sb.append(stringList.get(0));
        }
        for (int i = 1; i < length; i++)
        {
            sb.append(',');
            sb.append(stringList.get(i));
        }
        return sb.toString();
    }
}

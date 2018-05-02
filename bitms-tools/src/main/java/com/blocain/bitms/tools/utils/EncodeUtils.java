/*
 * @(#)EncodeUtils.java 2014-1-8 下午12:57:24
 * Copyright 2014 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.utils;

import com.blocain.bitms.tools.consts.CharsetConst;
import com.blocain.bitms.tools.exception.NoByteException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * <p>File：EncodeUtils.java</p>
 * <p>Title: 封装各种格式的编码解码工具类</p>
 * <p>Description:主要功能为Html编解码、Xml编解码、Hex编解码、URL编解码、base62/64编解码</p>
 * <p>Copyright: Copyright (c) 2014 2014-1-8 下午12:57:24</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class EncodeUtils
{
    private static final Logger logger     = LoggerFactory.getLogger(EncodeUtils.class);
    
    // 十六进制字符与数字转换表
    private static final String HEX_STRING = "0123456789ABCDEF";
    
    // 私有构造器，防止类的实例化
    public EncodeUtils()
    {
        super();
    }
    
    /**
     * 将字符串进行十六进制转码
     *
     * @param string      要进行转码的字符中
     * @param charsetName 编码格式
     * @return String 转码后的十六进制字符串
     */
    public static String stringToHex(String string, String charsetName)
    {
        String result = null;
        if (StringUtils.isNotBlank(string))
        {
            byte[] bytes = null;
            if (StringUtils.isNotBlank(charsetName))
            {
                bytes = string.getBytes(Charset.forName(charsetName));
            }
            else
            {
                bytes = string.getBytes();
            }
            result = Hex.encodeHexString(bytes).toUpperCase();
        }
        return result;
    }
    
    /**
     * 根据指定的编码将十六进制字符串转化为字符串
     *
     * @param hexString   十六进制字符串
     * @param charsetName 指定编码名称
     * @return String 转化后的字符串
     */
    public static String hexToString(String hexString, String charsetName)
    {
        String result = null;
        if (StringUtils.isNotBlank(hexString))
        {
            try
            {
                byte[] bytes = Hex.decodeHex(hexString.toCharArray());
                if (StringUtils.isNotBlank(charsetName))
                {
                    result = new String(bytes, Charset.forName(charsetName));
                }
                else
                {
                    result = new String(bytes);
                }
            }
            catch (DecoderException e)
            {
                logger.error(e.getMessage());
            }
        }
        return result;
    }
    
    /**
     * 将十六进制字符转换成数字。
     *
     * @param c 将转换的字符。
     * @return 转换成的数字。
     * @throws NoByteException
     */
    public static int stringHexToInt(char c) throws NoByteException
    {
        char cc = Character.toUpperCase(c);
        int value = HEX_STRING.indexOf(cc);
        if (value == -1) { throw new NoByteException("必须是十六进制字符"); }
        return value;
    }
    
    /**
     * 以指定的编码计算异或和
     *
     * @param str     计算异或和的字符串
     * @param charset 编码方式
     * @return String 异或校验码
     * @throws UnsupportedEncodingException
     */
    public static String getXorString(String str, String charset)
    {
        String result = null;
        byte[] bt = null;
        try
        {
            bt = str.getBytes(charset);
        }
        catch (UnsupportedEncodingException e)
        {
            LoggerUtils.logError(logger, e.getMessage());
        }
        if (null != bt)
        {
            byte checkValue = bt[0];
            for (int i = 1; i < bt.length; i++)
            {
                checkValue ^= bt[i];
            }
            // 转换成16进制字符串
            // int是32位，byte是8位，不进行&0xff时会出现高24位补位误差，产生0xffffff
            result = Integer.toHexString(checkValue & 0xFF).toUpperCase();
            if (result.length() == 1)
            {
                result = "0" + result;
            }
        }
        return result;
    }
    
    /**
     * html转码，防止XSS攻击
     *
     * @param htmlString 需要进行转码的html文本
     * @return String 转码后的html文本
     */
    public static String escapeHtml(String htmlString)
    {
        String result = null;
        if (StringUtils.isNotBlank(htmlString))
        {
            result = StringEscapeUtils.escapeHtml4(htmlString);
        }
        return result;
    }
    
    /**
     * html解码，将进行了Html转码的文本转换为html文本
     *
     * @param string 进行了Html转码的文本
     * @return String html解码后的文本
     */
    public static String unscapeHtml(String string)
    {
        String result = null;
        if (StringUtils.isNotBlank(string))
        {
            result = StringEscapeUtils.unescapeHtml4(string);
        }
        return result;
    }
    
    /**
     * 将xml字符串进行转码处理
     *
     * @param xmlString xml字符串
     * @return String 转码后的字符串
     */
    public static String escapeXml(String xmlString)
    {
        String result = null;
        if (StringUtils.isNotBlank(xmlString))
        {
            result = StringEscapeUtils.escapeXml11(xmlString);
        }
        return result;
    }
    
    /**
     * 将转码后的xml字符串进行解码处理
     *
     * @param string 转码后的xml字符串
     * @return String xml字符串
     */
    public static String unscapeXml(String string)
    {
        String result = null;
        if (StringUtils.isNotBlank(string))
        {
            result = StringEscapeUtils.unescapeXml(string);
        }
        return result;
    }
    
    /**
     * 解决XLS文件导出时，文件名乱码
     *
     * @param request
     * @param fileName
     * @return
     * @author 徐志勇
     */
    public static String fileNameEncode(HttpServletRequest request, String fileName)
    {
        String userAgent = request.getHeader("USER-AGENT");
        String finalFileName = null;
        try
        {
            if (StringUtils.contains(userAgent, "MSIE"))
            {
                finalFileName = URLEncoder.encode(fileName, "UTF8");
            }
            else if (StringUtils.contains(userAgent, "Mozilla"))
            {
                finalFileName = new String(fileName.getBytes(), "ISO8859-1");
            }
            else
            {
                finalFileName = URLEncoder.encode(fileName, "UTF8");
            }
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return finalFileName;
    }
    
    /**
     * URL 编码, Encode默认为UTF-8.
     */
    public static String urlEncode(String part)
    {
        try
        {
            return URLEncoder.encode(part, CharsetConst.CHARSET_UT);
        }
        catch (UnsupportedEncodingException e)
        {
            LoggerUtils.logError(logger, "URL编码失败：", e.getMessage());
        }
        return part;
    }
}

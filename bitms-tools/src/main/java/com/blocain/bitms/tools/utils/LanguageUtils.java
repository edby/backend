/*
 * @(#)LanguageUtils.java 2014-2-26 上午11:46:18
 * Copyright 2014 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>File：LanguageUtils.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2014 2014-2-26 上午11:46:18</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class LanguageUtils
{
    // 私有构造器
    private LanguageUtils()
    {
        super();
    }
    
    /**
     * 通过HTTP请求取语言
     * @param request
     * @return
     */
    public static String getLang(HttpServletRequest request)
    {
//        Locale locale = request.getLocale();
//        StringBuffer lang = new StringBuffer(locale.getLanguage());
//        if (StringUtils.isNotBlank(locale.getCountry()))
//        {
//            lang.append("_").append(locale.getCountry());
//        }
//        if (!StringUtils.equalsIgnoreCase("en", locale.getLanguage())//
//                && StringUtils.equalsIgnoreCase("cn", locale.getLanguage()))
//        {// 非英文和中文的时候统一返回英文编码
//            new StringBuffer("en_US");
//        }
        return "zh_CN";
    }
    
    /**  
     * 半角转全角  
     * @param input 要转换的字符串
     * @return String 转换后的字符串
     */
    public static String banToQuan(String input)
    {
        if (null == input)
        {
            return null;
        }
        else
        {
            char c[] = input.toCharArray();
            for (int i = 0; i < c.length; i++)
            {
                if (c[i] == ' ')
                {
                    c[i] = '\u3000';
                }
                else if (c[i] < '\177')
                {
                    c[i] = (char) (c[i] + 65248);
                }
            }
            return new String(c);
        }
    }
    
    /**  
     * 全角转半角 
     * @param input 要转换的字符串  
     * @return String 转换后的字符串
     */
    public static String quanToBan(String input)
    {
        if (null == input) return null;
        else
        {
            char c[] = input.toCharArray();
            for (int i = 0; i < c.length; i++)
            {
                if (c[i] == '\u3000')
                {
                    c[i] = ' ';
                }
                else if (c[i] > '\uFF00' && c[i] < '\uFF5F')
                {
                    c[i] = (char) (c[i] - 65248);
                }
            }
            String returnString = new String(c);
            return returnString;
        }
    }
}

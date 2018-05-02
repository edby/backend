/*
 * @(#)SerialNumberUtils.java 2014-1-8 下午1:07:00
 * Copyright 2014 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.utils;

import java.util.Random;
import java.util.UUID;

import com.blocain.bitms.tools.bean.IdWorker;

/**
 * <p>File：SerialNumberUtils.java</p>
 * <p>Title: 系统唯一编号生成工具类</p>
 * <p>Description:主要功能为主键、订单、支付、退款、发货、退货等编号的生成 </p>
 * <p>Copyright: Copyright (c) 2014 2014-1-8 下午1:07:00</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class SerialnoUtils
{
    static char[]   charArray = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A',
            'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    
    static String   TABLE     = "0123456789";
    
    static IdWorker idworker;
    
    // 退款
    // 私有构造器，防止类的实例化
    public SerialnoUtils()
    {
        super();
    }
    
    /**
     * 创建数据库主键
     *
     * @return String 数据库主键
     */
    public static Long buildPrimaryKey()
    {
        if (null == idworker) idworker = new IdWorker(1, 0);
        return idworker.nextId();
    }

    /**
     * 创建唯一编码
     *
     * @return String 数据库主键
     */
    public static String buildUUID()
    {
        String uuid = UUID.randomUUID().toString();
        return StringUtils.replace(uuid, "-", "").toLowerCase();
    }
    
    /**
     * 创建指定长度的随机数
     * @param length
     * @return
     */
    public static String randomNum(int length)
    {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; ++i)
        {
            int number = random.nextInt(TABLE.length());
            sb.append(TABLE.charAt(number));
        }
        return sb.toString();
    }
    
    public static boolean containsHexString(String s)
    {
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            for (int j = 0; j < charArray.length; j++)
            {
                if (c == charArray[j]) { return true; }
            }
        }
        return false;
    }
    
    public static void setIdworker(IdWorker idworker)
    {
        SerialnoUtils.idworker = idworker;
    }
}

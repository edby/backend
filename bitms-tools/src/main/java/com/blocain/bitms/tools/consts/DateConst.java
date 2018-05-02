/*
 * @(#)DateConst.java 2015-4-16 下午2:51:01
 * Copyright 2015 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.consts;

import org.apache.commons.lang3.time.DateUtils;

/**
 * <p>File：DateConst.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2015 2015-4-16 下午2:51:01</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class DateConst
{
    private DateConst()
    {
    }
    
    public static final String DATE_FORMAT_YMD          = "yyyy-MM-dd";
    
    public static final String DATE_FORMAT_YMDHMS       = "yyyy-MM-dd HH:mm:ss";
    
    public static final String DATE_FORMAT_YYMMDDHHMMSS = "yyyyMMddHHmmss";
    
    public static final String DATE_FORMAT_YM           = "YYYY-MM";
    
    public static final String DATE_FORMAT_YYYYMM       = "yyyyMM";
    
    public static final String DATE_FORMAT_YYYYMMDD     = "yyyyMMdd";
    
    public static final String DATE_FORMAT_YMD_CN       = "YYYY年MM月dd日";
    
    public static final String DATE_FORMAT_YMD_X        = "yyyy/MM/dd";
    
    public static final String DATE_FORMAT_YMDHMS_X     = "yyyy/MM/dd HH:mm:ss";
    
    public static final String DATE_FORMAT_YM_X         = "YYYY/MM";
    
    // 每秒对应的毫秒数
    public static final long   MILLIS_PER_SECOND        = DateUtils.MILLIS_PER_SECOND;
    
    // 每分对应的毫秒数
    public static final long   MILLIS_PER_MINUTE        = DateUtils.MILLIS_PER_MINUTE;
    
    // 每小时对应的毫秒数
    public static final long   MILLIS_PER_HOUR          = DateUtils.MILLIS_PER_HOUR;
    
    // 每天对应的毫秒数
    public static final long   MILLIS_PER_DAY           = DateUtils.MILLIS_PER_DAY;
}

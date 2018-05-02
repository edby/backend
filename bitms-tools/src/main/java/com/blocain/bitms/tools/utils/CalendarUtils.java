/*
 * @(#)CalendarUtils.java 2015-4-14 下午1:50:44
 * Copyright 2015 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.DateTime.Property;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * <p>File：CalendarUtils.java</p>
 * <p>Title: </p>
 * <p>Description:请注意，joda-time特性或其他原因，本工具类中的Object对象可以是Date、Calendar、Long、GregorianCalendar...，不能为String</p>
 * <p>Copyright: Copyright (c) 2015 2015-4-14 下午1:50:44</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class CalendarUtils
{
    public static final String startTime = " 00:00:00";
    
    public static final String endTime   = " 23:59:59";
    
    private CalendarUtils()
    {
        super();
    }
    
    /**
     * 获取当前时间毫秒数，返回long
     * @return long 当前时间毫秒数
     */
    public static long getCurrentTime()
    {
        return new DateTime(new Date()).getMillis();
    }
    
    /**
     * 根据指定的格式返回当前时间的字符串
     * @param format 指定的格式，为null时会返回格式：2015-01-12T14:17:53.908+08:00，此次不允许
     * @return String 字符串格式的当前时间
     */
    public static String getCurrentTime(String format)
    {
        if (StringUtils.isBlank(format)) { throw new NullPointerException(); }
        return new DateTime().toString(format);
    }
    
    /**
     * 将指定时间加上指定月数，返回长整数时间戳
     * @param object
     * @param amount
     * @return
     */
    public static Long addMonth(Object object, int amount)
    {
        DateTime dt = getDateTimeFromObject(object);
        return dt.plusMonths(amount).getMillis();
    }
    
    /**
     * 将指定的Date对象加上指定月数，并返 回指定格式的日期字符串
     * @param date Date对象
     * @param amount 指定的月数
     * @param format 日期格式
     * @return String 返 回指定格式的日期字符串
     */
    public static String addMonth(Object date, int amount, String format)
    {
        if (null == date)
        {
            throw new NullPointerException("日期参数为null");
        }
        else
        {
            DateTime dt = new DateTime(date);
            return dt.plusMonths(amount).toString(format);
        }
    }
    
    /**
     * 根据系统时间获取当前年份
     * @return int 当前年份
     */
    public static int getCurrentYear()
    {
        return new DateTime().getYear();
    }
    
    /**
     * 根据给定的日期参数，计算年龄
     * @param date Unix时间戳(精确到秒)
     * @return int 年龄
     */
    public static int getAge(long date)
    {
        Date now = new Date();
        Date birthDate = getTimeFromLong(date);
        int age = yearDiff(birthDate, now);
        return age;
    }
    
    /**
     * 计算两个日期对象间隔的年数(date2-date1)
     * @param date1 Date或字符串格式的日期
     * @param date2 Date或字符串格式的日期
     * @return int 间隔年数
     */
    public static int yearDiff(Object date1, Object date2)
    {
        if (null == date1 || null == date2)
        {
            throw new NullPointerException("日期参数为Null");
        }
        else
        {
            DateTime dt1 = new DateTime(date1);
            DateTime dt2 = new DateTime(date2);
            Period period = new Period(dt1, dt2, PeriodType.years());
            return period.getYears();
        }
    }
    
    /**
     * 获取指定日期对象的年份，字符串格式请不要调用该方法，普通的日期格式不符合ISO标准
     * @link ISODateTimeFormat#dateTimeParser()
     * @param object 指定的日期对象，如：Date，Long，DateTime、Calendar等
     * @return int 指定日期对象的年份
     */
    public static int getYear(Object object)
    {
        return getDateTimeFromObject(object).getYear();
    }
    
    /**
     * 获取指定格式字符串的年份
     * @param date 指定格式的日期字符串
     * @param format 字符串格式
     * @return int 指定格式字符串的年份
     */
    public static int getYear(String date, String format)
    {
        DateTime dt = getDateTimeFromString(date, format);
        return dt.getYear();
    }
    
    /**
     * 根据系统时间获取当前月份
     * @return int 当前月份
     */
    public static int getCurrentMonth()
    {
        return new DateTime().getMonthOfYear();
    }
    
    /**
     * 获取指定日期对象的月份，字符串格式请不要调用该方法，普通的日期格式不符合ISO标准
     * @link ISODateTimeFormat#dateTimeParser()
     * @param object 指定的日期对象，如：Date，Long，DateTime、Calendar等
     * @return int 指定日期的月份
     */
    public static int getMonth(Object object)
    {
        return getDateTimeFromObject(object).getMonthOfYear();
    }
    
    /**
     * 获取指定格式日期字符串的月份
     * @param date 指定格式日期字符串
     * @param format 字符串格式
     * @return int 指定格式日期字符串的月份
     */
    public static int getMonth(String date, String format)
    {
        return getDateTimeFromString(date, format).getMonthOfYear();
    }
    
    /**
     * 计算两个日期对象间隔的天数(date2-date1)
     * @param date1 Date或字符串格式的日期
     * @param date2 Date或字符串格式的日期
     * @return int 间隔天数
     */
    public static int dateDiff(Object date1, Object date2)
    {
        if (null == date1 || null == date2)
        {
            throw new NullPointerException("日期参数为Null");
        }
        else
        {
            DateTime dt1 = new DateTime(date1);
            DateTime dt2 = new DateTime(date2);
            Period period = new Period(dt1, dt2, PeriodType.days());
            return period.getDays();
        }
    }
    
    /**
     * 获取当前星期几
     * @return int 当前为周几
     */
    public static int getCurrentWeek()
    {
        return new DateTime().getDayOfWeek();
    }
    
    /**
     * 根据日期对象获取星期几，字符串格式请不要调用该方法，普通的日期格式不符合ISO标准
     * @link ISODateTimeFormat#dateTimeParser()
     * @param object 日期对象
     * @return int 星期几
     */
    public static int getWeek(Object object)
    {
        return getDateTimeFromObject(object).getDayOfWeek();
    }
    
    /**
     * 根据日期字符串及格式计算该时间是周几
     * @param date 日期字符串
     * @param format 字符串格式
     * @return int 星期几
     */
    public static int getWeek(String date, String format)
    {
        return getDateTimeFromString(date, format).getDayOfWeek();
    }
    
    /**
     * 根据系统时间获取当前日期
     * @return int 当前日期
     */
    public static int getCurrentDay()
    {
        return new DateTime().getDayOfMonth();
    }
    
    /**
     * 将时间精确到月（如：2014-03）并转化为long格式
     * @return iTime 整数格式的当前时间，精确到毫秒
     * @author 吴万杰
     */
    public static long getMonthLong(Long milliseconds)
    {
        if (null == milliseconds)
        {
            milliseconds = new DateTime().getMillis();
        }
        String dateStr = getStringTime(milliseconds, "yyyy-MM");
        long iTime = getLongFromTime(dateStr, "yyyy-MM");
        return iTime;
    }
    
    /**
     * 获取当前月的第一天的日期(零点零分零秒)
     * @return
     * @author 吴万杰
     */
    public static Long getFirstDayOfMonthLong()
    {
        Calendar currDate = Calendar.getInstance();
        currDate.setTime(new Date());
        currDate.set(Calendar.DATE, 1);// 设为当前月的1号
        return CalendarUtils.getDayLong(currDate.getTimeInMillis());
    }
    
    /**
     * 将时间精确到天（即今天零点零分零秒）并转化为long格式
     * @return
     * @author 潘健雷
     */
    public static long getDayLong(Long milliseconds)
    {
        DateTime dt = new DateTime(milliseconds);
        long iTime = getDate(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth(), 0, 0).getTime();
        return iTime;
    }
    
    /**
     * 将当前时间精确到天（即今天零点零分零秒）并转化为long格式
     * @return
     * @author 潘健雷
     */
    public static long getTodayLong()
    {
        DateTime dt = new DateTime();
        long iTime = getDate(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth(), 0, 0).getTime();
        return iTime;
    }
    
    /**
     * 根据长整型时间戳返回指定格式的日期字符串 排除lTime 为0的情况
     * @param lTime 长整型时间戳
     * @param format 日期格式
     * @return String 指定格式的日期字符串
     * @author 罗盛平
     */
    public static String getStringTime(Long lTime, String format)
    {
        if (!lTime.equals(0l))
        {
            DateTime dt = new DateTime(lTime);
            return dt.toString(format);
        }
        return null;
    }
    
    /**
     * 获取指定日期对象的日期，字符串格式请不要调用该方法，普通的日期格式不符合ISO标准
     * @link ISODateTimeFormat#dateTimeParser()
     * @param object 指定的日期对象
     * @return int 指定日期对象的日期
     */
    public static int getDay(Object object)
    {
        return getDateTimeFromObject(object).getDayOfMonth();
    }
    
    /**
     * 根据日期字符串及日期格式计算该时间是月里的哪天
     * @param date 日期字符串
     * @param format 字符串格式
     * @return int 指定日期对象的日期
     */
    public static int getDay(String date, String format)
    {
        return getDateTimeFromString(date, format).getDayOfMonth();
    }
    
    /**
     * 获取当前系统时间的小时数
     * @return int 当前系统时间的小时数
     */
    public static int getCurrentHour()
    {
        return new DateTime().getHourOfDay();
    }
    
    /**
     * 获取指定日期对象的小时数，字符串格式请不要调用该方法，普通的日期格式不符合ISO标准
     * @link ISODateTimeFormat#dateTimeParser()
     * @param object 指定的日期对象，如：Long、Date、DateTime、Calendar
     * @return int 指定日期对象的小时数
     */
    public static int getHour(Object object)
    {
        return getDateTimeFromObject(object).getHourOfDay();
    }
    
    /**
     * 获取指定格式日期字符串的当天小时数
     * @param date 字符串格式的日期
     * @param format 字符串格式
     * @return int 当天小时数
     */
    public static int getHour(String date, String format)
    {
        return getDateTimeFromString(date, format).getHourOfDay();
    }
    
    /**
     * 获取当前小时内的分钟数
     * @return int 当前小时内的分钟数
     */
    public static int getCurrentMinute()
    {
        return new DateTime().getMinuteOfHour();
    }
    
    /**
     * 获取指定日期对象小时内的分钟数，字符串格式请不要调用该方法，普通的日期格式不符合ISO标准
     * @link ISODateTimeFormat#dateTimeParser()
     * @param object 指定的日期对象，如：Long、Date、DateTime、Calendar
     * @return int 指定日期对象小时内的分钟数
     */
    public static int getMinute(Object object)
    {
        return getDateTimeFromObject(object).getMinuteOfHour();
    }
    
    /**
     * 根据日期字符串及指定的格式获取小时内的分钟数
     * @param date 日期
     * @param format 字符串格式
     * @return int 小时内的分钟数
     */
    public static int getMinute(String date, String format)
    {
        return getDateTimeFromString(date, format).getMinuteOfHour();
    }
    
    /**
     * 获取当前时间下分钟内的秒数
     * @return int 当前时间下分钟内的秒数
     */
    public static int getCurrentSecond()
    {
        return new DateTime().getSecondOfMinute();
    }
    
    /**
     * 获取指定日期对象分钟内的秒数，字符串格式请不要调用该方法，普通的日期格式不符合ISO标准
     * @link ISODateTimeFormat#dateTimeParser()
     * @param object 指定日期对象
     * @return int 指定日期对象分钟内的秒数
     */
    public static int getSecond(Object object)
    {
        return getDateTimeFromObject(object).getSecondOfMinute();
    }
    
    /**
     * 获取指定格式的日期字符串的当前分钟的秒数
     * @param date 日期字符串
     * @param format 字符串格式
     * @return int 当前分钟的秒数
     */
    public static int getSecond(String date, String format)
    {
        return getDateTimeFromString(date, format).getSecondOfMinute();
    }
    
    /**
     * 将指定的日期对象转换为Date，如：Long、DateTime、Calendar等
     * @param object 指定的日期对象
     * @return Date 转换后的日期对象
     */
    public static Date getDate(Object object)
    {
        return getDateTimeFromObject(object).toDate();
    }
    
    /**
     * 将指定格式的日期字符串转化为java.util.Date对象
     * @param date 日期字符串
     * @param format 字符串格式
     * @return Date java.util.Date
     */
    public static Date getDate(String date, String format)
    {
        return getDateTimeFromString(date, format).toDate();
    }
    
    /**
     * 根据年、月、日、时、分来构造Date对象
     * @param year 年
     * @param monthOfYear 月
     * @param dayOfMonth 日
     * @param hourOfDay 时
     * @param minuteOfHour 分
     * @return Date Date对象
     */
    public static Date getDate(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour)
    {
        return new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour).toDate();
    }
    
    /**
     * 根据年、月、日、时、分来构造指定日期格式的日期字符串
     * @param year 年,
     * @param monthOfYear 月
     * @param dayOfMonth 日
     * @param hourOfDay 时
     * @param minuteOfHour 分
     * @param format 日期格式
     * @return String 指定日期格式的日期字符串
     */
    public static String getDate(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, String format)
    {
        DateTime dt = new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour);
        return dt.toString(format);
    }
    
    /**
     * 根据年、月、日、时、分、秒来构造Date对象
     * @param year 年
     * @param monthOfYear 月
     * @param dayOfMonth 日
     * @param hourOfDay 时
     * @param minuteOfHour 分
     * @param secondOfMinute 秒
     * @return Date Date
     */
    public static Date getDate(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, int secondOfMinute)
    {
        DateTime dt = new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute);
        return dt.toDate();
    }
    
    /**
     * 根据年、月、日、时、分、秒返回指定格式的日期字符串
     * @param year 年
     * @param monthOfYear 月
     * @param dayOfMonth 日
     * @param hourOfDay 时
     * @param minuteOfHour 分
     * @param secondOfMinute 秒
     * @param format 格式
     * @return String 指定格式的日期字符串
     */
    public static String getDate(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, int secondOfMinute, String format)
    {
        DateTime dt = new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute);
        return dt.toString(format);
    }
    
    /**
     * 将日期对象以指定的日期格式字符串返回
     * @param object 日期格式对象，如：Long、String、Date、DateTime、Calendar
     * @param format 日期格式
     * @return String 指定的日期格式字符串
     */
    public static String getDate(Object object, String format)
    {
        return getDateTimeFromObject(object).toString(format);
    }
    
    /**
     * 字符串日期格式转换
     * @param date 要转换的日期字符串
     * @param oldFormat 日期字符串格式
     * @param newFormat 新格式
     * @return String 转换后的日期字符串
     */
    public static String getDate(String date, String oldFormat, String newFormat)
    {
        DateTime dt = getDateTimeFromString(date, oldFormat);
        return dt.toString(newFormat);
    }
    
    /**
     * 将当前时间以指定的日期格式以字符串形式返回
     * @param format 日期格式
     * @return String 指定的日期格式
     */
    public static String getCurrentDate(String format)
    {
        DateTime dt = new DateTime();
        return dt.toString(format);
    }
    
    /**
     * 将指定的日期对象加上指定秒数，返回长整数时间戳
     * @param object 指定的日期对象
     * @param amouont 加上的秒数
     * @return Long 结果
     */
    public static Long addSecond(Object object, int amouont)
    {
        DateTime dt = getDateTimeFromObject(object);
        return dt.plusSeconds(amouont).getMillis();
    }
    
    /**
     * 将指定的日期对象加上指定分钟数，返回长整数时间戳
     * @param object 指定的日期对象
     * @param amount 加上的分钟数
     * @return Long 结果
     */
    public static Long addMinute(Object object, int amount)
    {
        DateTime dt = getDateTimeFromObject(object);
        return dt.plusMinutes(amount).getMillis();
    }
    
    /**
     * 将指定的日期对象加上指定小时数，返回长整数时间戳
     * @param object 指定的日期对象
     * @param amount 加上的小时数
     * @return Long 结果
     */
    public static Long addHour(Object object, int amount)
    {
        DateTime dt = getDateTimeFromObject(object);
        return dt.plusHours(amount).getMillis();
    }
    
    /**
     * 将指定的日期对象加上指定天数，返回长整数时间戳
     * @param object 指定的日期对象
     * @param amount 加上的天数
     * @return Long 结果
     */
    public static Long addDay(Object object, int amount)
    {
        DateTime dt = getDateTimeFromObject(object);
        return dt.plusDays(amount).getMillis();
    }
    
    /**
     * 将指定时间加上指定天数，返回长整数时间戳
     * @param date 时间对象
     * @param amount 加上的天数
     * @return Date 结果
     */
    public static Date addDayToDate(Date date, int amount)
    {
        if (null == date)
        {
            throw new NullPointerException("日期参数为null");
        }
        else
        {
            DateTime dt = new DateTime(date);
            return dt.plusDays(amount).toDate();
        }
    }
    
    /**
     * 将指定的日期对象加上指定周数，返回长整型时间戳
     * @param object 指定的日期对象
     * @param amount 加上的周数
     * @return Long 结果
     */
    public static Long addWeek(Object object, int amount)
    {
        DateTime dt = getDateTimeFromObject(object);
        return dt.plusWeeks(amount).getMillis();
    }
    
    /**
     * 将指定的日期对象加上指定分钟数，并返回指定格式的日期字符串
     * @param date Date对象
     * @param amount 指定分钟数
     * @param format 日期格式
     * @return String 返回指定格式的日期字符串
     */
    public static String addMinute(String date, int amount, String format)
    {
        DateTime dt = getDateTimeFromObject(date);
        return dt.plusMinutes(amount).toString(format);
    }
    
    /**
     * 将指定的日期对象加上指定小时数，并返回指定格式的日期字符串
     * @param date Date对象
     * @param amount 指定小时数
     * @param format 日期格式
     * @return String 返回指定格式的日期字符串
     */
    public static String addHour(Object date, int amount, String format)
    {
        DateTime dt = getDateTimeFromObject(date);
        return dt.plusHours(amount).toString(format);
    }
    
    /**
     * 将指定的日期对象加上指定天数，并返回指定格式的日期字符串
     * @param date Date对象
     * @param amount 指定天数
     * @param format 日期格式
     * @return String 返回指定格式的日期字符串
     */
    public static String addDay(Object date, int amount, String format)
    {
        DateTime dt = getDateTimeFromObject(date);
        return dt.plusDays(amount).toString(format);
    }
    
    /**
     * 将指定的日期对象加上指定周数，并返回指定格式的日期字符串
     * @param date Date对象
     * @param amount 指定周数
     * @param format 日期格式
     * @return String 返回指定格式的日期字符串
     */
    public static String addWeek(Object date, int amount, String format)
    {
        DateTime dt = getDateTimeFromObject(date);
        return dt.plusWeeks(amount).toString(format);
    }
    
    /**
     * 将日期对象加上指定数量的年份，返回新的日期对象
     * @param date 指定的日期对象
     * @param amount 多少年
     * @return Date 加上指定年后的日期对象
     */
    public static Date addYear(Object date, int amount)
    {
        DateTime dt = getDateTimeFromObject(date);
        return dt.plusYears(amount).toDate();
    }
    
    /**
     * 将日期对象加上指定数量的年份，返回指定格式的字符串日期
     * @param date 指定的日期对象
     * @param amount 指定增加的年份数量
     * @param format 新的日期格式
     * @return String 增加指定年数后指定格式的字符串日期
     */
    public static String addYear(Object date, int amount, String format)
    {
        DateTime dt = getDateTimeFromObject(date);
        return dt.plusYears(amount).toString(format);
    }
    
    /**
     * 根据日期对象判断该月是否为润月
     * @param object 日期对象，如：String、Long、Date、DateTime、Calendar
     * @return boolean true：是，false：否
     */
    public static boolean monthIsLeap(Object object)
    {
        DateTime dt = getDateTimeFromObject(object);
        Property property = dt.monthOfYear();
        return property.isLeap();
    }
    
    /**
     * 判断当前月是否为润月
     * @return boolean true：是，false：否
     */
    public static boolean monthIsLeap()
    {
        DateTime dt = new DateTime();
        return monthIsLeap(dt);
    }
    
    /**
     * 计算两个日期对象间相隔的秒数
     * @param date1 日期对象一，如：String、Long、Date、DateTime、Calendar
     * @param date2 日期对象二，如：String、Long、Date、DateTime、Calendar
     * @return int 两个日期对象间相隔的秒数
     */
    public static int getDiffOfSecond(Object date1, Object date2)
    {
        if (null == date1 || null == date2) { throw new NullPointerException(); }
        DateTime dt1 = new DateTime(date1);
        DateTime dt2 = new DateTime(date2);
        Period period = new Period(dt1, dt2, PeriodType.seconds());
        return period.getSeconds();
    }
    
    /**
     * 计算两个日期对象间相隔的分钟数
     * @param date1 日期对象一，如：String、Long、Date、DateTime、Calendar
     * @param date2 日期对象二，如：String、Long、Date、DateTime、Calendar
     * @return int 两个日期对象间相隔的分钟数
     */
    public static int getDiffOfMinute(Object date1, Object date2)
    {
        if (null == date1 || null == date2) { throw new NullPointerException(); }
        DateTime dt1 = new DateTime(date1);
        DateTime dt2 = new DateTime(date2);
        Period period = new Period(dt1, dt2, PeriodType.minutes());
        return period.getMinutes();
    }
    
    /**
     * 计算两个日期对象间相隔的小时数
     * @param date1 日期对象一，如：String、Long、Date、DateTime、Calendar
     * @param date2 日期对象二，如：String、Long、Date、DateTime、Calendar
     * @return int 两个日期对象间相隔的小时数
     */
    public static int getDiffOfHour(Object date1, Object date2)
    {
        if (null == date1 || null == date2) { throw new NullPointerException(); }
        DateTime dt1 = new DateTime(date1);
        DateTime dt2 = new DateTime(date2);
        Period period = new Period(dt1, dt2, PeriodType.hours());
        return period.getHours();
    }
    
    /**
     * 获取现在距离今天结束还有多少毫秒
     * @return long 现在距离今天结束还有多少毫秒
     */
    public static long getNowDiffOfDay()
    {
        DateTime dt = new DateTime();
        DateTime endTimeOfDay = dt.millisOfDay().withMaximumValue();
        return endTimeOfDay.getMillis() - dt.getMillis();
    }
    
    /**
     * 计算两个日期对象之间间隔的天数
     * @param objOne 日期对象一
     * @param objTwo 日期对象二
     * @return int 两个日期之间间隔的天数
     */
    public static int getDiffOfDay(Object objOne, Object objTwo)
    {
        DateTime dtOne = new DateTime(objOne);
        DateTime dtTwo = new DateTime(objTwo);
        int days = Days.daysBetween(dtOne, dtTwo).getDays();
        return days;
    }
    
    /**
     * 计算两个日期对象间隔的周数(date2-date1)
     * @param date1 Date或字符串格式的日期
     * @param date2 Date或字符串格式的日期
     * @return int 间隔周数
     */
    public static int getDiffOfWeek(Object date1, Object date2)
    {
        if (null == date1 || null == date2) { throw new NullPointerException(); }
        DateTime dt1 = new DateTime(date1);
        DateTime dt2 = new DateTime(date2);
        Period period = new Period(dt1, dt2, PeriodType.weeks());
        return period.getWeeks();
    }
    
    /**
     * 计算两个日期对象间隔的月数(date2-date1)
     * @param date1 Date或字符串格式的日期
     * @param date2 Date或字符串格式的日期
     * @return int 间隔月数
     */
    public static int getDiffOfMonth(Object date1, Object date2)
    {
        if (null == date1 || null == date2) { throw new NullPointerException(); }
        DateTime dt1 = new DateTime(date1);
        DateTime dt2 = new DateTime(date2);
        Period period = new Period(dt1, dt2, PeriodType.months());
        return period.getMonths();
    }
    
    /**
     * 计算两个日期对象间隔的年数(date2-date1)
     * @param date1 Date或字符串格式的日期
     * @param date2 Date或字符串格式的日期
     * @return int 间隔年数
     */
    public static int getDiffOfYear(Object date1, Object date2)
    {
        if (null == date1 || null == date2) { throw new NullPointerException(); }
        DateTime dt1 = new DateTime(date1);
        DateTime dt2 = new DateTime(date2);
        Period period = new Period(dt1, dt2, PeriodType.years());
        return period.getYears();
    }
    
    /**
     * 计算指定日期到当前时间的间隔，如：xx天xx小时xx分xx秒，以long数组返回
     * @param object 指定的日期对象
     * @return long[] 间隔时间数组，天、小时、分、秒
     */
    public static long[] getDiffTimeOfNow(Object object)
    {
        long nowsLong = getCurrentLong();
        long dateLong = new DateTime(object).getMillis();
        return getDiffTimeOfNow(nowsLong, dateLong);
    }
    
    /**
     * 计算指定日期到当前时间的间隔，如：xx天xx小时xx分xx秒，以long数组返回
     * @param object1 指定的日期对象
     * @param object2 指定的日期对象
     * @return long[] 间隔时间数组，天、小时、分、秒
     */
    public static long[] getDiffTimeOfNow(Object object1, Object object2)
    {
        if (null == object1 || null == object2) { throw new NullPointerException(); }
        long long1 = new DateTime(object1).getMillis();
        long long2 = new DateTime(object2).getMillis();
        long between = Math.abs((long1 - long2) / 1000);
        long[] diff = new long[4];
        long day = between / (24 * 3600);
        diff[0] = day;
        long hour = between % (24 * 3600) / 3600;
        diff[1] = hour;
        long minute = between % 3600 / 60;
        diff[2] = minute;
        long second = between % 60;
        diff[3] = second;
        return diff;
    }
    
    /**
     * 获取指定日期的开始时间，如：2014-01-08 00:00:00
     * @param object 日期对象，如：Date，String，Long，DateTime、Calendar等
     * @return DateTime 包含指定日期开始时间的DateTime对象
     */
    public static DateTime getTimeStartOfDay(Object object)
    {
        DateTime dt = getDateTimeFromObject(object);
        return dt.withTimeAtStartOfDay();
    }
    
    /**
     * 获取指定日期的开始时间的unix时间戳
     * @param object 日期对象，如：Date，String，Long，DateTime、Calendar等
     * @return long 指定日期的开始时间的unix时间戳
     */
    public static long getLongStartOfDay(Object object)
    {
        if (object == null) return 0;
        DateTime dt = getDateTimeFromObject(object);
        return dt.withTimeAtStartOfDay().getMillis();
    }
    
    /**
     * 查询时的日期区间处理(开始时间)
     * 按指定的日期及希望返回的格式返回该日期当天开始的时间
     * @param date 指定查询的开始时间
     * @param currentFormat 当前日期格式
     * @param format 希望返回的时间格式
     * @return String 返回的时间
     */
    public static String getTimeStartOfDay(String date, String currentFormat, String format)
    {
        DateTime dt = getDateTimeFromString(date, currentFormat);
        return dt.withTimeAtStartOfDay().toString(format);
    }
    
    /**
     * 查询时的日期区间处理(开始时间)
     * 按指定的日期及希望返回的格式返回该日期当天开始的Unix时间戳/1000的值
     * @param date 指定查询的开始时间
     * @param currentFormat 当前日期格式
     * @return long 该日期当天开始的Unix时间戳/1000的值
     */
    public static long getLongStartOfDay(String date, String currentFormat)
    {
        DateTime dt = getDateTimeFromString(date, currentFormat);
        return dt.withTimeAtStartOfDay().getMillis();
    }
    
    /**
     * 获取指定日期的结束时间，如：2014-01-08 23:59:59
     * @param object 日期对象，如：Date，Long，DateTime、Calendar等
     * @return DateTime 包含指定日期结束时间的DateTime对象
     */
    public static DateTime getTimeOverOfDay(Object object)
    {
        DateTime dt = getDateTimeFromObject(object);
        return dt.millisOfDay().withMaximumValue();
    }
    
    /**
     * 获取指定日期结束时间的Unix时间戳
     * @param object 日期对象，如：Date，Long，DateTime、Calendar等
     * @return
     */
    public static long getLongOverOfDay(Object object)
    {
        if (object == null) return 0;
        DateTime dt = getDateTimeFromObject(object);
        return dt.millisOfDay().withMaximumValue().getMillis();
    }
    
    /**
     * 查询时的日期区间处理(结束时间)
     * 按指定的日期及希望返回的格式返回该日期当天结束的时间
     * @param date 指定查询的结束时间
     * @param currentFormat 当前日期格式
     * @param format 希望返回的时间格式
     * @return String 返回的时间
     */
    public static String getTimeOverOfDay(String date, String currentFormat, String format)
    {
        DateTime dt = getDateTimeFromString(date, currentFormat);
        return dt.millisOfDay().withMaximumValue().toString(format);
    }
    
    /**
     * 查询时的日期区间处理(结束时间)
     * 按指定的日期及希望返回的格式返回该日期当天结束的Unix时间戳/1000的值
     * @param date 指定查询的开始时间
     * @param currentFormat 当前日期格式
     * @return long 该日期当天开始的Unix时间戳/1000的值
     */
    public static long getLongOverOfDay(String date, String currentFormat)
    {
        DateTime dt = getDateTimeFromString(date, currentFormat);
        return dt.millisOfDay().withMaximumValue().getMillis();
    }
    
    /**
     * 取得当前月的最后一天并以指定格式的字符串返回
     * @param format 指定的日期格式
     * @return String 指定格式的日期字符串
     */
    public static String getLastDayOfMonth(String format)
    {
        DateTime dt = new DateTime();
        return dt.dayOfMonth().withMaximumValue().toString(format);
    }
    
    /**
     * 取得当前月的最后一天
     * @param date 日期对象
     * @param format 日期格式
     * @return String 最后一天
     */
    public static String getLastDayOfMonth(Date date, String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        c.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
        return sdf.format(c.getTime());
    }
    
    /**
     * 取得月的最后一天(结束时间,23:59:59)
     * @param dateTime
     * @return
     */
    public static long getLastDayOfMonthEndTime(Long dateTime)
    {
        if (null == dateTime)
        {
            return new DateTime().dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue().getMillis();
        }
        else
        {
            return new DateTime(dateTime).dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue().getMillis();
        }
    }
    
    /**
     * 按指定格式获取当前月的第一天的日期
     * @param date 日期对象
     * @param format 指定的日期格式
     * @return String 日期字符串
     */
    public static String getFirstDayOfMonth(Date date, String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar currDate = Calendar.getInstance();
        currDate.setTime(date);
        currDate.set(Calendar.DATE, 1);// 设为当前月的1号
        return sdf.format(currDate.getTime());
    }
    
    /**
     * 取得当前月第一天的当前时间的长整型毫秒数
     * @return long 当前月第一天的长整型毫秒数
     */
    public static long getFirstDayOfMonthCurrentTime()
    {
        return new DateTime().dayOfMonth().withMinimumValue().getMillis();
    }
    
    /**
     * 取得指定月份第一天开始时间(0点0分0秒)的长整型毫秒数
     * @param dateTime 为空是为当前月
     * @return long 前月第一天开始时间(0点0分0秒)的长整型毫秒数
     */
    public static long getFirstDayOfMonthStartTime(Long dateTime)
    {
        if (null == dateTime)
        {
            return new DateTime().dayOfMonth().withMinimumValue().withTimeAtStartOfDay().getMillis();
        }
        else
        {
            return new DateTime(dateTime).dayOfMonth().withMinimumValue().withTimeAtStartOfDay().getMillis();
        }
    }
    
    /**
     * 取得指定月份下一月份第一天开始时间(0点0分0秒)的长整型毫秒数
     * @param dateTime 为空是为当前月
     * @return long 前月第一天开始时间(0点0分0秒)的长整型毫秒数
     */
    public static long getFirstDayOfNextMonthStartTime(Long dateTime)
    {
        if (null == dateTime)
        {
            return new DateTime().plusMonths(1).dayOfMonth().withMinimumValue().withTimeAtStartOfDay().getMillis();
        }
        else
        {
            return new DateTime(dateTime).plusMonths(1).dayOfMonth().withMinimumValue().withTimeAtStartOfDay().getMillis();
        }
    }
    
    /**
     * 根据给定的日期参数，计算年龄
     * @param date 日期对象
     * @return int 年龄
     */
    public static int getAge(Object date)
    {
        Date now = new Date();
        DateTime dt = getDateTimeFromObject(date);
        int age = getDiffOfYear(dt, now);
        return age;
    }
    
    /**
     * 将当前时间精确到秒并转化为long格式
     * @return iTime 整数格式的当前时间，精确到毫秒
     */
    public static long getCurrentLong()
    {
        long iTime = System.currentTimeMillis();
        return iTime;
    }
    
    /**
     * 根据长整型时间戳返回指定格式的日期字符串
     * @param lTime 长整型时间戳
     * @param format 日期格式
     * @return String 指定格式的日期字符串
     */
    public static String getTimeFromLong(long lTime, String format)
    {
        DateTime dt = new DateTime(lTime);
        return dt.toString(format);
    }
    
    /**
     * 根据长整型时间戳返回java.util.Date对象
     * @param lTime 长整型时间戳
     * @return Date java.util.Date对象
     */
    public static Date getTimeFromLong(long lTime)
    {
        return new DateTime(lTime).toDate();
    }
    
    /**
     * 根据日期对象，返回对应的长整型时间戳
     * @param object 日期对象
     * @return long 长整型时间戳
     */
    public static long getLongFromTime(Object object)
    {
        return getDateTimeFromObject(object).getMillis();
    }
    
    /**
     * 根据字符串格式日期时间及字符串格式，获取该时间的Unix时间戳
     * @param date 符串格式日期时间
     * @param format 字符串格式
     * @return long 该时间的Unix时间戳
     */
    public static long getLongFromTime(String date, String format)
    {
        return getDateTimeFromString(date, format).getMillis();
    }
    
    /**
     * 将日期对象格式化为指定格式的字符串
     * @param object 日期对象，如：Date，Long，String，DateTime、Calendar
     * @param format 指定的日期字符串格式
     * @return String 指定格式的日期字符串
     */
    public static String dateFormat(Object object, String format)
    {
        DateTime dt = getDateTimeFromObject(object);
        return dt.toString(format);
    }
    
    /**
     * 将yyyy-MM-dd HH:mm:ss格式的字符串时间转化为日期格式
     * @param time yyyy-MM-dd HH:mm:ss格式的字符串
     * @return Date Date
     */
    public static Date parseStringToDate(String time)
    {
        DateTime dt = new DateTime(time);
        return dt.toDate();
    }
    
    /**
     * 将指定格式的日期字符串转化为DateTime对象
     * @param date 字符串格式的日期对象
     * @param format 字符串格式
     * @return DateTime DateTime
     */
    public static DateTime getDateTimeFromString(String date, String format)
    {
        if (null == date || null == format) { throw new NullPointerException(); }
        DateTimeFormatter formater = DateTimeFormat.forPattern(format);
        return formater.parseDateTime(date);
    }
    
    /**
     * 将对象类型的日期转化为DateTime对象
     * @param object 如：Calendar、Date、Long等
     * @return DateTime DateTime
     */
    public static DateTime getDateTimeFromObject(Object object)
    {
        if (null == object) { throw new NullPointerException(); }
        return new DateTime(object);
    }
    
    /**
     * 统计查询处理：前一天当前时间，获取指定格式的前一天的日期字符串
     * @param format 指定的字符串格式
     * @return String 指定格式的前一天的日期字符串
     */
    public static String getPreviousDayOfCurrentTime(String format)
    {
        return new DateTime().minusDays(1).toString(format);
    }
    
    /**
     * 统计查询处理：后一天当前时间，获取指定格式的后一天的日期字符串
     * @param format 指定返回的字符串格式
     * @return String 指定格式的前一天的日期字符串
     */
    public static String getNextDayOfCurrentTime(String format)
    {
        return new DateTime().plusDays(1).toString(format);
    }
    
    /**
     * 统计查询处理：前一周当前时间，获取指定格式的前一周的日期字符串
     * @param format 指定返回的字符串格式
     * @return String 指定格式的前一周的日期字符串
     */
    public static String getPreviousWeekOfCurrentTime(String format)
    {
        return new DateTime().minusWeeks(1).toString(format);
    }
    
    /**
     * 统计查询处理：后一周当前时间，获取指定格式的后一周的日期字符串
     * @param format 指定返回的字符串格式
     * @return String 指定格式的后一周的日期字符串
     */
    public static String getNextWeekOfCurrentTime(String format)
    {
        return new DateTime().plusWeeks(1).toString(format);
    }
    
    /**
     * 统计查询处理：前一月当前时间，获取指定格式的前一月的日期字符串
     * @param format 指定返回的字符串格式
     * @return String 指定格式的前一月的日期字符串
     */
    public static String getPreviousMonthOfCurrentTime(String format)
    {
        return new DateTime().minusMonths(1).toString(format);
    }
    
    /**
     * 统计查询处理：后一月当前时间，获取指定格式的后一月的日期字符串
     * @param format 指定返回的字符串格式
     * @return String 指定格式的后一月的日期字符串
     */
    public static String getNextMonthOfCurrentTime(String format)
    {
        return new DateTime().plusMonths(1).toString(format);
    }
    
    /**
     * 统 计查询处理：获取前一天0点0分0秒的长整型毫秒数
     * @return long 前一天0点0分0秒的长整型毫秒数
     */
    public static long getPreviousDayOfStartTime()
    {
        return new DateTime().minusDays(1).withTimeAtStartOfDay().getMillis();
    }
    
    /**
     * 统计查询处理：获取前一天结束时间的长整型毫秒数
     * @return long 前一天结束时间的长整型毫秒数
     */
    public static long getPreviousDayOfOverTime()
    {
        return new DateTime().minusDays(1).millisOfDay().withMaximumValue().getMillis();
    }
    
    /**
     * 统 计查询处理：获取后一天0点0分0秒的长整型毫秒数
     * @return long 前一天0点0分0秒的长整型毫秒数
     */
    public static long getNextDayOfStartTime()
    {
        return new DateTime().plusDays(1).withTimeAtStartOfDay().getMillis();
    }
    
    /**
     * 统 计查询处理：获取后一天结束时间的长整型毫秒数
     * @return long 后一天结束时间的长整型毫秒数
     */
    public static long getNextDayOfOverTime()
    {
        return new DateTime().plusDays(1).millisOfDay().withMaximumValue().getMillis();
    }

    /**
     * 统计查询处理：获取后一天时间的长整型毫秒数
     * @return long 后一天结束时间的长整型毫秒数
     */
    public static long getNextDay()
    {
        return new DateTime().plusDays(1).millisOfDay().withMaximumValue().getMillis();
    }
    
    /**
     * 统计查询处理：获取前一月0点0分0秒的长整型毫秒数
     * @return long 前一月0点0分0秒的长整型毫秒数
     */
    public static long getPreviousMonthOfStartTime()
    {
        return new DateTime().minusMonths(1).withTimeAtStartOfDay().getMillis();
    }
    
    /**
     * 统计查询处理：获取前一月结束时间的长整型毫秒数
     * @return long 前一月结束时间的长整型毫秒数
     */
    public static long getPreviousMonthOfOverTime()
    {
        return new DateTime().minusMonths(1).millisOfDay().withMaximumValue().getMillis();
    }
    
    /**
     * 统计查询处理：获取后一月0点0分0秒的长整型毫秒数
     * @return long 后一月0点0分0秒的长整型毫秒数
     */
    public static long getNextMonthOfStartTime()
    {
        return new DateTime().plusMonths(1).withTimeAtStartOfDay().getMillis();
    }
    
    /**
     * 统计查询处理：获取后一月结束时间的长整型毫秒数
     * @return long 后一月结束时间的长整型毫秒数
     */
    public static long getNextMonthOfOverTime()
    {
        return new DateTime().plusMonths(1).millisOfDay().withMaximumValue().getMillis();
    }
    
    /**
     * 统计查询处理：获取前一周0点0分0秒的长整型毫秒数
     * @return long 前一周0点0分0秒的长整型毫秒数
     */
    public static long getPreviousWeekOfStartTime()
    {
        return new DateTime().minusWeeks(1).millisOfDay().withMinimumValue().getMillis();
    }
    
    /**
     * 统计查询处理：获取前一周结束时间的长整型毫秒数
     * @return long 前一周结束时间的长整型毫秒数
     */
    public static long getPreviousWeekOfOverTime()
    {
        return new DateTime().minusWeeks(1).millisOfDay().withMaximumValue().getMillis();
    }
    
    /**
     * 统 计查询处理：获取前一周0点0分0秒的长整型毫秒数
     * @return long 前一周0点0分0秒的长整型毫秒数
     */
    public static long getNextWeekOfStartTime()
    {
        return new DateTime().plusWeeks(1).millisOfDay().withMinimumValue().getMillis();
    }
    
    /**
     * 统计查询处理：获取后一周结束时间的长整型毫秒数
     * @return long 后一周结束时间的长整型毫秒数
     */
    public static long getNextWeekOfOverTime()
    {
        return new DateTime().plusWeeks(1).millisOfDay().withMaximumValue().getMillis();
    }
    
    /**
     * 获得指定年数后的当前日期的最大时间
     * @param num
     * @return
     */
    public static long getOverTimeForYears(Long startTime, int num)
    {
        return new DateTime(startTime).plusYears(num).millisOfDay().withMaximumValue().getMillis();
    }
    
    /**
     * 用于查询参数，获取指定前几周周或月的开始时间
     * @param num 例如：1
     * @param unit 例如："week","month"
     * @return
     */
    public static long getPreviousStartTime(int num, String unit)
    {
        if (StringUtils.equals("week", unit)) { return new DateTime().minusWeeks(num).millisOfDay().withMinimumValue().getMillis(); }
        if (StringUtils.equals("month", unit)) { return new DateTime().minusMonths(num).millisOfDay().withMinimumValue().getMillis(); }
        return new DateTime().getMillis();
    }
    
    /**
     * 日期大小比较
     * @param object1 日期对象一，不能是String类型，可以是Date，Calendar，Long...
     * @param object2 日期对象二，不能是String类型，可以是Date，Calendar，Long...
     * @return boolean 日期一在日期二之后：返回true，否则返回false
     */
    public static boolean compareDateDiff(Object object1, Object object2)
    {
        if (null == object1 || null == object2) { throw new NullPointerException(); }
        DateTime dt1 = getDateTimeFromObject(object1);
        DateTime dt2 = getDateTimeFromObject(object2);
        return dt1.isAfter(dt2);
    }
    
    /**
     * 将日期对象与当前时间进行比较
     * @param object 日期对象，不能是String类型，可以是Date，Calendar，Long...
     * @return boolean 日期在当前时间之后返回true，否则返回false
     */
    public static boolean compareDateWithNow(Object object)
    {
        return compareDateDiff(object, new Date());
    }
    
    /**
     * 获取当前时间所处的季度 6月 返回2，11月返回4
     * @return
     */
    public static int getQuarterOfCurrentTime()
    {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int quarter = 0;
        if (month <= 3)
        {
            quarter = 1;
        }
        else
        {
            quarter = (month / 3) + ((month % 3) > 0 ? 1 : 0);
        }
        return quarter;
    }
}

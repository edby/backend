package com.blocain.bitms.orm.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @Title 
 * @Description 
 * @Copyright Copyright(C) 2012
 * @Company 
 * @author 肖纯勇(xcy)
 * @version 1.0
 * @create 2012-7-18 上午10:12:46
 */
public class ExceptionUtils
{
    /**
     * 将CheckedException转换为UncheckedException.
     */
    public static RuntimeException unchecked(Exception e)
    {
        if (e instanceof RuntimeException)
        {
            return (RuntimeException) e;
        }
        else
        {
            return new RuntimeException(e);
        }
    }
    
    /**
     * 将ErrorStack转化为String.
     */
    public static String getStackTraceAsString(Throwable e)
    {
        if (e == null) { return ""; }
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
    
    /**
     * 判断异常是否由某些底层的异常引起.
     */
    @SuppressWarnings("unchecked")
    public static boolean isCausedBy(Exception ex, Class<? extends Exception> ... causeExceptionClasses)
    {
        Throwable cause = ex.getCause();
        while (cause != null)
        {
            for (Class<? extends Exception> causeClass : causeExceptionClasses)
            {
                if (causeClass.isInstance(cause)) { return true; }
            }
            cause = cause.getCause();
        }
        return false;
    }
}

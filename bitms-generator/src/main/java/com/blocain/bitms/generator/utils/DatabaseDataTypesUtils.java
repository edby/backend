package com.blocain.bitms.generator.utils;

import java.sql.Types;
import java.util.HashMap;

/**
 * JAVA类型与数据库类型关系映射
 * <p>File：DatabaseDataTypesUtils.java</p>
 * <p>Title: DatabaseDataTypesUtils</p>
 * <p>Description:DatabaseDataTypesUtils</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class DatabaseDataTypesUtils
{
    private final static IntStringMap _preferredJavaTypeForSqlType = new IntStringMap();
    
    public static boolean isFloatNumber(int sqlType, int size, int decimalDigits)
    {
        String javaType = getPreferredJavaType(sqlType, size, decimalDigits);
        if (javaType.endsWith("Float") || javaType.endsWith("Double") || javaType.endsWith("BigDecimal")) { return true; }
        return false;
    }
    
    public static boolean isIntegerNumber(int sqlType, int size, int decimalDigits)
    {
        String javaType = getPreferredJavaType(sqlType, size, decimalDigits);
        if (javaType.endsWith("Long") || javaType.endsWith("Integer") || javaType.endsWith("Short")) { return true; }
        return false;
    }
    
    public static boolean isDate(int sqlType, int size, int decimalDigits)
    {
        String javaType = getPreferredJavaType(sqlType, size, decimalDigits);
        if (javaType.endsWith("Date") || javaType.endsWith("Timestamp")) { return true; }
        return false;
    }
    
    public static boolean isBoolean(int sqlType, int size, int decimalDigits)
    {
        String javaType = getPreferredJavaType(sqlType, size, decimalDigits);
        if (javaType.endsWith("Boolean")) { return true; }
        return false;
    }
    
    public static String getPreferredJavaType(int sqlType, int size, int decimalDigits)
    {
        if ((sqlType == Types.DECIMAL || sqlType == Types.NUMERIC) && decimalDigits == 0)
        {
            if (size == 1)
            {
                return "java.lang.Boolean";
            }
            else if (size < 3)
            {
                return "java.lang.Byte";
            }
            else if (size < 5)
            {
                return "java.lang.Short";
            }
            else if (size < 10)
            {
                return "java.lang.Integer";
            }
            else
            {
                return "java.lang.Long";
            }
        }
        String result = _preferredJavaTypeForSqlType.getString(sqlType);
        if (result == null)
        {
            result = "java.lang.Object";
        }
        return result;
    }
    
    static
    {
        _preferredJavaTypeForSqlType.put(Types.TINYINT, "java.lang.Short");
        _preferredJavaTypeForSqlType.put(Types.SMALLINT, "java.lang.Short");
        _preferredJavaTypeForSqlType.put(Types.INTEGER, "java.lang.Integer");
        _preferredJavaTypeForSqlType.put(Types.BIGINT, "java.lang.Long");
        _preferredJavaTypeForSqlType.put(Types.REAL, "java.lang.Float");
        _preferredJavaTypeForSqlType.put(Types.FLOAT, "java.lang.Float");
        _preferredJavaTypeForSqlType.put(Types.DOUBLE, "java.lang.Double");
        _preferredJavaTypeForSqlType.put(Types.DECIMAL, "java.math.BigDecimal");
        _preferredJavaTypeForSqlType.put(Types.NUMERIC, "java.lang.Integer");
        _preferredJavaTypeForSqlType.put(Types.BIT, "java.lang.Boolean");
        _preferredJavaTypeForSqlType.put(Types.CHAR, "java.lang.String");
        _preferredJavaTypeForSqlType.put(Types.VARCHAR, "java.lang.String");
        _preferredJavaTypeForSqlType.put(Types.LONGVARCHAR, "java.lang.String");
        _preferredJavaTypeForSqlType.put(Types.BINARY, "byte[]");
        _preferredJavaTypeForSqlType.put(Types.VARBINARY, "byte[]");
        _preferredJavaTypeForSqlType.put(Types.LONGVARBINARY, "java.io.InputStream");
        _preferredJavaTypeForSqlType.put(Types.DATE, "java.util.Date");
        _preferredJavaTypeForSqlType.put(Types.TIME, "java.util.Date");
        _preferredJavaTypeForSqlType.put(Types.TIMESTAMP, "java.util.Date");
        _preferredJavaTypeForSqlType.put(Types.CLOB, "java.lang.String");
        _preferredJavaTypeForSqlType.put(Types.BLOB, "java.lang.String");
        _preferredJavaTypeForSqlType.put(Types.ARRAY, "java.lang.String");
        _preferredJavaTypeForSqlType.put(Types.REF, "java.lang.String");
        _preferredJavaTypeForSqlType.put(Types.STRUCT, "java.lang.String");
        _preferredJavaTypeForSqlType.put(Types.JAVA_OBJECT, "java.lang.Object");
    }
    
    @SuppressWarnings("rawtypes")
    private static class IntStringMap extends HashMap
    {
        private static final long serialVersionUID = 5764849017550740211L;
        
        public String getString(int i)
        {
            return (String) get(new Integer(i));
        }
        
        @SuppressWarnings("unused")
        public String[] getStrings(int i)
        {
            return (String[]) get(new Integer(i));
        }
        
        @SuppressWarnings("unchecked")
        public void put(int i, String s)
        {
            put(new Integer(i), s);
        }
        
        @SuppressWarnings({"unused", "unchecked"})
        public void put(int i, String[] sa)
        {
            put(new Integer(i), sa);
        }
    }
}

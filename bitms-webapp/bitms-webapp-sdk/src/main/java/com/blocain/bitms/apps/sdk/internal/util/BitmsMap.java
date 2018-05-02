package com.blocain.bitms.apps.sdk.internal.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.blocain.bitms.apps.sdk.BitmsConstants;

/**
 * 纯字符串字典结构。
 * 
 * @author playguy
 * @since 1.0, Sep 13, 2009
 */
public class BitmsMap extends HashMap<String, String>
{
    private static final long serialVersionUID = -1277791390393392630L;
    
    public BitmsMap()
    {
        super();
    }
    
    public BitmsMap(Map<? extends String, ? extends String> m)
    {
        super(m);
    }
    
    public String put(String key, Object value)
    {
        String strValue;
        if (value == null)
        {
            strValue = null;
        }
        else if (value instanceof String)
        {
            strValue = (String) value;
        }
        else if (value instanceof Integer)
        {
            strValue = ((Integer) value).toString();
        }
        else if (value instanceof Long)
        {
            strValue = ((Long) value).toString();
        }
        else if (value instanceof Float)
        {
            strValue = ((Float) value).toString();
        }
        else if (value instanceof Double)
        {
            strValue = ((Double) value).toString();
        }
        else if (value instanceof Boolean)
        {
            strValue = ((Boolean) value).toString();
        }
        else
        {
            strValue = value.toString();
        }
        return this.put(key, strValue);
    }
    
    public String put(String key, String value)
    {
        if (StringUtils.areNotEmpty(key, value))
        {
            return super.put(key, value);
        }
        else
        {
            return null;
        }
    }
}

package com.blocain.bitms.generator.utils;

/**
 * 字符串辅助类
 * <p>File：StringHelper.java</p>
 * <p>Title: StringHelper</p>
 * <p>Description:StringHelper</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class StringHelper
{
    /**
     * 所有分割好的字符串的第一个字母转成大写
     * @author Playguy
     * @param sqlName
     * @return
     */
    public static String makeAllWordFirstLetterUpperCase(String sqlName)
    {
        String[] strs = sqlName.split("_");
        String result = "";
        String preStr = "";
        for (String str : strs)
        {
            if (preStr.length() == 1)
            {
                result += str;
            }
            else
            {
                result += capitalize(str);
            }
            preStr = str;
        }
        return result;
    }
    
    /**
     * 将所有分割好的字符串的第一个字母转成小写
     * @author Playguy
     * @param sqlName
     * @return
     */
    public static String makeWordFirstLetterLowerCase(String sqlName)
    {
        return uncapitalize(sqlName);
    }
    
    /**
     * 替换字符
     * @author Playguy
     * @param inString
     * @param oldPattern
     * @param newPattern
     * @return
     */
    public static String replace(String inString, String oldPattern, String newPattern)
    {
        if (inString == null) { return null; }
        if (oldPattern == null || newPattern == null) { return inString; }
        StringBuffer sbuf = new StringBuffer();
        int pos = 0;
        int index = inString.indexOf(oldPattern);
        int patLen = oldPattern.length();
        while (index >= 0)
        {
            sbuf.append(inString.substring(pos, index));
            sbuf.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }
        sbuf.append(inString.substring(pos));
        return sbuf.toString();
    }
    
    public static String capitalize(String str)
    {
        return changeFirstCharacterCase(str, true);
    }
    
    public static String uncapitalize(String str)
    {
        return changeFirstCharacterCase(str, false);
    }
    
    /**
     * 转换首字母大小写
     * @author Playguy
     * @param str
     * @param capitalize
     * @return
     */
    private static String changeFirstCharacterCase(String str, boolean capitalize)
    {
        if (str == null || str.length() == 0) { return str; }
        StringBuffer buf = new StringBuffer(str.length());
        if (capitalize)
        {
            buf.append(Character.toUpperCase(str.charAt(0)));
        }
        else
        {
            buf.append(Character.toLowerCase(str.charAt(0)));
        }
        buf.append(str.substring(1));
        return buf.toString();
    }
}

package com.blocain.bitms.tools.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * 集合工具类
 * <p>File: ListUtils.java </p>
 * <p>Title: ListUtils </p>
 * <p>Description: ListUtils </p>
 * <p>Copyright: Copyright (c) 16/3/17 </p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class ListUtils extends org.apache.commons.collections.ListUtils
{
    /**
     * 判断集合对象是否存在
     * <p>
     *     list == null return false;
     *     list.size() <= 0 return false;
     * </p>
     * @param list
     * @return
     */
    public static boolean isNotNull(Collection<?> list)
    {
        boolean flag = false;
        if (null != list && !list.isEmpty())
        {
            flag = true;
        }
        return flag;
    }
    
    /**
     * 判断集合对象是否存在
     * <p>
     *     list == null return true;
     *     list.size() <= 0 return true;
     * </p>
     * @param list
     * @return
     */
    public static boolean isNull(Collection<?> list)
    {
        return !isNotNull(list);
    }
    
    /***
     * 将集合转换为字符串,元素之间以','分隔。<br>
     * @param list Collection
     * @return String 转换后的字符串
     */
    public static String join2String(Collection<?> list)
    {
        StringBuffer sb = new StringBuffer();
        int i = 0;
        for (Iterator<?> it = list.iterator(); it.hasNext();)
        {
            if (i != 0) sb.append(",");
            sb.append(String.valueOf(it.next()));
            i++;
        }
        return sb.toString();
    }
    
    /***
     * 将集合转换为字符串,每个元素添加''号,元素之间以','分隔。<br>
     * @param list Collection
     * @return String 转换后的字符串
     */
    public static String join2String2(Collection<?> list)
    {
        StringBuffer sb = new StringBuffer();
        int i = 0;
        for (Iterator<?> it = list.iterator(); it.hasNext();)
        {
            if (i != 0) sb.append(",");
            sb.append("'" + (String) it.next() + "'");
            i++;
        }
        return sb.toString();
    }
    
    /**
     * list 去重
     * @author Playguy
     * @param list
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <E> void removeDuplicate(List<E> list)
    {
        if (null != list && list.size() > 0)
        {
            list.removeAll(Collections.singleton(null));
            list.removeAll(Collections.singleton(""));
            HashSet h = new HashSet(list);
            list.clear();
            list.addAll(h);
        }
    }
}

/*
 * @(#)ObjectUtils.java 2015-4-24 上午11:14:24
 * Copyright 2015 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.utils;

import com.blocain.bitms.tools.consts.CharsetConst;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Method;

/**
 * <p>File：ObjectUtils.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2015 2015-4-24 上午11:14:24</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils
{
    private static final Logger logger = LoggerFactory.getLogger(ObjectUtils.class);
    
    private ObjectUtils()
    {
    }
    
    /**
     * 注解到对象复制，只复制能匹配上的方法。
     * @param annotation 注解
     * @param object 对象
     */
    public static void annotationToObject(Object annotation, Object object)
    {
        if (annotation != null)
        {
            Class<?> annotationClass = annotation.getClass();
            Class<?> objectClass = object.getClass();
            for (Method m : objectClass.getMethods())
            {
                if (StringUtils.startsWith(m.getName(), "set"))
                {
                    try
                    {
                        String s = StringUtils.uncapitalize(StringUtils.substring(m.getName(), 3));
                        Object obj = annotationClass.getMethod(s).invoke(annotation);
                        if (obj != null && !"".equals(obj.toString()))
                        {
                            if (object == null)
                            {
                                object = objectClass.newInstance();
                            }
                            m.invoke(object, obj);
                        }
                    }
                    catch (Exception e)
                    {
                        // 忽略所有设置失败方法
                    }
                }
            }
        }
    }
    
    /**
     * 序列化对象
     * @param object 对象
     * @return byte[] 字节数组
     */
    public static byte[] serialize(Object object)
    {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try
        {
            if (object != null)
            {
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject(object);
                return baos.toByteArray();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 反序列化对象
     * @param bytes 字节数组
     * @return Object 对象
     */
    public static Object unserialize(byte[] bytes)
    {
        ByteArrayInputStream bais = null;
        try
        {
            if (bytes != null && bytes.length > 0)
            {
                bais = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                return ois.readObject();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 对象转字节数组
     * @param object 对象
     * @return byte[] 字节数组
     */
    public static byte[] getBytes(Object object)
    {
        if (object instanceof String)
        {
            String string = (String) object;
            byte[] bytes = null;
            try
            {
                bytes = string.getBytes(CharsetConst.CHARSET_UT);
            }
            catch (UnsupportedEncodingException e)
            {
                LoggerUtils.logError(logger, e.getMessage());
            }
            return bytes;
        }
        else
        {
            return serialize(object);
        }
    }
    
    /**
     * Object转换byte[]类型
     * @param object 对象
     * @return byte[] 字节数组
     */
    public static byte[] toBytes(Object object)
    {
        return serialize(object);
    }
    
    /**
     * byte[]型转换Object
     * @param bytes 字节数组
     * @return Object 对象
     */
    public static Object toObject(byte[] bytes)
    {
        return unserialize(bytes);
    }
}

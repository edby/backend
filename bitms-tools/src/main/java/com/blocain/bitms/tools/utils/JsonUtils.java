
package com.blocain.bitms.tools.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * <p>File：JsonUtils.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月20日 上午10:29:14</p>
 * <p>Company: BloCain</p>
 * @author Jiangsc
 * @version 1.0
 */
public class JsonUtils
{
    /**
     * 私有构造器，防止类的实例化
     */
    private JsonUtils()
    {
        super();
    }
    
    /**
     * 将Object对象转化为json字符串
     * @param object 要转化的java对象
     * @return String 转化后的json字符串
     */
    public static String beanToJson(Object object)
    {
        return JSON.toJSONString(object);
    }

    /**
     * 将Object对象转化为json字符串并指定日期格式
     * @param object 要转化的java对象
     * @param dateFormat 日期格式
     * @return String 转化后的json字符串
     */
    public static String beanToJson(Object object, String dateFormat)
    {
        return JSON.toJSONStringWithDateFormat(object, dateFormat,
                SerializerFeature.PrettyFormat);
    }

    /**
     * 将json字符串转化为java对象
     * @param json json字符串
     * @param classes java对象类型
     * @return T 转化后的java对象
     */
    public static <T> T jsonToBean(String json, Class<T> classes)
    {
        return JSON.parseObject(json, classes);
    }
}

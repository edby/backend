package com.blocain.bitms.quotation.consts;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.blocain.bitms.tools.utils.DateUtils;

/**
 * 行情服务缓存设置
 * QuotationCacheConsts Introduce
 * <p>File：QuotationCacheConsts.java</p>
 * <p>Title: QuotationCacheConsts</p>
 * <p>Description: QuotationCacheConsts</p>
 * <p>Copyright: Copyright (c) 2017/9/20</p>
 * <p>Company: BloCain</p>
 *
 * @author Jiangsc
 * @version 1.0
 */
public class QuotationCacheConsts
{
    // 缓存默认时间
    public static int                  DEFAULT_CACHE_MINUTE = 5;
    
    /**
     * 数据缓存map
     */
    private static Map<String, Object> dataMap              = new ConcurrentHashMap<String, Object>();
    
    /**
     * 数据缓存过期map
     */
    private static Map<String, Date>   dataExpireMap        = new ConcurrentHashMap<String, Date>();
    
    public static void put(String key, Object val)
    {
        dataMap.put(key, val);
    }
    
    public static Object getObject(String cacheKey)
    {
        return dataMap.get(cacheKey);
    }
    
    /**
     * 将一个key、value值放入内存缓存,并设置过期分钟数
     *
     * @param key
     * @param val
     * @param expireMiute
     */
    public static void put(String key, Object val, int expireMiute)
    {
        dataMap.put(key, val);
        dataExpireMap.put(key, DateUtils.addMinutes(new Date(), expireMiute));
    }
    
    /**
     * 从缓存中获取一个key的数据(若过期返回null)
     *
     * @param cacheKey
     * @return
     */
    public static Object get(String cacheKey)
    {
        Object obj = null;
        Date expireDate = QuotationCacheConsts.dataExpireMap.get(cacheKey);
        if (expireDate != null && expireDate.compareTo(new Date()) > 0)
        {
            obj = QuotationCacheConsts.dataMap.get(cacheKey);
        }
        return obj;
    }
    
    public static boolean containsKey(String cacheKey)
    {
        return QuotationCacheConsts.dataMap.containsKey(cacheKey);
    }
    
    public static boolean containsValue(Object value)
    {
        return QuotationCacheConsts.dataMap.containsValue(value);
    }
    
    public static void clear()
    {
        QuotationCacheConsts.dataMap.clear();
        QuotationCacheConsts.dataExpireMap.clear();
    }
}

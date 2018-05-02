package com.blocain.bitms.tools.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * 基于spring data redis 实现 Introduce
 * <p>File：RedisUtils.java</p>
 * <p>Title: RedisUtils</p>
 * <p>Description: RedisUtils</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class RedisUtils
{
    private static final Logger  logger = LoggerFactory.getLogger(RedisUtils.class);
    
    /**
     * Redis模版
     */
    private static RedisTemplate redisTemplate;
    
    public void setRedisTemplate(RedisTemplate redisTemplate)
    {
        RedisUtils.redisTemplate = redisTemplate;
    }
    
    /**
     * 消息订阅/发送消息
     * @param channel 通道
     * @param message 消息
     */
    public static void send(String channel, String message)
    {
        try
        {
            redisTemplate.convertAndSend(channel, message);
        }
        catch (Exception e)
        {
            logger.warn("send  {} : {}  ERROR:{}", channel, message, e);
        }
    }
    
    /**
     * 设置过期时间
     * @param key
     * @param time
     * @param unit
     * @return
     */
    public static boolean expire(String key, long time, TimeUnit unit)
    {
        boolean flag = false;
        try
        {
            flag = redisTemplate.expire(key, time, unit);
        }
        catch (Exception e)
        {
            logger.warn("expire {}  ERROR:{}", key, e);
        }
        return flag;
    }
    
    /**
     * 增量
     * @param key
     * @param step
     * @return
     */
    public static long increment(String key, long step)
    {
        Long value = null;
        try
        {
            value = redisTemplate.opsForValue().increment(key, step);
        }
        catch (Exception e)
        {
            logger.warn("increment {}  ERROR:{}", key, e);
        }
        return value;
    }
    
    /**
     * 删除缓存
     *
     * @param key 键
     * @return 值
     */
    public static void del(String key)
    {
        if (StringUtils.isBlank(key)) return;
        try
        {
            redisTemplate.delete(key);
        }
        catch (Exception e)
        {
            logger.warn("del {} ERROR:{}", key, e);
        }
    }
    
    /**
     * 从Map中删除指定的存储
     *
     * @param key
     * @param hashKey
     * @return 状态码，1成功，0失败
     * */
    public static Long delMap(String key, Object hashKey)
    {
        Long flag = null;
        try
        {
            flag = redisTemplate.opsForHash().delete(key, hashKey);
        }
        catch (Exception e)
        {
            logger.warn("delMap {} ERROR:{}", key, e);
        }
        return flag;
    }
    
    /**
    * 获取缓存
    *
    * @param key 键
    * @return 值
    */
    public static String get(String key)
    {
        if (StringUtils.isBlank(key)) return null;
        String value = null;
        try
        {
            return (String) redisTemplate.opsForValue().get(key);
        }
        catch (Exception e)
        {
            logger.warn("get {} ERROR:{}", key, e);
        }
        return value;
    }
    
    /**
    * 获取缓存
    *
    * @param key 键
    * @return 值
    */
    public static Object getObject(String key)
    {
        if (StringUtils.isBlank(key)) return null;
        Object value = null;
        try
        {
            return redisTemplate.opsForValue().get(key);
        }
        catch (Exception e)
        {
            logger.warn("get {} ERROR:{}", key, e);
        }
        return value;
    }
    
    /**
     * 获取Map中指定的Key
     *
     * @param cacheKey 缓存Key
     * @param key hashKey
     * @return 值
     */
    public static Object hget(String cacheKey, Object key)
    {
        if (null == key) return null;
        Object value = null;
        try
        {
            return redisTemplate.opsForHash().get(cacheKey, key);
        }
        catch (Exception e)
        {
            logger.warn("get {} ERROR:{}", key, e);
        }
        return value;
    }
    
    /**
     * 取list对象
     * @param key
     * @return
     */
    public static List<Object> getList(String key)
    {
        List<Object> dataList = Lists.newArrayList();
        try
        {
            ListOperations<String, Object> listOperation = redisTemplate.opsForList();
            Long size = listOperation.size(key);
            for (int i = 0; i < size; i++)
            {
                dataList.add(listOperation.leftPop(key));
            }
        }
        catch (Exception e)
        {
            logger.warn("get {} ERROR:{}", key, e);
        }
        return dataList;
    }
    
    /**
     * 获得缓存的list对象
     * @Title: range
     * @param @param key
     * @param @param start
     * @param @param end
     * @param @return    
     * @return List<T>    返回类型 
     * @throws
     */
    public List<Object> range(String key, long start, long end)
    {
        List<Object> data = Lists.newArrayList();
        try
        {
            ListOperations<String, Object> operations = redisTemplate.opsForList();
            data = operations.range(key, start, end);
        }
        catch (Exception e)
        {
            logger.warn("range {} ERROR:{}", key, e);
        }
        return data;
    }
    
    /**
     * list集合长度
     * @param key
     * @return
     */
    public static Long getSize(String key)
    {
        Long value = null;
        try
        {
            value = redisTemplate.opsForList().size(key);
        }
        catch (Exception e)
        {
            logger.warn("getSize {} ERROR:{}", key, e);
        }
        return value;
    }
    
    /**
     * 获得缓存的Map
     * @param key
     * @return
     */
    public static Map<String, Object> getMap(String key)
    {
        Map<String, Object> map = Maps.newHashMap();
        try
        {
            map = redisTemplate.opsForHash().entries(key);
        }
        catch (Exception e)
        {
            logger.warn("getMap {} ERROR:{}", key, e);
        }
        return map;
    }
    
    /**
     * 获得缓存的Map
     * @param key
     * @return
     */
    public static Set<Object> getKeys(String key)
    {
        Set<Object> set = Sets.newHashSet();
        try
        {
            set = redisTemplate.opsForSet().members(key);
        }
        catch (Exception e)
        {
            logger.warn("getMap {} ERROR:{}", key, e);
        }
        return set;
    }
    
    /**
     * 获得缓存的Set
     * @param key
     * @return
     */
    public static Set<Object> getSet(String key)
    {
        Set<Object> dataSet = Sets.newHashSet();
        try
        {
            BoundSetOperations<String, Object> operations = redisTemplate.boundSetOps(key);
            Long size = operations.size();
            for (int i = 0; i < size; i++)
            {
                dataSet.add(operations.pop());
            }
        }
        catch (Exception e)
        {
            logger.warn("getSet {} ERROR:{}", key, e);
        }
        return dataSet;
    }
    
    /**
    * 缓存String对象
    * @param key    缓存的键值
    * @param value    缓存的值 
    * @param cacheSeconds   过期时间
    */
    public static void putObject(String key, Object value, int cacheSeconds)
    {
        putObject(key, value, cacheSeconds, TimeUnit.SECONDS);
    }
    
    /**
     * 缓存基本的对象，Integer、String、实体类等
     * @param key    缓存的键值
     * @param value    缓存的值
     * @param cacheSeconds   过期时间
     */
    public static void putObject(String key, Object value, int cacheSeconds, TimeUnit timeUnit)
    {
        try
        {
            redisTemplate.opsForValue().set(key, value, cacheSeconds, timeUnit);
        }
        catch (Exception e)
        {
            logger.warn("putObject {} = {} ERROR:{}", key, value.toString(), e);
        }
    }
    
    /**
     * 缓存List数据
     * @param key 键
     * @param objects 值
     */
    public static void putList(String key, List<Object> objects)
    {
        try
        {
            ListOperations<String, Object> operations = redisTemplate.opsForList();
            if (null != objects)
            {
                for (Object object : objects)
                {
                    operations.rightPush(key, object);
                }
            }
        }
        catch (Exception e)
        {
            logger.warn("putList {} = {} ERROR:{}", key, objects.hashCode(), e);
        }
    }
    
    /**
     * 缓存Map
     * @param key
     * @param dataMap
     * @return
     */
    public static void putMap(String key, Map<Object, Object> dataMap)
    {
        try
        {
            if (null != dataMap)
            {
                HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
                if (operations != null)
                {
                    operations.putAll(key, dataMap);
                }
            }
        }
        catch (Exception e)
        {
            logger.warn("putMap {} = {} ERROR:{}", key, dataMap.hashCode(), e);
        }
    }
    
    /**
     * 缓存Set
     * @param key
     * @param dataSet
     */
    public static void putSet(String key, Set<Object> dataSet)
    {
        try
        {
            BoundSetOperations<String, Object> operations = redisTemplate.boundSetOps(key);
            if (operations != null)
            {
                Iterator<Object> it = dataSet.iterator();
                while (it.hasNext())
                {
                    operations.add(it.next());
                }
            }
        }
        catch (Exception e)
        {
            logger.warn("putSet {} = {} ERROR:{}", key, dataSet.hashCode(), e);
        }
    }
    
    /**
     * 向List尾部追加记录
     *
     * @param key
     * @param obj
     * @return 记录总数
     * */
    public static void leftPush(String key, Object obj)
    {
        try
        {
            redisTemplate.opsForList().leftPush(key, obj);
        }
        catch (Exception e)
        {
            logger.warn("leftPush {} = {} ERROR:{}", key, obj, e);
        }
    }
    
    /**
     * 向List头部追加记录
     *
     * @param  key
     * @param  obj
     * @return 记录总数
     * */
    public static void rightPush(String key, Object obj)
    {
        try
        {
            redisTemplate.opsForList().rightPush(key, obj);
        }
        catch (Exception e)
        {
            logger.warn("rightPush {} = {} ERROR:{}", key, obj, e);
        }
    }
    
    /**
     * 只保留start与end之间的记录
     * @param key
     * @param start
     * @param end
     */
    public static void trim(String key, int start, int end)
    {
        try
        {
            redisTemplate.opsForList().trim(key, start, end);
        }
        catch (Exception e)
        {
            logger.warn("trim {} ERROR:{}", key, e);
        }
    }
    
    /**
     * 删除List记录值为value
     * @param key
     * @param i
     * @param obj
     */
    public static void remove(String key, long i, Object obj)
    {
        try
        {
            redisTemplate.opsForList().remove(key, i, obj);
        }
        catch (Exception e)
        {
            logger.warn("remove {} ERROR:{}", key, e);
        }
    }
    
    /**
     * 获取byte[]类型Key
     *
     * @param object
     * @return
     */
    public static byte[] getBytesKey(Object object)
    {
        if (object instanceof String)
        {
            return StringUtils.getBytes((String) object);
        }
        else
        {
            return ObjectUtils.serialize(object);
        }
    }
    
    /**
     * Object转换byte[]类型
     *
     * @param object
     * @return
     */
    public static byte[] toBytes(Object object)
    {
        return ObjectUtils.serialize(object);
    }
    
    /**
     * byte[]型转换Object
     *
     * @param bytes
     * @return
     */
    public static Object toObject(byte[] bytes)
    {
        return ObjectUtils.unserialize(bytes);
    }
}

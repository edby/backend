package com.blocain.bitms.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;

import com.blocain.bitms.security.interceptor.CSRFTokenManager;

import java.util.concurrent.TimeUnit;

/**
 * Jedis 会话管理
 */
public class RedisSessionManager
{
    public static final Integer defaultExpireSeconds = 60 * 30; // 30分钟
    
    /**
     * 将对象放入session缓存
     * @param request
     * @param key
     * @param value
     * @return
     */
    public boolean put(HttpServletRequest request, SessionKey key, Object value)
    {
        return put(request, key, value, defaultExpireSeconds);
    }
    
    /**
     * 将对象放入session缓存
     * @param request
     * @param key
     * @param value
     * @param expireSecond
     * @return
     */
    public boolean put(HttpServletRequest request, SessionKey key, Object value, int expireSecond)
    {
        String realKey = getRealKey(request, key);
        if (StringUtils.isNotBlank(realKey))
        {
            RedisUtils.putObject(realKey, value, expireSecond);
            return true;
        }
        return false;
    }
    
    /**
     * 清除session中的指定值
     * @param request
     * @param key
     * @return
     */
    public boolean remove(HttpServletRequest request, SessionKey key)
    {
        String realKey = getRealKey(request, key);
        if (StringUtils.isNotBlank(realKey))
        {
            RedisUtils.del(realKey);
            return true;
        }
        return false;
    }
    
    /**
     * 清除session中的指定值
     * @param request
     * @param key
     * @return
     */
    public boolean remove(HttpServletRequest request, SessionKey sessionKey, String key)
    {
        String realKey = getRealKey(request, sessionKey, key);
        if (StringUtils.isNotBlank(realKey))
        {
            RedisUtils.del(realKey);
            return true;
        }
        return false;
    }
    
    /**
     * 从session缓存获取对象
     * @param request
     * @param key
     * @return
     */
    public Object getObject(HttpServletRequest request, SessionKey key)
    {
        String realKey = getRealKey(request, key);
        if (StringUtils.isNotBlank(realKey)) { return RedisUtils.getObject(realKey); }
        return null;
    }
    
    /**
    * 从session缓存获取对象
    * @param request
    * @param sessionKey
    * @param key
    * @return
    */
    public Object getObject(HttpServletRequest request, SessionKey sessionKey, String key)
    {
        String realKey = getRealKey(request, sessionKey, key);
        if (StringUtils.isNotBlank(realKey)) { return RedisUtils.getObject(realKey); }
        return null;
    }
    
    /**
     * 更新过期的时间
     * @param request
     * @param key
     */
    public void expire(HttpServletRequest request, SessionKey key)
    {
        String realKey = getRealKey(request, key);
        if (StringUtils.isNotBlank(realKey))
        {
            RedisUtils.expire(realKey, defaultExpireSeconds, TimeUnit.SECONDS);
        }
    }
    
    /**
     * 更新过期的时间
     * @param request
     * @param sessionKey
     * @param key
     */
    public void expire(HttpServletRequest request, SessionKey sessionKey, String key)
    {
        String realKey = getRealKey(request, sessionKey, key);
        if (StringUtils.isNotBlank(realKey))
        {
            RedisUtils.expire(realKey, defaultExpireSeconds, TimeUnit.SECONDS);
        }
    }
    
    public String getString(HttpServletRequest request, SessionKey key)
    {
        return (String) getObject(request, key);
    }
    
    public Integer getInteger(HttpServletRequest request, SessionKey key)
    {
        return (Integer) getObject(request, key);
    }
    
    public String getString(HttpServletRequest request, SessionKey sessionKey, String key)
    {
        return (String) getObject(request, sessionKey, key);
    }
    
    /**
     * 将对象放入session缓存
     * @param request
     * @param sessionKey
     * @param key
     * @param value
     * @param expireSecond
     * @return
     */
    public boolean put(HttpServletRequest request, SessionKey sessionKey, String key, Object value, int expireSecond)
    {
        String realKey = getRealKey(request, sessionKey, key);
        if (StringUtils.isNotBlank(realKey))
        {
            RedisUtils.putObject(realKey, value, expireSecond);
            return true;
        }
        return false;
    }
    
    /**
     * 构造具体的KEY
     * @param request
     * @param key
     * @return
     */
    static String getRealKey(HttpServletRequest request, SessionKey key)
    {
        String sessionId = getSessionId(request);
        if (StringUtils.isBlank(sessionId)) return null;
        return new StringBuilder(key.getValue()).append(BitmsConst.SEPARATOR).append(sessionId).toString();
    }
    
    /**
    * 构造具体的KEY
    * @param request
    * @param sessionKey
    * @param key
    * @return
    */
    static String getRealKey(HttpServletRequest request, SessionKey sessionKey, String key)
    {
        String sessionId = getSessionId(request);
        if (StringUtils.isBlank(sessionId)) return null;
        StringBuilder realKey = new StringBuilder(sessionKey.getValue()).append(BitmsConst.SEPARATOR);
        realKey.append(sessionId).append(BitmsConst.SEPARATOR).append(key);
        return realKey.toString();
    }
    
    /**
     * 清空指定session的所有缓存, 并清除随机生成的sessionID
     * @param request
     */
    public void clear(HttpServletRequest request, HttpServletResponse response)
    {
        SessionKey[] keys = SessionKey.values();
        for (SessionKey key : keys)
        {
            remove(request, key);
        }
    }
    
    /**
     * 取SESSION ID
     * @param request
     * @return
     */
    public static final String getSessionId(HttpServletRequest request)
    {
        return request.getSession().getId();
    }
    
    /**
     * 使用这给类维护key值，方便清理memcache中的指定session
     */
    public enum SessionKey
    {
        // CSRF key
        CSRF_KEY(CSRFTokenManager.CSRF_TOKEN_NAME),
        // 验证码
        CAPTCHA("captcha"),
        // 是否显示验证码
        SHOW_CAPTCHA("showCaptcha");
        private String value;
        
        SessionKey(String value)
        {
            this.value = value;
        }
        
        public String getValue()
        {
            return value;
        }
    }
}

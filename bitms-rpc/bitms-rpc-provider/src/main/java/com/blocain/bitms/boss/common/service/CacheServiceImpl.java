package com.blocain.bitms.boss.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.blocain.bitms.orm.cache.RedisCache;
import com.blocain.bitms.security.shiro.session.RedisSessionDAO;
import com.blocain.bitms.tools.exception.BusinessException;

/**
 * 缓存服务实现类
 */
@Service
public class CacheServiceImpl implements CacheService
{
    @Autowired
    private RedisTemplate redisTemplate;
    
    @Override
    public void delete(String key) throws BusinessException
    {
        redisTemplate.delete(key);
    }
    
    @Override
    public void cleanAll() throws BusinessException
    {
        redisTemplate.execute((RedisCallback) connection -> {
            connection.flushDb();
            return "ok";
        });
    }
    
    @Override
    public void cleanMybatis() throws BusinessException
    {
        for (String key : RedisCache.cacheKeys)
        {
            redisTemplate.delete(key);
        }
    }
    
    @Override
    public void cleanSession() throws BusinessException
    {
        RedisSessionDAO.clean();
    }
}

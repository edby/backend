package com.blocain.bitms.orm.core;

import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.ListUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CachePeekMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.cache.Cache;
import java.util.*;

/**
 * <p>File：AbstractIgnite.java</p>
 * <p>Title: AbstractIgnite</p>
 * <p>Description: AbstractIgnite</p>
 * <p>Copyright: Copyright (c) 2018/04/13 下午1:46:45</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class AbstractIgniteImpl<T extends GenericEntity> implements AbstractIgnite<T>
{
    @Autowired
    private Ignite ignite;
    
    @Override
    @Transactional
    public T save(String cacheName, T entity) throws BusinessException
    {
        getCache(cacheName).put(entity.getId(), entity);
        return entity;
    }
    
    @Override
    @Transactional
    public List<T> save(String cacheName, List<T> entities) throws BusinessException
    {
        if (ListUtils.isNotNull(entities))
        {
            Map map = Maps.newHashMap();
            for (T entity : entities)
            {
                map.put(entity.getId(), entity);
            }
            getCache(cacheName).putAll(map);
        }
        return entities;
    }
    
    @Override
    public T findOne(String cacheName, Long id) throws BusinessException
    {
        return getCache(cacheName).get(id);
    }
    
    @Override
    public boolean exists(String cacheName, Long id) throws BusinessException
    {
        return getCache(cacheName).containsKey(id);
    }
    
    @Override
    public List<T> findAll(String cacheName) throws BusinessException
    {
        Iterator<Cache.Entry<Long, T>> result = getCache(cacheName).iterator();
        Iterator iterator = new Iterator()
        {
            @Override
            public boolean hasNext()
            {
                return result.hasNext();
            }
            
            @Override
            public T next()
            {
                return result.next().getValue();
            }
        };
        return Lists.newArrayList(iterator);
    }
    
    @Override
    public List<T> findAll(String cacheName, List<Long> ids) throws BusinessException
    {
        TreeSet<Long> keys = Sets.newTreeSet(ids);
        Collection result = getCache(cacheName).getAll(keys).values();
        return null != result ? Lists.newArrayList(result) : null;
    }
    
    @Override
    public long count(String cacheName) throws BusinessException
    {
        return getCache(cacheName).size(CachePeekMode.PRIMARY);
    }
    
    @Override
    @Transactional
    public void delete(String cacheName, Long id) throws BusinessException
    {
        getCache(cacheName).remove(id);
    }
    
    @Override
    @Transactional
    public void deleteAll(String cacheName, List<Long> ids) throws BusinessException
    {
        getCache(cacheName).removeAll(Sets.newHashSet(ids));
    }
    
    @Override
    @Transactional
    public void deleteAll(String cacheName) throws BusinessException
    {
        getCache(cacheName).removeAll();
    }
    
    protected IgniteCache<Long, T> getCache(String cacheName)
    {
        return ignite.cache(cacheName);
    }
}

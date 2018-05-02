package com.blocain.bitms.orm.core;

import java.util.List;
import java.util.Map;

import org.apache.ignite.springdata.repository.IgniteRepository;
import org.springframework.transaction.annotation.Transactional;

import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Ignite基础服务实现类
 * <p>File：GenericIgnigeImpl.java </p>
 * <p>Title: GenericIgnigeImpl </p>
 * <p>Description:GenericIgnigeImpl </p>
 * <p>Copyright: Copyright (c) 2018/4/2</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class GenericIgnigeImpl<T extends GenericEntity> implements GenericIgnige<T>
{
    /** Ignite Cache bound to the service */
    private IgniteRepository<T, Long> igniteRepository;
    
    public GenericIgnigeImpl(IgniteRepository<T, Long> repository)
    {
        this.igniteRepository = repository;
    }
    
    /** {@inheritDoc} */
    @Override
    @Transactional
    public T save(T entity) throws BusinessException
    {
        if (null == entity) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        return igniteRepository.save(entity.getId(), entity);
    }
    
    /** {@inheritDoc} */
    @Override
    @Transactional
    public List<T> save(List<T> entities) throws BusinessException
    {
        Map<Long, T> map = Maps.newHashMap();
        for (T entity : entities)
        {
            map.put(entity.getId(), entity);
        }
        Iterable<T> result = igniteRepository.save(map);
        return null != result ? Lists.newArrayList(result) : null;
    }
    
    /** {@inheritDoc} */
    @Override
    public T findOne(Long id) throws BusinessException
    {
        if (null == id) return null;
        return igniteRepository.findOne(id);
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean exists(Long id) throws BusinessException
    {
        if (null == id) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        return igniteRepository.exists(id);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<T> findAll() throws BusinessException
    {
        return Lists.newArrayList(igniteRepository.findAll());
    }
    
    /** {@inheritDoc} */
    @Override
    public List<T> findAll(List<Long> ids) throws BusinessException
    {
        return Lists.newArrayList(igniteRepository.findAll(ids));
    }
    
    /** {@inheritDoc} */
    @Override
    public long count() throws BusinessException
    {
        return igniteRepository.count();
    }
    
    /** {@inheritDoc} */
    @Override
    @Transactional
    public void delete(Long id) throws BusinessException
    {
        igniteRepository.delete(id);
    }
    
    /** {@inheritDoc} */
    @Override
    @Transactional
    public void deleteAll(List<Long> ids) throws BusinessException
    {
        igniteRepository.deleteAll(ids);
    }
    
    /** {@inheritDoc} */
    @Override
    @Transactional
    public void deleteAll() throws BusinessException
    {
        igniteRepository.deleteAll();
    }
}

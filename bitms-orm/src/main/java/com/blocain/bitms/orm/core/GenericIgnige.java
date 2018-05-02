package com.blocain.bitms.orm.core;

import java.util.List;

import com.blocain.bitms.tools.exception.BusinessException;

/**
 * Ignite基础服务
 * <p>File：GenericIgnige.java </p>
 * <p>Title: GenericIgnige </p>
 * <p>Description:GenericIgnige </p>
 * <p>Copyright: Copyright (c) 2018/4/2</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public interface GenericIgnige<T extends GenericEntity>
{
    /**
     * Saves a given entity using provided key.
     * @param entity
     * @return T
     * @throws BusinessException
     */
    T save(T entity) throws BusinessException;
    
    /**
     * Saves all given keys and entities combinations.
     * @param entities
     * @return {@link List}
     * @throws BusinessException
     */
    List<T> save(List<T> entities) throws BusinessException;
    
    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal null} if none found
     * @throws BusinessException if {@code id} is {@literal null}
     */
    T findOne(Long id) throws BusinessException;
    
    /**
     * Returns whether an entity with the given id exists.
     *
     * @param id must not be {@literal null}.
     * @return true if an entity with the given id exists, {@literal false} otherwise
     * @throws BusinessException if {@code id} is {@literal null}
     */
    boolean exists(Long id) throws BusinessException;
    
    /**
     * Returns all instances of the type.
     *
     * @return all entities
     * @throws BusinessException
     */
    List<T> findAll() throws BusinessException;
    
    /**
     * Returns all instances of the type with the given IDs.
     *
     * @param ids
     * @return
     * @throws BusinessException
     */
    List<T> findAll(List<Long> ids) throws BusinessException;
    
    /**
     * Returns the number of entities available.
     *
     * @return the number of entities
     * @throws BusinessException
     */
    long count() throws BusinessException;
    
    /**
     * Deletes the entity with the given id.
     *
     * @param id must not be {@literal null}.
     * @throws BusinessException in case the given {@code id} is {@literal null}
     */
    void delete(Long id) throws BusinessException;
    
    /**
     * Deletes all the entities for the provided ids.
     *
     * @param ids List of ids to delete.
     * @throws BusinessException
     */
    void deleteAll(List<Long> ids) throws BusinessException;
    
    /**
     * Deletes all entities managed by the repository.
     * @throws BusinessException
     */
    void deleteAll() throws BusinessException;
}

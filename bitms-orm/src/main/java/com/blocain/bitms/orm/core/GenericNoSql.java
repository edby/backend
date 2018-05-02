package com.blocain.bitms.orm.core;

import java.util.List;

import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.exception.BusinessException;

/**
 * 非关系型数据库基础服务
 * <p>File：GenericNoSql.java</p>
 * <p>Title: GenericNoSql</p>
 * <p>Description:GenericNoSql</p>
 * <p>Copyright: Copyright (c) 2015 2015-8-7 下午6:09:36</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 * @author Playguy 
 */
public interface GenericNoSql<T extends GenericEntity>
{
    /**
     * 插入或修改数据
     * @param entity
     * @return {@link Integer}
     * @throws BusinessException
     */
    void save(T entity) throws BusinessException;
    
    /**
     * 逻辑删除
     * @param id
     * @return {@link Integer}
     * @throws BusinessException
     */
    void delete(Long id) throws BusinessException;
    
    /**
     * 删除数据(物理删除）
     * @param id
     * @return {@link Integer}
     * @throws BusinessException
     */
    void remove(Long id) throws BusinessException;
    
    /**
     * 批量插入
     * @param list
     * @return {@link Integer}
     * @throws BusinessException
     */
    void insertBatch(List<T> list) throws BusinessException;
    
    /**
     * 批量逻辑删除
     * @param ids
     * @return {@link Integer}
     * @throws BusinessException
     */
    void deleteBatch(Long[] ids) throws BusinessException;
    
    /**
     * 批量删除数据(物理删除）
     * @param ids
     * @return {@link Integer}
     * @throws BusinessException
     */
    void removeBatch(Long[] ids) throws BusinessException;
    
    /**
     * 根据主键查询数据
     * @param id
     * @return {@link T}
     * @throws BusinessException
     */
    T selectByPrimaryKey(Long id) throws BusinessException;
    
    /**
     * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<T>());
     * @param entity
     * @return {@link List}
     * @throws BusinessException
     */
    List<T> findList(T entity) throws BusinessException;
    
    /**
     * 查询所有数据列表
     * @return {@link List}
     * @throws BusinessException
     */
    List<T> selectAll() throws BusinessException;
    
    /**
     * 分页查询
     * @param pagin
     * @param entity
     * @return {@link PaginateResult}
     * @throws BusinessException
     */
    PaginateResult<T> search(Pagination pagin, T entity) throws BusinessException;
}

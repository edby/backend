package com.blocain.bitms.orm.core;

import java.util.List;

import com.blocain.bitms.orm.annotation.SlaveDataSource;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.exception.BusinessException;

/**
 * <p>File：GenericService.java</p>
 * <p>Title: GenericService</p>
 * <p>Description:GenericService</p>
 * <p>Copyright: Copyright (c) 2015 2015-8-7 下午6:09:36</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 * @author Playguy 
 */
public interface GenericService<T extends GenericEntity>
{
    /**
     * 插入或修改数据
     * @param entity
     * @return {@link Integer}
     * @throws BusinessException
     */
    int save(T entity) throws BusinessException;
    
    /**
    * 插入数据
    * @param entity
    * @return {@link Integer}
    * @throws BusinessException
    */
    int insert(T entity) throws BusinessException;
    
    /**
     * 逻辑删除
     * @param id
     * @return {@link Integer}
     * @throws BusinessException
     */
    int delete(Long id) throws BusinessException;
    
    /**
     * 删除数据(物理删除）
     * @param id
     * @return {@link Integer}
     * @throws BusinessException
     */
    int remove(Long id) throws BusinessException;
    
    /**
     * 批量插入
     * @param list
     * @return {@link Integer}
     * @throws BusinessException
     */
    int insertBatch(List<T> list) throws BusinessException;
    
    /**
     *  批量更新
     * @param list
     * @return {@link Integer}
     * @throws BusinessException
     */
    int updateBatch(List<T> list) throws BusinessException;
    
    /**
     * 批量逻辑删除
     * @param ids
     * @return {@link Integer}
     * @throws BusinessException
     */
    int deleteBatch(Long[] ids) throws BusinessException;
    
    /**
     * 批量逻辑删除
     * @param ids
     * @return {@link Integer}
     * @throws BusinessException
     */
    int deleteBatch(String[] ids) throws BusinessException;
    
    /**
     * 批量删除数据(物理删除）
     * @param ids
     * @return {@link Integer}
     * @throws BusinessException
     */
    int removeBatch(Long[] ids) throws BusinessException;
    
    /**
     * 批量删除数据(物理删除）
     * @param ids
     * @return {@link Integer}
     * @throws BusinessException
     */
    int removeBatch(String[] ids) throws BusinessException;
    
    /**
     * 根据条件是否插入数据
     * @param record
     * @throws BusinessException
     */
    void insertSelective(T record) throws BusinessException;
    
    /**
     * 根据主键查询数据
     * @param id
     * @return {@link T}
     * @throws BusinessException
     */
    T selectByPrimaryKey(Long id) throws BusinessException;
    
    /**
     * 选择性更新数据
     * @param record
     * @return {@link Integer}
     * @throws BusinessException
     */
    int updateByPrimaryKeySelective(T record) throws BusinessException;
    
    /**
     * 根据主键更新一条信息所有数据
     * @param record
     * @return {@link Integer}
     * @throws BusinessException
     */
    int updateByPrimaryKey(T record) throws BusinessException;
    
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
     * <p>
     *     主要针对单表操作，分页对象需放置在参数对象中
     * </p>
     * @param entity
     * @return {@link PaginateResult}
     * @throws BusinessException
     */
    //@SlaveDataSource()
    PaginateResult<T> search(T entity) throws BusinessException;
    
    /**
     * 分页查询
     * @param pagin
     * @param entity
     * @return {@link PaginateResult}
     * @throws BusinessException
     */
    //@SlaveDataSource()
    PaginateResult<T> search(Pagination pagin, T entity) throws BusinessException;
}

package com.blocain.bitms.orm.core;

import java.util.List;

/**
 * <p>File：GenericMapper.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2015 2015-4-17 下午3:24:01</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 * @param <T>
 */
public interface GenericMapper<T extends GenericEntity>
{
    /**
     * 插入数据
     * @param entity
     * @return
     */
    int insert(T entity);
    
    /**
     * 逻辑删除
     * @param refrencdId
     *  @author chenjp
     * @return
     */
    int delete(Long refrencdId);
    
    /**
     * 物理删除
     * @param id
     * @see public int delete(T entity)
     * @return
     */
    int remove(Long id);
    
    /**
     * 根据条件是否插入数据
     * @param record
     */
    void insertSelective(T record);
    
    /**
     * 根据主键查询数据
     * @param id
     * @return
     */
    T selectByPrimaryKey(Long id);
    
    /**
     * 选择性更新数据
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(T record);
    
    /**
     * 根据主键更新一条信息所有数据
     * @param record
     * @return
     */
    int updateByPrimaryKey(T record);
    
    /**
     * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<T>());
     * @param entity
     * @return
     */
    List<T> findList(T entity);
    
    /**
     * 查询所有数据列表
     * @return
     */
    List<T> selectAll();
    
    /**
     * 批量插入
     * @author
     * @param list
     * @return
     */
    int insertBatch(List<T> list);
    
    /**
     * 批量更新
     * @author
     * @param list
     * @return
     */
    int updateBatch(List<T> list);
}

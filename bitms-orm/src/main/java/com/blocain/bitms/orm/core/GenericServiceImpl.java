package com.blocain.bitms.orm.core;

import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.blocain.bitms.tools.bean.BeanValidators;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.SerialnoUtils;

/**
 * <p>File：GenericServiceImpl.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2015 2015-8-7 下午6:10:09</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class GenericServiceImpl<T extends GenericEntity> implements GenericService<T>
{
    protected static final Logger logger = LoggerFactory.getLogger(GenericServiceImpl.class);
    
    private GenericMapper<T>      dao;
    
    /**
     * 验证Bean实例对象
     */
    @Autowired
    protected Validator           validator;
    
    public GenericServiceImpl()
    {
    }
    
    public GenericServiceImpl(GenericMapper<T> dao)
    {
        this.dao = dao;
    }
    
    @Override
    public int save(T entity) throws BusinessException
    {
        this.beanValidator(entity);
        if (null == entity.getId())
        {
            entity.setId(SerialnoUtils.buildPrimaryKey());
            return dao.insert(entity);
        }
        else
        {
            return dao.updateByPrimaryKeySelective(entity);
        }
    }
    
    @Override
    public int insert(T entity) throws BusinessException
    {
        this.beanValidator(entity);
        if (null == entity.getId())
        {
            entity.setId(SerialnoUtils.buildPrimaryKey());
        }
        return dao.insert(entity);
    }
    
    @Override
    public int delete(Long id) throws BusinessException
    {
        return dao.delete(id);
    }
    
    @Override
    public int remove(Long id) throws BusinessException
    {
        return dao.remove(id);
    }
    
    @Override
    public int deleteBatch(Long[] ids) throws BusinessException
    {
        int count = 0;
        for (Long id : ids)
        {
            count += delete(id);
        }
        return count;
    }
    
    @Override
    public int deleteBatch(String[] ids) throws BusinessException
    {
        int count = 0;
        for (String id : ids)
        {
            count += delete(Long.parseLong(id));
        }
        return count;
    }
    
    @Override
    public int removeBatch(String[] ids) throws BusinessException
    {
        int count = 0;
        for (String id : ids)
        {
            count += remove(Long.parseLong(id));
        }
        return count;
    }
    
    @Override
    public int removeBatch(Long[] ids) throws BusinessException
    {
        int count = 0;
        for (Long id : ids)
        {
            count += remove(id);
        }
        return count;
    }
    
    @Override
    public void insertSelective(T record) throws BusinessException
    {
        dao.insertSelective(record);
    }
    
    @Override
    public T selectByPrimaryKey(Long id) throws BusinessException
    {
        return dao.selectByPrimaryKey(id);
    }
    
    @Override
    public int updateByPrimaryKeySelective(T record) throws BusinessException
    {
        return dao.updateByPrimaryKeySelective(record);
    }
    
    @Override
    public int updateByPrimaryKey(T record) throws BusinessException
    {
        return dao.updateByPrimaryKey(record);
    }
    
    @Override
    public List<T> findList(T entity) throws BusinessException
    {
        return dao.findList(entity);
    }
    
    @Override
    public List<T> selectAll() throws BusinessException
    {
        return dao.selectAll();
    }
    
    @Override
    public int insertBatch(List<T> list) throws BusinessException
    {
        return dao.insertBatch(list);
    }
    
    @Override
    public int updateBatch(List<T> list) throws BusinessException
    {
        return dao.updateBatch(list);
    }
    
    @Override
    public PaginateResult<T> search(T entity) throws BusinessException
    {
        Pagination pagin = entity.getPagin();
        if (null == pagin) pagin = new Pagination();
        entity.setPagin(pagin);
        List<T> data = findList(entity);
        return new PaginateResult<>(pagin, data);
    }
    
    @Override
    public PaginateResult<T> search(Pagination pagin, T entity) throws BusinessException
    {
        if (null == pagin) pagin = new Pagination();
        entity.setPagin(pagin);
        List<T> data = findList(entity);
        return new PaginateResult<>(pagin, data);
    }
    
    /**
     * 服务端参数有效性验证
     * 
     * @param object 验证的实体对象
     * @param groups 验证组
     * @throws BusinessException
     * @return 验证成功：返回true；严重失败：将错误信息添加到 jsonMessage rows 中
     */
    protected boolean beanValidator(Object object, Class<?> ... groups) throws BusinessException
    {
        try
        {
            BeanValidators.validateWithException(validator, object, groups);
        }
        catch (ConstraintViolationException ex)
        {
            List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
            throw new BusinessException(CommonEnums.FAIL, list);
        }
        return true;
    }
}

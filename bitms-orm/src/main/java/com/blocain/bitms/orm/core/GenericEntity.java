package com.blocain.bitms.orm.core;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import com.alibaba.fastjson.annotation.JSONField;
import com.blocain.bitms.tools.bean.Pagination;

import io.swagger.annotations.ApiModelProperty;

/**
 * <p>File：BaseEntity.java</p>
 * <p>Title: BaseEntity</p>
 * <p>Description:基础实体对象</p>
 * <p>Copyright: Copyright (c) 2015/04/21 11:52</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class GenericEntity implements Serializable
{
    private static final long serialVersionUID = -5070399515553795258L;
    
    /**
     * 主键编号
     */
    @Id
    @ApiModelProperty(value = "主键编号")
    protected Long            id;
    
    /**
     * 删除标识
     */
    @JSONField(serialize = false)
    @ApiModelProperty(value = "删除标识")
    protected Boolean         delFlag          = false;
    
    /**
     * 分页对象
     * <p>用于动态加入SQL分页语句的对象</p>
     */
    @JSONField(serialize = false)
    @ApiModelProperty(value = "分页对象")
    private Pagination        pagin;
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public Boolean getDelFlag()
    {
        return delFlag;
    }
    
    public void setDelFlag(Boolean delFlag)
    {
        this.delFlag = delFlag;
    }
    
    public Pagination getPagin()
    {
        return pagin;
    }
    
    public void setPagin(Pagination pagin)
    {
        this.pagin = pagin;
    }
}

/*
 * @(#)JsonMessage.java 2014-4-17 下午4:04:43
 * Copyright 2014 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * <p>File：JsonMessage.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2014 2014-4-17 下午4:04:43</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "JSON返回对象")
public class JsonMessage implements Serializable
{
    //
    private static final long serialVersionUID = 714679657596837388L;
    
    public JsonMessage()
    {
    }
    
    public JsonMessage(EnumDescribable enumDescribable)
    {
        this.code = enumDescribable.getCode();
        this.message = enumDescribable.getMessage();
    }
    
    public JsonMessage(EnumDescribable enumDescribable, Object object)
    {
        this.object = object;
        this.code = enumDescribable.getCode();
        this.message = enumDescribable.getMessage();
    }
    
    public JsonMessage(Integer code, String message)
    {
        this.code = code;
        this.message = message;
    }
    
    @ApiModelProperty(value = "编码")
    private Integer code;
    
    @ApiModelProperty(value = "消息")
    private String  message;
    
    @ApiModelProperty(value = "对象")
    private Object  object;
    
    @ApiModelProperty(value = "数据集")
    private List<?> rows;
    
    @ApiModelProperty(value = "总记录数")
    private Long    total;
    
    // 分页查询时, 当前页码
    @ApiModelProperty(value = "当前页码")
    private Integer pages;
    
    // 分页查询时，总共页数
    @ApiModelProperty(value = "总页数")
    private Integer totalPage;
    
    // 是否有下一页
    @ApiModelProperty(value = "是否有下一页")
    private Boolean hasNext;
    
    // 是否有上一页
    @ApiModelProperty(value = "是否有上一页")
    private Boolean hasPrevious;
    
    @ApiModelProperty(hidden = true, value = "CSRF 口令")
    private String  csrf;
    
    public Long getTotal()
    {
        return total;
    }
    
    public void setTotal(Long total)
    {
        this.total = total;
    }
    
    public Integer getCode()
    {
        return code;
    }
    
    public void setCode(Integer code)
    {
        this.code = code;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    public Object getObject()
    {
        return object;
    }
    
    public void setObject(Object object)
    {
        this.object = object;
    }
    
    public List<?> getRows()
    {
        return rows;
    }
    
    public void setRows(List<?> rows)
    {
        this.rows = rows;
    }
    
    public Integer getPages()
    {
        return pages;
    }
    
    public void setPages(Integer pages)
    {
        this.pages = pages;
    }
    
    public Integer getTotalPage()
    {
        return totalPage;
    }
    
    public void setTotalPage(Integer totalPage)
    {
        this.totalPage = totalPage;
    }
    
    public Boolean getHasNext()
    {
        return hasNext;
    }
    
    public void setHasNext(Boolean hasNext)
    {
        this.hasNext = hasNext;
    }
    
    public Boolean getHasPrevious()
    {
        return hasPrevious;
    }
    
    public void setHasPrevious(Boolean hasPrevious)
    {
        this.hasPrevious = hasPrevious;
    }
    
    public String getCsrf()
    {
        return csrf;
    }
    
    public void setCsrf(String csrf)
    {
        this.csrf = csrf;
    }
}
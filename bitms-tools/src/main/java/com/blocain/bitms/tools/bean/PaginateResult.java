/*
 * @(#)PaginateResult.java 2014-1-8 下午1:32:17
 * Copyright 2014 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * <p>File：PaginateResult.java</p>
 * <p>Title: 分页查询结果对象</p>
 * <p>Description:封装Pagination及List对象，供前端调用及显示分页结果</p>
 * <p>Copyright: Copyright (c) 2014 2014-1-8 下午1:32:17</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "分页查询结果对象")
public class PaginateResult<T> implements Serializable
{
    private static final long serialVersionUID = 3612116988761318827L;
    
    public PaginateResult()
    {
        super();
    }
    
    public PaginateResult(Pagination page, List<T> list)
    {
        this.page = page;
        this.list = list;
    }

    @ApiModelProperty(value = "分页对象")
    private Pagination page;

    @ApiModelProperty(value = "数据列表")
    private List<T>    list;
    
    public Pagination getPage()
    {
        return page;
    }
    
    public void setPage(Pagination page)
    {
        this.page = page;
    }
    
    public List<T> getList()
    {
        return list;
    }
    
    public void setList(List<T> list)
    {
        this.list = list;
    }
}

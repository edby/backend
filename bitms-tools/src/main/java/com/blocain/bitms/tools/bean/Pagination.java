/*
 * @(#)Pagination.java 2014-1-8 下午1:30:30
 * Copyright 2014 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.bean;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.blocain.bitms.tools.consts.BitmsConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>File：Pagination.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2014 2014-1-8 下午1:30:30</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "分页对象,客户端只需传ROWS，PAGE两个参数即可")
public class Pagination implements Serializable
{
    private static final long serialVersionUID = -4312266410100339520L;
    
    // 是否有上一页
    @ApiModelProperty(value = "是否有上一页", hidden = true)
    protected Boolean         hasPreviousPage;
    
    // 是否有下一页
    @ApiModelProperty(value = "是否有下一页", hidden = true)
    protected Boolean         hasNextPage;
    
    // 每页的记录数
    @ApiModelProperty(value = "分页大小")
    protected Integer         rows             = BitmsConst.DEFAULT_PAGE_SIZE;
    
    // 当前是第几页
    @ApiModelProperty(value = "当前页数")
    protected Integer         page             = BitmsConst.DEFAULT_CURRENT_PAGE;
    
    // 记录开始位置
    @ApiModelProperty(value = "记录开始位置", hidden = true)
    protected Integer         startIndex       = BitmsConst.DEFAULT_START_INDEX;
    
    // 记录结束位置
    @ApiModelProperty(value = "记录结束位置", hidden = true)
    protected Integer         endIndex         = BitmsConst.DEFAULT_START_INDEX;
    
    // 记录的总数量
    @ApiModelProperty(value = "记录的总数量", hidden = true)
    protected Long            totalRows;
    
    // 记录的总页数
    @ApiModelProperty(value = "记录的总页数", hidden = true)
    protected Integer         totalPage;
    
    @JSONField(serialize = false)
    @ApiModelProperty(value = "排序字段", hidden = true)
    private String            sort             = "id";
    
    @JSONField(serialize = false)
    @ApiModelProperty(value = "排序方式", hidden = true)
    private String            order            = "desc";
    
    /**
     * 构造器一
     */
    public Pagination()
    {
        super();
    }
    
    public Pagination(Integer rows)
    {
        this.rows = rows;
    }
    
    /**
     * 构造器二
     */
    public Pagination(Integer page, Integer rows)
    {
        this.rows = rows;
        this.page = page;
        this.startIndex = (this.page - 1) * this.rows;
    }
    
    /**
     * 构造器三
     */
    public Pagination(Boolean hasPreviousPage, Boolean hasNextPage, Integer rows, Integer totalPage, Integer page, Integer startIndex, Long totalRows)
    {
        this.hasPreviousPage = hasPreviousPage;
        this.hasNextPage = hasNextPage;
        this.totalPage = totalPage;
        this.startIndex = startIndex;
        this.totalRows = totalRows;
        this.rows = rows;
        this.page = page;
    }
    
    /**
     * 取得是否有上页的标记
     *
     * @return boolean 是否有上页的标记(true：有,false：无)
     */
    public Boolean getHasPreviousPage()
    {
        return hasPreviousPage;
    }
    
    /**
     * 设置是否有上页的标记
     *
     * @param hasPreviousPage 是否有上页的标记(true：有,false：无)
     */
    public void setHasPreviousPage(Boolean hasPreviousPage)
    {
        this.hasPreviousPage = hasPreviousPage;
    }
    
    /**
     * 取得是否有下页的标记
     *
     * @return boolean 是否有下页的标记(true：有,false：无)
     */
    public Boolean getHasNextPage()
    {
        return hasNextPage;
    }
    
    /**
     * 设置是否有下页的标记
     *
     * @param hasNextPage 是否有下页的标记(true：有,false：无)
     */
    public void setHasNextPage(Boolean hasNextPage)
    {
        this.hasNextPage = hasNextPage;
    }
    
    /**
     * 取得每页显示的资料笔数
     *
     * @return int 每页显示的资料笔数(默认为20)
     */
    public Integer getRows()
    {
        if (rows > 100) { return 100; }
        return rows;
    }
    
    /**
     * 设置每页显示的资料笔数
     *
     * @param rows 每页显示的资料笔数(默认为20)
     */
    public void setRows(Integer rows)
    {
        if (rows < 1)
        {
            this.rows = BitmsConst.DEFAULT_PAGE_SIZE;
        }
        else
        {
            this.rows = rows;
        }
    }
    
    /**
     * 取得当前显示的页标
     *
     * @return int 当前显示的页标
     */
    public Integer getPage()
    {
        return page;
    }
    
    /**
     * 设置当前显示的页标
     *
     * @param page 当前显示的页标
     */
    public void setPage(Integer page)
    {
        if (page < 1)
        {
            this.page = BitmsConst.DEFAULT_CURRENT_PAGE;
        }
        else
        {
            this.page = page;
        }
    }
    
    /**
     * 取得记录开始位置
     *
     * @return int 记录开始位置
     */
    public Integer getStartIndex()
    {
        if (startIndex == 0) { return (this.page - 1) * this.rows; }
        return startIndex;
    }
    
    /**
     * 设置记录开始位置
     *
     * @param startIndex 记录开始位置
     */
    public void setStartIndex(Integer startIndex)
    {
        this.startIndex = startIndex;
    }
    
    /**
     * 取分页结束位置
     * @return
     */
    public Integer getEndIndex()
    {
        return getStartIndex() + rows;
    }
    
    /**
     * 设置分页结束位置
     * @return
     */
    public void setEndIndex(Integer endIndex)
    {
        this.endIndex = endIndex;
    }
    
    /**
     * 取得资料总笔数
     *
     * @return int 资料总笔数
     */
    public Long getTotalRows()
    {
        return totalRows;
    }
    
    /**
     * 设置资料总笔数
     *
     * @param totalRows 资料总笔数
     */
    public void setTotalRows(Long totalRows)
    {
        this.totalRows = totalRows;
        this.totalPage = new Double(Math.ceil(1.0 * totalRows / this.getRows())).intValue();
        this.hasNextPage = this.getTotalPage() > this.getPage();
        this.hasPreviousPage = this.getPage() > 1;
    }
    
    /**
     * 取得资料总页数
     *
     * @return int 资料总页数
     */
    public Integer getTotalPage()
    {
        return totalPage;
    }
    
    /**
     * 设置资料总页数
     *
     * @param totalPage 资料总页数
     */
    public void setTotalPage(Integer totalPage)
    {
        this.totalPage = totalPage;
    }
    
    public String getSort()
    {
        return sort;
    }
    
    public void setSort(String sort)
    {
        this.sort = sort;
    }
    
    public String getOrder()
    {
        return order;
    }
    
    public void setOrder(String order)
    {
        this.order = order;
    }
}

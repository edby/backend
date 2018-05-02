/*
 * @(#)PaginateUtils.java 2014-1-8 下午1:16:10
 * Copyright 2014 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.orm.utils;

import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;

/**
 * <p>File：PaginateUtils.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2014 2014-1-8 下午1:16:10</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class PaginateUtils
{
    // 私有构造器，防止类的实例化
    private PaginateUtils()
    {
        super();
    }
    
    /**
     * 根据Pagination对象取得完整的Pagination对象(当每次查询列表之前用count查总记录数时用该方法)
     * @param page 包含currentPage,pageSize,totalCount的Pagination对象
     * @param totalCount 资料总笔数
     * @return Pagination 完整的Pagination对象
     */
    public static Pagination getPagination(Pagination page, long totalCount)
    {
        if (page == null) // 首次查询请求
        {
            return getPagination(BitmsConst.DEFAULT_PAGE_SIZE, BitmsConst.DEFAULT_CURRENT_PAGE, totalCount);
        }
        else
        {
            return getPagination(page.getRows(), page.getPage(), totalCount);
        }
    }
    
    /**
     * 根据Pagination对象取得完整的Pagination对象并具体指定每页记录数
     * @param page Pagination对象
     * @param totalCount 资料总笔数
     * @param default_pageSize 每页记录数
     * @return Pagination Pagination对象
     */
    public static Pagination getPaginate(Pagination page, long totalCount, int default_pageSize)
    {
        if (null == page) page = PaginateUtils.getPagination(default_pageSize, BitmsConst.DEFAULT_CURRENT_PAGE, totalCount);
        else page = PaginateUtils.getPagination(page.getRows(), page.getPage(), totalCount);
        return page;
    }
    
    /**
     * 根据每页笔数、当前页数、记录总数取得完整的Pagination对象(缓存count时用该方法)
     * @param pageSize 每页显示的资料笔数
     * @param currentPage 当前显示的页数
     * @param totalCount 资料总笔数
     * @return Pagination Pagination对象
     */
    public static Pagination getPagination(int pageSize, int currentPage, long totalCount)
    {
        pageSize = getPageSize(pageSize);
        int totalPage = getTotalPage(pageSize, totalCount);
        currentPage = getCurrentPage(currentPage, totalPage);
        int startIndex = getStartIndex(currentPage, pageSize);
        boolean hasNextPage = getHasNextPage(currentPage, totalPage);
        boolean hasPreviousPage = getHasPreviousPage(currentPage);
        return new Pagination(hasPreviousPage, hasNextPage, pageSize, totalPage, currentPage, startIndex, totalCount);
    }
    
    /**
     * 取得分页下拉菜单数组
     * @param pageCount 当前的资料共有多少页
     * @return Integer[] 分页下拉菜单数组
     */
    public static Integer[] getPageSelect(int pageCount)
    {
        Integer[] pageSelect = new Integer[pageCount];
        for (int i = 1; i <= pageCount; i++)
        {
            pageSelect[i - 1] = i;
        }
        return pageSelect;
    }
    
    /**
     * 根据每页显示记录数及记录总数取得记录总页数
     * @param pageSize 每页显示记录数
     * @param totalCount 记录总数
     * @return int 记录总页数
     */
    private static int getTotalPage(int pageSize, long totalCount)
    {
        return pageSize == 0 ? 1 : (int) Math.ceil((double) totalCount / (double) pageSize);
    }
    
    /**
     * 根据当前页标取得是否有上一页的标记(根据此方法设定上一页,首页是否有超链接)
     * @param currentPage 当前显示的页标
     * @return boolean 是否有上一页的标记(true：有,false：无)
     */
    private static boolean getHasPreviousPage(int currentPage)
    {
        return currentPage == 1 ? false : true;
    }
    
    /**
     * 根据当前显示的页数及资料总页数判断是否该有下一页
     * @param currentPage 当前显示的页数
     * @param totalPage 资料总页数
     * @return boolean 否该有下一页(true：有,false：无)
     */
    private static boolean getHasNextPage(int currentPage, int totalPage)
    {
        return currentPage == totalPage || totalPage == 0 ? false : true;
    }
    
    /**
     * 取得任一页第一条数据在数据集的位置
     * @param currentPage 当前显示的页数
     * @param pageSize 每页显示的资料笔数
     * @return int 任一页第一条数据在数据集的位置
     */
    private static int getStartIndex(int currentPage, int pageSize)
    {
        return (currentPage - 1) * pageSize;
    }
    
    /**
     * 取得当前显示的页数
     * @param currentPage 当前显示的页数
     * @param totalPage 总页数
     * @return int 当前显示的页数
     */
    private static int getCurrentPage(int currentPage, int totalPage)
    {
        if (currentPage <= 0 || totalPage == 0) { return BitmsConst.DEFAULT_CURRENT_PAGE; }
        if (currentPage > totalPage) { return totalPage; }
        return currentPage;
    }
    
    /**
     * 取得每页显示的资料笔数
     * @param pageSize 每页显示的资料笔数
     * @return int 每页显示的资料笔数
     */
    private static int getPageSize(int pageSize)
    {
        return pageSize <= 0 ? BitmsConst.DEFAULT_PAGE_SIZE : pageSize;
    }
}

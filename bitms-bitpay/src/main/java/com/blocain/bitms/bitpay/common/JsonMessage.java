package com.blocain.bitms.bitpay.common;

import java.util.List;


/**
 * <p>File：JsonMessage.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月18日 下午12:59:53</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
public class JsonMessage  
{
    // 交易状态
    private Integer code;

    // 交易码内容描述
    private String  message;

    // 需要存储的对象
    private Object  object;
    
    // 需要存储的对象数据
    private Object result;

    // 分页查询时, 当前页码
    private Integer pageNum;
    
    // 分页查询时, 开始页码
    private Integer firstPage;
    
    // 分页查询时, 结束页码
    private Integer lastPage;

    // 分页查询时，总共页数
    private Integer pages;

    // 分页查询时，返回记录总数
    private Long    total;

    // 是否有下一页
    private Boolean hasNextPage;

    // 是否有上一页
    private Boolean hasPreviousPage;
    
    private int[] navigatepageNums;

    // 分页查询时，返回的记录集
    private List<?> rows;
    
    // 分页查询时，返回的记录集
    private List<?> list;

    public JsonMessage() {

    }

    public JsonMessage(ErrorCodeDescribable errorCodeDescribable) {
        this.code = errorCodeDescribable.getCode();
        this.message = errorCodeDescribable.getMessage();
    }

    /**
     * @param code
     * @param message
     * @param rows
     */
    public JsonMessage(ErrorCodeDescribable errorCodeDescribable, List<?> rows)
    {
        super();
        this.code = errorCodeDescribable.getCode();
        this.message = errorCodeDescribable.getMessage();
        this.rows = rows;
    }

    public Integer getPageNum()
    {
        return pageNum;
    }

    public void setPageNum(Integer pageNum)
    {
        this.pageNum = pageNum;
    }

    public Integer getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(Integer firstPage) {
        this.firstPage = firstPage;
    }

    public Integer getLastPage() {
        return lastPage;
    }

    public void setLastPage(Integer lastPage) {
        this.lastPage = lastPage;
    }

    public Integer getPages()
    {
        return pages;
    }

    public void setPages(Integer pages)
    {
        this.pages = pages;
    }

    public Boolean getHasNextPage()
    {
        return hasNextPage;
    }

    public void setHasNextPage(Boolean hasNextPage)
    {
        this.hasNextPage = hasNextPage;
    }

    public Boolean getHasPreviousPage()
    {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(Boolean hasPreviousPage)
    {
        this.hasPreviousPage = hasPreviousPage;
    }

    public int[] getNavigatepageNums() {
        return navigatepageNums;
    }

    public void setNavigatepageNums(int[] navigatepageNums) {
        this.navigatepageNums = navigatepageNums;
    }

    public Long getTotal()
    {
        return total;
    }

    public void setTotal(Long total)
    {
        this.total = total;
    }

    public List<?> getRows()
    {
        return rows;
    }

    public void setRows(List<?> rows)
    {
        this.rows = rows;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
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
    
    public Object getResult()
    {
        return result;
    }

    public void setResult(Object result)
    {
        this.result = result;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setErrorCodeDescribable(ErrorCodeDescribable errorCodeDescribable){
        this.code = errorCodeDescribable.getCode();
        this.message = errorCodeDescribable.getMessage();
    }   
}
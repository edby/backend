package com.blocain.bitms.bitpay.common;

import java.io.Serializable;

/**
 * <p>File：Pagination.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月18日 上午11:23:54</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
public class Pagination implements Serializable
{
    //
	private static final long	serialVersionUID	= -4312266410100339520L;

	/**
     * 构造器一
     */
    public Pagination()
    {
        super();
    }

    public Pagination(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 构造器二
     */
    public Pagination(Integer pageNo, Integer pageSize)
    {
        setPageNo(pageNo);
        setPageSize(pageSize);
        this.startIndex = (this.pageNo - 1) * this.pageSize;
    }
    
    public Pagination(Integer pageNo, Integer pageSize, Boolean isCount)
    {
        setPageNo(pageNo);
        setPageSize(pageSize);
        this.startIndex = (this.pageNo - 1) * this.pageSize;
        this.isCount = isCount;
    }

    /**
     * 构造器三
     */
    public Pagination(Boolean hasPreviousPage, Boolean hasNextPage,
            Integer pageSize, Integer totalPage, Integer currentPage, Integer startIndex,
            Long totalCount)
    {
        this.hasPreviousPage = hasPreviousPage;
        this.hasNextPage = hasNextPage;
        setPageNo(currentPage);
        setPageSize(pageSize);
        this.totalPage = totalPage;
        this.startIndex = startIndex;
        this.totalCount = totalCount;
    }

    // 是否有上一页
    @SuppressWarnings("unused")
	private Boolean hasPreviousPage;

    // 是否有下一页
    @SuppressWarnings("unused")
	private Boolean hasNextPage;

    // 每页的记录数
    private Integer     pageSize    = ApplicationConst.DEFAULT_PAGE_SIZE;

    // 当前是第几页
    private Integer     pageNo = ApplicationConst.DEFAULT_CURRENT_PAGE;

    // 记录开始位置
    private Integer     startIndex;

    // 记录的总数量
    private Long    totalCount;

    // 记录的总页数
    private Integer     totalPage;
    
    private Boolean isCount = false;

    /**
     * 取得是否有上页的标记
     * @return boolean 是否有上页的标记(true：有,false：无)
     */
    public Boolean getHasPreviousPage() 
    {
    	return getPageNo() == 1 ? false : true;
    }

    /**
     * 设置是否有上页的标记
     * @param hasPreviousPage 是否有上页的标记(true：有,false：无)
     */
    public void setHasPreviousPage(Boolean hasPreviousPage)
    {
        this.hasPreviousPage = hasPreviousPage;
    }

    /**
     * 取得是否有下页的标记
     * @return boolean 是否有下页的标记(true：有,false：无)
     */
    public Boolean getHasNextPage()
    {
    	return getPageNo() == getTotalPage() || getTotalPage() == 0 ? false : true;
    }

    /**
     * 设置是否有下页的标记
     * @param hasNextPage 是否有下页的标记(true：有,false：无)
     */
    public void setHasNextPage(Boolean hasNextPage)
    {
        this.hasNextPage = hasNextPage;
    }

    /**
     * 取得每页显示的资料笔数
     * @return int 每页显示的资料笔数(默认为20)
     */
    public Integer getPageSize()
    {
		if(!isInt(pageSize, 0) || pageSize.intValue() > ApplicationConst.MAX_PAGE_SIZE.intValue()){
			pageSize = ApplicationConst.DEFAULT_PAGE_SIZE;
		}
        return pageSize;
    }

    /**
     * 设置每页显示的资料笔数
     * @param pageSize 每页显示的资料笔数(默认为20)
     */
    public void setPageSize(Integer pageSize)
    {
    	this.pageSize = pageSize;
    }

    /**
     * 取得当前显示的页标
     * @return int 当前显示的页标
     */
    public Integer getPageNo()
    {
    	if(!isInt(this.pageNo, 0)){
    		this.pageNo = ApplicationConst.DEFAULT_CURRENT_PAGE;
		}
        return this.pageNo;
    }

    /**
     * 设置当前显示的页标
     * @param currentPage 当前显示的页标
     */
    public void setPageNo(Integer pageNo)
    {
    	this.pageNo = pageNo;
    }

    /**
     * 取得记录开始位置
     * @return int 记录开始位置
     */
    public Integer getStartIndex()
    {
        return startIndex;
    }

    /**
     * 设置记录开始位置
     * @param startIndex 记录开始位置
     */
    public void setStartIndex(Integer startIndex)
    {
        this.startIndex = startIndex;
    }

    /**
     * 取得资料总笔数
     * @return int 资料总笔数
     */
    public Long getTotalCount()
    {
        return totalCount;
    }

    /**
     * 设置资料总笔数
     * @param totalCount 资料总笔数
     */
    public void setTotalCount(Long totalCount)
    {
        this.totalCount = totalCount;
    }

    /**
     * 取得资料总页数
     * @return int 资料总页数
     */
    public Integer getTotalPage()
    {
    	if(0 == getPageSize() || null == totalCount){
    		this.totalPage = 1;
    	}else{
    		this.totalPage = (int) Math.ceil((double) totalCount / (double) getPageSize());
    	}
    	return totalPage;
    }

    /**
     * 设置资料总页数
     * @param totalPage 资料总页数
     */
    public void setTotalPage(Integer totalPage)
    {
        this.totalPage = totalPage;
    }

	public Boolean getIsCount() {
		return isCount;
	}

	public void setIsCount(Boolean isCount) {
		this.isCount = isCount;
	}
	
	private Boolean isInt(Integer pageNo, Integer min) {
	    if(null != pageNo && pageNo.intValue() > min.intValue()) {
	        return Boolean.TRUE;
	    }
	    return Boolean.FALSE;
	}
}

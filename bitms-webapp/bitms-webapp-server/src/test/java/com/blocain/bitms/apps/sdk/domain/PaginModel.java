package com.blocain.bitms.apps.sdk.domain;

import com.blocain.bitms.apps.sdk.BitmsObject;
import com.blocain.bitms.apps.sdk.internal.mapping.ApiField;

/**
 * 分页请求测试
 *
 * @author auto create
 * @since 1.0, 2017-12-06 21:42:16
 */
public class PaginModel extends BitmsObject
{
    private static final long serialVersionUID = 5768574576598366886L;
    
    @ApiField("page")
    private Integer           page;
    
    @ApiField("rows")
    private Integer           rows;
    
    @ApiField("id")
    private Long              id;
    
    @ApiField("auth_token")
    private String            authToken;
    
    @ApiField("pair")
    private String            pair;
    
    @ApiField("stockType")
    private String            stockType;
    
    public Integer getPage()
    {
        return page;
    }
    
    public void setPage(Integer page)
    {
        this.page = page;
    }
    
    public Integer getRows()
    {
        return rows;
    }
    
    public void setRows(Integer rows)
    {
        this.rows = rows;
    }
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getAuthToken()
    {
        return authToken;
    }
    
    public void setAuthToken(String authToken)
    {
        this.authToken = authToken;
    }
    
    public String getPair()
    {
        return pair;
    }
    
    public void setPair(String pair)
    {
        this.pair = pair;
    }
    
    public String getStockType()
    {
        return stockType;
    }
    
    public void setStockType(String stockType)
    {
        this.stockType = stockType;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("PaginModel{");
        sb.append("page=").append(page);
        sb.append(", rows=").append(rows);
        sb.append(", id=").append(id);
        sb.append(", authToken='").append(authToken).append('\'');
        sb.append(", pair='").append(pair).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

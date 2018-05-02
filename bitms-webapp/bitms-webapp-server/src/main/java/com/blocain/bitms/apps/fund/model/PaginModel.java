package com.blocain.bitms.apps.fund.model;

import com.blocain.bitms.apps.sdk.internal.mapping.ApiField;

/**
 * 互转分页model
 * @author auto create
 * @since 1.0, 2017-12-06 21:42:16
 */
public class PaginModel
{
    
    @ApiField("page")
    private Integer           page;
    
    @ApiField("rows")
    private Integer           rows;

    @ApiField("auth_token")
    private String            authToken;

    /**开始时间*/
    @ApiField("timeStart")
    private String               timeStart;

    /**结束时间 */
    @ApiField("timeEnd")
    private String               timeEnd;

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

    public String getAuthToken()
    {
        return authToken;
    }
    
    public void setAuthToken(String authToken)
    {
        this.authToken = authToken;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }
}

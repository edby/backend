package com.blocain.bitms.apps.spot.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.blocain.bitms.apps.account.beans.AuthTokenRequest;
import com.blocain.bitms.apps.sdk.internal.mapping.ApiField;
import com.blocain.bitms.tools.consts.BitmsConst;

/**
 * <p>Author：yukai </p>
 * <p>Description:溢价费记录请求Model </p>
 * <p>Date: Create in 16:56 2018/4/16</p>
 * <p>Modify By: yukai</p>
 *
 * @version 1.0
 */
public class PremiumModel extends AuthTokenRequest {

    /**
     * 开始时间
     */
    private String timeStart;
    /**
     * 结束时间
     */
    private String timeEnd;
    /**
     * 目标页
     */
    private Integer page = BitmsConst.DEFAULT_CURRENT_PAGE;
    /**
     * 每页显示数量
     */
    private Integer rows = BitmsConst.DEFAULT_PAGE_SIZE;

    public PremiumModel() {
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

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
}

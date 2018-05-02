package com.blocain.bitms.apps.fund.model;

import com.blocain.bitms.apps.account.beans.AuthTokenRequest;
import com.blocain.bitms.tools.consts.BitmsConst;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * <p>Author：XiaoDing</p>
 * <p>Description: AccountDebitAssetModel</p>
 * <p>Date: Create in 9:43 2018/3/26</p>
 * <p>Modify By: XiaoDing</p>
 *
 * @version 1.0
 */
public class AccountDebitAssetModel extends AuthTokenRequest
{
    /**借贷证券信息关联id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "借贷证券信息关联id 对应Stockinfo表中的ID字段不可为空")
    @ApiModelProperty(value = "借贷证券信息关联id 对应Stockinfo表中的ID字段", required = true)
    private Long                 relatedStockinfoId;

    /**每页的记录数*/
    protected Integer rows = BitmsConst.DEFAULT_PAGE_SIZE;

    /**当前是第几页*/
    protected Integer page = BitmsConst.DEFAULT_CURRENT_PAGE;

    /**开始时间*/
    private String               timeStart;

    /**结束时间 */
    private String               timeEnd;

    public Long getRelatedStockinfoId()
    {
        return this.relatedStockinfoId;
    }

    public void setRelatedStockinfoId(Long relatedStockinfoId)
    {
        this.relatedStockinfoId = relatedStockinfoId;
    }

    public Integer getRows()
    {
        return rows;
    }

    public void setRows(Integer rows)
    {
        this.rows = rows;
    }

    public Integer getPage()
    {
        return page;
    }

    public void setPage(Integer page)
    {
        this.page = page;
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

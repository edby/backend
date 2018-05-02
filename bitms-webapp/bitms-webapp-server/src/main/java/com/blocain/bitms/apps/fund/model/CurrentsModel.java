package com.blocain.bitms.apps.fund.model;


import com.blocain.bitms.apps.account.beans.AuthTokenRequest;
import com.blocain.bitms.tools.annotation.ExcelField;
import com.blocain.bitms.tools.consts.BitmsConst;

import javax.validation.constraints.NotNull;

/**
 * <p>Author：XiaoDing</p>
 * <p>Description:FinancialCurrentsController</p>
 * <p>Date: Create in 8:56 2018/3/27</p>
 * <p>Modify By: XiaoDing</p>
 *
 * @version 1.0
 */
public class CurrentsModel extends AuthTokenRequest
{
    /**币种(资产分类)*/
    @NotNull(message = "币种(资产分类)不可为空")
    @ExcelField(title = "币种(资产分类)")
    private String               symbol;

    /**查询类别(历史查询或当前查询)*/
    @NotNull(message = "查询类别不可为空")
    @ExcelField(title = "查询类别")
    private String               isHis;

    /**每页的记录数*/
    protected Integer rows = BitmsConst.DEFAULT_PAGE_SIZE;

    /**当前是第几页*/
    protected Integer page = BitmsConst.DEFAULT_CURRENT_PAGE;

    /**开始时间*/
    private String               timeStart;

    /**结束时间 */
    private String               timeEnd;

    public String getIsHis() {
        return isHis;
    }

    public void setIsHis(String isHis) {
        this.isHis = isHis;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
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

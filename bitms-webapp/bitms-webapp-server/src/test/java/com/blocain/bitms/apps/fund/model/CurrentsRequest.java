package com.blocain.bitms.apps.fund.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.blocain.bitms.apps.sdk.BitmsObject;
import com.blocain.bitms.apps.sdk.internal.mapping.ApiField;
import com.blocain.bitms.tools.annotation.ExcelField;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 财务流水查询model
 * Created by XiaoDing on 2018/3/27.
 */
public class CurrentsRequest extends BitmsObject
{
    @NotNull
    @JSONField(name = "auth_token")
    @ApiField("auth_token")
    protected String            authToken;

    /**币种(资产分类)*/
    @NotNull(message = "币种(资产分类)不可为空")
    @ApiField("symbol")
    @ExcelField(title = "币种(资产分类)")
    private String               symbol;

    /**查询类别(历史查询或当前查询)*/
    @NotNull(message = "查询类别不可为空")
    @ApiField("isHis")
    @ExcelField(title = "查询类别")
    private String               isHis;

    /**开始时间*/
    @ApiField("timeStart")
    private String               timeStart;

    /**结束时间 */
    @ApiField("timeEnd")
    private String               timeEnd;

    public String getAuthToken()
    {
        return authToken;
    }

    public void setAuthToken(String authToken)
    {
        this.authToken = authToken;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getIsHis() {
        return isHis;
    }

    public void setIsHis(String isHis) {
        this.isHis = isHis;
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
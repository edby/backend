package com.blocain.bitms.apps.fund.model;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONField;
import com.blocain.bitms.apps.account.beans.AuthTokenRequest;
import com.blocain.bitms.apps.sdk.BitmsObject;
import com.blocain.bitms.apps.sdk.internal.mapping.ApiField;
import io.swagger.annotations.ApiModelProperty;

/**
 * 借款记录查询model
 * Created by XiaoDing on 2018/3/21.
 */
public class AccountDebitAssetRequest extends BitmsObject
{
    @NotNull
    @JSONField(name = "auth_token")
    @ApiField("auth_token")
    protected String            authToken;

    /**借贷证券信息关联id 对应Stockinfo表中的ID字段*/
    @NotNull(message = "借贷证券信息关联id 对应Stockinfo表中的ID字段不可为空")
    @ApiField("relatedStockinfoId")
    @ApiModelProperty(value = "借贷证券信息关联id 对应Stockinfo表中的ID字段", required = true)
    private Long                 relatedStockinfoId;

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

    public Long getRelatedStockinfoId() {
        return relatedStockinfoId;
    }

    public void setRelatedStockinfoId(Long relatedStockinfoId) {
        this.relatedStockinfoId = relatedStockinfoId;
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
package com.blocain.bitms.trade.account.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.blocain.bitms.tools.bean.Pagination;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Notice查询参数对象 介绍
 * <p>File：NoticeModel.java </p>
 * <p>Title: NoticeModel </p>
 * <p>Description:NoticeModel </p>
 * <p>Copyright: Copyright (c) 2017/7/13 </p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "公告查询参数对象")
public class NoticeModel implements Serializable
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7977060172358580961L;
    
    /**
     * 帐户ID
     */
    @ApiModelProperty(value = "帐户ID,系统自动获取", hidden = true)
    private Long              accountId;
    
    /**类型(message消息、notice公告)*/
    @ApiModelProperty(value = "类型(message消息、notice公告)")
    private String            noticeType;
    
    /**语言类型(zh_CN中文简体、zh_HK中文繁体、en_US英文)*/
    @ApiModelProperty(value = "语言类型(zh_CN:中文简体、zh_HK:中文繁体、en_US:英文)")
    private String            langType;
    
    /**状态(unPublished未发布、published已发布)*/
    @ApiModelProperty(value = "状态,系统自动处理", hidden = true)
    private String            status;
    
    /**
     * 分页对象
     * <p>用于动态加入SQL分页语句的对象</p>
     */
    @JSONField(serialize = false, deserialize = false)
    @ApiModelProperty(value = "分页对象", hidden = true)
    private Pagination        pagination;
    
    public Long getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public String getNoticeType()
    {
        return noticeType;
    }
    
    public void setNoticeType(String noticeType)
    {
        this.noticeType = noticeType;
    }
    
    public String getLangType()
    {
        return langType;
    }
    
    public void setLangType(String langType)
    {
        this.langType = langType;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public Pagination getPagination()
    {
        return pagination;
    }
    
    public void setPagination(Pagination pagination)
    {
        this.pagination = pagination;
    }
}

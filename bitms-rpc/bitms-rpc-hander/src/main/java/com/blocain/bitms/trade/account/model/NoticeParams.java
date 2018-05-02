package com.blocain.bitms.trade.account.model;

import java.io.Serializable;

import com.blocain.bitms.tools.bean.Pagination;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 消息公告参数对象 介绍
 * <p>File：NoticeRequestModel.java </p>
 * <p>Title: NoticeRequestModel </p>
 * <p>Description:NoticeRequestModel </p>
 * <p>Copyright: Copyright (c) 2017/7/13 </p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "消息公告参数对象")
public class NoticeParams implements Serializable
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5792227583812568718L;
    
    @ApiModelProperty(value = "Notice查询参数对象")
    private NoticeModel notice;
    
    @ApiModelProperty(value = "分页对象")
    private Pagination  pagin;
    
    public NoticeModel getNotice()
    {
        return notice;
    }
    
    public void setNotice(NoticeModel notice)
    {
        this.notice = notice;
    }
    
    public Pagination getPagin()
    {
        return pagin;
    }
    
    public void setPagin(Pagination pagin)
    {
        this.pagin = pagin;
    }
}

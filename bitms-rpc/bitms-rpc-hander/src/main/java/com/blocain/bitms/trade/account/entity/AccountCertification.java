/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.entity;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.blocain.bitms.orm.core.GenericEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户实名认证信息 实体对象
 * <p>File：AccountCertification.java</p>
 * <p>Title: AccountCertification</p>
 * <p>Description:AccountCertification</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@ApiModel(description = "用户实名认证信息")
public class AccountCertification extends GenericEntity
{
    private static final long serialVersionUID = 1L;
    
    /**帐户编号*/
    @NotNull(message = "帐户编号不可为空")
    @ApiModelProperty(value = "帐户编号")
    private Long              accountId;
    
    /**帐户*/
    private String            accountName;
    
    /**姓氏*/
    @NotNull(message = "姓氏不可为空")
    @ApiModelProperty(value = "姓氏", required = true)
    private String            surname;
    
    /**名字*/
    @NotNull(message = "名字不可为空")
    @ApiModelProperty(value = "名字", required = true)
    private String            realname;
    
    /**区域代码*/
    @NotNull(message = "区域代码不可为空")
    @ApiModelProperty(value = "区域代码", required = true)
    private Long              regionId;
    
    /**护照编号*/
    @NotNull(message = "护照编号不可为空")
    @ApiModelProperty(value = "护照编号", required = true)
    private String            passportId;
    
    /**附件信息（JSON对象）*/
    // @NotNull(message = "附件信息（JSON对象）不可为空")
    @ApiModelProperty(value = "附件信息（JSON对象）")
    private String            attachments;
    
    /**审核状态(0未审核 1：已审核通过 2:审核失败)*/
    @NotNull(message = "审核状态(0未审核 1：已审核通过 2:审核失败)不可为空")
    @ApiModelProperty(value = "审核状态(0未审核 1：已审核通过 2:审核失败)")
    private Short             status;
    
    /**文件上传成功后的地址*/
    @ApiModelProperty(value = "附件信息")
    private Attachment        attachment;
    
    public Long getAccountId()
    {
        return this.accountId;
    }
    
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }
    
    public String getSurname()
    {
        return null != this.surname ? this.surname.toUpperCase() : this.surname;
    }
    
    public void setSurname(String surname)
    {
        this.surname = surname;
    }
    
    public String getRealname()
    {
        return null != this.realname ? this.realname.toUpperCase() : this.realname;
    }
    
    public void setRealname(String realname)
    {
        this.realname = realname;
    }
    
    public Long getRegionId()
    {
        return this.regionId;
    }
    
    public void setRegionId(Long regionId)
    {
        this.regionId = regionId;
    }
    
    public String getPassportId()
    {
        return this.passportId;
    }
    
    public void setPassportId(String passportId)
    {
        this.passportId = passportId;
    }
    
    public String getAttachments()
    {
        return this.attachments;
    }
    
    public void setAttachments(String attachments)
    {
        this.attachments = attachments;
    }
    
    public Short getStatus()
    {
        return this.status;
    }
    
    public void setStatus(Short status)
    {
        this.status = status;
    }
    
    public Attachment getAttachment()
    {
        return attachment;
    }
    
    public void setAttachment(Attachment attachment)
    {
        this.attachment = attachment;
    }
    
    public String getAccountName()
    {
        return accountName;
    }
    
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    
    /**
     * 附件对象
     */
    @ApiModel(description = "附件对象")
    public static class Attachment implements Serializable
    {
        private static final long serialVersionUID = 1L;
        
        // 封面
        @ApiModelProperty(value = "封面")
        private String            cover;
        
        // 正面
        @ApiModelProperty(value = "正面")
        private String            frontage;
        
        // 反面
        @ApiModelProperty(value = "反面")
        private String            opposite;
        
        public String getCover()
        {
            return cover;
        }
        
        public void setCover(String cover)
        {
            this.cover = cover;
        }
        
        public String getFrontage()
        {
            return frontage;
        }
        
        public void setFrontage(String frontage)
        {
            this.frontage = frontage;
        }
        
        public String getOpposite()
        {
            return opposite;
        }
        
        public void setOpposite(String opposite)
        {
            this.opposite = opposite;
        }
    }
}

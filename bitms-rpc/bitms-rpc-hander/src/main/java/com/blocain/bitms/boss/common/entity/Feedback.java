package com.blocain.bitms.boss.common.entity;

import com.blocain.bitms.orm.core.GenericEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * 问题反馈 Introduce
 * <p>File：Feedback.java</p>
 * <p>Title: Feedback</p>
 * <p>Description: Feedback</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Document(collection = "Feedback")
@ApiModel(description = "问题反馈")
public class Feedback extends GenericEntity
{
    private static final long serialVersionUID = 1L;
    
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    @NotNull(message = "姓名不可为空")
    private String            trueName;
    
    /**
     * 问题描述
     */
    @ApiModelProperty(value = "问题描述")
    @NotNull(message = "问题描述不可为空")
    private String            describe;
    
    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    @NotNull(message = "联系电话不可为空")
    private String            contactNumber;
    
    /**
     * 附件，JSON对象
     */
    @ApiModelProperty(value = "附件，JSON对象")
    private String            attachments;
    
    public String getTrueName()
    {
        return trueName;
    }
    
    public void setTrueName(String trueName)
    {
        this.trueName = trueName;
    }
    
    public String getDescribe()
    {
        return describe;
    }
    
    public void setDescribe(String describe)
    {
        this.describe = describe;
    }
    
    public String getContactNumber()
    {
        return contactNumber;
    }
    
    public void setContactNumber(String contactNumber)
    {
        this.contactNumber = contactNumber;
    }
    
    public String getAttachments()
    {
        return attachments;
    }
    
    public void setAttachments(String attachments)
    {
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Feedback{");
        sb.append("trueName='").append(trueName).append('\'');
        sb.append(", describe='").append(describe).append('\'');
        sb.append(", contactNumber='").append(contactNumber).append('\'');
        sb.append(", attachments='").append(attachments).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

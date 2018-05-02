/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.system.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.blocain.bitms.orm.core.SignableEntity;
import com.blocain.bitms.tools.consts.CharsetConst;

import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;

/**
 * 用户基础信息表 实体对象
 * <p>File：UserInfo.java</p>
 * <p>Title: UserInfo</p>
 * <p>Description:UserInfo</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class UserInfo extends SignableEntity
{
    private static final long serialVersionUID = 1L;
    
    /**机构ID*/
    @NotNull(message = "机构ID不可为空")
    private Long              orgId;
    
    /**机构名称*/
    private String            orgName;
    
    /**用户名*/
    @NotNull(message = "用户名不可为空")
    private String            userName;
    
    /**真实姓名*/
    private String            trueName;
    
    /**密码*/
    @JSONField(serialize = false)
    private String            passWord;
    
    /**性别*/
    private Boolean           gender;
    
    /**激活状态*/
    private Boolean           active;
    
    /**身份证号*/
    private String            idCard;
    
    /**google验证器私钥*/
    private String            authKey;
    
    private String            roleIds;

    /**头像*/
    private String            userLogo;

    /**描述*/
    private String            userDest;

    /**职称*/
    private String            jobTitle;

    /**所在地址*/
    private String            address;

    /**创建时间*/
    private Long              createDate;

    /**修改时间*/
    private Long              updateDate;

    @Override
    protected byte[] acquiresSignValue() throws UnsupportedEncodingException
    {
        String sign = new StringBuffer(String.valueOf(orgId)).append(userName).append(passWord).append(authKey).toString();
        return sign.getBytes(CharsetConst.CHARSET_UT);
    }
    
    public Long getOrgId()
    {
        return this.orgId;
    }
    
    public void setOrgId(Long orgId)
    {
        this.orgId = orgId;
    }
    
    public String getOrgName()
    {
        return orgName;
    }
    
    public void setOrgName(String orgName)
    {
        this.orgName = orgName;
    }
    
    public String getUserName()
    {
        return this.userName;
    }
    
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    public String getTrueName()
    {
        return this.trueName;
    }
    
    public void setTrueName(String trueName)
    {
        this.trueName = trueName;
    }
    
    public String getPassWord()
    {
        return this.passWord;
    }
    
    public void setPassWord(String passWord)
    {
        this.passWord = passWord;
    }
    
    public Boolean getGender()
    {
        return this.gender;
    }
    
    public void setGender(Boolean gender)
    {
        this.gender = gender;
    }
    
    public Boolean getActive()
    {
        return this.active;
    }
    
    public void setActive(Boolean active)
    {
        this.active = active;
    }
    
    public String getIdCard()
    {
        return this.idCard;
    }
    
    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }
    
    public String getRoleIds()
    {
        return roleIds;
    }
    
    public void setRoleIds(String roleIds)
    {
        this.roleIds = roleIds;
    }
    
    public String getAuthKey()
    {
        return authKey;
    }
    
    public void setAuthKey(String authKey)
    {
        this.authKey = authKey;
    }

    public String getUserLogo()
    {
        return userLogo;
    }

    public void setUserLogo(String userLogo)
    {
        this.userLogo = userLogo;
    }

    public String getUserDest()
    {
        return userDest;
    }

    public void setUserDest(String userDest)
    {
        this.userDest = userDest;
    }

    public String getJobTitle()
    {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle)
    {
        this.jobTitle = jobTitle;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public Long getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(Long createDate)
    {
        this.createDate = createDate;
    }

    public Long getUpdateDate()
    {
        return updateDate;
    }

    public void setUpdateDate(Long updateDate)
    {
        this.updateDate = updateDate;
    }

    @Override public String toString()
    {
        final StringBuilder sb = new StringBuilder("UserInfo{");
        sb.append("orgId=").append(orgId);
        sb.append(", orgName='").append(orgName).append('\'');
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", trueName='").append(trueName).append('\'');
        sb.append(", passWord='").append(passWord).append('\'');
        sb.append(", gender=").append(gender);
        sb.append(", active=").append(active);
        sb.append(", idCard='").append(idCard).append('\'');
        sb.append(", authKey='").append(authKey).append('\'');
        sb.append(", roleIds='").append(roleIds).append('\'');
        sb.append(", userLogo='").append(userLogo).append('\'');
        sb.append(", userDest='").append(userDest).append('\'');
        sb.append(", jobTitle='").append(jobTitle).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", createDate=").append(createDate);
        sb.append(", updateDate=").append(updateDate);
        sb.append('}');
        return sb.toString();
    }
}

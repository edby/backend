/*
 * @(#)Member.java 2017年7月21日 上午9:32:16
 * Copyright 2017 施建波, Inc. All rights reserved. BloCain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.bitpay.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>File：Member.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月21日 上午9:32:16</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
public class Member extends BaseEntity
{

    @TableField(exist = false)
    private static final long serialVersionUID = -1198286429782636546L;
    
    @TableId(type = IdType.INPUT)
    private Long id;
    /**用户名*/
    private String userName;
    /**密码*/
    private String passWord;
    /**角色：admin(管理员) operate(运营)*/
    private String role;
    /**创建时间*/
    private Long createDate;
    
    public Long getId()
    {
        return id;
    }
    public void setId(Long id)
    {
        this.id = id;
    }
    public String getUserName()
    {
        return userName;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    public String getPassWord()
    {
        return passWord;
    }
    public void setPassWord(String passWord)
    {
        this.passWord = passWord;
    }
    public String getRole()
    {
        return role;
    }
    public void setRole(String role)
    {
        this.role = role;
    }
    public Long getCreateDate()
    {
        return createDate;
    }
    public void setCreateDate(Long createDate)
    {
        this.createDate = createDate;
    }
}

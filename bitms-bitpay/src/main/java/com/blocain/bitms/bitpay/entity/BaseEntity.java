/*
 * @(#)BaseEntity.java 2017年7月18日 上午11:35:09
 * Copyright 2017 施建波, Inc. All rights reserved. BloCain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.bitpay.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;

/**
 * <p>File：BaseEntity.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月18日 上午11:35:09</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
public class BaseEntity implements Serializable
{
    @TableField(exist = false)
    private static final long serialVersionUID = 3985988483986830092L;
    
    @TableField(exist = false)
    private String entityColumns;
    @TableField(exist = false)
    private String orderByField;
    
    public String getEntityColumns()
    {
        return entityColumns;
    }
    public void setEntityColumns(String entityColumns)
    {
        this.entityColumns = entityColumns;
    }
    public String getOrderByField()
    {
        return orderByField;
    }
    public void setOrderByField(String orderByField)
    {
        this.orderByField = orderByField;
    }
    
    
}

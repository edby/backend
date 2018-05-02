/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.system.service;

import com.blocain.bitms.boss.system.entity.UserInfo;
import com.blocain.bitms.orm.core.GenericService;

/**
 * 用户基础信息表 服务接口
 * <p>File：UserInfoService.java </p>
 * <p>Title: UserInfoService </p>
 * <p>Description:UserInfoService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface UserInfoService extends GenericService<UserInfo>
{
    /**
     * 根据用户名找用户
     * @param userName
     * @return
     */
    UserInfo findByUserName(String userName);
}

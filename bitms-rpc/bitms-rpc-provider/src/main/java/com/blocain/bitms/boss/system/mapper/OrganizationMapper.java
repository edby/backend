/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.system.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.boss.system.entity.Organization;

/**
 * 机构信息表 持久层接口
 * <p>File：OrganizationDao.java </p>
 * <p>Title: OrganizationDao </p>
 * <p>Description:OrganizationDao </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface OrganizationMapper extends GenericMapper<Organization>
{
}

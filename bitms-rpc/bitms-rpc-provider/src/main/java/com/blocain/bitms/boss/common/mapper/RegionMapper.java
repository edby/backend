/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.common.mapper;

import com.blocain.bitms.boss.common.entity.Region;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;

/**
 * 区域代码 持久层接口
 * <p>File：RegionDao.java </p>
 * <p>Title: RegionDao </p>
 * <p>Description:RegionDao </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface RegionMapper extends GenericMapper<Region>
{
}

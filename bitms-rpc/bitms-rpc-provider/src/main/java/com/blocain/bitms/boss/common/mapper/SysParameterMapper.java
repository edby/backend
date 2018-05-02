/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.common.mapper;

import com.blocain.bitms.orm.core.GenericMapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.blocain.bitms.boss.common.entity.SysParameter;
import com.blocain.bitms.orm.annotation.MyBatisDao;

/**
 * 系统参数表 持久层接口
 * <p>File：SysParameterDao.java </p>
 * <p>Title: SysParameterDao </p>
 * <p>Description:SysParameterDao </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface SysParameterMapper extends GenericMapper<SysParameter>
{
    SysParameter getSysParameterByEntity(SysParameter sysParameter);
    
    List<SysParameter> getSysValueByParams(@Param("systemName") String systemName, @Param("params") String[] params);
}

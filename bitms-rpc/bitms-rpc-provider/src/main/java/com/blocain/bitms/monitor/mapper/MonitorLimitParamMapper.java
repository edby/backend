/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.mapper;

import com.blocain.bitms.monitor.entity.MonitorLimitParam;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;

import java.util.List;

/**
 * MonitorLimitParam 持久层接口
 * <p>File：MonitorLimitParamMapper.java </p>
 * <p>Title: MonitorLimitParamMapper </p>
 * <p>Description:MonitorLimitParamMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface MonitorLimitParamMapper extends GenericMapper<MonitorLimitParam>
{

//    MonitorLimitParam findByIdxAndStock(Long idxid, Long stockinfoId);

    List<MonitorLimitParam> findByIdx(Long idx);

    List<MonitorLimitParam> findJoinList(MonitorLimitParam entity);
}

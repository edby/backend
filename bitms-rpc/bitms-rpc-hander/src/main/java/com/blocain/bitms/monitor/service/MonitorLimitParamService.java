/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.entity.MonitorLimitParam;
import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;

/**
 * MonitorLimitParam 服务接口
 * <p>File：MonitorLimitParamService.java </p>
 * <p>Title: MonitorLimitParamService </p>
 * <p>Description:MonitorLimitParamService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface MonitorLimitParamService extends GenericService<MonitorLimitParam>{

    /**
     * 根据指标id和证券id(币种)查询阈值参数。
//     * @param idxid
//     * @param stockinfoId
     * @return
     */
//    MonitorLimitParam findByIdxAndStock(Long idxid,Long stockinfoId);

    PaginateResult<MonitorLimitParam> findJoinList(Pagination pagin, MonitorLimitParam entity);
}

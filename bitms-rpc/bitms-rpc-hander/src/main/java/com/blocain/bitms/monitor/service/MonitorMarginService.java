package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.entity.MonitorMargin;
import com.blocain.bitms.orm.annotation.SlaveDataSource;
import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;

import java.util.List;

/**
 * 杠杆保证金监控服务
 * MonitorMarginService Introduce
 * <p>File：MonitorMarginService.java</p>
 * <p>Title: MonitorMarginService</p>
 * <p>Description: MonitorMarginService</p>
 * <p>Copyright: Copyright (c) 2017/9/26</p>
 * <p>Company: BloCain</p>
 *
 * @author Jiangsc
 * @version 1.0
 */
public interface MonitorMarginService  extends GenericService<MonitorMargin>
{
    /**
     * 查询
     * @param  ids 逗号分割
     * @return
     */
    List<MonitorMargin> findListByIds(String ids, String targetStockinfoIds,String capitalStockinfoIds);

    /**
     * 查询保证金情况
     * @param pagination
     * @param monitorMargin
     * @return
     */
    PaginateResult<MonitorMargin> findMarginList(Pagination pagination, MonitorMargin monitorMargin);

    /**
     * 查询已经爆仓的保证金记录
     * @return
     */
    List<MonitorMargin> findClosePositionDataList();

    MonitorMargin findRiskInfo();
}

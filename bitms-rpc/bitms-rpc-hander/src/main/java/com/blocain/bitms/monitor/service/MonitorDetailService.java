package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.entity.MonitorDetail;
import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;

public interface MonitorDetailService extends GenericService<MonitorDetail> {

    PaginateResult<MonitorDetail> findMonitorDetailList(Pagination pagin, MonitorDetail entity);
}

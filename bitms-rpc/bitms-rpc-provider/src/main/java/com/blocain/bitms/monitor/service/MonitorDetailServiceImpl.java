package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.entity.MonitorDetail;
import com.blocain.bitms.monitor.mapper.MonitorDetailMapper;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitorDetailServiceImpl extends GenericServiceImpl<MonitorDetail> implements MonitorDetailService {

    protected MonitorDetailMapper monitorDetailMapper;

    @Autowired
    public MonitorDetailServiceImpl(MonitorDetailMapper monitorDetailMapper) {
        super(monitorDetailMapper);
        this.monitorDetailMapper = monitorDetailMapper;
    }

    @Override
    public PaginateResult<MonitorDetail> findMonitorDetailList(Pagination pagin, MonitorDetail entity) {
        entity.setPagin(pagin);
        List<MonitorDetail> list = monitorDetailMapper.findMonitorDetailList(entity);
        return new PaginateResult<>(pagin, list);
    }
}

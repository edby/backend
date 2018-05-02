package com.blocain.bitms.monitor.mapper;

import com.blocain.bitms.monitor.entity.MonitorDetail;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;

import java.util.List;

@MyBatisDao
public interface MonitorDetailMapper extends GenericMapper<MonitorDetail> {

    List<MonitorDetail> findMonitorDetailList(MonitorDetail entity);
}

package com.blocain.bitms.monitor.mapper;

import java.util.HashMap;

import com.blocain.bitms.orm.annotation.MyBatisDao;

@MyBatisDao
public interface MonitorArchiveMapper
{
    void executeArchive(HashMap<String, Object> paramMap);
}

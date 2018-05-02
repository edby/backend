package com.blocain.bitms.monitor.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocain.bitms.monitor.entity.MonitorArchiveResult;
import com.blocain.bitms.monitor.mapper.MonitorArchiveMapper;

@Service
public class MonitorArchiveServiceImpl implements MonitorArchiveService
{
    protected MonitorArchiveMapper monitorArchiveMapper;
    
    @Autowired
    MonitorArchiveServiceImpl(MonitorArchiveMapper monitorArchiveMapper)
    {
        this.monitorArchiveMapper = monitorArchiveMapper;
    }
    
    @Override
    public MonitorArchiveResult executeArchive()
    {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        monitorArchiveMapper.executeArchive(paramMap);
        MonitorArchiveResult res = new MonitorArchiveResult();
        res.setArchiveCode((Integer) paramMap.get("return_code"));
        res.setArchiveDesc((String) paramMap.get("return_str"));
        return res;
    }
}

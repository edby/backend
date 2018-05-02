package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.entity.MonitorArchiveResult;

public interface MonitorArchiveService {

    /**
     * 手动发起归档
     * @return
     */
    MonitorArchiveResult executeArchive();
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocain.bitms.monitor.entity.ArchiveRunLogs;
import com.blocain.bitms.monitor.mapper.ArchiveRunLogsMapper;
import com.blocain.bitms.orm.core.GenericServiceImpl;

/**
 * 归档运行日志 服务实现类
 * <p>File：ArchiveRunLogsServiceImpl.java </p>
 * <p>Title: ArchiveRunLogsServiceImpl </p>
 * <p>Description:ArchiveRunLogsServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class ArchiveRunLogsServiceImpl extends GenericServiceImpl<ArchiveRunLogs> implements ArchiveRunLogsService
{

    protected ArchiveRunLogsMapper archiveRunLogsMapper;

    @Autowired
    public ArchiveRunLogsServiceImpl(ArchiveRunLogsMapper archiveRunLogsMapper)
    {
        super(archiveRunLogsMapper);
        this.archiveRunLogsMapper = archiveRunLogsMapper;
    }
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.mapper;

import com.blocain.bitms.monitor.entity.ArchiveRunLogs;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;

/**
 * 归档运行日志 持久层接口
 * <p>File：ArchiveRunLogsMapper.java </p>
 * <p>Title: ArchiveRunLogsMapper </p>
 * <p>Description:ArchiveRunLogsMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface ArchiveRunLogsMapper extends GenericMapper<ArchiveRunLogs>
{

}

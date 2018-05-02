/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.mapper;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.trade.account.entity.Notice;

/**
 * 消息公告表 持久层接口
 * <p>File：NoticeDao.java </p>
 * <p>Title: NoticeDao </p>
 * <p>Description:NoticeDao </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface NoticeMapper extends GenericMapper<Notice>
{
    /**
     * 根据帐户ID和语言取最新一条公告
     * @param lang
     * @return
     */
    Notice findLatestNotice(String lang);
}

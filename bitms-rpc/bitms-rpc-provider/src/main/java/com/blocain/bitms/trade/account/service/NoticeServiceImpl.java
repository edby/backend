/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.account.entity.Notice;
import com.blocain.bitms.trade.account.mapper.NoticeMapper;

/**
 * 消息公告表 服务实现类
 * <p>File：Notice.java </p>
 * <p>Title: Notice </p>
 * <p>Description:Notice </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class NoticeServiceImpl extends GenericServiceImpl<Notice> implements NoticeService
{
    private NoticeMapper        noticeMapper;

    @Autowired
    public NoticeServiceImpl(NoticeMapper noticeMapper)
    {
        super(noticeMapper);
        this.noticeMapper = noticeMapper;
    }
    
    @Override
    public Notice findLatestNotice(String lang) throws BusinessException
    {
        return noticeMapper.findLatestNotice(lang);
    }

}

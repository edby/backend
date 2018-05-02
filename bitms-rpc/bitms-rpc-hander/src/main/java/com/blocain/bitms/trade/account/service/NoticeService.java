/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.account.entity.Notice;

/**
 * 消息公告表 服务接口
 * <p>File：NoticeService.java </p>
 * <p>Title: NoticeService </p>
 * <p>Description:NoticeService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface NoticeService extends GenericService<Notice>
{
    /**
     * 根据帐户ID和语言取最新一条公告
     * @param lang
     * @return
     * @throws BusinessException
     */
    Notice findLatestNotice(String lang) throws BusinessException;
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocain.bitms.boss.common.entity.MsgTemplate;
import com.blocain.bitms.boss.common.mapper.MsgTemplateMapper;
import com.blocain.bitms.orm.core.GenericServiceImpl;

/**
 * 消息模版 服务实现类
 * <p>File：MsgTemplate.java </p>
 * <p>Title: MsgTemplate </p>
 * <p>Description:MsgTemplate </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class MsgTemplateServiceImpl extends GenericServiceImpl<MsgTemplate> implements MsgTemplateService
{
    MsgTemplateMapper msgTemplateMapper;
    
    @Autowired
    public MsgTemplateServiceImpl(MsgTemplateMapper msgTemplateMapper)
    {
        super(msgTemplateMapper);
        this.msgTemplateMapper = msgTemplateMapper;
    }
}

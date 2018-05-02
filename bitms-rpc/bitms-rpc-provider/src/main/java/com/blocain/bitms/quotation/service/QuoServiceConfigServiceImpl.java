/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.quotation.service;


import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.quotation.entity.QuoServiceConfig;
import com.blocain.bitms.quotation.mapper.QuoServiceConfigMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 行情服务配置表 服务实现类
 * <p>File：QuoServiceConfigServiceImpl.java </p>
 * <p>Title: QuoServiceConfigServiceImpl </p>
 * <p>Description:QuoServiceConfigServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class QuoServiceConfigServiceImpl extends GenericServiceImpl<QuoServiceConfig> implements QuoServiceConfigService
{

    protected QuoServiceConfigMapper quoServiceConfigMapper;

    @Autowired
    public QuoServiceConfigServiceImpl(QuoServiceConfigMapper quoServiceConfigMapper)
    {
        super(quoServiceConfigMapper);
        this.quoServiceConfigMapper = quoServiceConfigMapper;
    }
}

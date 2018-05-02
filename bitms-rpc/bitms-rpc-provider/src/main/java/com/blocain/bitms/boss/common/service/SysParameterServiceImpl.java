/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocain.bitms.boss.common.entity.SysParameter;
import com.blocain.bitms.boss.common.mapper.SysParameterMapper;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.exception.BusinessException;
import com.google.common.collect.Lists;

/**
 * 系统参数表 服务实现类
 * <p>File：SysParameter.java </p>
 * <p>Title: SysParameter </p>
 * <p>Description:SysParameter </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class SysParameterServiceImpl extends GenericServiceImpl<SysParameter> implements SysParameterService
{
    private SysParameterMapper sysParameterMapper;
    
    @Autowired
    public SysParameterServiceImpl(SysParameterMapper sysParameterMapper)
    {
        super(sysParameterMapper);
        this.sysParameterMapper = sysParameterMapper;
    }
    
    @Override
    public SysParameter getSysParameterByEntity(SysParameter sysParameter)
    {
        return sysParameterMapper.getSysParameterByEntity(sysParameter);
    }
    
    @Override
    public List<SysParameter> getSysValueByParams(String systemName, String ... params)
    {
        List<String> filter = Lists.newArrayList(params);
        return sysParameterMapper.getSysValueByParams(systemName, filter.toArray(new String[]{}));
    }
    
    @Override
    public int updateBatch(List<SysParameter> list, boolean flag) throws BusinessException
    {
        int count = sysParameterMapper.updateBatch(list);
        if (flag) throw new BusinessException("执行失败！");
        return count;
    }
}

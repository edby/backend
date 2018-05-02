/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.quotation.service;


import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.quotation.entity.QuoDetailedRule;
import com.blocain.bitms.quotation.mapper.QuoDetailedRuleMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 行情数据明细表 服务实现类
 * <p>File：QuoDetailedRuleServiceImpl.java </p>
 * <p>Title: QuoDetailedRuleServiceImpl </p>
 * <p>Description:QuoDetailedRuleServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class QuoDetailedRuleServiceImpl extends GenericServiceImpl<QuoDetailedRule> implements QuoDetailedRuleService
{

    protected QuoDetailedRuleMapper quoDetailedRuleMapper;

    @Autowired
    public QuoDetailedRuleServiceImpl(QuoDetailedRuleMapper quoDetailedRuleMapper)
    {
        super(quoDetailedRuleMapper);
        this.quoDetailedRuleMapper = quoDetailedRuleMapper;
    }
}

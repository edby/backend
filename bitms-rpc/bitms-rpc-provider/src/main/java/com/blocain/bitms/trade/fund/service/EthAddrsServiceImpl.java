/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.fund.entity.EthAddrs;
import com.blocain.bitms.trade.fund.mapper.EthAddrsMapper;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * EthAddrs 服务实现类
 * <p>File：EthAddrsServiceImpl.java </p>
 * <p>Title: EthAddrsServiceImpl </p>
 * <p>Description:EthAddrsServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class EthAddrsServiceImpl extends GenericServiceImpl<EthAddrs> implements EthAddrsService
{
    protected EthAddrsMapper ethAddrsMapper;
    
    @Autowired
    public EthAddrsServiceImpl(EthAddrsMapper ethAddrsMapper)
    {
        super(ethAddrsMapper);
        this.ethAddrsMapper = ethAddrsMapper;
    }
    
    @Override
    public List<Long> getChargeFromAddress(Long accountId)
    {
        return ethAddrsMapper.getChargeFromAddress(accountId);
    }
    
    @Override
    public List<EthAddrs> getByIdsForUpdate(Long ... ids)
    {
        List<Long> filter = Lists.newArrayList(ids);
        return ethAddrsMapper.getByIdsForUpdate(filter.toArray(new Long[]{}));
    }
    
    /**
     * 查询高度字段最小值记录findMinHeight
     * @return
     */
    @Override
    public List<EthAddrs> findMinHeight()
    {
        return ethAddrsMapper.findMinHeight();
    }
}

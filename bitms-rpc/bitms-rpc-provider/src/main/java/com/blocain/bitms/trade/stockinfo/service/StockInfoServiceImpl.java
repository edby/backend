/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.stockinfo.service;

import java.util.List;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.mapper.StockInfoMapper;

/**
 * 证券信息表 服务实现类
 * <p>File：StockInfo.java </p>
 * <p>Title: StockInfo </p>
 * <p>Description:StockInfo </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Service
public class StockInfoServiceImpl extends GenericServiceImpl<StockInfo> implements StockInfoService {
    private StockInfoMapper stockInfoMapper;

    @Autowired
    public StockInfoServiceImpl(StockInfoMapper stockInfoMapper) {
        super(stockInfoMapper);
        this.stockInfoMapper = stockInfoMapper;
    }

    /**
     * 按id获取证券信息（多个id 逗号分割）
     *
     * @param ids
     * @return
     */
    @Override
    public List<StockInfo> findListByIds(String ids) throws BusinessException {
        if (StringUtils.isBlank(ids)) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        return stockInfoMapper.findListByIds(ids.split(","));
    }

    @Override
    public List<StockInfo> findLIstByDistinctCaptalId() {
        return stockInfoMapper.findLIstByDistinctCaptalId();
    }

    @Override
    public List<StockInfo> findListByTypes(String... type) throws BusinessException {
        List<String> filter = Lists.newArrayList(type);
        return stockInfoMapper.findListByTypes(filter.toArray(new String[]{}));
    }

    @Override
    public StockInfo findByContractAddr(String addr) {
        return stockInfoMapper.findByContractAddr(addr);
    }

    @Override public StockInfo findOneNotActive()
    {
        return stockInfoMapper.findOneNotActive();
    }

    @Override
    public List<StockInfo> findAll() {
        return stockInfoMapper.findAll();
    }
}

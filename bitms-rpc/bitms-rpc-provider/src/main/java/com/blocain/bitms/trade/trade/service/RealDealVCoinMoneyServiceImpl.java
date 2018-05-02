/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.trade.service;

import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.trade.entity.RealDealVCoinMoney;
import com.blocain.bitms.trade.trade.mapper.RealDealVCoinMoneyMapper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 成交表X 服务实现类
 * <p>File：RealDealVCoinMoneyServiceImpl.java </p>
 * <p>Title: RealDealVCoinMoneyServiceImpl </p>
 * <p>Description:RealDealVCoinMoneyServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class RealDealVCoinMoneyServiceImpl extends GenericServiceImpl<RealDealVCoinMoney> implements RealDealVCoinMoneyService
{

    protected RealDealVCoinMoneyMapper realDealVCoinMoneyMapper;

    @Autowired
    public RealDealVCoinMoneyServiceImpl(RealDealVCoinMoneyMapper realDealVCoinMoneyMapper)
    {
        super(realDealVCoinMoneyMapper);
        this.realDealVCoinMoneyMapper = realDealVCoinMoneyMapper;
    }

    @Override
    public PaginateResult<RealDealVCoinMoney> findRealDealListByEntrustId(Pagination pagination, RealDealVCoinMoney entity) throws BusinessException
    {
        entity.setPagin(pagination);
        List<RealDealVCoinMoney> data = realDealVCoinMoneyMapper.findRealDealListByEntrustId(entity);
        return new PaginateResult<>(pagination, data);
    }

    @Override
    public List<Long> getChangeAcctListByTimestamp(Timestamp currentdate, String tableName)
    {
        List<Long> acctList = new ArrayList<Long>();
        acctList = realDealVCoinMoneyMapper.getChangeAcctListByTimestamp(currentdate,tableName);

        return acctList;
    }
}

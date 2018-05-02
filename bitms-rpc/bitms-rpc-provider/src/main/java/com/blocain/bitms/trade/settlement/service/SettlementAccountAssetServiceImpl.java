/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.settlement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.settlement.entity.SettlementAccountAsset;
import com.blocain.bitms.trade.settlement.mapper.SettlementAccountAssetMapper;


/**
 * 交割结算账户资产表 服务实现类
 * <p>File：SettlementAccountAssetServiceImpl.java </p>
 * <p>Title: SettlementAccountAssetServiceImpl </p>
 * <p>Description:SettlementAccountAssetServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class SettlementAccountAssetServiceImpl extends GenericServiceImpl<SettlementAccountAsset> implements SettlementAccountAssetService
{

    protected SettlementAccountAssetMapper settlementAccountAssetMapper;

    @Autowired
    public SettlementAccountAssetServiceImpl(SettlementAccountAssetMapper settlementAccountAssetMapper)
    {
        super(settlementAccountAssetMapper);
        this.settlementAccountAssetMapper = settlementAccountAssetMapper;
    }

    @Override
    public Long insertFromAsset(SettlementAccountAsset settlementAccountAsset)
    {
        return settlementAccountAssetMapper.insertFromAsset(settlementAccountAsset);
    }
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.quotation.entity.RtQuotationInfo;
import com.blocain.bitms.quotation.service.RtQuotationInfoService;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.MarketSnap;
import com.blocain.bitms.trade.fund.mapper.MarketSnapMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 行情快照表 服务实现类
 * <p>File：MarketSnapServiceImpl.java </p>
 * <p>Title: MarketSnapServiceImpl </p>
 * <p>Description:MarketSnapServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class MarketSnapServiceImpl extends GenericServiceImpl<MarketSnap> implements MarketSnapService
{
    protected MarketSnapMapper marketSnapMapper;
    
    @Autowired
    RtQuotationInfoService     rtQuotationInfoService;

    @Autowired
    public MarketSnapServiceImpl(MarketSnapMapper marketSnapMapper)
    {
        super(marketSnapMapper);
        this.marketSnapMapper = marketSnapMapper;
    }
    
    @Override
    public MarketSnap selectLastOne(Long pairStockinfoId)
    {
        return marketSnapMapper.selectLastOne(pairStockinfoId);
    }
}

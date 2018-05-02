/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.quotation.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.quotation.consts.QuotationConsts;
import com.blocain.bitms.quotation.entity.Quotation;
import com.blocain.bitms.quotation.mapper.QuotationMapper;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.ListUtils;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;

/**
 * 外部行情信息 服务实现类
 * <p>File：QuotationServiceImpl.java </p>
 * <p>Title: QuotationServiceImpl </p>
 * <p>Description:QuotationServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class QuotationServiceImpl extends GenericServiceImpl<Quotation> implements QuotationService
{
    protected QuotationMapper  quotationMapper;
    
//    public static final String CACHE_KEY = "cache|find|quotation|by|last|time|";
    public static final String CACHE_KEY = "quotation|idx|lastTime|";

    @Autowired
    public QuotationServiceImpl(QuotationMapper quotationMapper)
    {
        super(quotationMapper);
        this.quotationMapper = quotationMapper;
    }
    
    /**
     * 取最新外部指数行情价
     * @param quotationParam
     * @return
     */
    @Override
    public Quotation findQuotationByLastTime(Quotation quotationParam) throws BusinessException
    {
        // 初始化行情对象，避免空指针异常
        Quotation quotation = initAttr();
        if (null == quotationParam) return quotation;

        String quotation_key = new StringBuffer(CACHE_KEY).append(quotationParam.getStockId()).toString();
        List<Quotation> lists = (List<Quotation>) RedisUtils.getObject(quotation_key);
        if (ListUtils.isNull(lists))
        {
            lists = quotationMapper.findQuotationByLastTime(quotationParam);
            RedisUtils.putObject(quotation_key, lists, CacheConst.TWO_SECONDS_CACHE_TIME);
        }
        // 对行情精度进行处理，保留到小数点后两位
        if (CollectionUtils.isNotEmpty(lists))
        {
            Quotation quota = lists.get(0);
            quotation.setId(quota.getId());
            quotation.setChannel(quota.getChannel());
            quotation.setStockId(quota.getStockId());
            quotation.setIdxPriceAvg(quota.getIdxPriceAvg().setScale(2, BigDecimal.ROUND_HALF_UP));
            quotation.setIdxPrice(quota.getIdxPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        return quotation;
    }
    
    /***********************************
     * 初始化行情信息
     * 当外部行情库为空时，直接返回，避免空指针问题
     * @return
     */
    private Quotation initAttr()
    {
        Quotation quotation = new Quotation();
        quotation.setId(0L);
        quotation.setIdxPriceAvg(BigDecimal.ZERO);
        quotation.setIdxPrice(BigDecimal.ZERO);
        quotation.setChannel(QuotationConsts.RATES_CHANNEL_BITFINEX);
        quotation.setStockId(FundConsts.WALLET_BTC_TYPE);
        return quotation;
    }
}

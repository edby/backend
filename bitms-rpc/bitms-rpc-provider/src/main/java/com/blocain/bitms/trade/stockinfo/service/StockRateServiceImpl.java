/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.stockinfo.service;

import com.blocain.bitms.boss.common.consts.ParamConsts;
import com.blocain.bitms.boss.common.entity.SysParameter;
import com.blocain.bitms.boss.common.service.SysParameterService;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.quotation.entity.RtQuotationInfo;
import com.blocain.bitms.quotation.service.RtQuotationInfoService;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.entity.StockRate;
import com.blocain.bitms.trade.stockinfo.mapper.StockRateMapper;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 证券费率表 服务实现类
 * <p>File：StockRate.java </p>
 * <p>Title: StockRate </p>
 * <p>Description:StockRate </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class StockRateServiceImpl extends GenericServiceImpl<StockRate> implements StockRateService
{
    StockRateMapper        stockRateMapper;
    
    @Autowired
    StockInfoService       stockInfoService;
    
    @Autowired
    RtQuotationInfoService rtQuotationInfoService;
    
    @Autowired
    SysParameterService    sysParameterService;
    
    @Autowired
    public StockRateServiceImpl(StockRateMapper stockRateMapper)
    {
        super(stockRateMapper);
        this.stockRateMapper = stockRateMapper;
    }
    
    @Override
    public void fiexWithdrawFeeRateFromQuotation() throws BusinessException
    {
        StockInfo entity = new StockInfo();
        entity.setStockType(FundConsts.STOCKTYPE_PURESPOT);
        entity.setIsActive("no");// 查询所有
        List<StockInfo> list = stockInfoService.findList(entity);
        for (StockInfo stockInfo : list)
        {
            BigDecimal platPrice = BigDecimal.ZERO;
            RtQuotationInfo rtQuotationInfo = rtQuotationInfoService.queryRtQuotationInfo(stockInfo.getTradeStockinfoId(), stockInfo.getId());
            if (null != rtQuotationInfo)
            {
                platPrice = rtQuotationInfo.getPlatPrice();
            }
            if (platPrice == null)
            {
                platPrice = BigDecimal.ZERO;
            }
            SysParameter params = new SysParameter();
            params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
            params.setParameterName(ParamConsts.ERC20TOKEN_WITHDRAW_ETH_FEE);
            params = sysParameterService.getSysParameterByEntity(params);
            BigDecimal fee = BigDecimal.valueOf(Double.valueOf(params.getValue())).divide(platPrice,4,BigDecimal.ROUND_DOWN);
            logger.debug(stockInfo.getId()+" "+stockInfo.getStockCode() +" 最新成交价="+platPrice+" ETH="+params.getValue());
            // 提现手续费检查和设置
            StockRate stockRate = new StockRate();
            stockRate.setStockinfoId(stockInfo.getTradeStockinfoId());
            stockRate.setRateType(FundConsts.RATE_TYPE_WITHDRAW_FEE);
            List<StockRate> feerateList = stockRateMapper.findList(stockRate);
            if (feerateList.size() == 0)
            {
                stockRate.setId(SerialnoUtils.buildPrimaryKey());
                stockRate.setRateValueType(2);
                stockRate.setRate(fee);
                stockRate.setCreateBy(FundConsts.SYSTEM_ACCOUNT_ID);
                stockRate.setCreateDate(new Timestamp(System.currentTimeMillis()));
                stockRate.setUpdateBy(FundConsts.SYSTEM_ACCOUNT_ID);
                stockRate.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                stockRateMapper.insert(stockRate);
            }
            else
            {
                stockRate = feerateList.get(0);
                stockRate.setRate(fee);
                stockRateMapper.updateByPrimaryKey(stockRate);
            }
        }
    }
}

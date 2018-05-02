/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.quotation;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.quotation.entity.RtQuotationInfo;
import com.blocain.bitms.quotation.service.RtQuotationInfoService;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 *  行情API  控制器
 * <p>File：QuotationAPIController.java</p>
 * <p>Title: QuotationAPIController</p>
 * <p>Description:QuotationAPIController</p>
 * <p>Copyright: Copyright (c) 2017年7月14日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.API)
@Api(description = "行情API")
public class QuotationAPIController extends GenericController
{
    private static final Logger    logger         = LoggerFactory.getLogger(QuotationAPIController.class);

    private static final String    opQuotationKey = new StringBuffer(CacheConst.REDIS_QUOTATION_PREFIX).append(BitmsConst.SEPARATOR).append(BitmsConst.OP_RTQUOTATIONINFO)
            .append(BitmsConst.SEPARATOR).toString();

    @Autowired(required = false)
    private RtQuotationInfoService rtQuotationInfoService;

    /**
     * 最新行情API
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/ticker", method = RequestMethod.GET)
    @ApiOperation(value = "最新行情API", httpMethod = "GET", consumes = "application/x-www-form-urlencoded")
    public JsonMessage ticker(String symbol) throws BusinessException {
        if (symbol.equals("btc2usd") || symbol.equals("bex2btc") || symbol.equals("btc2eur")) {
            if (symbol.equals("btc2usd")) {
                String quotationKey = new StringBuffer(opQuotationKey).append(TradeEnums.SYMBOL_BTC2USD.getMessage()).toString();
                RtQuotationInfo rtQuotationInfo = (RtQuotationInfo) RedisUtils.getObject(quotationKey);
                if (null == rtQuotationInfo || rtQuotationInfo.getPlatPrice().compareTo(BigDecimal.ZERO) == 0) {
                    logger.debug("★☆★☆★☆★☆★☆ [queryRtQuotationInfoFromCache]: 缓存中不存在行情，尝试从数据库获取行情");
                    rtQuotationInfo = rtQuotationInfoService.queryRtQuotationInfoFromCache(Long.valueOf(TradeEnums.SYMBOL_BTC2USD.getCode()), Long.valueOf(TradeEnums.SYMBOL_BTC2USD.getMessage()));
                }
                return getJsonMessage(CommonEnums.SUCCESS, rtQuotationInfo);
            } else if (symbol.equals("bex2btc")) {
                // symbol.equals("bex2btc")
                String quotationKey = new StringBuffer(opQuotationKey).append(TradeEnums.SYMBOL_BEX2BTC.getMessage()).toString();
                RtQuotationInfo rtQuotationInfo = (RtQuotationInfo) RedisUtils.getObject(quotationKey);
                if (null == rtQuotationInfo || rtQuotationInfo.getPlatPrice().compareTo(BigDecimal.ZERO) == 0) {
                    logger.debug("★☆★☆★☆★☆★☆ [queryRtQuotationInfoFromCache]: 缓存中不存在行情，尝试从数据库获取行情");
                    rtQuotationInfo = rtQuotationInfoService.queryRtQuotationInfoFromCache(Long.valueOf(TradeEnums.SYMBOL_BEX2BTC.getCode()), Long.valueOf(TradeEnums.SYMBOL_BEX2BTC.getMessage()));
                }
                return getJsonMessage(CommonEnums.SUCCESS, rtQuotationInfo);
            } else {
                // symbol.equals("btc2eur")
                String quotationKey = new StringBuffer(opQuotationKey).append(TradeEnums.SYMBOL_BTC2EUR.getMessage()).toString();
                RtQuotationInfo rtQuotationInfo = (RtQuotationInfo) RedisUtils.getObject(quotationKey);
                if (null == rtQuotationInfo || rtQuotationInfo.getPlatPrice().compareTo(BigDecimal.ZERO) == 0) {
                    logger.debug("★☆★☆★☆★☆★☆ [queryRtQuotationInfoFromCache]: 缓存中不存在行情，尝试从数据库获取行情");
                    rtQuotationInfo = rtQuotationInfoService.queryRtQuotationInfoFromCache(Long.valueOf(TradeEnums.SYMBOL_BTC2EUR.getCode()), Long.valueOf(TradeEnums.SYMBOL_BTC2EUR.getMessage()));
                }
                return getJsonMessage(CommonEnums.SUCCESS, rtQuotationInfo);
            }
        } else {
            return getJsonMessage(CommonEnums.SUCCESS, "symbol不合法！");
        }

    }

}

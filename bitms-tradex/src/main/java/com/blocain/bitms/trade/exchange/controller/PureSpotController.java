/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.exchange.controller;

import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.model.FundChangeModel;
import com.blocain.bitms.trade.quotation.QuotationController;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 *  纯正现货交易中心  控制器
 * <p>File：PureSpotController.java</p>
 * <p>Title: PureSpotController</p>
 * <p>Description:PureSpotController</p>
 * <p>Copyright: Copyright (c) 2018-03-28</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.SOPT)
@Api(description = "纯正现货交易中心")
public class PureSpotController extends QuotationController
{
    @Autowired(required = false)
    StockInfoService stockInfoService;
    
    /**
     * 获取账户资金资产等信息
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/pureSpotTrade/getAccountFundAsset", method = RequestMethod.GET)
    @ApiOperation(value = "获取账户资金资产等信息（共用）", httpMethod = "GET")
    public JsonMessage getAccountFundAsset(String pair) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfo.setRemark(pair);
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
        if (stockInfoList.size() == 0) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        stockInfo = stockInfoList.get(0);
        boolean isVCoin = (stockInfo.getTradeStockinfoId().longValue() != stockInfo.getId());
        Long exchangePairVCoin = (isVCoin ? stockInfo.getTradeStockinfoId() : stockInfo.getCapitalStockinfoId());
        FundChangeModel accountFundAsset = getAccountAsset(principal.getId(), exchangePairVCoin, stockInfo.getId());
        return getJsonMessage(CommonEnums.SUCCESS, accountFundAsset);
    }
}

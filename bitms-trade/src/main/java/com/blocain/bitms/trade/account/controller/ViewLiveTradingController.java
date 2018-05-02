/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.controller;

import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.quotation.QuotationController;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 *  ViewLiveTrading  控制器
 * <p>File：ViewLiveTradingController.java</p>
 * <p>Title: ViewLiveTradingController</p>
 * <p>Description:ViewLiveTradingController</p>
 * <p>Copyright: Copyright (c) 2017年7月14日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@Api(description = "ViewLiveTrading")
public class ViewLiveTradingController extends QuotationController
{
    @Autowired(required = false)
    StockInfoService stockInfoService;
    
    @Autowired(required = false)
    AccountService   accountService;
    
    /**
     * viewLiveTrading页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/viewLiveTrading")
    @ApiOperation(value = "viewLiveTrading页面导航")
    public ModelAndView viewLiveTrading(Long exchangePairMoney) throws BusinessException
    {
        if (SecurityUtils.getSubject().isAuthenticated())
        {// 如果登陆过就直接进入后台
            return new ModelAndView("redirect:/dispatch");
        }

        // 未登录
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfo.setId(exchangePairMoney);
        stockInfo.setStockType(FundConsts.STOCKTYPE_LEVERAGEDSPOT);
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
        if (stockInfoList.size() == 0)
        {
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        stockInfo = stockInfoList.get(0);
        boolean isVCoin = (stockInfo.getTradeStockinfoId().longValue() != stockInfo.getId());
        Long exchangePairVCoin = (isVCoin ? stockInfo.getTradeStockinfoId() : stockInfo.getCapitalStockinfoId());

        StockInfo stockInfoSelect = new StockInfo();
        stockInfoSelect.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfoSelect.setStockType(FundConsts.STOCKTYPE_LEVERAGEDSPOT);
        List<StockInfo> stockInfoListSelect = stockInfoService.findList(stockInfoSelect);
        ModelAndView mav = new ModelAndView("spot/leveragedSpotTradeNoLogin");
        mav.addObject("exchangePairMoney", exchangePairMoney);
        mav.addObject("exchangePairVCoin", exchangePairVCoin);
        mav.addObject("money", stockInfo);
        mav.addObject("vcoin", stockInfoService.selectByPrimaryKey(exchangePairVCoin));
        mav.addObject("stockInfoList", stockInfoListSelect);
        mav.addObject("isVCoin", isVCoin);
        return mav;
    }
}

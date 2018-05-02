/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.spot.controller;

import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.account.service.AccountService;
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
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 *  现货合约交易中心  控制器
 * <p>File：SpotContractController.java</p>
 * <p>Title: SpotContractController</p>
 * <p>Description:SpotContractController</p>
 * <p>Copyright: Copyright (c) 2017年7月14日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
//@Controller
@RequestMapping(BitmsConst.SOPT)
@Api(description = "合约交易中心")
public class SpotContractController extends QuotationController
{
    @Autowired(required = false)
    StockInfoService stockInfoService;
    
    @Autowired(required = false)
    AccountService   accountService;
    
    /**
     * 现货合约交易中心页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/spotContractTrade")
    @ApiOperation(value = "现货合约交易中心页面导航")
    public ModelAndView spotContractTrade(String exchangePair) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfo.setRemark(exchangePair);
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
        if (stockInfoList.size() == 0) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        stockInfo = stockInfoList.get(0);
        boolean isVCoin = (stockInfo.getTradeStockinfoId().longValue() != stockInfo.getId());
        Long exchangePairVCoin = (isVCoin ? stockInfo.getTradeStockinfoId() : stockInfo.getCapitalStockinfoId());
        StockInfo stockInfoSelect = new StockInfo();
        stockInfoSelect.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfoSelect.setStockType(FundConsts.STOCKTYPE_CONTRACTSPOT);
        List<StockInfo> stockInfoListSelect = stockInfoService.findList(stockInfoSelect);
        ModelAndView mav = new ModelAndView("spot/spotContractTrade");
        mav.addObject("exchangePairMoney", stockInfo.getId());
        mav.addObject("exchangePairVCoin", exchangePairVCoin);
        mav.addObject("money", stockInfo);
        mav.addObject("vcoin", stockInfoService.selectByPrimaryKey(exchangePairVCoin));
        mav.addObject("stockInfoList", stockInfoListSelect);
        mav.addObject("isVCoin", isVCoin);
        mav.addObject("autoBorrow", accountService.selectByPrimaryKey(principal.getId()).getAutoDebit());
        mav.addObject("accountFundAsset", getAccountAsset(principal.getId(), exchangePairVCoin, stockInfo.getId()));
        return mav;
    }
    
    /**
     * 获取账户资金资产等信息
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/spotContractTrade/getAccountFundAsset", method = RequestMethod.GET)
    @ApiOperation(value = "获取账户资金资产等信息", httpMethod = "GET")
    public JsonMessage getAccountFundAsset(Long exchangePairMoney) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfo.setId(exchangePairMoney);
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
        if (stockInfoList.size() == 0) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        stockInfo = stockInfoList.get(0);
        boolean isVCoin = (stockInfo.getTradeStockinfoId().longValue() != stockInfo.getId());
        Long exchangePairVCoin = (isVCoin ? stockInfo.getTradeStockinfoId() : stockInfo.getCapitalStockinfoId());
        FundChangeModel accountFundAsset = getAccountAsset(principal.getId(), exchangePairVCoin, exchangePairMoney);
        return getJsonMessage(CommonEnums.SUCCESS, accountFundAsset);
    }
    
    /**
     * 行情中心页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/trade/quotation", method = RequestMethod.GET)
    @ApiOperation(value = "合约交易中心页面导航", httpMethod = "GET")
    public ModelAndView quotation() throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        ModelAndView mav = new ModelAndView("spot/quotation");
        return mav;
    }
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.settlement.controller;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.DateUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountFundCurrent;
import com.blocain.bitms.trade.fund.service.AccountFundCurrentService;
import com.blocain.bitms.trade.settlement.entity.SettlementRecord;
import com.blocain.bitms.trade.settlement.service.SettlementRecordService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.trade.entity.EntrustVCoinMoney;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import com.blocain.bitms.trade.trade.service.EntrustVCoinMoneyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

/**
 *  现货交割结算  控制器
 * <p>File：SettlementController.java</p>
 * <p>Title: SettlementController</p>
 * <p>Description:SettlementController</p>
 * <p>Copyright: Copyright (c) 2017年7月14日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
//@Controller
@RequestMapping(BitmsConst.SETTLEMENT)
@Api(description = "现货交割结算")
public class SpotSettlementController extends GenericController
{
    @Autowired(required = false)
    private EntrustVCoinMoneyService  entrustVCoinMoneyService;
    
    @Autowired(required = false)
    private AccountFundCurrentService accountFundCurrentService;
    
    @Autowired(required = false)
    private SettlementRecordService   settlementRecordService;

    @Autowired(required = false)
    private StockInfoService          stockInfoService;
    
    /**
     * 现货爆仓委托记录页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/explosionEntrust", method = RequestMethod.GET)
    @ApiOperation(value = "现货爆仓委托记录页面导航", httpMethod = "GET")
    public ModelAndView explosionEntrust() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("settlement/spotExplosionEntrust");
        StockInfo entity = new StockInfo();
        entity.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        entity.setStockType(FundConsts.STOCKTYPE_CONTRACTSPOT);
        List<StockInfo> list = stockInfoService.findList(entity);
        mav.addObject("stockinfos",list);
        return mav;
    }
    
    /**
     * 现货爆仓委托记录
     * @param entity
     * @param pagin
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/explosionEntrust/data", method = RequestMethod.POST)
    @ApiOperation(value = "现货爆仓委托记录", httpMethod = "POST", consumes = "application/x-www-form-urlencoded")
    public JsonMessage explosionEntrustData(@ModelAttribute EntrustVCoinMoney entity, String timeStart, String timeEnd,Long exchangePairVCoin,Long exchangePairMoney, @ModelAttribute Pagination pagin) throws BusinessException
    {
        if (!com.blocain.bitms.tools.utils.StringUtils.isBlank(timeStart))
        {
            entity.setTimeStart(timeStart + " 00:00:00");
        }
        if (!com.blocain.bitms.tools.utils.StringUtils.isBlank(timeEnd))
        {
            entity.setTimeEnd(timeEnd + " 23:59:59");
        }
        if(StringUtils.equals(timeEnd, DateUtils.formatDate(new Date(),"yyyy-MM-dd"))
                && StringUtils.equals(timeStart, DateUtils.formatDate(new Date(),"yyyy-MM-dd")))
        {
            entity.setTableName(getStockInfo(exchangePairMoney).getTableEntrust());
        }
        else
        {
            entity.setTableName(getStockInfo(exchangePairMoney).getTableEntrust());
        }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        entity.setAccountId(principal.getId());// 个人数据
        entity.setEntrustStockinfoId(exchangePairVCoin);
        entity.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());
        PaginateResult<EntrustVCoinMoney> result = entrustVCoinMoneyService.findAdminEnturstList(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    /**
     * 现货分摊基金页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/spotAllocationFund", method = RequestMethod.GET)
    @ApiOperation(value = "现货分摊基金页面导航", httpMethod = "GET")
    public ModelAndView spotAllocationFund() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("settlement/spotAllocationFund");
        StockInfo entity = new StockInfo();
        entity.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        entity.setStockType(FundConsts.STOCKTYPE_CONTRACTSPOT);
        List<StockInfo> list = stockInfoService.findList(entity);
        mav.addObject("stockinfos",list);
        return mav;
    }
    
    /**
     * 现货分摊基金记录
     * @param entity
     * @param pagin
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/spotAllocationFund/data", method = RequestMethod.POST)
    @ApiOperation(value = "现货分摊基金记录", httpMethod = "POST", consumes = "application/x-www-form-urlencoded")
    public JsonMessage spotAllocationFundData(@ModelAttribute AccountFundCurrent entity,  @ModelAttribute Pagination pagin)
            throws BusinessException
    {
        entity.setAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_RESERVE_ALLOCATION_ID);
        entity.setTableName(getStockInfo(entity.getRelatedStockinfoId()).getTableFundCurrent());
        entity.setRelatedStockinfoId(null);
        PaginateResult<AccountFundCurrent> result = accountFundCurrentService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 现货交割结算页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/spotSettlement", method = RequestMethod.GET)
    @ApiOperation(value = "现货交割结算页面导航", httpMethod = "GET")
    public ModelAndView spotSettlement() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("settlement/spotSettlement");
        StockInfo entity = new StockInfo();
        entity.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        entity.setStockType(FundConsts.STOCKTYPE_CONTRACTSPOT);
        List<StockInfo> list = stockInfoService.findList(entity);
        mav.addObject("stockinfos",list);
        return mav;
    }

    /**
     * 现货交割结算
     * @param entity
     * @param pagin
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/spotSettlement/data", method = RequestMethod.POST)
    @ApiOperation(value = "现货交割结算记录", httpMethod = "POST", consumes = "application/x-www-form-urlencoded")
    public JsonMessage settlementRecordData(@ModelAttribute SettlementRecord entity,  @ModelAttribute Pagination pagin)
            throws BusinessException
    {
        entity.setSettlementType(1);//交割记录
        PaginateResult<SettlementRecord> result = settlementRecordService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    public  StockInfo  getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
}

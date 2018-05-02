/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.spot.controller;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.trade.entity.EntrustVCoinMoney;
import com.blocain.bitms.trade.trade.entity.RealDealVCoinMoney;
import com.blocain.bitms.trade.trade.service.EntrustVCoinMoneyService;
import com.blocain.bitms.trade.trade.service.RealDealVCoinMoneyService;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *  历史现货委托记录  控制器
 * <p>File：HistoryEntrustController.java</p>
 * <p>Title: HistoryEntrustController</p>
 * <p>Description:HistoryEntrustController</p>
 * <p>Copyright: Copyright (c) 2017年7月14日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.SOPT)
@Api(description = "历史现货委托记录")
public class HistoryEntrustController extends GenericController
{
    @Autowired(required = false)
    private EntrustVCoinMoneyService  entrustVCoinMoneyService;

    @Autowired(required = false)
    private RealDealVCoinMoneyService realDealVCoinMoneyService;

    @Autowired(required = false)
    private StockInfoService          stockInfoService;

    /**
     * 历史现货委托记录页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/historyEntrust", method = RequestMethod.GET)
    @ApiOperation(value = "历史现货委托记录页面导航", httpMethod = "GET")
    public ModelAndView historyEntrust() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("spot/historyEntrust");
        StockInfo entity = new StockInfo();
        entity.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        entity.setIsActive("yes");
        List<StockInfo> list = stockInfoService.findList(entity);
        mav.addObject("stockinfos",list);
        return mav;
    }

    /**
     * 历史现货委托列表
     * @param entity
     * @param pagin
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/entrustData", method = RequestMethod.POST)
    @ApiOperation(value = "历史现货委托列表", httpMethod = "POST", consumes = "application/x-www-form-urlencoded")
    public JsonMessage entrustxData(String isHis,@ModelAttribute EntrustVCoinMoney entity, String timeStart, String timeEnd, @ModelAttribute Pagination pagin,Long exchangePairVCoin,Long exchangePairMoney) throws BusinessException
    {
        boolean isHisValue=StringUtils.equalsIgnoreCase(isHis,"yes");
        if (!StringUtils.isBlank(timeStart))
        {
            entity.setTimeStart(timeStart+" 00:00:00");
        }
        if (!StringUtils.isBlank(timeEnd))
        {
            entity.setTimeEnd(timeEnd+" 23:59:59");
        }
        entity.setTableName(isHisValue?getStockInfo(exchangePairMoney).getTableEntrustHis():getStockInfo(exchangePairMoney).getTableEntrust());
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        entity.setAccountId(principal.getId());// 个人数据
        PaginateResult<EntrustVCoinMoney> result = entrustVCoinMoneyService.getAccountHistoryEntrustVCoinMoneyList(pagin,entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    /**
     * 历史现货委托成交明细
     * @param entity
     * @param id
     * @param pagin
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/realDealByEntrustId", method = RequestMethod.POST)
    @ApiOperation(value = "历史现货委托成交明细", httpMethod = "POST", consumes = "application/x-www-form-urlencoded")
    public JsonMessage realDealByEntrustId(@ModelAttribute RealDealVCoinMoney entity, long id, @ModelAttribute Pagination pagin,Long exchangePairVCoin,Long exchangePairMoney,String tableName) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        String entrustTable = getStockInfo(exchangePairMoney).getTableEntrust();
        if(StringUtils.contains(tableName,"His"))
        {
            entrustTable = getStockInfo(exchangePairMoney).getTableEntrustHis();
        }
        EntrustVCoinMoney entrustVCoinMoney = entrustVCoinMoneyService.selectByPrimaryKey(entrustTable, id);
        if(entrustVCoinMoney.getAccountId().longValue()!=principal.getId().longValue())
        {
            throw new BusinessException("Illegal operation");
        }
        else
        {
            boolean isHis = false;
            if(StringUtils.contains(tableName,"His"))
            {
                isHis=true;
            }
            entity.setEntrustId(id);
            entity.setTableName(isHis?getStockInfo(exchangePairMoney).getTableRealDealHis():getStockInfo(exchangePairMoney).getTableRealDeal());
            entity.setEnturstTableName(isHis?getStockInfo(exchangePairMoney).getTableEntrustHis():getStockInfo(exchangePairMoney).getTableEntrust());
            PaginateResult<RealDealVCoinMoney> result = realDealVCoinMoneyService.findRealDealListByEntrustId(pagin,entity);
            for(RealDealVCoinMoney realDealVCoinMoney:result.getList())
            {
                realDealVCoinMoney.setEntrustDirect(entrustVCoinMoney.getEntrustDirect());
            }
            return getJsonMessage(CommonEnums.SUCCESS, result);
        }
    }

    public  StockInfo  getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
}

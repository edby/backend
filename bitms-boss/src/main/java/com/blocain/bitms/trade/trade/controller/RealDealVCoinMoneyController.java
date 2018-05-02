/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.trade.controller;

import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.trade.entity.RealDealVCoinMoney;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import com.blocain.bitms.trade.trade.service.RealDealVCoinMoneyService;
import io.swagger.annotations.ApiOperation;

/**
 * 账户成交查询
 * <p>File：RealDealXController.java</p>
 * <p>Title: RealDealXController</p>
 * <p>Description:RealDealXController</p>
 * <p>Copyright: Copyright (c) 2017年11月1日15:06:35</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.REALDEAL)
public class RealDealVCoinMoneyController extends GenericController
{
    @Autowired(required = false)
    private RealDealVCoinMoneyService realDealVCoinMoneyService;

    @Autowired(required = false)
    private StockInfoService          stockInfoService;

    /**
     * 账户成交查询页面导航
     * @return
     */
    @RequestMapping("/matchDealList")
    @RequiresPermissions("trade:setting:matchDealList:index")
    @ApiOperation(value = "账户成交查询页面导航", httpMethod = "get")
    public ModelAndView index()
    {
        ModelAndView mav = new ModelAndView("trade/trade/realdeal/matchDealList");
        return mav;
    }

    /**
     * 查询 账户成交查询
     * @param entity
     * @param pagin
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/matchDealList/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:matchDealList:data")
    public JsonMessage data(RealDealVCoinMoney entity,String timeStart, String timeEnd, Pagination pagin) throws BusinessException
    {
        if(StringUtils.contains(entity.getTableName(),"His"))
        {
            entity.setTableName(getStockInfo(entity.getDealStockinfoId()).getTableRealDealHis());
        }
        else
        {
            entity.setTableName(getStockInfo(entity.getDealStockinfoId()).getTableRealDeal());
        }
        entity.setDealStockinfoId(null);//只用做传参用  不用做查询
        if (StringUtils.isNotBlank(timeStart))
        {
            entity.setTimeStart(timeStart);
        }
        if (StringUtils.isNotBlank(timeEnd))
        {
            entity.setTimeEnd(timeEnd);
        }
        PaginateResult<RealDealVCoinMoney> result = realDealVCoinMoneyService.search(pagin,entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }

}

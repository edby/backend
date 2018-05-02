/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.common.controller;

import java.util.List;

import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;

/**
 * 证券信息 控制器
 * <p>File：StockInfoController.java</p>
 * <p>Title: StockInfoController</p>
 * <p>Description:StockInfoController</p>
 * <p>Copyright: Copyright (c) 2017年7月20日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.COMMON)
public class StockInfoController extends GenericController
{
    @Autowired(required = false)
    private StockInfoService stockInfoService;

    /**
     * 查询所有数字货币
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/stockinfo/allCoin", method = RequestMethod.GET)
    public List<StockInfo> allDigitalCoin() throws BusinessException
    {
        List<StockInfo> stockInfoList = stockInfoService.findListByTypes(FundConsts.STOCKTYPE_DIGITALCOIN,FundConsts.STOCKTYPE_CASHCOIN,FundConsts.STOCKTYPE_ERC20_TOKEN);
        return stockInfoList;
    }

    /**
     * 查询所有交易对
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/stockinfo/isExchange", method = RequestMethod.GET)
    public List<StockInfo> isExchange() throws BusinessException
    {
        StockInfo stockInfoSelect = new StockInfo();
        stockInfoSelect.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfoSelect.setIsActive("yes");
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfoSelect);
        return stockInfoList;
    }

    /**
     * 查询所有可借贷的交易对
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/stockinfo/canBorrow", method = RequestMethod.GET)
    public List<StockInfo> canBorrow() throws BusinessException
    {
        StockInfo stockInfoSelect = new StockInfo();
        stockInfoSelect.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfoSelect.setCanBorrow(FundConsts.PUBLIC_STATUS_YES);
        stockInfoSelect.setIsActive("yes");
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfoSelect);
        return stockInfoList;
    }

    /**
     * 按id查询所有证券(多个id用,分割)
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/stockinfo/findByIds", method = RequestMethod.GET)
    public List<StockInfo> findByIds(String ids) throws BusinessException
    {
        List<StockInfo> list = stockInfoService.findListByIds(ids);
        return list;
    }

    /**
     * 查询所有证券
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/stockinfo/all", method = RequestMethod.GET)
    public List<StockInfo> all() throws BusinessException
    {
        List<StockInfo> stockInfoList = stockInfoService.selectAll();
        return stockInfoList;
    }
}

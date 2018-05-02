/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountWalletAsset;
import com.blocain.bitms.trade.fund.entity.AccountWealthAsset;
import com.blocain.bitms.trade.fund.model.AccountAssetModel;
import com.blocain.bitms.trade.fund.service.AccountAssetService;
import com.blocain.bitms.trade.fund.service.AccountSpotAssetService;
import com.blocain.bitms.trade.fund.service.AccountWalletAssetService;
import com.blocain.bitms.trade.fund.service.AccountWealthAssetService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Fund账户资产  控制器
 * <p>File：AccountAssetController.java</p>
 * <p>Title: AccountAssetController</p>
 * <p>Description:AccountAssetController</p>
 * <p>Copyright: Copyright (c) 2017年7月14日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
@Api(description = "Fund账户资产")
public class AccountAssetController extends GenericController
{
    @Autowired(required = false)
    private AccountAssetService       accountAssetService;
    
    @Autowired(required = false)
    private AccountWalletAssetService accountWalletAssetService;

    @Autowired(required = false)
    private AccountWealthAssetService accountWealthAssetService;

    @Autowired(required = false)
    private StockInfoService          stockInfoService;
    
    /**
     * Fund账户资产页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/accountAsset", method = RequestMethod.GET)
    @ApiOperation(value = "Fund账户资产页面导航", httpMethod = "GET")
    public ModelAndView index() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("fund/accountAsset");
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        List<StockInfo> list = stockInfoService.findList(stockInfo);
        mav.addObject("stockinfos", JSON.toJSONString(list));
        return mav;
    }

    /**
     * 当前账户钱包账户资产
     * @param entity
     * @param pagin
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/accountAsset/walletAssetData", method = RequestMethod.POST)
    @ApiOperation(value = "当前账户钱包账户资产", httpMethod = "POST")
    public JsonMessage walletAssetData(@ModelAttribute AccountWalletAsset entity, @ModelAttribute Pagination pagin) throws BusinessException
    {
        // 限定自己账户的账户资产情况
        entity.setAccountId(OnLineUserUtils.getId());
        PaginateResult<AccountWalletAsset> data = accountWalletAssetService.search(pagin, entity);
        return this.getJsonMessage(CommonEnums.SUCCESS, data);
    }

    /**
     * 当前账户理财账户资产
     * @param entity
     * @param pagin
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/accountAsset/wealthAssetData", method = RequestMethod.POST)
    @ApiOperation(value = "当前账户理财账户资产", httpMethod = "POST")
    public JsonMessage wealthAssetData(@ModelAttribute AccountWealthAsset entity, @ModelAttribute Pagination pagin) throws BusinessException
    {
        // 限定自己账户的账户资产情况
        entity.setWealthAccountId(OnLineUserUtils.getId());
        PaginateResult<AccountWealthAsset> data = accountWealthAssetService.search(pagin, entity);
        return this.getJsonMessage(CommonEnums.SUCCESS, data);
    }
    
    /**
     * 当前账户合约账户资产
     * @param entity
     * @param pagin
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/accountAsset/contractAssetData", method = RequestMethod.POST)
    @ApiOperation(value = "当前账户合约账户资产", httpMethod = "POST")
    public JsonMessage data(@ModelAttribute AccountAssetModel entity, @ModelAttribute Pagination pagin) throws BusinessException
    {
        // 限定自己账户的账户资产情况
        entity.setAccountId(OnLineUserUtils.getId());
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfo.setStockType(FundConsts.STOCKTYPE_CONTRACTSPOT);
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for(int i = 0 ; i < stockInfoList.size(); i ++)
        {
            StockInfo stockInfotemp = stockInfoList.get(i);
            Map<String,Object> obj = new HashMap<String,Object>();
            obj.put("tableAsset",stockInfotemp.getTableAsset());
            obj.put("tableDebitAsset",stockInfotemp.getTableDebitAsset());
            list.add(obj);
        }
        PaginateResult<AccountAssetModel> data = accountAssetService.findAssetList(pagin, entity,list);
        return this.getJsonMessage(CommonEnums.SUCCESS, data);
    }


    /**
     * 当前账户合约账户资产
     * @param entity
     * @param pagin
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/accountAsset/spotAssetData", method = RequestMethod.POST)
    @ApiOperation(value = "当前账户杠杆现货账户资产", httpMethod = "POST")
    public JsonMessage spotAssetdata(@ModelAttribute AccountAssetModel entity, @ModelAttribute Pagination pagin) throws BusinessException
    {
        // 限定自己账户的账户资产情况
        entity.setAccountId(OnLineUserUtils.getId());
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfo.setStockType(FundConsts.STOCKTYPE_LEVERAGEDSPOT);
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for(int i = 0 ; i < stockInfoList.size(); i ++)
        {
            StockInfo stockInfotemp = stockInfoList.get(i);
            Map<String,Object> obj = new HashMap<String,Object>();
            obj.put("tableAsset",stockInfotemp.getTableAsset());
            obj.put("tableDebitAsset",stockInfotemp.getTableDebitAsset());
            list.add(obj);
        }
        PaginateResult<AccountAssetModel> data = accountAssetService.findAssetList(pagin, entity,list);
        return this.getJsonMessage(CommonEnums.SUCCESS, data);
    }
    
    /**
     * 重置钱包账户（用完请毁掉）
     * @param key
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/accountAsset/updateWalletAsset" )
    @ApiOperation(value = "重置钱包账户（用完请毁掉）")
    public JsonMessage data(String key) throws BusinessException
    {
        if(StringUtils.equals("fuzaMIMA123@bitms.com", key)) {
            AccountWalletAsset entity=new AccountWalletAsset();
            List<AccountWalletAsset> list = accountWalletAssetService.findList(entity);
            for(AccountWalletAsset wallet:list) {
                accountWalletAssetService.updateByPrimaryKey(wallet);
                logger.debug(new StringBuffer("id=").append(wallet.getId()).append(" ").append(wallet.toString()).toString());
            }
            return this.getJsonMessage(CommonEnums.SUCCESS);
        }else {
            return this.getJsonMessage(CommonEnums.FAIL);
        }
    }
}

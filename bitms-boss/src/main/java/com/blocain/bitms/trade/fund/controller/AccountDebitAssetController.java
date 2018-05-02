/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.controller;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountDebitAsset;
import com.blocain.bitms.trade.fund.service.AccountDebitAssetService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 账户借贷资产表
 * <p>File：AccountDebitAsset.java</p>
 * <p>Title: AccountDebitAsset</p>
 * <p>Description:AccountDebitAsset</p>
 * <p>Copyright: Copyright (c) 2017年9月18日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
public class AccountDebitAssetController extends GenericController
{
    @Autowired(required = false)
    private AccountDebitAssetService accountDebitAssetService;

    @Autowired(required = false)
    private StockInfoService         stockInfoService;

    /**
     * 列表页面导航-借款记录
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/debitAsset")
    @RequiresPermissions("trade:setting:debitAsset:index")
    public String list() throws BusinessException
    {
        return "trade/fund/debitAsset/list";
    }

    /**
     * 账户借贷记录表-查询
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/debitAsset/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:debitAsset:data")
    public JsonMessage data(AccountDebitAsset entity, Pagination pagin) throws BusinessException
    {
        entity.setTableName(stockInfoService.selectByPrimaryKey(entity.getRelatedStockinfoId()).getTableDebitAsset());
        entity.setRelatedStockinfoId(stockInfoService.selectByPrimaryKey(entity.getRelatedStockinfoId()).getCapitalStockinfoId());
        PaginateResult<AccountDebitAsset> result = accountDebitAssetService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    /**
     * 账户借贷记录表
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/margin/data", method = RequestMethod.GET)
    @RequiresPermissions("trade:setting:debitMargin:data")
    public JsonMessage marginData(AccountDebitAsset entity, Pagination pagin) throws BusinessException
    {
        PaginateResult<AccountDebitAsset> result = accountDebitAssetService.findMarginList(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }


    /**
     * 借贷记录表-统计查询
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/debitAsset/debitSum")
    @RequiresPermissions("trade:setting:debitSum:index")
    public String sumList() throws BusinessException
    {
        return "trade/fund/debitAsset/sumlist";
    }

    /**
     * 借贷记录表-统计查询
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/debitAsset/debitSum/debitSumData", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:debitSum:data")
    public JsonMessage debitSumData(AccountDebitAsset entity,Pagination pagin) throws BusinessException
    {
        StockInfo searchEntity = new StockInfo();
        searchEntity.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        searchEntity.setCanBorrow(FundConsts.PUBLIC_STATUS_YES);
        List<StockInfo> stockInfoList = stockInfoService.findList(searchEntity);
        String tables[] = new String[stockInfoList.size()];
        for(int i = 0 ; i < stockInfoList.size(); i ++)
        {
            StockInfo stockInfotemp = stockInfoList.get(i);
            tables[i] = stockInfotemp.getTableDebitAsset();
        }
        entity.setTableNames(tables);
        PaginateResult<AccountDebitAsset> result = accountDebitAssetService.debitSumData(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

}

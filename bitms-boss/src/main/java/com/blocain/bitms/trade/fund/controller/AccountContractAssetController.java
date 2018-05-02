/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.controller;

import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.entity.AccountContractAsset;
import com.blocain.bitms.trade.fund.service.AccountContractAssetService;
import com.blocain.bitms.trade.trade.enums.TradeEnums;

/**
 * 账户合约资产
 * <p>File：AccountContractAssetController.java</p>
 * <p>Title: AccountContractAssetController</p>
 * <p>Description:AccountContractAssetController</p>
 * <p>Copyright: Copyright (c) 2017年10月24日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
public class AccountContractAssetController extends GenericController
{
    @Autowired(required = false)
    private AccountContractAssetService accountContractAssetService;

    @Autowired(required = false)
    private StockInfoService            stockInfoService;

    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/contractAsset")
    @RequiresPermissions("trade:setting:contractAsset:index")
    public String list() throws BusinessException
    {
        return "trade/fund/contractAsset/list";
    }
    
    /**
     * 账户合约资产-查询
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/contractAsset/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:contractAsset:data")
    public JsonMessage data(AccountContractAsset entity, Pagination pagin) throws BusinessException
    {
        entity.setTableName(getStockInfo(entity.getRelatedStockinfoId()).getTableAsset());
        entity.setTableDebitName(getStockInfo(entity.getRelatedStockinfoId()).getTableDebitAsset());
        logger.debug(" debit = "+getStockInfo(entity.getRelatedStockinfoId()).getTableDebitAsset());
        PaginateResult<AccountContractAsset> result = accountContractAssetService.selectAll(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }

}

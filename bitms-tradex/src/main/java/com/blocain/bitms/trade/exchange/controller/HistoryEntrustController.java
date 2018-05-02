/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.exchange.controller;

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
@RequestMapping(BitmsConst.EXCHANGE)
@Api(description = "历史现货委托记录")
public class HistoryEntrustController extends GenericController
{
    @Autowired(required = false)
    private EntrustVCoinMoneyService entrustVCoinMoneyService;
    
    @Autowired(required = false)
    private StockInfoService         stockInfoService;
    
    /**
     * 历史现货委托列表
     * @param entity
     * @param pagin
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/entrustData", method = RequestMethod.GET)
    @ApiOperation(value = "历史现货委托列表", httpMethod = "GET", consumes = "application/x-www-form-urlencoded")
    public JsonMessage entrustxData(String isHis, @ModelAttribute EntrustVCoinMoney entity, String timeStart, String timeEnd, @ModelAttribute Pagination pagin, String contractAddr) throws BusinessException
    {
        boolean isHisValue = StringUtils.equalsIgnoreCase(isHis, "yes");
        if (!StringUtils.isBlank(timeStart))
        {
            entity.setTimeStart(timeStart + " 00:00:00");
        }
        if (!StringUtils.isBlank(timeEnd))
        {
            entity.setTimeEnd(timeEnd + " 23:59:59");
        }
        if(StringUtils.isNotBlank(contractAddr))
        {
            if(StringUtils.equalsIgnoreCase(contractAddr,"eth"))
            {
                contractAddr = FundConsts.WALLET_VINPAIR_CONTRACTADDR;
            }
            StockInfo stockInfo = getStockInfo(contractAddr);
            if(StringUtils.equalsIgnoreCase(contractAddr,"eth"))
            {
                entity.setEntrustStockinfoId(stockInfo.getCapitalStockinfoId());
                entity.setEntrustRelatedStockinfoId(stockInfo.getId());
            }
            else
            {
                entity.setEntrustStockinfoId(stockInfo.getTradeStockinfoId());
                entity.setEntrustRelatedStockinfoId(stockInfo.getId());
            }
        }
        else
        {
            contractAddr = FundConsts.WALLET_VINPAIR_CONTRACTADDR;
        }
        entity.setTableName(isHisValue ? getStockInfo(contractAddr).getTableEntrustHis() : getStockInfo(contractAddr).getTableEntrust());
        entity.setEntrustRelatedStockinfoId(entity.getId());
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        entity.setAccountId(principal.getId());// 个人数据
        PaginateResult<EntrustVCoinMoney> result = entrustVCoinMoneyService.getAccountHistoryEntrustVCoinMoneyList(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    public StockInfo getStockInfo(String addr)
    {
        StockInfo stockInfo = stockInfoService.findByContractAddr(addr);
        if(!StringUtils.equalsIgnoreCase(stockInfo.getIsActive(), FundConsts.PUBLIC_STATUS_YES))
        {
            throw new BusinessException("pair not open");
        }
        return stockInfo;
    }
}

/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.trade.controller;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.trade.entity.EntrustVCoinMoney;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import com.blocain.bitms.trade.trade.service.EntrustVCoinMoneyService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  委托表控制器
 * <p>File：EntrustController.java </p>
 * <p>Title:EntrustController </p>
 * <p>Description:EntrustController </p>
 * <p>Copyright: Copyright (c) 2017.11.01</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.ENTRUST)
public class EntrustController extends GenericController
{
    @Autowired(required = false)
    private EntrustVCoinMoneyService entrustVCoinMoneyService;

    @Autowired(required = false)
    private StockInfoService         stockInfoService;
    /**
     * 列表页面导航-综合查询[账户委托查询]
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/matchEntrustList")
    @RequiresPermissions("trade:setting:accountEntrustXSearch:index")
    public String matchEntrustList() throws BusinessException
    {
        return "trade/trade/entrust/matchEntrustList";
    }
    
    /**
     * 查询账户委托表-查询
     * @param entrustVCoinMoney
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/matchEntrustList/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:accountEntrustXSearch:data")
    public JsonMessage matchEntrustListData(EntrustVCoinMoney entrustVCoinMoney, String timeStart, String timeEnd, Pagination pagin) throws BusinessException
    {
        String table=getStockInfo(entrustVCoinMoney.getEntrustRelatedStockinfoId()).getTableEntrust();
        if(StringUtils.contains(entrustVCoinMoney.getTableName(),"His"))
        {
            table=getStockInfo(entrustVCoinMoney.getEntrustRelatedStockinfoId()).getTableEntrustHis();
        }
        entrustVCoinMoney.setTableName(table);
        if (StringUtils.isNotBlank(timeStart))
        {
            entrustVCoinMoney.setTimeStart(timeStart);
        }
        if (StringUtils.isNotBlank(timeEnd))
        {
            entrustVCoinMoney.setTimeEnd(timeEnd);
        }
        PaginateResult<EntrustVCoinMoney> result = entrustVCoinMoneyService.search(pagin, entrustVCoinMoney);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }

}

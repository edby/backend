/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.controller;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.fund.entity.AccountDebitAssetDetail;
import com.blocain.bitms.trade.fund.service.AccountDebitAssetDetailService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 *  Fund账户借贷资产  控制器
 * <p>File：AccountDebitAssetDetailController.java</p>
 * <p>Title: AccountDebitAssetDetailController</p>
 * <p>Description:AccountDebitAssetDetailController</p>
 * <p>Copyright: Copyright (c) 2017年7月14日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
@Api(description = "Fund账户借贷资产")
public class AccountDebitAssetController extends GenericController
{
    @Autowired(required = false)
    private AccountDebitAssetDetailService accountDebitAssetDetailService;
    
    @Autowired(required = false)
    private StockInfoService               stockInfoService;
    
    /**
     * Fund账户借贷资产页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/accountDebitAsset", method = RequestMethod.GET)
    @ApiOperation(value = "Fund账户借贷资产页面导航", httpMethod = "GET")
    public ModelAndView index() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("fund/accountDebitAsset");
        return mav;
    }
    
    /**
     * 当前账户借贷资产
     * @param entity
     * @param pagin
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/accountDebitAsset/getAccountDebitAssetData")
    @ApiOperation(value = "当前账户借贷资产")
    public JsonMessage getAccountDebitAssetData(String timeStart, String timeEnd, @ModelAttribute AccountDebitAssetDetail entity, @ModelAttribute Pagination pagin) throws BusinessException
    {
        // 限定自己账户的账户借贷资产情况
        entity.setBorrowerAccountId(OnLineUserUtils.getId());
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(entity.getRelatedStockinfoId());
        entity.setTableName(stockInfo.getTableDebitAssetDetail());
        entity.setRelatedStockinfoId(null);
        if (!StringUtils.isBlank(timeStart))
        {
            entity.setTimeStart(timeStart + " 00:00:00");
        }
        if (!StringUtils.isBlank(timeEnd))
        {
            entity.setTimeEnd(timeEnd + " 23:59:59");
        }
        PaginateResult<AccountDebitAssetDetail> data = accountDebitAssetDetailService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, data);
    }
}

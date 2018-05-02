/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.controller;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.payment.eth.Erc20TokenLocalService;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountCandyRecord;
import com.blocain.bitms.trade.fund.entity.AccountWalletAsset;
import com.blocain.bitms.trade.fund.service.AccountCandyRecordService;
import com.blocain.bitms.trade.fund.service.AccountWalletAssetService;
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

import java.math.BigDecimal;
import java.util.List;

/**
 * biexCandy 控制器
 * <p>File：BiexCandyController.java</p>
 * <p>Title: BiexCandyController</p>
 * <p>Description:BiexCandyController</p>
 * <p>Copyright: Copyright (c) 2018年3月10日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
@Api(description = "biexCandy")
public class BiexCandyController extends GenericController
{
    @Autowired(required = false)
    AccountCandyRecordService accountCandyRecordService;
    
    @Autowired(required = false)
    Erc20TokenLocalService    erc20TokenLocalService;
    
    @Autowired(required = false)
    StockInfoService          stockInfoService;
    
    @Autowired(required = false)
    AccountWalletAssetService accountWalletAssetService;
    
    /**
     * biexCandy页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/biexCandy", method = RequestMethod.GET)
    @ApiOperation(value = "biexCandy", httpMethod = "GET")
    public ModelAndView tokenPie() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("fund/biexCandy");
        AccountWalletAsset entity = new AccountWalletAsset();
        entity.setStockinfoId(FundConsts.WALLET_BIEX_TYPE);
        entity.setAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
        BigDecimal hasAmt = BigDecimal.ZERO;
        List<AccountWalletAsset> list = accountWalletAssetService.findList(entity);
        if(list.size()>0)
        {
            AccountWalletAsset accountWalletAsset = list.get(0);
            hasAmt = accountWalletAsset.getAmount();
        }

        BigDecimal orgAmt = BigDecimal.valueOf(5000000000L);//原始创建数量
        BigDecimal lastAmt = hasAmt;// 剩余未奖励出去的数量
        BigDecimal burnAmt = BigDecimal.ZERO; // 燃烧掉的数量
        BigDecimal presentAmt = BigDecimal.ZERO; // 奖励出去的数量
        BigDecimal totalSupply = BigDecimal.ZERO; // 当前发行的数量
        AccountCandyRecord record = accountCandyRecordService.findLastRecord(FundConsts.WALLET_BIEX_TYPE);
        if (null != record)
        {
            lastAmt = record.getLastAmt();
        }
        else
        {
            lastAmt = hasAmt;
        }
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(FundConsts.WALLET_BIEX_TYPE);
        try
        {
            totalSupply = erc20TokenLocalService.erc20_totalSupply(stockInfo.getTokenContactAddr());
            burnAmt = orgAmt.subtract(totalSupply);
        }
        catch (Exception e)
        {
            logger.debug(e.getLocalizedMessage());
        }
        presentAmt = orgAmt.subtract(lastAmt).subtract(burnAmt);
        mav.addObject("lastAmt", lastAmt);// 剩余数量
        mav.addObject("orgAmt", orgAmt);// 供应总量
        mav.addObject("presentAmt", presentAmt);
        mav.addObject("totalSupply", totalSupply);// 现有发行量
        mav.addObject("burnAmt", burnAmt);// 已销毁
        return mav;
    }
    
    /**
     * Fund账户借贷资产页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/candyRecord", method = RequestMethod.GET)
    @ApiOperation(value = "candyRecord", httpMethod = "GET")
    public ModelAndView candyRecord() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("fund/candyRecord");
        return mav;
    }
    
    @ResponseBody
    @RequestMapping(value = "/biexCandy/biexCandyData")
    @ApiOperation(value = "获得糖果记录")
    public JsonMessage getBiexCandyData(String timeStart, String timeEnd, @ModelAttribute Pagination pagin) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        AccountCandyRecord accountCandyRecord = new AccountCandyRecord();
        accountCandyRecord.setAccountId(principal.getId());
        if (!StringUtils.isBlank(timeStart))
        {
            accountCandyRecord.setTimeStart(timeStart + " 00:00:00");
        }
        if (!StringUtils.isBlank(timeEnd))
        {
            accountCandyRecord.setTimeEnd(timeEnd + " 23:59:59");
        }
        PaginateResult<AccountCandyRecord> data = accountCandyRecordService.search(pagin, accountCandyRecord);
        return getJsonMessage(CommonEnums.SUCCESS, data);
    }
}
